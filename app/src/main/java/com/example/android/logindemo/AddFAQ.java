package com.example.android.logindemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddFAQ extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_faq);
    }

    public void addquestion(View view)
    {
        String q=((EditText)findViewById(R.id.question)).getText().toString().trim();
        String ans=((EditText)findViewById(R.id.answer)).getText().toString().trim();
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("FAQ").child(q);
        databaseReference.setValue(ans);
        Toast.makeText(AddFAQ.this, "Added Question Succesfully!!!!", Toast.LENGTH_SHORT).show();

        Intent i=new Intent(this,FAQ.class);
        startActivity(i);
    }
}
