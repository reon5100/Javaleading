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

public class PPGacGraphActivity extends AppCompatActivity {
    private LineChart SelectPPGacGraph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ppgac_graph2);
        SelectPPGacGraph = findViewById(R.id.SelectPPGacChart);
        int[] msec = getIntent().getIntArrayExtra("msec");
        int[] PPGac = getIntent().getIntArrayExtra("PPGac");
        if (msec != null) {
            setupChart(msec, PPGac);
        } else {
            showToast("sec が null です。");
            // sec が null の場合の処理
            // 例: エラーメッセージを表示するなど
        }
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
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}