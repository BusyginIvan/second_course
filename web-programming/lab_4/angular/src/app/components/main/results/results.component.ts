import { Component } from '@angular/core';
import { PointsService } from "../../../services/points.service";

@Component({
  selector: 'app-results',
  templateUrl: './results.component.html',
  styleUrls: ['./results.component.sass']
})
export class ResultsComponent {
  constructor(public pointsService: PointsService) { }

  public round(num: number) {
    return Math.round(num * 1000) / 1000;
  }
}
