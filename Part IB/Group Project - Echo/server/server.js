#!/usr/bin/env node

const http = require("http");
const https = require("https");
const url = require("url");

const PORT = process.argv[2] || 8080;

const API_HOST = "uk-imgate.vw.informetis.com";
const API_VER = "0.1";
const AUTH = "imSP 0055:llqBRq5zr4QSDsN4FO2y";

const { MongoClient } = require('mongodb');
const assert = require('assert');

// Connection URL
const MONGO_URL = "mongodb://localhost:27017";
const MONGO_DB_NAME = "cmCustomers";

var mongoDb = null;

function connectMongo(callback) {
  MongoClient.connect(MONGO_URL, (err, client) => {
    assert.equal(null, err);
    console.log("Connected successfully to mongo server");

    mongoDb = client.db(MONGO_DB_NAME);

    // TODO might want to reconnect on failure
    mongoDb.on("close", () => { mongoDb = null; });

    let carr = mongoDb.listCollections().toArray();

    if (callback != null) {
      callback();
    }
  });
}

function handleGetCustomer(url, request, response) {
  if (mongoDb == null) {
    connectMongo(() => handleGetCustomer(url, request, response));
    return;
  }
  console.log("db: ", mongoDb);

  // Use connect method to connect to the server
  mongoDb.collection("customers", {strict: true}, (err, col) => {
    assert.equal(err, null);

    col.findOne({customer: url.query.customer}, (err, doc) => {
      assert.equal(err, null);

      response.writeHead(200, {'content-type': 'application/json; charset=utf-8'});
      response.write(JSON.stringify(doc));
      response.end();
    })
  });
}

function handleObservedData(url, request, response) {
  let options = {
    hostname: API_HOST,
    path: `/${API_VER}${url.path}`,
    headers: {
      "Authorization": AUTH
    }
  };

  let connector = https.request(options, (serverResponse) => {
    console.log("Received response for " + request.url);
    console.log("Response headers: ", serverResponse.headers);

    serverResponse.pause();

    serverResponse.headers["access-control-allow-origin"] = "*";

    response.writeHeader(serverResponse.statusCode, serverResponse.headers);
    // Pipe the response back to the client.
    serverResponse.pipe(response, { end: true });

    serverResponse.resume();
    console.log("\n\n");
  });
  connector.on("error", console.error);

  // Pipe the whole request to the API server.
  request.pipe(connector, {end:true});
}

var server = http.createServer((request, response) => {
  console.log("Received request: ", request.url);

  let parsedUrl = url.parse(request.url, true);
  console.log(parsedUrl);

  request.pause();

  // URL filed to parse so handled it manually.
  if (parsedUrl.pathname === "/observed_data") {
    handleObservedData(parsedUrl, request, response);
  } else if (parsedUrl.pathname === "/get_customer") {
    handleGetCustomer(parsedUrl, request, response);
  } else {
    console.log("Unsupported request. Ignoring.\n");
  }

  request.resume();
});

console.log("Listening on http://localhost:%s", PORT);

server.on("clientError", (err, socket) => {
  socket.end("HTTP/1.1 400 Bad Request\n\n");
});

server.on("close", () => {
  console.error("Server closing.\n\n");
})

connectMongo();

server.listen(PORT);
