// JavaScript Document


var modal = document.querySelector(".modal"); 
var trigger = document.querySelector(".trigger"); 
var closeButton = document.querySelector(".close-button"); 
var cancelButton = document.querySelector("#cancel");

        //console.log(modal);

function toggleModal() 
{ 
   modal.classList.toggle("show-modal"); 
}


function windowOnClick(event) 
{ 
    if (event.target === modal) 
	{ 
        toggleModal(); 
    } 
}

trigger.addEventListener("click", toggleModal); 
closeButton.addEventListener("click", toggleModal); 
cancel.addEventListener("click", toggleModal); 
window.addEventListener("click", windowOnClick);

