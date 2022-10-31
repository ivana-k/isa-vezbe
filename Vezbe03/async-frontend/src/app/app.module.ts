import { BrowserModule } from '@angular/platform-browser';
import { APP_INITIALIZER, NgModule } from '@angular/core';

import { AppConfig } from './services/app.config.service';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { RegistrationComponent } from './registration/registration.component';
import { SuccessMessageComponent } from './success-message/success-message.component';

import {HttpClientModule} from '@angular/common/http'

import { FormsModule } from '@angular/forms';
@NgModule({
  declarations: [
    AppComponent,
    RegistrationComponent,
    SuccessMessageComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule
  ],
  providers: [AppConfig,
    { 
      provide: APP_INITIALIZER, 
      useFactory: initConfig, 
      deps: [AppConfig], 
      multi: true 
    }],
  bootstrap: [AppComponent]
})
export class AppModule { }

export function initConfig(config: AppConfig) {
  return () => config.load();
}
