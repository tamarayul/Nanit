package com.e.realnanitapp.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.e.realnanitapp.Models.BabyModule;
import com.e.realnanitapp.Utils.Constants;
import com.e.realnanitapp.R;
import com.e.realnanitapp.Utils.UriUtil;

public class MainActivity extends BaseActivity {

    EditText babyName;
    DatePicker babyDate;
    Button showBirthdayButton, uploadImage;
    Uri uploadedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        setBehaviors();
    }

    private void initView() {
        showBirthdayButton = (Button) findViewById(R.id.btnShowBirthday);
        uploadImage = (Button) findViewById(R.id.btnGetPicture);
        babyName = (EditText) findViewById(R.id.etBabyName);
        babyDate = (DatePicker) findViewById(R.id.dpBabyDate);
    }

    private void setBehaviors() {
        showBirthdayButton.setEnabled(false);
        showBirthdayButton.setOnClickListener(view -> showBirthdayScreen());
        uploadImage.setOnClickListener(view -> selectImage());
        babyName.addTextChangedListener(watcher);
    }

    private void showBirthdayScreen() {
        BabyModule babyModule = new BabyModule();
        babyModule.day = babyDate.getDayOfMonth();
        babyModule.month = babyDate.getMonth();
        babyModule.year = babyDate.getYear();
        babyModule.name = babyName.getText().toString();
        Intent birthdayIntent = new Intent(this, BirthdayActivity.class);
        if(uploadedImage != null) {
            babyModule.uri = uploadedImage.toString();
        }
        birthdayIntent.putExtra("baby_details", babyModule);
        startActivity(birthdayIntent);
    }

    private final TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after)
        { }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count)
        {}
        @Override
        public void afterTextChanged(Editable s) {
            enableButton();
        }
    };

    private void enableButton(){
        if (babyName.getText().toString().length() == 0) {
            showBirthdayButton.setEnabled(false);
        } else {
            showBirthdayButton.setEnabled(true);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.PICK_IMAGE){
            uploadedImage = data.getData();
        }
        if(requestCode == Constants.REQUEST_IMAGE_CAPTURE
                && resultCode == Constants.RESULT_OK) {
            Bundle extras = data.getExtras();
            uploadedImage = UriUtil.getUriFromBitmap(this, (Bitmap) extras.get("data"));
        }
    }
}