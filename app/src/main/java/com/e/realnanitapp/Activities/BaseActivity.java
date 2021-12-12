package com.e.realnanitapp.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.MediaStore;

import com.e.realnanitapp.Utils.Constants;

import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {

    protected void selectImage() {
        final CharSequence[] options = { "Take a picture", "Choose from gallery","Cancle" };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take a picture"))
                {
                    dispatchTakePictureIntent();
                }
                else if (options[item].equals("Choose from gallery"))
                {
                    dispatchUploadPhotoIntent();
                }
                else if (options[item].equals("Cancle")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePictureIntent, Constants.REQUEST_IMAGE_CAPTURE);
    }

    private void dispatchUploadPhotoIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), Constants.PICK_IMAGE);
    }
}
