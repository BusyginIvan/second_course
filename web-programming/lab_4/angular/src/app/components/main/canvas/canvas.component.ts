import { AfterViewInit, Component } from '@angular/core';
import { ScreenSizeService } from "../../../services/screen-size.service";
import { MessagesService } from "../../../services/messages.service";
import { RadiusService } from "../../../services/radius.service";
import {PointsService} from "../../../services/points.service";
import {Point} from "../../../structures/point";

const divisionsNumber = 5, indent = 10;
const darkColor = '#000720';

@Component({
  selector: 'app-canvas',
  template: `
    <canvas></canvas>
  `,
  styleUrls: ['./canvas.component.sass']
})
export class CanvasComponent implements AfterViewInit {
  canvasSize!: number;
  arrowsLength!: number;
  scale!: number;
  canvas!: HTMLCanvasElement;
  ctx!: CanvasRenderingContext2D;

  constructor(
    public screenSize: ScreenSizeService,
    public messageService: MessagesService,
    public radiusService: RadiusService,
    private pointsService: PointsService,
  ) { }

  private update(newSize: number) {
    this.canvasSize = newSize;
    this.arrowsLength = newSize / 2 - indent;
    this.scale = this.arrowsLength - 20;
    this.canvas.setAttribute('width', newSize + 'px');
    this.canvas.setAttribute('height', newSize + 'px');
    const ctx = this.canvas.getContext('2d');
    if (ctx != null) {
      this.ctx = ctx;
      this.ctx.translate(indent + this.arrowsLength, indent + this.arrowsLength);
      this.ctx.lineWidth = 2;
      this.redraw();
    }
  }

  ngAfterViewInit(): void {
    this.canvas = document.getElementsByTagName('canvas')[0];
    this.canvas.addEventListener('mousedown', this.mouseListener.bind(this));

    this.screenSize.subscribeDesktop(() => this.update(350));
    this.screenSize.subscribeTablet(() => this.update(330));
    this.screenSize.subscribePhone(() => this.update(250));

    this.radiusService.action = this.redraw.bind(this);
    this.pointsService.action = this.redraw.bind(this);
  }

  private mouseListener(event: MouseEvent) {
    this.messageService.clear();
    let norm = (k: number) => Math.round((k - 2 - this.arrowsLength - indent) / this.scale * divisionsNumber * 1000) / 1000;
    const x = norm(event.pageX - this.canvas.getBoundingClientRect().x);
    const y = -norm(event.pageY - this.canvas.getBoundingClientRect().y);
    if (this.radiusService.radius) {
      if (x >= -5 && x <= 5 && y >= -5 && y <= 5)
        this.pointsService.addPoint(x, y, this.radiusService.radius);
    } else this.messageService.add('Чтобы добавить точку, необходимо выбрать радиус.');
  }

  redraw() {
    this.clear();
    this.drawArea();
    this.drawArrows();
    this.drawText();
    this.drawPoints();
  }

  clear() {
    this.ctx.fillStyle = 'white';
    this.ctx.fillRect(-this.arrowsLength, -this.arrowsLength, this.arrowsLength * 2, this.arrowsLength * 2);
  }

  drawArea() {
    if (!this.radiusService.radius) return;
    let figuresScale = this.radiusService.radius / divisionsNumber * this.scale;
    this.ctx.strokeStyle = 'navy';
    this.ctx.fillStyle = 'blue';
    this.ctx.globalAlpha = 0.5;

    this.drawRect(0, -figuresScale, figuresScale/2, figuresScale);

    this.ctx.beginPath();

    this.drawPolygon([[0, 0], [figuresScale, 0], [0, figuresScale]]);
    this.ctx.arc(0, 0, figuresScale/2, Math.PI / 2, Math.PI);
    this.ctx.lineTo(0, 0);
    this.ctx.lineTo(0, figuresScale/2);

    this.ctx.stroke();
    this.ctx.fill();

    this.ctx.globalAlpha = 1;
  }

  drawArrows() {
    this.ctx.strokeStyle = darkColor;
    this.ctx.fillStyle = darkColor;

    this.ctx.beginPath();

    this.ctx.moveTo(-this.arrowsLength, 0); this.ctx.lineTo(this.arrowsLength, 0);
    this.ctx.moveTo(0, -this.arrowsLength); this.ctx.lineTo(0, this.arrowsLength);

    let l = 5, w = 3;
    this.drawPolygon([
      [this.arrowsLength - l, -w - 1], [this.arrowsLength - l, w], [this.arrowsLength, -0.5]
    ]);
    this.drawPolygon([
      [-w - 1, -this.arrowsLength + l], [w, -this.arrowsLength + l], [-0.5, -this.arrowsLength]
    ]);

    for (let i = -divisionsNumber; i <= divisionsNumber; i++) {
      if (i === 0) i++;
      let c = this.scale * i / divisionsNumber;
      this.ctx.moveTo(c, -w); this.ctx.lineTo(c, w);
      this.ctx.moveTo(-w, -c); this.ctx.lineTo(w, -c);
    }

    this.ctx.stroke();
    this.ctx.fill();
  }

  private drawPolygon(points: number[][]) {
    this.ctx.moveTo(points[0][0], points[0][1]);
    for (let i = 1; i < points.length; i++)
      this.ctx.lineTo(points[i][0], points[i][1]);
  }

  private drawRect(x: number, y: number, w: number, h: number) {
    this.ctx.fillRect(x, y, w, h);
    this.ctx.strokeRect(x, y, w, h);
  }

  drawText() {
    let indent;
    if (this.screenSize.isDesktop || this.screenSize.isTablet) {
      indent = 6;
      this.ctx.font = "bold 14pt TimesNewRoman";
    } else {
      indent = 4;
      this.ctx.font = "bold 10pt TimesNewRoman";
    }

    this.ctx.strokeStyle = darkColor;
    this.ctx.fillStyle = darkColor;

    this.ctx.textAlign = "center";
    this.ctx.textBaseline = "hanging";
    for (let i = -divisionsNumber; i <= divisionsNumber; i++) {
      if (i === 0) i++;
      this.ctx.fillText(String(i), this.scale * i / divisionsNumber, indent);
    }

    this.ctx.textAlign = "right";
    this.ctx.textBaseline = "middle";
    for (let i = -divisionsNumber; i <= divisionsNumber; i++) {
      if (i === 0) i++;
      this.ctx.fillText(String(-i), -indent, this.scale * i / divisionsNumber);
    }
  }

  private drawPoint(point: Point) {
    if (point.r !== this.radiusService.radius) return;

    if (point.result) {
      this.ctx.strokeStyle = 'DarkGreen';
      this.ctx.fillStyle = 'Green';
    } else {
      this.ctx.strokeStyle = 'FireBrick';
      this.ctx.fillStyle = 'Red';
    }

    this.ctx.beginPath();
    this.ctx.arc(point.x / divisionsNumber * this.scale, -point.y / divisionsNumber * this.scale,
      4, 0, Math.PI * 2);
    this.ctx.stroke();
    this.ctx.fill();
  }

  drawPoints() {
    this.pointsService.points.forEach(point => this.drawPoint(point));
  }
}
