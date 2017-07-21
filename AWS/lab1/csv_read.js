var util = require('util');
var AWS = require('aws-sdk');

var S = new AWS.S3({
    maxRetries: 0,
    region: 'us-east-1',
});

exports.handler = function(event, context, callback) {
    // Read options from the event.
    console.log("Reading options from event:\n", util.inspect(event, {depth: 5}));
    var srcBucket = event.Records[0].s3.bucket.name;
    var srcKey    = event.Records[0].s3.object.key;

    // don't run on anything that isn't a CSV
    if (srcKey.match(/\.csv$/) === null) {
        var msg = "Key " + srcKey + " is not a csv file, bailing out";
        console.log(msg);
        return callback(null, {message: msg});
    }
    S.getObject({
        Bucket: srcBucket,
        Key: srcKey,
    }, function (err, data) {
        if (err !== null) { return callback(err, null); }
        console.log("Raw CSV data: " + data.Body.toString('utf-8'));
        return callback(null, data);
    });
};
