package org.techtown.puppydiary;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import org.techtown.puppydiary.network.Data.CheckemailData;
import org.techtown.puppydiary.network.Data.SignupData;
import org.techtown.puppydiary.network.Response.CheckemailResponse;
import org.techtown.puppydiary.network.Response.SignupResponse;
import org.techtown.puppydiary.network.RetrofitClient;
import org.techtown.puppydiary.network.ServiceApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Signup extends AppCompatActivity {

    public static int set_flag;
    ActionBar actionBar;
    private ServiceApi service;
    private EditText email_check;
    private EditText pwd_check;
    private EditText pwd_cfr;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        set_flag = 0;
        actionBar = getSupportActionBar();
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xffD6336B));
        getSupportActionBar().setTitle("댕댕이어리");
        actionBar.setIcon(R.drawable.logo);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        service = RetrofitClient.getClient().create(ServiceApi.class);


        email_check = findViewById(R.id.tv_email);
        pwd_check = findViewById(R.id.tv_password);
        pwd_cfr = findViewById(R.id.tv_passwordcheck);

        // 중복 확인 버튼 눌렀을 때
        Button btn_check = findViewById(R.id.btn_emailcheck);
        btn_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = email_check.getText().toString();
                Log.e("email", "email "+ email);
                emailCheck(new CheckemailData(email));
            }
        });

        // 회원가입 버튼 눌렀을 때
        final Button btn_sign = findViewById(R.id.btn_signup);
        btn_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btn_sign.setBackgroundColor( Color.parseColor("#D6336B"));

                final String email = email_check.getText().toString();
                final String password = pwd_check.getText().toString();
                final String passwordConfirm = pwd_cfr.getText().toString();

                Log.e("email", "email "+ email);
                Log.e("pwd", "pwd "+ password);
                Log.e("pwdc", "pwdc "+ passwordConfirm);
                signupCheck(new SignupData(email, password, passwordConfirm) );
            }
        });
    }

    private void emailCheck(final CheckemailData data){
        service.checkemail(data).enqueue(new Callback<CheckemailResponse>() {
            @Override
            public void onResponse(Call<CheckemailResponse> call, Response<CheckemailResponse> response) {
                CheckemailResponse result =  response.body();

                Toast.makeText(Signup.this, result.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override public void onFailure(Call<CheckemailResponse> call, Throwable t) {
                Toast.makeText(Signup.this, "이메일이 중복되었습니다", Toast.LENGTH_SHORT).show();
                Log.e("이메일 중복 에러 발생", t.getMessage());
            }
        });
    }

    private void signupCheck(SignupData data){
        service.usersignup(data).enqueue(new Callback<SignupResponse>() {
            @Override
            public void onResponse(Call<SignupResponse> call, Response<SignupResponse> response) {
                SignupResponse result = response.body();
                Toast.makeText(Signup.this, result.getMessage(), Toast.LENGTH_SHORT).show();

                if (result.getSuccess() == true) {

                    String jwtToken = result.getJwtToken();

                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("TOKEN", jwtToken).apply();

                    Intent intent = new Intent(getApplicationContext(), SetPuppy.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<SignupResponse> call, Throwable t) {
                Toast.makeText(Signup.this, "회원가입 에러가 발생하였습니다.", Toast.LENGTH_SHORT).show();
                Log.e("회원가입 에러 발생", t.getMessage());
            }
        });
    }


}