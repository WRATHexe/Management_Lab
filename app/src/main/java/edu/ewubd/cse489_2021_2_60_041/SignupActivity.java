package edu.ewubd.cse489_2021_2_60_041;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SignupActivity extends AppCompatActivity {

    private EditText etUserName, etEmail, etPhone, etPassword, etCPassword;

    private CheckBox cbRememberUser, cbRememberLogin;

    private Button btnHaveAccount, btnSignup;


    private SharedPreferences sp;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = this.getSharedPreferences("my_sp", MODE_PRIVATE);
        String email = sp.getString("USER-EMAIL", "NOT-YET-CREATED");
        if (!email.equals("NOT-YET-CREATED")){
            System.out.println("Moving from signup.");
            Toast.makeText(SignupActivity.this,"Saved account info found", Toast.LENGTH_LONG).show();
            Intent i = new Intent(SignupActivity.this, edu.ewubd.cse489_2021_2_60_041.LoginActivity.class);
            startActivity(i);
            finishAffinity();
        }

        setContentView(R.layout.activity_signup);

        etUserName = findViewById(R.id.etUserName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etPassword = findViewById(R.id.etPassword);
        etCPassword = findViewById(R.id.etCPassword);

        cbRememberUser = findViewById(R.id.cbRememberUser);
        cbRememberLogin = findViewById(R.id.cbRememberLogin);

        btnHaveAccount = findViewById(R.id.btnHaveAccount);
        btnSignup = findViewById(R.id.btnSignup);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = etUserName.getText().toString().trim();
                String email = etEmail.getText().toString().trim();
                String phone = etPhone.getText().toString().trim();
                String pass = etPassword.getText().toString().trim();
                String cPass = etCPassword.getText().toString().trim();

                System.out.println(userName);
                System.out.println(email);
                System.out.println(phone);
                System.out.println(pass);
                System.out.println(cPass);

                if (userName.length() < 4){
                    Toast.makeText(SignupActivity.this, "Username should be 4-8 letters", Toast.LENGTH_LONG).show();
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    Toast.makeText(SignupActivity.this, "Use a valid Email", Toast.LENGTH_LONG).show();
                    return;
                }
                if (phone.length() < 8){
                    Toast.makeText(SignupActivity.this, "Phone should be 8-13 digits", Toast.LENGTH_LONG).show();
                    return;
                }
                if (pass.length() < 4){
                    Toast.makeText(SignupActivity.this, "Password should be 4 digits", Toast.LENGTH_LONG).show();
                    return;
                }
                if(!pass.equals(cPass)){
                    Toast.makeText(SignupActivity.this,"password didnt match",Toast.LENGTH_SHORT).show();
                }

                SharedPreferences.Editor e = sp.edit();
                e.putString("USER-EMAIL", email);
                e.putString("USER-NAME", userName);
                e.putString("USER-PHONE", phone);
                e.putString("PASSWORD", pass);
                e.putBoolean("REMEMBER-USER", cbRememberLogin.isChecked());
                e.putBoolean("REMEMBER-LOGIN", cbRememberUser.isChecked());
                e.apply();

                Intent i = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(i);
                finishAffinity();
            }
        });

        btnHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });


    }
}