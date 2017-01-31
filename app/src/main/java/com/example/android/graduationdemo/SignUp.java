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
    private boolean flag = true;

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
                checkData(mUserName);
                checkData(mPassword);
                checkData(mEmail);
                checkData(mPhoneNumber);
                if(!flag)
                {
                    return;
                }

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

    private void checkData(EditText editText)
    {
        if(editText.getId() == R.id.user_name_sign_up)
        {
            int len = editText.getText().length();
            if(len < 7)
            {
                editText.setError("Name must contain at least 7 letters");
                flag = false;
            }
            else
                flag = true;
        }
        else if(editText.getId() == R.id.password_sign_up)
        {
            int len = editText.getText().length();
            String text = editText.getText().toString();
            boolean isUpper = false;
            boolean isLetter = false;
            for(char ch : text.toCharArray())
            {
                if(Character.isUpperCase(ch))
                {
                    isUpper = true;
                }
                if(Character.isLetter(ch))
                {
                    isLetter = true;
                }
            }
            if(!isUpper)
            {
                editText.setError("Must contain at least 1 Uppercase letter");
                flag = false;
            }
            else
                flag = true;
            if(!isLetter)
            {
                editText.setError("Must contain at least 1 letter");
                flag = false;
            }
            else
                flag = true;
            if(len < 9)
            {
                editText.setError("Must contain at least 9 digits");
                flag = false;
            }
            else
                flag = true;

        }
        else if(editText.getId() == R.id.email_sign_up)
        {
            String text = editText.getText().toString();
            boolean hasAt = false;
            boolean hasDot = false;
           for(char ch : text.toCharArray())
           {

               if(Character.toString(ch).equals("@"))
               {
                   hasAt = true;
               }
               if(Character.toString(ch).equals("."))
               {
                   hasDot = true;
               }

           }
            if(!hasAt || !hasDot)
            {
                editText.setError("Invalid Email Address");
                flag = false;
            }
            else
                flag = true;

        }
        else if(editText.getId() == R.id.phone_number_sign_up)
        {
            boolean hasLetter = false;
            String text = editText.getText().toString();
            for(char ch : text.toCharArray())
            {
                if(Character.isLetter(ch))
                {
                    hasLetter = true;
                }
            }
            if(hasLetter)
            {
                editText.setError("Invalid Phone Number");
                flag = false;
            }
            else
                flag = true;

        }

    }
}
