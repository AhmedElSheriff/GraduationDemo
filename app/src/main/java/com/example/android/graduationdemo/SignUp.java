package com.example.android.graduationdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.example.android.graduationdemo.Firebase.FirebaseHandler;
import com.example.android.graduationdemo.callbacks.LoginCallBack;
import com.google.firebase.auth.FirebaseAuth;

public class SignUp extends AppCompatActivity {

    private Spinner mSpinner;
    private ArrayAdapter<String> adapter;
    private Button mSignUpBtn;

    private EditText mUserName;
    private EditText mEmail;
    private EditText mPassword;
    private EditText mPhoneNumber;
    private RadioButton mRadionBtn;
    private RadioGroup mRadioGroup;
    private User user;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();
        mSpinner = (Spinner) findViewById(R.id.international_code_spinner);

        mUserName = (EditText) findViewById(R.id.user_name_sign_up);
        mEmail = (EditText) findViewById(R.id.email_sign_up);
        mPassword = (EditText) findViewById(R.id.password_sign_up);
        mPhoneNumber = (EditText) findViewById(R.id.phone_number_sign_up);


        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,getResources().getStringArray(R.array.international_code));
        mSpinner.setAdapter(adapter);

        mRadioGroup = (RadioGroup) findViewById(R.id.gender_radio_group);

        user = new User();

        mSignUpBtn = (Button) findViewById(R.id.signup_btn);
        mSignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                createNewUser();
                FirebaseHandler.addNewUser(mAuth, user, SignUp.this, new LoginCallBack() {
                    @Override
                    public void onLoggedIn() {
                        Intent intent = new Intent(SignUp.this,MainActivity.class);
                        intent.putExtra("userEmail",user.getUserEmail());
                        SignUp.this.finish();
                        startActivity(intent);
                    }

                    @Override
                    public void onFail() {

                    }
                });
            }
        });


    }

    private void createNewUser()
    {
        user.setUserName(mUserName.getText().toString());
        user.setUserEmail(mEmail.getText().toString());
        user.setUserPassword(mPassword.getText().toString());
        user.setPhoneNumber(mSpinner.getSelectedItem().toString() + "" +  mPhoneNumber.getText().toString());
        int selectedId = mRadioGroup.getCheckedRadioButtonId();
        mRadionBtn = (RadioButton) findViewById(selectedId);
        user.setUserGender(mRadionBtn.getText().toString());
    }
}
