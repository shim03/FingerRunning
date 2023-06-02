package com.example.fingerrunning;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.Scroller;

public class InfiniteScrollView extends View {
    private Drawable drawable;
    private float scrollY = 0;
    private int imageHeight;
    private float lastTouchY; // 最後にタッチされたY座標を追跡
    private double points = 0;  // ポイントを保存する変数を追加
    private double calorie=0;
    private Scroller mScroller;
    private VelocityTracker mVelocityTracker = null;


    public InfiniteScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScroller = new Scroller(context);
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
        imageHeight = drawable.getIntrinsicHeight();
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int canvasHeight = canvas.getHeight();
        int canvasWidth = canvas.getWidth();
        int topLimit = (int)(canvasHeight * 1.0 / 5);  // 上限をビューの高さの1/3に設定
        int bottomLimit = canvasHeight;

        if (drawable != null) {
            canvas.save();
            canvas.clipRect(0, topLimit, canvasWidth, canvasHeight);  // 描画範囲を制限

            int top = -((int) scrollY % imageHeight); // 修正
            while (top < getHeight()) {
                drawable.setBounds(0, top, getWidth(), top + imageHeight);
                drawable.draw(canvas);
                top += imageHeight;
            }
        }
        canvas.restore();


        // 走行距離，消費カロリーの描画
        Paint paint = new Paint();
        paint.setColor(Color.CYAN);  // テキストの色
        paint.setTextSize(150);  // テキストの大きさ
        String text;
        String text_calorie;
        text = String.format("%.2f",points);  // 表示するテキスト
        text_calorie= String.format("%.2f",calorie);
        canvas.drawText(text, canvasWidth/7, topLimit-120 , paint);  // テキストを描画（10,50 は描画する位置）
        canvas.drawText(text_calorie, canvasWidth/2+50, topLimit-120, paint);

        //固定文字の描画
        Paint paint_static = new Paint();
        paint_static.setColor(Color.BLACK);  // テキストの色
        paint_static.setTextSize(60);  // テキストの大きさ
        String static_soko="走行距離";
        String static_calorie="消費カロリー";
        String static_km="km";
        String static_kcal="kcal";
        canvas.drawText(static_soko, canvasWidth/7,topLimit-300, paint_static);
        canvas.drawText(static_calorie, canvasWidth/2+20,topLimit-300, paint_static);
        canvas.drawText(static_km, canvasWidth/7+250,topLimit-30, paint_static);
        canvas.drawText(static_kcal, canvasWidth/2+290,topLimit-30, paint_static);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                lastTouchY = event.getY(); // タッチ開始時のY座標を保存
                return true;

            case MotionEvent.ACTION_MOVE:
                float deltaY = -event.getY() + lastTouchY; // 以前のタッチ位置からの変化量を計算
                lastTouchY = event.getY(); // 最新のタッチY座標を更新
                scrollY += deltaY; // スクロール量に変化量を追加
                if (scrollY < 0) {
                    scrollY += imageHeight;
                } else if (scrollY > imageHeight) {
                    scrollY -= imageHeight;
                }
                points+=scrollY/1000000;
                calorie=points/50;
                invalidate(); // ビューを再描画
                return true;
            case MotionEvent.ACTION_UP:
                //速度の計算
                mVelocityTracker.computeCurrentVelocity(1000); //1000は単位をミリ秒にするためのスケールファクター
                float velocityY = mVelocityTracker.getYVelocity();

                //スクロールの開始
                mScroller.fling(0, (int) scrollY, 0, (int) -velocityY, 0, 0, 0, Integer.MAX_VALUE);
                invalidate();

                //VelocityTrackerのリセット
                mVelocityTracker.clear();
                mVelocityTracker.recycle();
                mVelocityTracker = null;
                return true;
        }
        return super.onTouchEvent(event);
    }
    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollY = mScroller.getCurrY();
            invalidate();
        }
    }
}
