import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { LayoutModule } from '@angular/cdk/layout';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';

import { AppComponent } from '../components/app.component';
import { AppRoutingModule } from "./app-routing.module";
import { LoginComponent } from '../components/login/login.component';
import { PageNotFoundComponent } from '../components/page-not-found/page-not-found.component';
import { MainComponent } from '../components/main/main.component';
import { FormComponent } from '../components/main/form/form.component';
import { CanvasComponent } from '../components/main/canvas/canvas.component';
import { ResultsComponent } from '../components/main/results/results.component';
import { MessagesComponent } from '../components/messages/messages.component';
import {ScreenSizeService} from "../services/screen-size.service";

@NgModule({
  imports: [
    BrowserModule,
    AppRoutingModule,
    LayoutModule,
    FormsModule,
    HttpClientModule,
  ],
  declarations: [
    AppComponent,
    LoginComponent,
    PageNotFoundComponent,
    MainComponent,
    FormComponent,
    CanvasComponent,
    ResultsComponent,
    MessagesComponent,
  ],
  bootstrap: [ AppComponent ]
})
export class AppModule { }
