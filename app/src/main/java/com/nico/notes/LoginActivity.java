package com.nico.notes;

import androidx.appcompat.app.AppCompatActivity;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    String password;
    TextView textView;

    TextView number0, number1, number2, number3, number4, number5, number6, number7, number8, number9;
    ImageView numberBack, numberOK;
    ViewGroup layout_psd;

    String WRONG_INPUT = "ACCESS DENIED";
    String CORRECT_INPUT = "ACCESS GRANTED";
    String TIP = "ENTER 4 digit PIN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SharedPreferences sp = getSharedPreferences("PIN", 0);
        password = sp.getString("password", "");

        textView = findViewById(R.id.textview);
        textView.setText(TIP);

        layout_psd = findViewById(R.id.layout_psd);
        number0 = findViewById(R.id.number0);
        number1 = findViewById(R.id.number1);
        number2 = findViewById(R.id.number2);
        number3 = findViewById(R.id.number3);
        number4 = findViewById(R.id.number4);
        number5 = findViewById(R.id.number5);
        number6 = findViewById(R.id.number6);
        number7 = findViewById(R.id.number7);
        number8 = findViewById(R.id.number8);
        number9 = findViewById(R.id.number9);
        numberOK = findViewById(R.id.numberOK);
        numberBack = findViewById(R.id.numberB);

        number0.setOnClickListener(this);
        number1.setOnClickListener(this);
        number2.setOnClickListener(this);
        number3.setOnClickListener(this);
        number4.setOnClickListener(this);
        number5.setOnClickListener(this);
        number6.setOnClickListener(this);
        number7.setOnClickListener(this);
        number8.setOnClickListener(this);
        number9.setOnClickListener(this);
        number0.setTag(0);
        number1.setTag(1);
        number2.setTag(2);
        number3.setTag(3);
        number4.setTag(4);
        number5.setTag(5);
        number6.setTag(6);
        number7.setTag(7);
        number8.setTag(8);
        number9.setTag(9);

        numberBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteChar();
            }
        });

        numberOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                next();
            }
        });
    }

    @Override
    public void onClick(View view) {
        int number = (int) view.getTag();
        addChar(number);
    }

    private int dpToPx(float valueInDp) {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, metrics);
    }

    private void addChar(int number) {
        if (layout_psd.getChildCount() >= password.length()) {
            return;
        }
        DotView psdView = new DotView(getApplicationContext());
        int size = dpToPx(10);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(size, size);
        params.setMargins(size, 0, size, 0);
        psdView.setLayoutParams(params);
        psdView.setColor(Color.WHITE);
        psdView.setTag(number);
        layout_psd.addView(psdView);
    }

    private void deleteChar() {
        int childCount = layout_psd.getChildCount();
        if (childCount <= 0) {
            return;
        }
        layout_psd.removeViewAt(childCount - 1);
    }

    private String getPINFromView() {
        StringBuilder sb = new StringBuilder();
        int childCount = layout_psd.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = layout_psd.getChildAt(i);
            int num = (int) child.getTag();
            sb.append(num);
        }
        return sb.toString();
    }

    private void next() {
        String psd = getPINFromView();
        if (psd.equals(password))
        {
            textView.setTextColor(Color.GREEN);
            textView.setText(CORRECT_INPUT);
            Handler handler = new Handler();
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    int childCount = layout_psd.getChildCount();
                    if (childCount <= 0) {
                        return;
                    }
                    layout_psd.removeAllViews();

                    textView.setTextColor(Color.WHITE);
                    textView.setText(TIP);
                }
            }, 900);

        }
        else if (psd.length() < password.length())
        {
            runTipTextAnimation();
        }
        else
        {
            runTipTextAnimation();
            textView.setText(WRONG_INPUT);
            textView.setTextColor(Color.parseColor("#FFE63A3A"));
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    textView.setTextColor(Color.WHITE);

                    int childCount = layout_psd.getChildCount();
                    if (childCount <= 0) {
                        return;
                    }
                    layout_psd.removeAllViews();
                }
            }, 900);
        }
    }

    public void runTipTextAnimation() {
        shakeAnimator(textView).start();
    }

    private Animator shakeAnimator(View view) {
        return ObjectAnimator
                .ofFloat(view, "translationX", 0, 25, -25, 25, -25, 15, -15, 6, -6, 0)
                .setDuration(500);
    }
}