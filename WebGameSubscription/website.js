
//Is called when you hit the "submit" button on the Login page
function validateForm()
{
    var username = document.forms["login"]["uname"].value;
    var password = document.forms["login"]["pswd"].value;

    //if valid login info brings you to next page
    if(username == "a" && password == "a")
    {
        //I think to do the whole redirect thing you need 
        redirect();    
    }
    else if(username == "admin" && password == "admin")
    {
        redirectAdminPage();
    }
    else
    {
        alert("This isn't an account fool! Ya fool!");
    }

    //need to put false here otherwise the page won't get redirected
    return false;
}


function redirect()
{
    /*--------------------------------------------------------------------------------
      Basically by default when you click on the submit button the form gets submitted
      but this cancels any ongoing HTTP requests from going through so you can 
      by pass this by just returning false.
    ---------------------------------------------------------------------------------*/

    window.location.href = 'gameList.html';
    return false;
}

function redirectAdminPage()
{
    window.location.href = "adminPage.html";
    return false;
}

function createAccount()
{
    alert("have not added the account creation! :(")
}


//Note: there is a "download" attribute for <a> tags
        
