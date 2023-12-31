package com.websarva.wings.adroid.javaleading;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.formatter.ValueFormatter;


public class MainActivity extends AppCompatActivity {

    private ActivityResultLauncher<Intent> filePickerLauncher;
    private LineChart ChartPPGdc;
    private LineChart ChartPressure;
    private LineChart ChartPPGRaw;
    private Button selectFileButton;
    private Button resetFileButton;
    private Button PressureButton;
    private Button PPGdcButton;
    private Button PPGacButton;
    private EditText skipVariable;
    private static final int Bt_select = R.id.bt_open;
    private static final int Bt_Reset = R.id.bt_clear;
    private static final int Bt_Pressure = R.id.bt_Pressure;
    private static final int Bt_PPGdc = R.id.bt_PPGdc;
    private static final int Bt_PPGac = R.id.bt_PPGac;
    private static final int yousosuu = 6000;
    private final ArrayList<Entry> datanull = new ArrayList<>();
    private final LineDataSet Linedatasetnull = new LineDataSet(datanull, " ");
    private final LineData Linedata = new LineData(Linedatasetnull);
    private static int[] msec = new int[yousosuu];
    private static int[] pressure = new int[yousosuu];
    //private static int[] PPGac = new int[yousosuu];
    private static int[] PPGdc = new int[yousosuu];
    private static int[] PPGraw = new int[yousosuu];
    private static int[][] setPeakBottomData = new int[yousosuu][yousosuu];
    private static int[] peak = new int[yousosuu];
    private static int[] bottom = new int[yousosuu];
    private static float[] FIR_PPGac = new float[yousosuu];



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ChartPPGdc = findViewById(R.id.ChartPPGdc);
        ChartPressure = findViewById(R.id.SelectChart);
        ChartPPGRaw = findViewById(R.id.ChartPPGRaw);
        skipVariable = findViewById(R.id.skipVariable);
        XAxis PressurexAxis = ChartPressure.getXAxis();
        PressurexAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        PressurexAxis.setTextSize(20f);
        YAxis PressureyAxis = ChartPressure.getAxisRight();
        PressureyAxis.setEnabled(true);
        PressureyAxis.setDrawAxisLine(true);
        PressureyAxis.setAxisMaximum(240f);

        PressureyAxis.setValueFormatter(new CustomYAxisValueFormatter());
        PressureyAxis = ChartPressure.getAxisLeft();
        PressureyAxis.setTextSize(20f);
        PressureyAxis.setAxisMaximum(240f);



        XAxis PPGdcxAxis = ChartPPGdc.getXAxis();
        PPGdcxAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        PPGdcxAxis.setTextSize(20f);

        YAxis PPGdcyAxis = ChartPPGdc.getAxisRight();
        PPGdcyAxis.setEnabled(true);
        PPGdcyAxis.setDrawAxisLine(true);
        PPGdcyAxis.setAxisMaximum(4000f);
        PPGdcyAxis.setValueFormatter(new CustomYAxisValueFormatter());
        PPGdcyAxis = ChartPPGdc.getAxisLeft();
        PPGdcyAxis.setTextSize(20f);
        PPGdcyAxis.setAxisMaximum(4000f);

        XAxis PPGrawxAxis = ChartPPGRaw.getXAxis();
        PPGrawxAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        PPGrawxAxis.setTextSize(20f);
        YAxis PPGrawyAxis = ChartPPGRaw.getAxisRight();
        PPGrawyAxis.setEnabled(true);
        PPGrawyAxis.setDrawAxisLine(true);
        PPGrawyAxis.setAxisMaximum(2400f);
        PPGrawyAxis.setValueFormatter(new CustomYAxisValueFormatter());
        PPGrawyAxis = ChartPPGRaw.getAxisLeft();
        PPGrawyAxis.setTextSize(20f);
        PPGrawyAxis.setAxisMaximum(2400f);

        ChartPressure.setData(Linedata);
        ChartPPGdc.setData(Linedata);
        ChartPPGRaw.setData(Linedata);
        ChartPressure.invalidate();
        ChartPPGdc.invalidate();
        ChartPPGRaw.invalidate();

