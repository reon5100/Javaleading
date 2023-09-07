package com.websarva.wings.adroid.javaleading;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    private static final int YOUR_REQUEST_CODE = 123;
    private ActivityResultLauncher<Intent> filePickerLauncher;
    private TextView ResultText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ResultText =findViewById(R.id.tv_result);
        Button selectFileButton = findViewById(R.id.bt_Lead);
        selectListener listener = new selectListener();
        selectFileButton.setOnClickListener(listener);
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
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("*/*");

            filePickerLauncher.launch(intent);
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
            inputStream.close();
            String fileContents = stringBuilder.toString();
            ResultText.setText(fileContents);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}