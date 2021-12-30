import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { LoginComponent } from "../components/login/login.component";
import { PageNotFoundComponent } from "../components/page-not-found/page-not-found.component";
import { MainComponent } from "../components/main/main.component";
import { MainGuard } from "../components/main/main.guard";

const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'main', redirectTo: '/main' },
  { path: 'login', redirectTo: '/login' },
  { path: 'login', component: LoginComponent },
  { path: 'main', component: MainComponent, canActivate: [MainGuard] },
  { path: '**', component: PageNotFoundComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }

