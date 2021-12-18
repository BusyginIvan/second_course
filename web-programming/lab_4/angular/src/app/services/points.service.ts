import { Injectable } from '@angular/core';
import { HttpService } from "./http.service";
import { Point } from "../structures/point";
import {MessagesService} from "./messages.service";
import {Observable, tap} from "rxjs";

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
    this.httpService.loadPoints().subscribe(answer => this.points = answer);
  }

  addPoint(x: number, y: number, r: number) {
    return  this.httpService.addPoint(x, y, r).subscribe(answer => {
      this.points.push(answer);
      this.action();
    });
  }

  clearPoints() {
    this.httpService.clearPoints().subscribe(() => {
      this.points = [];
      this.action();
    });
  }

  isEmpty(): boolean {
    return this.points.length == 0;
  }
}
