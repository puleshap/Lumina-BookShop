document.addEventListener("DOMContentLoaded", () => {
    loadAndInitializeHeader();
});

async function loadAndInitializeHeader() {
    const headerWrapper = document.getElementById("global-header");
    if (!headerWrapper) return;

    try {
        // 1. Fetch the static component template structure
        const response = await fetch("header2.html"); // Adjust path if placed inside a components folder
        if (response.ok) {


            headerWrapper.innerHTML = await response.text();
            console.log(headerWrapper);

            // 2. Once the HTML structure is safely injected, update the dynamic auth state
            await updateNavbarAuth();
        }
    } catch (e) {
        console.error("Failed to load global header skeleton: ", e);
    }
}

async function updateNavbarAuth() {
    const authContainer = document.getElementById("nav-items");
    if (!authContainer) return;

    try {
        const response = await fetch("api/auth/session");
        const data = await response.json();
        console.log(data);

        if (data.isLoggedIn) {
            // Injected when logged in: Clean profile/settings icon dropdown
            authContainer.innerHTML = `
                <li class="nav-item dropdown">
                    <button class="btn nav-settings-btn d-flex align-items-center justify-content-center" 
                            type="button" id="userMenuBtn" data-bs-toggle="dropdown" aria-expanded="false" title="My Settings">
                        <span class="">My Settings</span>
                    </button>
                    <ul class="dropdown-menu dropdown-menu-end custom-dropdown border-secondary text-white m-0 shadow-lg" aria-labelledby="userMenuBtn">
                        <li class="px-3 py-2 border-bottom border-secondary mb-1">
                            <span class="d-block small text-muted">Signed in as</span>
                            <span class="fw-bold text-black d-block text-truncate" style="max-width: 160px;">${data.name}</span>
                        </li>
                       
                        <li><a class="dropdown-item custom-dropdown-item" href="profile.html">My Profile</a></li>
                        <li><a class="dropdown-item custom-dropdown-item" href="My-Library.html">My Library</a></li>
                        <li><a class="dropdown-item custom-dropdown-item" href="cart.html">My Cart</a></li>
                        <li><a class="dropdown-item custom-dropdown-item" href="OrderList.html">My Purchases</a></li>
                        
                        <li><hr class="dropdown-divider border-secondary"></li>
                        <li><a class="dropdown-item custom-dropdown-item text-danger fw-bold" href="#" onclick="logout();">Logout</a></li>
                    </ul>
                </li>
            `;
        } else {
            // Injected when guest view is active
            authContainer.innerHTML = `

                <li class="nav-item">
                    <a href="SignUpPage.html" class="btn btn-accent-orange px-4 py-2 fw-semibold">Get Started</a>
                </li>
            `;
        }
    } catch (e) {
        console.error("Error setting up navbar session elements: ", e);
    }
}


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