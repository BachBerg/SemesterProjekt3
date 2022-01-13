async function sogCPR(){
    let cpr = document.getElementById("cpr").value;
    if (cpr.length === 10) { /*sørger for samme værdi og samme type*/
    }
    /*url skal ændres til database til cpr*/
    fetch("http://localhost:8080/Semesterprojekt3_war/data/login?/" + new URLSearchParams ({
        cpr: cpr
    }), {
            headers: {
                "Authorization": localStorage.getItem("token")
            }
        })
}

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
            data:[-0.01, -0.02, -0.03, 0.01, 0.02, 0.03, -0.01, -0.02, -0.03, 0.01, 0.02, 0.03, -0.01, -0.02, -0.03, 0.01, 0.02, 0.03, -0.01, -0.02, -0.03, 0.01, 0.02, 0.03, -0.01, -0.02, -0.03, 0.01, 0.02, 0.03]
        }
    ]
}
data.labels = Array(data.datasets[0].data.length).fill("")

const config = {
    type: 'line',
    data: data,
    options: {}
}


window.onload = (function (){
    let canvas = document.getElementById("mycanvas");
    let context = canvas.getContext("2d");
    context.moveTo(0,300);
    for (let i = 0; i < data.length; i++) {
        context.lineTo(i, 300 - data[i] * 100);
        context.stroke();
    }

    const myChart = new Chart(
        document.getElementById('myChart'),
       config
    )
})