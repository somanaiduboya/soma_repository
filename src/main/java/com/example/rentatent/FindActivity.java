package com.example.rentatent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FindActivity extends AppCompatActivity {
    DatabaseReference myRef;
    ArrayList<RAT> arrayList;
    ListAdapter listAdapter;
    Button searchCity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        arrayList = new ArrayList<RAT>();
        super.onCreate(savedInstanceState);
        myRef = FirebaseDatabase.getInstance().getReference("Homes");
        setContentView(R.layout.activity_find);
        searchCity=findViewById(R.id.imageButtonsearch);
        searchCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText e = findViewById(R.id.searchView);
                getHomesFromCity(e.getText().toString());
            }
        });
        getAllHomes();
    }

    private void getAllHomes() {


        // Read from the database
        myRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().exists()) {
                        DataSnapshot d = task.getResult();
                        //RAT r = d.getValue(RAT.class);
                        String[] s = new String[( int ) d.getChildrenCount()];
                        int index=0;
                        arrayList.clear();
                        for (DataSnapshot postSnapshot: d.getChildren()) {
                            // TODO: handle the post
                            s[index] = postSnapshot.getKey();
                            DataSnapshot eachHome = d.child(s[index]);

                            String name, city, description, address;
                            int price;
                            if (!( eachHome.getKey().toString().equals("homecount") )) {
                                if (String.valueOf(eachHome.child("available").getValue()).equalsIgnoreCase("yes")){
                                name = String.valueOf(eachHome.child("name").getValue());
                                city = String.valueOf(eachHome.child("city").getValue());
                                address = String.valueOf(eachHome.child("address").getValue());
                                description = String.valueOf(eachHome.child("description").getValue());
                                String price1 = String.valueOf(eachHome.child("price").getValue());
                                price = Integer.parseInt(price1);
                                String image = String.valueOf(eachHome.child("image").getValue());
                                String mobile = String.valueOf(eachHome.child("mobile").getValue());
                                String available = String.valueOf(eachHome.child("available").getValue());

                                Double latitude = Double.parseDouble(String.valueOf(eachHome.child("latitude").getValue()));

                                Double longitude = Double.parseDouble(String.valueOf(eachHome.child("longitude").getValue()));
                                price = Integer.parseInt(price1);
                                arrayList.add(new RAT("" + address, "" + available,
                                        "" + city, "" + description, "" + name, "" + image, "" + mobile, price, latitude, longitude));
                                index++;
                            }
                        }
                        }

                            listAdapter = new ListAdapter(FindActivity.this, arrayList);
                            ListView listview = findViewById(R.id.listview);
                            listview.setAdapter(listAdapter);
                            listview.setClickable(true);
                            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView< ? > adapterView, View view, int i, long l) {
                                    Intent intent = new Intent(FindActivity.this, SelectedHomeActivity.class);
                                    intent.putExtra("name", arrayList.get(i).getName());
                                    intent.putExtra("price",""+ arrayList.get(i).getPrice());
                                    intent.putExtra("description", arrayList.get(i).getDescription());
                                    intent.putExtra("image", arrayList.get(i).getLink());
                                    intent.putExtra("address", arrayList.get(i).getAddress());
                                    intent.putExtra("city", arrayList.get(i).getCity());
                                    intent.putExtra("latitude", ""+arrayList.get(i).getLatitude());
                                    intent.putExtra("longitude", ""+arrayList.get(i).getLongitude());
                                    intent.putExtra("mobile", arrayList.get(i).getMobile());
                                    intent.putExtra("available", ""+arrayList.get(i).getAvailable());
                                    startActivity(intent);
                                }
                            });



                    }
                    else {
                        Toast.makeText(FindActivity.this, "Data Does not Exists", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(FindActivity.this, "Failure Occured", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void getHomesFromCity(String key) {
        myRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().exists()) {
                        DataSnapshot d = task.getResult();
                        //RAT r = d.getValue(RAT.class);
                        String[] s = new String[( int ) d.getChildrenCount()];
                        int index=0;
                        arrayList.clear();
                        for (DataSnapshot postSnapshot: d.getChildren()) {
                            // TODO: handle the post
                            s[index]=postSnapshot.getKey();
                            DataSnapshot eachHome = d.child(s[index]);

                            String name, city, description, address;
                            int price;
                            String k = key.toString().trim();
                            if(!(eachHome.getKey().toString().equals("homecount"))) {
                                if (String.valueOf(eachHome.child("city").getValue()).equalsIgnoreCase(k)) {
                                    if (String.valueOf(eachHome.child("available").getValue()).equalsIgnoreCase("yes")){
                                        name = String.valueOf(eachHome.child("name").getValue());
                                    city = String.valueOf(eachHome.child("city").getValue());
                                    address = String.valueOf(eachHome.child("address").getValue());
                                    description = String.valueOf(eachHome.child("description").getValue());
                                    String price1 = String.valueOf(eachHome.child("price").getValue());
                                    price = Integer.parseInt(price1);
                                    String image = String.valueOf(eachHome.child("image").getValue());
                                    String mobile = String.valueOf(eachHome.child("mobile").getValue());
                                    Double latitude = Double.parseDouble(String.valueOf(eachHome.child("latitude").getValue()));
                                    Double longitude = Double.parseDouble(String.valueOf(eachHome.child("longitude").getValue()));
                                    String available = String.valueOf(eachHome.child("available").getValue());
                                    arrayList.add(new RAT("" + address, "" + available,
                                            "" + city, "" + description, "" + name, "" + image, "" + mobile, price, latitude, longitude));
                                    index++;
                                }
                            }
                            }
                        }
                            listAdapter = new ListAdapter(FindActivity.this, arrayList);
                            ListView listview = findViewById(R.id.listview);
                            listview.setAdapter(listAdapter);
                            listview.setClickable(true);
                            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView< ? > adapterView, View view, int i, long l) {
                                    Intent intent = new Intent(FindActivity.this, SelectedHomeActivity.class);
                                    intent.putExtra("name", arrayList.get(i).getName());
                                    intent.putExtra("price", arrayList.get(i).getPrice());
                                    intent.putExtra("description", arrayList.get(i).getDescription());
                                    intent.putExtra("image", arrayList.get(i).getLink());
                                    intent.putExtra("address", arrayList.get(i).getAddress());
                                    intent.putExtra("city", arrayList.get(i).getCity());
                                    intent.putExtra("latitude", arrayList.get(i).getLatitude());
                                    intent.putExtra("longitude", arrayList.get(i).getLongitude());
                                    intent.putExtra("mobile", arrayList.get(i).getMobile());
                                    intent.putExtra("available", arrayList.get(i).getAvailable());
                                    startActivity(intent);
                                }
                            });
                    } else {
                        Toast.makeText(FindActivity.this, "Data Does not Exists", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(FindActivity.this, "Failure Occured", Toast.LENGTH_SHORT).show();
                }
            }
        });
        myRef.get().addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(FindActivity.this,"No Data Found",Toast.LENGTH_SHORT);
            }
        });

    }

}