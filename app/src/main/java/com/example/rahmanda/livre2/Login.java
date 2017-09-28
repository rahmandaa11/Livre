package com.example.rahmanda.livre2;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    EditText etUserid,etPassword;
    String userid, password;
    TextView register;

    public static final String TOKEN = "token";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUserid = (EditText) findViewById(R.id.etUserid);
        etPassword = (EditText) findViewById(R.id.etPassword);
        register = (TextView) findViewById(R.id.tvRegister);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               dialogRegister();
            }
        });

    }

    public void dialogRegister (){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);  //Dialog
        LayoutInflater inflater = getLayoutInflater();  //Dialog

        final View dialogView = inflater.inflate(R.layout.register, null); //Dialog
        dialogBuilder.setView(dialogView);  //Dialog

        final TextView tvRegister = (TextView) dialogView.findViewById(R.id.tvRegister);
        final Button buttonEmail = (Button) dialogView.findViewById(R.id.buttonEmail);

        final AlertDialog alertDialog = dialogBuilder.create();   //Dialog
        alertDialog.show(); //Dialog


        buttonEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setType("text/plain");
                startActivity(emailIntent);

                alertDialog.dismiss();
            }
        });


    }

    public void btnBackLSW (View view) {
        Intent i = new Intent(getApplicationContext(),MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    public void pindahHSW(View view) {
        userid = etUserid.getText().toString();
        password = etPassword.getText().toString();

        if(userid.equals("Panitia") && password.equals("Livre123")){
            Intent i = new Intent(getApplicationContext(),Home.class);
            i.putExtra(TOKEN, "1");
            startActivity(i);
        }
        else {
            Toast.makeText(getApplicationContext(), "Gagal Masuk",Toast.LENGTH_SHORT).show();
        }
    }

    public void btnRegister(View view){

    }
}
