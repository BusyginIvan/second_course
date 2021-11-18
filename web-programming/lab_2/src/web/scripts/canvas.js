let canvas, ctx;
let semiaxisLength = 140, indent = 10, R = semiaxisLength - 20;

function initCanvas() {
    canvas = document.getElementById('canvas')
    canvas.setAttribute('width', 2 * (semiaxisLength + indent) + "px");
    canvas.setAttribute('height', 2 * (semiaxisLength + indent) + "px");

    canvas.offscreenCanvas = new OffscreenCanvas(canvas.width, canvas.height);

    ctx = canvas.offscreenCanvas.getContext('2d');
    ctx.translate(indent + semiaxisLength, indent + semiaxisLength);
    ctx.lineWidth = 2;
    
    drawArea();
    drawAxes();
    drawText();

    ctx = canvas.getContext('2d');
    ctx.drawImage(canvas.offscreenCanvas, 0, 0);

    ctx.strokeStyle = 'firebrick';
    ctx.fillStyle = 'red';
    ctx.lineWidth = 2;
}

function drawArea() {
    ctx.strokeStyle = 'navy';
    ctx.fillStyle = 'blue';
    ctx.globalAlpha = 0.5;

    drawRect(0, 0, R / 2, R);
    drawPolygon([[-R, 0], [0, 0], [0, -R / 2]]);
    ctx.beginPath();
    ctx.arc(0, 0, R / 2, Math.PI / 2, Math.PI);
    ctx.lineTo(0, 0);
    ctx.lineTo(0, R / 2);
    ctx.stroke();
    ctx.fill();
}

function drawAxes() {
    ctx.strokeStyle = '#000720';
    ctx.fillStyle = '#000720';
    ctx.globalAlpha = 1;

    ctx.beginPath();
    ctx.moveTo(-semiaxisLength, 0);
    ctx.lineTo(semiaxisLength, 0);
    ctx.moveTo(0, -semiaxisLength);
    ctx.lineTo(0, semiaxisLength);
    ctx.stroke();

    let l = 5, w = 3;
    drawPolygon([[semiaxisLength - l, -w], [semiaxisLength - l, w], [semiaxisLength, 0]]);
    drawPolygon([[-w, -semiaxisLength + l], [w, -semiaxisLength + l], [0, -semiaxisLength]]);

    ctx.moveTo(-R, -w);         ctx.lineTo(-R, w);
    ctx.moveTo(-R/2, -w);  ctx.lineTo(-R/2, w);
    ctx.moveTo(R, -w);          ctx.lineTo(R, w);
    ctx.moveTo(R/2, -w);   ctx.lineTo(R/2, w);
    ctx.moveTo(-w, -R);         ctx.lineTo(w, -R);
    ctx.moveTo(-w, -R/2);  ctx.lineTo(w, -R/2);
    ctx.moveTo(-w, R);          ctx.lineTo(w, R);
    ctx.moveTo(-w, R/2);   ctx.lineTo(w, R/2);
    ctx.stroke();
}

function drawText() {
    let indent = 8;
    ctx.font = "bold 17px TimesNewRoman";

    ctx.textAlign = "center";
    ctx.textBaseline = "hanging";
    ctx.fillText("-R", -R, indent);
    ctx.fillText("-R/2", -R/2, indent);
    ctx.fillText("R/2", R/2, indent);
    ctx.fillText("R", R, indent);

    ctx.textAlign = "right";
    ctx.textBaseline = "middle";
    ctx.fillText("-R", -indent, R);
    ctx.fillText("-R/2", -indent, R/2);
    ctx.fillText("R/2", -indent, -R/2);
    ctx.fillText("R", -indent, -R);
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

function drawPointer(x, y, r) {
    ctx.beginPath();
    ctx.arc(indent + semiaxisLength + x / r * R, indent + semiaxisLength - y / r * R, 4, 0, Math.PI * 2);
    ctx.stroke(); ctx.fill();
}