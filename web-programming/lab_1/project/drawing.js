let canvas, ctx;
let rad = 140, ind = 10, max = rad - 20;

function draw() {
    canvas = document.getElementById('canvas')
    canvas.setAttribute('width', 2 * (rad + ind) + "px");
    canvas.setAttribute('height', 2 * (rad + ind) + "px");

    canvas.offscreenCanvas = new OffscreenCanvas(canvas.width, canvas.height);

    ctx = canvas.offscreenCanvas.getContext('2d');
    ctx.translate(ind + rad, ind + rad);
    ctx.lineWidth = 2;

    ctx.strokeStyle = 'navy';
    ctx.fillStyle = 'blue';
    ctx.globalAlpha = 0.5;

    drawRect(0, -max / 2, max, max / 2);
    drawPolygon([[-max, 0], [0, 0], [0, -max]]);
    ctx.beginPath();
    ctx.arc(0, 0 ,max, 0, Math.PI / 2);
    ctx.lineTo(0, 0);
    ctx.lineTo(max, 0);
    ctx.stroke(); ctx.fill();

    ctx.strokeStyle = '#000720';
    ctx.fillStyle = '#000720';
    ctx.globalAlpha = 1;

    ctx.beginPath();
    ctx.moveTo(-rad, 0); ctx.lineTo(rad, 0);
    ctx.moveTo(0, -rad); ctx.lineTo(0, rad);
    ctx.stroke();

    let l = 5, w = 3;
    drawPolygon([[rad - l, -w], [rad - l, w], [rad, 0]]);
    drawPolygon([[-w, -rad + l], [w, -rad + l], [0, -rad]]);

    ctx.moveTo(-max, -w); ctx.lineTo(-max, w);
    ctx.moveTo(-max / 2, -w); ctx.lineTo(-max / 2, w);
    ctx.moveTo(max, -w); ctx.lineTo(max, w);
    ctx.moveTo(max / 2, -w); ctx.lineTo(max / 2, w);
    ctx.moveTo(-w, -max); ctx.lineTo(w, -max);
    ctx.moveTo(-w, -max / 2); ctx.lineTo(w, -max / 2);
    ctx.moveTo(-w, max); ctx.lineTo(w, max);
    ctx.moveTo(-w, max / 2); ctx.lineTo(w, max / 2);
    ctx.stroke();

    ind = 8;
    ctx.font = "bold 17px TimesNewRoman";

    ctx.textAlign = "center";
    ctx.textBaseline="hanging";
    ctx.fillText("-R", -max, ind);
    ctx.fillText("-R/2", -max/2, ind);
    ctx.fillText("R/2", max/2, ind);
    ctx.fillText("R", max, ind);

    ctx.textAlign = "right";
    ctx.textBaseline="middle";
    ctx.fillText("-R", -ind, max);
    ctx.fillText("-R/2", -ind, max/2);
    ctx.fillText("R/2", -ind, -max/2);
    ctx.fillText("R", -ind, -max);

    ctx = canvas.getContext('2d');
    ctx.drawImage(canvas.offscreenCanvas, 0, 0);
    ctx.strokeStyle = 'firebrick';
    ctx.fillStyle = 'red';
    ctx.lineWidth = 2;
}

function drawPolygon(points) {
    ctx.beginPath();
    ctx.moveTo(points[0][0], points[0][1]);
    for (let i = 1; i < points.length; i++)
        ctx.lineTo(points[i][0], points[i][1]);
    ctx.closePath();
    ctx.fill();
    ctx.stroke();
}

function drawRect(x, y, w, h) {
    ctx.fillRect(x, y, w, h);
    ctx.strokeRect(x, y, w, h);
}