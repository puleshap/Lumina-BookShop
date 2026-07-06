async function processcheckout(){

    const response = await fetch("api/checkout",{
        method:"POST",
        headers:{
            "Content-Type":"application/json"
        }

    });


    if(response.ok){
        const data= await response.json();

        if(data.status) {
            payhere.onCompleted = function onCompleted(orderId) {
                console.log("Payment completed. OrderID:" + orderId);
                window.location="http://localhost:8080/luminabooks/payment-success.html?OrderId="+orderId;
            };

            payhere.onDismissed = function onDismissed() {
                console.log("Payment dismissed");
            };

            payhere.onError = function onError(error) {
                console.log("Error:"  + error);
            };


            // 3. Extract the paymentDetails object your Java code created
            var payment = {
                "sandbox": true, // Set to false in production
                "merchant_id": data.paymentDetails.merchant_id,
                "return_url": data.paymentDetails.return_url,
                "cancel_url": data.paymentDetails.cancel_url,

                "notify_url": data.paymentDetails.notify_url,
                "order_id": data.paymentDetails.order_id.toString(),
                "items": data.paymentDetails.items,
                "amount": data.paymentDetails.amount.toFixed(2), // Ensure 2 decimal places
                "currency": data.paymentDetails.currency,
                "hash": data.paymentDetails.hash,
                "first_name": data.user.FirstName, // You should pull these from your User session
                "last_name": data.user.LastName,
                "email": data.user.Email,
                "phone": "0771234567",
                "address": "No.1, Galle Road",
                "city": "Colombo",
                "country": "Sri Lanka",
            };

            // 4. Start the payment modal
            payhere.startPayment(payment);
        }else{
            Notiflix.Report.failure(
                'Lumina Books',
                "Login Before Checking Out",
                'Login',
                () => {
                    window.location = "SignUpPage.html"
                },

            );
            Notiflix.Notify.failure("Login Before Checking Out");
        }
    }


}