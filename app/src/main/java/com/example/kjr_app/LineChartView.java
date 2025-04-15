package com.example.kjr_app;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class LineChartView extends View {

    private List<Float> dataPoints = new ArrayList<>();
    private Paint linePaint;
    private Paint pointPaint;

    public LineChartView(Context context) {
        super(context);
        init();
    }

    public LineChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LineChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        linePaint = new Paint();
        linePaint.setColor(Color.RED);
        linePaint.setStrokeWidth(5);
        linePaint.setStyle(Paint.Style.STROKE);

        pointPaint = new Paint();
        pointPaint.setColor(Color.BLUE);
        pointPaint.setStyle(Paint.Style.FILL);
    }

    public void setDataPoints(List<Float> dataPoints) {
        this.dataPoints = dataPoints;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (dataPoints.size() == 0) {
            return;
        }

        float width = getWidth();
        float height = getHeight();

        float xStep = width / (dataPoints.size() - 1);
        float maxValue = Float.MIN_VALUE;
        for (float value : dataPoints) {
            if (value > maxValue) {
                maxValue = value;
            }
        }

        Path path = new Path();
        for (int i = 0; i < dataPoints.size(); i++) {
            float x = i * xStep;
            float y = height - (dataPoints.get(i) / maxValue) * height;
            if (i == 0) {
                path.moveTo(x, y);
            } else {
                path.lineTo(x, y);
            }
            canvas.drawCircle(x, y, 5, pointPaint);
        }
        canvas.drawPath(path, linePaint);
    }
}