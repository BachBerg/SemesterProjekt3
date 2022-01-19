//EKG visualisering
let data = {
    datasets:
        [
            {
                label: "ekg",
                backgroundColor: 'rgb(11,71,144)',
                borderColor: 'rgb(11,71,144)',
                data: [],
                fill: false,
                tension: 0,
                pointRadius: 2,
                pointHoverRadius: 1
            }
        ]
}
let myChart;

function createChart(array) {
    myChart.destroy();
    data.datasets[0].data = array;
    data.labels = Array(array.length).fill("");

    let ctx = document.getElementById("myChart").getContext("2d");
    myChart = new Chart(ctx, {
        type: 'line',
        data: data,
        option: {}
    });

}

window.onload = function () {
    let ctx = document.getElementById("myChart").getContext("2d");
    myChart = new Chart(ctx, {
        type: 'line',
        data: data,
        option: {}
    });
}

async function getSessions() {
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
    let sessID = "";
    let cpr = "";
    let note = "";
    // først ryddes feltet hvis der skulle være sessioner fra tidligere søgninger
    document.getElementById("sessionsField").innerHTML = "";

    // sessions listen køres igennem og konstruere en button for hver session
    for (let i = 0; i < data.ekgSessionList.length; i++) {
        let div = document.createElement("div");
        div.id = "div" + i;

        sessID = data.ekgSessionList[i].sessionID;
        cpr = data.ekgSessionList[i].cpr;
        note = data.ekgSessionList[i].comment;

        let buffer = "CPR: " + cpr + " sessionID: " + sessID;

        let btn = document.createElement("button");
        btn.innerHTML = buffer;
        btn.id = "btn" + sessID;
        btn.setAttribute("session", sessID);
        btn.setAttribute("comment", note);
        btn.className = "button2";
        btn.onclick = function () {
            getSessionData(btn.getAttribute("session"));
            setNewComment(btn.getAttribute("comment"), btn.getAttribute("session"));
        };
        document.getElementById("sessionsField").appendChild(div);
        document.getElementById("div" + i).appendChild(btn);
    }
}

function getSessionData(ID) {
    fetch("data/ekgSessions/measurementsJson?" + new URLSearchParams({
        sessionID: ID,
    }), {
        headers: {
            "Authorization": localStorage.getItem("token")
        }
    }).then(resp => resp.json()).then(data => createChart(data.measurment));
}

function setNewComment(newComment, sessionID) {
    document.getElementById("comment").value = newComment;
    document.getElementById("comment").setAttribute("session", sessionID);


}

function savaEditedComment() {
    let newNote = document.getElementById("comment").value;
    let newSessID = document.getElementById("comment").getAttribute("session");
    console.log("note: " + newNote + " id: " + newSessID);
    fetch("data/ekgSessions/ekgSessionNote?" + new URLSearchParams({
        note: newNote,
        sessionID: newSessID
    }), {
        method: "POST",
        headers: {
            "Authorization": localStorage.getItem("token")
        }
    });
    document.getElementById("btn" + newSessID).setAttribute("comment", newNote);
}