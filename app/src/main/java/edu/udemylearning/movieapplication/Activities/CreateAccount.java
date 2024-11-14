package edu.udemylearning.movieapplication.Activities;

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

public class CreateAccount extends AppCompatActivity {
    private EditText myEmail, myPassword, myConfirmPassword;
    private  TextView loginTextView;
    private AppCompatButton createAccountBtn;
    private ProgressBar progressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        progressbar = findViewById(R.id.progress_bar);
        loginTextView = findViewById(R.id.login_btn_signin);
        myEmail = findViewById(R.id.signup_email);
        myPassword = findViewById(R.id.signup_password);
        myConfirmPassword = findViewById(R.id.signup_confirm_password);
        createAccountBtn = findViewById(R.id.createAccountBtn);
        createAccountBtn.setOnClickListener(V->createAccount());
        loginTextView.setOnClickListener(V->finish());

    }
    void createAccount(){
        String email = myEmail.getText().toString();
        String password = myPassword.getText().toString();
        String confirmPassword = myConfirmPassword.getText().toString();
        boolean isValidated = validateData(email,password,confirmPassword);
        if (!isValidated){
            return;
        }
        createAccountInFireBase(email,password);

    }
    void  createAccountInFireBase(String email, String password){
        changeInProgressBar(true);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(CreateAccount.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(CreateAccount.this,"Account Created Successfully. Check Email to verify.",Toast.LENGTH_LONG).show();
                    firebaseAuth.getCurrentUser().sendEmailVerification();
                    firebaseAuth.signOut();
                    finish();
                }
                else {
                    Toast.makeText(CreateAccount.this,task.getException().getLocalizedMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    boolean validateData(String email, String password, String confirmPassword){
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            myEmail.setError("Invalid Email.");
            return false;
        }
        if (password.length()<6){
            myPassword.setError("Password length must have 6 characters.");
            return  false;
        }if (!password.equals(confirmPassword)){
            myConfirmPassword.setError("Password didn't match.");
            return  false;
        }
        return true;
    }
    void changeInProgressBar(boolean inProgress){
        if (inProgress){
            progressbar.setVisibility(View.VISIBLE);
            createAccountBtn.setVisibility(View.GONE);
        }else {
            progressbar.setVisibility(View.GONE);
            createAccountBtn.setVisibility(View.VISIBLE);
        }

    }
}