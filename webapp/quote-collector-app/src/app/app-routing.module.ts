import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {MyCollectionComponent} from "./my-collection/my-collection.component";

const routes: Routes = [
  {
    path: '',
    component: MyCollectionComponent,
    title: 'my collection'
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
