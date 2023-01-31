import {RouterModule, Routes} from "@angular/router";
import {NgModule} from "@angular/core";
import {AdminHomeComponent} from "../admin-home/admin-home.component";
import {CreateQuoteComponent} from "../create-quote/create-quote.component";

const routes: Routes = [
  {
    path: '',
    component: AdminHomeComponent,
    title: 'admin home'
  },
  {
    path: 'create-quote',
    component: CreateQuoteComponent,
    title: 'create quote'
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class CustomersRoutingModule {
}
