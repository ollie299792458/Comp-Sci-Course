# -*- coding: utf-8 -*-

import os
import flask
import requests
import uuid
import google.oauth2.credentials
import google_auth_oauthlib.flow
from googleapiclient.discovery import build
import urllib.parse as urllib
import config
from utils import error
import googleapiclient.discovery
import main
import json
import fetch_emails

# This variable specifies the name of a file that contains the OAuth 2.0
# information for this application, including its client_id and client_secret.
CLIENT_SECRETS_FILE = "credentials.json"

# This OAuth 2.0 access scope allows for full read/write access to the
# authenticated user's account and requires requests to use an SSL connection.
SCOPES = ['https://www.googleapis.com/auth/gmail.readonly']
API_SERVICE_NAME = 'drive'
API_VERSION = 'v2'

app = flask.Flask(__name__)
# Note: A secret key is included in the sample so that it works.
# If you use this code in your application, replace this with a truly secret
# key. See http://flask.pocoo.org/docs/0.12/quickstart/#sessions.
app.secret_key = 'REPLACE ME - this value is here as a placeholder.'


@app.route('/')
def index():
    return flask.render_template('index.html')


@app.route('/test')
def test_api_request():
    if 'credentials' not in flask.session:
        return flask.redirect('authorize')

    # Load credentials from the session.
    credentials = google.oauth2.credentials.Credentials(
        **flask.session['credentials'])

    service = build('gmail', 'v1', credentials=credentials)

    subject_keywords = ['order', 'receipt', 'booking']
    query = 'subject:' + ' OR subject:'.join(subject_keywords)
    print(query)
    response = service.users().messages().list(userId='me', q=query).execute()
    print(response)
    # Save credentials back to session in case access token was refreshed.
    # ACTION ITEM: In a production app, you likely want to save these
    #              credentials in a persistent database instead.
    flask.session['credentials'] = credentials_to_dict(credentials)

    return flask.jsonify(**response)


oauth_state = uuid.uuid4().hex


@app.route('/authorize_monzo')
def authorize_monzo():
    oauth2_GET_params = {
        "client_id": config.MONZO_CLIENT_ID,
        "redirect_uri": config.MONZO_OAUTH_REDIRECT_URI,
        "response_type": config.MONZO_RESPONSE_TYPE,
        "state": oauth_state
    }
    request_url = "https://{}/?{}".format(config.MONZO_OAUTH_HOSTNAME,
                                          urllib.urlencode(oauth2_GET_params, doseq=True))

    return flask.redirect(request_url)


@app.route('/oauth2callbackmonzo/')
def oauth2callbackmonzo():
    code = flask.request.args.get('code')

    oauth2_POST_params = {
        "grant_type": config.MONZO_AUTH_GRANT_TYPE,
        "client_id": config.MONZO_CLIENT_ID,
        "client_secret": config.MONZO_CLIENT_SECRET,
        "redirect_uri": config.MONZO_OAUTH_REDIRECT_URI,
        "code": code,
    }
    request_url = "https://{}/oauth2/token?".format(config.MONZO_API_HOSTNAME)
    response = requests.post(request_url, data=oauth2_POST_params)
    if response.status_code != 200:
        error("Auth failed, bad status code returned: {} ({})".format(response.status_code,
                                                                      response.text))

    response_object = response.json()
    if "access_token" in response_object:
        print("Auth successful, access token received.")
        access_token = response_object["access_token"]

        if "refresh_token" in response_object:
            refresh_token = response_object["refresh_token"]

    print(json.dumps(response_object))

    accounts_response = requests.get('https://api.monzo.com/accounts', headers={'Authorization': 'Bearer '+access_token})
    accounts = accounts_response.json()
    print(accounts)
    first_account_id = accounts['accounts'][0]['id']
    print(first_account_id)
    flask.session['credentialsmonzo'] = {'refresh_token': refresh_token, 'access_token': access_token, 'first_account_id': first_account_id}

    return flask.redirect('/')


@app.route('/authorize')
def authorize():
    # Create flow instance to manage the OAuth 2.0 Authorization Grant Flow steps.
    flow = google_auth_oauthlib.flow.Flow.from_client_secrets_file(
        CLIENT_SECRETS_FILE, scopes=SCOPES)

    flow.redirect_uri = flask.url_for('oauth2callback', _external=True)

    authorization_url, state = flow.authorization_url(
        # Enable offline access so that you can refresh an access token without
        # re-prompting the user for permission. Recommended for web server apps.
        access_type='offline',
        # Enable incremental authorization. Recommended as a best practice.
        include_granted_scopes='true')

    # Store the state so the callback can verify the auth server response.
    flask.session['state'] = state

    return flask.redirect(authorization_url)

