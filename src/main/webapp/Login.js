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

    user = document.getElementById("username").value;
    pass = document.getElementById("password").value;
    sessionStorage.setItem("user", user);

    //Bruger fetch-API til at sende data - POST. JSON.stringify for at serialisere objekt til string.
    fetch("data/login?" + new URLSearchParams({
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
    if (loginToken != null) {
        // hvis vi får en token, gemmer vi den i browserens localstorage
        localStorage.setItem("token", loginToken);

        fetch("data/login/auth", {
            headers: {
                "Authorization": localStorage.getItem("token")
            }
        }).then(async resp => {
            const auth = await resp.text();
            console.log(auth)
            localStorage.setItem("authstatus", auth);
            if(auth === "1") {
                window.location.href = "StartSide.html"
            }else{
                window.location.href = "patientStartSide.html"
            }
        });


    }
}
