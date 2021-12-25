import {Inject, Injectable} from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpHeaders } from "@angular/common/http";
import { catchError, Observable, tap, throwError } from "rxjs";
import { Point } from "../structures/point";
import { MessagesService } from "./messages.service";
import { Credentials } from "../structures/credentials";
import { Token } from "../structures/token";

@Injectable({ providedIn: 'root' })
export class HttpService {
  private authUrl: string;
  private pointsUrl: string;
  private token: string = '';

  constructor(
    private http: HttpClient,
    private messageService: MessagesService,
    @Inject('serverUrl') private serverUrl: string
  ) {
    this.authUrl = serverUrl + 'auth/';
    this.pointsUrl = serverUrl + 'points';
  }

  private get options() {
    return {
      headers: new HttpHeaders({ authorization: this.token })
    };
  }

  public get authorized(): boolean {
    return this.token !== '';
  }

  public logout() { this.token = '' }

  private handleError(error: HttpErrorResponse) {
    console.error('Ошибка HTTP (' + error.status + '). ' + error.message);
    this.messageService.add(error.message);
    return throwError(error);
  }

  login(credentials: Credentials): Observable<any> {
    return this.postCredentials('login', credentials);
  }

  register(credentials: Credentials): Observable<any> {
    return this.postCredentials('register', credentials);
  }

  private postCredentials(host: string, credentials: Credentials): Observable<any> {
    return this.http.post<Token>(this.authUrl + host, credentials).pipe(
      tap(response => this.token = response.token),
      catchError(this.handleError.bind(this))
    );
  }

  getPoints(): Observable<Point[]> {
    return this.http.get<Point[]>(this.pointsUrl, this.options).pipe(
      catchError(this.handleError.bind(this))
    );
  }

  postPoint(point: Point): Observable<Point> {
    return this.http.post<Point>(this.pointsUrl, point, this.options).pipe(
      catchError(this.handleError.bind(this))
    );
  }

  deletePoints(): Observable<any> {
    return this.http.delete(this.pointsUrl, this.options).pipe(
      catchError(this.handleError.bind(this))
    );
  }
}
