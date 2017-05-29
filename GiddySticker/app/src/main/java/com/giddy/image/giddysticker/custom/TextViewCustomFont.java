package com.giddy.image.giddysticker.custom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by binhnk on 5/22/2017.
 */

@SuppressLint("AppCompatCustomView")
public class TextViewCustomFont  extends TextView{

    public TextViewCustomFont(Context context) {
        super(context);
        applyCustomFont(context);
    }

    public TextViewCustomFont(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        applyCustomFont(context);
    }

    public TextViewCustomFont(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        applyCustomFont(context);
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = FontCache.getTypeface("Cattleya.otf", context);
        setTypeface(customFont);
    }
}
