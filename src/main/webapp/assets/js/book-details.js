const params = new URLSearchParams(window.location.search);

const id= params.get("id");

if(!id){
    Notiflix.Notify.failure("Invalid book");

}

addEventListener("DOMContentLoaded",async()=>{
    await loaddetailbook();
    await  loadfeedback();
})

async function loaddetailbook() {

    const params = new URLSearchParams(window.location.search);
    const id = params.get("id");

    if (!id) {
        alert("Invalid book");
        return;
    }

    Notiflix.Loading.pulse("Data Loading...", {
        clickToClose: false,
        svgColor: '#0284c7'
    });


    const response = await fetch(`api/books/bookdetails?id=${id}`);
    const data = await response.json();

    if (!data.status) {
        Notiflix.Notify.failure("Book Not Found", {
            position: 'center-top'
        });;
        return;
    }

    console.log(data.related);

    DetailBook(data.book);
    loadrelatedBooks(data.related);
    loadauthoerBooks(data.authoredbooks);
}

async function loadfeedback() {
console.log("loading feed");
    const params = new URLSearchParams(window.location.search);
    const id = params.get("id");

    if (!id) {
        alert("Invalid book");
        return;
    }



    const response = await fetch(`api/books/detailedFeedback?id=${id}`);
    const data = await response.json();



    if (!data.status) {
        Notiflix.Notify.failure("Book Not Found", {
            position: 'center-top'
        });
        return;
    }

    displayFeedbacks(data.feedbacks);
}

function displayFeedbacks(feedbacksArray) {
    const container = document.getElementById('feedback-list-container');

    // Safety check: Ensure the container exists on the page
    if (!container) return;

    // Clear any hardcoded HTML placeholders
    container.innerHTML = '';

    // Check if there are no feedbacks yet
    if (!feedbacksArray || feedbacksArray.length === 0) {
        container.innerHTML = `
            <div class="text-center text-muted py-4">
                <p class="mb-0">No feedback submitted for this book yet. Be the first!</p>
            </div>
        `;
        return;
    }

    // Loop through each feedback item from the API payload
    feedbacksArray.forEach(feedback => {
        // Evaluate rate: 1 = Good, 2 = Bad (per your modal configuration)
        const isGood = feedback.rate === 1;

        const badgeClass = isGood
            ? 'bg-success-subtle text-success border border-success-subtle'
            : 'bg-danger-subtle text-danger border border-danger-subtle';

        const badgeText = isGood ? '👍 Good Experience' : '👎 Bad Experience';

        // Format the date string cleanly (e.g., "May 31, 2026")
        const formattedDate = new Date(feedback.createdDate).toLocaleDateString('en-US', {
            year: 'numeric',
            month: 'short',
            day: 'numeric'
        });

        // Construct the review card element
        const feedbackCard = `
            <div class="card border-0 bg-light p-3 shadow-sm rounded-3">
                <div class="d-flex justify-content-between align-items-start mb-2 gap-2">
                    <div>
                        <h6 class="fw-bold mb-1 text-dark">${feedback.usertName}</h6>
                        <span class="badge ${badgeClass} px-2 py-1 fs-7">${badgeText}</span>
                    </div>
                    <small class="text-muted flex-shrink-0">${formattedDate}</small>
                </div>
                <p class="mb-0 text-secondary italic">"${feedback.text}"</p>
            </div>
        `;

        // Inject the generated element into the scrollable list container
        container.innerHTML += feedbackCard;
    });
}

