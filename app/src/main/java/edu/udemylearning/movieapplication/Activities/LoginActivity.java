package edu.udemylearning.movieapplication.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import edu.udemylearning.movieapplication.R;

public class LoginActivity extends AppCompatActivity {
    TextView createAccountTextView;
     EditText emailLogin, passwordLogin;
    AppCompatButton loginBtn;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        createAccountTextView = findViewById(R.id.registerBtnLogin);
        emailLogin = findViewById(R.id.email_login);
        passwordLogin = findViewById(R.id.password_login);
        loginBtn = findViewById(R.id.loginBtn);
        progressBar = findViewById(R.id.progress_bar);

        createAccountTextView.setOnClickListener(V->startActivity(new Intent(LoginActivity.this, CreateAccount.class)));
        loginBtn.setOnClickListener(V->loginAccount());

    }
    void loginAccount(){
        String email = emailLogin.getText().toString();
        String password = passwordLogin.getText().toString();
        boolean isValidate = validateDate(email,password);
        if (!isValidate){
            return;
        }
            loginAccountInFireBase(email,password);
    }
    void loginAccountInFireBase(String email, String password){
        changeInprogress(true);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                changeInprogress(false);
                if (task.isSuccessful()){
                    if (firebaseAuth.getCurrentUser().isEmailVerified()){
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    }else {
                        Toast.makeText(LoginActivity.this,"Email not verified, Please verify email.",Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(LoginActivity.this,task.getException().getLocalizedMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    boolean validateDate(String email, String password){
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailLogin.setError("Invalid Email.");
            return false;
        }
        if (password.length()<6){
            passwordLogin.setError("Password Invalid.");
            return false;
        }
        return true;
    }
    void changeInprogress(boolean progress){
        if(progress){
            loginBtn.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        }else {
            loginBtn.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }
    }
}