let currentpages = 0;
let pagesize = 8;

let currentPage = 0;
let pageSize = 8;

async function searchbooks(page = 0) {

    currentPage = page;

    const title = document.getElementById("title").value;
    const author = document.getElementById("author").value;
    const minPrice = document.getElementById("min_price").value;
    const maxPrice = document.getElementById("max_price").value;
    const genreSelect = document.getElementById("genreSelect");

    let params = new URLSearchParams();

    if (title) params.append("title", title);
    if (author) params.append("author", author);
    if (minPrice) params.append("minPrice", minPrice);
    if (maxPrice) params.append("maxPrice", maxPrice);
    if (genreSelect.value !== "0") {
        params.append("genreId", genreSelect.value);
    }

    params.append("page", currentPage);
    params.append("size", pageSize);

    try {
        const response = await fetch("api/books/search?" + params.toString());
        const data = await response.json();

        console.log(data);

        if (!data.status) {
            Notiflix.Notify.failure("Problem: " + data.message, {
                position: 'center-top'
            });
            return;
        }

        renderBooks(data.books);
        renderPagination(data.page, data.totalPages);

    } catch (e) {
        console.error(e);
        Notiflix.Notify.failure("Error: " + e.message, {
            position: 'center-top'
        });
    }
}


function renderPagination(current, totalPages) {
    const pagination = document.getElementById("book-pagination");
    if (!pagination) return;

    pagination.innerHTML = "";

    const wrapper = document.createElement("div");
    wrapper.className = "d-flex justify-content-center flex-wrap gap-2 mt-4";
    pagination.appendChild(wrapper);

    for (let i = 0; i < totalPages; i++) {
        wrapper.innerHTML += `
            <button class="btn ${i === current ? 'btn-accent' : 'btn-outline-dark'} pagination-btn"
                    onclick="searchbooks(${i})">
                ${i + 1}
            </button>
        `;
    }
}

function renderBooks(books) {
    const container = document.getElementById("book-results");
    container.innerHTML = "";

    if (books.length === 0) {
        container.innerHTML = `
        <div id="no-results-container" class="no-results-state text-center py-5">
            <div class="no-results-icon mb-3">
                <svg xmlns="http://www.w3.org/2000/svg" width="64" height="64" fill="#ced4da" class="bi bi-exclamation-circle" viewBox="0 0 16 16">
                    <path d="M8 15A7 7 0 1 1 8 1a7 7 0 0 1 0 14zm0 1A8 8 0 1 0 8 0a8 8 0 0 0 0 16z"/>
                    <path d="M7.002 11a1 1 0 1 1 2 0 1 1 0 0 1-2 0zM7.1 4.995a.905.905 0 1 1 1.8 0l-.35 3.507a.552.552 0 0 1-1.1 0L7.1 4.995z"/>
                </svg>
            </div>
            <h4 class="fw-bold">Oops! We couldn't find that book.</h4>
            <p class="text-muted">Try checking for typos or using more general keywords.</p>
            <div class="mt-4">
                <button class="btn btn-outline-primary" onclick="window.location.reload()">Refresh</button>
                
            </div>
        </div>
`;
        return;
    }

    books.forEach(book => {
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
                                <span class="price-tag mb-5">
                                    LKR ${book.price.toFixed(2)}
                                </span>
                              `
        }
                    <div class="d-flex justify-content-between align-items-center mt-2">
                       
                         <a href="book-details.html?id=${book.id}"><button class="btn btn-sm btn-outline-warning"  
                            >View Book
                        </button>
                        </a>
                    ${book.bought ? `
                            <a href="${book.filePath}" download class="btn btn-sm btn-success">
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
        </div>`;
    });
}


async function loadGenres(){

    const response = await fetch ("api/basic/genres");

    if(response.ok){
        const data = await response.json();
        const genreselect = document.getElementById("genreSelect");
        console.log(data);

        data.genres.forEach(genre=>{
            const option = document.createElement("option");
            option.value = genre.id;
            option.textContent = genre.name;
            genreselect.appendChild(option);
        });


    } else{
        alert("sad");
    }

}
document.addEventListener("DOMContentLoaded", async()=>
{await loadGenres(); });






