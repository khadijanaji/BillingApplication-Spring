import { Component, OnInit } from '@angular/core';
import {KeycloakSecurityService} from '../services/keycloak-security.service';
import {ActivatedRoute, Router} from '@angular/router';

@Component({
  selector: 'app-savebill',
  templateUrl: './savebill.component.html',
  styleUrls: ['./savebill.component.css']
})
export class SavebillComponent implements OnInit {
  public bill:any={id:0,billingdate:new Date(),customerid:0,productItems:[]};
  public customers:any;
  public products:any;
  public id:string="";
  public url:string="http://localhost:8888/BILLING-SERVICE/bills/full/";

  constructor(private kcService:KeycloakSecurityService, private route: Router, private routeActive: ActivatedRoute) { }
  ngOnInit(): void {
    this.id=this.routeActive.snapshot.params['id'];
    if(this.id!=null)
      this.kcService.getById(this.url,this.id).subscribe(data=>{
        this.bill=data;
        this.bill.billingdate=new Date(this.bill.billingdate).toISOString().slice(0, 16);
      });
    this.kcService.getCustomers().subscribe(data=>{
      let customers:any=data;
      this.customers=customers._embedded.customers;
    });
    this.kcService.getProducts().subscribe(data=>{
      let products:any=data;
      this.products=products._embedded.products;
    });
  }
  save() {
    if(this.id!=null){
      this.bill.id=Number(this.bill.id);
      this.kcService.update(this.url, this.bill).subscribe(data=>{});
    }
    else {
      this.kcService.add(this.url, this.bill).subscribe(data=>{});
    }
    setTimeout(() => {
      this.route.navigate(['bills']);
    }, 2000);  //2s
  }
}