let bookID;
function DetailBook(book){
    bookID=book.id;

    document.getElementById("bookTitle").textContent = book.title;
    document.getElementById("bookAuthor").textContent = "By " + book.author;
    const bookPrice = document.getElementById("bookPrice");
    const discountPrice = document.getElementById("discountPrice");
    if (book.discountPrice > 0) {
        bookPrice.textContent = "LKR" + book.price.toFixed(2);
        bookPrice.classList.add("text-decoration-line-through", "text-muted");

        discountPrice.textContent = "LKR" +book.discountPrice.toFixed(2);
    } else {
        bookPrice.textContent = "LKR" + book.price.toFixed(2);
        bookPrice.classList.remove("text-decoration-line-through", "text-muted");

        discountPrice.textContent = "";
    }
    document.getElementById("bookDescription").textContent = book.description;
    document.getElementById("bookImage").src =book.coverImagePath;
    document.getElementById("breadcrumb-title").textContent=book.title;
    const container = document.getElementById("add-cart");

    container.innerHTML=`
                                                ${
        book.bought
            ? `
                            <a href="${book.filePath}" download class="btn btn-success btn-lg w-100 mb-2">
                                Download Book
                            </a>
                            <button class="btn btn-accent btn-lg w-100" data-bs-toggle="modal" data-bs-target="#feedbackModal" id="openFeedbackBtn" ">
                               Leave a Feedback
                            </button>
                            `
            : `
                            <button class="btn btn-accent btn-lg w-100"
                                    onclick="addToCart(${book.id})">
                                Add to Cart
                            </button>
                            
                            `
    }
`

    document.getElementById("")

}

function loadauthoerBooks(books){
    const container = document.getElementById("author-book-section")
    container.innerHTML="";
    books.forEach(book =>{
        container.innerHTML+=`
            <div class="col-lg-3 col-md-4 col-sm-6" >
                <div class="card book-card h-100">
                <div class="book-img-container">
                    <img src="${book.coverImagePath}" class="img-fluid">
                </div>
                <div class="card-body">
                    <h5 class="card-title fw-bold">${book.title}</h5>
                    <p class="text-muted small">${book.author}</p>
                    <div class="d-flex justify-content-between align-items-center">
                        <span class="price-tag">$${book.price}</span>
                         <a href="book-details.html?id=${book.id}"><button class="btn btn-sm btn-outline-warning"  
                            >View Book
                        </button>
                        </a>
                        <button class="btn btn-sm btn-outline-dark" onclick="addToCart(${book.id})">
                        Add to Cart
                        </button>
                    </div>
                </div>
            </div>
            </div>


`

    })


}





function loadrelatedBooks(books){
    const container = document.getElementById("related-books-section")
container.innerHTML="";
    books.forEach(book =>{
container.innerHTML+=`
            <div class="col-lg-3 col-md-4 col-sm-6" >
                <div class="card book-card h-100">
                <div class="book-img-container">
                    <img src="${book.coverImagePath}" class="img-fluid">
                </div>
                <div class="card-body">
                    <h5 class="card-title fw-bold">${book.title}</h5>
                    <p class="text-muted small">${book.author}</p>
                    <div class="d-flex justify-content-between align-items-center">
                        <span class="price-tag">$${book.price}</span>
                         <a href="book-details.html?id=${book.id}"><button class="btn btn-sm btn-outline-warning"  
                            >View Book
                        </button>
                        </a>
                        <button class="btn btn-sm btn-outline-dark" onclick="addToCart(${book.id})">
                        Add to Cart
                        </button>
                    </div>
                </div>
            </div>
            </div>


`

    })


}



async function applyfeeddback(){
    const rating = document.querySelector('input[name="rating"]:checked').value;
    const text= document.getElementById("feedbackText").value;

    Notiflix.Loading.pulse("Uploading Feedback...", {
        clickToClose: false,
        svgColor: '#0284c7'
    });


    feedback={
        bookId:bookID,
        rate:rating,
        text:text
    }
   try{
       const response = await fetch("api/books/feedback", {
           method:"POST",
           headers: {
               "Content-Type": "Application/json"
           },
           body: JSON.stringify(feedback)
       })
       const data = await response.json();
       if(response.ok){
           if (!data.status) {
               Notiflix.Notify.failure(data.message);
           }else{
               Notiflix.Notify.success(data.message);
               await  loadfeedback();



           }
       }
   }catch (e) {
       console.error(e);

   }finally {
       Notiflix.Loading.remove();

   }
}


