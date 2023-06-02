package com.example.fingerrunning;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
//    private InfiniteScrollView infiniteScrollView;
//    private Drawable drawableTop, drawableBottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        scoreTextView = findViewById(R.id.kcal);

        InfiniteScrollView infiniteScrollView = findViewById(R.id.infiniteScrollView);

        Drawable drawable = getResources().getDrawable(R.drawable.background, null);
        infiniteScrollView.setDrawable(drawable);

    }
}
