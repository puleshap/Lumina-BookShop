



function changeView() {
    var signUpBox = document.getElementById("signUpBox");
    var signInBox = document.getElementById("signInBox");

    signUpBox.classList.toggle("d-none");
    signInBox.classList.toggle("d-none");
}
function changes() {

    const passwordField = document.getElementById("password");
    const eyeIcon = document.getElementById("eye");

    // Toggle the type attribute
    const isPassword = passwordField.getAttribute("type") === "password";
    passwordField.setAttribute("type", isPassword ? "text" : "password");

    // Toggle the Bootstrap icon classes
    eyeIcon.classList.toggle("bi-eye-fill");
    eyeIcon.classList.toggle("bi-eye-slash-fill");



}
function change1() {

    var text = document.getElementById("password");
    var eye = document.getElementById("eye");

    if (text.type == "password") {

        text.type = "text";
        eye.className = "bi bi-eye-fill";

    } else {

        text.type = "password";
        eye.className = "bi-eye-slash-fill";
    }



}
function change(x) {

    var text = document.getElementById("password2");
    var eye = document.getElementById("eye2");

    if (text.type == "password") {

        text.type = "text";
        eye.className = "bi bi-eye-fill";

    } else {

        text.type = "password";
        eye.className = "bi-eye-slash-fill";
    }



}

function signup() {
    var fname = document.getElementById("fname");
    var lname = document.getElementById("lname");
    var email = document.getElementById("email");
    var password = document.getElementById("password");
    var mobile = document.getElementById("mobile");
    var gender = document.getElementById("gender");

    var form = new FormData();
    form.append("f", fname.value);
    form.append("l", lname.value);
    form.append("e", email.value);
    form.append("p", password.value);
    form.append("m", mobile.value);
    form.append("g", gender.value);

    var request = new XMLHttpRequest();

    request.onreadystatechange = function () {
        if (request.status == 200 & request.readyState == 4) {
            var response = request.responseText;

            if (response == "success") {
                document.getElementById("msg").innerHTML = "Registration Successfull";
                document.getElementById("msg").className = "alert alert-success";
                document.getElementById("msgdiv").className = "d-block";
                document.getElementById("alertdiv").className = "alert alert-success";

                setTimeout(function () {
                    window.location.reload();
                }, 3000);
            } else {
                document.getElementById("msg").innerHTML = "  " + response;
                document.getElementById("msgdiv").className = "d-block";
            }

        }
    }

    request.open("POST", "signupProcess.php", true);
    request.send(form);
}

// function signin() {
//
//     var email = document.getElementById("email2");
//     var password = document.getElementById("password2");
//     var rememberme = document.getElementById("rememberme");
//
//     var form = new FormData();
//     form.append("e", email.value);
//     form.append("p", password.value);
//     form.append("r", rememberme.checked);
//
//     var request = new XMLHttpRequest();
//
//     request.onreadystatechange = function () {
//         if (request.status == 200 & request.readyState == 4) {
//             var response = request.responseText;
//
//             if (response == "success") {
//                 window.location = "home.php";
//             } else {
//                 document.getElementById("msg1").innerHTML = response;
//                 document.getElementById("msgdiv1").className = "d-block";
//             }
//
//         }
//     }
//
//     request.open("POST", "signInProcess.php", true);
//     request.send(form);
//
// }

var forgotPasswordModal;
const loadingOverlay = document.getElementById('loadingOverlay');

function showLoading() {
    loadingOverlay.style.display = 'flex';
}


function hideLoading() {
    loadingOverlay.style.display = 'none';
}

function forgotPassword() {
    showLoading();
    var email = document.getElementById("email2");

    var request = new XMLHttpRequest();

    request.onreadystatechange = function () {
        if (request.status == 200 & request.readyState == 4) {
            var text = request.responseText;

            if (text == "Success") {
                alert("Verification code has sent successfully. Please check your Email.");
                var modal = document.getElementById("fpmodal");
                forgotPasswordModal = new bootstrap.Modal(modal);
                forgotPasswordModal.show();
                hideLoading();
            } else {
                document.getElementById("msg1").innerHTML = text;
                document.getElementById("msgdiv1").className = "d-block";
                hideLoading();
            }

        }
    }

    request.open("GET", "forgotPasswordProcess.php?e=" + email.value, true);
    request.send();

}

function showPassword1() {

    var textfield = document.getElementById("np");
    var button = document.getElementById("npb");

    if (textfield.type == "password") {
        textfield.type = "text";
        button.innerHTML = "Hide";
    } else {
        textfield.type = "password";
        button.innerHTML = "Show";
    }

}

function showPassword2() {

    var textfield = document.getElementById("rnp");
    var button = document.getElementById("rnpb");

    if (textfield.type == "password") {
        textfield.type = "text";
        button.innerHTML = "Hide";
    } else {
        textfield.type = "password";
        button.innerHTML = "Show";
    }

}

