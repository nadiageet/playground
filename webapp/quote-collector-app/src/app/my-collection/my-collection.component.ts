import {Component} from '@angular/core';

@Component({
  selector: 'app-my-collection',
  templateUrl: './my-collection.component.html',
  styleUrls: ['./my-collection.component.scss']
})
export class MyCollectionComponent {


  fetchQuotes(): void {
    fetch("http://localhost:8080/quotes")
      .then(res => res.json())
      .then(console.log)
      .catch(console.log);
  }
}
