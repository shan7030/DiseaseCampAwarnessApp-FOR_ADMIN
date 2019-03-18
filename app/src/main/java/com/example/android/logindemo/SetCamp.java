package com.example.android.logindemo;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


class CampInfo
{
    public String name;
    public  String startdate;
    public String enddate;
    public String complete;
    public String Address;
    public Double Latitute;
    public Double Longitude;
    CampInfo()
    {
        this.complete="No";
    }
}
public class SetCamp extends AppCompatActivity {

    TextView t1, t2, t3;
    int PLACE_PICKER_REQUEST = 1;
    LatLng latlong;
    String address;
    Button btnChangeDate,btnChangeDate1;
    private int year;
    private int month;
    private int day;

    private FirebaseAuth firebaseAuth;

    private DatePicker dpResult;
    static final int DATE_DIALOG_ID = 999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_camp);

        t1 = (TextView) findViewById(R.id.startdate);
        t2 = (TextView) findViewById(R.id.enddate);
        t3 = (TextView) findViewById(R.id.address);
        firebaseAuth=FirebaseAuth.getInstance();

        btnChangeDate = (Button) findViewById(R.id.startdatebutton);

        btnChangeDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                t1=(TextView)findViewById(R.id.startdate);
                showDialog(DATE_DIALOG_ID);
            }
        });

        btnChangeDate1 = (Button) findViewById(R.id.enddatebutton);

        btnChangeDate1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                t1=(TextView)findViewById(R.id.enddate);
                showDialog(DATE_DIALOG_ID);

            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:

// set date picker as current date
                return new DatePickerDialog(this, datePickerListener,year, month,day);
        }
        return null;
    }
    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;

// set selected date into textview
            t1.setText(new StringBuilder().append(month + 1)
                    .append("-").append(day).append("-").append(year)
                    .append(" "));

// set selected date into datepicker also
       //     dpResult.init(year, month, day, null);
        }



    };



    private void Logout(){
        firebaseAuth.signOut();
        finish();
        startActivity(new Intent(SetCamp.this, MainActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.logoutMenu:{
                Logout();
                break;
            }
            case R.id.profileMenu:
                startActivity(new Intent(SetCamp.this, ProfileActivity.class));
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    public void toaddcamp(View view) {
    CampInfo c=new CampInfo();
    c.Address=address;
    c.Latitute=latlong.latitude;
    c.Longitude=latlong.longitude;
    c.name=((EditText)findViewById(R.id.name)).getText().toString().trim();
    c.startdate=((TextView)findViewById(R.id.startdate)).getText().toString().trim();
    c.enddate=((TextView)findViewById(R.id.enddate)).getText().toString().trim();

        Date cm = Calendar.getInstance().getTime();
        System.out.println("Current time => " + cm);

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
        String formattedDate = df.format(cm);

        Log.v("SetCamp",""+c.Address);
        Log.v("SetCamp",""+c.name);
        Log.v("SetCamp",""+c.startdate);
        Log.v("SetCamp",""+c.Address);
        Log.v("SetCamp",""+c.enddate);
        Log.v("SetCamp",""+formattedDate);
        Log.v("SetCamp",""+c.Longitude);



        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("Camp/"+formattedDate);
        databaseReference.setValue(c);
     Toast.makeText(SetCamp.this, "Successfully Added this camp!", Toast.LENGTH_SHORT).show();
      Intent appInfo = new Intent(SetCamp.this,SecondActivity.class);
      startActivity(appInfo);
    }





    public void selectaddress(View view) {

        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        try {
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);

        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);

                latlong = place.getLatLng();
                address = (String) place.getAddress();
                t3.setText("Address Selected :" + place.getAddress());

            }

        }
    }
}
