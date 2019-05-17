import re
from datetime import datetime

from bs4 import BeautifulSoup



def scrape(message_id, mime_msg):
    soup = None
    if mime_msg.is_multipart():
        for p in mime_msg.get_payload():
            if p.get_content_maintype() == 'text':
                soup = BeautifulSoup(p.get_payload(decode=True), 'html.parser')
    else:
        soup = BeautifulSoup(mime_msg.get_payload(decode=True), 'html.parser')

    regex_digits = r'\s*\d+\s*\.\s*\d+\s*'
    regex = r'(?:GBP|£)('+regex_digits+')|('+regex_digits+')(?:GBP|£)'

    moniestrings = soup.find_all(string=re.compile(regex))

    max_money = None
    for s in moniestrings:
        if re.compile(regex).match(s) is None:
            continue
        money = float(list(filter(lambda x: x is not None, re.compile(regex).match(s).groups()))[0])
        money = int(money * 100)

        if max_money is None or money > max_money:
            max_money = money

    if max_money is None:
        return 1,datetime.now(),"hi","downloadmoreram.com"

    money = max_money

    time = datetime.strptime(mime_msg['date'], '%a, %d %b %Y %H:%M:%S %z')

    subject = mime_msg['subject']


    email_link = "https://mail.google.com/mail/#inbox/{}".format(message_id)

    return -money, time, subject, email_link
