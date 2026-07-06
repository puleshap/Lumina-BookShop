document.addEventListener("DOMContentLoaded",async ()=>{
    try {
        Notiflix.Loading.pulse("Data Loading...", {
            clickToClose: false,
            svgColor: '#0284c7'
        });
        await loadbooks();
        await AdminloadAuthor();
        await AdminloadGenres();
        await AdminloadStatus();
    }finally {
        Notiflix.Loading.remove();

    }

})
async function AdminloadGenres(){

    const response = await fetch ("../api/basic/genres");

    if(response.ok){
        const data = await response.json();
        const genreselect = document.getElementById("genreSelect");
        const editGenreSelect = document.getElementById("editGenreSelect");
        const loadGenreSelect = document.getElementById("loadgenre");


        console.log(data);

        data.genres.forEach(genre=>{
            const addOption = document.createElement("option");
            addOption.value = genre.id;
            addOption.textContent = genre.name;
            genreselect.appendChild(addOption);

            // 2. Create a separate, distinct option for the "Edit Book" dropdown
            const editOption = document.createElement("option");
            editOption.value = genre.id;
            editOption.textContent = genre.name;
            editGenreSelect.appendChild(editOption);

            const loadOption = document.createElement("option");
            loadOption.value = genre.id;
            loadOption.textContent = genre.name;
            loadGenreSelect.appendChild(loadOption);

        });


    } else{
        alert("sad");
    }

}
async function AdminloadAuthor(){
    console.log("aauthors");
    const response = await fetch ("../api/basic/authors");

    if(response.ok){
        const data = await response.json();
        const AuthorSelect = document.getElementById("AuthorSelect");
        const editAuthorSelect = document.getElementById("editAuthorSelect");
        const loadAuthorSelect = document.getElementById("loadauthor");


        console.log(data);

        data.authors.forEach(author=>{
            const addOption = document.createElement("option");
            addOption.value = author.id;
            addOption.textContent = author.name;
            AuthorSelect.appendChild(addOption);

            // 2. Create a separate, distinct option for the "Edit Book" dropdown
            const editOption = document.createElement("option");
            editOption.value = author.id;
            editOption.textContent = author.name;
            editAuthorSelect.appendChild(editOption);

            const loadtOption = document.createElement("option");
            loadtOption.value = author.id;
            loadtOption.textContent = author.name;
            loadAuthorSelect.appendChild(loadtOption);


        });


    } else{
        alert("sad");
    }

}

async function AdminloadStatus(){

    const response = await fetch(`../api/basic/admin/statuses`);
    if(response.ok){
        const data = await response.json();
        const StatusSelect = document.getElementById("StatusSelect");
        console.log(data);
        data.statuses.forEach(status=>{
            const addOption = document.createElement("option");
            addOption.value = status.id;
            addOption.textContent = status.value;
            StatusSelect.appendChild(addOption);
console.log(addOption);

        });


    } else{
        alert("sad");
    }

}


