async function loadSettingsMenu() {
    const menu = document.getElementById("settingsMenu");

    const res = await fetch("/api/auth/status");
    const data = await res.json();

    if (data.status) {
        // USER LOGGED IN
        menu.innerHTML = `
            <li><a class="dropdown-item" href="home.html">Homepage</a></li>
            <li><a class="dropdown-item" href="profile.html">My Profile</a></li>
            <li><a class="dropdown-item" href="my-products.html">My Products</a></li>
            <li><a class="dropdown-item" href="watchlist.html">My Watchlist</a></li>
            <li><a class="dropdown-item" href="cart.html">My Cart</a></li>
            <li><a class="dropdown-item" href="sales.html">My Sales</a></li>
            <li><a class="dropdown-item" href="purchase-history.html">Purchase History</a></li>
            <li><a class="dropdown-item" onclick="logout()">Logout</a></li>
        `;
    } else {
        // USER NOT LOGGED IN
        menu.innerHTML = `
            <li><a class="dropdown-item" href="sign-in.html">Login</a></li>
            <li><a class="dropdown-item" href="sign-up.html">Register</a></li>
        `;
    }
}

loadSettingsMenu();
