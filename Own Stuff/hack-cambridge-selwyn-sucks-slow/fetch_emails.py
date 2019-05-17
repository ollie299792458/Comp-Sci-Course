from __future__ import print_function
import pickle
import os.path
from googleapiclient.discovery import build
from google_auth_oauthlib.flow import InstalledAppFlow
from google.auth.transport.requests import Request
import json
import email
import monzo
import base64
import email_scraper
# If modifying these scopes, delete the file token.pickle.
SCOPES = ['https://www.googleapis.com/auth/gmail.readonly']


def main(creds, monzo_creds):
    """Shows basic usage of the Gmail API.
    Lists the user's Gmail labels.
    """

    service = build('gmail', 'v1', credentials=creds)

    subject_keywords = ['order', 'receipt', 'booking', 'confirmation', 'invoice', 'payment', 'successful', 'debit']
    query = 'subject:' + ' OR subject:'.join(subject_keywords)
    print(query)
    response = service.users().messages().list(userId='me', q=query).execute()
    messages = []

    if 'messages' in response:
        messages.extend(response['messages'])

    while 'nextPageToken' in response:
        page_token = response['nextPageToken']
        response = service.users().messages().list(userId='me',
                                                   pageToken=page_token, q=query).execute()
        messages.extend(response['messages'])


    print("Gmail emails downloaded: " + str(len(messages)))
    transactions = monzo.get_transactions( monzo_creds['first_account_id'], monzo_creds['access_token']);
    print("Monzo transactions downloaded: " + str(len(transactions)))
    print("Munching receipts")
    count = 0;
    for message in messages:
        message = service.users().messages().get(userId='me', id=message['id'], format='raw').execute()
        msg_str = base64.urlsafe_b64decode(message['raw'])
        mime_msg = email.message_from_bytes(msg_str)
        #print("Munched: "+mime_msg['subject'])
        try:
            money, time, subject, email_link = email_scraper.scrape(message['id'], mime_msg)
            #print("Found money: " + str(money))

            if money != 1:
                if 1 == monzo.match_and_upload_receipt(money, time, subject, email_link, transactions, monzo_creds['access_token']) :
                    count = count + 1
                    print("Receipt munched successfully"+str(mime_msg['subject']))

        except:
            continue
    print("Receipts munched: "+ str(count))

if __name__ == '__main__':
    main()
