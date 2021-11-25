/* kode til at skjule og vise kodeordet */
const visibilityListen = document.getElementById('visibilityListen')
visibilityListen.addEventListener('click', togglevisibiliy) //brug functionen hvis man trykker på iconet

function togglevisibiliy() {
    const passwordInput = document.getElementById("password")
    const icon = document.getElementById("icon")
    if (passwordInput.type === "password") { //vis koden
        passwordInput.type = "text"
        icon.innerText = "visibility_off"
    } else { //vis ikke koden
        passwordInput.type = "password"
        icon.innerText = "visibility"
    }
}

/* kode til at validere koden */
let user = "";
let pass = "";

function login() {
    user = document.getElementById("username").value;
    pass = document.getElementById("password").value;
    fetch("http://localhost:8080/IT3_Delopgave_2_war/data/login?" + new URLSearchParams({
            username: user,
            password: pass,
        }
    )).then(async resp => {
        if (resp.status >= 200 && resp.status <= 299) {
            return resp.text();
        } else {
            throw Error(await resp.text());
        }
    }).then(data => validate(data)).catch(Error =>alert(Error));

}*/
