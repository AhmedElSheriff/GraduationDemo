package com.example.android.graduationdemo;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.dd.CircularProgressButton;
import com.example.android.graduationdemo.Firebase.FirebaseHandler;
import com.example.android.graduationdemo.callbacks.LoginCallBack;
import com.google.firebase.auth.FirebaseAuth;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Checked;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Max;
import com.mobsandgeeks.saripaar.annotation.Min;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;

import java.util.List;

public class SignUp extends AppCompatActivity implements Validator.ValidationListener{

    private Spinner mSpinner;
    private ArrayAdapter<String> adapter;

    @NotEmpty
    private EditText mUserName;
    @NotEmpty
    @Email
    private EditText mEmail;
    @NotEmpty
    @Password(min = 6)
    private EditText mPassword;
    @NotEmpty
    @Min(11)
    @Max(11)
    private EditText mPhoneNumber;
    private RadioButton mRadionBtn;
    @Checked
    private RadioGroup mRadioGroup;
    private User user;

    private FirebaseAuth mAuth;
    private Validator validator;
    private CircularProgressButton mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        validator = new Validator(this);
        validator.setValidationListener(this);

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

        mButton = (CircularProgressButton) findViewById(R.id.circlesignupbtn);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mButton.setProgress(0);

                createNewUser();
                validator.validate();

                mButton.setProgress(50);
                if(mUserName.getText().toString().trim().length() > 0 && mPassword.getText().toString().trim().length() > 0
                        && mEmail.getText().toString().trim().length() > 0 && mPhoneNumber.getText().toString().trim().length() == 11


                        )
                {

                    FirebaseHandler.addNewUser(mAuth, user, SignUp.this, new LoginCallBack() {
                        @Override
                        public void onLoggedIn() {
                            animateSuccess();
                            mButton.setProgress(100);

                        }

                        @Override
                        public void onFail() {
                            animateError();
                            mButton.setProgress(-1);
                        }
                    });
                }
                else
                {
                    animateError();
                    mButton.setProgress(-1);
                }

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
        if(selectedId != -1) {
            user.setUserGender(mRadionBtn.getText().toString());
        }
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


    }

    private void animateSuccess()
    {
        YoYo.with(Techniques.TakingOff)
                .onEnd(new YoYo.AnimatorCallback() {
                    @Override
                    public void call(Animator animator) {
                        Intent intent = new Intent(SignUp.this,MainActivity.class);
                        intent.putExtra("userEmail",user.getUserEmail());
                        SignUp.this.finish();
                        startActivity(intent);
                    }
                })
                .duration(700)
                .playOn(findViewById(R.id.edit_area));
    }
}
