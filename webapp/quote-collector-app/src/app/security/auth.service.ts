import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {UserAuthentication} from "./UserAuthentication";
import {BehaviorSubject, Observable, tap} from "rxjs";
import {Router} from "@angular/router";

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  $authentication: BehaviorSubject<UserAuthentication | null> = new BehaviorSubject<UserAuthentication | null>(null);

  constructor(private http: HttpClient,
              private router: Router) {

  }

  login(userName: string, password: string) {
    return this.http.post<UserAuthentication>('http://localhost:8080/api/v1/auth/login', {
        userName,
        password
      }
    ).pipe(
      tap(json => this.saveAuthentication(json))
    )
  }

  logout() {
    localStorage.removeItem("session");
    console.log('log out')
    this.$authentication.next(null);
    this.router.navigate(['/login']).then();
  }

  public isLoggedIn() {
    console.count('loggedin')
    return !!localStorage.getItem('session')
  }

  public getAuthentication(): Observable<UserAuthentication | null> {
    return this.$authentication.asObservable();
  }

  loadSession() {
    console.log("loading session...")
    const session = localStorage.getItem('session');
    if (session) {
      console.log("session restored", session)
      this.$authentication.next(JSON.parse(session));
    } else {
      console.log('no current session found in local storage')
    }
  }

  private saveAuthentication(authResult: UserAuthentication) {
    localStorage.setItem('session', JSON.stringify(authResult));
    this.$authentication.next(authResult)
  }
}
