import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {CustomersRoutingModule} from "./admin-routing.module";
import {AdminHomeComponent} from '../admin-home/admin-home.component';
import {CreateQuoteComponent} from '../create-quote/create-quote.component';
import {ReactiveFormsModule} from "@angular/forms";


@NgModule({
  declarations: [
    AdminHomeComponent,
    CreateQuoteComponent
  ],
  imports: [
    CommonModule,
    CustomersRoutingModule,
    ReactiveFormsModule
  ],
  exports: []
})
export class AdminModule {
}
