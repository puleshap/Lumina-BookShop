document.addEventListener("DOMContentLoaded",async ()=>{
    try {
        Notiflix.Loading.pulse("Data Loading...", {
            clickToClose: false,
            svgColor: '#0284c7'
        });
console.log("works");
        const param = new URLSearchParams(window.location.search);
        const order = param.get("OrderId");

        loadorder(order);


    }finally {
        Notiflix.Loading.remove();

    }

})


async function loadorder(order){
    console.log("works");
    const response = await fetch(`api/Orders/ordersummary?id=${order}`);
data= await response.json();
    console.log(data);
    if(data.status){
        const container = document.getElementById("load");

        container.innerHTML+=` <div class="success-icon-circle">
        <svg xmlns="http://www.w3.org/2000/svg" width="60" height="60" fill="currentColor" class="bi bi-check-lg" viewBox="0 0 16 16">
            <path d="M12.736 3.97a.733.733 0 0 1 1.047 0c.286.289.29.756.01 1.05L7.88 12.01a.733.733 0 0 1-1.065.02L3.217 8.384a.757.757 0 0 1 0-1.06.733.733 0 0 1 1.047 0l3.052 3.093 5.42-6.446z"/>
        </svg>
    </div>

    <h1 class="fw-bold">Payment Successful!</h1>
    <p class="text-muted fs-5">Thank you for your purchase. Your order has been confirmed.</p>

    <div class="order-box shadow-sm">
        <div class="d-flex justify-content-between mb-3">
            <span class="text-muted">Order Number:</span>
            <span class="fw-bold">${data.order.orderNumber}</span>
        </div>
        <div class="d-flex justify-content-between mb-3">
            <span class="text-muted">Date:</span>
            <span>${data.order.orderDate}</span>
        </div>
        <div class="d-flex justify-content-between mb-3">
            <span class="text-muted">Full Name:</span>
            <span>${data.user.firstName + " "+ data.user.lastName}</span>
        </div>
        <div class="d-flex justify-content-between mb-3">
            <span class="text-muted">Email:</span>
            <span>${data.user.email}</span>
        </div>
        <hr>
        <div class="d-flex justify-content-between mb-1 fw-bold">
            <span>Total Amount Paid:</span>
            <span class="text-accent">Rs.${data.order.totalAmount}</span>
        </div>
    </div>

    <p class="mb-4">A confirmation email with your invoice has been sent to <strong>${data.user.email}</strong>.</p>

    <div class="success-action-btns">
        <span  class="btn btn-accent btn-lg px-5" onclick="loadinvoice(data.order.orderNumber);">View the Invoice</span>
        <a href="search.html" class="btn btn-outline-secondary btn-lg px-5">Continue Shopping</a>
    </div>`;




    }

}

async function loadinvoice(id){
    console.log("res"+id);
    window.location="Invoice.html?id="+id;

}