package com.example.shaba.clicker;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.atomic.AtomicInteger;

public class MainActivity extends AppCompatActivity {
    private Button btn;
    private Button buy;
    private TextView tv;
    private TextView textView;
    private AtomicInteger count;
    private int number;
    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn = findViewById(R.id.btn);
        buy = findViewById(R.id.buy);
        tv = findViewById(R.id.tv);
        textView = findViewById(R.id.textView);

        number = 1;
        count = new AtomicInteger(0);

        sharedPreferences = getSharedPreferences("count", Context.MODE_PRIVATE);

        if(sharedPreferences.contains("count")) {
            count.set(sharedPreferences.getInt("count",0));
        }


        startBackgroundCounter();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count.getAndAdd(number);
                tv.setText(count + " кликов!");
            }
        });
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count.get() > number * 2) {
                    count.set(count.get() - number * 2);
                    number *= 2;
                    tv.setText(count + " кликов!");
                    buy.setText("+" + number + " кликов за раз(" + number * 10 + " кликов)");
                    textView.setText("Кликов за раз: " + number);
                    Toast.makeText(MainActivity.this, "Куплено успешно", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(MainActivity.this, "нехватает кликов", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void upd() {
        tv.setText(count + " кликов!");
    }

    @Override
    protected void onStop() {
        super.onStop();
        save();
    }

    private void save(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("count",count.get());
        editor.apply();
    }
    private void startBackgroundCounter(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (; ; ) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    count.getAndIncrement();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            upd();
                        }
                    });
                }

            }
        }).start();
    }
}

