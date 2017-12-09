package edu.purdue.qingdong.simpleimageeditor;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends AppCompatActivity {

    ImageButton btnCamera;
    ImageButton btnGallery;
    //Intent takePictureIntent;
    private Bitmap mImageBitmap;
    private String mCurrentPhotoPath;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_IMAGE_PICK = 2;

    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Image Editor");
        btnCamera = (ImageButton) findViewById(R.id.btnCamera);
        btnGallery = (ImageButton) findViewById(R.id.btnGallery);
        imageView = (ImageView) findViewById(R.id.imageView);
        Button btnEdit = (Button)findViewById(R.id.btnEdit);
        Button btnHistory = (Button)findViewById(R.id.btnHistory);


        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/Green_Avocado.ttf");

        btnEdit.setTypeface(custom_font);
        btnHistory.setTypeface(custom_font);

    }




    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_.jpg";
        File image = new File(Environment.getExternalStorageDirectory(),
                imageFileName);
        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }


    public void onCamera(View v) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    /**
     * This onClick method picks an image when users click the gallery button
     * @param v
     */
    public void onGallery (View v) {
        Intent pickPhotoFromGalleryIntent = new Intent();
        pickPhotoFromGalleryIntent.setAction(Intent.ACTION_PICK);
        pickPhotoFromGalleryIntent.setType("image/*");
        startActivityForResult(pickPhotoFromGalleryIntent,  REQUEST_IMAGE_PICK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 4;      // 1/4 of original image
                Bitmap b = BitmapFactory.decodeFile(mCurrentPhotoPath,options);
                imageView.setImageBitmap(b);
            } else if (requestCode == REQUEST_IMAGE_PICK) {
                if (data != null) {
                    //Toast.makeText(this, "pick done", Toast.LENGTH_SHORT).show();
                    Uri uri = data.getData();
                    mCurrentPhotoPath = getRealPathFromURI(this, uri);
                    //Toast.makeText(this, mCurrentPhotoPath, Toast.LENGTH_SHORT).show();
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 4;      // 1/4 of original image
                    Bitmap b = BitmapFactory.decodeFile(mCurrentPhotoPath,options);
                    imageView.setImageBitmap(b);
                }
            }
        }
    }

    /**
     *
     * @param context
     * @param contentUri
     * @return the absolute path of a image file from Uri
     */
    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public void onEdit(View v) {
        Intent intent = new Intent(this, EditActivity.class);
        intent.putExtra("imagePath", mCurrentPhotoPath);
        startActivity(intent);
    }

    public void onHistory(View v) {
        Intent intent = new Intent(this,HistoryActivity.class);
        startActivity(intent);
    }

}
