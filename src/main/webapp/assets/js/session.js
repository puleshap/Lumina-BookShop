async function loadNavbar() {
    try {
        const response = await fetch("api/auth/session", {
            method: "GET",
            credentials: "include" // VERY IMPORTANT for session cookies
        });

        const navItems = document.getElementById("nav-items");

        if (response.ok) {
            // User logged in
            navItems.innerHTML = `
                <li class="nav-item"><a class="nav-link" href="index.html">Home</a></li>
                <li class="nav-item"><a class="nav-link" href="search.html">Browse</a></li>
                <li class="nav-item"><a class="nav-link" href="library.html">My Library</a></li>
                <li class="nav-item"><a class="btn btn-accent ms-lg-3" href="#" onclick="logout()">Logout</a></li>
            `;
        } else {
            // Not logged in
            navItems.innerHTML = `
                <li class="nav-item"><a class="nav-link" href="index.html">Home</a></li>
                <li class="nav-item"><a class="nav-link" href="search.html">Browse</a></li>
                <li class="nav-item"><a class="btn btn-accent ms-lg-3" href="login.html">Login</a></li>
            `;
        }

    } catch (error) {
        console.error("Navbar load error:", error);
    }
}

document.addEventListener("DOMContentLoaded",async ()=>{
    await loadNavbar();

})

