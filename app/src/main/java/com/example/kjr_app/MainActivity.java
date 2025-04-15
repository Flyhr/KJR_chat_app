package com.example.kjr_app;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private LineChartView kneeChart;
    private LineChartView temperatureChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        kneeChart = findViewById(R.id.knee_chart);
        temperatureChart = findViewById(R.id.temperature_chart);

        // 模拟屈膝度数据
        List<Float> kneeData = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            kneeData.add((float) (Math.random() * 100));
        }
        kneeChart.setDataPoints(kneeData);

        // 模拟温度数据
        List<Float> temperatureData = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            temperatureData.add((float) (Math.random() * 50));
        }
        temperatureChart.setDataPoints(temperatureData);
    }
}