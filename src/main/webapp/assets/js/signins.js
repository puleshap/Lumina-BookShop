function changestate() {
    alert("sad");
    var signUpBox = document.getElementById("signup_card");
    var signInBox = document.getElementById("login_card");

    signUpBox.classList.toggle("d-none");
    signInBox.classList.toggle("d-none");
}

function showpasswordss() {

    const password = document.getElementById("password");
    let confirm;
    if(document.getElementById("confirm")!=null){
        confirm = document.getElementById("confirm");
    }

   if(password.getAttribute("type")==="password"){
       password.setAttribute("type","text");
       confirm.setAttribute("type","text");
   }else{
       password.setAttribute("type","password");
       confirm.setAttribute("type","password");
   }






}