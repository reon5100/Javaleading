package com.websarva.wings.adroid.javaleading;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

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
        YAxis PressureyAxis = SelectPressureGraph.getAxisRight();
        PressureyAxis.setEnabled(false);
        PressureyAxis = SelectPressureGraph.getAxisLeft();
        PressureyAxis.setAxisMaximum(240f);
        int[] sec = getIntent().getIntArrayExtra("msec");
        int[] pressures = getIntent().getIntArrayExtra("pressure");

            setupChart(sec, pressures);
    }

    private void setupChart(int[] sec, int[] pressure){
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
    public void onBackButtonClick(View view){
        finish();
    }

}