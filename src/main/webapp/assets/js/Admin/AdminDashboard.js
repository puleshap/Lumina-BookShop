let revenueChartInstance = null;
let signupChartInstance = null;
let currentFilter = "Total"; // Default filter tracking

// Initialize blank charts on DOM content load
document.addEventListener("DOMContentLoaded", () => {
    initializeCharts();
    loadDashboardData("Total"); // Initial fetch execution

    // Bind dynamic event click triggers to your time-filter buttons
    const filterButtons = document.querySelectorAll(".filter-btn");
    filterButtons.forEach(button => {
        button.addEventListener("click", (e) => {
            // Remove active styling flag from other elements
            filterButtons.forEach(btn => btn.classList.remove("active"));
            // Add active state to clicked button
            e.target.classList.add("active");
            document.querySelector(".featured-book-img").innerHTML = `<span class="small">COVER</span>`;


            const chosenFilter = e.target.innerText.trim();
            currentFilter = chosenFilter;

            // Fetch new dataset window
            loadDashboardData(chosenFilter);
        });
    });
});

function initializeCharts() {
    const ctx1 = document.getElementById('revenueChart').getContext('2d');
    revenueChartInstance = new Chart(ctx1, {
        type: 'line',
        data: { labels: [], datasets: [{ label: 'Revenue', data: [], borderColor: '#ff6600', tension: 0.4, fill: false }] },
        options: { responsive: true, maintainAspectRatio: false, plugins: { legend: { display: false } } }
    });

    const ctx2 = document.getElementById('signupChart').getContext('2d');
    signupChartInstance = new Chart(ctx2, {
        type: 'bar',
        data: { labels: [], datasets: [{ label: 'New Users', data: [], backgroundColor: '#ff6600' }] },
        options: { responsive: true, maintainAspectRatio: false, plugins: { legend: { display: false } } }
    });
}

async function loadDashboardData(filterType) {
    try {
        // Start Notiflix pulse loader overlay
        if (typeof Notiflix !== 'undefined') {
            Notiflix.Loading.pulse("Compiling Metrics...", { clickToClose: false, svgColor: '#ff6600' });
        }

        const response = await fetch(`../api/admin/dashboard/summary?filter=${filterType.toLowerCase()}`);
        const data = await response.json();

        if (data.status) {

            console.log(data);
            // 1. Update text content inside standard KPI metric cards
            document.querySelector(".row.g-4 .col-md-4:nth-child(1) .stat-value").innerText = Number(data.totalUsers).toLocaleString();
            document.querySelector(".row.g-4 .col-md-4:nth-child(2) .stat-value").innerText = Number(data.totalBooks).toLocaleString();
            document.querySelector(".row.g-4 .col-md-4:nth-child(3) .stat-value").innerText = "LKR " + Number(data.totalRevenue).toLocaleString(undefined, {minimumFractionDigits: 2, maximumFractionDigits: 2});

            // 2. Update top-selling spotlight display card metrics
            if (data.featuredBook) {
                document.querySelector(".featured-book-card h5").innerText = data.featuredBook.title;
                document.querySelector(".featured-book-card p").innerText = `${data.featuredBook.unitsSold} units sold this period`;
                if (data.featuredBook.coverPath) {
                    document.querySelector(".featured-book-img").innerHTML = `<img src="../${data.featuredBook.coverPath}" class="img-fluid rounded" style="max-height: 80px; object-fit: cover;" alt="Cover">`;
                }
            } else {
                document.querySelector(".featured-book-card h5").innerText = "No Data Available";
                document.querySelector(".featured-book-card p").innerText = "0 items moved";
                    document.querySelector(".featured-book-img").innerHTML = `<img src="https://via.placeholder.com/400x600" class="img-fluid rounded" style="max-height: 80px; object-fit: cover;" alt="Cover">`;

            }

            // 3. Dynamic payload mapping directly into Chart structures
            // Updates Revenue Line Chart
            revenueChartInstance.data.labels = data.revenueTimelineLabels || [];
            revenueChartInstance.data.datasets[0].data = data.revenueTimelineData || [];
            revenueChartInstance.update();

            // Updates Signups Bar Chart
            signupChartInstance.data.labels = data.signupTimelineLabels || [];
            signupChartInstance.data.datasets[0].data = data.signupTimelineData || [];
            signupChartInstance.update();

        } else {
            if (typeof Notiflix !== 'undefined') Notiflix.Notify.failure(data.message);
        }
    } catch (e) {
        console.error("Dashboard tracking exception: ", e);
        if (typeof Notiflix !== 'undefined') Notiflix.Notify.failure("Failed to refresh dashboard data matrix.");
    } finally {
        if (typeof Notiflix !== 'undefined') Notiflix.Loading.remove();
    }
}