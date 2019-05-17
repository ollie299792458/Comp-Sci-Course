# Contract
# Give me a price, date, text, and email links
# I upload these if they match a debit from the monzo account (date close and price match)
# Fails in the event of multiple matches, returns false

# Download transactions around the date of the data
# Match price, if one match succeed, if multiple pick closest date, if still multiple fail
# Upload data - text & link in notes, (email screenshot as receipt?)

# pipenv install requests
import uuid

import requests
#
import json
#
from datetime import timedelta, datetime
from main import ReceiptsClient

import main
import receipt_types

def get_transactions( ACCOUNT_ID, ACCESS_TOKEN):
    r = requests.get('https://api.monzo.com/transactions?expand[]=merchant&account_id='+ACCOUNT_ID, headers={'Authorization': 'Bearer '+ACCESS_TOKEN})
    raw_transactions = json.loads(r.text)["transactions"]
    transactions = {}
    for raw_transaction in raw_transactions :
        transactions[str(raw_transaction['amount'])] = []
    for raw_transaction in raw_transactions :
        transactions[str(raw_transaction['amount'])].append(raw_transaction)
    return transactions


#price integer pennies, date is datetime, text is string, link is string
def match_and_upload_receipt(price, date, text, link, transactions, ACCESS_TOKEN):
    #get all transactions
    #http "https://api.monzo.com/transactions" \
    "Authorization: Bearer $access_token" \
    "account_id==$account_id"

    if not str(-abs(price)) in transactions:
        return -1

    candidates = list(transactions[str(-abs(price))])

    #print("candidates" + str(candidates))
    #print("firstcandidates" + str(candidates[0]))

    if len(candidates) == 0:
        print("no candidates")
        return -1


    #candidate = min(candidates, key=lambda x: (datetime.fromisoformat(x['created']) - date).total_seconds())
    candidate = candidates[0]

    before = date + timedelta(days=7)
    since = date - timedelta(days=7)

    if not (True):
        #fix the time thing
        return

    #print("found candidate: " + str(candidate))

    # Using a random receipt ID we generate as external ID
    receipt_id = uuid.uuid4().hex

    example_items = [receipt_types.Item(text+" "+link, 1, "", abs(candidate["amount"]), "GBP", 0, [])]

    example_receipt = receipt_types.Receipt("", receipt_id, candidate["id"],
                                            abs(candidate["amount"]), "GBP", "", "", example_items)
    example_receipt_marshaled = example_receipt.marshal()
    #print(date.isoformat() + " " + str(example_receipt_marshaled))
    client = requests.put("https://api.monzo.com/transaction-receipts/", data=example_receipt_marshaled, headers={'Authorization': 'Bearer '+ACCESS_TOKEN})
    return 1

# test
#match_and_upload_receipt(-1010, datetime(2019,1,2),"Testing testing 1 2 3 receipt muncher","downloadmoreram.com")