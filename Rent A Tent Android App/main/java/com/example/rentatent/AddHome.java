package com.example.rentatent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AddHome extends AppCompatActivity {
    Button rent_it_now;
    Uri uri_img;
    int f;
    ImageView new_home_imageview;
    EditText hname;
    EditText hdesc;
    EditText haddress;
    EditText hcity;
    EditText hlat;
    EditText hprice;
    EditText hlon;
    EditText hmobile;
    String image,name,description,address,city,homeid,mobile;
    int price,PPR=1;
    int ink;
    String new_home_url;
    double latitude,longitude;
    final int IMAGE_REQUEST=2;
    DatabaseReference myRef,myRef1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_home);
        myRef=FirebaseDatabase.getInstance().getReference("Homes");
        hname = findViewById(R.id.new_homename);
        hprice = findViewById(R.id.new_homeprice);
        hdesc=findViewById(R.id.new_homedescription);
        haddress=findViewById(R.id.new_homeaddress);
        hcity=findViewById(R.id.new_homecity);
        hlat=findViewById(R.id.new_homelatitude);
        hlon=findViewById(R.id.new_homelongitude);
        hmobile = findViewById(R.id.new_homecontact);
        new_home_imageview = findViewById(R.id.new_home_ImageView);
        new_home_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImage();
            }
        });
        rent_it_now = findViewById(R.id.new_home_register);
        rent_it_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = hname.getText().toString();
                price = Integer.parseInt(hprice.getText().toString());
                description = hdesc.getText().toString();
                address= haddress.getText().toString();
                city = hcity.getText().toString();
                latitude = Double.parseDouble(hlat.getText().toString());
                longitude = Double.parseDouble(hlon.getText().toString());
                mobile = hmobile.getText().toString();
                uploadHome(name,price,description,address,city,latitude,longitude,new_home_url,mobile);
            }
        });
    }

    private void uploadHome(String name, int price, String description, String address, String city, double latitude, double longitude, String new_home_url,String mobile) {
        myRef =FirebaseDatabase.getInstance().getReference("Homes/homecount");
        myRef.get().addOnSuccessListener(new OnSuccessListener< DataSnapshot >() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                ink = dataSnapshot.getValue(Integer.class);
                f= ink+1;
                homeid = "RAT"+f;
                myRef1 =FirebaseDatabase.getInstance().getReference("Homes/"+homeid);
                myRef1.child("name").setValue(name);
                myRef1.child("available").setValue("yes");
                myRef1.child("price").setValue(price);
                myRef1.child("description").setValue(description);
                myRef1.child("address").setValue(address);
                String c = city.toString().trim();
                myRef1.child("city").setValue(c);
                myRef1.child("latitude").setValue(latitude);
                myRef1.child("longitude").setValue(longitude);
                myRef1.child("mobile").setValue(mobile);
                myRef1.child("image").setValue(homeid+".jpg");
                String email =FirebaseAuth.getInstance().getCurrentUser().getEmail();
                myRef1.child("owner").setValue(email);
                uploadImg();
                myRef.setValue(ink+1);
            }
        });
        /*
        int value = dataSnapshot.getResult().getValue(Integer.class);


         */
    }

    private void openImage() {
        Intent i =new Intent();
        i.setType("image/*");
        i.setAction(i.ACTION_GET_CONTENT);
        startActivityForResult(i,IMAGE_REQUEST);
    }
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==IMAGE_REQUEST && resultCode==RESULT_OK){
            uri_img = data.getData();
            new_home_imageview.setImageURI(uri_img);
        }
    }

    private void uploadImg() {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Uploading...");
        pd.show();
        if(uri_img!=null){
            image=homeid+".jpg";
            StorageReference fileRef = FirebaseStorage.getInstance().getReference().child("uploads").child(image);
            fileRef.putFile(uri_img).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            new_home_url = uri.toString();
                            pd.dismiss();
                            Toast.makeText(AddHome.this, "Home Successfully Registered", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(AddHome.this, MainActivity.class);
                            startActivity(i);
                            finish();
                        }
                    });
                }
            });
        }
    }

    private String getExtension(Uri uri_img) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mtm = MimeTypeMap.getSingleton();
        return mtm.getExtensionFromMimeType(cr.getType(uri_img));
    }
}