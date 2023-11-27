function login() {

    var email = document.getElementById("email").value;
    var password = document.getElementById("password").value;

    if (email === "dr.krleza@gmail.com" && password === "MedicinaJeZakon123") {
        window.location.href = "../../home-Jelena/static/home_doctor.html";
    } else if (email === "g.kremenko@gmail.com" && password === "JabaDabaDooo") {
        window.location.href = "../../home-Jelena/static/home_patient.html";
    } else {
        alert("Podatci za prijavu koje ste unijeli su pogrešni. Molim pokušajte ponovno.");
    }

}