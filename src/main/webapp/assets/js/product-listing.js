document.getElementById("product-anchor").addEventListener("click", async ()=>{
    Notiflix.Loading.pulse("Data Loading...", {
        clickToClose: false,
        svgColor: '#0284c7'
    });

    try {
        await loadBrands();
        await loadProductSpecification();
    } finally {
        Notiflix.Loading.remove();
    }
});

async function loadBrands(){
    try{
        const response = await fetch("api/data/brands");

        if (response.ok){
            const data = await response.json();

            const brandSelect = document.getElementById("brandSelect");
            brandSelect.innerHTML = `<option value="0">Select</option>`;

            data.brands.forEach((brand) =>{
                const option = document.createElement("option");
                option.value = brand.id;
                option.innerHTML = brand.name;
                brandSelect.appendChild(option);
            });
        } else {
            Notiflix.Notify.failure("Brand loading failed!", {
                position: 'center-top'
            });
        }

    } catch (e) {
        Notiflix.Notify.failure(e.message, {
            position: 'center-top'
        });
    }
}

async function loadModels(){
    Notiflix.Loading.pulse("Wait...", {
        clickToClose: false,
        svgColor: '#0284c7'
    });

    const brandSelect = document.getElementById("brandSelect");

    try{
        const response = await fetch(`api/data/${brandSelect.value}/models`);

        if (response.ok){
            const data = await response.json();

            if (data.status){
                const modelSelect = document.getElementById("model");
                modelSelect.innerHTML = `<option value="0">Select</option>`;

                data.models.forEach((model) =>{
                    const option = document.createElement("option");
                    option.value = model.id;
                    option.innerHTML = model.name;
                    modelSelect.appendChild(option);
                });
            } else {
                Notiflix.Notify.failure(data.message, {
                    position: 'center-top'
                });
            }
        } else {
            Notiflix.Notify.failure("Model loading failed!", {
                position: 'center-top'
            });
        }
    } catch (e) {
        Notiflix.Notify.failure(e.message, {
            position: 'center-top'
        });
    } finally {
        Notiflix.Loading.remove(1000);
    }
}

async function loadProductSpecification(){
    try{
        const response = await fetch("api/data/specifications");

        if (response.ok){
            const data = await response.json();
            const storageSelect = document.getElementById("storage");
            const colorSelect = document.getElementById("color");
            const qualitySelector = document.getElementById("condition");

            renderDropdowns(storageSelect, data.storages, 'value');
            renderDropdowns(colorSelect, data.colors, 'value');
            renderDropdowns(qualitySelector, data.qualities, 'value');
        } else {
            Notiflix.Notify.failure("Specification loading failed!", {
                position: 'center-top'
            });
        }

    } catch (e) {
        Notiflix.Notify.failure(e.message, {
            position: 'center-top'
        });
    }
}

function renderDropdowns(selector, list, suffix){
    selector.innerHTML = `<option value="0">Select</option>`;
    list.forEach((item)=>{
        const option = document.createElement("option");
        option.value = item.id;
        option.innerHTML = item[suffix];
        selector.appendChild(option);
    })
}

async function saveProduct(){
    Notiflix.Loading.pulse("Wait...", {
        clickToClose: false,
        svgColor: '#0284c7'
    });

    let brandSelect = document.getElementById("brandSelect");
    let modelSelect = document.getElementById("model");
    let title = document.getElementById("title");
    let description = document.getElementById("description");
    let storageSelect = document.getElementById("storage");
    let colorSelect = document.getElementById("color");
    let conditionSelect = document.getElementById("condition");
    let price = document.getElementById("price");
    let qty = document.getElementById("qty");
    let img1 = document.getElementById("img1");
    let img2 = document.getElementById("img2");
    let img3 = document.getElementById("img3");

    const productDataObj= {
        brandId:brandSelect.value,
        modelId:modelSelect.value,
        title:title.value,
        description:description.value,
        storageId:storageSelect.value,
        colorId:colorSelect.value,
        qualityId:conditionSelect.value,
        price:parseFloat(price.value),
        qty:parseInt(qty.value)
    };

    const formData = new FormData();
    formData.append("product", JSON.stringify(productDataObj));
    formData.append("images[]", img1.files[0]);
    formData.append("images[]", img2.files[0]);
    formData.append("images[]", img3.files[0]);

    try{
        const response = await fetch("api/products/save-product", {
            method:"POST",
            body:formData
        });

        if (response.ok){
            const data = await response.json();
            // console.log(data);
            if(data.status){
                await uploadProductImages(data.productId);
            }else {
                Notiflix.Notify.failure(data.message, {
                    position: 'center-top'
                });
            }
        } else {
            Notiflix.Notify.failure("Product saving failed!", {
                position: 'center-top'
            });
        }
    } catch (e) {
        Notiflix.Notify.failure(e.message, {
            position: 'center-top'
        });
    } finally {
        Notiflix.Loading.remove(1000);
    }
}

async function uploadProductImages(productId){
    let img1 = document.getElementById("img1");
    let img2 = document.getElementById("img2");
    let img3 = document.getElementById("img3");

    const formData = new FormData();
    formData.append("images[]", img1.files[0]);
    formData.append("images[]", img2.files[0]);
    formData.append("images[]", img3.files[0]);
    try{
        const response = await fetch(`api/products/${productId}/upload-images`,{
            method: "PUT",
            body: formData
        });

        if(response.ok){
            const data = await response.json();
            if (data.status){
                Notiflix.Report.success(
                    'SmartTrade',
                    data.message,
                    'Okay',
                );
            } else {
                Notiflix.Notify.failure(data.message, {
                    position: 'center-top'
                });
            }
        }else{
            Notiflix.Notify.failure("Product image uploading failed", {
                position: 'center-top'
            });
        }

    } catch (e) {
        Notiflix.Notify.failure(e.message, {
            position: 'center-top'
        });
    }
}

document.getElementById("product-listing-anchor").addEventListener("click", async ()=>{
    Notiflix.Loading.pulse("Data Loading...", {
        clickToClose: false,
        svgColor: '#0284c7'
    });

    try {
        await loadAllProducts();
    } finally {
        Notiflix.Loading.remove();
    }
});

async function loadAllProducts(){
    try {
        const tbody = document.getElementById("product-listing-tbody");
        tbody.innerHTML = "";
        const response = await fetch("api/products/all");

        if (response.ok){
            const data = await response.json();

            let no = 1;
            data.products.forEach((product)=>{
                product.stockDTOList.forEach((stock)=>{
                    tbody.innerHTML += ` <tr>
                                                            <th scope="row">#${no}</th>
                                                            <td>${product.title}</td>
                                                            <td>Rs ${stock.price}</td>
                                                            <td>${stock.qty}</td>
                                                            <td>${stock.createdAt}</td>
                                                            <td><a href="#" class="axil-btn view-btn">View</a></td>
                                                        </tr>`;

                    no++;
                });
            });
        } else {
            Notiflix.Notify.failure("Product data loading failed!", {
                position: 'center-top'
            });
        }
    } catch (e) {
        Notiflix.Notify.failure(e.message, {
            position: 'center-top'
        });
    }
}