import { Component } from '@angular/core';
import {HttpService} from "../../services/http.service";
import {MessagesService} from "../../services/messages.service";
import {Router} from "@angular/router";

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

    this.httpService.registration(this.login, this.password).subscribe(
      answer => {
        if (answer) this.router.navigate(['/main']);
        else this.addMessage('Такой логин уже существует.');
      },
      error => this.addMessage(error)
    );
  }

  authorization() {
    this.messagesService.clear();

    if (this.login === '')
      this.addMessage('Логин не может быть пустой строкой.');
    if (this.password === '')
      this.addMessage('Пароль не может быть пустой строкой.');

    if (this.messagesService.messages.length > 0) return;

    this.httpService.authorization(this.login, this.password).subscribe(
      answer => {
        if (answer) this.router.navigate(['/main']);
        else this.addMessage('Неправильный логин или пароль.');
      },
      error => this.addMessage(error)
    );
  }
}
