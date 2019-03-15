import { Injectable } from '@angular/core';
import {Platform} from '@ionic/angular';
import {BehaviorSubject} from 'rxjs';

const TOKEN_KEY = 'auth-token';
@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {
  authenticationState = new BehaviorSubject(false);
  constructor(private storage: Storage, private plt: Platform) {
    this.plt.ready().then(() => {
      this.checkToken();
    })

  }
  login() {
    return this.storage.set(TOKEN_KEY, 'root 1234').then(res => {
      this.authenticationState.next(true);
    });

  }
  isAuthenticated() {
    return this.authenticationState.value;

  }
  checkToken() {
    return this.storage.get(TOKEN_KEY).then(res => {
      if (res) {
        this.authenticationState.next(true);
      }
    });
  }
}
