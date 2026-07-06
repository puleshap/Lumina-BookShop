let uemail;
const myModal = new bootstrap.Modal(document.getElementById('verificationModal'));

async function  register() {
    let firstName = document.getElementById("fn").value;
    let lastName = document.getElementById("ln").value;
    let email = document.getElementById("email").value;
    let password = document.getElementById("password").value;
    let confirm = document.getElementById("confirm").value;


    const user = {
        firstName: firstName,
        lastName: lastName,
        email: email,
        password: password,
        confirm:confirm
    }

  try{
      const response = await fetch("api/users/register", {
          method: "POST",
          headers: {
              "Content-Type": "Application/json"
          },
          body: JSON.stringify(user)
      });
      Notiflix.Loading.pulse("Wait...", {
          clickToClose: false,
          svgColor: '#0284c7'
      });

      if (response.ok){
          Notiflix.Loading.remove(1000);
          const data = await response.json();
          if (data.status){
              uemail=email;
              Notiflix.Report.success(
                  'Lumina Books',
                  data.message,
                  'Okay',
                  () => {
                      myModal.show();
                  },
              );
          } else {
              Notiflix.Notify.failure(data.message);
          }
      } else {
          const notification = new Notification();
          Notiflix.Notify.failure("Something went wrong, please check your credentials");
      }
      }catch (e) {
      Notiflix.Notify.failure(e.message);
  }


}

function verifymove(){
    let email = document.getElementById("email2").value;
    if (email === ""){
        Notiflix.Notify.failure("Input the Email before trying to verify");
    }else{
        window.location = "verify-account.html?email=" + email;
    }

}



async function verifyEmail(){
    const code = document.getElementById('vcode').value;

    Notiflix.Loading.pulse("Wait...", {
        clickToClose: false,
        svgColor: '#0284c7'
    });

    const verifyObj = {
        email:uemail,
        verificationCode:code
    }

    try{
        const response = await fetch("api/verify-accounts",{
            method:"POST",
            headers:{
                "Content-Type":"application/json"
            },
            body:JSON.stringify(verifyObj)
        });

        if (response.ok){
            const data = await response.json();
            if (data.status){
                Notiflix.Report.success(
                    'Successful Verification',
                    "You can now Sign In!!",
                    'Okay',
                    () => {
                        myModal.hide();
                       changeView();
                    },
                );
            } else {
                Notiflix.Notify.failure(data.message);
            }
        } else {
            Notiflix.Notify.failure("Something went wrong, please check your code and try again");
        }
    } catch (e) {
        Notiflix.Notify.failure(e.message);
    } finally {
        Notiflix.Loading.remove(1000);
    }
}