function resetPassword() {

    var email = document.getElementById("email2");
    var newPassword = document.getElementById("np");
    var retypePassword = document.getElementById("rnp");
    var verification = document.getElementById("vcode");

    var form = new FormData();
    form.append("e", email.value);
    form.append("n", newPassword.value);
    form.append("r", retypePassword.value);
    form.append("v", verification.value);

    var request = new XMLHttpRequest();

    request.onreadystatechange = function () {
        if (request.status == 200 & request.readyState == 4) {
            var response = request.responseText;
            if (response == "success") {
                alert("Password updated successfully.");
                forgotPasswordModal.hide();
            } else {
                alert(response);
            }
        }
    }

    request.open("POST", "resetPasswordProcess.php", true);
    request.send(form);

}
function togglecolor(clr) {
    var btn = document.getElementById("clr_button");
    var form = new FormData();
    form.append("e", clr);


    var request = new XMLHttpRequest();

    request.onreadystatechange = function () {
        if (request.status == 200 & request.readyState == 4) {
            var response = request.responseText;

            window.location.reload();

        }
    }

    request.open("POST", "changecookie.php", true);
    request.send(form);


}
function signout() {

    var request = new XMLHttpRequest();

    request.onreadystatechange = function () {
        if (request.status == 200 & request.readyState == 4) {
            var response = request.responseText;
            if (response == "success") {
                window.location.reload();
            }
        }
    }

    request.open("GET", "signOutProcess.php", true);
    request.send();

}


function changeProfileImg() {

    var img = document.getElementById("profileimage");

    img.onchange = function () {
        if (confirm("Confirm Updating your Profile Picture")) {
            var file = this.files[0];
            var url = window.URL.createObjectURL(file);

            document.getElementById("img").src = url;
            alert("Apply Profile Picture Change.");
        }
    }

}

function updateProfile() {
    if (confirm("Confirm Updating your Personal Information")) {
        var fname = document.getElementById("fname");
        var lname = document.getElementById("lname");
        var mobile = document.getElementById("mobile");
        var line1 = document.getElementById("line1");
        var line2 = document.getElementById("line2");
        var province = document.getElementById("province");
        var district = document.getElementById("district");
        var city = document.getElementById("city");
        var pcode = document.getElementById("pcode");
        var image = document.getElementById("profileimage");

        var form = new FormData();

        form.append("f", fname.value);
        form.append("l", lname.value);
        form.append("m", mobile.value);
        form.append("l1", line1.value);
        form.append("l2", line2.value);
        form.append("p", province.value);
        form.append("d", district.value);
        form.append("c", city.value);
        form.append("pc", pcode.value);
        form.append("i", image.files[0]);

        var request = new XMLHttpRequest();

        request.onreadystatechange = function () {
            if (request.status == 200 & request.readyState == 4) {
                var response = request.responseText;

                if (response == "Updated" || response == "Saved") {
                    window.location.reload();
                } else if (response == "You have not selected any image.") {

                    window.location.reload();
                } else {
                    alert(response);
                }

            }
        }

        request.open("POST", "updateProfileProcess.php", true);
        request.send(form);
    }
    alert("Update Complete!")

}

function addProduct() {
    var category = document.getElementById("category");
    var brand = document.getElementById("brand");
    var model = document.getElementById("model");
    var title = document.getElementById("title");
    var condition = 0;

    if (document.getElementById("b").checked) {
        condition = 1;
    } else if (document.getElementById("u").checked) {
        condition = 2;
    }


    var qty = document.getElementById("qty");
    var cost = document.getElementById("cost");
    var dwc = document.getElementById("dwc");
    var doc = document.getElementById("doc");
    var desc = document.getElementById("desc");
    var image = document.getElementById("imageInput");

    var set;

    if (category.value == 0) {
        alert("Please select a category");
        set = 1;
    }
    if (brand.value == 0) {
        alert("Please select a brand");
        set = 1;
    }
    if (model.value == 0) {
        alert("Please select a model");
        set = 1;
    }
    if (title.value == "") {
        alert("Please Enter Title");
        set = 1;
    }
    if (qty.value == "") {
        alert("Please Enter Quantity");
        set = 1;
    }
    if (cost.value == "") {
        alert("Please Enter Cost");
        set = 1;
    }
    if (desc.value == "") {
        alert("Please Enter Description");
        set = 1;
    }
    var file_count = image.files.length;
    if (file_count == 0 || file_count >= 8) {
        set = 1;
        alert("Invalid Number of Images");
    }
    if (set !== 1) {
        var form = new FormData();
        form.append("ca", category.value);
        form.append("b", brand.value);
        form.append("m", model.value);
        form.append("t", title.value);
        form.append("con", condition);

        form.append("q", qty.value);
        form.append("co", cost.value);
        form.append("dwc", dwc.value);
        form.append("doc", doc.value);
        form.append("de", desc.value);



        for (var x = 0; x < file_count; x++) {
            form.append("image" + x, image.files[x]);
        }

        var request = new XMLHttpRequest();

        request.onreadystatechange = function () {
            if (request.status == 200 & request.readyState == 4) {
                var response = request.responseText;

                if (response == "success") {
                    alert("Product Saved Successfully.");
                    window.location = 'myProducts.php';
                } else {
                    alert(response);
                }
            }
        }

        request.open("POST", "addProductProcess.php", true);
        request.send(form);
    }
}

function changeProductImage() {

    var image = document.getElementById("imageuploader");

    image.onchange = function () {
        var file_count = image.files.length;

        if (file_count <= 3) {

            for (var x = 0; x < file_count; x++) {

                var file = this.files[x];
                var url = window.URL.createObjectURL(file);

                document.getElementById("i" + x).src = url;
            }

        } else {
            alert(file_count + " files. You are proceed to upload only 3 or less than 3 files.");
        }
    }

}

