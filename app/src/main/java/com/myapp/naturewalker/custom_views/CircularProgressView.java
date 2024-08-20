package com.myapp.naturewalker.custom_views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.core.content.ContextCompat;

public class CircularProgressView extends View {

    private Paint paint;
    private RectF rectF;
    private float progress = 0f;
    private int color;
    private float strokeWidth = 15f;
    private int startAngle = 90;
    private int sweepAngle = 360;

    public CircularProgressView(Context context) {
        super(context);
        init(context);
    }

    public CircularProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CircularProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        rectF = new RectF();
        color = ContextCompat.getColor(context, android.R.color.holo_blue_bright);

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
    }

    public void setProgress(float value) {
        progress = Math.max(0f, Math.min(100f, value));
        invalidate();
    }

    public void setColor(int newColor) {
        color = newColor;
        invalidate();
    }

    public void setStrokeWidth(float width) {
        strokeWidth = width;
        invalidate();
    }

    public void setStartAngle(int angle) {
        startAngle = angle;
        invalidate();
    }

    public void setSweepAngle(int angle) {
        sweepAngle = angle;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float width = getWidth();
        float height = getHeight();

        paint.setColor(color);
        paint.setStrokeWidth(strokeWidth);

        rectF.set(strokeWidth / 2, strokeWidth / 2, width - strokeWidth / 2, height - strokeWidth / 2);

        // Draw background circle
        paint.setAlpha(50);
        canvas.drawArc(rectF, startAngle, sweepAngle, false, paint);

        // Draw progress arc
        paint.setAlpha(255);
        float progressSweepAngle = sweepAngle * progress / 100;
        canvas.drawArc(rectF, startAngle, progressSweepAngle, false, paint);
    }
}