let currentPage = 0;
let totalPages = 0;
async function loadbooks(page = 0){
    currentPage = page;

   try{
       const response = await fetch(`../api/admin/books/load?page=${page}`);

       const data = await response.json();

       if(response.ok){
           if(data.status){
               displayBooks(data);
               totalPages = Math.ceil(data.totalBooks / data.pageSize);
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

        btn.onclick = () => loadbooks(i);

        nextBtn.before(btn);
    }

}

function displayBooks(data){

    document.getElementById("count").innerText =
        "Total Books found : " + data.totalBooks;

    const tbody = document.getElementById("table");

    tbody.innerHTML = "";

    data.books.forEach(book => {
console.log(book);
        const row = document.createElement("tr");
        userstatus = book.status;
        let statusClass = "";
        if(userstatus === "ACTIVE"){
            statusClass = "status-active";
        }else if(userstatus === "BLOCKED"){
            statusClass = "status-blocked";
        }


        row.innerHTML = `
             <td>
                        <div class="d-flex align-items-center">
                            <img src="../${book.coverImagePath}" class="book-thumbnail-admin me-3" alt="Cover">
                            <div>
                                <div class="fw-bold text-white">${book.title}</div>
                                
                            </div>
                        </div>
                    </td>

            <td>
                <span class="genre-tag">
                    ${book.Genrevalue}
                </span>
            </td>
              <td>
                <span class="">
                    ${book.author}
                </span>
            </td>

            <td class="text-white">
                ${
            book.discountPrice > 0
                ? `
                            <span class="text-decoration-line-through text-danger">
                                LKR${book.price.toFixed(2)}
                            </span>
                            <br>
                            <span class="text-success fw-bold">
                                LKR${book.discountPrice.toFixed(2)}
                            </span>
                          `
                : `LKR${book.price.toFixed(2)}`
        }
            <td> <span class="status-pill ${statusClass}">${book.status}</span></td>
          

            <td class="text-end">

                <button class="action-btn-circle me-1"onclick="viewBook(${book.id})"> 👁 </button>

                <button class="action-btn-circle me-1" onclick="openEditModal(${book.id});">✎ </button>



            </td>
        `;

        tbody.appendChild(row);
    });

    window.currentBooks = data.books;
}

function viewBook(bookId){

    const book = currentBooks.find(
        b => b.id === bookId
    );

    if(!book) return;

    document.getElementById("modalTitle").textContent = book.title;
    document.getElementById("modalId").textContent = book.id;
    document.getElementById("modalAuthor").textContent = book.author;
    document.getElementById("modalGenre").textContent = book.Genrevalue;
    document.getElementById("modalPrice").textContent = book.price.toFixed(2);
    document.getElementById("modalDiscount").textContent = book.discountPrice.toFixed(2);
    document.getElementById("modalCreated").textContent = book.CreatedAt;
    document.getElementById("modalDescription").textContent = book.description;
    document.getElementById("modalFile").href = "../"+book.filePath;
    document.getElementById("modalCover").src ="../"+ book.coverImagePath || "https://via.placeholder.com/250x350";

    const modal = new bootstrap.Modal(document.getElementById("viewBookModal"));

    modal.show();
}

function openEditModal(bookId){
    // Assuming currentBooks is the array inside your JSON: data.books
    const book = currentBooks.find(b => b.id === bookId);

    if (!book) {
        console.error("Book not found with ID:", bookId);
        return;
    }

    // 1. Set Hidden ID
    document.getElementById("editBookId").value = book.id;

    // 2. Set Text Fields
    document.getElementById("editTitle").value = book.title;
    document.getElementById("editPrice").value = book.price;
    document.getElementById("editDiscountPrice").value = book.discountPrice || "";
    document.getElementById("editDescription").value = book.description;
    // 3. Set Dropdowns (Make sure the option values match book.author and book.genreId)
    const authorSelect = document.getElementById("editAuthorSelect");
    if (authorSelect) authorSelect.value = book.authorId;

    const genreSelect = document.getElementById("editGenreSelect");
    if (genreSelect) genreSelect.value = book.genreId;

    const statusSelect = document.getElementById("StatusSelect");
    if (statusSelect) statusSelect.value = book.statusId;

    // 4. Update Cover Image Preview
    const coverPreview = document.getElementById("editCoverPreview");
    if (coverPreview) {
        coverPreview.src = book.coverImagePath ? `../${book.coverImagePath}` : "https://via.placeholder.com/250x350";
    }

    // 5. Update File Path Label (Fixing your specific issue)
    const fileLabel = document.getElementById("editCurrentFileLabel");
    if (fileLabel) {
        if (book.filePath) {
            // Extracts just 'book.pdf' out of 'assets/books/das/book.pdf' for a cleaner look
            const fileName = book.filePath.split('/').pop();
            fileLabel.textContent = `Current file: ../${fileName}`;
        } else {
            fileLabel.textContent = "No file currently uploaded";
        }
    }

    // 6. Show the Modal safely
    const modalElement = document.getElementById("editBookModal");
    const modalInstance = bootstrap.Modal.getInstance(modalElement) || new bootstrap.Modal(modalElement);
    modalInstance.show();
}
async function addBook() {
    const formData = new FormData();

    formData.append("title", document.getElementById("addTitle").value);
    formData.append("authorId", document.getElementById("AuthorSelect").value);
    formData.append("genreId", document.getElementById("genreSelect").value);
    formData.append("price", document.getElementById("addPrice").value);
    formData.append("discountPrice", document.getElementById("addDiscountPrice").value);
    formData.append("description", document.getElementById("addDescription").value);

    const coverImage = document.getElementById("CoverImageInput").files[0];
    const ebook = document.getElementById("EbookInput").files[0];

    formData.append("coverImage", coverImage);
    formData.append("ebookFile", ebook);

    try {
        Notiflix.Loading.pulse("Adding Book...", {
            clickToClose: false,
            svgColor: '#0284c7'
        });

        const response = await fetch("../api/admin/books/add", {
            method: "POST",
            body: formData
        });

        const data = await response.json();
        console.log(data);

        if (data.status) {
            Notiflix.Notify.success(data.message || "Book added successfully!");
            setTimeout(() => { location.reload(); }, 200);
        } else {
            Notiflix.Notify.failure(data.message || "Failed to add book.");
        }

    } catch (e) {
        console.error(e);
        Notiflix.Notify.failure("An unexpected network error occurred.");
    } finally {
        // Always remove the loading screen when processing wraps up
        Notiflix.Loading.remove();
    }
}

async function updateBook() {
    const formData = new FormData();
    formData.append("id", document.getElementById("editBookId").value);
    formData.append("title", document.getElementById("editTitle").value);
    formData.append("authorId", document.getElementById("editAuthorSelect").value);
    formData.append("genreId", document.getElementById("editGenreSelect").value);
    formData.append("statusId", document.getElementById("StatusSelect").value);
    formData.append("price", document.getElementById("editPrice").value);
    formData.append("discountPrice", document.getElementById("editDiscountPrice").value);
    formData.append("description", document.getElementById("editDescription").value);


    const coverImage = document.getElementById("editCoverImageInput").files[0];
    const ebook = document.getElementById("editEbookInput").files[0];

    if (coverImage) {
        formData.append("coverImage", coverImage);
    }
    if (ebook) {
        formData.append("ebookFile", ebook);
    }

    try {
        // Start loading sequence
        Notiflix.Loading.pulse("Updating Book Details...", {
            clickToClose: false,
            svgColor: '#0284c7'
        });

        const response = await fetch("../api/admin/books/update", {
            method: "PUT",
            body: formData
        });

        const data = await response.json();
        console.log(data);

        if (data.status) {
            Notiflix.Notify.success(data.message || "Book synchronized successfully!");
            // Delayed reload allows the user to actually read the success message
            setTimeout(() => { location.reload(); }, 1500);
        } else {
            Notiflix.Notify.failure(data.message || "Failed to update record.");
        }

    } catch (e) {
        console.error("Failed to update book: ", e);
        Notiflix.Notify.failure("An internal communication exception occurred.");
    } finally {
        // Kill the overlay
        Notiflix.Loading.remove();
    }
}


async function searchAdminBooks(page = 0) {
    currentPage = page;

    // Admin fields: text (title query), author name, and genre drop-down select
    const searchText = document.getElementById("searchadmin").value;
    const author = document.getElementById("loadauthor").value;
    const genre = document.getElementById("loadgenre").value;


    let params = new URLSearchParams();

    if (searchText) params.append("text", searchText);
    if (author !== "0") {
        params.append("author", author);
    }
    if (genre !== "0") {
        params.append("genreId", genre);
    }

    params.append("page", currentPage);

    try {
        Notiflix.Loading.pulse("Searching Book...", {
            clickToClose: false,
            svgColor: '#0284c7'
        });

        const response = await fetch("../api/admin/books/search?" + params.toString());
        const data = await response.json();

        console.log(data);

        if (!data.status) {
            Notiflix.Notify.failure("Admin Problem: " + data.message, { position: 'center-top' });
            return;
        }

        // Call your admin dashboard render methods
        displayBooks(data);
        renderPagination(data.page, data.totalPages);

    } catch (e) {
        console.log(e);
        Notiflix.Notify.failure("Admin Network Error: " + e.message, { position: 'center-top' });
    } finally {
        Notiflix.Loading.remove();
    }
}
