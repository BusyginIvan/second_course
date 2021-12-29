import { Component } from '@angular/core';
import { Router } from "@angular/router";

import { Credentials } from "../../structures/credentials";
import { HttpService } from "../../services/http.service";
import { MessagesService } from "../../services/messages.service";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.sass']
})
export class LoginComponent {
  login = ''; password = '';
  showMessages = false;

  constructor(
    private httpService: HttpService,
    private messagesService: MessagesService,
    private router: Router
  ) { }

  private get credentials(): Credentials {
    return { username: this.login, password: this.password };
  }

  private addMessage(message: string) {
    this.messagesService.add(message);
    this.showMessages = true;
  }

  registration() {
    this.messagesService.clear();

    if (this.login === '')
      this.addMessage('Логин не может быть пустой строкой.');
    if (this.password === '')
      this.addMessage('Пароль не может быть пустой строкой.');

    if (this.messagesService.messages.length > 0) return;

    this.httpService.register(this.credentials).subscribe(
      answer => this.router.navigate(['/main']),
      error => this.showMessages = true
    );
  }

  authorization() {
    this.messagesService.clear();

    if (this.login === '')
      this.addMessage('Логин не может быть пустой строкой.');
    if (this.password === '')
      this.addMessage('Пароль не может быть пустой строкой.');

    if (this.messagesService.messages.length > 0) return;

    this.httpService.login(this.credentials).subscribe(
      answer => this.router.navigate(['/main']),
      error => this.showMessages = true
    );
  }
}
