
function paymentProcess() {
    console.log("hello");
    let options = {
        "key": "rzp_test_O3AUSxB8L47Bsf", // Enter the Key ID generated from the Dashboard
        "amount": 320*100, // Amount is in currency subunits. Default currency is INR. Hence, 50000 means 50000 paise or ₹500.
        "currency": "INR",
        "name": "Devotional Study",
        "description": "Tutorial",
        "image": "logo.png",// Replace this with the order_id created using Orders API (https://razorpay.com/docs/api/orders).
        "handler": function (response){
            alert(response.razorpay_payment_id);
            /*savetoDB(response);
            $('#myModal').modal();*/
        },
        "prefill": {
            "name": "Akshay Bhatia",
            "email": "akshay.bhatia@gmail.com",
            "contact": "9999999999"
        },
        "notes": {
            "address": "note value"
        },
        "theme": {
            "color": "#9932CC"
        }
    }
    let propay = new Razorpay(options);
    propay.open();
}

function getRadioButtona() {
    let address = document.getElementsByName("addressId");
    for (let i = 0; i < address.length; i++) {
        if (address[i].checked) {
            let addrIdSet = document.querySelector("#addressIdSet");
            addrIdSet["value"] = address[i].valueOf()["value"];
            console.log(addrIdSet["value"]);
            //address is giving a node list
            //value of gives a particular ele and value gives its value.
            //since i'm selecting value by name hence it will give a list.
        }
    }
}

let total = document.querySelector("#totalPrice");
let totalPrice = total.attributes['value']['value'];
/*console.log(total);
console.log(total["attributes"]["value"]["value"]);
let widget = document.querySelector('#paymentWidget');
console.log(totalPrice);
widget.attributes['data-amount']['value'] = totalPrice * 1000 / 10;
console.log(widget.attributes['data-amount']['value']);*/
//  document.getElementById("paymentWidget").setAttribute('data-amount', "200")
//console.log(document.getElementById("paymentattributes['value']['value']Widget"));

// let original = document.querySelector('#merchant');
//  console.log(original);
