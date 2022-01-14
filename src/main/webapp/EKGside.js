/*viser dropdown menu*/
function myFunction() {
    document.getElementById("myDropdown").classList.toggle("show");
}

async function MarkerFunktion(){
}

//EKG visualisering
let data = {
    datasets:
    [
        {label:"ekg",
            backgroundColor: 'rgb(11,71,144)',
            borderColor: 'rgb(11,71,144)',
            color: 'rgb(11,71,144)',
            data:[],
            tension: 1,
            pointRadius: 0.5,
            pointHoverRadius: 0.5
        }
    ]
}

function createChart(){
    let ctx = document.getElementById("myChart").getContext("2d")
    let array = [-0.01, -0.02, -0.03, 0.01, 0.02, 0.03, -0.01, -0.02, -0.03, 0.01, 0.02, 0.03, -0.01, -0.02, -0.03, 0.01, 0.02, 0.03, -0.01, -0.02, -0.03, 0.01, 0.02, 0.03, -0.01, -0.02, -0.03, 0.01, 0.02, 0.03];

    data.datasets[0].data = array;
    data.labels = Array(data.datasets[0].data.length).fill("");

    const myChart = new Chart(ctx,{
        type: 'line',
        data: data,
        option: {}
    });
}

window.onload = (function (){
    createChart();
});



async function getSessions(){
    /*url skal ændres til database til cpr*/
    await fetch("data/ekgSessions/ekgSessionJson?" + new URLSearchParams({
        cpr: document.getElementById("cpr").value
    }), {
        headers: {
            "Authorization": localStorage.getItem("token")
        }
    }).then(resp => resp.json()).then(data => createSession(data));
}
function createSession(data) {
    let timestart = "";
    let sesionID = "";
    let marker = "";
    let note = "";
    let klinikID = "";
    let container = "";
    // først ryddes feltet hvis der skulle være sessioner fra tidligere søgninger
    document.getElementById("sessionsfelt").innerHTML ="";

    // sessions listen køres igennem og konstruere en button for hver session
    for (let i = 0; i < data.ekgSessionList.length; i++) {
        timestart = data.ekgSessionList[i].timeStart;
        sesionID = data.ekgSessionList[i].sessionID;
        //marker = data.ekgSessionList[i].markers;
        //note = data.ekgSessionList[i].comment;

        let buffer = "klinik id: " + klinikID + " sessionID: " + sesionID;

        let btn = document.createElement("button");
        btn.innerHTML = buffer;
        btn.setAttribute("session", sesionID);
        btn.className = "button2";
        btn.onclick = function () {
            alert("plot session: " + btn.getAttribute("session"));
        };
        document.getElementById("sessionsfelt").appendChild(btn);

    }

}