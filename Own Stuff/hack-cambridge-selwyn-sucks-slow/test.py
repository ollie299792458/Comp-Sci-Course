# If modifying these scopes, delete the file token.pickle.
import base64
import email
import json
import pickle
import os.path
import re

from bs4 import BeautifulSoup
from googleapiclient.discovery import build
from google_auth_oauthlib.flow import InstalledAppFlow
from google.auth.transport.requests import Request
from flask import Flask

from email_scraper import scrape

SCOPES = ['https://www.googleapis.com/auth/gmail.readonly']


def main():
    """Shows basic usage of the Gmail API.
    Lists the user's Gmail labels.
    """
    creds = None
    # The file token.pickle stores the user's access and refresh tokens, and is
    # created automatically when the authorization flow completes for the first
    # time.
    if os.path.exists('token.pickle'):
        with open('token.pickle', 'rb') as token:
            creds = pickle.load(token)
    # If there are no (valid) credentials available, let the user log in.
    if not creds or not creds.valid:
        if creds and creds.expired and creds.refresh_token:
            creds.refresh(Request())
        else:
            flow = InstalledAppFlow.from_client_secrets_file(
                'credentials.json', SCOPES)
            creds = flow.run_local_server()
        # Save the credentials for the next run
        with open('token.pickle', 'wb') as token:
            pickle.dump(creds, token)

    service = build('gmail', 'v1', credentials=creds)

    response = service.users().messages().list(userId='me').execute()
    messages = []

    if 'messages' in response:
        messages.extend(response['messages'])

    if 'nextPageToken' in response:
        page_token = response['nextPageToken']
        response = service.users().messages().list(userId='me',
                                                   pageToken=page_token).execute()
        messages.extend(response['messages'])

    for m in messages:
        message = service.users().messages().get(userId='me', id=m['id'], format='raw').execute()
        msg_str = base64.urlsafe_b64decode(message['raw'])
        mime_msg = email.message_from_bytes(msg_str)
        if not mime_msg['subject'] is None:
            if 'order' in mime_msg['subject']:
                (money, a, b, c) = scrape(m['id'], mime_msg)
                print(mime_msg['subject']+" "+m['id']+" "+str(abs(money))+"p")


if __name__ == '__main__':
    main()