import {Component} from '@angular/core';
import {AuthService} from "../security/auth.service";
import {Observable, tap} from "rxjs";
import {UserAuthentication} from "../security/UserAuthentication";
import {Router} from "@angular/router";

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent {
  $auth: Observable<UserAuthentication | null>;


  constructor(public authService: AuthService,
              private router: Router) {
    this.$auth = authService.getAuthentication()
      .pipe(
        tap(user => {
          console.log(user)
          return user;
        })
      )
  }

  redirectToAdminPanel() {
    this.router.navigate(['/admin']).then()
  }

  redirectToMainPage() {
    this.router.navigate(['/']).then()
  }
}
