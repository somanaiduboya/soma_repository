package com.example.rentatent;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ContactOwner extends AppCompatActivity {
    String mobile="8309476308";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_owner);
        Intent i = this.getIntent();
        if(i!=null)
        {

            TextView ownerDetails = findViewById(R.id.home_name);
            mobile = i.getStringExtra("mobile");
            ownerDetails.setText("Owner Mobile Number : "+mobile);
        }

        Button b = findViewById(R.id.call);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPhone(mobile);
            }
        });
        Button b2 = findViewById(R.id.goback);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ContactOwner.this,MainActivity.class);
                startActivity(i);
            }
        });

}

    private void openPhone(String mobile) {
    }
    }