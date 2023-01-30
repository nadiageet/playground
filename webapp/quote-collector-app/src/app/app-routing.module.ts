import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {MyCollectionComponent} from "./my-collection/my-collection.component";
import {LoginComponent} from "./login/login.component";
import {AuthenticatedGuard} from "./security/authenticated.guard";

const routes: Routes = [
  {
    path: '',
    component: MyCollectionComponent,
    title: 'my collection',
    canActivate: [AuthenticatedGuard]
  },
  {
    path: 'login',
    component: LoginComponent,
    title: 'login'
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
