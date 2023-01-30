import {Component, OnInit} from '@angular/core';
import {AuthService} from "../security/auth.service";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {Router} from "@angular/router";
import {catchError} from "rxjs";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
  form: FormGroup;


  constructor(private authService: AuthService,
              private router: Router) {
    this.form = new FormGroup({
      userName: new FormControl('', Validators.required),
      password: new FormControl('', Validators.required)
    })
  }

  ngOnInit() {


  }

  onSubmit(e: Event) {
    e.preventDefault();
    this.authService.login(this.form.get('userName')?.value, this.form.get('password')?.value)
      .pipe(catchError(e => {
        alert(JSON.stringify(e));
        return e
      }))
      .subscribe(() => this.accessMainContent())
  }

  private accessMainContent() {
    return this.router.navigate(['/']);
  }
}
