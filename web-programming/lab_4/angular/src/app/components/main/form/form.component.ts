import { Component } from '@angular/core';
import { PointsService } from "../../../services/points.service";
import { HttpService } from "../../../services/http.service";
import { Router } from "@angular/router";
import { MessagesService } from "../../../services/messages.service";
import { RadiusService } from "../../../services/radius.service";
import { Point } from "../../../structures/point";

@Component({
  selector: 'app-form',
  templateUrl: './form.component.html',
  styleUrls: ['./form.component.sass']
})
export class FormComponent {
  firstLineValues = [-5, -4, -3, -2, -1];
  secondLineValues = [0, 1, 2, 3];

  x!: number;
  y = '';

  constructor(
    private router: Router,
    private httpService: HttpService,
    private pointsService: PointsService,
    public radiusService: RadiusService,
    private messageService: MessagesService,
  ) { }

  public get r(): number { return this.radiusService.radius }

  private get currentPoint(): Point {
    return {x: this.x, y: this.getYAsNum(), r: this.r};
  }

  logout() {
    this.httpService.logout();
    this.router.navigate(['/login']);
  }

  clearPoints() {
    this.messageService.clear();
    this.pointsService.clearPoints();
  }

  addPoint() {
    this.messageService.clear();
    if (this.x && this.validY() && this.r != undefined) {
      this.pointsService.addPoint(this.currentPoint);
      return;
    }
    if (this.y == '')
      this.messageService.add('Поле y не может быть пустым.');
    if (isNaN(this.getYAsNum()))
      this.messageService.add('Координата y должна быть числом.');
    const y = this.getYAsNum();
    if (!isNaN(y) && (y < -5 || y > 5))
      this.messageService.add('Координата y должна лежать в диапазоне от -5 до 5.');
    if (!this.x)
      this.messageService.add('Необходимо выбрать x.');
    if (this.r == undefined)
      this.messageService.add('Необходимо выбрать r.');
  }

  private getYAsNum(): number {
    return Number(this.y.replace(',', '.'));
  }

  private validY(): boolean {
    const y = this.getYAsNum();
    return this.y != '' && !isNaN(y) && y >= -5 && y <= 5;
  }
}
