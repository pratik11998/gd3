package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity implements View.OnClickListener {
    EditText num,pass;
    Button login;
    TextView newuser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        num=findViewById(R.id.numberlogin);
        num.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        pass=findViewById(R.id.lgtxtPwd);
        login=findViewById(R.id.Login);
        newuser=findViewById(R.id.lnkreister);
        login.setOnClickListener(this);
        newuser.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.Login) {

        }
        if (v.getId() == R.id.lnkreister) {
            Intent intent=new Intent(Login.this,Signup.class);
            startActivity(intent);
        }
    }



}