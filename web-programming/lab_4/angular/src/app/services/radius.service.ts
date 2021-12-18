import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class RadiusService {
  radius!: number;
  action = () => {};

  setR(newR: number) {
    this.radius = newR;
    this.action();
  }
}
