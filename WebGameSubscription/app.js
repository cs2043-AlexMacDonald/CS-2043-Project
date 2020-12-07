//File for web server & Database

var mysql = require('mysql');

var con = mysql.createConnection({
    host: "127.0.0.1",
    port: "3306",
    user: "root",
    password: "254moyw#" //MAKE SURE TO NOT ACTUALLY PUT YOUR INFO WHEN PUSHING CODE TO GITHUB
  });
  
  con.connect(function(err) {
    if (err) throw err;
    console.log("Connected!");
  });