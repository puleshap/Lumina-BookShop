document.addEventListener("DOMContentLoaded",async ()=>{
    try {
        Notiflix.Loading.pulse("Data Loading...", {
            clickToClose: false,
            svgColor: '#0284c7'
        });
        await loadGenres();
    }finally {
        Notiflix.Loading.remove();

    }

})


let currentPage = 0;
let totalPages = 0;

async function loadGenres(page = 0){
    currentPage = page;

try{
    const response = await fetch(`../api/admin/genre/load?page=${page}`);
    const data = await response.json();
    console.log(data);

    if(response.ok){
        if(data.status){
            displayGenre(data);
            totalPages = Math.ceil(data.totalGenres / data.pageSize);
            renderPagination();
        }
    }
}catch (e){
    Notiflix.Notify.failure(e.message);
}finally {
    Notiflix.Loading.remove(1000);
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
            loadGenres(currentPage - 1);
        }
    };

    nextBtn.onclick = () => {
        if(currentPage < totalPages - 1){
            loadGenres(currentPage + 1);
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

        btn.onclick = () => loadGenres(i);

        nextBtn.before(btn);
    }

}


function displayGenre(data){
    document.getElementById("count").innerText="Total Genres found : "+data.totalGenres;
    const tbody = document.getElementById("table");
    tbody.innerHTML=``;
    data.genres.forEach(genre=>{




        const row = document.createElement("tr");
        row.innerHTML=`
                    <tr>                            
                    <td><span class="fw-bold text-white">${genre.id}</span></td>



                            <td class="align-content-center"><span class="fw-bold text-white">${genre.name}</span></td>
                            <td class="align-content-center"><span class="genre-stat-badge ">${genre.BookCount}</span></td>
                            <td class="text-white small">${genre.CreatedAt}</td>
                            
                        </tr>

                  
                `;
        tbody.appendChild(row);
    });
}

async function  addGenre() {

    Notiflix.Loading.pulse("Wait...", {
        clickToClose: false,
        svgColor: '#0284c7'
    });

    let name = document.getElementById("genre_name").value;



    const genre = {
        name: name
    }
    try{
        const response = await fetch("../api/admin/genre/addGenre", {
            method: "POST",
            headers: {
                "Content-Type": "Application/json"
            },
            body: JSON.stringify(genre)
        });


        if (response.ok){

            const data = await response.json();
            if (data.status){
                Notiflix.Notify.success('New Genre added Successfully');
                loadGenres();

            } else {
                Notiflix.Notify.failure(data.message);
            }
        }
    }catch (e) {
        Notiflix.Notify.failure(e.message);
    }finally {
        Notiflix.Loading.remove(1000);
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
                    displayGenre(data);


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