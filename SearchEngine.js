const mysql = require('mysql');
const express = require('express');
const bodyParser = require('body-parser');

function loadJSON(callback) 
{
    var xobj = new XMLHttpRequest();
    xobj.overrideMimeType("application/json");
    xobj.open('GET', 'bookkeeping.json', true);
    xobj.onreadystatechange = function() 
	{
        if (xobj.readyState == 4 && xobj.status == "200") 
		{
            // .open will NOT return a value but simply returns undefined in async mode so use a callback
            callback(xobj.responseText);
        }
    }
    xobj.send(null);
}

function tfScore(term, docLink)
{
	var count = 0;
	for (var i = 0; i < 1; i++)
	{
		var doc = "";
		doc = doc.replace("\\", "/");
	}
	if (count === 0)
		return 0.0;
	
	return 1.0 + Math.log(count);
}

function idfScore(term)
{
	var docCount = 37497;
	var numDocs = 1;
	return 1.0 + Math.log(docCount/numDocs);
}

function tfidfScore(terms, link_string)
{
	var total = 0.0;
	for (var i = 0; i < terms.length; i++)
	{
		total += tfScore(terms[i],link_string) * idfScore(terms[i]);
	}
	return total;
}

var connection = mysql.createConnection({
	host: '127.0.0.1',
	user: 'root',
	password: '$pin2WIN!!',
	database: 'cs121'
});

connection.connect(function(err){
	if (!err)
	{
		console.log("Database is connected.");
	}
	else
	{
		console.log("Error: Could not connect to database.");
	}
});

connection.query('SELECT document FROM Tokens WHERE token="Anuj"', function(errr, rows, fields) {
		connection.end();
		if (!err)
		{
			console.log("The query is: ", rows);
		}
		else
		{
			console.log("ERROR: Could not perform query.");
		}
});

var app = express();
app.listen(3000);