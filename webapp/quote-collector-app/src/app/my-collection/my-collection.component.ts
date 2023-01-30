import {Component, OnInit} from '@angular/core';
import {QuoteClientService} from "../quote-client.service";
import {BehaviorSubject} from "rxjs";
import {Quotedex} from "./Quotedex";

@Component({
  selector: 'app-my-collection',
  templateUrl: './my-collection.component.html',
  styleUrls: ['./my-collection.component.scss']
})
export class MyCollectionComponent implements OnInit {
  $quotes: BehaviorSubject<Quotedex[]> = new BehaviorSubject<Quotedex[]>([]);


  constructor(private quoteClient: QuoteClientService) {
  }

  ngOnInit(): void {
    this.quoteClient.getMyCollection().subscribe(json => this.$quotes.next(json))
  }
}
