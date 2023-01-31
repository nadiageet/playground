import {Component} from '@angular/core';
import {Router} from "@angular/router";
import {QuoteClientService} from "../quote-client.service";

@Component({
  selector: 'app-admin-home',
  templateUrl: './admin-home.component.html',
  styleUrls: ['./admin-home.component.scss']
})
export class AdminHomeComponent {


  constructor(private router: Router,
              private quoteService: QuoteClientService) {
  }

  redirectToCreateQuote() {
    this.router.navigate(['/admin/create-quote']).then()
  }

  winAQuote() {
    this.quoteService.registerRandomQuote().subscribe();
  }
}