function changeStatus(id) {
    var product_id = id;

    var request = new XMLHttpRequest();

    request.onreadystatechange = function () {
        if (request.status == 200 & request.readyState == 4) {
            var response = request.responseText;
            if (response == "deactivated" || response == "activated") {
                alert("status changed")
                window.location.reload();
            } else {
                alert(response);
            }
        }
    }

    request.open("GET", "changeStatusProcess.php?id=" + product_id, true);
    request.send();

}

function sort1(x) {

    var search = document.getElementById("s");
    var time = "0";

    if (document.getElementById("n").checked) {
        time = "1";
    } else if (document.getElementById("o").checked) {
        time = "2";
    }

    var qty = "0";

    if (document.getElementById("h").checked) {
        qty = "1";
    } else if (document.getElementById("l").checked) {
        qty = "2";
    }

    var condition = "0";

    if (document.getElementById("b").checked) {
        condition = "1";
    } else if (document.getElementById("u").checked) {
        condition = "2";
    }

    var form = new FormData();
    form.append("s", search.value);
    form.append("t", time);
    form.append("q", qty);
    form.append("c", condition);
    form.append("page", x);

    var request = new XMLHttpRequest();

    request.onreadystatechange = function () {
        if (request.status == 200 & request.readyState == 4) {
            var response = request.responseText;
            document.getElementById("sort").innerHTML = response;
        }
    }

    request.open("POST", "sortProcess.php", true);
    request.send(form);
}

function clearSort() {
    window.location.reload();
}

function sendid(id) {

    var request = new XMLHttpRequest();

    request.onreadystatechange = function () {
        if (request.status == 200 & request.readyState == 4) {
            var response = request.responseText;

            if (response == "Success") {
                window.location = "updateProduct.php";
            } else {
                alert(response);
            }
        }
    }

    request.open("GET", "sendIdProcess.php?id=" + id, true);
    request.send();

}


function updateProduct() {


    var title = document.getElementById("t");
    var price = document.getElementById("new_value");
    var qty = document.getElementById("q");
    var dwc = document.getElementById("dwc");
    var doc = document.getElementById("doc");
    var description = document.getElementById("d");
    var images = document.getElementById("imageInput");

    var form = new FormData();
    form.append("t", title.value);
    form.append("q", qty.value);
    form.append("price", price.value);
    form.append("dwc", dwc.value);
    form.append("doc", doc.value);
    form.append("d", description.value);

    var file_count = images.files.length;

    for (var x = 0; x < file_count; x++) {
        form.append("i" + x, images.files[x]);
    }

    var request = new XMLHttpRequest();

    request.onreadystatechange = function () {
        if (request.status == 200 & request.readyState == 4) {
            var response = request.responseText;
            if (response == "Product has been Updated.") {


                window.location = "myProducts.php";
            } else {
                alert(response);
            }

        }
    }

    request.open("POST", "updateProductProcess.php", true);
    request.send(form);

}

function basicSearch(x) {

    var txt = document.getElementById("basic_search_txt");
    var select = document.getElementById("basic_search_select");

    var form = new FormData();
    form.append("t", txt.value);
    form.append("s", select.value);
    form.append("page", x);

    var request = new XMLHttpRequest();

    request.onreadystatechange = function () {
        if (request.status == 200 & request.readyState == 4) {
            var response = request.responseText;
            document.getElementById("basicSearchResult").innerHTML = response;
        }
    }

    request.open("POST", "basicSearchProcess.php", true);
    request.send(form);

}
function SearchType(x) {

    var txt = document.getElementById("basic_search_txt");

    var num = 1;
    var form = new FormData();
    form.append("t", txt.value);
    form.append("s", num);
    form.append("page", x);

    var request = new XMLHttpRequest();

    request.onreadystatechange = function () {
        if (request.status == 200 & request.readyState == 4) {
            var response = request.responseText;
            if (response == "Please type a product name") {
                alert("Please type a product name");
            }
            document.getElementById("main-content").innerHTML = "";
            document.getElementById("main-content").innerHTML = response;
        }
    }

    request.open("POST", "basicSearchProcess.php", true);
    request.send(form);

}

function SearchCat(x) {

    var txt = document.getElementById("cate");
    alert(txt.innerHTML);

    // var num = 1;
    // var form = new FormData();
    // form.append("t", txt.value);
    // form.append("s", num);
    // form.append("page", x);

    // var request = new XMLHttpRequest();

    // request.onreadystatechange = function () {
    //     if (request.status == 200 & request.readyState == 4) {
    //         var response = request.responseText;
    //         if (response == "Please type a product name") {
    //             alert("Please type a product name");
    //         }
    //         document.getElementById("main-content").innerHTML = "";
    //         document.getElementById("main-content").innerHTML = response;
    //     }
    // }

    // request.open("POST", "catshow.php", true);
    // request.send(form);

}
function SearchType2(x) {
    var txt = document.getElementById("basic_search_txt");

    var num = 1;
    var form = new FormData();
    form.append("t", txt.value);
    form.append("s", num);
    form.append("page", x);

    var request = new XMLHttpRequest();

    request.onreadystatechange = function () {
        if (request.status == 200 && request.readyState == 4) {
            var response = request.responseText;
            if (response == "Please type a product name") {
                alert("Please type a product name");
            } else {
                // Change the URL to Test3.php without reloading the page
                window.location = "Test3.php";

                // Ensure the main-content div is available on Test3.php
                var mainContent = document.getElementById("main-content");


                // Load the response into the main-content div
                mainContent.innerHTML = response;

                // If main-content div doesn't exist, redirect to Test3.php with a full page load


            }
        }
    }

    request.open("POST", "basicSearchProcess.php", true);
    request.send(form);
}

