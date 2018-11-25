var MongoClient = require('mongodb').MongoClient
  , assert = require('assert');

MongoClient.connect('mongodb://127.0.0.1:27017/', function(err, db) {
	  assert.equal(null, err);
	var dbo = db.db('cmCustomers');
	  dbo.collections(function(err, collections){
		        console.log(collections);
		    });
});
