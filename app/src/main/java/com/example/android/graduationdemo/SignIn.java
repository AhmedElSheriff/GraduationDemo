package com.example.android.graduationdemo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.graduationdemo.Firebase.FirebaseHandler;
import com.example.android.graduationdemo.callbacks.LoginCallBack;
import com.example.android.graduationdemo.callbacks.UserAvailability;
import com.google.firebase.auth.FirebaseAuth;

public class SignIn extends AppCompatActivity {

    private Button mLoginBtn;
    private TextView mNeedAcc;
    private EditText mEmail;
    private EditText mPassword;
    private Button signInBtn;
    private FirebaseAuth mAuth;
    private User user;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Please Wait");

        if(FirebaseAuth.getInstance().getCurrentUser()!=null)
        {

            mProgressDialog.show();
            FirebaseHandler.checkIfUserAvailable(FirebaseAuth.getInstance().getCurrentUser().getEmail(), new UserAvailability() {
                @Override
                public void onSearchComplete(boolean isFounded) {
                    if(isFounded)
                    {
                        mProgressDialog.dismiss();
                        Intent intent = new Intent(SignIn.this,MainActivity.class);
                        intent.putExtra("userEmail",FirebaseAuth.getInstance().getCurrentUser().getEmail());
                        SignIn.this.finish();
                        startActivity(intent);
                    }
                    else
                    {
                        mProgressDialog.dismiss();
                        Toast.makeText(SignIn.this,"Not Founded",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        setContentView(R.layout.activity_sign_in);


        mAuth = FirebaseAuth.getInstance();

        mEmail = (EditText) findViewById(R.id.email_login);
        mPassword = (EditText) findViewById(R.id.password_login);

        mLoginBtn = (Button) findViewById(R.id.login_btn);
        mNeedAcc = (TextView) findViewById(R.id.link_signup);
        mNeedAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignIn.this,SignUp.class));
            }
        });

        user = new User();
        signInBtn = (Button) findViewById(R.id.login_btn);
        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUserInfo();
                FirebaseHandler.signIn(mAuth, user, SignIn.this, new LoginCallBack() {

                    @Override
                    public void onLoggedIn() {
                        Intent intent = new Intent(SignIn.this,MainActivity.class);
                        intent.putExtra("userEmail",user.getUserEmail());
                        SignIn.this.finish();
                        startActivity(intent);

                    }

                    @Override
                    public void onFail() {
                        Toast.makeText(SignIn.this,"Failed",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void setUserInfo()
    {
        user.setUserEmail(mEmail.getText().toString());
        user.setUserPassword(mPassword.getText().toString());
    }


}
