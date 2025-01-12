package edu.ewubd.cse489_2021_2_60_041;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import edu.ewubd.cse489_2021_2_60_041.ReportActivity;
import edu.ewubd.cse489_2021_2_60_041.SignupActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;

    private CheckBox cbRememberUser, cbRememberLogin;

    private Button btnDosentHaveAccount, btnLogin;

    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);

        cbRememberUser = findViewById(R.id.cbRememberUser);
        cbRememberLogin = findViewById(R.id.cbRememberLogin);

        btnDosentHaveAccount = findViewById(R.id.btnDosentHaveAccount);
        btnLogin = findViewById(R.id.btnLogin);

        sp = this.getSharedPreferences("my_sp", MODE_PRIVATE);
        String email = sp.getString("USER-EMAIL", "");
        String pass = sp.getString("PASSWORD", "");
        boolean remUser = sp.getBoolean("REMEMBER-USER", false);
        boolean remLogin = sp.getBoolean("REMEMBER-LOGIN", false);

        if(remUser){
            etEmail.setText(email);
            cbRememberUser.setChecked(true);

        }

        if (remLogin){
            etPassword.setText(pass);
            cbRememberLogin.setChecked(true);
        }



        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String e = etEmail.getText().toString().trim();
                String p = etPassword.getText().toString().trim();

                if(!e.equals(email)){
                    Toast.makeText(LoginActivity.this, "Email didn't match", Toast.LENGTH_LONG).show();
                    return;
                }

                if(!p.equals(pass)){
                    Toast.makeText(LoginActivity.this, "Password didn't match", Toast.LENGTH_LONG).show();
                    return;
                }

                System.out.println(email);
                System.out.println(pass);

                SharedPreferences.Editor ed = sp.edit();
                ed.putBoolean("REMEMBER-USER", cbRememberLogin.isChecked());
                ed.putBoolean("REMEMBER-LOGIN", cbRememberUser.isChecked());
                ed.apply();

                Intent i = new Intent(LoginActivity.this, ReportActivity.class);
                startActivity(i);

            }
        });

        btnDosentHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(i);
            }
        });

    }
}