"use strict";

let x = 1, y = 1, r = 2;
let currentButton = document.createElement("input");

function initButtons() {
    let buttons = document.querySelectorAll("input[name=Y-button]");
    buttons.forEach(button =>
        button.onclick = function () {
            currentButton.style.boxShadow = "";
            currentButton.style.transform = "";
            y = this.value;
            this.style.boxShadow = "0 0 40px 5px #F41C52";
            this.style.transform = "scale(1.05)";
            currentButton = this;
        }
    );
}

document.getElementById("checkButton").onclick = function () {
    if (validateX() && validateY() && validateR()) {
        fetch("answer.php?x=" + encodeURIComponent(x) + "&y=" + encodeURIComponent(y) + "&r=" + encodeURIComponent(r) +
        "&timezone=" + encodeURIComponent(Intl.DateTimeFormat().resolvedOptions().timeZone))
            .then(response => response.text())
            .then(serverAnswer => {
                setPointer();
                document.getElementById("outputContainer").innerHTML = serverAnswer;
            })
            .catch(err => createNotification("Ошибка HTTP. Повторите попытку позже." + err));
    }
};

function setPointer() {
    ctx.clearRect(0, 0, canvas.width, canvas.height);
    ctx.drawImage(canvas.offscreenCanvas, 0, 0);
    ctx.beginPath();
    ctx.arc(ind + rad + x / r * max, ind + rad - y / r * max, 4, 0, Math.PI * 2);
    ctx.stroke(); ctx.fill();
}

function createNotification(message) {
    let outputContainer = document.getElementById("outputContainer");
    let notification = document.querySelector(".notification");
    notification.textContent = message;
    if (outputContainer.contains(notification))
        notification.classList.replace("outputStub", "errorStub");
    else {
        let notificationTableRow = document.createElement("h4");
        notificationTableRow.innerHTML = "<span class='notification errorStub'></span>";
        outputContainer.prepend(notificationTableRow);
    }
}

function validateY() {
    if (isNumeric(y)) return true;
    else {
        createNotification("y не выбран");
        return false;
    }
}

function validateX() {
    x = document.querySelector("input[name=X-input]").value.replace(",", ".");
    if (x === undefined) {
        createNotification("x не введён");
        return false;
    } if (!isNumeric(x)) {
        createNotification("x не число");
        return false;
    } if ((x < -5) || (x > 3)) {
        createNotification("x не входит в область допустимых значений");
        return false;
    } return true;
}

function validateR() {
    try {
        r = document.querySelector("input[type=radio]:checked").value;
        return true;
    } catch (err) {
        createNotification("Значение R не выбрано");
        return false;
    }
}

function isNumeric(n) {
    return !isNaN(parseFloat(n)) && isFinite(n);
}