async function loadAuthor(){
console.log("aauthors");
    const response = await fetch ("api/basic/authors");

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
{await loadAuthor(); });

