/* kode til at skjule og vise kodeordet */
const visibilityListen = document.getElementById('visibilityListen')
visibilityListen.addEventListener('click', togglevisibiliy) //brug functionen hvis man trykker på iconet

var passlabel = document.getElementById('password')
var btn = document.getElementById('btn')

passlabel.addEventListener('keyup', (e) => {
    if (e.keyCode === 13) {
        btn.click();
    }
})

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

async function login() {
    // Serialiser formen til js-objekt
    //let loginform = document.getElementById("loginform");
    user = document.getElementById("username").value;
    pass = document.getElementById("password").value;
    sessionStorage.setItem("user", user);

    //const formData = new FormData(loginform);
    //const object = Object.fromEntries(formData);
    console.log(user + pass)
    //Bruger fetch-API til at sende data - POST. JSON.stringify for at serialisere objekt til string.
    fetch("http://localhost:8080/Semesterprojekt3_war/data/login?" + new URLSearchParams({
        username: user,
        password: pass,
    }, {
        method: "GET"
    })).then(async resp => {
        if (resp.status >= 200 && resp.status <= 299) {
            const token = await resp.text()
            tokenHandler(token);
            //her
        } else {
            alert("Forkert kode")
        }
    });

}

function tokenHandler(loginToken) {
    //const token = await res.text();
    if (loginToken != null) {
        // hvis vi får en token, gemmer vi den i browserens localstorage
        localStorage.setItem("token", loginToken);

        //For ekstra krymmel fisker vi en bruger ud af tokenen
        const payload = window.atob(loginToken.split(".")[1]);
        const payloadJson = JSON.parse(payload);
        localStorage.setItem("user", payloadJson.username);

        //Viderestil til den rigtige side!
        window.location.href = "StartSide.html"
    }

}
