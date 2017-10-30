package edu.purdue.qingdong.simpleimageeditor;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    ImageButton btnCamera;
    ImageButton btnGallery;
    Intent camIntent;
    File file;
    Uri uri;

    private static final int TAKE_PICTURE = 0;

    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnCamera = (ImageButton) findViewById(R.id.btnCamera);
        btnGallery = (ImageButton) findViewById(R.id.btnGallery);
        imageView = (ImageView) findViewById(R.id.imageView);

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                file = new File(Environment.getExternalStorageDirectory(),
//                        "file" + String.valueOf(System.currentTimeMillis()) + ".jpg");
//                uri = Uri.fromFile(file);
//                camIntent.putExtra(MediaStore.EXTRA_OUTPUT,uri);
                startActivityForResult(camIntent,TAKE_PICTURE);
            }
        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == TAKE_PICTURE && resultCode == Activity.RESULT_OK) {

            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(photo);
//            Uri selectedImage = uri;
//            getContentResolver().notifyChange(selectedImage, null);
//            ContentResolver cr = getContentResolver();
//            Bitmap bitmap;
//            try {
//                bitmap = android.provider.MediaStore.Images.Media.getBitmap(cr, selectedImage);
//                imageView.setImageBitmap(bitmap);
//            } catch (Exception e) {
//
//                Toast.makeText(this,"cannot load the pic", Toast.LENGTH_SHORT).show();
//            }

        }
    }
    public void onEdit(View v) {
        Intent intent = new Intent(this, EditActivity.class);
        startActivity(intent);
    }
}
