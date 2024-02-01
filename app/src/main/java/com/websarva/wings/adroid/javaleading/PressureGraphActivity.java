package com.websarva.wings.adroid.javaleading;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;

public class PressureGraphActivity extends AppCompatActivity {
    private LineChart SelectPressureGraph;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        SelectPressureGraph = findViewById(R.id.SelectPressureChart);

        XAxis PressurexAxis = SelectPressureGraph.getXAxis();
        PressurexAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        PressurexAxis.setTextSize(20f);

        YAxis PressureyAxis = SelectPressureGraph.getAxisRight();
        PressureyAxis.setEnabled(true);
        PressureyAxis.setDrawAxisLine(true);
        PressureyAxis.setAxisMaximum(240f);
        PressureyAxis.setAxisMinimum(0f);
        PressureyAxis.setValueFormatter(new CustomYAxisValueFormatter());

        PressureyAxis = SelectPressureGraph.getAxisLeft();
        PressureyAxis.setAxisMaximum(240f);
        PressureyAxis.setTextSize(20f);
        PressureyAxis.setAxisMinimum(0f);

        Legend Pressurelegend = SelectPressureGraph.getLegend();
        Pressurelegend.setTextSize(20f); // テキストサイズを設定
        Pressurelegend.setFormSize(10f); // 凡例エントリのサイズを設定

        float[] sec = getIntent().getFloatArrayExtra("msec");
        float[] pressures = getIntent().getFloatArrayExtra("pressure");

            setupChart(sec, pressures);
    }

    private void setupChart(float[] sec, float[] pressure){
        ArrayList<Entry> presuureentries = new ArrayList<>();
        for (int i = 0; i < sec.length; i++) {
            if (i != 0 && sec[i] == 0) {
                break;
            }
            presuureentries.add(new Entry(sec[i], pressure[i]));
        }
        LineDataSet pressuredataSet = new LineDataSet(presuureentries, "Pressure");
        pressuredataSet.setColor(Color.BLUE);
        pressuredataSet.setDrawCircles(false);
        LineData presuurelineData = new LineData(pressuredataSet);

        SelectPressureGraph.setData(presuurelineData);
        SelectPressureGraph.invalidate();

    }
    public class CustomYAxisValueFormatter extends ValueFormatter {
        @Override
        public String getAxisLabel(float value, AxisBase axis) {
            // ラベルを非表示にする
            return "";
        }
    }
    public void onBackButtonClick(View view){
        finish();
    }

}