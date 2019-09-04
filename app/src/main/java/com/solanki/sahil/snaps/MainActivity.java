package com.solanki.sahil.snaps;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    EditText editTextUsername;
    EditText editTextPassword;
    Button buttonSignUp, buttonLogin;
    TextView textView;
    RelativeLayout backgroundLayout;
    ImageView imageView;
    FirebaseAuth mAuth;
    int count = 0;



    public void redirect() {


        if (mAuth.getCurrentUser() != null) {
            editTextUsername.setText("");
            editTextPassword.setText("");
            Intent intent = new Intent(MainActivity.this, SnapsActivity.class);
            startActivity(intent);
        }
    }

    public void keyboardDown(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }




    public void signUpClick(View view) {

        if (editTextUsername.getText().toString().matches("") || editTextPassword.getText().toString().matches("")) {
            Toast.makeText(MainActivity.this, "Email id and Password required", Toast.LENGTH_LONG).show();
        } else {

            mAuth.createUserWithEmailAndPassword(editTextUsername.getText().toString(), editTextPassword.getText().toString())
                    .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Utils.getDatabase().getReference().child("users").child(task.getResult().getUser().getUid()).child("email").setValue(editTextUsername.getText().toString());
                                redirect();

                            } else {

                                Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();

                            }


                        }
                    });
        }
    }




    public void toggle(View view) {

        if (editTextUsername.getText().toString().matches("") || editTextPassword.getText().toString().matches("")) {
            Toast.makeText(MainActivity.this, "Email id and Password required", Toast.LENGTH_LONG).show();
        }else{

            mAuth.signInWithEmailAndPassword(editTextUsername.getText().toString(), editTextPassword.getText().toString())
                    .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                redirect();
                            } else {
                                Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }



    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPaasword);
        buttonSignUp = findViewById(R.id.buttonSignUp);
        buttonLogin = findViewById(R.id.buttonLogin);
        textView = findViewById(R.id.textView);
        backgroundLayout = findViewById(R.id.backgroundLayout);
        imageView = findViewById(R.id.imageView);

        mAuth = FirebaseAuth.getInstance();

        editTextPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (editTextPassword.getRight() - editTextPassword.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        if (count == 0) {
                            count = 1;
                            editTextPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            editTextPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility_black_24dp, 0);

                            return true;
                        } else if (count == 1) {
                            count = 0;
                            editTextPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            editTextPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility_off_black_24dp, 0);
                            return true;
                        }
                    }
                }
                return false;
            }
        });


        redirect();


    }



}
