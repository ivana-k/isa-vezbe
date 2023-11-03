import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from "rxjs/operators";
import { User } from '../model/user.model';
import { BaseService } from './base.service';

@Injectable({
  providedIn: 'root'
})
export class UserService extends BaseService {

  constructor(private http: HttpClient) { 
    super()
  }

  

  registerUser(u: User, actionType: string): Observable<string> {
    return this.http.post( BaseService.appUrl + `/signup/` + actionType, u, { responseType: 'text'}).pipe(map((data: any) => {
      return data;
    }));
  }
}