        selectFileButton = findViewById(R.id.bt_open);
        resetFileButton = findViewById(R.id.bt_clear);
        PressureButton = findViewById(R.id.bt_Pressure);
        PPGdcButton = findViewById(R.id.bt_PPGdc);
        PPGacButton = findViewById(R.id.bt_PPGac);
        PressureButton.setVisibility(View.INVISIBLE);
        PPGacButton.setVisibility(View.INVISIBLE);
        PPGdcButton.setVisibility(View.INVISIBLE);
        selectListener listener = new selectListener();
        selectFileButton.setOnClickListener(listener);
        resetFileButton.setOnClickListener(listener);
        PressureButton.setOnClickListener(listener);
        PPGdcButton.setOnClickListener(listener);
        PPGacButton.setOnClickListener(listener);
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*"); // すべてのファイルタイプを許可
        for (int i=0;i < msec.length;i++){
            if (i!=0 && msec[i]!=0){
                selectFileButton.setVisibility(View.INVISIBLE);
                break;
            }
        }
        filePickerLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                if (result.getData() != null) {
                    Uri selectedFileUri = result.getData().getData();
                    displayTextFromFile(selectedFileUri);// 選択されたファイルのUriを取得し、処理を行う
                }
            }
        });
    }
    public static class CustomYAxisValueFormatter extends ValueFormatter {
        @Override
        public String getAxisLabel(float value, AxisBase axis) {
            // ラベルを非表示にする
            return "";
        }
    }

    private class selectListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            int Id = view.getId();
            if (Id == Bt_select) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("text/plain");
                filePickerLauncher.launch(intent);
                selectFileButton.setVisibility(View.INVISIBLE);
                PressureButton.setVisibility(View.VISIBLE);
                PPGdcButton.setVisibility(View.VISIBLE);
                PPGacButton.setVisibility(View.VISIBLE);
            } else if (Id == Bt_Reset) {
                for (int i = 0; i < msec.length; i++){
                    msec[i] = 0;
                    pressure[i] = 0;
                    PPGraw[i] = 0;
                    PPGdc[i] = 0;
                    peak[i] = 0;
                    bottom[i] = 0;
                    for (int j = 0; j < 5;j++){
                        setPeakBottomData[i][j] = 0;
                    }
                }
                ChartPressure.clear();
                ChartPPGdc.clear();
                ChartPPGRaw.clear();
                ChartPressure.setData(Linedata);
                ChartPPGdc.setData(Linedata);
                ChartPPGRaw.setData(Linedata);
                ChartPressure.invalidate();
                ChartPPGdc.invalidate();
                ChartPPGRaw.invalidate();
                selectFileButton.setVisibility(View.VISIBLE);
                PressureButton.setVisibility(View.INVISIBLE);
                PPGdcButton.setVisibility(View.INVISIBLE);
                PPGacButton.setVisibility(View.INVISIBLE);
            } else if (Id == Bt_Pressure) {
                Intent intent = new Intent(MainActivity.this, PressureGraphActivity.class);
                intent.putExtra("msec", msec);
                intent.putExtra("pressure", pressure);
                startActivity(intent);
            }
            else if (Id == Bt_PPGdc) {
                Intent intent = new Intent(MainActivity.this, PPGdcGraphActivity.class);
                intent.putExtra("msec", msec);
                intent.putExtra("PPGdc", PPGdc);
                startActivity(intent);
            }
            else if (Id == Bt_PPGac) {
                Intent intent = new Intent(MainActivity.this, PPGacGraphActivity.class);
                intent.putExtra("msec", msec);
                intent.putExtra("PPGac", PPGraw);
                startActivity(intent);
            }
        }
    }

    private void displayTextFromFile(Uri fileUri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(fileUri);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append("\n");
            }

            String fileContents = stringBuilder.toString();
            String[] dataArray = fileContents.split("\n+|,");
            List<Integer> numbersList = new ArrayList<>();
            for (String data : dataArray) {
                try {
                    int number = Integer.parseInt(data.trim()); // 文字列を整数に変換
                    numbersList.add(number);
                } catch (NumberFormatException e) {
                    // 数字でないデータは無視
                }

            }

            // numbersListを配列に変換
            Integer[] numbersArray = numbersList.toArray(new Integer[0]);
            int count1 = 0;
            int Msec = 0, Pressure = 0, PPGDc = 0, PPGRaw = 0;
            for (int number : numbersArray) {
                switch (count1 % 9) {
                    case 0:
                        msec[Msec] += (number * 86400000);
                        break;
                    case 1:
                        msec[Msec] += (number * 3600000);
                        break;
                    case 2:
                        msec[Msec] += (number * 60000);
                        break;
                    case 3:
                        msec[Msec] += (number * 1000);
                        break;
                    case 4:
                        msec[Msec] += number;
                        Msec++;
                        break;
                    case 5:
                        pressure[Pressure] += number;
                        Pressure++;
                        break;
                    case 6:
                       /* PPGac[PPGAc] += number;
                        PPGAc++;*/
                        break;
                    case 7:
                        PPGdc[PPGDc] += number;
                        PPGDc++;
                        break;
                    case 8:
                        PPGraw[PPGRaw] += number;
                        PPGRaw++;
                        break;
                    default:
                        break;
                }
                count1++;
            }
            int mode =0;
            int Width = 6;
            int peakBottomCount = 0;
            int skip = 0;
            double nowPeak = 0;
            double nowBottom = 10000;
            ArrayList<Entry> peakEntry = new ArrayList<>();
            ArrayList<Entry> bottomEntry = new ArrayList<>();
            for (int i = 500; i < msec.length; i++){
                if (msec[i] == 0) {
                    break;
                }
                if (skip != 0){
                    peak[i] = 0;
                    bottom[i] = 0;
                    skip--;
                    continue;
                }
                if (pressure[i] >=30 && mode == 1 && PPGraw[i] > PPGraw[i+1] && PPGraw[i] >= nowPeak){
                    nowPeak = (double)PPGraw[i]*0.925;
                    peak[i] = PPGraw[i];
                    setPeakBottomData[peakBottomCount][0] = PPGraw[i];
                    setPeakBottomData[peakBottomCount][1] = i;
                    setPeakBottomData[peakBottomCount][2] = pressure[i];
                    mode = 0;
                    String skipVariableText = skipVariable.getText().toString();
                    if(!skipVariableText.isEmpty()){
                        try {
                            skip =Integer.parseInt(skipVariableText);
                        }catch (NumberFormatException e){
                            e.printStackTrace();
                            showErrorMessage("エラー: " + e.getMessage());
                        }
                    }
                    }
                else {
                    peak[i] = 0;
                }
                if(mode == 0 && PPGraw[i] < PPGraw[i+1]) {
                    if (PPGraw[i] <= nowBottom) {
                        nowBottom = (double) PPGraw[i] * 1.125;
                        if ((pressure[i] >= 30 && pressure[i] <60)){
                            bottom[i] = PPGraw[i];
                            setPeakBottomData[peakBottomCount][3] = PPGraw[i];
                            setPeakBottomData[peakBottomCount][4] = i;
                            mode = 1;
                        }
                        if (pressure[i] >= 60  && (PPGraw[i + 1] - PPGraw[i]) >= Width) {
                            bottom[i] = PPGraw[i];
                            setPeakBottomData[peakBottomCount][3] = PPGraw[i];
                            setPeakBottomData[peakBottomCount][4] = i;
                            mode = 1;
                        }
                    }
                }
                peakEntry.add(new Entry(msec[i], peak[i]));
                bottomEntry.add(new Entry(msec[i],bottom[i]));
                peakBottomCount++;
            }
            LineDataSet peakdataSet = new LineDataSet(peakEntry,"peak");
            LineDataSet bottomdataSet = new LineDataSet(bottomEntry,"bottom");

            peakdataSet.setCircleColor(Color.RED);
            bottomdataSet.setCircleColor(Color.GREEN);


            ArrayList<Entry> presuureentries = new ArrayList<>();
            for (int i = 0; i < msec.length; i++) {
                if (i != 0 && msec[i] == 0) {
                    break;
                }
                presuureentries.add(new Entry(msec[i], pressure[i]));
            }
            LineDataSet pressuredataSet = new LineDataSet(presuureentries, "Pressure");
            //LineDataSet pressuredataSet = new LineDataSet(peakEntry, "Pressure");
            pressuredataSet.setColor(Color.BLUE);
            pressuredataSet.setDrawCircles(false);
            //pressuredataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            LineData presuurelineData = new LineData(pressuredataSet);

            ArrayList<Entry> PPGdcentries = new ArrayList<>();
            for (int i = 0; i < msec.length; i++) {
                if (i != 0 && msec[i] == 0) {
                    break;
                }
                PPGdcentries.add(new Entry(msec[i], PPGdc[i]));
            }
            LineDataSet PPGdcdataSet = new LineDataSet(PPGdcentries, "PPGdc");
            PPGdcdataSet.setColor(Color.BLUE);
            PPGdcdataSet.setDrawCircles(false);
            //PPGdcdataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            LineData PPGdclineData = new LineData(PPGdcdataSet);

            ArrayList<Entry> PPGrawentries = new ArrayList<>();
            for (int i = 0; i < msec.length; i++) {
                if (i != 0 && msec[i] == 0) {
                    break;
                }
                PPGrawentries.add(new Entry(msec[i], PPGraw[i]));
            }
            LineDataSet PPGrawdataSet = new LineDataSet(PPGrawentries, "PPGac");
            PPGrawdataSet.setColor(Color.BLUE);
            PPGrawdataSet.setDrawCircles(false);
            //PPGrawdataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            LineData PPGrawlineData = new LineData(PPGrawdataSet,peakdataSet,bottomdataSet);

