document.addEventListener("DOMContentLoaded",async ()=>{
    try {
        Notiflix.Loading.pulse("Data Loading...", {
            clickToClose: false,
            svgColor: '#0284c7'
        });
        await loadOrders();
        await AdminloadStatus();
    }finally {
        Notiflix.Loading.remove();

    }

})



let currentPage = 0;
let totalPages = 0;
async function loadOrders(page = 0){
    currentPage = page;

   try{
       const response = await fetch(`../api/admin/orders/load?page=${page}`);

       const data = await response.json();

       if(response.ok){
           if(data.status){
               displayOrder(data);
               totalPages = Math.ceil(data.totalOrders / data.pageSize);
               renderPagination();
           }else{

           }
       }
   }catch (e) {
       Notiflix.Notify.failure(e.message);

   }

}

function renderPagination(){

    const container = document.getElementById("pagination");
    container.style.display="flex";

    container.querySelectorAll(".page-number").forEach(btn => btn.remove());

    const prevBtn = document.getElementById("prevBtn");
    const nextBtn = document.getElementById("nextBtn");

    prevBtn.disabled = currentPage === 0;
    nextBtn.disabled = currentPage === totalPages - 1;

    prevBtn.onclick = () => {
        if(currentPage > 0){
            loadbooks(currentPage - 1);
        }
    };

    nextBtn.onclick = () => {
        if(currentPage < totalPages - 1){
            loadbooks(currentPage + 1);
        }
    };

    for(let i = 0; i < totalPages; i++){

        const btn = document.createElement("button");

        btn.className = "action-btn-circle page-number";

        btn.textContent = i + 1;

        if(i === currentPage){
            btn.style.background = "var(--accent-orange)";
            btn.style.color = "black";
        }

        btn.onclick = () => loadOrders(i);

        nextBtn.before(btn);
    }

}

function displayOrder(data){

    document.getElementById("orderCount").innerText =
        "Total Orders found : " + data.totalOrders;

    const tbody = document.getElementById("Otable");

    tbody.innerHTML = "";

    data.orders.forEach(order => {
        console.log(order);
        const row = document.createElement("tr");
        orderStatus = order.orderStatus;
       let statusText;
        let statusClass = "";
        if (orderStatus === "COMPLETED") {
            statusClass = "status-active";
            statusText="COMPLETED";
        } else {
            statusText="CANCELLED";

            statusClass = "status-blocked";
        }


        row.innerHTML = `

    <td><a href="../Invoice.html?id=${order.orderNumber}" class="order-id-highlight">#LMN-${order.orderNumber}</a></td>
            <td>
                <div class="text-white fw-bold">${order.CustomerName}</div>
                <div class="small text-white">${order.CustomerEmail}</div>

            </td>
            <td class="small text-white">${order.orderDate}</td>
            <td>${order.itemCount}  Items</td>
            <td class="fw-bold text-white">LKR.${order.totalAmount}</td>
            <td> <span class="status-pill ${statusClass}">${statusText}</span></td>
            <td class="text-end">
                <a href="../Invoice.html?id=${order.orderNumber}" class="action-btn-circle" title="View Invoice">👁</a>
            </td>
            
        `;

        tbody.appendChild(row);
    });

    window.currentBooks = data.books;
}

async function searchOrder(page = 0) {
    currentPage = page;

    // Admin fields: text (title query), author name, and genre drop-down select
    const searchText = document.getElementById("searchOrder").value;



    let params = new URLSearchParams();

    if (searchText) params.append("search", searchText);


    params.append("page", currentPage);

    try {
        Notiflix.Loading.pulse("Searching Order...", {
            clickToClose: false,
            svgColor: '#0284c7'
        });

        const response = await fetch("../api/admin/orders/searchOrders?" + params.toString());
        const data = await response.json();

        console.log(data);

        if (!data.status) {
            Notiflix.Notify.failure("Admin Problem: " + data.message, { position: 'center-top' });
            return;
        }

        displayOrder(data);
        renderPagination(data.page, data.totalPages);

    } catch (e) {
        console.log(e);
        Notiflix.Notify.failure("Admin Network Error: " + e.message, { position: 'center-top' });
    } finally {
        Notiflix.Loading.remove();
    }
}
