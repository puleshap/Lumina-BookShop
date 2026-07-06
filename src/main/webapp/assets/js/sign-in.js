function changeView() {

    const signUpBox = document.getElementById("signUpBox");
    const signInBox = document.getElementById("signInBox");

    signUpBox.classList.toggle("d-none");
    signInBox.classList.toggle("d-none");
}

function togglePasswords() {

    document.querySelectorAll(".password-field").forEach(input => {

        input.type = input.type === "password"
            ? "text"
            : "password";
    });
}

function toggleLoginPassword() {

    const password = document.getElementById("password2");

    password.type = password.type === "password"
        ? "text"
        : "password";
}




async function login(){

    let email = document.getElementById("email2");
    let password = document.getElementById("password2");

    Notiflix.Loading.pulse("Wait...", {
        clickToClose: false,
        svgColor: '#0284c7'
    });

    const userLoginObject = {
        email:email.value,
        password:password.value
    }

    try{
        const response = await fetch("api/users/login", {
            method:"POST",
            headers:{
                "Content-Type":"application/json"
            },
            body:JSON.stringify(userLoginObject)
        });

        if(response.ok){
            const data = await response.json();
            if (data.status){
                Notiflix.Report.success(
                    'Lumina Books',
                    data.message,
                    'Okay',
                    () => {
                        localStorage.setItem("loggedIn","true");
                        window.location = "index.html"
                    },
                );
            } else {

                Notiflix.Notify.failure(data.message);
                if(data.pending){
                    myModal.show();
                    uemail=email.value;
                }
            }
        } else {
            Notiflix.Notify.failure("Login failed! Please try again later");
        }
    } catch (e) {
        Notiflix.Notify.failure(e.message);
    } finally {
        Notiflix.Loading.remove(1000);
    }
}



function logout() {
    localStorage.removeItem("loggedIn");
    updateMenu();
}



    function toggleForgotPasswords() {

    const fields = document.querySelectorAll(".forgot-password-field");

    fields.forEach(field => {

    field.type = field.type === "password"
    ? "text"
    : "password";
});

    const eye1 = document.getElementById("forgotEye1");
    const eye2 = document.getElementById("forgotEye2");

    eye1.classList.toggle("bi-eye-fill");
    eye1.classList.toggle("bi-eye-slash-fill");

    eye2.classList.toggle("bi-eye-fill");
    eye2.classList.toggle("bi-eye-slash-fill");
}




function updateMenu() {
    const settings = document.getElementById("settingsDropdown");
    const joinUs = document.getElementById("joinUsButton");

    const isLoggedIn = localStorage.getItem("loggedIn");

    if (isLoggedIn) {
        settings.style.display = "block";
        joinUs.style.display = "none";
    } else {
        settings.style.display = "none";
        joinUs.style.display = "block";
    }
}




const forgotmodal = new bootstrap.Modal(document.getElementById("forgotPasswordModal"));
    async function openForgotPasswordModal() {

    const email = document.getElementById("email2").value;

    // validate email first
console.log(email);
    try {
    const response = await fetch("api/users/forgot", {
        method:"POST",
        headers:{
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            email: email
        })
    });

        if(response.ok){
            const data = await response.json();
            if (data.status){
                document.getElementById("fp_email").value = email;

                const modal = new bootstrap.Modal(
                    document.getElementById("forgotPasswordModal")
                );

                forgotmodal.show();
            } else {
                Notiflix.Notify.failure(data.message);
            }
        } else {
            Notiflix.Notify.failure("Login failed! Please try again later");
        }
    } catch (e) {
        Notiflix.Notify.failure(e.message);

    }
}



async function changepassword() {

    let email = document.getElementById("fp_email").value;
    let code = document.getElementById("fp_code").value;

    let password = document.getElementById("fp_password").value;
    let confirm = document.getElementById("fp_confirm").value;


    user = {
        email:email,
        verificationCode:code,
        password:password,
        confirm:confirm
    }


    try {
        const response = await fetch("api/users/changepassword", {
            method:"POST",
            headers:{
                "Content-Type": "application/json"
            },
            body: JSON.stringify(user)
        });

        if(response.ok){
            const data = await response.json();
            if (data.status){


                forgotmodal.hide();
                Notiflix.Report.success(
                    'Lumina Books',
                    data.message,
                    'Okay'
                );
            } else {
                Notiflix.Notify.failure(data.message);
            }
        } else {
            Notiflix.Notify.failure("Login failed! Please try again later");
        }
    } catch (e) {
        Notiflix.Notify.failure(e.message);

    }
}





