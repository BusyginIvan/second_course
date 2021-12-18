import { Component } from '@angular/core';
import { MessagesService } from "../../services/messages.service";

@Component({
  selector: 'app-messages',
  template: `
    <div id="messages">
      <!--<h1>Здесь будут отображаться сообщения об ошибках.</h1>-->
      <p *ngFor='let message of messageService.messages'> {{message}} </p>
    </div>
  `,
  styleUrls: ['./messages.component.sass']
})
export class MessagesComponent {
  constructor(public messageService: MessagesService) {}
}
