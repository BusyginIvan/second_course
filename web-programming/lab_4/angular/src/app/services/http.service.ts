import { HttpClient, HttpErrorResponse, HttpHeaders } from "@angular/common/http";
import { Inject, Injectable } from "@angular/core";
import { catchError, Observable, tap, throwError } from "rxjs";
import { Point } from "../structures/point";
import { MessagesService } from "./messages.service";
import { Credentials } from "../structures/credentials";
import { Token } from "../structures/token";

const tokenKey = 'token';

@Injectable({ providedIn: 'root' })
export class HttpService {
  private authUrl: string;
  private pointsUrl: string;

  constructor(
    private http: HttpClient,
    private messageService: MessagesService,
    @Inject('serverUrl') private serverUrl: string
  ) {
    this.authUrl = serverUrl + 'auth';
    this.pointsUrl = serverUrl + 'points';
  }

  private get options() {
    if (this.token !== null)
      return { headers: new HttpHeaders({ authorization: this.token }) };
    else return { };
  }

  public authorized(): boolean { return this.token !== null }

  public logout() { this.token = null }

  private get token() { return localStorage.getItem(tokenKey) }

  private set token(token: string | null) {
    if (token) localStorage.setItem(tokenKey, token);
    else localStorage.removeItem(tokenKey);
  }

  private handleError(error: HttpErrorResponse) {
    if (error.status)
      this.messageService.add(error.error);
    else
      this.messageService.add('Ошибка соединения с сервером.');
    return throwError(error);
  }

  login(credentials: Credentials): Observable<any> {
    return this.postCredentials('login', credentials);
  }

  register(credentials: Credentials): Observable<any> {
    return this.postCredentials('register', credentials);
  }

  private postCredentials(host: string, credentials: Credentials): Observable<any> {
    return this.http.post<Token>(this.authUrl + '/' + host, credentials).pipe(
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
