import { Injectable } from '@angular/core';
import { HttpService } from "./http.service";
import { Point } from "../structures/point";

@Injectable({
  providedIn: 'root'
})
export class PointsService {
  points: Point[] = [
    /*{x: 1, y: 1, r: 2, result: true},
    {x: 1, y: 1, r: 2, result: true},
    {x: 1, y: 1, r: 2, result: true}*/
  ];
  action = () => {};

  constructor(private httpService: HttpService) { }

  loadPoints() {
    this.httpService.getPoints().subscribe(answer => this.points = answer);
  }

  addPoint(point: Point) {
    return  this.httpService.postPoint(point).subscribe(answer => {
      this.points.push(answer);
      this.action();
    });
  }

  clearPoints() {
    this.httpService.deletePoints().subscribe(() => {
      this.points = [];
      this.action();
    });
  }

  isEmpty(): boolean {
    return this.points.length == 0;
  }
}
