package com.example.cybercell.bms_upload_pic;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

/**
 * Created by vishnu on 8/1/2016.
 */
public class Firstactivity extends AppCompatActivity
{
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.firstactivity);

        View.OnClickListener listnr=new View.OnClickListener() {

            @Override

            public void onClick(View v) {

                Intent i= new Intent("LoginActivity");

                startActivity(i);

            }

        };

        TextView txt =(TextView) findViewById(R.id.login);
        TextView txt1=(TextView) findViewById(R.id.report);

        txt.setOnClickListener(listnr);
        txt1.setOnClickListener(listnr);

    }

}

