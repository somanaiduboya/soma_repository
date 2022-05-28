package com.example.rentatent;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    Button find,rent,signout;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth = FirebaseAuth.getInstance();
        find = findViewById(R.id.findHome);
        rent = findViewById(R.id.rentHome);
        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent int1 = new Intent(MainActivity.this, FindActivity.class);
                startActivity(int1);
            }
        });
        rent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent int2 = new Intent(MainActivity.this, AddHome.class);
                startActivity(int2);
            }
        });
        signout=findViewById(R.id.signoutbtn);
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
                Intent i =new Intent(MainActivity.this,Login.class);
                startActivity(i);
                finish();
            }
        });
    }
    private void signOut() {
        FirebaseAuth.getInstance().signOut();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            String nameDisplay = user.getDisplayName();
            TextView username = findViewById(R.id.userId);
            username.setText(nameDisplay);
        }
        else{
            Intent i = new Intent(MainActivity.this, Login.class);
            startActivity(i);
            finish();
        }
    }
}