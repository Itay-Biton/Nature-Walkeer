package com.myapp.naturewalker.custom_views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.myapp.naturewalker.R;
import com.myapp.naturewalker.objects.User;

import java.time.LocalDate;
import java.util.ArrayList;

public class WeeklyStepsBarChart extends View {

    private final Paint barPaint = new Paint();
    private final Paint barBGPaint = new Paint();
    private final Paint textPaint = new Paint();
    private final String[] days = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
    private float[] values = new float[7];
    private int highlightDay; // Thursday (0-based index)

    public WeeklyStepsBarChart(Context context) {
        super(context);
        init();
    }

    public WeeklyStepsBarChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void updateValues() {
        highlightDay = LocalDate.now().getDayOfWeek().getValue();
        highlightDay = (highlightDay == 7) ? 0 : highlightDay;

        ArrayList<Integer> weeklySteps = User.getInstance().getWeeklySteps();
        int todaySteps = User.getInstance().getTodaySteps();
        double max = 0;
        for (Integer weeklyStep : weeklySteps) {
            if (weeklyStep > max)
                max = weeklyStep;
        }
        if (todaySteps > max)
            max = todaySteps;
        max = 1.2 * max;

        for (int i=0; i<7; i++) {
            values[i] = (float) (weeklySteps.get(i)/max);
        }
        values[highlightDay] = (float) (todaySteps/max);
    }

    private void init() {
        barPaint.setColor(ContextCompat.getColor(getContext(), R.color.green));
        barBGPaint.setColor(ContextCompat.getColor(getContext(), R.color.light_gray));
        textPaint.setColor(ContextCompat.getColor(getContext(), android.R.color.black));
        textPaint.setTextSize(30f);
        textPaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();
        float barWidth = width / 9f; // 7 bars + 2 spaces on sides
        float maxBarHeight = height * 0.8f;
        float bottomPadding = height * 0.1f;

        for (int i = 0; i < 7; i++) {
            float left = barWidth + (i * barWidth);
            float top = height - bottomPadding - (maxBarHeight * values[i]);
            float right = left + barWidth * 0.8f;
            float bottom = height - bottomPadding;


            RectF rectBG = new RectF(left, height - bottomPadding - maxBarHeight, right, top);
            canvas.drawRect(rectBG, barBGPaint);

            RectF rect = new RectF(left, top, right, bottom);
            if (i == highlightDay) {
                barPaint.setAlpha(255);
                canvas.drawRect(rect, barPaint);
            }
            else {
                barPaint.setAlpha(100);
                canvas.drawRect(rect, barPaint);
            }

            canvas.drawText(days[i], left + (barWidth * 0.4f), height - (bottomPadding / 2f), textPaint);
            if (i < highlightDay)
                canvas.drawText(User.getInstance().getWeeklySteps().get(i)+"", left + (barWidth * 0.4f), maxBarHeight + (bottomPadding / 2f), textPaint);
            else
            if (i == highlightDay)
                canvas.drawText(User.getInstance().getTodaySteps()+"", left + (barWidth * 0.4f), maxBarHeight + (bottomPadding / 2f), textPaint);
        }
    }
}
