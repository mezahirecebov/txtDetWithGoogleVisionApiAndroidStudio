package mezahir.txtDetect;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    TextView detectedText;
    Button detectText;
    ImageView image;
    Uri imageuri;
    private  static  final  int PICK_IMAGE=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        detectedText=findViewById(R.id.detectedText);
        detectText=findViewById(R.id.button);
        image =findViewById(R.id.image);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 Intent gallery=new Intent();
                 gallery.setType("image/*");
                 gallery.setAction(Intent.ACTION_GET_CONTENT);
                 startActivityForResult(Intent.createChooser(gallery,"select image"),PICK_IMAGE);
            }
        });

        detectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                image.setImageBitmap(null);
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
    }
}
