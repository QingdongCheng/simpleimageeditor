package edu.purdue.qingdong.simpleimageeditor;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    ArrayList<Image> imageArry = new ArrayList<Image>();
    ImageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        DataBaseHandler db = new DataBaseHandler(this);
        /*
        // get image from drawable
        Bitmap image = BitmapFactory.decodeResource(getResources(),
                R.drawable.android);

        // convert bitmap to byte
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte imageInByte[] = stream.toByteArray();

        //  CRUD Operations

        // Inserting Images

        // db.addImage(new Image("Android", imageInByte));
        // display main List view bcard and contact name

        // Reading all contacts from database

        */

        List<Image> images = db.getAllImages();
        for (Image cn : images) {
            String log = "ID:" + cn.getID() + " Name: " + cn.getName()
                    + " ,Image: " + cn.getImage();

            //add contacts data in arrayList
            imageArry.add(cn);


        }
        adapter = new ImageAdapter(this, R.layout.screen_list,
                imageArry);
        ListView dataList = (ListView) findViewById(R.id.list);
        dataList.setAdapter(adapter);
    }
}
