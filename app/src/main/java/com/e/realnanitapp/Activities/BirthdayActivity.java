package com.e.realnanitapp.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.e.realnanitapp.Models.BabyModule;
import com.e.realnanitapp.Utils.Constants;
import com.e.realnanitapp.R;
import com.e.realnanitapp.Utils.UriUtil;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Collections;

public class BirthdayActivity extends BaseActivity {

    TextView tvBabyName, tvHowOld;
    ImageView tvBabyAge, ivDefaultPlaceHolder, ivNanit;
    Button btnBack, btnRetakePhoto, btnShareBirthday;
    Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(chooseContentView());
        initView();
        openParcel();
        setBehaviors();
    }

    private int chooseContentView(){
        ArrayList<String> list = new ArrayList<>();
        list.add("i_os_bg_elephant_layout");
        list.add("i_os_bg_fox_layout");
        list.add("i_os_bg_pelican_layout");
        Collections.shuffle(list);
        return this.getResources().getIdentifier(list.get(0), "layout", this.getPackageName());
    }

    private void openParcel() {
        Intent intent = getIntent();
        BabyModule babyDetails = (BabyModule) intent.getParcelableExtra("baby_details");
        if(babyDetails.uri != null) {
            Uri fileUri = Uri.parse(babyDetails.uri);
            setImageDrawable(fileUri);
        }
        //We assume that the baby is 10 years old max :P
        int age = getAge(babyDetails.year, babyDetails.month, babyDetails.day);
        int icAge = this.getResources().getIdentifier("ic_age_number_" + age, "drawable", this.getPackageName());
        tvBabyName.setText("TODAY "+ babyDetails.name.toUpperCase().replace(" ","\n") + " IS");
        tvBabyAge.setBackgroundResource(icAge);
    }

    public int getAge(int year, int month, int dayOfMonth) {
        try {
            Period time = Period.between(LocalDate.of(year, month, dayOfMonth), LocalDate.now());
            if (time.getYears() < 1) {
                tvHowOld.setText("MONTHS OLD!");
                return time.getMonths()-1;
            } else {
                tvHowOld.setText("YEARS OLD!");
                return time.getYears();
            }
        }
        catch (Exception e){
            Log.e("BirthdayActivity", e.toString());
            tvHowOld.setText("AND NOT BORN YET");
            return 0;
        }
    }

    private void initView() {
        btnBack = (Button) findViewById(R.id.btnBack);
        tvBabyAge = (ImageView) findViewById(R.id.tvBabyAge);
        tvBabyName = (TextView) findViewById(R.id.tvBabyName);
        tvHowOld = (TextView) findViewById(R.id.tvHowOld);
        ivDefaultPlaceHolder = (ImageView) findViewById(R.id.ivDefaultPlaceHolder);
        btnRetakePhoto = (Button) findViewById(R.id.btnRetakePhoto);
        btnShareBirthday = (Button) findViewById(R.id.btnShareBirthday);
        ivNanit =  (ImageView) findViewById(R.id.ivNanit);
    }


    private void setBehaviors() {
        btnBack.setOnClickListener(view -> finish());
        btnRetakePhoto.setOnClickListener(view -> selectImage());
        btnShareBirthday.setOnClickListener(view -> shareTheNews());
    }

    private void shareTheNews() {
        Bitmap bitmap = takeScreenshot();
        shareIt(UriUtil.getUriFromBitmap(this, bitmap));
    }

    private void setImageDrawable(Uri uri){
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            ivDefaultPlaceHolder.setImageDrawable(Drawable.createFromStream(inputStream, uri.toString()));
        } catch (FileNotFoundException e) {
            Log.e("BirthdayActivity", e.getMessage());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.PICK_IMAGE) {
            selectedImageUri = data.getData();
            setImageDrawable(selectedImageUri);
        }
        if (requestCode == Constants.REQUEST_IMAGE_CAPTURE && resultCode == Constants.RESULT_OK) {
            Bundle extras = data.getExtras();
            setImageDrawable(UriUtil.getUriFromBitmap(this, (Bitmap) extras.get("data")));
        }
    }

    public Bitmap takeScreenshot() {
        setButtonsVisability(View.INVISIBLE);
        View rootView = findViewById(android.R.id.content).getRootView();
        rootView.setDrawingCacheEnabled(true);
        Bitmap bitmap = rootView.getDrawingCache();
        setButtonsVisability(View.VISIBLE);
        return bitmap;
    }

    private void shareIt(Uri uri) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("image/*");
        sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    private void setButtonsVisability(int vis){
        btnShareBirthday.setVisibility(vis);
        btnBack.setVisibility(vis);
        btnRetakePhoto.setVisibility(vis);
    }
}
