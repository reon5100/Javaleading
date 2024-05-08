package com.websarva.wings.adroid.javaleading;

import static android.os.Environment.getExternalStoragePublicDirectory;
import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
    private Button PeakBottomButton;
    private Button DerivativeButton;
    private Button SecondDerivativeButton;
    private Button SaveButton;
    private EditText skipVariable;
    private static final int Bt_select = R.id.bt_open;
    private static final int Bt_Reset = R.id.bt_clear;
    private static final int Bt_Pressure = R.id.bt_Pressure;
    private static final int Bt_PPGdc = R.id.bt_PPGdc;
    private static final int Bt_PPGac = R.id.bt_PPGac;
    private static final int Bt_Derivative = R.id.bt_Derivative;
    private static final int Bt_SecondDerivative = R.id.bt_SecondDerivative;
    private static final int Bt_PeakBottom = R.id.bt_PeakBottom;
    private static final int Bt_save = R.id.bt_save;
    private static final int ET_SkipTextView = R.id.skipVariable;
    private static final int yousosuu = 6000;
    private final ArrayList<Entry> datanull = new ArrayList<>();
    private final LineDataSet Linedatasetnull = new LineDataSet(datanull, " ");
    private final LineData Linedata = new LineData(Linedatasetnull);
    private static double[] msec = new double[yousosuu];
    private static double[] pressure = new double[yousosuu];
    //private static int[] PPGac = new int[yousosuu];
    private static double[] PPGdc = new double[yousosuu];
    private static double[] PPGraw = new double[yousosuu];
    private static double[][] setPeakBottomData = new double[yousosuu][yousosuu];
    private static double[] peak = new double[yousosuu];
    private static double[] bottom = new double[yousosuu];
    private static double[] PPGacDerivative = new double[yousosuu];
    private static double[] PPGacSecondDerivative = new double[yousosuu];
    private static double[] SDPPGacA = new double[yousosuu];
    private static double[] SDPPGacB = new double[yousosuu];

    private static double[] SDPPGacC = new double[yousosuu];

    private static double[] SDPPGacD = new double[yousosuu];

    private static int skip;


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
        //PPGrawyAxis.setAxisMaximum(2400f);
        PPGrawyAxis.setValueFormatter(new CustomYAxisValueFormatter());
        PPGrawyAxis = ChartPPGRaw.getAxisLeft();
        PPGrawyAxis.setTextSize(20f);
        //PPGrawyAxis.setAxisMaximum(2400f);

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
        DerivativeButton = findViewById(R.id.bt_Derivative);
        SecondDerivativeButton = findViewById(R.id.bt_SecondDerivative);
        PeakBottomButton = findViewById(R.id.bt_PeakBottom);
        SaveButton = findViewById(R.id.bt_save);

        PressureButton.setVisibility(View.INVISIBLE);
        PPGacButton.setVisibility(View.INVISIBLE);
        PPGdcButton.setVisibility(View.INVISIBLE);
        selectListener listener = new selectListener();
        selectFileButton.setOnClickListener(listener);
        resetFileButton.setOnClickListener(listener);
        PressureButton.setOnClickListener(listener);
        PPGdcButton.setOnClickListener(listener);
        PPGacButton.setOnClickListener(listener);
        DerivativeButton.setOnClickListener(listener);
        SecondDerivativeButton.setOnClickListener(listener);
        PeakBottomButton.setOnClickListener(listener);
        skipVariable.setOnClickListener(listener);
        SaveButton.setOnClickListener(listener);

        String skipVariableText = skipVariable.getText().toString();
        if (!skipVariableText.isEmpty()) {
            try {
                skip = Integer.parseInt(skipVariableText);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                showErrorMessage("エラー: " + e.getMessage());
            }
        }
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*"); // すべてのファイルタイプを許可
        for (int i = 0; i < msec.length; i++) {
            if (i != 0 && msec[i] != 0) {
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

        @RequiresApi(api = Build.VERSION_CODES.O)
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
                for (int i = 0; i < msec.length; i++) {
                    msec[i] = 0;
                    pressure[i] = 0;
                    PPGraw[i] = 0;
                    PPGdc[i] = 0;
                    peak[i] = 0;
                    bottom[i] = 0;
                    PPGacDerivative[i] = 0;
                    PPGacSecondDerivative[i] = 0;
                    SDPPGacA[i] = 0;
                    SDPPGacB[i] = 0;
                    SDPPGacC[i] = 0;
                    SDPPGacD[i] = 0;
                    for (int j = 0; j < 5; j++) {
                        setPeakBottomData[i][j] = 0;
                    }
                }
                ChartPressure.clear();ChartPPGdc.clear();ChartPPGRaw.clear();ChartPressure.setData(Linedata);ChartPPGdc.setData(Linedata);ChartPPGRaw.setData(Linedata);
                ChartPressure.invalidate();ChartPPGdc.invalidate();ChartPPGRaw.invalidate();selectFileButton.setVisibility(View.VISIBLE);PressureButton.setVisibility(View.INVISIBLE);PPGdcButton.setVisibility(View.INVISIBLE);PPGacButton.setVisibility(View.INVISIBLE);
            } else if (Id == Bt_Pressure) {
                Intent intent = new Intent(MainActivity.this, PressureGraphActivity.class);
                intent.putExtra("msec", msec);
                intent.putExtra("pressure", pressure);
                startActivity(intent);
            } else if (Id == Bt_PPGdc) {
                Intent intent = new Intent(MainActivity.this, PPGdcGraphActivity.class);
                intent.putExtra("msec", msec);
                intent.putExtra("PPGdc", PPGdc);
                startActivity(intent);
            } else if (Id == Bt_PPGac) {
                Intent intent = new Intent(MainActivity.this, PPGacGraphActivity.class);
                intent.putExtra("msec", msec);
                intent.putExtra("PPGac", PPGraw);
                startActivity(intent);
            } else if (Id == Bt_Derivative) {
                ArrayList<Entry> PPGacDerivativeEntries = new ArrayList<>();
                for (int i = 0; i < msec.length; i++) {
                    if (i != 0 && msec[i] == 0) {
                        break;
                    }
                    PPGacDerivativeEntries.add(new Entry((float) msec[i], (float)PPGacDerivative[i]));
                }
                LineDataSet PPGacDerivativeDataSet = new LineDataSet(PPGacDerivativeEntries, "DPPGac");
                PPGacDerivativeDataSet.setColor(Color.BLUE);
                PPGacDerivativeDataSet.setDrawCircles(false);
                LineData PPGacDerivativeData = new LineData(PPGacDerivativeDataSet);
                ChartPPGRaw.setData(PPGacDerivativeData);
                ChartPPGRaw.invalidate();
            }else if (Id == Bt_SecondDerivative){
                ArrayList<Entry> PPGacSecondDerivativeEntries = new ArrayList<>();
                ArrayList<Entry> sdPPGac_A_Entries = new ArrayList<>();
                ArrayList<Entry> sdPPGac_B_Entries = new ArrayList<>();
                ArrayList<Entry> sdPPGac_C_Entries = new ArrayList<>();
                ArrayList<Entry> sdPPGac_D_Entries = new ArrayList<>();
                for (int i = 0; i < msec.length; i++) {
                    if (msec[i] == 0) {
                        break;
                    }
                    PPGacSecondDerivativeEntries.add(new Entry((float)msec[i], (float)PPGacSecondDerivative[i]));

                    if (SDPPGacA[i] != 0)
                        sdPPGac_A_Entries.add(new Entry((float)msec[i], (float)SDPPGacA[i]));
                    if (SDPPGacB[i] != 0)
                        sdPPGac_B_Entries.add(new Entry((float)msec[i], (float)SDPPGacB[i]));
                    if (SDPPGacC[i] != 0)
                        sdPPGac_C_Entries.add(new Entry((float)msec[i], (float)SDPPGacC[i]));
                    if (SDPPGacD[i] != 0)
                        sdPPGac_D_Entries.add(new Entry((float)msec[i], (float)SDPPGacD[i]));

                }

                LineDataSet PPGacSecondDerivativeDataSet = new LineDataSet(PPGacSecondDerivativeEntries, "SDPPGac");
                PPGacSecondDerivativeDataSet.setColor(Color.BLUE);
                PPGacSecondDerivativeDataSet.setDrawCircles(false);


                LineDataSet sdPPGac_A_DataSet =new LineDataSet(sdPPGac_A_Entries,"");
                sdPPGac_A_DataSet.setColor(Color.TRANSPARENT);
                sdPPGac_A_DataSet.setCircleColor(Color.RED);

                LineDataSet sdPPGac_B_DataSet =new LineDataSet(sdPPGac_B_Entries,"");
                sdPPGac_B_DataSet.setColor(Color.TRANSPARENT);
                sdPPGac_B_DataSet.setCircleColor(Color.BLUE);

                LineDataSet sdPPGac_C_DataSet =new LineDataSet(sdPPGac_C_Entries,"");
                sdPPGac_C_DataSet.setColor(Color.TRANSPARENT);
                sdPPGac_C_DataSet.setCircleColor(Color.GREEN);

                LineDataSet sdPPGac_D_DataSet =new LineDataSet(sdPPGac_D_Entries,"");
                sdPPGac_D_DataSet.setColor(Color.TRANSPARENT);
                sdPPGac_D_DataSet.setCircleColor(Color.MAGENTA);

                LineData PPGacSecondDerivativeData = new LineData(PPGacSecondDerivativeDataSet,sdPPGac_A_DataSet,sdPPGac_B_DataSet,sdPPGac_C_DataSet,sdPPGac_D_DataSet);

                ChartPPGRaw.setData(PPGacSecondDerivativeData);
                ChartPPGRaw.invalidate();
            }else if (Id == ET_SkipTextView) {
                int PreviousSkip = skip;
                String skipVariableText = skipVariable.getText().toString();
                if (!skipVariableText.isEmpty()) {
                    try {
                        skip = Integer.parseInt(skipVariableText);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        showErrorMessage("エラー: " + e.getMessage());
                    }
                }
                if (PreviousSkip != skip) {
                    for (int i = 0; i < msec.length; i++) {
                        peak[i] = 0;
                        bottom[i] = 0;
                        for (int j = 0; j < 5; j++) {
                            setPeakBottomData[i][j] = 0;
                        }
                    }
                    showGraph();
                }
            else if (Id == Bt_save){
/// 保存ファイル名
                    String fileName = "hogehoge.txt";

/// ファイル保存のためのピッカーUI起動
                    Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType("*/*");
                    intent.putExtra(Intent.EXTRA_TITLE, fileName);
                    //String line[] = new String[yousosuu];
                    /*LocalDate currentDate = LocalDate.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("/yyyy-MM-dd.csv");
                    String formattedDate = currentDate.format(formatter);

                    try  {
                        FileWriter writer = new FileWriter(getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getPath()+"test.txt",false);
                        PrintWriter printWriter = new PrintWriter(new BufferedWriter(writer));
                            for (int i = 0;i<PPGacSecondDerivative.length;i++) {
                                line[i] = String.format("%s,%s", msec[i], PPGacSecondDerivative[i]);
                                printWriter.println(line[i]);
                        }
                        printWriter.close();
                        Log.i(TAG, "CSVファイルが保存されました");
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e(TAG, "ファイルの保存中にエラーが発生しました");
                    } */
                }
            } else if (Id == Bt_PeakBottom) {
                Intent intent = new Intent(MainActivity.this, PeakBottomListActivity.class);
                double[] peakData = new double[yousosuu];
                double[] peakPressureData = new double[yousosuu];
                double[] peakTimeData = new double[yousosuu];
                double[] bottomData = new double[yousosuu];
                double[] bottomTimeData = new double[yousosuu];
                for (int i = 0; i < setPeakBottomData.length; i++) {
                    for (int j = 0; j < 5; j++) {
                        switch (j) {
                            case 0:
                                peakData[i] = setPeakBottomData[i][j];
                                break;
                            case 1:
                                peakTimeData[i] = setPeakBottomData[i][j];
                                break;
                            case 2:
                                peakPressureData[i] = setPeakBottomData[i][j];
                                break;
                            case 3:
                                bottomData[i] = setPeakBottomData[i][j];
                                break;
                            case 4:
                                bottomTimeData[i] = setPeakBottomData[i][j];
                                break;
                        }
                    }
                }
                intent.putExtra("peak", peakData);
                intent.putExtra("peakTime", peakTimeData);
                intent.putExtra("peakPressure", peakPressureData);
                intent.putExtra("bottom", bottomData);
                intent.putExtra("bottomTime", bottomTimeData);
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
            showGraph();
            PPGraw = FIRPPGacFilter(PPGraw);
            PPGacDerivative = FIRFilter(Derivative(PPGraw));
            PPGacSecondDerivative = FIRFilter(Derivative(PPGacDerivative));
            //SDPPGacDetection(PPGacSecondDerivative);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            // その他の例外処理
            ex.printStackTrace();
            showErrorMessage("エラー: " + ex.getMessage());
        }

    }

    private void showGraph() {
        int mode = 1;
        int Width = 6;
        int peakBottomCount = 0;
        float nowPeak = 0;
        float nowBottom = 10000;
        skip =0;
        ArrayList<Entry> peakEntry = new ArrayList<>();
        ArrayList<Entry> bottomEntry = new ArrayList<>();
        for (int i = 600; i < msec.length; i++) {
            if (msec[i] == 0.0) {
                break;
            }
            if (skip != 0) {
                skip--;
                continue;
            }
            if (pressure[i] >= 30.0 && mode == 1 && PPGraw[i] > PPGraw[i + 1] && PPGraw[i] >= nowPeak) {
                nowPeak = (float) (PPGraw[i] * 0.75);
                peak[i] = PPGraw[i];
                setPeakBottomData[peakBottomCount][0] = PPGraw[i];
                setPeakBottomData[peakBottomCount][1] = msec[i];
                setPeakBottomData[peakBottomCount][2] = pressure[i];
                mode = 0;
                String skipVariableText = skipVariable.getText().toString();
                if (!skipVariableText.isEmpty()) {
                    try {
                        skip = Integer.parseInt(skipVariableText);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        showErrorMessage("エラー: " + e.getMessage());
                    }
                }
            }
            if (mode == 0 && PPGraw[i] < PPGraw[i + 1]) {
                if (PPGraw[i] <= nowBottom) {
                    nowBottom = (float) (PPGraw[i] * 1.25);
                    if ((pressure[i] >= 30 && pressure[i] < 60)) {
                        bottom[i] = PPGraw[i];
                        setPeakBottomData[peakBottomCount][3] = PPGraw[i];
                        setPeakBottomData[peakBottomCount][4] = msec[i];
                        mode = 1;
                        peakBottomCount++;
                    }
                    if (pressure[i] >= 60.0 && (PPGraw[i + 1] - PPGraw[i]) >= Width) {
                        bottom[i] = PPGraw[i];
                        setPeakBottomData[peakBottomCount][3] = PPGraw[i];
                        setPeakBottomData[peakBottomCount][4] = msec[i];
                        mode = 1;
                        peakBottomCount++;
                    }
                }
            }
            if (peak[i] != 0)
                peakEntry.add(new Entry((float)msec[i], (float)peak[i]));
            if (bottom[i] != 0)
                bottomEntry.add(new Entry((float)msec[i], (float)bottom[i]));

        }
        LineDataSet peakdataSet = new LineDataSet(peakEntry,"");
        LineDataSet bottomdataSet = new LineDataSet(bottomEntry, "");

        peakdataSet.setCircleColor(Color.RED);
        peakdataSet.setColor(Color.TRANSPARENT);
        bottomdataSet.setCircleColor(Color.GREEN);
        bottomdataSet.setColor(Color.TRANSPARENT);


        ArrayList<Entry> presuureentries = new ArrayList<>();
        for (int i = 0; i < msec.length; i++) {
            if (i != 0 && msec[i] == 0) {
                break;
            }
            presuureentries.add(new Entry((float)msec[i], (float)pressure[i]));
        }
        LineDataSet pressuredataSet = new LineDataSet(presuureentries, "Pressure");
        pressuredataSet.setColor(Color.BLUE);
        pressuredataSet.setDrawCircles(false);
        LineData presuurelineData = new LineData(pressuredataSet);

        ArrayList<Entry> PPGdcentries = new ArrayList<>();
        for (int i = 0; i < msec.length; i++) {
            if (i != 0 && msec[i] == 0) {
                break;
            }
            PPGdcentries.add(new Entry((float)msec[i], (float)PPGdc[i]));
        }
        LineDataSet PPGdcdataSet = new LineDataSet(PPGdcentries, "PPGdc");
        PPGdcdataSet.setColor(Color.BLUE);
        PPGdcdataSet.setDrawCircles(false);
        LineData PPGdclineData = new LineData(PPGdcdataSet);
        ArrayList<Entry> PPGrawentries = new ArrayList<>();
        for (int i = 0; i < msec.length; i++) {
            if (i != 0 && msec[i] == 0) {
                break;
            }
            PPGrawentries.add(new Entry((float)msec[i], (float)PPGraw[i]));
        }
        LineDataSet PPGrawdataSet = new LineDataSet(PPGrawentries, "PPGac");
        PPGrawdataSet.setColor(Color.BLUE);
        PPGrawdataSet.setDrawCircles(false);
        //PPGrawdataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        LineData PPGrawlineData = new LineData(PPGrawdataSet, peakdataSet, bottomdataSet);

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
    }

    private void showErrorMessage(String message) {
        // エラーメッセージをユーザーに表示する方法を実装してください。
        // 例えば、ダイアログボックス、Toastメッセージ、またはTextViewにエラーメッセージを表示できます。
        // 以下は、Toastメッセージを表示する例です。
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public double[] Derivative(double[] PPGacData) {
        double[] derivativeArray = new double[yousosuu];
        for (int i = 1; i < msec.length -1; i++) {// 中心差分を使用
            derivativeArray[i] = (float) ((PPGacData[i + 1] - PPGacData[i - 1]) / ((msec[i + 1] - msec[i - 1]) * 0.001));
        }
        return derivativeArray;
    }
    public double[] FIRPPGacFilter(double[] PPGacData){
        double samplingFrequency = 100;
        double cutoffFrequency = 15;
        double omega = (2 * Math.PI * cutoffFrequency/samplingFrequency);
        double alpha =  Math.sin(omega)/(2);
        double a0 = alpha + 1;
        double a1 =  (2 * Math.cos(omega));
        double a2 = 1 - alpha;
        double b0 =  ((1-Math.cos(omega))/2);
        double b1 =  (1 - Math.cos(omega));
        double b2 =  (1 - Math.cos(omega));
        double[] previousOutput = {0, 0};
        double[] previousInput =  {0, 0};
        double[] FIRFilterArray = new double[yousosuu];
        for (int i = 0;i < msec.length;i++){
            FIRFilterArray[i] = (b0/a0*PPGacData[i])+(b1/a0*previousInput[0])+(b2/a0*previousInput[1])-(a1/a0*previousOutput[0])-(a2/a0*previousOutput[1]);
            if(i==0){
                previousInput[0] = PPGacData[i];
                previousOutput[0] = FIRFilterArray[i];
            }
            if (i>1) {
                previousInput[1] = previousInput[0];
                previousInput[0] = PPGacData[i];
                previousOutput[1] = previousOutput[0];
                previousOutput[0] = FIRFilterArray[i];
            }
        }
        return FIRFilterArray;
    }
    public double[] FIRFilter(double[] PPGacData){
        double samplingFrequency = 100;
        double cutoffFrequency = 12;
        double omega = (2 * Math.PI * cutoffFrequency/samplingFrequency);
        double alpha =  Math.sin(omega)/(2);
        double a0 = alpha + 1;
        double a1 =  (2 * Math.cos(omega));
        double a2 = 1 - alpha;
        double b0 =  ((1-Math.cos(omega))/2);
        double b1 =  (1 - Math.cos(omega));
        double b2 =  (1 - Math.cos(omega));
        double[] previousOutput = {0, 0};
        double[] previousInput =  {0, 0};
        double[] FIRFilterArray = new double[yousosuu];
        for (int i = 0;i < msec.length;i++){
            FIRFilterArray[i] = (b0/a0*PPGacData[i])+(b1/a0*previousInput[0])+(b2/a0*previousInput[1])-(a1/a0*previousOutput[0])-(a2/a0*previousOutput[1]);
                previousInput[1] = previousInput[0];
                previousInput[0] = PPGacData[i];
                previousOutput[1] = previousOutput[0];
                previousOutput[0] = FIRFilterArray[i];
        }
        return FIRFilterArray;
    }
    public void SDPPGacDetection(float[] SDPPGac){
        int mode=1;
        float now_A=5000,now_B=0,now_C=200,now_D=0;
        for (int i=1000;i<=msec.length;i++){
            if (msec[i]==0){
                break;
            }
                switch (mode) {
                    case (1):
                        if (SDPPGac[i] > SDPPGac[i+1]  && SDPPGac[i] >=now_A) {
                            now_A =  (float)(SDPPGac[i]*0.7);
                            SDPPGacA[i] = SDPPGac[i];
                            mode = 2;
                        }
                        break;
                    case (2):
                        if (SDPPGac[i] < SDPPGac[i+1] && SDPPGac[i] <= now_B) {
                            now_B = (float) (SDPPGac[i]*0.8);
                            SDPPGacB[i] = SDPPGac[i];
                            mode = 3;
                        }
                        break;
                    case (3):
                        if (SDPPGac[i] > SDPPGac[i+1] &&  SDPPGac[i] >= now_D && SDPPGac[i] < now_A) {
                            now_C = (float)(SDPPGac[i]*0.8);
                            SDPPGacC[i] = SDPPGac[i];
                            mode = 4;
                        }
                        break;
                    case (4):
                        if (SDPPGac[i] < SDPPGac[i+1] &&  SDPPGac[i] <= now_C && SDPPGac[i] > now_B) {
                            now_D = (float)(SDPPGac[i]*0.8);
                            SDPPGacD[i] = SDPPGac[i];
                            mode = 1;
                        }
                        break;
                }
        }
    }
}
