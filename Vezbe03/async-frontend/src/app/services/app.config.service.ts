import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { throwError } from 'rxjs';
import { environment } from '../../environments/environment';

import { BaseService } from './base.service';

@Injectable()
export class AppConfig {

  private config: Object = null;
  private env: Object = null;

  constructor(private http: HttpClient) {
  }

  /**
   * Use to get the data found in the second file (config file)
   */
  public getConfig(key: any) {
    return this.config[key];
  }

  /**
   * Use to get the data found in the first file (env file)
   */
  public getEnv(key: any) {
    return this.env[key];
  }

  /**
   * This method:
   *   a) Loads "env.json" to get the current working environment (e.g.: 'prod', 'dev')
   *   b) Loads "config.[env].json" to get all env's variables (e.g.: 'config.dev.json')
   */
  public load() {
    return new Promise((resolve, reject) => {
      this.http.get<any>('./assets/config.json').subscribe((responseData) => {
            this.config = responseData;
            BaseService.appUrl = this.config['apiUrl'];
            resolve(true);
        },
        (error: any) => {
            console.log('Configuration file "env.json" could not be read');
            resolve(error);
            return throwError(error.json().error || 'Server error');
        });

    });
  }

}
