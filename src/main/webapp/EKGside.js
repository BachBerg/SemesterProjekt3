async function sogCPR(){
    //indsæt funktionerne her...
}


/*viser dropdown menu*/
function myFunction() {
    document.getElementById("myDropdown").classList.toggle("show");
}


    function showTime() {
    var date = new Date();
    let dato = `${date.getDate()}/${date.getMonth()}/${date.getFullYear()}`
    var time = date.getHours();
    var minut = date.getMinutes();

    if (minut < 10) {
        minut = "0" + minut;
    }
    //  var sekunder = date.getSeconds(); //Hvis vi skal have sekunder med

    document.getElementById("MyClockDisplay").innerText = `${dato} kl. ${time}:${minut}` //kl. " + time + ":" + minut; // +":"+sekunder;
    //document.getElementById("MyClockDisplay").textContent = "kl. " + time + ":" + minut; //+":"+sekunder;

    setTimeout(showTime, 10000,); //Tiden kan ændres, hvis vi er begrænset på processernes kapicitet
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