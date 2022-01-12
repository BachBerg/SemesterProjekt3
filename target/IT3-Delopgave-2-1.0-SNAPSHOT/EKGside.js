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