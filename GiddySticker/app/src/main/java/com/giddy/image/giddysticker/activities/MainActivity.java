package com.giddy.image.giddysticker.activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.giddy.image.giddysticker.R;
import com.giddy.image.giddysticker.utils.Constant;
import com.giddy.image.giddysticker.utils.Utils;
import com.mz.A;
import com.mz.ZAndroidSystemDK;
import com.yalantis.ucrop.UCrop;

import java.io.File;

/**
 * Created by HoangHiep on 5/14/2017.
 */

public class MainActivity extends Activity implements View.OnClickListener {

    private static final String[] PERMISSIONS = {Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,Manifest.permission.SET_WALLPAPER,
            Manifest.permission.SET_WALLPAPER_HINTS};
    private static final int REQUEST_CODE_PERMISSION = 2;

    private LinearLayout btCamera, btGallery;
    private ImageView img;
    private ContentValues values;
    private Uri mUri;
    private String imageurl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Utils.checkPermission(PERMISSIONS, this) != PackageManager.PERMISSION_GRANTED) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage(getString(R.string.permission_type));
                builder.setNegativeButton(getString(R.string.accept), new DialogInterface.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.M)
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        requestPermissions(PERMISSIONS, REQUEST_CODE_PERMISSION);
                    }
                });
                builder.setPositiveButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                builder.show();
            }
        }

        ZAndroidSystemDK.init(this);
        A.f(this);
    }

    private void initView() {
        btCamera = (LinearLayout) findViewById(R.id.bt_camera);
        btGallery = (LinearLayout) findViewById(R.id.bt_gallery);

        btCamera.setOnClickListener(this);
        btGallery.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_camera:
                values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, "New Picture");
                values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
                mUri = getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, mUri);
                startActivityForResult(intent, Constant.CAMERA_REQUEST);
                break;
            case R.id.bt_gallery:
                Intent i = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, Constant.RESULT_LOAD_IMAGE);
                break;
            default:
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        Uri imgUri = null;
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            Log.d("ádasdasdas","đâsdasdas");
            imgUri = Uri.fromFile(new File(picturePath));
            Log.d("imgUri", imgUri.toString());
            UCrop.Options options = new UCrop.Options();
            UCrop.of(imgUri, imgUri)
                    .withMaxResultSize(width,height)
                    .withOptions(options)
                    .start(this);

        }
        if (requestCode == Constant.CAMERA_REQUEST && resultCode == Activity.RESULT_OK ) {
            try {

                imageurl = getRealPathFromURI(mUri);
                File file = new File(imageurl);
                Uri uri = Uri.fromFile(file);
                Log.d("imgUri", uri.toString());
                Log.d("ádasdasdas","đâsdasdas");
                UCrop.Options options = new UCrop.Options();
                UCrop.of(uri, uri)
                        .withMaxResultSize(width,height)
                        .withOptions(options)
                        .start(this);
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            final Uri resultUri = UCrop.getOutput(data);
            Intent intent = new Intent(this, GiddyActivity.class);
            intent.putExtra(Constant.IMAGE_URI, resultUri);
            startActivity(intent);

        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
            Log.d("cropError", cropError.toString());
        }

    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_PERMISSION:
                if (Utils.checkPermission(PERMISSIONS, MainActivity.this) != PackageManager.PERMISSION_GRANTED) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage(getString(R.string.permission_type));
                    builder.setNegativeButton(getString(R.string.accept), new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.M)
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            requestPermissions(PERMISSIONS, REQUEST_CODE_PERMISSION);
                        }
                    });
                    builder.setPositiveButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
                    builder.show();
                }
                break;

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        }
    }
}

