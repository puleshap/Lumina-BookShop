document.addEventListener("DOMContentLoaded",async ()=>{
    try {
        Notiflix.Loading.pulse("Data Loading...", {
            clickToClose: false,
            svgColor: '#0284c7'
        });

        loadCart();

    }finally {
        Notiflix.Loading.remove();

    }

})


async function addToCart(id){

    const response = await fetch ("api/cart/add",{
        method:"POST",
        headers:{
            "Content-Type": "application/json"
        },
        body:JSON.stringify({
            bookid:id
        })
    });
    const data = await response.json();

    if(response.ok){
        if (!data.status) {
            Notiflix.Notify.failure(data.message);
        }else{
            Notiflix.Notify.success("Added to cart");

        }

    }


}



async function loadCart() {
    Notiflix.Loading.pulse("Data Loading...", {
        clickToClose: false,
        svgColor: '#0284c7'
    });
    document.getElementById('empty-cart-state').classList.add("d-none");
    document.getElementById('cart-items-wrapper').classList.add("d-none");
    try {

        const response = await fetch("api/cart");
        const data = await response.json();

        if (!data.status) {
             document.getElementById("empty-cart-state").classList.remove("d-none");
return;

        }if(data.canreload){
window.location.reload();
        }
console.log(data.items);

        document.getElementById("total-price").textContent ="LKR " + data.total.toFixed(2);

        renderCartItems(data.items);


    } catch (e) {
        console.error(e);
    }  finally {
    Notiflix.Loading.remove();
}
}


function renderCartItems(items) {
    const container = document.getElementById("cart-items-container");
    container.innerHTML = "";
    if( items!=null ){
    document.getElementById('empty-cart-state').classList.add("d-none");
    document.getElementById('cart-items-wrapper').classList.remove("d-none");

    }
    items.forEach(book => {
        container.innerHTML += `
             <div class="cart-item d-md-flex align-items-center justify-content-between">
                <div class="d-flex align-items-center">
                    <img src="${book.coverImagePath}" class="cart-item-img me-3" alt="Book">
                    <div>
                        <h5 class="fw-bold mb-1">${book.title}</h5>
                        <p class="text-muted small mb-2">${book.title}</p>
                         <a href="book-details.html?id=${book.id}"><button class="btn btn-sm btn-outline-warning "  
                            >View Book
                        </button>
                        </a>
                        <button class="btn remove-btn  btn-outline-danger " onclick="removeFromCart(${book.id})">
                        Remove from Cart
                        
                    </div>
                </div>
                <div class="d-flex align-items-center mt-3 mt-md-0">
                    
                    <div class="text-end">
                        <span class="fw-bold fs-5">LKR ${book.price}</span>
                    </div>
                </div>
            </div>
        `;
    });
}


async function removeFromCart(bookId) {

    const send={
        id:bookId
    }

    const response = await fetch("api/cart/remove", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(send)
    });

    const data = await response.json();



    if (data.status) {
        Notiflix.Notify.success("Removed From Cart");
        loadCart();
    }else{
        Notiflix.Notify.failure("Does not exist in Cart");
        loadCart();
    }
    if(data.reloadpage){
        window.location.reload();
    }
}

async function clearCart() {
    const response = await fetch("api/cart/clear", {
        method: "POST"
    });

    const data = await response.json();
    if (data.status) {
        Notiflix.Notify.success("Cart has been Cleared");

        loadCart();


    }
}
