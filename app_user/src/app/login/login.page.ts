import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, FormControl, Validators } from '@angular/forms';
@Component({
  selector: 'app-login',
  templateUrl: './login.page.html',
  styleUrls: ['./login.page.scss'],
})
export class LoginPage implements OnInit {

  loginForm: FormGroup;

  error_messages = {
    'email': [
      {type: 'required', message: 'Email is required.'},
      {type: 'minlength', message: 'Email length must be longer or equal than 6 characters.'},
      {type: 'maxlength', message: 'Email length must be lower or equal than 50 characters.'},
      {type: 'pattern', message: 'Please enter a valid email address.'}
    ],
    'password': [
      {type: 'required', message: 'Password is required.'},
      {type: 'minlength', message: 'Password length must be longer or equal than 6 characters.'},
      {type: 'maxlength', message: 'Password length must be lower or equal than 50 characters.'},
      {type: 'pattern', message: 'Password must contain numbers, uppercase and lowercase characters.'}
    ],
  }

  constructor(
    public formBuilder: FormBuilder
  ) {
    this.loginForm = this.formBuilder.group({
      password: new FormControl('', Validators.compose([
        Validators.required,
        Validators.minLength(6),
        Validators.maxLength(30),
        Validators.pattern('^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])[a-zA-Z0-9]+$')
    ])),
      email: new FormControl('',Validators.compose([
        Validators.required,
        Validators.minLength(6),
        Validators.maxLength(50),
        Validators.pattern('^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+.[a-zA-Z0-9-.]+$')
      ]))
    });
   }

  ngOnInit() {
  }

  login(){
    console.log('email: ',this.loginForm.value.email);
    console.log('password: ',this.loginForm.value.password);
  }
}
