/**********************************************************************************
 
    This file conatains the setup and functionalities for the server and 
    database components.

********************************************************************************** */

//-------------------------------------------------------------------------------------------------------------------
//Stuff for the server (on localhost:8080)
const express = require('express');
const server = express();
const path = require('path');
const bodyParser = require('body-parser');
const router = express.Router();
const jsdom = require('jsdom');

//Stuff for mysql
const mysql = require('mysql');
const { defaultCoreCipherList } = require('constants');

// For rendering css and images
server.use(express.static(path.join(__dirname, 'public')));

// needed for GET and POST methods to retrieve data from front-end (textfields)
server.use(express.json());
server.use(express.urlencoded({ extended: true }));

//---------------------------------------------------------------------------------------------------------------------


//Connecting to the database
const db = mysql.createConnection({
    user: "root",
    host: "localhost",
    password: "password",              
    database: "database"
});




// POST REQUESTS --------------------------------------------------------------------------------------------------------

//POST for user creation, on ADMIN PAGE
//Creation of user and insertion into the database                  
server.post("/admin", (request, response) => {

    var username = request.body.uname;
    var password = request.body.pswd;

    if(username && password){
        db.query("INSERT INTO users (username, password) VALUES(?, ?)", [username, password],
            (error, result) =>{
                if(error){
                    console.log(error);
                    //something went wrong while sending the data to the database
                    response.redirect('/admin-error');
                }
                else{
                    response.redirect('/admin-success');
                }
        });
    }
    else{
        response.redirect('/admin');
        //add something to say that they need to enter a username or password to create an account
    }
});



// POST for login info, on LOGIN PAGE
server.post('/', (request, response) => {
    //These are being grabbed from the textbox in the login page
    var username = request.body.uname;
    var password = request.body.pswd;
    if(username && password){
        db.query("SELECT * FROM users WHERE username = ? AND password = ?", [username, password],
            (error, result) =>{
                if(result.length > 0){ //meaning there is a user with this info
                    if(username != "admin"){
                        response.redirect('/home');
                    }
                    else{
                        response.redirect('/admin');
                    }
                } 
                else{
                    response.redirect('/');
                    //add something to tell the user that that username and/or password was wrong 
                }
        });
    }
    else{
        response.redirect('/');
        //add something to say that they need to enter a username or password
    }
});

//---------------------------------------------------------------------------------------------------------------------





//GET REQUESTS ---------------------------------------------------------------------------------------------------------

//GET to serve HTML pages to client
// Mapping the routes to their respective HTML files 
router.get('/', (request, response) => {
    // the __dirname automatically puts in the project folder
    response.sendFile(path.join(__dirname+'/index.html'));
});

router.get('/home', (request, response) => {
    // the __dirname automatically puts in the project folder
    response.sendFile(path.join(__dirname+'/gameList.html'));
});

router.get('/admin', (request, response) => {
    // the __dirname automatically puts in the project folder
    response.sendFile(path.join(__dirname+'/adminPage.html'));
});

router.get('/admin-success', (request, response) => {
    // the __dirname automatically puts in the project folder
    response.sendFile(path.join(__dirname+'/successfulCreation.html'));
});

router.get('/admin-error', (request, response) => {
    // the __dirname automatically puts in the project folder
    response.sendFile(path.join(__dirname+'/unsuccessfulCreation.html'));        
});

router.get('/about', (request, response) => {
    // the __dirname automatically puts in the project folder
    response.sendFile(path.join(__dirname+'/about.html'));
});

router.get('/home/AimTrainer', (request, response) => {
    // the __dirname automatically puts in the project folder
    response.sendFile(path.join(__dirname+'/AimTrainer.html'));                  
});


//ATTEMPT AT MAKING THE DYNAMIC SCOREBOARD (NOT COMPLETE)
// router.get('/home/AimTrainer', (request, response) => {
    
//     //var dom = new JSDOM("aimTrainer.html");
//     //parallel arrays
//     //var done = false;
//     var uIDArray = [];
//     var usernamesArray = [];
//     var scoreArray = [];
//     var data;
    
   
//     db.query("SELECT user_id, score FROM score WHERE game_id = 'AimTrainer' ORDER BY score ASC LIMIT 5",
//         (error, result) => {
//             if (error) {
//                 console.log(error);
//             }
//             else {
//                 Object.keys(result).forEach(function (key) {
//                     var row = result[key]; //var row = 

//                     uIDArray.push(row.user_id);
//                     scoreArray.push(row.score);
//                     console.log(row); //"ID: " + row.user_id + ", score: " + row.score);

//                     db.query("SELECT username FROM users WHERE user_id = ?", [row.user_id],
//                         (error, result) => {
//                             if (error) {
//                                 console.log(error);
//                             }
//                             else {
//                                 //Should only return one row (username)
//                                 Object.keys(result).forEach(function (key) {
//                                     var unameRow = result[key];
//                                     console.log(unameRow.username);

//                                     usernamesArray.push(unameRow.username);
//                                     console.log("inside " + usernamesArray); 
//                                 });
//                                 //done = true;
//                                 var i = 1;
//                                 data = {
//                                     headers: ["#", "Username", "Highscore"],
//                                     rows: new Array(5).fill(undefined).map(() => {
//                                         return [
                                        
//                                             i++,
//                                             usernamesArray[i],
//                                             scoreArray[i]
//                                         ];
//                                     })
//                                 }; 
//                                 response.send(results);
                                
//                             }
                            
//                         });
//                 });


//             }
//     });
// });

router.get('/home/Pong', (request, response) => {
    // the __dirname automatically puts in the project folder
    response.sendFile(path.join(__dirname+'/Pong.html'));
});

//---------------------------------------------------------------------------------------------------------------------


// this is needed to actually have the routes work
server.use('/', router);
//Runs the server on port 8080
server.listen(8080, function(){
    console.log("server runnin'");
});

