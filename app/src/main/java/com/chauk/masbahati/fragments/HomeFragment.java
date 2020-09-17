package com.chauk.masbahati.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import com.chauk.masbahati.R;
import com.chauk.masbahati.activities.MainActivity;
import com.chauk.masbahati.database.DBHelper;
import com.chauk.masbahati.utils.Constant;
import com.chauk.masbahati.utils.Tasbih;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import static android.content.Context.VIBRATOR_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements View.OnClickListener {

    public static DBHelper helper;
    private Vibrator vibrator;
    public TextView total_counter, current_counter,tasbih_name;
    private SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    public static int totalCounter;
    private boolean isVibrationEnabled;
    private SeekBar seekBar;
    private int counter_wered = 0;
    private static int wered;

    private static final String MyPREFERENCES = "MyPrefs";
    private static final String count = "countKey";
    private static final String first = "firstKey";

    private AdView mAdView;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        vibrator = (Vibrator) getActivity().getSystemService(VIBRATOR_SERVICE);

        initUIs(rootView);
        MobileAds.initialize(getContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                System.out.println("initialisation "+initializationStatus + " ---->");
            }
        });
        mAdView = rootView.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                System.out.println("ad loaded -------");
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                // Code to be executed when an ad request fails.
                System.out.println("ad failed in load -------"+adError.getMessage());
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
                System.out.println("ad opened -------");
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });
        SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        isVibrationEnabled = app_preferences.getBoolean("vibration", false);

        sharedPreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putInt(first, 0);
        editor.commit();

        int cpt = sharedPreferences.getInt(first, 0);

        helper = new DBHelper(getContext());
        if (cpt == 0) {
            helper.insert(0);
        } else {
            cpt++;
            editor.putInt(first, cpt);
            editor.commit();
        }

        int i = sharedPreferences.getInt(count, 0);

        current_counter.setText(String.valueOf(i));
        Tasbih t = helper.getTasbihById(1);
        totalCounter = t.getCounter();

        total_counter.setText(String.valueOf(totalCounter));

        if(Constant.isWered){
            seekBar.setVisibility(View.VISIBLE);
            String tasbihName = getArguments().getString("tasbih_name");
            tasbih_name.setText(tasbihName);
            wered = getArguments().getInt("wered_count");
            seekBar.setMax(wered);
        }

        return rootView;
    }

    public void saveToSharedPref(int value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(count, value);
        editor.commit();
    }

    public void clearSharedPref() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(count, 0);
        editor.commit();
    }

    private void initUIs(View view) {
        Button btn_increment = view.findViewById(R.id.increment_button);
        Button btn_reset = view.findViewById(R.id.reset_button);
        btn_reset.setOnClickListener(this);
        btn_increment.setOnClickListener(this);
        total_counter = view.findViewById(R.id.total_counter_textView);
        current_counter = view.findViewById(R.id.counter_textView);
        current_counter.setTextColor(Constant.color);
        tasbih_name = view.findViewById(R.id.text_tasbih_name);
        seekBar = view.findViewById(R.id.seekBar);
    }

    @Override
    public void onResume() {
        super.onResume();
        //Set title of this fragment
        if (getActivity() != null) {
            ((MainActivity)getActivity()).setTitle(getString(R.string.app_name));
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        helper.updateById(1, totalCounter);
    }

    @Override
    public void onPause() {
        super.onPause();
        helper.updateById(1, totalCounter);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.increment_button) {
            int s = Integer.parseInt(current_counter.getText().toString());
            s++;
            current_counter.setText(String.valueOf(s));
            saveToSharedPref(Integer.parseInt(current_counter.getText().toString()));
            helper.updateById(1, s);
            if (isVibrationEnabled) {
                vibrator.vibrate(50);
            }
            if(Constant.isWered){
                counter_wered++;
                seekBar.setProgress(counter_wered);
                if (counter_wered == wered ) {
                    if(isVibrationEnabled) {
                        vibrator.vibrate(500);
                    }
                    counter_wered = 0;
                    seekBar.setProgress(counter_wered);
                }
            }
            totalCounter++;
            total_counter.setText(String.valueOf(totalCounter));
        } else if (id == R.id.reset_button) {
            if (isVibrationEnabled) {
                vibrator.vibrate(1000);
            }
            current_counter.setText("0");
            clearSharedPref();
        }
    }
}
