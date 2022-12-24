import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { Message } from '../model/message';
import { map } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class SocketService {
  url: string = environment.url + "api/socket";
  restUrl:string = environment.url + "/sendMessageRest";

  constructor(private http: HttpClient) { }

  post(data: Message) {
    return this.http.post<Message>(this.url, data)
      .pipe(map((data: Message) => { return data; }));
  }

  postRest(data: Message) {
    return this.http.post<Message>(this.restUrl, data)
      .pipe(map((data: Message) => { return data; }));
  }
}
