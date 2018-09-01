var MongoClient = require('mongodb').MongoClient
  , assert = require('assert');

// Connection URL
var url = 'mongodb://localhost:27017/';

// Use connect method to connect to the server
MongoClient.connect(url, function(err, db) {
    assert.equal(null, err);
    console.log("Connected successfully to server");

    var fs = require('fs');
    var customers = JSON.parse(fs.readFileSync('/home/charlie/cmdatabase/cm_customers.json', 'utf8'));

    var dbo = db.db("cmCustomers");
    dbo.createCollection("customers", function(err, res) {
	assert.equal(null, err);
    });

    customers.customers.forEach((cust) => {
//        console.log(cust);
        dbo.collection("customers").insertOne(cust, function(err, res) {
            assert.equal(null, err);
        });
    });

 //   dbo.collection("customers").find({}).toArray(function(err, result) {
 //       assert.equal(null, err);
 //       console.log(result);
 //   });
    
    db.close();
});
