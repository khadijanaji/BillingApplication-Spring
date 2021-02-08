import { BrowserModule } from '@angular/platform-browser';
import {APP_INITIALIZER, NgModule} from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { ProductsComponent } from './products/products.component';
import { CustomersComponent } from './customers/customers.component';
import { BillsComponent } from './bills/bills.component';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {KeycloakSecurityService} from './services/keycloak-security.service';
import {KeycloakHttpInterceptorService} from './services/keycloak-http-interceptor.service';
import { SavecustomerComponent } from './savecustomer/savecustomer.component';
import { SaveproductComponent } from './saveproduct/saveproduct.component';
import {FormsModule} from '@angular/forms';
import { SavebillComponent } from './savebill/savebill.component';

export function kcFactory(kcSecService:KeycloakSecurityService){
  return ()=>kcSecService.init();
}

@NgModule({
  declarations: [
    AppComponent,
    ProductsComponent,
    CustomersComponent,
    BillsComponent,
    SavecustomerComponent,
    SaveproductComponent,
    SavebillComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    AppRoutingModule,
    FormsModule,
  ],
  providers: [
    { provide: APP_INITIALIZER, deps: [KeycloakSecurityService], useFactory: kcFactory, multi: true},
    { provide: HTTP_INTERCEPTORS, useClass: KeycloakHttpInterceptorService, multi: true}
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
