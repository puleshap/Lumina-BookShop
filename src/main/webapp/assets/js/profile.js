document.addEventListener("DOMContentLoaded",()=>{loaduser();} );


async function loaduser(){

    Notiflix.Loading.pulse("Wait...", {
        clickToClose: false,
        svgColor: '#0284c7'
    });
    try{
        const response = await fetch("api/users");

        if(response.ok) {
            data = await response.json();
            if (data.status) {
                console.log(data);
                Notiflix.Notify.success(data.message);
                document.getElementById("fn").value = data.user.firstName;
                document.getElementById("ln").value = data.user.lastName;
                document.getElementById("e").value = data.user.email;
                document.getElementById("m").value = data.user.mobile;
                document.getElementById("date").value = data.user.sinceAt;
                document.getElementById("img").src=data.user.profileImagePath;
            } else {
                Notiflix.Notify.failure(data.message);
            }
        }
    } catch (e) {
        Notiflix.Notify.failure(e.message);
    } finally {
        Notiflix.Loading.remove(1000);
    }

}

function selectProfileImage() {

    document.getElementById("profileImageInput").click();

}

document.getElementById("profileImageInput").addEventListener("change", function (event) {
        const file = event.target.files[0];
        if (file) {
            const imageURL = URL.createObjectURL(file);
            document.getElementById("img").src = imageURL;
        }
    });

async function updateProfil(){

    Notiflix.Loading.pulse("Wait...", {
        clickToClose: false,
        svgColor: '#0284c7'
    });

    const formData = new FormData();

    formData.append("firstName", document.getElementById("fn").value);
    formData.append("lastName", document.getElementById("ln").value);


    formData.append("email", document.getElementById("e").value);
    formData.append("mobile", document.getElementById("m").value);
    formData.append("sinceAt", document.getElementById("date").value);

    const imageFile = document.getElementById("profileImageInput").files[0];

    if (imageFile) {
        formData.append("image", imageFile);
    }

    try {

        const response = await fetch("api/users/update", {
            method: "PUT",
            body: formData
        });

    const data = await response.json();
    if(response.ok){
        if (data.status){
            Notiflix.Notify.success(data.message);

            await loaduser();
        }else{
            Notiflix.Notify.failure(data.message);
        }
    }
    } catch (e) {
        Notiflix.Notify.failure(e.message);
    } finally {
        Notiflix.Loading.remove(1000);
    }
}