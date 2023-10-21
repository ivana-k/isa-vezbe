import { Component, OnInit } from '@angular/core';
import { User } from '../model/user.model';
import { UserService } from '../services/user.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css']
})
export class RegistrationComponent implements OnInit {

  constructor(private userService: UserService, private router: Router) { }

  userSync : User = {} as User;
  userAsync : User = {} as User;

  ngOnInit(): void {
  }

  onSubmit(event: Event, actionType: string) {
    event.preventDefault();

    var user = actionType == 'sync' ? this.userSync : this.userAsync;

    this.userService.registerUser(user, actionType).subscribe((data: string) =>  {
        if (data == "success") {
          this.router.navigateByUrl('/success');
        } else {
          alert(data)
        }
    }
      
      );
  }

}
