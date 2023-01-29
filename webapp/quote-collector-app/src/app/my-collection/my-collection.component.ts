import {Component} from '@angular/core';
import {QuoteClientService} from "../quote-client.service";

@Component({
  selector: 'app-my-collection',
  templateUrl: './my-collection.component.html',
  styleUrls: ['./my-collection.component.scss']
})
export class MyCollectionComponent {


  constructor(private quoteClient: QuoteClientService) {
  }

  fetchQuotes(): void {
    this.quoteClient.getMyCollection().subscribe(console.table);
  }
}
