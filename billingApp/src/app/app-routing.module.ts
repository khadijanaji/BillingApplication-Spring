import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {ProductsComponent} from './products/products.component';
import {BillsComponent} from './bills/bills.component';
import {CustomersComponent} from './customers/customers.component';
import {SaveproductComponent} from './saveproduct/saveproduct.component';
import {SavecustomerComponent} from './savecustomer/savecustomer.component';
import {SavebillComponent} from './savebill/savebill.component';

const routes: Routes = [
  { path: 'bills' , component : BillsComponent },
  { path: 'bills/get' , component : SavebillComponent },
  { path: 'bills/get/:id' , component : SavebillComponent },
  { path: 'products', component : ProductsComponent },
  { path: 'products/get', component : SaveproductComponent },
  { path: 'products/get/:id', component : SaveproductComponent },
  { path: 'customers', component : CustomersComponent },
  { path: 'customers/get', component : SavecustomerComponent },
  { path: 'customers/get/:id', component : SavecustomerComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
