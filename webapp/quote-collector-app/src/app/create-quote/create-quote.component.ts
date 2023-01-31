import {Component} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {QuoteClientService} from "../quote-client.service";
import {tap} from "rxjs";

@Component({
  selector: 'app-create-quote',
  templateUrl: './create-quote.component.html',
  styleUrls: ['./create-quote.component.scss']
})
export class CreateQuoteComponent {

  form: FormGroup;


  constructor(private quoteClientService: QuoteClientService) {
    this.form = new FormGroup({
      quote: new FormControl('', [Validators.required])
    });
  }

  submit(event: Event) {
    event.preventDefault();
    this.quoteClientService.createQuote(this.form.get('quote')?.value)
      .pipe(tap(() => this.form.get('quote')?.reset()))
      .subscribe(() => alert('OK'))
  }
}
