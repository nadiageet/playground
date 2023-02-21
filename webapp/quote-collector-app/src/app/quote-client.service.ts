import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Quotedex} from "./my-collection/Quotedex";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class QuoteClientService {

  constructor(private http: HttpClient) {
  }

  getMyCollection(): Observable<Quotedex[]> {
    return this.http.get<Quotedex[]>('http://localhost:8080/api/v2/quotedex');
  }

  createQuote(content: string) {
    return this.http.post('http://localhost:8080/api/v1/quote', {content});
  }

  registerRandomQuote() {
    return this.http.post('http://localhost:8080/api/v1/quoteRegistration/random', null);
  }
}
