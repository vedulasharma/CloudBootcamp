'use strict';

//Lets require/import the HTTP module
const express = require('express');
const bodyParser = require('body-parser');
const redis = require('redis');
const assert = require('assert');

const app = express();
const dummyStoreNumberForDefaultSettings = '9999';
const jsonParser = bodyParser.json();

// example store settings format

// {
// 	"receivingUrl": "https://receivingui-ad.apps-np.homedepot.com",
// 	“features”:
// 	{
// 	"scanFeature": true,
// 	"auditRDCFeature": true
// 	}
// }


// parsing rediscloud credentials
function setupRedisClient() {
	let client = null;
	let vcap_services = process.env.VCAP_SERVICES;
	console.log('vcap_services', vcap_services);
	if (vcap_services !== undefined & vcap_services !== null) {
//		let rediscloud_service = JSON.parse(vcap_services)["p-redis"][0];
        let rediscloud_service = JSON.parse(vcap_services)["rediscloud"][0];
		console.log('rediscloud_service', rediscloud_service);
		let credentials = rediscloud_service.credentials;
		console.log('credentials', credentials);
		client = redis.createClient(credentials.port, credentials.host, { no_ready_check: true });
		client.auth(credentials.password);
	}
	else {
		client = redis.createClient();
	}
	
	return client;
}

function configureClientErrorHandler(client) {
	client.on('error', function(err) {
		assert(err instanceof Error);
		assert.strictEqual(err.code, 'ECONNRESET');
		console.log('Error with redis connection, ', err);
	});
}

const client = setupRedisClient();
configureClientErrorHandler(client);

app.get('/reroute/:storeNumber', (request, response) => {
	let storeNumber = request.params.storeNumber;
	console.log("Got storeNumber : ", storeNumber)
	client.get(storeNumber, function (err, reply) {

		//console.log(err);
		//response.send('value: ' + reply)
		if (reply !== null) {
			console.log(reply);
			response.redirect(JSON.parse(reply).receivingUrl);
		}
		else {
			client.get(dummyStoreNumberForDefaultSettings, function (err, reply) {
				if (reply !== null) {
					response.redirect(JSON.parse(reply).receivingUrl);
				}
				else {
					response.send("{\"error\":\"Store and Default not found in Redis\"}");
				}

			});

		}

	});

})

app.get('/store-settings/:storeNumber', (request, response) => {
	let storeNumber = request.params.storeNumber;
	console.log("Got key : ", storeNumber)
	client.get(storeNumber, function (err, reply) {


		//console.log(err);
		//response.send('value: ' + reply)
		if (reply !== null) {
			//store found 
			let storeSettings = JSON.parse(reply);
			response.send(storeSettings);
		} else {
			client.get(dummyStoreNumberForDefaultSettings, function (err, reply) {
				if (reply !== null) {
					let defaultStoreSettings = JSON.parse(reply);
					response.send(defaultStoreSettings);
				}
				else {
					response.send("{\"error\":\"Store and Default not found in Redis\"}");
				}

			});

		}
	});

});

app.post('/store-settings/:storeNumber', jsonParser, (request, response) => {

	let storeSettings = request.body;
	console.log("storeSettings is", storeSettings);
	let storeNumber = request.params.storeNumber;
	console.log("storeNumber is", storeNumber);

	//Inserrting route to redis
	let storeSettingsAsString = JSON.stringify(storeSettings);
	client.set(storeNumber, storeSettingsAsString);

	//Add to git repo

	response.send(storeSettings);


});


app.listen(process.env.PORT || 3000, (err) => {
	if (err) {
		return console.log('something bad happened', err)
	}

	console.log(`server is started.`)
});