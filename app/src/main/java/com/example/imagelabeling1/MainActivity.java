package com.example.imagelabeling1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler;
import com.google.firebase.ml.vision.text.FirebaseVisionText;

import java.util.List;

public class MainActivity extends AppCompatActivity {
Button click,detect;
ImageView image;
public int CAMERA_REQUEST=100;
public Bitmap imageDetect;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        image = findViewById(R.id.image);
        click = findViewById(R.id.btn1);
        detect = findViewById(R.id.btn2);


        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE),CAMERA_REQUEST);
            }
        });





        detect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                detectLabel();
            }
        });
    }



    @Override
    protected void onActivityResult(int requestCode,int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        if(requestCode==CAMERA_REQUEST){
            imageDetect=(Bitmap) data.getExtras().get("data");
            image.setImageBitmap(imageDetect);
        }



    }



    void detectLabel(){
        FirebaseVisionImage imagef=FirebaseVisionImage.fromBitmap(imageDetect);
        FirebaseVisionImageLabeler labeler= FirebaseVision.getInstance()
                .getOnDeviceImageLabeler();
        labeler.processImage(imagef).addOnCompleteListener(new OnCompleteListener<List<FirebaseVisionImageLabel>>() {
            @Override
            public void onComplete(@NonNull Task<List<FirebaseVisionImageLabel>> task) {
                String outD="";
                for(FirebaseVisionImageLabel labeld:task.getResult()){

                    outD+=labeld.getText()+"\t"+labeld.getConfidence()*100+"%"+"\n";
                }



                Toast.makeText(MainActivity.this,""+outD,Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }


}
