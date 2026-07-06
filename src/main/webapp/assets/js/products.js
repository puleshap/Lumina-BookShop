async function getCategories(){

    const response = await fetch("/api/basic/categories");
if(response.ok){
    alert("tes");

}else{
    alert(response.json());
}
}