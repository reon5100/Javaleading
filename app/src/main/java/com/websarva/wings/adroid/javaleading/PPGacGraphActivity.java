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

public class PPGacGraphActivity extends AppCompatActivity {
    private LineChart SelectPPGacGraph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ppgac_graph2);
        SelectPPGacGraph = findViewById(R.id.SelectPPGacChart);
        XAxis PPGacxAxis = SelectPPGacGraph.getXAxis();
        PPGacxAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        YAxis PPGacyAxis = SelectPPGacGraph.getAxisRight();
        PPGacyAxis.setEnabled(false);
        PPGacyAxis = SelectPPGacGraph.getAxisLeft();
        PPGacyAxis.setAxisMaximum(2400f);
        int[] msec = getIntent().getIntArrayExtra("msec");
        int[] PPGac = getIntent().getIntArrayExtra("PPGac");

        setupChart(msec, PPGac);

    }

    private void setupChart(int[] msec, int[] PPGac) {
        ArrayList<Entry> PPGacentries = new ArrayList<>();
        for (int i = 0; i < msec.length; i++) {
            if (i != 0 && msec[i] == 0) {
                break;
            }
            PPGacentries.add(new Entry(msec[i], PPGac[i]));
        }
        LineDataSet PPGacdataSet = new LineDataSet(PPGacentries, "Pressure");
        PPGacdataSet.setColor(Color.BLUE);
        PPGacdataSet.setDrawCircles(false);
        LineData presuurelineData = new LineData(PPGacdataSet);

        SelectPPGacGraph.setData(presuurelineData);
        SelectPPGacGraph.invalidate();
    }
    public void onBackButtonClick(View view){
        finish();
    }

}