@app.route('/doeverything')
def do_everything():
    if 'credentials' not in flask.session:
        return flask.redirect('authorize')

    # Load credentials from the session.
    credentials = google.oauth2.credentials.Credentials(
        **flask.session['credentials'])

    fetch_emails.main(credentials, flask.session['credentialsmonzo'])


@app.route('/oauth2callback')
def oauth2callback():
    # Specify the state when creating the flow in the callback so that it can
    # verified in the authorization server response.
    state = flask.session['state']

    flow = google_auth_oauthlib.flow.Flow.from_client_secrets_file(
        CLIENT_SECRETS_FILE, scopes=SCOPES, state=state)
    flow.redirect_uri = flask.url_for('oauth2callback', _external=True)

    # Use the authorization server's response to fetch the OAuth 2.0 tokens.
    authorization_response = flask.request.url
    flow.fetch_token(authorization_response=authorization_response)

    # Store credentials in the session.
    # ACTION ITEM: In a production app, you likely want to save these
    #              credentials in a persistent database instead.
    credentials = flow.credentials
    flask.session['credentials'] = credentials_to_dict(credentials)

    return flask.redirect('/')


@app.route('/revoke')
def revoke():
    if 'credentials' not in flask.session:
        return ('You need to <a href="/authorize">authorize</a> before ' +
                'testing the code to revoke credentials.')

    credentials = google.oauth2.credentials.Credentials(
        **flask.session['credentials'])

    revoke = requests.post('https://accounts.google.com/o/oauth2/revoke',
                           params={'token': credentials.token},
                           headers={'content-type': 'application/x-www-form-urlencoded'})

    status_code = getattr(revoke, 'status_code')
    if status_code == 200:
        return ('Credentials successfully revoked.' + print_index_table())
    else:
        return ('An error occurred.' + print_index_table())


@app.route('/clear')
def clear_credentials():
    if 'credentials' in flask.session:
        del flask.session['credentials']
        del flask.session['credentialsmonzo']
    return flask.redirect('/')


def credentials_to_dict(credentials):
    return {'token': credentials.token,
            'refresh_token': credentials.refresh_token,
            'token_uri': credentials.token_uri,
            'client_id': credentials.client_id,
            'client_secret': credentials.client_secret,
            'scopes': credentials.scopes}


def print_index_table():
    return ('<table>' +
            '<tr><td><a href="/authorize">Test the auth flow for google directly</a></td>' +
            '<td>Go directly to the authorization flow. If there are stored ' +
            '    credentials, you still might not be prompted to reauthorize ' +
            '    the application.</td></tr>' +
            '<tr><td><a href="/authorize_monzo">Test the auth flow for monzo directly</a></td>' +
            '<td>Go directly to the authorization flow. If there are stored ' +
            '    credentials, you still might not be prompted to reauthorize ' +
            '    the application.</td></tr>' +
            '<tr><td><a href="/doeverything">Add receipts to transactions</a></td>' +
            '<td>Adds reciepts to transcations</td></tr>' +
            '<tr><td><a href="/revoke">Revoke current credentials</a></td>' +
            '<td>Revoke the access token associated with the current user ' +
            '    session. After revoking credentials, if you go to the test ' +
            '    page, you should see an <code>invalid_grant</code> error.' +
            '</td></tr>' +
            '<tr><td><a href="/clear">Clear Flask session credentials</a></td>' +
            '<td>Clear the access token currently stored in the user session. ' +
            '    After clearing the token, if you <a href="/test">test the ' +
            '    API request</a> again, you should go back to the auth flow.' +
            '</td></tr></table>')


if __name__ == '__main__':
    # When running locally, disable OAuthlib's HTTPs verification.
    # ACTION ITEM for developers:
    #     When running in production *do not* leave this option enabled.
    os.environ['OAUTHLIB_INSECURE_TRANSPORT'] = '1'

    # Specify a hostname and port that are set as a valid redirect URI
    # for your API project in the Google API Console.
    app.run('localhost', 8080, debug=True)
