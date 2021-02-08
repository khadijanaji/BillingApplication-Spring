import {Component, OnInit} from '@angular/core';
import {KeycloakSecurityService} from './services/keycloak-security.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements  OnInit{
  title = 'billingApp';
  isAdmin: boolean | undefined;

  constructor(public kcService:KeycloakSecurityService){}

  ngOnInit(): void {
    // @ts-ignore
    this.isAdmin=this.kcService.kc.hasRealmRole("data-manager");
  }

  onLogout() {
    // @ts-ignore
    this.kcService.kc.logout();
  }
  onChangePassword() {
    // @ts-ignore
    this.kcService.kc.accountManagement();
  }
  onLogin() {
    // @ts-ignore
    this.kcService.kc.login();
  }
}
