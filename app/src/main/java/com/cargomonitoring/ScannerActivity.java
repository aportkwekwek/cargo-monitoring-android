package com.cargomonitoring;

import android.Manifest;
import android.content.pm.PackageManager;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ScannerActivity extends AppCompatActivity {

    private RetrofitInterface retrofitInterface;
    private Retrofit retrofit;
    private String BASE_URL = "http://192.168.0.13:3000/";
    private CodeScanner mCodeScanner;
    public static String _scannedId;
    private static String driver;
    private static String task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);


        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);


        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{ Manifest.permission.CAMERA},200);
        }

        CodeScannerView scannerView = findViewById(R.id.scannerView);
        mCodeScanner = new CodeScanner(this, scannerView);
        mCodeScanner.getCamera();
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
               ScannerActivity.this.runOnUiThread(new Runnable() {
                   @Override
                   public void run() {

                       _scannedId = result.toString();
                       Tasks tasks = new Tasks(_scannedId);
                       Call<TaskModel> call = retrofitInterface.getCertainTask(tasks);
                       call.enqueue(new Callback<TaskModel>() {
                           @Override
                           public void onResponse(Call<TaskModel> call, Response<TaskModel> response) {
                               if(response.isSuccessful()){

                                   task = response.body().getTask();
                                   driver = response.body().getDriver();

                                   TextView txtTask = findViewById(R.id.txtTaskSaved);
                                   TextView txtDriver = findViewById(R.id.txtDriverName);

                                   txtTask.setText(task);
                                   txtDriver.setText(driver);

                               }else{
                                   Toast.makeText(ScannerActivity.this, response.message(),
                                           Toast.LENGTH_LONG).show();
                               }
                           }
                           @Override
                           public void onFailure(Call<TaskModel> call, Throwable t) {
                               Toast.makeText(ScannerActivity.this, t.getMessage(),
                                       Toast.LENGTH_LONG).show();
                           }
                       });

                   }
               });
            }
        });
        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCodeScanner.startPreview();
            }
        });

        findViewById(R.id.btnBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScannerActivity.this, Dashboard.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume(){
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    protected void onPause(){
        mCodeScanner.releaseResources();
        super.onPause();
    }
}