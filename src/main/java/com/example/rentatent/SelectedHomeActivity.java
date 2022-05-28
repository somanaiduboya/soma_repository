package com.example.rentatent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class SelectedHomeActivity extends AppCompatActivity {
    ProgressDialog pd;
    StorageReference storageReference;
    ImageView img;
    DatabaseReference myRef;
    String name,price,description,address,city,mobile;
    Double latitude,longitude;
    TextView h_name,h_price,h_address,h_city,h_desc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_home);
        img = findViewById(R.id.imageofhome);
        myRef= FirebaseDatabase.getInstance().getReference("Homes");
        Intent i = this.getIntent();
        if(i!=null)
        {

            h_name = findViewById(R.id.home_name);
            name = i.getStringExtra("name");
            h_name.setText(name);
            h_desc = findViewById(R.id.home_desc);
            description = i.getStringExtra("description");
            h_desc.setText(description);
            h_address = findViewById(R.id.home_address);
            address = i.getStringExtra("address");
            h_address.setText(address);
            h_city = findViewById(R.id.home_city);
            city = i.getStringExtra("city");
            h_city.setText(city);
            String image = i.getStringExtra("image");
            String available = i.getStringExtra("available");
            String mobile = i.getStringExtra("mobile");
            getImage(image);
            getHomesFromNCA(name,city,address);
            Button showLoc = findViewById(R.id.locate_home);
            showLoc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:"+latitude+","+longitude+"?z=18"));
                    startActivity(intent);
                }
            });
            Button reg = findViewById(R.id.rent_home);
            reg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent =new Intent(SelectedHomeActivity.this,GetHome.class);
                    intent.putExtra("name", name);
                    intent.putExtra("price", price);
                    intent.putExtra("address", address);
                    intent.putExtra("city", city);
                    intent.putExtra("mobile", mobile);
                    startActivity(intent);
                }
            });
        }
    }


    private void getImage(String image_name){
        pd= new ProgressDialog(SelectedHomeActivity.this);
        pd.setMessage("Fetching image..");
        pd.setCancelable(false);
        pd.show();
        storageReference = FirebaseStorage.getInstance().getReference("uploads/"+image_name);
        try {
            File lf = File.createTempFile("temp",".jpg");
            storageReference.getFile(lf).addOnSuccessListener(new OnSuccessListener< FileDownloadTask.TaskSnapshot >() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    if(pd.isShowing()){
                        pd.dismiss();
                    }
                    Bitmap bm = BitmapFactory.decodeFile((lf.getAbsolutePath()));
                    img.setImageBitmap(bm);
                }
            });

        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

    }
    private void getHomesFromNCA(String name_1,String city_1,String address_1) {
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
                                            //key=eachHome.getKey();
                                            latitude = Double.parseDouble(String.valueOf(eachHome.child("latitude").getValue()));
                                            longitude = Double.parseDouble(String.valueOf(eachHome.child("longitude").getValue()));
                                            h_price = findViewById(R.id.home_price);
                                            price = String.valueOf(eachHome.child("price").getValue());
                                            h_price.setText("₹"+price+"/Month");
                                            index++;
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        Toast.makeText(SelectedHomeActivity.this, "Data Does not Exists", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(SelectedHomeActivity.this, "Failure Occured", Toast.LENGTH_SHORT).show();
                }
            }
        });
        myRef.get().addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SelectedHomeActivity.this,"No Data Found",Toast.LENGTH_SHORT);
            }
        });

    }

}