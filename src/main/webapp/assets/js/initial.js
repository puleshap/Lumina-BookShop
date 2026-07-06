document.addEventListener("DOMContentLoaded",()=>{loadbooks();} );


async function loadbooks(){

    try{
        const response = await fetch("api/books");

        if(response.ok){
            data = await response.json();
            console.log(data);

const container = document.getElementById("bookContainer");
container.innerHTML="";

            data.books.forEach(book => {

                container.innerHTML += `
        <div class="col-md-3">
            <div class="card book-card h-100">
                <div class="book-img-container">
                    <img src="${book.coverImagePath}" class="img-fluid">
                </div>

                <div class="card-body">
                    <h5 class="card-title fw-bold">${book.title}</h5>
                    <p class="text-muted small">${book.author}</p>

                    ${
                    book.discountPrice > 0
                        ? `
                                <div class="mb-3">
                                    <span class="text-decoration-line-through price-tag me-2">
                                        LKR ${book.price.toFixed(2)}
                                    </span>
                                    <span class="price-tag text-danger fw-bold">
                                        LKR ${book.discountPrice.toFixed(2)}
                                    </span>
                                </div>
                              `
                        : `
                                <span class="price-tag mb-3">
                                    LKR ${book.price.toFixed(2)}
                                </span>
                              `
                }

                    <div class="d-flex justify-content-between align-items-center">

                        <a href="book-details.html?id=${book.id}">
                            <button class="btn btn-sm btn-outline-warning">
                                View Book
                            </button>
                        </a>

                        ${
                    book.bought
                        ? `
                                    <a href="${book.filePath}" download class="btn btn-sm btn-outline-success">
                                        Download Book
                                    </a>
                                  `
                        : `
                                    <button class="btn btn-sm btn-outline-dark"
                                            onclick="addToCart(${book.id})">
                                        Add to Cart
                                    </button>
                                  `
                }

                    </div>
                </div>
            </div>
        </div>
    `;
            });


        }

    }catch (e) {
        console.error("Error loading books", e);
    }

}