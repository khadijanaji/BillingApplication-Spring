import { Component, OnInit } from '@angular/core';
import {KeycloakSecurityService} from '../services/keycloak-security.service';
import {ActivatedRoute, Router} from '@angular/router';

@Component({
  selector: 'app-saveproduct',
  templateUrl: './saveproduct.component.html',
  styleUrls: ['./saveproduct.component.css']
})

export class SaveproductComponent implements OnInit {
  public product:any={id:0,name:"",price:0};
  public id:string="";
  public url:string="http://localhost:8888/INVENTORY-SERVICE/products/";
  constructor(private kcService:KeycloakSecurityService, private route: Router, private routeActive: ActivatedRoute) { }
  ngOnInit(): void {
    this.id=this.routeActive.snapshot.params['id'];
    if(this.id!=null)
      this.kcService.getById(this.url,this.id).subscribe(data=>{
        this.product=data;
      });
  }
  save() {
    if(this.id!=null){
      this.product.id=Number(this.product.id);
      this.kcService.update(this.url, this.product).subscribe(data=>{});
    }
    else {
      this.kcService.add(this.url, this.product).subscribe(data=>{});
    }
    setTimeout(() => {
      this.route.navigate(['products']);
    }, 2000);  //2s
  }
}
