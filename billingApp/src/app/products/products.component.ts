import { Component, OnInit } from '@angular/core';
import {KeycloakSecurityService} from '../services/keycloak-security.service';

@Component({
  selector: 'app-products',
  templateUrl: './products.component.html',
  styleUrls: ['./products.component.css']
})
export class ProductsComponent implements OnInit {
  public products:any;
  public errorMessage: any;
  private url:string="http://localhost:8888/INVENTORY-SERVICE/products/";
  constructor(private kcService:KeycloakSecurityService) { }
  ngOnInit() {
    this.onGetProducts();
  }
  onGetProducts() {
    this.kcService.getProducts().subscribe(data=>{
        let products:any=data;
        this.products=products._embedded.products;
      },
      err=>{ this.errorMessage=err.error.message;});
  }
  deleteProduct(id: number){
    if(confirm('Are you sur to delete this product?')) {
      this.products.forEach((element: any, index: number) => {
        if (element.productID == id)
          this.products.splice(index, 1);
      });
      this.kcService.delete(this.url,id).subscribe(data => {
      });
    }
  }

}
