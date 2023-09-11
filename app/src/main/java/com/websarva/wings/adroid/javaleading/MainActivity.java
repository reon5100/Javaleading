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
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;

public class MainActivity extends AppCompatActivity {

    private ActivityResultLauncher<Intent> filePickerLauncher;
    //private TextView ResultText;
    private LineChart ChartPPGdc;
    private LineChart ChartPressure;
    private LineChart ChartPPGRaw;
    private static final int Bt_select = R.id.bt_Lead;
    private static final int Bt_Reset = R.id.bt_Reset;
    private static final int yousosuu = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ChartPPGdc = findViewById(R.id.ChartPPGdc);
        ChartPressure = findViewById(R.id.ChartPressure);
        ChartPPGRaw = findViewById(R.id.ChartPPGRaw);


        //ResultText =findViewById(R.id.tv_result);
        Button selectFileButton = findViewById(R.id.bt_Lead);
        Button resetFileButton = findViewById(R.id.bt_Reset);
        selectListener listener = new selectListener();
        selectFileButton.setOnClickListener(listener);
        resetFileButton.setOnClickListener(listener);
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*"); // すべてのファイルタイプを許可

        filePickerLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                if (result.getData() != null) {
                    Uri selectedFileUri = result.getData().getData();
                    displayTextFromFile(selectedFileUri);// 選択されたファイルのUriを取得し、処理を行う
                }
            }
        });

    }
    private class selectListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            int Id =view.getId();
            if(Id == Bt_select) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("text/plain");
                filePickerLauncher.launch(intent);
            }
            else if(Id == Bt_Reset){
                ChartPPGdc.notifyDataSetChanged();
                ChartPPGdc.invalidate();
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
            int Msec=0,Pressure=0,PPGAc=0,PPGDc=0,PPGRaw=0;
            int[] msec = new int[yousosuu];
            int[] pressure = new int[yousosuu];
            int[] PPGac = new int[yousosuu];
            int[] PPGdc= new int[yousosuu];
            int[] PPGraw= new int[yousosuu];
            for (int number :numbersArray){
                switch (count1%9){
                    case 0:
                        msec[Msec]+=(number*86400000);
                        break;
                    case 1:
                        msec[Msec]+=(number*3600000);
                        break;
                    case 2:
                        msec[Msec]+=(number*60000);
                        break;
                    case 3:
                        msec[Msec]+=(number*1000);
                        break;
                    case 4:
                        msec[Msec]+=number;
                        Msec++;
                        break;
                    case 5:
                        pressure[Pressure]+=number;
                        Pressure++;
                        break;
                    case 6:
                        PPGac[PPGAc]+=number;
                        PPGAc++;
                        break;
                    case 7:
                        PPGdc[PPGDc]+=number;
                        PPGDc++;
                        break;
                    case 8:
                        PPGraw[PPGRaw]+=number;
                        PPGRaw++;
                        break;
                    default:
                        break;
                }
                count1++;
            }

           /* StringBuilder displayText = new StringBuilder();
            //int count2 = 0;
            for (int number : PPGraw) {
                displayText.append(number);
                displayText.append("\n");
            }
            ResultText.setText(displayText.toString());*/
            ArrayList<Entry> presuureentries = new ArrayList<>();
            for (int i = 0; i < msec.length; i++) {
                presuureentries.add(new Entry(msec[i],pressure[i]));
            }
            LineDataSet pressuredataSet = new LineDataSet(presuureentries, "Pressure");
            pressuredataSet.setColor(Color.BLUE);
            pressuredataSet.setDrawCircles(false);
            pressuredataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            LineData presuurelineData = new LineData(pressuredataSet);

            ArrayList<Entry> PPGdcentries = new ArrayList<>();
            for (int i = 0; i < msec.length; i++) {
                PPGdcentries.add(new Entry(msec[i],PPGdc[i]));
            }
            LineDataSet PPGdcdataSet = new LineDataSet(PPGdcentries, "PPGdc");
            PPGdcdataSet.setColor(Color.BLUE);
            PPGdcdataSet.setDrawCircles(false);
            PPGdcdataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            LineData PPGdclineData = new LineData(PPGdcdataSet);

            ArrayList<Entry> PPGrawentries = new ArrayList<>();
            for (int i = 0; i < msec.length; i++) {
                PPGrawentries.add(new Entry(msec[i],PPGraw[i]));
            }
            LineDataSet PPGrawdataSet = new LineDataSet(PPGrawentries, "PPGraw");
            PPGrawdataSet.setColor(Color.BLUE);
            PPGrawdataSet.setDrawCircles(false);
            PPGrawdataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            LineData PPGrawlineData = new LineData(PPGrawdataSet);

// グラフにデータを設定
            ChartPressure.setData(presuurelineData);
            ChartPPGdc.setData(PPGdclineData);
            ChartPPGRaw.setData(PPGrawlineData);

            ChartPressure.setDrawMarkers(true);
            ChartPPGdc.setDrawMarkers(true);
            ChartPPGRaw.setDrawMarkers(true);
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