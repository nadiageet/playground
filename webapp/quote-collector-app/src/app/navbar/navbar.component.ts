import {Component} from '@angular/core';
import {AuthService} from "../security/auth.service";
import {map, Observable, tap} from "rxjs";

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent {
  $auth: Observable<boolean>;


  constructor(public authService: AuthService) {
    this.$auth = authService.getAuthentication()
      .pipe(
        tap(user => {
          console.log(user)
          return user;
        }),
        map(user => !!user)
      )
  }

}
