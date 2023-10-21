import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { SuccessMessageComponent } from './success-message/success-message.component';
import { AppComponent } from './app.component';
import { RegistrationComponent } from './registration/registration.component';

const routes: Routes = [
  {path: "success", component: SuccessMessageComponent} ,
  {path: "registration", component: RegistrationComponent} ,
  {path: "", redirectTo: "/registration", pathMatch: "full"}



];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {  



}
