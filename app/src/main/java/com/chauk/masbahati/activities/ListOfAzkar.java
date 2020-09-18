package com.chauk.masbahati.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.chauk.masbahati.R;
import com.chauk.masbahati.utils.Constant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.chauk.masbahati.utils.JsonUtils.getZekerByCategory;

public class ListOfAzkar extends AppCompatActivity {

    SharedPreferences app_preferences;
    int appTheme;
    int themeColor;
    int appColor;
    JSONArray jsonArray;

    LinearLayout  layoutReference, linearLayout;
    TextView repeatNumber, reference, zeker, currentZeker;
    public static int mCurrentZeker = 0;
    private int lengthOfZeker;
    private float minimumSize;
    ImageButton zoomIn,zoomOut;
    SeekBar seekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app_preferences = PreferenceManager.getDefaultSharedPreferences(this);
        appColor = app_preferences.getInt("color", 0xff3F51B5);
        appTheme = app_preferences.getInt("theme", 0);

        themeColor = appColor;
        Constant.color = appColor;

        if (themeColor == 0) {
            setTheme(Constant.theme);
        } else if (appTheme == 0) {
            setTheme(Constant.theme);
        } else {
            setTheme(appTheme);
        }
        setContentView(R.layout.activity_list_of_azkar_);

        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(Constant.color);
        TextView textView =  findViewById(R.id.toolbar_textview);


        String category = getIntent().getExtras().getString("category");
        textView.setText(category);
        jsonArray = getZekerByCategory(getApplicationContext(), category);
        lengthOfZeker = jsonArray.length();

        initViews();

        minimumSize = 25;

        setData(mCurrentZeker);

    }

    public void initViews() {
        repeatNumber = findViewById(R.id.repeat_number);
        reference = findViewById(R.id.reference);
        zeker = findViewById(R.id.zeker);
        linearLayout = findViewById(R.id.navigation);
        layoutReference = findViewById(R.id.reference_layout);
        currentZeker = findViewById(R.id.current_zeker);
        zoomIn = findViewById(R.id.zoomIn);
        zoomOut = findViewById(R.id.zoomOut);
        seekBar = findViewById(R.id.seekBar);
        seekBar.setMax(40);
        seekBar.setProgress(25);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                zoomInOut(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        repeatNumber.setTextColor(Constant.color);
        reference.setTextColor(Constant.color);
        zeker.setTextColor(Constant.color);
        linearLayout.setBackgroundColor(Constant.color);
        zoomIn.setBackgroundColor(Constant.color);
        zoomOut.setBackgroundColor(Constant.color);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mCurrentZeker = 0;
        lengthOfZeker = 0;
        this.finish();
    }

    public void setData(int position) {
        try {
            JSONObject data = jsonArray.getJSONObject(position);

            if(!data.getString("count").equals("") || !data.getString("reference").equals("") ){
                layoutReference.setVisibility(View.VISIBLE);
            }
            if (!data.getString("count").equals("")) {
                int number = Integer.parseInt(data.optString("count"));
                String text = String.valueOf(number);
                if (number == 1 || number == 100) {
                    text += " مرة";
                } else {
                    text += " مرات";
                }
                System.out.println("number of repeat: " + text);
                repeatNumber.setText(text);
            }

            zeker.setText(data.getString("zekr"));

            if (!data.getString("reference").equals("")) {
                reference.setText("(" + data.getString("reference") + ")");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        currentZeker.setText((position + 1) + "/" + lengthOfZeker);

    }

    public void nextZeker(View view) {
        mCurrentZeker++;
        if (mCurrentZeker > lengthOfZeker - 1) {
            mCurrentZeker = lengthOfZeker - 1;
        } else {
            setData(mCurrentZeker);
        }
    }

    public void previousZeker(View view) {
        mCurrentZeker--;
        if (mCurrentZeker < 0) {
            mCurrentZeker = 0;
        } else {
            setData(mCurrentZeker);
        }
    }

    public void zoomInOut(int i){
//        float px = zeker.getTextSize();
//        float currentSize = px/getResources().getDisplayMetrics().scaledDensity;
//        System.out.println("current size:: " + currentSize);
//        currentSize++;
//        if(currentSize > 40){
//            currentSize = 40;
//        }
        zeker.setTextSize(i);
    }

//    public void zoomOut(){
//        float px = zeker.getTextSize();
//        float currentSize = px/getResources().getDisplayMetrics().scaledDensity;
//        currentSize--;
//        System.out.println("current size:: " + currentSize);
//        if(currentSize < minimumSize){
//            currentSize = minimumSize;
//        }
//        zeker.setTextSize(currentSize);
//    }
}
