import { Component, OnInit } from '@angular/core';
import {KeycloakSecurityService} from '../services/keycloak-security.service';

@Component({
  selector: 'app-bills',
  templateUrl: './bills.component.html',
  styleUrls: ['./bills.component.css']
})
export class BillsComponent implements OnInit {
  public bills:any;
  public errorMessage: any;
  private url:string="http://localhost:8888/BILLING-SERVICE/bills/full/";
  constructor(private kcService:KeycloakSecurityService) { }
  ngOnInit() {
    this.onGetBills();
  }
  onGetBills() {
    this.kcService.getBills().subscribe(data=>{
        this.bills=data;
        },
      err=>{ this.errorMessage=err.error.message;});
  }
  deleteBill(id: number){
    if(confirm('Are you sur to delete this bill?')) {
      this.bills.forEach((element: any, index: number) => {
        if (element.productID == id)
          this.bills.splice(index, 1);
      });
      this.kcService.delete(this.url,id).subscribe(data => {
      });
    }
  }
}
