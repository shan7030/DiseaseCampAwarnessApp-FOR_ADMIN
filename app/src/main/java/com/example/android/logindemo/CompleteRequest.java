package com.example.android.logindemo;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


class RequestInfo {
    public String Address;
    public Double Latitude;
    public Double Longitude;
    public String Disease;
    public String uid;
    public String checked;

    RequestInfo()
    {
        this.checked="No";
    }
}
public class CompleteRequest extends AppCompatActivity {

    TextView t1,t2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_request);
        Log.v("CompleteRequest",""+RequestPasser.datetime);
        t1=(TextView)findViewById(R.id.disease);
        t2=(TextView)findViewById(R.id.address);
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("Request/"+RequestPasser.datetime);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                RequestInfo ri=dataSnapshot.getValue(RequestInfo.class);
                t1.setText("DISEASE :"+ri.Disease);
                t2.setText("ADDRESS :"+ri.Address);
                NumberPasser.number=ri.uid;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(CompleteRequest.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void completerequest(View view)
    {
        final DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("Request/"+RequestPasser.datetime);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                RequestInfo ri=dataSnapshot.getValue(RequestInfo.class);

                ri.checked="Yes";
                databaseReference.setValue(ri);
                Toast.makeText(CompleteRequest.this, "Successfully Took this request into Consideration!", Toast.LENGTH_SHORT).show();
                Intent appInfo = new Intent(CompleteRequest.this,SeeRequests.class);
                startActivity(appInfo);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(CompleteRequest.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void sendmessage(View view)
    {
            DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child("CitizenInfo/"+NumberPasser.number);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                UserProfile up=dataSnapshot.getValue(UserProfile.class);
                String number=up.userAge;

                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                sendIntent.setData(Uri.parse("sms:"+number));
                String ft="You Request is took into Consideration!!";
                sendIntent.putExtra("sms_body",ft);
                startActivity(sendIntent);

                Toast.makeText(CompleteRequest.this, ""+number, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(CompleteRequest.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void seeaddress(View view)
    {
        final DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("Request/"+RequestPasser.datetime);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                RequestInfo d=dataSnapshot.getValue(RequestInfo.class);
                Double lat=d.Latitude;
                Double longp=d.Longitude;

                String label = "Address to set the campaign!!";
                String uriBegin = "geo:"+lat+","+longp;
                String query = lat+","+longp+"(" + label + ")";
                String encodedQuery = Uri.encode( query  );
                String uriString = uriBegin + "?q=" + encodedQuery;
                Uri uri = Uri.parse( uriString );
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, uri );
                startActivity( intent );

                Toast.makeText(CompleteRequest.this, "Going to the address !!", Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(CompleteRequest.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
