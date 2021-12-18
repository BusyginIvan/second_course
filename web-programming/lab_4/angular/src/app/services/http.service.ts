import { Injectable } from '@angular/core';
import {HttpClient, HttpErrorResponse, HttpHeaders, HttpParams} from "@angular/common/http";
import {catchError, Observable, tap, throwError} from "rxjs";
import {Point} from "../structures/point";
import {MessagesService} from "./messages.service";

@Injectable({ providedIn: 'root' })
export class HttpService {
  private options = {};
  public authorized = false;

  constructor(private http: HttpClient, private messageService: MessagesService) { }

  private static url(host: string): string {
    return 'rest/' + host
  }

  private setOptions(login: string, password: string) {
    this.options = {
      headers: new HttpHeaders()
        .set("login", login)
        .set("password", password)
    };
  }

  private handleError(error: HttpErrorResponse) {
    console.error(error);
    const errorMessage = 'Ошибка HTTP. Код состояния: ' + error.status;
    this.messageService.add(errorMessage);
    return throwError(errorMessage);
  }

  registration(login: string, password: string): Observable<boolean> {
    this.setOptions(login, password);
    return this.http.get<boolean>(HttpService.url('registration'), this.options).pipe(
      tap(answer => this.authorized = answer),
      catchError(this.handleError.bind(this))
    );
  }

  authorization(login: string, password: string): Observable<boolean> {
    this.setOptions(login, password);
    return this.http.get<boolean>(HttpService.url('authorization'), this.options).pipe(
      tap(answer => this.authorized = answer),
      catchError(this.handleError.bind(this))
    );
  }

  logout() { this.authorized = false }

  loadPoints(): Observable<Point[]> {
    return this.http.get<Point[]>(HttpService.url('getPoints'), this.options).pipe(
      catchError(this.handleError.bind(this))
    );
  }

  addPoint(x: number, y: number, r: number): Observable<Point> {
    const body = new HttpParams().set('x', x).set('y', y).set('r', r);
    return this.http.post<Point>(HttpService.url('addPoint'), body, this.options).pipe(
      catchError(this.handleError.bind(this))
    );
  }

  clearPoints(): Observable<void> {
    return this.http.delete<void>(HttpService.url('clearPoints'), this.options).pipe(
      catchError(this.handleError.bind(this))
    );
  }
}
