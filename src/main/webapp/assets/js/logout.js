 function logout() {

     Notiflix.Confirm.show(
         'Lumina Books',
         'Are you sure you want to log out?',
         'Yes',
         'Cancel',

         function okCb() {
             logoutUser();
         },

         function cancelCb() {
             // optional
         }
     );

}

async function logoutUser(){
    try {

        const response = await fetch("api/users/logout", {
            method: "POST"
        });

        const data = await response.json();

        if (data.status) {

            Notiflix.Notify.success(data.message);

            setTimeout(() => {
                window.location.href = "index.html";
            }, 1000);

        } else {

            Notiflix.Notify.failure(data.message);
        }

    } catch (e) {

        Notiflix.Notify.failure(e.message);
    }
}