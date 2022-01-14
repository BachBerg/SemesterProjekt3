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
            data:[-0.01, -0.02, -0.03, 0.01, 0.02, 0.03, -0.01, -0.02, -0.03, 0.01, 0.02, 0.03, -0.01, -0.02, -0.03, 0.01, 0.02, 0.03, -0.01, -0.02, -0.03, 0.01, 0.02, 0.03, -0.01, -0.02, -0.03, 0.01, 0.02, 0.03],
            tension: 1,
            pointRadius: 0.5,
            pointHoverRadius: 0.5
        }
    ]
}
data.labels = Array(data.datasets[0].data.length).fill("");


window.onload = (function (){
    let canvas = document.getElementById("myChart");
    let context = canvas.getContext("2d");

    context.moveTo(0,300);

    for (let i = 0; i < data.length; i++) {
        context.lineTo(i, 300 - data[i] * 100);
        context.stroke();
    }
    const myChart = new Chart(canvas,{
        type: 'line',
        data: data,
        option: {}
    }
    );
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

    for (i = 0; i < data.ekgSessionList.length; i++) {
        timestart = data.ekgSessionList[i].timeStart;
        sesionID = data.ekgSessionList[i].sessionID;
        marker = data.ekgSessionList[i].markers;
        note = data.ekgSessionList[i].comment;
        //klinikID = data.ekgSessionList[i].klinikID;
        //console.log(klinikID);

        let buffer = '<span class="button1" id="session' + i + '" hidden>KlinikID: ' + klinikID
            +'      '+ 'Sessionid: ' + sesionID + '<br> Marker:<br><p id="marker' + i + '">' + marker
            + '</p><br> Note:<br><textarea style="width: 225px" id="textarea' + i + '">' + note
            + '</textarea><hr></span>';

        container += buffer;
    }

    document.getElementById("sessionsfelt").innerText = container;
}