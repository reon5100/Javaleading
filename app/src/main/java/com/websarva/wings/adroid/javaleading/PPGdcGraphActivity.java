package com.websarva.wings.adroid.javaleading;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;

public class PPGdcGraphActivity extends AppCompatActivity {
    private LineChart SelectPPGdcGraph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ppgac_graph);
        SelectPPGdcGraph = findViewById(R.id.SelectPPGdcChart);
        int[] msec = getIntent().getIntArrayExtra("msec");
        int[] PPGdc = getIntent().getIntArrayExtra("PPGdc");
        if (msec != null) {
            setupChart(msec, PPGdc);
        } else {
            showToast("sec が null です。");
            // sec が null の場合の処理
            // 例: エラーメッセージを表示するなど
        }
    }

    private void setupChart(int[] msec, int[] PPGdc) {
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
    public void onBackButtonClick(View view){
        finish();
    }
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}