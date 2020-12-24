package com.cargomonitoring;

import android.Manifest;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.util.Log;
import android.widget.Toast;
import com.google.gson.Gson;
import retrofit2.Call;
import retrofit2.Callback;
import java.util.HashMap;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Sign_up extends AppCompatActivity implements LocationListener {

    private RetrofitInterface retrofitInterface;
    private Retrofit retrofit;
    private String BASE_URL = "http://192.168.0.13:3000/";
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    protected Context context;
    String provider;
    Double latitude, longitude;
    String lats,longs;
    protected boolean gps_enabled, network_enabled;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{ Manifest.permission.INTERNET},200);
        }

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//
            ActivityCompat.requestPermissions(this,new String[]{ Manifest.permission.ACCESS_FINE_LOCATION},200);
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},200);
            return;
        }else{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }

        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Sign_up.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    public void clickSignUp(View v){

        EditText txtName = findViewById(R.id.edtName);
        EditText txtEmail = findViewById(R.id.edtEmail);
        EditText txtPosition = findViewById(R.id.edtPosition);
        EditText txtPassword = findViewById(R.id.edtPassword);
        Button btnSignup = findViewById(R.id.btnSign);


        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                longitude = longitude * -1;

                HashMap<String , String> map = new HashMap<>();
                map.put("name",txtName.getText().toString());
                map.put("email",txtEmail.getText().toString());
                map.put("position_title",txtPosition.getText().toString());
                map.put("password",txtPassword.getText().toString());
                map.put("latitude", String.valueOf(latitude));
                map.put("longitude", String.valueOf(longitude));

                Call<Void> call = retrofitInterface.executeSign_up(map);

                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {

                        Gson gson = new Gson();
                        String json = gson.toJson(response.message());
                        System.out.println(json);


                        Toast.makeText(Sign_up.this,response.message(),
                                Toast.LENGTH_SHORT).show();

//                        if(response.message() == "OK"){
//                            Intent intent = new Intent(Sign_up.this , MainActivity.class);
//                            startActivity(intent);
//                        } else{
//
//                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(Sign_up.this,t.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude","Status");
        System.out.println("STATUS");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude","Enabled");
        System.out.println("Enabled");
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude","Disabled");
        System.out.println("Disabled");
    }



}