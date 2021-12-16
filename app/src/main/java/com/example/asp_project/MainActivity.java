package com.example.asp_project;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ImageSwitcher imagesIs;
    private Button previousBtn, NextBtn, pickImageBtn;
    //store images uris in this array list
    private ArrayList<Uri> imagesUris;

    //position of selected image
    int position =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.imagestostore);

        //initialise UI views
        imagesIs = findViewById(R.id.imageIS);
        previousBtn = findViewById(R.id.previousBtn);
        NextBtn = findViewById(R.id.NextBtn);
        pickImageBtn = findViewById(R.id.pickImageBtn);

        //initialize List
        imagesUris =new ArrayList<>();

        //setup image switcher
        imagesIs.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView imageView = new ImageView(getApplicationContext());
                return imageView;
            }
        });

        //click handle, pick images
        pickImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImagesIntent();
            }
        });

        //click handle, show previous image
        previousBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(position>0){
                    position--;
                    imagesIs.setImageURI(imagesUris.get(position));
                }
                else{
                    Toast.makeText(MainActivity.this, "No previous images...", Toast.LENGTH_SHORT).show();
                }

            }
        });


        //click handle, show next image
        NextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(position< imagesUris.size() - 1){
                    position++;
                    imagesIs.setImageURI(imagesUris.get(position));
                }
                else{
                    Toast.makeText(MainActivity.this,"No more images...", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void pickImagesIntent(){
        Intent intent = new Intent();
        intent.setType("images/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        galleryActivityResultLauncher.launch(intent);

    }

    private ActivityResultLauncher<Intent> galleryActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    //here will handle the result of out intent

                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent data= result.getData();

                        if(data.getClipData() != null){
                            //picked multiple images

                            int cout = data.getClipData().getItemCount();//number of picked images
                            for(int i=0; i<cout; i++){
                                // get image uri at specific index
                                Uri imageUri = data.getClipData().getItemAt(i).getUri();
                                imagesUris.add(imageUri);//add to list
                            }

                            //set first image to image switcher
                            imagesIs.setImageURI(imagesUris.get(0));
                            position =0;
                        }
                        else{
                            //picked single image
                            Uri imagesUri = data.getData();
                            imagesUris.add(imagesUri);
                            //set image to our image switcher
                            imagesIs.setImageURI(imagesUris.get(0));
                            position =0;
                        }
                    }
                }
            }


    );
    
}