function loadcat(x) {
    alert("Please enter" + x);

    var request = new XMLHttpRequest();

    request.onreadystatechange = function () {
        if (request.status == 200 & request.readyState == 4) {
            var response = request.responseText;
            if (response == "Please type a product name") {
                alert("Please type a product name");
            }
            document.getElementById("main-content").innerHTML = response;
        }
    }

    request.open("GET", "basicSearchProcess.php?id=" + x, true);
    request.send();
}
function load_brand() {
    var category = document.getElementById("category").value;

    var r = new XMLHttpRequest();

    r.onreadystatechange = function () {
        if (r.readyState == 4) {
            var t = r.responseText;
            document.getElementById("brand").innerHTML = t;

        }
    }

    r.open("GET", "loadBrand.php?c=" + category, true);
    r.send();
}

function load_model() {
    var brand = document.getElementById("brand").value;

    var r = new XMLHttpRequest();

    r.onreadystatechange = function () {
        if (r.readyState == 4) {
            var t = r.responseText;
            document.getElementById("model").innerHTML = t;

        }
    }

    r.open("GET", "loadModel.php?c=" + brand, true);
    r.send();
}

function advancedSearch(x) {
    var txt = document.getElementById("basic_search_txt");
    var category = document.getElementById("category");
    var brand = document.getElementById("brand");
    var model = document.getElementById("model");
    var condition = document.getElementById("c2");
    var from = document.getElementById("pf");
    var to = document.getElementById("pt");
    var sort = document.getElementById("s");
    var form = new FormData();
    form.append("t", txt.value);
    form.append("cat", category.value);
    form.append("b", brand.value);
    form.append("m", model.value);
    form.append("con", condition.value);
    form.append("pf", from.value);
    form.append("pt", to.value);
    form.append("s", sort.value);
    form.append("page", x);
    var request = new XMLHttpRequest();
    request.onreadystatechange = function () {
        if (request.status == 200 && request.readyState == 4) {
            var response = request.responseText;
            document.getElementById("main-content").innerHTML = response;
        }
    }
    request.open("POST", "advancedSearchProcess.php", true);
    request.send(form);
}

function loadMainImg(id) {

    var sample_img = document.getElementById("productImg" + id).src;
    var main_img = document.getElementById("mainImg");
    main_img.innerHTML = "";
    main_img.style.backgroundImage = "url(" + sample_img + ")";
}

function check_value(qty) {

    var input = document.getElementById("qty_input");

    if (input.value <= 0) {
        alert("Quantity must be 01 or more.");
        input.value = 1;
    } else if (input.value > qty) {
        alert("Insufficient Quantity.");
        input.value = qty;
    }

}

function qty_inc(qty) {
    var input = document.getElementById("qty_input");

    if (input.value < qty) {
        var newValue = parseInt(input.value) + 1;
        input.value = newValue;
    } else {
        alert("Maximum quantity has achieved.");
        input.value = qty;
    }

}

function qty_dec() {
    var input = document.getElementById("qty_input");

    if (input.value > 1) {
        var newValue = parseInt(input.value) - 1;
        input.value = newValue;
    } else {
        alert("Minimum quantity has achieved.");
        input.value = 1;
    }
}

function addToWatchlist(id) {

    var request = new XMLHttpRequest();

    request.onreadystatechange = function () {
        if (request.status == 200 & request.readyState == 4) {
            var response = request.responseText;

            if (response == "added") {
                document.getElementById("heart" + id).style.className = "text-danger";
                alert('Product has been added to the Wishlist!');
            } else if (response == "removed") {
                document.getElementById("heart" + id).style.className = "text-dark";
                alert('Product has been removed from the Wishlist!');
            } else {
                alert(response);
            }

        }
    }

    request.open("GET", "addToWatchlistProcess.php?id=" + id, true);
    request.send();

}
function addToWatchlistSearch(id) {

    var request = new XMLHttpRequest();

    request.onreadystatechange = function () {
        if (request.status == 200 & request.readyState == 4) {
            var response = request.responseText;

            if (response == "added") {
                document.getElementById("heart" + id).style.className = "text-danger";
                alert("Added product to the watchlist");
            } else if (response == "removed") {
                document.getElementById("heart" + id).style.className = "text-dark";
                alert("Removed the product from the watchlist");
            } else {
                alert(response);
            }

        }
    }

    request.open("GET", "addToWatchlistProcess.php?id=" + id, true);
    request.send();

}

function removeFromWatchlist(id) {

    var request = new XMLHttpRequest();

    request.onreadystatechange = function () {
        if (request.status == 200 && request.readyState == 4) {
            var response = request.responseText;
            if (response == "success") {
                alert("Succesfully removed from watchlist.");
                window.location.reload();
            } else {
                alert(response);
            }
        }
    }

    request.open("GET", "removeWatchlistProcess.php?id=" + id, true);
    request.send();

}

// function addToCarts(id) {
//
//     var request = new XMLHttpRequest();
//
//     request.onreadystatechange = function () {
//         if (request.readyState == 4) {
//             var response = request.responseText;
//             alert(response);
//         }
//     }
//
//     request.open("GET", "addToCartProcess.php?id=" + id, true);
//     request.send();
//
// }

