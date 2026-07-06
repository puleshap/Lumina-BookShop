addEventListener("DOMContentLoaded",async()=>{loaddetailbook()})


async function loaddetailbook() {


    const response = await fetch(`api/library/get`);
    const data = await response.json();

    console.log(data);

    if (!data.status) {
        Notiflix.Notify.failure("Problem: " + data.message, {
            position: 'center-top'
        });
        return;
    }

    renderBooks(data.books);

    console.log(data);


}

function renderBooks(books) {
    const container = document.getElementById("book-show");
    container.innerHTML = "";

    if (books.length === 0) {
        container.innerHTML = `
<div class="col-12 text-center py-5">
        <div class="mb-4">
            <svg width="100" height="100" fill="#f8f9fa" class="bi bi-book-half" viewBox="0 0 16 16" style="color: #e9ecef;">
                <path d="M8.5 2.687c.654-.689 1.782-.886 3.112-.752 1.234.124 2.503.523 3.388.893v9.923c-.918-.35-2.107-.692-3.287-.81-1.094-.111-2.278.039-3.213.492V2.687zM8 1.783C7.015.936 5.587.81 4.287.94c-1.514.153-3.042.672-3.994 1.105A.5.5 0 0 0 0 2.5v11a.5.5 0 0 0 .707.455c.883-.4 2.303-.837 3.896-.97 1.137-.096 2.446.045 3.424.577a.5.5 0 0 0 .416 0c.978-.532 2.287-.673 3.424-.576 1.593.132 3.013.569 3.896.97a.5.5 0 0 0 .707-.455v-11a.5.5 0 0 0-.293-.455c-.952-.433-2.48-.952-3.994-1.105C10.413.81 8.985.936 8 1.783z"/>
            </svg>
        </div>
        <h3 class="fw-bold">Your library is empty</h3>
        <p class="text-muted mx-auto" style="max-width: 400px;">
            It looks like you haven't added any ebooks yet. Start exploring our collection to build your digital library.
        </p>
        <a href="search.html" class="btn btn-accent mt-3 px-5 py-2">Browse Books</a>
    </div>
`;
        return;
    }

    books.forEach(book => {
        container.innerHTML += `

         <div class="col-lg-3 col-md-4 col-sm-6">
            <div class="card library-card h-100 p-3">
                <div class="book-img-container mb-3" style="height: 250px;">
                    <img src="${book.coverImagePath}" class="img-fluid rounded" alt="Book">
                </div>
                <h6 class="fw-bold mb-1 text-truncate">${book.title}</h6>
                <p class="small text-muted mb-2">${book.author}</p>

              

                <div class="d-grid gap-2">
                    <a  href="book-details.html?id=${book.id}" class="btn btn-accent btn-sm">View Book</a>
                    
                        <a href="${book.filePath}" download class="download-link text-center mt-1">
                        <svg width="14" height="14" fill="currentColor" class="bi bi-download me-1" viewBox="0 0 16 16">
                            <path d="M.5 9.9a.5.5 0 0 1 .5.5v2.5a1 1 0 0 0 1 1h12a1 1 0 0 0 1-1v-2.5a.5.5 0 0 1 1 0v2.5a2 2 0 0 1-2 2H2a2 2 0 0 1-2-2v-2.5a.5.5 0 0 1 .5-.5z"/>
                            <path d="M7.646 11.854a.5.5 0 0 0 .708 0l3-3a.5.5 0 0 0-.708-.708L8.5 10.293V1.5a.5.5 0 0 0-1 0v8.793L5.354 8.146a.5.5 0 1 0-.708.708l3 3z"/>
                        </svg> Download
                    </a>
                </div>
            </div>
        </div>`;
    });
}

