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

public class PPGdcGraphActivity extends AppCompatActivity {
    private LineChart SelectPPGdcGraph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ppgac_graph);
        SelectPPGdcGraph = findViewById(R.id.SelectPPGdcChart);

        XAxis PPGdcxAxis = SelectPPGdcGraph.getXAxis();
        PPGdcxAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        PPGdcxAxis.setTextSize(20f);

        YAxis PPGdcyAxis = SelectPPGdcGraph.getAxisRight();
        PPGdcyAxis.setEnabled(true);
        PPGdcyAxis.setDrawAxisLine(true);
        PPGdcyAxis.setAxisMaximum(4000f);
        PPGdcyAxis.setAxisMinimum(0f);
        PPGdcyAxis.setValueFormatter(new PPGdcGraphActivity.CustomYAxisValueFormatter());

        PPGdcyAxis = SelectPPGdcGraph.getAxisLeft();
        PPGdcyAxis.setTextSize(20f);
        PPGdcyAxis.setAxisMaximum(4000f);
        PPGdcyAxis.setAxisMinimum(0f);

        Legend PPGdclegend = SelectPPGdcGraph.getLegend();
        PPGdclegend.setTextSize(20f); // テキストサイズを設定
        PPGdclegend.setFormSize(10f); // 凡例エントリのサイズを設定

        float[] msec = getIntent().getFloatArrayExtra("msec");
        float[] PPGdc = getIntent().getFloatArrayExtra("PPGdc");

        setupChart(msec, PPGdc);

    }

    private void setupChart(float[] msec, float[] PPGdc) {
        ArrayList<Entry> PPGdcentries = new ArrayList<>();
        for (int i = 0; i < msec.length; i++) {
            if (i != 0 && msec[i] == 0) {
                break;
            }
            PPGdcentries.add(new Entry(msec[i], PPGdc[i]));
        }
        LineDataSet PPGdcdataSet = new LineDataSet(PPGdcentries, "Pressure");
        PPGdcdataSet.setColor(Color.BLUE);
        PPGdcdataSet.setDrawCircles(false);
        LineData presuurelineData = new LineData(PPGdcdataSet);

        SelectPPGdcGraph.setData(presuurelineData);
        SelectPPGdcGraph.invalidate();
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