function changeQTY(id) {
    var qty = document.getElementById("qty_num").value;

    var request = new XMLHttpRequest();

    request.onreadystatechange = function () {
        if (request.status == 200 & request.readyState == 4) {
            var response = request.responseText;
            if (response == "Updated") {
                alert(response);
                window.location.reload();
            } else if (response == "Number of items is not available") {
                alert(response);
                window.location.reload();
            } else {

                alert(response);
            }
        }
    }

    request.open("GET", "cartQtyUpdateProcess.php?qty=" + qty + "&id=" + id, true);
    request.send();

}

function deleteFromCart(id) {

    var request = new XMLHttpRequest();

    request.onreadystatechange = function () {
        if (request.status == 200 & request.readyState == 4) {
            var response = request.responseText;
            if (response == "Removed") {
                alert("Product removed from Cart.");
                window.location.reload();
            } else {
                alert(response);
            }
        }
    }

    request.open("GET", "deleteFromCartProcess.php?id=" + id, true);
    request.send();

}

function payNow(id) {

    var qty = document.getElementById("qty_input").value;

    var request = new XMLHttpRequest();

    request.onreadystatechange = function () {
        if (request.status == 200 & request.readyState == 4) {
            var response = request.responseText;
            alert(response);
            var obj = JSON.parse(response);

            var mail = obj["umail"];
            var amount = obj["amount"];

            if (response == 1) {
                alert("Please Login.");
                window.location = "index.php";
            } else if (response == 2) {
                alert("Please update your profile.");
                window.location = "userProfile.php";
            } else {

                // Payment completed. It can be a successful failure.
                payhere.onCompleted = function onCompleted(orderId) {
                    console.log("Payment completed. OrderID:" + orderId);

                    alert("Payment completed. OrderID:" + orderId);
                    saveInvoice(orderId, id, mail, amount, qty);

                };

                // Payment window closed
                payhere.onDismissed = function onDismissed() {
                    // Note: Prompt user to pay again or show an error page
                    console.log("Payment dismissed");
                };

                // Error occurred
                payhere.onError = function onError(error) {
                    // Note: show an error page
                    console.log("Error:" + error);
                };

                // Put the payment variables here
                var payment = {
                    "sandbox": true,
                    "merchant_id": obj["mid"],    // Replace your Merchant ID
                    "return_url": "http://localhost/eshop/singleProductView.php?id=" + id,     // Important
                    "cancel_url": "http://localhost/eshop/singleProductView.php?id=" + id,     // Important
                    "notify_url": "http://sample.com/notify",
                    "order_id": obj["id"],
                    "items": obj["item"],
                    "amount": amount + ".00",
                    "currency": "LKR",
                    "hash": obj["hash"], // *Replace with generated hash retrieved from backend
                    "first_name": obj["fname"],
                    "last_name": obj["lname"],
                    "email": mail,
                    "phone": obj["mobile"],
                    "address": obj["address"],
                    "city": obj["city"],
                    "country": "Sri Lanka",
                    "delivery_address": obj["address"],
                    "delivery_city": obj["city"],
                    "delivery_country": "Sri Lanka",
                    "custom_1": "",
                    "custom_2": ""
                };

                // Show the payhere.js popup, when "PayHere Pay" is clicked
                // document.getElementById('payhere-payment').onclick = function (e) {
                payhere.startPayment(payment);
                // };

            }

        }
    }

    request.open("GET", "buyNowProcess.php?id=" + id + "&qty=" + qty, true);
    request.send();
}



function saveInvoice(orderId, id, mail, amount, qty) {

    var form = new FormData();

    form.append("o", orderId);
    form.append("i", id);
    form.append("m", mail);
    form.append("a", amount);
    form.append("q", qty);

    var request = new XMLHttpRequest();

    request.onreadystatechange = function () {
        if (request.status == 200 & request.readyState == 4) {
            var response = request.responseText;

            if (response == "success") {
                window.location = "invoice.php?id=" + orderId;
            } else {
                alert(response);
            }
        }
    }

    request.open("POST", "saveInvoiceProcess.php", true);
    request.send(form);

}


