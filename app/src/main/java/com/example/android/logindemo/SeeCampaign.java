package com.example.android.logindemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Vector;

public class SeeCampaign extends AppCompatActivity {

    public FirebaseAuth firebaseAuth;
    ListView listView;
    Vector<String> v=new Vector<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_campaign);

        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("Camp");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot uniqueKeySnapshot : dataSnapshot.getChildren()) {

                    CampInfo campInfo=uniqueKeySnapshot.getValue(CampInfo.class);
                    if(!campInfo.complete.equals("Yes"))
                    v.add(uniqueKeySnapshot.getKey());
                }
                listView = (ListView) findViewById(R.id.list_2);

                ArrayAdapter adapter = new ArrayAdapter<String>(SeeCampaign.this, R.layout.patient_list, v);

                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                    @Override
                                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                                                        DateTimePasser.datetime=(String)adapterView.getItemAtPosition(i);

                                                        Intent appInfo = new Intent(SeeCampaign.this, CampDetails.class);
                                                        startActivity(appInfo);
                                                    }
                                                }
                );
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void Logout(){
        firebaseAuth.signOut();
        finish();
        startActivity(new Intent(SeeCampaign.this, MainActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menuforseecamps, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.logoutMenu:{
                Logout();
                break;
            }
            case R.id.prevcamps:{
                startActivity(new Intent(SeeCampaign.this, PreviousCamps.class));
                break;
            }
            case R.id.profileMenu:
                startActivity(new Intent(SeeCampaign.this, ProfileActivity.class));
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}
