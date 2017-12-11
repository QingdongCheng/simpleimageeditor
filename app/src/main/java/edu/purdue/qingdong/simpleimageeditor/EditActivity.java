package edu.purdue.qingdong.simpleimageeditor;
  
 +import android.app.Dialog;
  import android.content.ActivityNotFoundException;
  import android.content.ContentResolver;
  import android.content.DialogInterface;
 @@ -22,11 +23,14 @@
  import android.text.InputType;
  import android.util.DisplayMetrics;
  import android.util.Log;
 +import android.view.LayoutInflater;
  import android.view.MotionEvent;
  import android.view.View;
 +import android.view.ViewGroup;
  import android.widget.Button;
  import android.widget.EditText;
  import android.widget.ImageView;
 +import android.widget.SeekBar;
  import android.widget.Toast;
  
  import java.io.ByteArrayOutputStream;
 @@ -58,15 +62,16 @@
      ImageView editImageView;
      String imagePath;
      String grayPhotoPath;
 +    String brightnessPhotoPath;
 +    String contrastPhotoPath;
 +    String saturationPhotoPath;
      private static final int REQUEST_CROP = 1;
  
      private CallbackManager callbackManager;
      private ShareDialog shareDialog;
      private Bitmap bitmap;
      private Bitmap bitmapAddText;
  
 -
 -
      private DataBaseHandler db;
  
      @Override
 @@ -76,7 +81,6 @@ protected void onCreate(Bundle savedInstanceState) {
  
          db = new DataBaseHandler(this);
  
 -
          editImageView = (ImageView) findViewById(R.id.editImageView);
          imagePath = getIntent().getStringExtra("imagePath");
  
 @@ -95,6 +99,351 @@ protected void onCreate(Bundle savedInstanceState) {
          initFacebook();
      }
  
 +    float brightness;
 +    float contrast;
 +    float saturation;
 +
 +    public void onClickBrightness(View view)
 +    {
 +        dialogBrightness();
 +    }
 +
 +    public void onClickContrast(View view)
 +    {
 +        dialogContrast();
 +    }
 +
 +    public void onClickSaturation(View view)
 +    {
 +        dialogSaturation();
 +    }
 +
 +    public void dialogBrightness()
 +    {
 +        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
 +        final SeekBar adjustment = new SeekBar(this);
 +        adjustment.setMax(255);
 +        adjustment.setKeyProgressIncrement(1);
 +
 +        builder.setTitle("Brightness");
 +        builder.setMessage("Drag around to adjust brightness.");
 +        builder.setView(adjustment);
 +        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener()
 +        {
 +            public void onClick(DialogInterface dialog, int which)
 +            {
 +                Bitmap bitmap;
 +                if (imagePath != null) {
 +                    bitmap = BitmapFactory.decodeFile(imagePath);
 +                } else {
 +                    bitmap= BitmapFactory.decodeResource(getResources(), R.drawable.android);
 +                }
 +
 +                Bitmap brightnessBmp = adjustBrightness(bitmap, brightness);
 +                File brightnessPhotoFile = null;
 +                try {
 +                    brightnessPhotoFile = createSaturationImageFile();
 +                } catch (IOException e) {
 +
 +                }
 +
 +                if (brightnessPhotoFile != null) {
 +                    try {
 +                        FileOutputStream out = new FileOutputStream(brightnessPhotoFile);
 +                        brightnessBmp.compress(Bitmap.CompressFormat.JPEG, 90, out);
 +                        out.flush();
 +                        out.close();
 +
 +                    } catch (Exception e) {
 +                        e.printStackTrace();
 +                    }
 +
 +                }
 +
 +                BitmapFactory.Options options = new BitmapFactory.Options();
 +                options.inSampleSize = 2;      // 1/4 of original image
 +                Bitmap b = BitmapFactory.decodeFile(brightnessPhotoPath, options);
 +                editImageView.setImageBitmap(b);
 +                imagePath = brightnessPhotoPath;
 +            }
 +        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener()
 +        {
 +            public void onClick(DialogInterface dialog, int which)
 +            {
 +                dialog.cancel();
 +            }
 +        }
 +        );
 +        builder.show();
 +
 +        adjustment.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
 +            @Override
 +            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
 +            {
 +                if (fromUser)
 +                {
 +                    brightness = (float) progress;
 +                }
 +            }
 +
 +            @Override
 +            public void onStartTrackingTouch(SeekBar seekBar) {
 +
 +            }
 +
 +            @Override
 +            public void onStopTrackingTouch(SeekBar seekBar) {
 +
 +            }
 +        });
 +    }
 +
 +    public void dialogContrast()
 +    {
 +        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
 +        final SeekBar adjustment = new SeekBar(this);
 +        adjustment.setMax(255);
 +        adjustment.setKeyProgressIncrement(1);
 +
 +        builder.setTitle("Contrast");
 +        builder.setMessage("Drag around to adjust contrast.");
 +        builder.setView(adjustment);
 +        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener()
 +        {
 +            public void onClick(DialogInterface dialog, int which)
 +            {
 +                Bitmap bitmap;
 +                if (imagePath != null) {
 +                    bitmap = BitmapFactory.decodeFile(imagePath);
 +                } else {
 +                    bitmap= BitmapFactory.decodeResource(getResources(), R.drawable.android);
 +                }
 +
 +                Bitmap contrastBmp = adjustContrast(bitmap, contrast);
 +                File contrastPhotoFile = null;
 +                try {
 +                    contrastPhotoFile = createSaturationImageFile();
 +                } catch (IOException e) {
 +
 +                }
 +
 +                if (contrastPhotoFile != null) {
 +                    try {
 +                        FileOutputStream out = new FileOutputStream(contrastPhotoFile);
 +                        contrastBmp.compress(Bitmap.CompressFormat.JPEG, 90, out);
 +                        out.flush();
 +                        out.close();
 +
 +                    } catch (Exception e) {
 +                        e.printStackTrace();
 +                    }
 +
 +                }
 +
 +                BitmapFactory.Options options = new BitmapFactory.Options();
 +                options.inSampleSize = 2;      // 1/4 of original image
 +                Bitmap b = BitmapFactory.decodeFile(contrastPhotoPath, options);
 +                editImageView.setImageBitmap(b);
 +                imagePath = contrastPhotoPath;
 +            }
 +        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener()
 +        {
 +             public void onClick(DialogInterface dialog, int which)
 +             {
 +                 dialog.cancel();
 +             }
 +        }
 +        );
 +        builder.show();
 +
 +        adjustment.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
 +            @Override
 +            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
 +            {
 +                if (fromUser)
 +                {
 +                    contrast = (float) progress;
 +                }
 +            }
 +
 +            @Override
 +            public void onStartTrackingTouch(SeekBar seekBar) {
 +
 +            }
 +
 +            @Override
 +            public void onStopTrackingTouch(SeekBar seekBar) {
 +
 +            }
 +        });
 +    }
 +
 +    public void dialogSaturation()
 +    {
 +        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
 +        final SeekBar adjustment = new SeekBar(this);
 +        adjustment.setMax(255);
 +        adjustment.setKeyProgressIncrement(1);
 +
 +        builder.setTitle("Saturation");
 +        builder.setMessage("Drag around to adjust saturation.");
 +        builder.setView(adjustment);
 +        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener()
 +        {
 +            public void onClick(DialogInterface dialog, int which)
 +            {
 +                Bitmap bitmap;
 +                if (imagePath != null) {
 +                    bitmap = BitmapFactory.decodeFile(imagePath);
 +                } else {
 +                    bitmap= BitmapFactory.decodeResource(getResources(), R.drawable.android);
 +                }
 +
 +                Bitmap saturationBmp = adjustSaturation(bitmap, saturation);
 +                File saturationPhotoFile = null;
 +                try {
 +                    saturationPhotoFile = createSaturationImageFile();
 +                } catch (IOException e) {
 +
 +                }
 +
 +                if (saturationPhotoFile != null) {
 +                    try {
 +                        FileOutputStream out = new FileOutputStream(saturationPhotoFile);
 +                        saturationBmp.compress(Bitmap.CompressFormat.JPEG, 90, out);
 +                        out.flush();
 +                        out.close();
 +
 +                    } catch (Exception e) {
 +                        e.printStackTrace();
 +                    }
 +
 +                }
 +
 +                BitmapFactory.Options options = new BitmapFactory.Options();
 +                options.inSampleSize = 2;      // 1/4 of original image
 +                Bitmap b = BitmapFactory.decodeFile(saturationPhotoPath, options);
 +                editImageView.setImageBitmap(b);
 +                imagePath = saturationPhotoPath;
 +            }
 +        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener()
 +        {
 +            public void onClick(DialogInterface dialog, int which)
 +            {
 +                dialog.cancel();
 +            }
 +        });
 +        builder.show();
 +
 +        adjustment.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
 +            @Override
 +            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
 +            {
 +                if (fromUser)
 +                {
 +                    saturation = (float) progress;
 +                }
 +            }
 +
 +            @Override
 +            public void onStartTrackingTouch(SeekBar seekBar) {
 +
 +            }
 +
 +            @Override
 +            public void onStopTrackingTouch(SeekBar seekBar) {
 +
 +            }
 +        });
 +    }
 +
 +    public Bitmap adjustSaturation(Bitmap bmpOriginal, float saturation)
 +    {
 +        int width, height;
 +        height = bmpOriginal.getHeight();
 +        width = bmpOriginal.getWidth();
 +        Bitmap bmpSaturation = Bitmap.createBitmap(width, height, bmpOriginal.getConfig());
 +        Canvas c = new Canvas(bmpSaturation);
 +        Paint paint = new Paint();
 +        ColorMatrix cm = new ColorMatrix();
 +        cm.setSaturation(saturation);
 +        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
 +        paint.setColorFilter(f);
 +        c.drawBitmap(bmpOriginal, 0, 0, paint);
 +        return bmpSaturation;
 +    }
 +
 +    public Bitmap adjustBrightness(Bitmap bmpOriginal, float brightness)
 +    {
 +        ColorMatrix cm = new ColorMatrix(new float[]
 +                {
 +                        0, 0, 0, 0, brightness,
 +                        0, 0, 0, 0, brightness,
 +                        0, 0, 0, 0, brightness,
 +                        0, 0, 0, 1, 0
 +                });
 +
 +        Bitmap bmpBrightness = Bitmap.createBitmap(bmpOriginal.getWidth(), bmpOriginal.getHeight(), bmpOriginal.getConfig());
 +        Canvas canvas = new Canvas(bmpBrightness);
 +        Paint paint = new Paint();
 +        paint.setColorFilter(new ColorMatrixColorFilter(cm));
 +        canvas.drawBitmap(bmpOriginal, 0, 0, paint);
 +        return bmpBrightness;
 +    }
 +
 +    public static Bitmap adjustContrast(Bitmap bmpOriginal, float contrast)
 +    {
 +        ColorMatrix cm = new ColorMatrix(new float[]
 +                {
 +                        contrast, 0, 0, 0, 0,
 +                        0, contrast, 0, 0, 0,
 +                        0, 0, contrast, 0, 0,
 +                        0, 0, 0, 1, 0
 +                });
 +
 +        Bitmap bmpContrast = Bitmap.createBitmap(bmpOriginal.getWidth(), bmpOriginal.getHeight(), bmpOriginal.getConfig());
 +        Canvas canvas = new Canvas(bmpContrast);
 +        Paint paint = new Paint();
 +        paint.setColorFilter(new ColorMatrixColorFilter(cm));
 +        canvas.drawBitmap(bmpOriginal, 0, 0, paint);
 +        return bmpContrast;
 +    }
 +
 +    private File createSaturationImageFile() throws IOException
 +    {
 +        // Create an image file name
 +        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
 +        String imageFileName = "Saturation_" + timeStamp + "_.jpg";
 +        File image = new File(Environment.getExternalStorageDirectory(),
 +                imageFileName);
 +        // Save a file: path for use with ACTION_VIEW intents
 +        saturationPhotoPath = image.getAbsolutePath();
 +        return image;
 +    }
 +
 +    private File createBrightnessImageFile() throws IOException
 +    {
 +        // Create an image file name
 +        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
 +        String imageFileName = "Brightness_" + timeStamp + "_.jpg";
 +        File image = new File(Environment.getExternalStorageDirectory(),
 +                imageFileName);
 +        // Save a file: path for use with ACTION_VIEW intents
 +        brightnessPhotoPath = image.getAbsolutePath();
 +        return image;
 +    }
 +
 +    private File createContrastImageFile() throws IOException
 +    {
 +        // Create an image file name
 +        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
 +        String imageFileName = "Contrast_" + timeStamp + "_.jpg";
 +        File image = new File(Environment.getExternalStorageDirectory(),
 +                imageFileName);
 +        // Save a file: path for use with ACTION_VIEW intents
 +        contrastPhotoPath = image.getAbsolutePath();
 +        return image;
 +    }
  
      public void onCrop(View v) {
  
         try {
 
             if (imagePath == null) {
                 Toast.makeText(this, "Take a photo or pick one from gallery first", Toast.LENGTH_LONG).show();
                 return;
             }
             Intent intent = new Intent("com.android.camera.action.CROP");
             //intent.setClassName("com.android.camera", "com.android.camera.CropImage");
             File file = new File(imagePath);
             Uri uri = Uri.fromFile(file);
             intent.setDataAndType(uri, "image/*");
             intent.putExtra("crop", "true");
             intent.putExtra("aspectX", 1);
             intent.putExtra("aspectY", 1);
             intent.putExtra("outputX", 96);
             intent.putExtra("outputY", 96);
             intent.putExtra("scaleUpIfNeeded",true);
             //intent.putExtra("noFaceDetection", true);
             intent.putExtra("return-data", true);
             startActivityForResult(intent, REQUEST_CROP);
         } catch (ActivityNotFoundException e) {
 
         }
     }
 
 
     private void initFacebook() {
         FacebookSdk.sdkInitialize(getApplicationContext());
         callbackManager = CallbackManager.Factory.create();
         shareDialog = new ShareDialog(this);
         // this part is optional
         shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
 
             @Override
             public void onSuccess(Sharer.Result result) {
                 //
             }
 
             @Override
             public void onCancel() {
 
             }
 
             @Override
             public void onError(FacebookException error) {
 
             }
         });
     }
 
     @Override
     protected void onActivityResult(int requestCode, int resultCode, Intent data) {
         super.onActivityResult(requestCode, resultCode, data);
         //facebook callback
         callbackManager.onActivityResult(requestCode, resultCode, data);
         if (resultCode == RESULT_OK) {
             if (requestCode == REQUEST_CROP) { // get the cropped image and show in editview
                 if (data != null) {
                     //Uri uri = data.getData();
                     //editImageView.setImageURI(uri);
                     Bitmap bm = data.getParcelableExtra("data");
                     editImageView.setImageBitmap(bm);
                     Toast.makeText(this, "Crop is done", Toast.LENGTH_LONG).show();
                 }
             }
         }
     }
 
     /**
      * share to facebook
      * if facebook not installed, open the browser
      *
      * @param view
      */
     public void shareToFacebook(View view) {
         //share link, more information on: https://developers.facebook.com/docs/sharing/android
         if (ShareDialog.canShow(ShareLinkContent.class)) {
             ShareLinkContent linkContent = new ShareLinkContent.Builder()
                     .setContentUrl(Uri.parse("https://developers.facebook.com"))
                     .build();
             shareDialog.show(linkContent);
         }
     }
 
     public void addText(View v) {
         BitmapFactory.Options options = new BitmapFactory.Options();
         options.inSampleSize = 2; // 1/2 of original image
 
         if (imagePath == null) {
             //not get the pic from gallery or take picture from camera
             bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.android);
         } else {
             bitmap = BitmapFactory.decodeFile(imagePath,options);
 
         }
 
         bitmapAddText = bitmap.copy(Bitmap.Config.ARGB_8888, true);
 
 
         /*Bitmap bitmap=Bitmap.createBitmap(800,600,Bitmap.Config.ARGB_8888);*/
         final Canvas canvas=new Canvas(bitmapAddText);
         final Paint p=new Paint();
         p.setColor(0xffff0000);//set the color
         p.setAntiAlias(true);//anti lias
         p.setTextSize(85);//set text size
 
         AlertDialog.Builder builder = new AlertDialog.Builder(this);
         builder.setTitle("Type the text");
 
         // Set up the input
         final EditText input = new EditText(this);
         // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
         input.setInputType(InputType.TYPE_CLASS_TEXT);
         //input.setRawInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
         input.setHint("Type something...");
         builder.setView(input);
 
         // Set up the buttons
         builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialog, int which) {
                 m_Text = input.getText().toString();
                 canvas.drawText(m_Text,40,100,p);//draw on the canvas
                 editImageView.setImageBitmap(bitmapAddText);
                 saveImage(bitmapAddText);
             }
         });
         builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialog, int which) {
                 dialog.cancel();
             }
         });
 
         builder.show();
 
     }
 
     public void saveImage(Bitmap bmInput) {
         try {
             File file = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis()+ ".jpg");
             FileOutputStream strem = new FileOutputStream(file);
             bmInput.compress(Bitmap.CompressFormat.JPEG, 100, strem);
             strem.close();
             imagePath = file.getPath();
         } catch (Exception e) {
             e.printStackTrace();
         }
     }
 
     public void onSaveToHistory(View view) {
         BitmapFactory.Options options = new BitmapFactory.Options();
         options.inSampleSize = 2;
 
         Bitmap image = BitmapFactory.decodeFile(imagePath, options);
 
         // convert bitmap to byte
         ByteArrayOutputStream stream = new ByteArrayOutputStream();
         image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
         byte imageInByte[] = stream.toByteArray();
 
         //  CRUD Operations
 
         // Inserting Images
         Image image1 = new Image(" ", imageInByte);
 
         db.addImage(image1);
 
     }
 
 
     public Bitmap toGrayscale(Bitmap bmpOriginal)
     {
         int width, height;
         height = bmpOriginal.getHeight();
         width = bmpOriginal.getWidth();
         Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
         Canvas c = new Canvas(bmpGrayscale);
         Paint paint = new Paint();
         ColorMatrix cm = new ColorMatrix();
         cm.setSaturation(0);
         ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
         paint.setColorFilter(f);
         c.drawBitmap(bmpOriginal, 0, 0, paint);
         return bmpGrayscale;
     }
 
     private File createGrayImageFile() throws IOException {
         // Create an image file name
         String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
         String imageFileName = "Gray_" + timeStamp + "_.jpg";
         File image = new File(Environment.getExternalStorageDirectory(),
                 imageFileName);
         // Save a file: path for use with ACTION_VIEW intents
         grayPhotoPath = image.getAbsolutePath();
         return image;
     }
 
     public void onGrayscale(View v) {
         Bitmap bitmap;
         if (imagePath != null) {
             bitmap = BitmapFactory.decodeFile(imagePath);
         } else {
             bitmap= BitmapFactory.decodeResource(getResources(), R.drawable.android);
         }
 
         Bitmap grayBmp = toGrayscale(bitmap);
         File grayPhotoFile = null;
         try {
             grayPhotoFile = createGrayImageFile();
         } catch (IOException e) {
 
         }
 
         if (grayPhotoFile != null) {
             try {
                 FileOutputStream out = new FileOutputStream(grayPhotoFile);
                 grayBmp.compress(Bitmap.CompressFormat.JPEG, 90, out);
                 out.flush();
                 out.close();
 
             } catch (Exception e) {
                 e.printStackTrace();
             }
 
         }
 
         BitmapFactory.Options options = new BitmapFactory.Options();
         options.inSampleSize = 2;      // 1/4 of original image
         Bitmap b = BitmapFactory.decodeFile(grayPhotoPath, options);
         editImageView.setImageBitmap(b);
         imagePath = grayPhotoPath;
 
     }
 
 }