function checkout(shipping, value, number, total) {
    var itemNames = [];
    var itemInputs = document.querySelectorAll('input[name="item_names[]"]');
    itemInputs.forEach(function (input) {
        itemNames.push(input.value);
    });
    var itemqty = [];
    var itemInputs = document.querySelectorAll('input[name="item_qty[]"]');
    itemInputs.forEach(function (input) {
        itemqty.push(input.value);
    });
    var itemid = [];
    var itemInput = document.querySelectorAll('input[name="item_id[]"]');
    itemInput.forEach(function (input) {
        itemid.push(input.value);
    });

    var itemprice = [];
    var itemInput = document.querySelectorAll('input[name="item_price[]"]');

    itemInput.forEach(function (input) {
        itemprice.push(input.value);
    });

    var request = new XMLHttpRequest();
    var f = new FormData();
    f.append("shipping", shipping)
    f.append("value", value)
    f.append("number", number)
    f.append("total", total)
    f.append('itemPrice', JSON.stringify(itemprice));
    f.append('itemNames', JSON.stringify(itemNames));
    f.append('itemNames1', itemNames);
    f.append('itemqty', JSON.stringify(itemqty));



    request.onreadystatechange = function () {
        if (request.readyState == 4) {
            var response = request.responseText;
            alert(response);
            var obj = JSON.parse(response);

            var mail = obj["umail"];
            var amount = obj["amount"];
            var names = obj["item"];
            var shipping = obj["shipping"];

            if (response == 1) {
                alert("Please Login.");
                window.location = "index.php";
            } else if (response == 2) {
                alert("Please update your profile.");
                window.location = "userProfile.php";

                //Payhere Setup
            } else {

                // Payment completed. It can be a successful failure.
                payhere.onCompleted = function onCompleted(orderId) {
                    console.log("Payment completed. OrderID:" + orderId);
                    alert("Payment completed. OrderID:" + orderId);
                    saveCartInvoice(orderId, total, itemid, shipping, value, itemqty, names, shipping);




                };

                // Payment window closed
                payhere.onDismissed = function onDismissed() {
                    // Note: Prompt user to pay again or show an error page
                    console.log("Payment dismissed");
                };

                // Error occurred
                payhere.onError = function onError(error) {
                    // Note: show an error page
                    console.log("Error:" + error);
                };

                // Put the payment variables here
                var payment = {
                    "sandbox": true,
                    "merchant_id": obj["mid"],    // Replace your Merchant ID
                    "return_url": "http://localhost/eshop/singleProductView.php",     // Important
                    "cancel_url": "http://localhost/eshop/singleProductView.php?",     // Important
                    "notify_url": "http://sample.com/notify",
                    "order_id": obj["id"],
                    "items": obj["item"],
                    "amount": amount + ".00",
                    "currency": "LKR",
                    "hash": obj["hash"], // *Replace with generated hash retrieved from backend
                    "first_name": obj["fname"],
                    "last_name": obj["lname"],
                    "email": mail,
                    "phone": obj["mobile"],
                    "address": obj["address"],
                    "city": obj["city"],
                    "country": "Sri Lanka",
                    "delivery_address": obj["address"],
                    "delivery_city": obj["city"],
                    "delivery_country": "Sri Lanka",
                    "custom_1": "",
                    "custom_2": ""
                };

                // Show the payhere.js popup, when "PayHere Pay" is clicked
                // document.getElementById('payhere-payment').onclick = function (e) {
                payhere.startPayment(payment);
                // };

            }

        }
    }


    request.open("POST", "CheckoutProcess.php", true);
    request.send(f);

}



function saveCartInvoice(o_id, total, id, shipping, value, number, name, shipping) {


    var form = new FormData();
    form.append('itemNames', JSON.stringify(id));
    form.append("oid", o_id);
    form.append("o", JSON.stringify(number));
    form.append("i", id);
    form.append("m", shipping);
    form.append("a", value);
    form.append("q", total);
    form.append("n", name);
    form.append("s", shipping);

    var request = new XMLHttpRequest();

    request.onreadystatechange = function () {
        if (request.readyState == 4) {
            var response = request.responseText;
            if (response == 'success') {
                window.location = "invoicecart.php?id=" + o_id;




            } else { alert(response); }




        }
    }

    request.open("POST", "saveCartInvoiceProcess.php", true);
    request.send(form);

}
function printInvoice() {
    var restorePage = document.body.innerHTML;
    var page = document.getElementById("page").innerHTML;
    document.body.innerHTML = page;
    window.print();

    document.body.innerHTML = restorePage;
}
function exportpdf() {
    var element = document.getElementById("page");

    var opt = {
        margin: [0.5, 0.5, 0.5, 0.5], // Adjust margins as needed
        filename: 'receipt.pdf',
        image: { type: 'jpeg', quality: 0.1 },
        html2canvas: { scale: 10, useCORS: true, letterRendering: true }, // Adjust scale and useCORS for better image quality
        jsPDF: { unit: 'in', format: 'letter', orientation: 'portrait' }
    };

    html2pdf().from(element).set(opt).save();
}


var as;
function addFeedback(id) {
    var label = document.getElementById("prod");
    label.innerHTML = id;
    var as;
    var feedbackModal = document.getElementById("feedbackmodal");
    as = new bootstrap.Modal(feedbackModal);
    as.show();
}

var as;
function report(id) {
    var label = document.getElementById("prodd");
    label.innerHTML = id;
    var as;
    var feedbackModal = document.getElementById("report");
    as = new bootstrap.Modal(feedbackModal);
    as.show();
}
function deleteFeedback(id) {
    if (confirm("You will no longer be able to see this invoice, Confirm?")) {
        var request = new XMLHttpRequest();

        request.onreadystatechange = function () {
            if (request.readyState == 4) {
                var response = request.responseText;
                if (response == "Successfully deleted") {
                    alert(response);
                    window.location.reload();
                } else { alert(response); }

            }
        }

        request.open("GET", "DeleteInvoiceProcess.php?id=" + id, true);
        request.send();
    }
}

function deleteMFeedback(id) {

    if (confirm("You will no longer be able to see this invoice, Are you sure?")) {
        var request = new XMLHttpRequest();

        request.onreadystatechange = function () {
            if (request.readyState == 4) {
                var response = request.responseText;
                if (response == "Successfully deleted") {
                    alert(response);
                    window.location.reload();
                } else {
                    alert(response);
                }

            }
        }

        request.open("GET", "DeleteInvoiceProcess.php?idd=" + id, true);
        request.send();
    }
}


