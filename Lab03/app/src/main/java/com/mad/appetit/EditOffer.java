package com.mad.appetit;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.icu.text.SimpleDateFormat;
import android.media.Image;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class EditOffer extends AppCompatActivity {
    private static final String MyOFFER = "Daily_Offer";
    private static final String Name = "keyName";
    private static final String Description = "keyDescription";
    private static final String Price = "keyEuroPrice";
    private static final String Photo ="keyPhotoPath";
    private static final String Quantity = "keyQuantity";
    private static final String CameraOpen = "keyCameraDialog";
    private static final String PriceOpen = "keyPriceDialog";
    private static final String QuantOpen = "keyQuantityDialog";
    private static final String NItem = "NItemKey";

    private ArrayList<String> dataArray = new ArrayList<>();
    private int nItem;

    private String name;
    private String desc;
    private String error_msg;
    private float priceValue = -1;
    private int quantValue = -1;

    private boolean camera_open = false;
    private boolean price_open = false;
    private boolean quant_open = false;

    private static final int PERMISSION_GALLERY_REQUEST = 1;
    private String currentPhotoPath;
    private Button priceButton;
    private Button quantButton;
    private SharedPreferences offer_data;
    private int pos;
    private boolean edit = false;
    private SharedPreferences dishes_data;
    private ImageView imageview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_offer);

        priceButton = findViewById(R.id.price);
        quantButton = findViewById(R.id.quantity);

        priceButton.setOnClickListener(e->setPrice());
        quantButton.setOnClickListener(f->setQuantity());
        findViewById(R.id.plus).setOnClickListener(p -> editPhoto());
        findViewById(R.id.img_profile).setOnClickListener(e -> editPhoto());
        imageview = (ImageView)findViewById(R.id.img_profile);

        int pos = getIntent().getIntExtra("Existing",-1);

        if(pos != -1 )
            getData(pos);
        else
            imageview.setImageResource(R.drawable.hamburger);

        Button confirm_reg = findViewById(R.id.button);
        confirm_reg.setOnClickListener(e -> {
            if(checkFields()){

                //data saved and start new activity
                Intent i = new Intent();
                i.putExtra(Name, name);
                i.putExtra(Description, desc);
                i.putExtra(Price, priceValue);
                i.putExtra(Photo, currentPhotoPath);
                i.putExtra(Quantity, quantValue);

                if(pos != -1){
                    i.putExtra("EDIT_PHOTO_POS", pos);
                    setResult(2, i);
                }
                else
                    setResult(1, i);

                finish();
            }
            else{
                Toast.makeText(getApplicationContext(), error_msg, Toast.LENGTH_LONG).show();
            }
        });
    }


    private void getData(Integer id){
        offer_data = getSharedPreferences("dishes", MODE_PRIVATE);

        String name;
        String desc;
        String photoPath;
        float price;
        int quantity;

        dishes_data = getSharedPreferences("dishes",0);
        name = dishes_data.getString("Name " + id.toString(), null);
        desc = dishes_data.getString("Desc " + id.toString(), null);
        photoPath = dishes_data.getString("Photo " + id.toString(), null);
        price = dishes_data.getFloat("Price " + id.toString(), 0);
        quantity = dishes_data.getInt("Quantity " + id.toString(), 0);

        this.name = name;
        this.desc = desc;
        priceValue = price;
        quantValue = quantity;
        currentPhotoPath = photoPath;

        ((EditText)findViewById(R.id.name)).setText(name);
        ((EditText)findViewById(R.id.description)).setText(desc);
        if(currentPhotoPath!=null)
            Glide.with(getApplicationContext()).load(photoPath).into((ImageView) findViewById(R.id.img_profile));
        else
            imageview.setImageResource(R.drawable.hamburger);

        priceButton.setText(Float.toString(price));
        quantButton.setText(Integer.toString(quantity));
    }


    private boolean checkFields(){
        name = ((EditText)findViewById(R.id.name)).getText().toString();
        desc = ((EditText)findViewById(R.id.description)).getText().toString();

        if(name.trim().length() == 0){
            error_msg = "Insert name";
            return false;
        }

        if(desc.trim().length() == 0){
            error_msg = "Insert description";
            return false;
        }

        if(priceValue == -1){
            error_msg = "Insert price";
            return false;
        }

        if(quantValue == -1){
            error_msg = "Insert quantity";
            return false;
        }

        return true;
    }

    private String[] setCentsValue(){
        String[] cent = new String[100];
        for(int i=0; i<100; i++){
            if(i<10) {
                cent[i] = "0" +i;
            }
            else{
                cent[i] = ""+i;
            }
        }
        return cent;
    }

    private void setPrice(){
        AlertDialog priceDialog = new AlertDialog.Builder(this).create();
        LayoutInflater inflater = LayoutInflater.from(EditOffer.this);
        final View view = inflater.inflate(R.layout.price_dialog, null);

        price_open = true;

        NumberPicker euro = view.findViewById(R.id.euro_picker);
        NumberPicker cent = view.findViewById(R.id.cent_picker);

        priceDialog.setView(view);

        priceDialog.setButton(AlertDialog.BUTTON_POSITIVE,"OK", (dialog, which) -> {
            price_open = false;
            float centValue = cent.getValue();
            priceValue = euro.getValue() + (centValue/100);
            priceButton.setText(Float.toString(priceValue));
        });
        priceDialog.setButton(DialogInterface.BUTTON_NEGATIVE,"CANCEL", (dialog, which) -> {
            price_open = false;
            dialog.dismiss();
        });

        euro.setMinValue(0);
        euro.setMaxValue(9999);
        euro.setValue(0);

        String[] cents = setCentsValue();

        cent.setDisplayedValues(cents);
        cent.setMinValue(0);
        cent.setMaxValue(99);
        cent.setValue(0);

        priceDialog.show();
    }

    private void setQuantity(){
        AlertDialog quantDialog = new AlertDialog.Builder(this).create();
        LayoutInflater inflater = LayoutInflater.from(EditOffer.this);
        final View view = inflater.inflate(R.layout.quantity_dialog, null);

        quant_open = true;

        NumberPicker quantity = view.findViewById(R.id.quant_picker);

        quantDialog.setView(view);

        quantDialog.setButton(AlertDialog.BUTTON_POSITIVE,"OK", (dialog, which) -> {
            quant_open = false;
            quantValue = quantity.getValue();
            quantButton.setText(Integer.toString(quantValue));
        });
        quantDialog.setButton(DialogInterface.BUTTON_NEGATIVE,"CANCEL", (dialog, which) -> {
            quant_open = false;
            dialog.dismiss();
        });

        quantity.setMinValue(1);
        quantity.setMaxValue(999);
        quantity.setValue(1);

        quantDialog.show();
    }

    private void editPhoto(){
        AlertDialog alertDialog = new AlertDialog.Builder(EditOffer.this, R.style.AlertDialogStyle).create();
        LayoutInflater factory = LayoutInflater.from(EditOffer.this);
        final View view = factory.inflate(R.layout.custom_dialog, null);

        camera_open = true;

        alertDialog.setOnCancelListener(dialog -> {
            camera_open = false;
            alertDialog.dismiss();
        });

        view.findViewById(R.id.camera).setOnClickListener( c -> {
            cameraIntent();
            camera_open = false;
            alertDialog.dismiss();
        });
        view.findViewById(R.id.gallery).setOnClickListener( g -> {
            galleryIntent();
            camera_open = false;
            alertDialog.dismiss();
        });
        alertDialog.setView(view);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Camera", (dialog, which) -> {
            cameraIntent();
            camera_open = false;
            dialog.dismiss();
        });
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Gallery", (dialog, which) -> {
            galleryIntent();
            camera_open = false;
            dialog.dismiss();
        });
        alertDialog.show();
    }

    private void cameraIntent(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.d("FILE: ","error creating file");
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, 2);
            }
        }
    }

    private void galleryIntent(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[] { Manifest.permission.READ_EXTERNAL_STORAGE },
                    PERMISSION_GALLERY_REQUEST);
        }
        else{
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, 1);
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = new File( storageDir + File.separator +
                imageFileName + /* prefix */
                ".jpg"
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();

        return image;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_GALLERY_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("Permission Run Time: ", "Obtained");

                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, 1);
                } else {
                    Log.d("Permission Run Time: ", "Denied");

                    Toast.makeText(getApplicationContext(), "Access to media files denied", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == RESULT_OK && null != data){
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            currentPhotoPath = picturePath;
        }

        if((requestCode == 1 || requestCode == 2) && resultCode == RESULT_OK){
            Glide.with(getApplicationContext()).load(currentPhotoPath).into((ImageView) findViewById(R.id.img_profile));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putString(Name, ((EditText)findViewById(R.id.name)).getText().toString());
        savedInstanceState.putString(Description, ((EditText)findViewById(R.id.description)).getText().toString());
        savedInstanceState.putFloat(Price, priceValue);
        savedInstanceState.putInt(Quantity, quantValue);
        savedInstanceState.putString(Photo, currentPhotoPath);
        savedInstanceState.putBoolean(CameraOpen, camera_open);
        savedInstanceState.putBoolean(PriceOpen, price_open);
        savedInstanceState.putBoolean(QuantOpen, quant_open);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        ((EditText)findViewById(R.id.name)).setText(savedInstanceState.getString(Name));
        ((EditText)findViewById(R.id.description)).setText(savedInstanceState.getString(Description));

        priceValue = savedInstanceState.getFloat(Price);
        if(priceValue != -1)
            priceButton.setText(Float.toString(priceValue));

        quantValue = savedInstanceState.getInt(Quantity);
        if(quantValue != -1)
            quantButton.setText(Integer.toString(quantValue));

        currentPhotoPath = savedInstanceState.getString(Photo);
        if(currentPhotoPath != null){
            Glide.with(getApplicationContext()).load(currentPhotoPath).into((ImageView) findViewById(R.id.img_profile));
                //setPhoto(currentPhotoPath);
        }

        if(savedInstanceState.getBoolean(CameraOpen))
            editPhoto();

        if(savedInstanceState.getBoolean(PriceOpen))
            setPrice();

        if(savedInstanceState.getBoolean(QuantOpen))
            setQuantity();
    }
}
