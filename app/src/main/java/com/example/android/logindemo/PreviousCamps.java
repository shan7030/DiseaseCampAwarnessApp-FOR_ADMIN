package com.example.android.logindemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Vector;

public class PreviousCamps extends AppCompatActivity {

    ListView listView;
    Vector<String> v=new Vector<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previous_camps);

        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("Camp");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot uniqueKeySnapshot : dataSnapshot.getChildren()) {

                    CampInfo campInfo=uniqueKeySnapshot.getValue(CampInfo.class);
                    if(campInfo.complete.equals("Yes"))
                        v.add("Camp Name :" +campInfo.name+"                                                 "+"Address :"+campInfo.Address);
                }
                listView = (ListView) findViewById(R.id.list_2);

                ArrayAdapter adapter = new ArrayAdapter<String>(PreviousCamps.this, R.layout.patient_list, v);

                listView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
