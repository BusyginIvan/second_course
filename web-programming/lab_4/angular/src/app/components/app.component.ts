import { AfterViewInit, Component } from '@angular/core';
import { ScreenSizeService } from "../services/screen-size.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.sass']
})
export class AppComponent implements AfterViewInit {
  header!: HTMLHeadElement;
  base!: HTMLElement | null;
  list!: HTMLElement | null;

  constructor(public screenSize: ScreenSizeService) { }

  ngAfterViewInit(): void {
    this.header = document.getElementsByTagName('header')[0];
    this.base = document.getElementById('base');
    this.list = document.getElementById('list');
    this.update();
    window.addEventListener('resize', this.update.bind(this));
  }

  private update(): void {
    if (this.base) this.base.style.top = this.header.offsetHeight + 'px';
    if ((this.screenSize.isTablet || this.screenSize.isPhone) && this.list) {
      this.list.style.top = this.header.offsetHeight + 'px';
    }
  }
}
