const assert = require("assert");
const fs = require("fs");

let file = fs.readFileSync('cm_customers.json', 'utf8');
let data = JSON.parse(file);

let customers = [];

for (let customer of data.customers) {
  customers.push(customer.customer);
}

let output = JSON.stringify(customers, null, "  ");
fs.writeFileSync("customer_ids.json", output);