document.addEventListener("DOMContentLoaded",async ()=>{
    try {
        Notiflix.Loading.pulse("Data Loading...", {
            clickToClose: false,
            svgColor: '#0284c7'
        });
        await loadAuthors();
    }finally {
        Notiflix.Loading.remove();

    }

})


let currentPage = 0;
let totalPages = 0;

async function loadAuthors(page = 0){
    currentPage = page;

    const response = await fetch(`../api/admin/authors/load?page=${page}`);
    const data = await response.json();
console.log(data);

        if(response.ok){
            if(data.status){
                displayUsers(data);
                totalPages = Math.ceil(data.totalAuthors / data.pageSize);
                renderPagination();
            }
        }
}

function clearPagination() {
    const container = document.getElementById("pagination").style.display = "none";
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
            loadAuthors(currentPage - 1);
        }
    };

    nextBtn.onclick = () => {
        if(currentPage < totalPages - 1){
            loadAuthors(currentPage + 1);
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

        btn.onclick = () => loadAuthors(i);

        nextBtn.before(btn);
    }

}


function displayUsers(data){
    document.getElementById("count").innerText="Total Authors found : "+data.totalAuthors;
    const tbody = document.getElementById("table");
    tbody.innerHTML=``;
    data.authors.forEach(author=>{




        const row = document.createElement("tr");
        row.innerHTML=`
                    <td>${author.id}</td>

                    <td>
                        <div class="d-flex align-items-center">
                            <div class="" style="width: 40px; height: 40px;"
                                  style="width:40px;height:40px;object-fit:cover;" />
                              </div>
                            <div>
                                <div class="fw-bold text-white">${author.name} </div>
                                
                            </div>
                        </div>
                    </td>
                   
                   <td><span class="author-count-badge">${author.BookCount} Books</span></td>
                   <td class="">${author.AddedDate}</td>

                `;
        tbody.appendChild(row);
    });
}

async function  addAuthor() {
    let name = document.getElementById("author_name").value;



    const author = {
        name: name
    }
    try{
        const response = await fetch("../api/admin/authors/addAuthor", {
            method: "POST",
            headers: {
                "Content-Type": "Application/json"
            },
            body: JSON.stringify(author)
        });
        Notiflix.Loading.pulse("Wait...", {
            clickToClose: false,
            svgColor: '#0284c7'
        });

        if (response.ok){
            Notiflix.Loading.remove(1000);
            const data = await response.json();
            if (data.status){
                Notiflix.Notify.success('New Author added Successfully');
                loadAuthors();

            } else {
                Notiflix.Notify.failure(data.message);
            }
        }
    }catch (e) {
        Notiflix.Notify.failure(e.message);
    }


}



async function search(){
    let search = document.getElementById("authorSearch").value;
    currentPage = 0;
    totalPages = 0;

    try {
        Notiflix.Loading.pulse("Data Loading...", {
            clickToClose: false,
            svgColor: '#0284c7'
        });
        if(search === null){
            await loadAuthors();
        }else{
            const response = await fetch(`../api/admin/authors/searchAuthors?search=${search}`);
            const data = await response.json();
            console.log(data);

            if(response.ok){
                if(data.status){
                    displayUsers(data);


                }
            }

        }



    }finally {
        Notiflix.Loading.remove();

    }
}

function confirmStatusChange(id){

    Notiflix.Confirm.show(
        'Change User Status',
        'Do you want to change the status of this user?',
        'Yes',
        'Cancel',
        function okCb(){
            changeUserstatus(id);
        },
        function cancelCb(){}
    );

}

async function changeUserstatus(id){




    try{
        const response = await fetch(`../api/admin/changeStatus?id=`+id);

        if(response.ok){
            const data = await response.json();
            if (data.status){
                Notiflix.Report.success('Admin User Management', 'User status changed successfully');
                loadAuthors();
            } else {
                Notiflix.Notify.failure(data.message);
            }
        } else {
            Notiflix.Notify.failure("Login failed! Please try again later");
        }
    } catch (e) {
        Notiflix.Notify.failure(e.message);
    } finally {
        Notiflix.Loading.remove(1000);
    }
}