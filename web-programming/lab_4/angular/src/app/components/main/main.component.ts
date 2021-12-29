import { Component } from '@angular/core';
import { ScreenSizeService } from "../../services/screen-size.service";
import { MessagesService } from "../../services/messages.service";
import { PointsService } from "../../services/points.service";

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.sass']
})
export class MainComponent {
  constructor(
    public screenSize: ScreenSizeService,
    public messageService: MessagesService,
    private pointsService: PointsService
  ) {
    pointsService.loadPoints();
  }
}
