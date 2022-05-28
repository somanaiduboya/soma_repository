package com.example.rentatent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class GetHome extends AppCompatActivity {

    DatabaseReference myRef;
    String name,price,address,mobile,city,key,msg;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_home);
        myRef = FirebaseDatabase.getInstance().getReference("Homes");
        Intent i = this.getIntent();
        user = FirebaseAuth.getInstance().getCurrentUser();
        if(i!=null) {

            name = i.getStringExtra("name");
            price = i.getStringExtra("price");
            address = i.getStringExtra("address");
            city = i.getStringExtra("city");
            mobile = i.getStringExtra("mobile");
            TextView t_agree = findViewById(R.id.agreement);
            msg = "   I "+getusername()+", want to book the home "+name+" with a rent of ₹"+price+"/ month by using Rent A Tent Online Home Rent App. I declare that i will not pay money until i visit the home at the specified Location. If I pay money, Rent A Tent is not responsible and I also confirm that if I book the home and I won't contact owner of home, I will pay the rent of one month ₹"+price+" to the owner of the home "+name;
            t_agree.setText(msg);
            getHomesFromHCA(name,city,address);
            Button confirm = findViewById(R.id.call);
            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DatabaseReference myRef1 =FirebaseDatabase.getInstance().getReference("Homes/"+key);
                    myRef1.child("available").setValue("no, user is : "+getuser());
                    sendMessage(mobile);
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:+91"+mobile));
                    startActivity(intent);
                    }
            });
        }
    }
    private void sendMessage(String mobileno) {
        try {
            String k = getusername();
            SmsManager smsManager=SmsManager.getDefault();
            smsManager.sendTextMessage(mobileno,null,"I want to book your home by using Rent A Tent \n Online Home Renting App.",null,null);
            Toast.makeText(getApplicationContext(),"Successfully Booked Home!",Toast.LENGTH_LONG).show();
        }catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Please give permissions to send messages", Toast.LENGTH_LONG).show();
        }
    }

    private void getHomesFromHCA(String name_1,String city_1,String address_1) {
        myRef.get().addOnCompleteListener(new OnCompleteListener< DataSnapshot >() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().exists()) {
                        DataSnapshot d = task.getResult();
                        //RAT r = d.getValue(RAT.class);
                        String[] s = new String[( int ) d.getChildrenCount()];
                        int index=0;
                        for (DataSnapshot postSnapshot: d.getChildren()) {
                            // TODO: handle the post
                            s[index]=postSnapshot.getKey();
                            DataSnapshot eachHome = d.child(s[index]);
                            if(!(eachHome.getKey().toString().equals("homecount"))) {
                                if (String.valueOf(eachHome.child("city").getValue()).equalsIgnoreCase(city_1)) {
                                    if (String.valueOf(eachHome.child("name").getValue()).equalsIgnoreCase(name_1)) {
                                        if (String.valueOf(eachHome.child("address").getValue()).equalsIgnoreCase(address_1)) {
                                            key=eachHome.getKey();
                                            index++;
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        Toast.makeText(GetHome.this, "Data Does not Exists", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(GetHome.this, "Failure Occured", Toast.LENGTH_SHORT).show();
                }
            }
        });
        myRef.get().addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(GetHome.this,"No Data Found",Toast.LENGTH_SHORT);
            }
        });

    }

    private String getuser() {
            String email = user.getEmail();
            return email;
    }
    private String getusername() {
        String name = user.getDisplayName();
        return name;
    }

}