package com.giddy.image.giddysticker.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.giddy.image.giddysticker.R;
import com.giddy.image.giddysticker.adapter.StickerAdapter;
import com.giddy.image.giddysticker.sticker.StickerImageView;
import com.giddy.image.giddysticker.sticker.StickerView;
import com.giddy.image.giddysticker.utils.Constant;
import com.giddy.image.giddysticker.utils.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by HoangHiep on 5/14/2017.
 */

public class GiddyActivity extends Activity implements View.OnClickListener {
    private Toolbar toolbar;
    private ImageView imBack, imDone;

    private ImageView imageView, btStickerA, btStickerB, btStickerC, btStickerD, btStickerE;
    private TextView tvQuit;
    private ImageView underLine1, underLine2, underLine3, underLine4, underLine5;
    private Button btBack, btSave, btOk;
    private EditText editText;
    private FrameLayout canvas;
    private RecyclerView stickerRecyclerView;
    private ArrayList<Integer> stickerList = new ArrayList<>();
    private GridLayoutManager layoutManagerSticker;
    private BroadcastReceiver broadcastReceiver;
    private TextView btText;
    private LinearLayout linearLayout, lnRecycle;
    private TypedArray taSticker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giddy);
        imageView = (ImageView) findViewById(R.id.img_crop);
        Intent intent = getIntent();
        if (intent.getParcelableExtra(Constant.IMAGE_URI) != null) {
            Uri uri = intent.getParcelableExtra(Constant.IMAGE_URI);
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            imageView.setImageBitmap(bitmap);
        }
        if (intent.getParcelableExtra(Constant.IMAGE_BITMAP) != null) {
            imageView.setImageBitmap((Bitmap) intent.getParcelableExtra(Constant.IMAGE_BITMAP));
        }
        initView();
        IntentFilter filter = new IntentFilter("com.receiver.add.sticker");
        filter.addAction("com.change.frame");
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                try {
                    if (intent.getAction().equals("com.receiver.add.sticker")) {
                        int stickerPosition = intent.getIntExtra("sticker_id", -1);
                        if (stickerPosition != -1) {
                            StickerImageView iv_sticker = new StickerImageView(GiddyActivity.this);
                            iv_sticker.setImageResource(stickerList.get(stickerPosition));
                            canvas.addView(iv_sticker);
                            lnRecycle.setVisibility(View.GONE);
                            underLine1.setVisibility(View.INVISIBLE);
                            underLine2.setVisibility(View.INVISIBLE);
                            underLine3.setVisibility(View.INVISIBLE);
                            underLine4.setVisibility(View.INVISIBLE);
                            underLine5.setVisibility(View.INVISIBLE);
                        }
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        };
        registerReceiver(broadcastReceiver, filter);

    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_save);
        imBack = (ImageView) findViewById(R.id.im_back);
        imDone = (ImageView) findViewById(R.id.im_save);
        imBack.setOnClickListener(this);
        imDone.setOnClickListener(this);

        underLine1 = (ImageView) findViewById(R.id.iv_underline1);
        underLine2 = (ImageView) findViewById(R.id.iv_underline2);
        underLine3 = (ImageView) findViewById(R.id.iv_underline3);
        underLine4 = (ImageView) findViewById(R.id.iv_underline4);
        underLine5 = (ImageView) findViewById(R.id.iv_underline5);

        tvQuit = (TextView) findViewById(R.id.bt_quit);
        btStickerA = (ImageView) findViewById(R.id.iv_a);
        btStickerB = (ImageView) findViewById(R.id.iv_b);
        btStickerC = (ImageView) findViewById(R.id.iv_c);
        btStickerD = (ImageView) findViewById(R.id.iv_d);
        btStickerE = (ImageView) findViewById(R.id.iv_e);
        btOk = (Button) findViewById(R.id.bt_ok);
        tvQuit.setOnClickListener(this);
        editText = (EditText) findViewById(R.id.edt_text);
        btOk.setOnClickListener(this);
        btStickerA.setOnClickListener(this);
        btStickerB.setOnClickListener(this);
        btStickerC.setOnClickListener(this);
        btStickerD.setOnClickListener(this);
        btStickerE.setOnClickListener(this);
        linearLayout = (LinearLayout) findViewById(R.id.ln_setText);
        lnRecycle = (LinearLayout) findViewById(R.id.ln_recycle);

        canvas = (FrameLayout) findViewById(R.id.canvasView);
        canvas.setOnClickListener(this);

        stickerRecyclerView = (RecyclerView) findViewById(R.id.rv_sticker);
        layoutManagerSticker = new GridLayoutManager(this, 4, LinearLayoutManager.VERTICAL, false);
    }

    private void initRV() {
        StickerAdapter stickerAdapter = new StickerAdapter(stickerList, this);
        stickerRecyclerView.setLayoutManager(layoutManagerSticker);
        stickerRecyclerView.setHasFixedSize(true);
        stickerRecyclerView.setAdapter(stickerAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_quit:
                lnRecycle.setVisibility(View.GONE);
                underLine1.setVisibility(View.INVISIBLE);
                underLine2.setVisibility(View.INVISIBLE);
                underLine3.setVisibility(View.INVISIBLE);
                underLine4.setVisibility(View.INVISIBLE);
                underLine5.setVisibility(View.INVISIBLE);
                Log.d("asdasda", "adasdsadas");
                break;
            case R.id.iv_a:
                taSticker = getResources().obtainTypedArray(R.array.a);
                stickerList.clear();
                for (int i = 0; i < taSticker.length(); i++) {
                    stickerList.add(taSticker.getResourceId(i, 0));
                }
                taSticker.recycle();
                initRV();
                underLine1.setVisibility(View.INVISIBLE);
                underLine2.setVisibility(View.INVISIBLE);
                underLine3.setVisibility(View.VISIBLE);
                underLine4.setVisibility(View.INVISIBLE);
                underLine5.setVisibility(View.INVISIBLE);
                lnRecycle.setVisibility(View.VISIBLE);
                break;
            case R.id.iv_b:
                taSticker = getResources().obtainTypedArray(R.array.b);
                stickerList.clear();
                for (int i = 0; i < taSticker.length(); i++) {
                    stickerList.add(taSticker.getResourceId(i, 0));
                }
                taSticker.recycle();
                initRV();
                underLine1.setVisibility(View.VISIBLE);
                underLine2.setVisibility(View.INVISIBLE);
                underLine3.setVisibility(View.INVISIBLE);
                underLine4.setVisibility(View.INVISIBLE);
                underLine5.setVisibility(View.INVISIBLE);
                lnRecycle.setVisibility(View.VISIBLE);
                break;
            case R.id.iv_c:
                taSticker = getResources().obtainTypedArray(R.array.c);
                stickerList.clear();
                for (int i = 0; i < taSticker.length(); i++) {
                    stickerList.add(taSticker.getResourceId(i, 0));
                }
                taSticker.recycle();
                initRV();
                underLine1.setVisibility(View.INVISIBLE);
                underLine2.setVisibility(View.INVISIBLE);
                underLine3.setVisibility(View.INVISIBLE);
                underLine4.setVisibility(View.VISIBLE);
                underLine5.setVisibility(View.INVISIBLE);
                lnRecycle.setVisibility(View.VISIBLE);
                break;
            case R.id.iv_d:
                taSticker = getResources().obtainTypedArray(R.array.d);
                stickerList.clear();
                for (int i = 0; i < taSticker.length(); i++) {
                    stickerList.add(taSticker.getResourceId(i, 0));
                }
                taSticker.recycle();
                initRV();
                underLine1.setVisibility(View.INVISIBLE);
                underLine2.setVisibility(View.VISIBLE);
                underLine3.setVisibility(View.INVISIBLE);
                underLine4.setVisibility(View.INVISIBLE);
                underLine5.setVisibility(View.INVISIBLE);
                lnRecycle.setVisibility(View.VISIBLE);
                break;
            case R.id.iv_e:
                taSticker = getResources().obtainTypedArray(R.array.e);
                stickerList.clear();
                for (int i = 0; i < taSticker.length(); i++) {
                    stickerList.add(taSticker.getResourceId(i, 0));
                }
                taSticker.recycle();
                initRV();

                underLine1.setVisibility(View.INVISIBLE);
                underLine2.setVisibility(View.INVISIBLE);
                underLine3.setVisibility(View.INVISIBLE);
                underLine4.setVisibility(View.INVISIBLE);
                underLine5.setVisibility(View.VISIBLE);
                lnRecycle.setVisibility(View.VISIBLE);
                break;
            case R.id.im_back:
                finish();
                break;
            case R.id.canvasView:
                if (imageView.getDrawable() == null) {
                    // Toast.makeText(this, R.string.cannot_save, Toast.LENGTH_SHORT).show();
                } else {
                    canvas.setDrawingCacheEnabled(true);
                    for (int i = 0; i <= canvas.getChildCount(); i++) {
                        if (canvas.getChildAt(i) != imageView && canvas.getChildAt(i) != imageView) {
                            try {
                                ((StickerView) canvas.getChildAt(i)).makeStaticSticker();
                            } catch (NullPointerException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                    canvas.destroyDrawingCache();
                }
                break;
            case R.id.im_save:
                if (imageView.getDrawable() == null) {
                    // Toast.makeText(this, R.string.cannot_save, Toast.LENGTH_SHORT).show();
                } else {
                    canvas.setDrawingCacheEnabled(true);
                    for (int i = 0; i <= canvas.getChildCount(); i++) {
                        if (canvas.getChildAt(i) != imageView && canvas.getChildAt(i) != imageView) {
                            try {
                                ((StickerView) canvas.getChildAt(i)).makeStaticSticker();
                            } catch (NullPointerException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                    canvas.destroyDrawingCache();
                    saveImageToStorage();
                }
                break;
            default:
                break;
        }

    }

    private void saveImageToStorage() {
        try {
            canvas.setDrawingCacheEnabled(true);
            Log.d("ádasdasdasdas", "ádadasdasdas");
            canvas.destroyDrawingCache();
            Bitmap bitmap = canvas.getDrawingCache();
            String root = Environment.getExternalStorageDirectory().toString()
                    + "/" + Environment.DIRECTORY_PICTURES;
            String folderPath = root + "/GiddySticker";
            File newDir = new File(folderPath);
            newDir.mkdirs();
            DateFormat df = new SimpleDateFormat("yyyyMMdd_HHmmss");
            Date date = new Date(System.currentTimeMillis());
            String sdt = df.format(date);
            String imageName = "GiddySticker_" + sdt + ".jpg";
            System.out.println(imageName);
            File file = new File(newDir, imageName);
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            Utils.addImageToGallery(folderPath + "/" + imageName, date.getTime(), this);
            out.flush();
            out.close();
            Toast.makeText(this, getString(R.string.save_success), Toast.LENGTH_SHORT).show();
            Intent intentResult = new Intent(this, ResultActivity.class);
            intentResult.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intentResult.setAction("show.result");
            intentResult.putExtra("path", folderPath + "/" + imageName);
            startActivity(intentResult);
            finish();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, getString(R.string.save_unsuccess), Toast.LENGTH_SHORT).show();
        }
    }
}
