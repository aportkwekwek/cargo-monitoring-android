package com.cargomonitoring;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.EditText;
import android.widget.Toast;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
    private RetrofitInterface retrofitInterface;
    private Retrofit retrofit;
    private String BASE_URL = "http://192.168.0.13:3000/";
    private static String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);


        findViewById(R.id.btnLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText txtUsername = findViewById(R.id.edtUsername);
                EditText txtPassword = findViewById(R.id.edtPassword);

                Login login = new Login(txtUsername.getText().toString() , txtPassword.getText().toString());

                Call<User> call  = retrofitInterface.executeLogin(login);
                call.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if(response.isSuccessful()){

                            token = response.body().getToken();

                            getSecretToken();

                            Intent intent = new Intent(MainActivity.this, Dashboard.class);
                            startActivity(intent);

                        }else {
                            Toast.makeText(MainActivity.this, response.message(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                            Toast.makeText(MainActivity.this, t.getMessage(),
                                    Toast.LENGTH_LONG).show();
                    }
                });

            }
        });
    }

    private void getSecretToken(){
        Call<ResponseBody> call = retrofitInterface.getSecret(token);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    try{
//                        Toast.makeText(MainActivity.this,response.body().toString(),
//                                Toast.LENGTH_SHORT).show();
                    }catch (Exception e ){
//                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    public void onLogin(View v){
        v.startAnimation(buttonClick);
    }

    public void signUp(View v){
        try {
            Intent intent = new Intent(MainActivity.this, Sign_up.class);
            startActivity(intent);
        } catch(Exception e){
            e.printStackTrace();
        }
    }

}