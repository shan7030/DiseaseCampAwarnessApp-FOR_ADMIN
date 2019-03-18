package com.example.android.logindemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Vector;

public class FAQ extends AppCompatActivity {
    public FirebaseAuth firebaseAuth;
    ListView listView;
    Vector<String> v=new Vector<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);
        firebaseAuth = FirebaseAuth.getInstance();

        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("FAQ");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot uniqueKeySnapshot : dataSnapshot.getChildren()) {

                    String q = uniqueKeySnapshot.getKey();
                    String ans = uniqueKeySnapshot.getValue(String.class);
                    q="Q. "+q;
                    ans="-->"+ans;
                    v.add(q+"                                     "+"                                     "+ans);
                }
                listView = (ListView) findViewById(R.id.list_2);

                ArrayAdapter adapter = new ArrayAdapter<String>(FAQ.this, R.layout.patient_list, v);

                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void Logout(){
        firebaseAuth.signOut();
        finish();
        startActivity(new Intent(FAQ.this, MainActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menuforfaq, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.logoutMenu:{
                Logout();
                break;
            }
            case R.id.Addfaq:{
                startActivity(new Intent(FAQ.this, AddFAQ.class));
                break;
            }
            case R.id.profileMenu:
                startActivity(new Intent(FAQ.this, ProfileActivity.class));
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}