function saveFeedback() {
    var id = document.getElementById("prod").innerHTML;
    var type;
    if (document.getElementById("type1").checked) {
        type = 1;
    } else if (document.getElementById("type2").checked) {
        type = 2;
    } else if (document.getElementById("type3").checked) {
        type = 3;
    }
    var feedback = document.getElementById("feed");
    var form = new FormData();
    form.append("pid", id);
    form.append("t", type);
    form.append("f", feedback.value);
    var request = new XMLHttpRequest();
    request.onreadystatechange = function () {
        if (request.status == 200 & request.readyState == 4) {
            var response = request.responseText;
            if (response == "success") {
                alert("Feedback saved.");
                window.location.reload();

            } else {
                alert(response);
            }
        }
    }
    request.open("POST", "saveFeedbackProcess.php", true);
    request.send(form);

}
function savereport() {
    var id = document.getElementById("prodd").innerHTML;
    var feedback = document.getElementById("rep");
    var form = new FormData();
    form.append("pid", id);
    form.append("f", feedback.value);
    var request = new XMLHttpRequest();
    request.onreadystatechange = function () {
        if (request.status == 200 & request.readyState == 4) {
            var response = request.responseText;
            if (response == "success") {
                alert("Report Submitted");
                window.location.reload();
            } else {
                alert(response);
            }
        }
    }
    request.open("POST", "savereportProcess.php", true);
    request.send(form);
}

function send_msg(id) {

    alert(id);
    var recever_mail = "0";

    var r2 = document.getElementById("select_user");

    if (r2.value == 0) {
        alert("Please select a user to message.");
    } else {
        recever_mail = r2.value;

        var msg_txt = document.getElementById("msg_txt");

        var form = new FormData();
        form.append("rm", recever_mail);
        form.append("mt", msg_txt.value);

        var request = new XMLHttpRequest();

        request.onreadystatechange = function () {

            if (request.status == 200 & request.readyState == 4) {

                var response = request.responseText;
                if (response == "success") {
                    alert("Message sent.");
                    window.location.reload();
                    viewMessage(id);
                } else {
                    alert(response);
                }
            }
        }

        request.open("POST", "sendMsgProcess.php", true);
        request.send(form);

    }


}

function sendmessage(email) {

    var form = new FormData();
    form.append("e", email);

    var request = new XMLHttpRequest();


    window.location = "messages.php";




    request.open("POST", "messages.php", true);
    request.send(form);
}
function viewMessage(email) {
    document.getElementById("select_user").value = email;
    var request = new XMLHttpRequest();

    request.onreadystatechange = function () {
        if (request.status == 200 & request.readyState == 4) {
            var response = request.responseText;
            document.getElementById("chat_box").innerHTML = response;
            // alert (response);
        }
    }

    request.open("GET", "viewMsgProcess.php?e=" + email, true);
    request.send();

}

var av;
function adminVerification() {

    var email = document.getElementById("e");

    var form = new FormData();
    form.append("e", email.value);

    var request = new XMLHttpRequest();

    request.onreadystatechange = function () {
        if (request.status == 200 & request.readyState == 4) {
            var response = request.responseText;
            if (response == "Success") {
                alert("Please take a look at your email to find the VERIFICATION CODE.");
                var adminVerificationModal = document.getElementById("verificationModal");
                av = new bootstrap.Modal(adminVerificationModal);
                av.show();
            } else {
                alert(response);
            }

        }
    }

    request.open("POST", "adminVerificationProcess.php", true);
    request.send(form);

}

function verify() {

    var code = document.getElementById("vcode");

    var form = new FormData();
    form.append("c", code.value);

    var request = new XMLHttpRequest();

    request.onreadystatechange = function () {
        if (request.status == 200 & request.readyState == 4) {
            var response = request.responseText;
            if (response == "success") {
                av.hide();
                window.location = "adminPanel.php";
            } else {
                alert(response);
            }

        }
    }

    request.open("POST", "verificationProcess.php", true);
    request.send(form);

}

function blockUser(email) {

    var request = new XMLHttpRequest();

    request.onreadystatechange = function () {
        if (request.status == 200 & request.readyState == 4) {
            var response = request.responseText;
            alert(response);
            AdminShowProductPanel();
        }
    }

    request.open("GET", "userBlockProcess.php?email=" + email, true);
    request.send();

}

var mm;

function BrandModal() {
    var m = document.getElementById("userMsgModal");
    mm = new bootstrap.Modal(m);
    mm.show();
}

function blockProduct(id) {

    var request = new XMLHttpRequest();

    request.onreadystatechange = function () {
        if (request.status == 200 & request.readyState == 4) {
            var response = request.responseText;
            alert(response);
            window.location.reload();
        }
    }

    request.open("GET", "productBlockProcess.php?id=" + id, true);
    request.send();

}

var pm;

function viewProductModal(id) {
    var m = document.getElementById("viewProductModal" + id);
    pm = new bootstrap.Modal(m);
    pm.show();
}

var cm;

function addNewCategory() {
    var m = document.getElementById("addCategoryModal");
    cm = new bootstrap.Modal(m);
    cm.show();
}

var vc;
var e;
var n;

function verifyCategory() {

    var ncm = document.getElementById("addCategoryVerificationModal");
    vc = new bootstrap.Modal(ncm);

    e = document.getElementById("e").value;
    n = document.getElementById("n").value;

    var form = new FormData();
    form.append("email", e);
    form.append("name", n);

    var request = new XMLHttpRequest();

    request.onreadystatechange = function () {
        if (request.status == 200 & request.readyState == 4) {
            var response = request.responseText;
            if (response == "Success") {
                cm.hide();
                vc.show();
            } else {
                alert(response);
            }
        }
    }

    request.open("POST", "addNewCategoryProcess.php", true);
    request.send(form);

}

