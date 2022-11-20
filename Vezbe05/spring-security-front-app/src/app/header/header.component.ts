import { Component, OnInit } from '@angular/core';
import { AuthService } from '../service';
import {UserService} from '../service/user.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  constructor( private userService: UserService, private authService: AuthService) { }

  ngOnInit() {
  }

  hasSignedIn() {
    return !!this.userService.currentUser;
  }

  userName() {
    const user = this.userService.currentUser;
    return user.firstName + ' ' + user.lastName;
  }
  logout() {
    this.authService.logout();
  }
}
