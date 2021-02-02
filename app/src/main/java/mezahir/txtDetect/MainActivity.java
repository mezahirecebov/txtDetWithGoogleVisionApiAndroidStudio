package mezahir.txtDetect;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    TextView detectedText;
    Button detectText,galery,camer;
    ImageView image;
    Uri imageuri;
    private  static  final  int PICK_IMAGE=1;
    private  static final int REQUEST_IMAGE_CAPTURE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        detectedText=findViewById(R.id.detectedText);
        detectedText.setMovementMethod(new ScrollingMovementMethod());
        detectText=findViewById(R.id.button);
        galery=findViewById(R.id.buttonGalery);
        camer=findViewById(R.id.buttonCamera);
        image =findViewById(R.id.image);



        camer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                detectedText.setText("");
                image.setImageBitmap(null);

                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                try {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                } catch (ActivityNotFoundException e) {
                    // display error state to the user
                }

            }
        });

        galery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                detectedText.setText("");
                image.setImageBitmap(null);
                Intent gallery=new Intent();
                gallery.setType("image/*");
                gallery.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(gallery,"select image"),PICK_IMAGE);
            }
        });

        detectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextRecognizer textRecognizer=new TextRecognizer.Builder(getApplicationContext()).build();
                if(!textRecognizer.isOperational()){
                    Log.d("detect","Detector dependencies are not yet available");

                }
                else{
                    try {
                        BitmapDrawable drawable = (BitmapDrawable) image.getDrawable();

                        Bitmap bitmap = drawable.getBitmap();
                        Frame frame=new Frame.Builder().setBitmap(bitmap).build();
                        SparseArray<TextBlock> items   =textRecognizer.detect(frame);
                        StringBuilder sb=new StringBuilder();
                        for(int i=0;i<items.size();i++){
                            TextBlock myItem=items.valueAt(i);
                            sb.append(myItem.getValue());
                            sb.append("\n");

                        }

                        detectedText.setText(sb.toString());
                    }catch (Exception e){
                            e.printStackTrace();
                    }


                }


               // image.setImageBitmap(null);


            }
        });






    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==PICK_IMAGE && resultCode==RESULT_OK){
                imageuri=data.getData();
                try {
                    Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),imageuri);
                    image.setImageBitmap(bitmap);
                }
                catch (IOException e){
                        e.printStackTrace();
                }
        }

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            image.setImageBitmap(imageBitmap);
        }
    }


}
