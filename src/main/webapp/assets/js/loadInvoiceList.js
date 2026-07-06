document.addEventListener("DOMContentLoaded",async ()=>{
    try {
        Notiflix.Loading.pulse("Data Loading...", {
            clickToClose: false,
            svgColor: '#0284c7'
        });

       await loadorderList();

    }finally {
        Notiflix.Loading.remove();

    }

})


async function loadorderList(){
    console.log("works");
    const response = await fetch(`api/Orders/OrderList`);
data= await response.json();
    console.log(data);

    if(response.ok){
        if(data.status){

            displayInvoice(data);
        }
    }

}


function displayInvoice(data){

    const tbody = document.getElementById("table");
    tbody.innerHTML=``;
    data.order.forEach(o=>{
        const row = document.createElement("tr");
        row.innerHTML=`
                            <td class="ps-4">
                        <a href="Invoice.html?id=${o.orderNumber}" class="order-id-link">#LMN-${o.orderNumber}</a>
                    </td>
                    <td class="text-muted">${o.orderDate}</td>
                    <td>${o.itemCount} Books</td>
                    <td class="fw-bold">Rs.${o.totalAmount}</td>
                    <td><span class="status-badge status-success">${o.orderStatus}</span></td>
                    <td class="text-end pe-4">
                        <a href="Invoice.html?id=${o.orderNumber}" class="btn btn-sm btn-outline-dark">View Invoice</a>
                    </td>
                
                
                `;
        tbody.appendChild(row);
    });
}

