let date = document.getElementById("date");
let time = document.getElementById("time");
date.textContent = new Date().toLocaleDateString().toString();
time.textContent = new Date().toLocaleTimeString().toString();
setInterval(() => {
    date.textContent = new Date().toLocaleDateString().toString();
    time.textContent = new Date().toLocaleTimeString().toString();
 }, 8000);