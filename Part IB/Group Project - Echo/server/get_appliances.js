const assert = require("assert");
const fs = require("fs");

let file = fs.readFileSync('cm_customers.json', 'utf8');
let data = JSON.parse(file);

let applianceTypes = new Map();

for (let customer of data.customers) {
  for (let meter of customer.meters) {
    let target = meter.observation_target;
    let applianceId = target.appliance_type_id;

    let applianceType = applianceTypes.get(applianceId);
    if (applianceType == null) {
      applianceType = {
        id: applianceId,
        name: target.name,
        count: 0
      };
      applianceTypes.set(applianceId, applianceType);
    }
    applianceType.count += 1;
  }
}

let applianceTypesArr = Array.from(applianceTypes.values()).sort((a, b) => a.id - b.id);

let output = JSON.stringify(applianceTypesArr, null, "  ");
fs.writeFileSync("appliance_types.json", output);