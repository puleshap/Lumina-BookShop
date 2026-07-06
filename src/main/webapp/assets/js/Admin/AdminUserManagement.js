document.addEventListener("DOMContentLoaded",async ()=>{
    try {
        Notiflix.Loading.pulse("Data Loading...", {
            clickToClose: false,
            svgColor: '#0284c7'
        });

        await loadUsers();


    }finally {
        Notiflix.Loading.remove();

    }

})


let currentPage = 0;
let totalPages = 0;

async function loadUsers(page = 0){
    currentPage = page;

    const response = await fetch(`../api/admin/getUsers?page=${page}&type=1`);
    const data = await response.json();
console.log(data);

        if(response.ok){
            if(data.status){
                displayUsers(data);
                totalPages = Math.ceil(data.totalUsers / data.pageSize);
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
            loadUsers(currentPage - 1);
        }
    };

    nextBtn.onclick = () => {
        if(currentPage < totalPages - 1){
            loadUsers(currentPage + 1);
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

        btn.onclick = () => loadUsers(i);

        nextBtn.before(btn);
    }

}


function displayUsers(data){
    document.getElementById("count").innerText="Total users found : "+data.totalUsers;
    const tbody = document.getElementById("table");
    tbody.innerHTML=``;
    data.users.forEach(user=>{
        userstatus = user.statusValue;
        let statusClass = "";
        if(userstatus === "VERIFIED"){
            statusClass = "status-active";
        }else if(userstatus === "PENDING"){
            statusClass = "status-pending";
        }else if(userstatus === "BLOCKED"){
            statusClass = "status-blocked";
        }
        let actionButton = "";

        if(userstatus=== "VERIFIED"){
            actionButton = `
                <button class="action-btn-circle text-danger" title="Block User" onclick="confirmStatusChange(${user.Id})">🔒 </button>
            `;
        }
        else if(userstatus === "BLOCKED"){
            actionButton = `
                <button class="action-btn-circle text-success"  title="Unblock User" onclick="confirmStatusChange(${user.Id})"> 🔑</button>
            `;
        }


        let src;
        if(user.profileImagePath){
            src=`../${user.profileImagePath}`;
        }else{
            src = '../assets/images/new_user.svg';
            console.log(src);

        }

        const row = document.createElement("tr");
        row.innerHTML=`
                    <td>${user.Id}</td>

                    <td>
                        <div class="d-flex align-items-center">
                            <div class="" style="width: 40px; height: 40px;">
                            <img alt="profile image" class="rounded-circle bg-secondary me-3" src="${src}"
                              style="width:40px;height:40px;object-fit:cover;" />
                              </div>
                            <div>
                                <div class="fw-bold text-white">${user.firstName} ${user.lastName}</div>
                                <div class="small text-white">${user.email}</div>
                            </div>
                        </div>
                    </td>
                    <td> <span class="status-pill ${statusClass}">${user.statusValue}</span></td>
                    <td>${user.sinceAt}</td>
                    <td class="fw-bold text-white">RS.${user.totalSpend}</td>
                    <td class="text-end">${actionButton}</td>
                `;
        tbody.appendChild(row);
    });
}



async function search(){
    let search = document.getElementById("search").value;
    currentPage = 0;
    totalPages = 0;

    try {
        Notiflix.Loading.pulse("Data Loading...", {
            clickToClose: false,
            svgColor: '#0284c7'
        });
        if(search === null){
            await loadUsers();
        }else{
            clearPagination();
            const response = await fetch(`../api/admin/searchusers?search=${search}`);
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
                loadUsers();
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