package com.example.android.logindemo;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class DiseaseInfo extends AppCompatActivity {
    TextView t1,t2,t3,t4,t5;
    private ImageView profilePic;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseStorage firebaseStorage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disease_info);
        t1=(TextView)findViewById(R.id.Disease);
        t2=(TextView)findViewById(R.id.tablets);
        t3=(TextView)findViewById(R.id.symptoms);
        t4=(TextView)findViewById(R.id.precautions);
        t5=(TextView)findViewById(R.id.emergencyhelpline);
        profilePic = findViewById(R.id.ivProfilePic);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        final DatabaseReference databaseReference = firebaseDatabase.getReference().child("Diseases").child(DiseasePasser.DiseaseName);
        Log.v("DiseaseInfo","sdfd"+DiseasePasser.DiseaseName);
        StorageReference storageReference = firebaseStorage.getReference();
        storageReference.child("Disease").child(DiseasePasser.DiseaseName).child("Images").child("Profile Pic").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).fit().centerCrop().into(profilePic);
            }
        });

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
              String name=dataSnapshot.getKey();
            Log.v("DiseaseInfo","sdfgjdsfdgf"+name);
            Disease d1=dataSnapshot.getValue(Disease.class);
            t1.setText("Disease Name :"+d1.disease);
            t2.setText("Tablets :"+d1.Tablets);
                t3.setText("Deal with it through Video ! ");
                t4.setText("Precautions :"+d1.Precautions);
                t5.setText("Emergency Helpline Number :1234567890");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(DiseaseInfo.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();
            }
        });



    }

    public void openphone(View view)
    {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:1234567890"));
        startActivity(intent);
    }

    public void openlink(View view)
    {
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child("Diseases").child(DiseasePasser.DiseaseName);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name=dataSnapshot.getKey();
                Log.v("DiseaseInfo","sdfgjdsfdgf"+name);
                Disease d1=dataSnapshot.getValue(Disease.class);
                String id=d1.Symptoms;
                Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
                Intent webIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://www.youtube.com/watch?v=" +id));
                try {
                    startActivity(appIntent);
                } catch (ActivityNotFoundException ex) {
                    startActivity(webIntent);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(DiseaseInfo.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
