async function AdminLogin(){

    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;


    const admin={
        username:username,
        password:password

    }

    const response = await fetch ("../api/admin/login", {
        method:"POST",
        headers:{
            "Content-Type": "application/json"
        },
        body: JSON.stringify(admin)
    });

    if(response.ok){
        const data = await response.json();
        console.log(data);
        if (data.status){
            Notiflix.Report.success(
                'Successful Admin Login',
                "Welcome to the Admin Dashboard",
                'Okay',
                () => {
                    window.location = "AdminDashboard.html"
                },
            );
        } else {
            Notiflix.Notify.failure(data.message);
        }
    } else {
        Notiflix.Notify.failure("Something went wrong, please check your code and try again");
    }


}