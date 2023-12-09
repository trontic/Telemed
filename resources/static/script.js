function login() {

    var email = document.getElementById("email").value;
    var password = document.getElementById("password").value;

    if (email === "doctor@gmail.com" && password === "doctor") {
        window.location.href = "doctor_home.html";
    } else if (email === "patient@gmail.com" && password === "patient") {
        window.location.href = "patient_home.html";
    } else {
        alert("Podaci za prijavu koje ste unijeli su pogrešni. Molim pokušajte ponovno.");
    }
}

function redirectToLogin() {

    var url = 'login.html'
    window.location.href = url;
}