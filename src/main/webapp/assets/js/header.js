class HeaderContent extends HTMLElement {
    connectedCallback() {
        this.innerHTML = `
        <div class="col-12">
            <div class="row mt-1 mb-1">

                <div class="col-12 col-lg-1 d-flex align-items-center">
                    <img src="assets/old/resource/logo.png" height="100px" alt="logo">
                </div>

                <div class="col-12 offset-lg-1 col-lg-7 d-flex align-items-center justify-content-center">
                    <h5 class="fw-bold text-center m-0">
                        Gamers Dream Shopping Place
                    </h5>
                </div>

                <div class="col-12 col-lg-2 offset-lg-1 align-self-center text-center">
                    <div class="row">
                        <div class="col-12 col-lg-7">

                            <div class="dropdown mt-3" id="settingsDropdown" style="display:none;">
                                <button class="btn btn-dark dropdown-toggle"
                                        type="button"
                                        data-bs-toggle="dropdown">
                                    My Settings
                                </button>
                                <ul class="dropdown-menu dropdown-menu-dark">
                                    <li><a class="dropdown-item" href="index.html">Homepage</a></li>
                                    <li><a class="dropdown-item" href="profile.html">My Profile</a></li>
                                    <li><a class="dropdown-item" href="#">My Cart</a></li>
                                    <li><a class="dropdown-item" href="#">Purchase History</a></li>
                                    <li><a class="dropdown-item" href="#" onclick="logout()">Logout</a></li>
                                </ul>
                            </div>

                            <div class="mt-3" id="joinUsButton">
                                <a href="sign-in.html" class="btn btn-dark text-white">
                                    Join Us
                                </a>
                            </div>

                        </div>
                    </div>
                </div>

            </div>
        </div>
        `;

        // 🔥 IMPORTANT: update menu AFTER HTML exists
       window.addEventListener("load", this.updateMenuState());
    }

    updateMenuState() {
        const loggedIn = sessionStorage.getItem("loggedIn");

        const settings = this.querySelector("#settingsDropdown");
        const joinUs = this.querySelector("#joinUsButton");

        if (loggedIn === "true") {
            settings.style.display = "block";
            joinUs.style.display = "none";
        } else {
            settings.style.display = "none";
            joinUs.style.display = "block";
        }
    }
}

customElements.define("header-content", HeaderContent);
