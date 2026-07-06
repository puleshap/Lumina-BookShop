document.addEventListener("DOMContentLoaded",async ()=>{
    try {
        Notiflix.Loading.pulse("Data Loading...", {
            clickToClose: false,
            svgColor: '#0284c7'
        });
console.log("works");
        const param = new URLSearchParams(window.location.search);
        const order = param.get("id");

        console.log("works" +order);
       await loadorder(order);

    }finally {
        Notiflix.Loading.remove();

    }

})


async function loadorder(order){
    console.log("works");
    const response = await fetch(`api/Orders/OrderById?id=${order}`);
data= await response.json();
    console.log(data);

    if(response.ok){
        if(data.status){

            displayInvoice(data);
        }
    }

}


function displayInvoice(data){
    document.getElementById("name").innerText=data.user.firstName + " "+ data.user.lastName;
    document.getElementById("email").innerText=data.user.email;
    document.getElementById("inv").innerText="#LMN- "+data.order.orderNumber;
    document.getElementById("date").innerText=data.order.orderDate;
    document.getElementById("count").innerText=data.size;
    document.getElementById("total").innerText="Rs. "+data.order.totalAmount;
    const tbody = document.getElementById("table");
    tbody.innerHTML=``;
    data.book.forEach(b=>{
        const row = document.createElement("tr");
        row.innerHTML=`
            
                    <td>
                        <div class="fw-bold">${b.title}</div>
                        <small class="text-muted">Genre : ${b.Genrevalue}</small>
                    </td>
                    <td class="text-center">1</td>
                    <td class="text-end">Rs.${b.price}</td>
                    <td class="text-end">Rs.${b.price}</td>
               
                
                `;
        tbody.appendChild(row);
    });
}

