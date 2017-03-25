package com.example.android.graduationdemo;

import android.animation.Animator;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.dd.CircularProgressButton;
import com.example.android.graduationdemo.Firebase.FirebaseHandler;
import com.example.android.graduationdemo.callbacks.LoginCallBack;
import com.google.firebase.auth.FirebaseAuth;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;

import java.util.List;

public class SignIn extends AppCompatActivity implements Validator.ValidationListener {

    private TextView mNeedAcc;
    private Validator validator;

    @NotEmpty
    @Email
    private EditText mEmail;

    @NotEmpty
    @Password(min = 6)
    private EditText mPassword;


    private FirebaseAuth mAuth;
    private User user;
    private ProgressDialog mProgressDialog;
    private CircularProgressButton mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Please Wait");

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            startActivity(new Intent(SignIn.this, MainActivity.class));
        }

        setContentView(R.layout.activity_sign_in);

        validator = new Validator(this);
        validator.setValidationListener(this);


        mButton = (CircularProgressButton) findViewById(R.id.circleloginbtn);
        mButton.setIndeterminateProgressMode(true);
        mAuth = FirebaseAuth.getInstance();

        mEmail = (EditText) findViewById(R.id.email_login);
        mPassword = (EditText) findViewById(R.id.password_login);

        mNeedAcc = (TextView) findViewById(R.id.link_signup);
        mNeedAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignIn.this, SignUp.class));
            }
        });

        user = new User();
        //signInBtn = (Button) findViewById(R.id.login_btn);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // mProgressDialog.show();
                mButton.setProgress(0);
                setUserInfo();
                validator.validate();
                if(mEmail.getText().toString().trim().length() > 0 && mPassword.getText().toString().trim().length() > 0)
                {
                    mButton.setProgress(50);
                FirebaseHandler.signIn(mAuth, user, SignIn.this, new LoginCallBack() {

                    @Override
                    public void onLoggedIn() {
                        mProgressDialog.dismiss();
                        animateSuccess();
                        mButton.setProgress(100);
                    }

                    @Override
                    public void onFail() {
                        mProgressDialog.dismiss();
                        animateError();
                        mButton.setProgress(-1);
                    }
                });
                }
                else
                {
                    mProgressDialog.dismiss();
                    animateError();
                    mButton.setProgress(-1);
                }
            }
        });
    }

    private void setUserInfo() {
        user.setUserEmail(mEmail.getText().toString());
        user.setUserPassword(mPassword.getText().toString());
    }

    @Override
    public void onValidationSucceeded() {
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            // Display error messages ;)
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
    }

    private void animateError()
    {
        YoYo.with(Techniques.Tada)
                .duration(700)
                .playOn(findViewById(R.id.edit_area));

        YoYo.with(Techniques.Pulse)
                .duration(700)
                .playOn(findViewById(R.id.link_signup));
    }

    private void animateSuccess()
    {
        YoYo.with(Techniques.TakingOff)
                .onEnd(new YoYo.AnimatorCallback() {
                    @Override
                    public void call(Animator animator) {
                        Intent intent = new Intent(SignIn.this, MainActivity.class);
                        intent.putExtra("userEmail", user.getUserEmail());
                        SignIn.this.finish();
                        startActivity(intent);
                    }
                })
                .duration(700)
                .playOn(findViewById(R.id.edit_area));
    }
}