// グラフにデータを設定
            Legend Pressurelegend = ChartPressure.getLegend();
            Pressurelegend.setTextSize(20f); // テキストサイズを設定
            Pressurelegend.setFormSize(10f); // 凡例エントリのサイズを設定

            Legend PPGdclegend = ChartPPGdc.getLegend();
            PPGdclegend.setTextSize(20f); // テキストサイズを設定
            PPGdclegend.setFormSize(10f); // 凡例エントリのサイズを設定

            Legend PPGaclegend = ChartPPGRaw.getLegend();
            PPGaclegend.setTextSize(20f); // テキストサイズを設定
            PPGaclegend.setFormSize(10f); // 凡例エントリのサイズを設定

            ChartPressure.setData(presuurelineData);
            ChartPPGdc.setData(PPGdclineData);
            ChartPPGRaw.setData(PPGrawlineData);


// グラフの更新
            ChartPressure.invalidate();
            ChartPPGdc.invalidate();
            ChartPPGRaw.invalidate();


        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            // その他の例外処理
            ex.printStackTrace();
            showErrorMessage("エラー: " + ex.getMessage());
        }
    }

    private void showErrorMessage(String message) {
        // エラーメッセージをユーザーに表示する方法を実装してください。
        // 例えば、ダイアログボックス、Toastメッセージ、またはTextViewにエラーメッセージを表示できます。
        // 以下は、Toastメッセージを表示する例です。
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


}