function saveCategory() {
    var txt = document.getElementById("txt").value;

    var form = new FormData();
    form.append("t", txt);
    form.append("e", e);
    form.append("n", n);

    var request = new XMLHttpRequest();

    request.onreadystatechange = function () {
        if (request.status == 200 & request.readyState == 4) {
            var response = request.responseText;
            if (response == "success") {
                vc.hide();
                window.location.reload();
            } else {
                alert(response);
            }

        }
    }

    request.open("POST", "saveCategoryProcess.php", true);
    request.send(form);
}
function DeactivateCategory(id) {
    if (confirm("Are you sure you want to deactivate this category?")) {
        var request = new XMLHttpRequest();

        request.onreadystatechange = function () {
            if (request.status == 200 & request.readyState == 4) {
                var response = request.responseText;
                if (response == "success") {

                    window.location.reload();
                } else {
                    alert(response);
                }

            }
        }
        request.open("GET", "saveCategoryProcess.php?id=" + id, true);
        request.send();
    }
}

function sendAdminMsg(email) {
    var txt = document.getElementById("msgtxt");

    var form = new FormData();
    form.append("t", txt.value);
    form.append("e", email);

    var request = new XMLHttpRequest();

    request.onreadystatechange = function () {
        if (request.status == 200 & request.readyState == 4) {
            var response = request.responseText;
            alert(response);
        }
    }

    request.open("POST", "sendAdminMsgProcess.php", true);
    request.send(form);

}

var cam;
function contactAdmin() {
    var m = document.getElementById("contactAdmin");
    cam = new bootstrap.Modal(m);
    cam.show();
}

function sendAdminMsg() {
    var txt = document.getElementById("msgtxt");

    var form = new FormData();
    form.append("t", txt.value);

    var request = new XMLHttpRequest();

    request.onreadystatechange = function () {
        if (request.status == 200 & request.readyState == 4) {
            var response = request.responseText;
            alert(response);
        }
    }

    request.open("POST", "sendAdminMsgProcess.php", true);
    request.send(form);

}

function searchInvoice() {
    var txt = document.getElementById("searchtxt").value;

    var request = new XMLHttpRequest();

    request.onreadystatechange = function () {
        if (request.status == 200 & request.readyState == 4) {
            var response = request.responseText;
            document.getElementById("viewArea").innerHTML = response;
        }
    }

    request.open("GET", "searchInvoiceProcess.php?id=" + txt, true);
    request.send();
}

function findsellings() {

    var from = document.getElementById("from").value;
    var to = document.getElementById("to").value;

    var request = new XMLHttpRequest();

    request.onreadystatechange = function () {
        if (request.status == 200 & request.readyState == 4) {
            var response = request.responseText;
            document.getElementById("viewArea").innerHTML = response;
        }
    }

    request.open("GET", "findSellingsProcess.php?f=" + from + "&t=" + to, true);
    request.send();

}

function changeInvoiceStatus(id) {

    var request = new XMLHttpRequest();

    request.onreadystatechange = function () {
        if (request.status == 200 & request.readyState == 4) {
            var response = request.responseText;
            if (response == "success") {
                window.location.reload();
            } else {
                alert(response);
            }
        }
    }

    request.open("GET", "changeInvoiceStatusProcess.php?id=" + id, true);
    request.send();

}

function aceptsale(id) {


    var request = new XMLHttpRequest();

    request.onreadystatechange = function () {
        if (request.status == 200 & request.readyState == 4) {
            var response = request.responseText;
            if (response == "success") {
                window.location.reload();
            } else {
                alert(response);
            }
        }
    }

    request.open("GET", "AceptSaleProcess.php?id=" + id, true);
    request.send();

}


function uploadImagesProducts() {

    var imageInput = document.getElementById('imageInput');
    var imagePreviewContainer = document.getElementById('imagePreviewContainer');
    var file_count = imageInput.files.length;

    imagePreviewContainer.innerHTML = ''; // Clear existing previews
    var files = Array.from(imageInput.files);

    for (let x = 0; x < file_count; x++) {
        var file = imageInput.files[x];
        var url = window.URL.createObjectURL(file);

        var imagePreview = document.createElement('div');
        imagePreview.classList.add('image-preview');
        imagePreview.innerHTML = `
            <img src="${url}" alt="Image Preview" id="i${x}">
            
        `;
        imagePreviewContainer.appendChild(imagePreview);
    }


}



function handleImageChange() {
    const imageInput = document.getElementById('imageInput');
    const imagePreviewContainer = document.getElementById('imagePreviewContainer');

    const file_count = imageInput.files.length;

    if (file_count <= 6) {
        imagePreviewContainer.innerHTML = ''; // Clear existing previews

        for (let x = 0; x < file_count; x++) {
            const file = imageInput.files[x];
            const url = window.URL.createObjectURL(file);

            const imagePreview = document.createElement('div');
            imagePreview.classList.add('image-preview');
            imagePreview.innerHTML = `
                <img src="${url}"  alt="Image Preview" id="i${x}">
                <span class='btn btn-success' onclick="removeImage(${x})">&times;</span>
            `;
            imagePreviewContainer.appendChild(imagePreview);
        }
    } else {
        alert(file_count + " files. You can upload images upto 7 maximum.");
    }
}

function removeImage(index) {
    const img = document.getElementById(`i${index}`);
    if (img) {
        img.parentElement.remove();
    }
}

















