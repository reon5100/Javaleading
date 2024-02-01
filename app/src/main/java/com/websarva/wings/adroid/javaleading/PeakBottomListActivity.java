package com.websarva.wings.adroid.javaleading;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.TextView;
import android.os.Bundle;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.ValueFormatter;


public class PeakBottomListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peak_bottom_list);
        float[] peak = getIntent().getFloatArrayExtra("peak");
        float[] peakTime = getIntent().getFloatArrayExtra("peakTime");
        float[] peakPressure = getIntent().getFloatArrayExtra("peakPressure");
        float[] bottom = getIntent().getFloatArrayExtra("bottom");
        float[] bottomTime = getIntent().getFloatArrayExtra("bottomTime");
        float[][] setPeakBottomData = new float[60][5];
        for (int i = 0; i < 60; i++){
            for (int j = 0 ; j < 5; j++){
                switch (j){
                    case 0:
                        setPeakBottomData[i][j]=peak[i];
                        break;
                    case 1:
                        setPeakBottomData[i][j]=peakTime[i];
                        break;
                    case 2:
                        setPeakBottomData[i][j]=peakPressure[i];
                        break;
                    case 3:
                        setPeakBottomData[i][j]=bottom[i];
                        break;
                    case 4:
                        setPeakBottomData[i][j]=bottomTime[i];
                        break;
                }
            }
        }
    String setPeakBottomDataString =convertArrayToString(setPeakBottomData);
        TextView tv_PeakBottomList = findViewById(R.id.tv_PeakBottomList);
        tv_PeakBottomList.setText(setPeakBottomDataString);
    }
    private String convertArrayToString(float[][] array) {
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                stringBuilder.append(array[i][j]);

                // 行の最後でない場合はカンマで区切る
                if (j < array[i].length - 1) {
                    stringBuilder.append("\t\t\t\t");
                }
            }

            // 行の最後でない場合は改行する
            if (i < array.length - 1) {
                stringBuilder.append("\n");
            }
        }

        return stringBuilder.toString();
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

