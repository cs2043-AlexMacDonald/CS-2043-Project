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

//Stuff for mysql
const mysql = require('mysql');

//Stuff for popup windows
//const popup = require('window-popup').windowPopup;


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
    password: "",              //NEED TO BLANK THIS OUT WHEN COMMITING!!!
    database: ""
});




// POST REQUESTS --------------------------------------------------------------------------------------------------------

//POST for user creation, on ADMIN PAGE
//Creation of user and insertion into the database                  NEED TO DO SOME SORT OF VALIDATION
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
    response.sendFile(path.join(__dirname+'/adminPage.html'));                  //NEED TO MAKE A PAGE FOR THE ERROR
});

router.get('/home/AimTrainer', (request, response) => {
    // the __dirname automatically puts in the project folder
    response.sendFile(path.join(__dirname+'/aimTrainer.html'));
});

router.get('/home/HardestGameEver', (request, response) => {
    // the __dirname automatically puts in the project folder
    response.sendFile(path.join(__dirname+'/hardestGameEver.html'));
});

//---------------------------------------------------------------------------------------------------------------------



// this is needed to actually have the routes work
server.use('/', router);
//Runs the server on port 8080
server.listen(8080, function(){
    console.log("server runnin'");
});

