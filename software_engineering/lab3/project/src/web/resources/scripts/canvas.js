const divisionsNumber = 5;
const arrowsLength = 160, indent = 10, scale = arrowsLength - 20;

const canvas = document.getElementById('canvas');
canvas.setAttribute('width', 2 * (arrowsLength + indent) + "px");
canvas.setAttribute('height', 2 * (arrowsLength + indent) + "px");

const ctx = canvas.getContext('2d');
ctx.translate(indent + arrowsLength, indent + arrowsLength);
ctx.lineWidth = 2;

function onevent(data) { if (data.status === 'success') redraw() }

function redraw() {
    clear();
    drawArea();
    drawArrows();
    drawPoints();
    drawText();
}

function clear() {
    ctx.fillStyle = 'white';
    ctx.fillRect(-arrowsLength, -arrowsLength, arrowsLength * 2, arrowsLength * 2);
}

function drawArea() {
    let figuresScale = currentR / divisionsNumber * scale;
    ctx.strokeStyle = 'navy';
    ctx.fillStyle = 'blue';
    ctx.globalAlpha = 0.5;

    ctx.beginPath();

    drawRect(-figuresScale, -figuresScale/2, figuresScale, figuresScale/2);
    drawPolygon([[0, 0], [figuresScale, 0], [0, figuresScale / 2]]);
    ctx.arc(0, 0, figuresScale, Math.PI / 2, Math.PI);
    ctx.lineTo(0, 0);
    ctx.lineTo(0, figuresScale);

    ctx.stroke();
    ctx.fill();

    ctx.globalAlpha = 1;
}

function drawArrows() {
    ctx.strokeStyle = '#000720';
    ctx.fillStyle = '#000720';

    ctx.beginPath();

    ctx.moveTo(-arrowsLength, 0); ctx.lineTo(arrowsLength, 0);
    ctx.moveTo(0, -arrowsLength); ctx.lineTo(0, arrowsLength);

    let l = 5, w = 3;
    drawPolygon([[arrowsLength - l, -w - 1], [arrowsLength - l, w], [arrowsLength, -0.5]]);
    drawPolygon([[-w - 1, -arrowsLength + l], [w, -arrowsLength + l], [-0.5, -arrowsLength]]);

    for (let i = -divisionsNumber; i <= divisionsNumber; i++) {
        if (i === 0) i++;
        let c = scale * i / divisionsNumber;
        ctx.moveTo(c, -w); ctx.lineTo(c, w);
        ctx.moveTo(-w, -c); ctx.lineTo(w, -c);
    }

    ctx.stroke();
    ctx.fill();
}

function drawText() {
    let indent = 6;
    ctx.strokeStyle = '#000720';
    ctx.fillStyle = '#000720';
    ctx.font = "bold 17px TimesNewRoman";

    ctx.textAlign = "center";
    ctx.textBaseline = "hanging";
    for (let i = -divisionsNumber; i <= divisionsNumber; i++) {
        if (i === 0) i++;
        ctx.fillText(String(i), scale * i / divisionsNumber, indent);
    }

    ctx.textAlign = "right";
    ctx.textBaseline = "middle";
    for (let i = -divisionsNumber; i <= divisionsNumber; i++) {
        if (i === 0) i++;
        ctx.fillText(String(-i), -indent, scale * i / divisionsNumber);
    }
}

function drawPolygon(points) {
    ctx.moveTo(points[0][0], points[0][1]);
    for (let i = 1; i < points.length; i++)
        ctx.lineTo(points[i][0], points[i][1]);
}

function drawRect(x, y, w, h) {
    ctx.fillRect(x, y, w, h);
    ctx.strokeRect(x, y, w, h);
}

function drawPoint(x, y, r, result) {
    if (Number(r) !== Number(currentR)) return;

    if (result === 'yes') {
        ctx.strokeStyle = 'DarkGreen';
        ctx.fillStyle = 'Green';
    } else if (result === 'no') {
        ctx.strokeStyle = 'FireBrick';
        ctx.fillStyle = 'Red';
    } else return;

    ctx.beginPath();
    ctx.arc(x / divisionsNumber * scale, -y / divisionsNumber * scale,
        4, 0, Math.PI * 2);
    ctx.stroke();
    ctx.fill();
}

function drawPoints() {
    let pointsTable = document.getElementById('pointsTable');
    if (pointsTable) {
        let rows = pointsTable.tBodies[0].rows;
        for (let i = 0; i < rows.length; i++) {
            let columns = rows[i].children;
            drawPoint(
                columns[0].textContent,
                columns[1].textContent,
                columns[2].textContent,
                columns[3].textContent
            );
        }
    }
}
