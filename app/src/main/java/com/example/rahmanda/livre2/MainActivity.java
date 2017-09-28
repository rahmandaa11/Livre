package com.example.rahmanda.livre2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button btnSupporter;
    public static final String TOKEN = "token";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSupporter = (Button) findViewById(R.id.btnSupporter);

        btnSupporter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,Home.class);
                i.putExtra(TOKEN, "0");
                startActivity(i);
            }
        });
    }

    public void pindahSW(View view) {
        Intent i = new Intent(MainActivity.this,Login.class);
        startActivity(i);
    }

}
