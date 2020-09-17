package com.chauk.masbahati.fragments;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import petrov.kristiyan.colorpicker.ColorPicker;

import android.os.Vibrator;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.chauk.masbahati.utils.Constant;
import com.chauk.masbahati.activities.MainActivity;
import com.chauk.masbahati.utils.Methods;
import com.chauk.masbahati.R;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment  implements View.OnClickListener {
    private SharedPreferences sharedPreferences, app_preferences;
    private SharedPreferences.Editor editor;
    private Button button;
    private Methods methods;
    private static Vibrator vibrator;
    private int appTheme;
    private int themeColor;
    private int appColor;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);
        // Inflate the layout for this fragment
        vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        app_preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        appColor = app_preferences.getInt("color", 0xff3F51B5);
        appTheme = app_preferences.getInt("theme", 0);
        themeColor = appColor;
        Constant.color = appColor;
        System.out.println("my color is: " + Constant.color);
        methods = new Methods();
        button = rootView.findViewById(R.id.button_color);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        editor = sharedPreferences.edit();

        colorize();
        button.setOnClickListener(this);

        CheckBox checkbox = (CheckBox) rootView.findViewById(R.id.checkbox_vibration);
        boolean isChecked = app_preferences.getBoolean("vibration", false);
        if (isChecked) checkbox.setChecked(true);
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    vibrator.vibrate(50);
                    editor.putBoolean("vibration", true);
                } else {
                    editor.putBoolean("vibration", false);
                }
                editor.commit();
            }
        });
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        //Set title of this fragment
        if (getActivity() != null) {
            ((MainActivity) getActivity()).setTitle(getString(R.string.app_setting));
        }
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void colorize() {
        ShapeDrawable d = new ShapeDrawable(new OvalShape());
        d.setBounds(58, 58, 58, 58);

        d.getPaint().setStyle(Paint.Style.FILL);
        d.getPaint().setColor(Constant.color);

        button.setBackground(d);
    }

    private void createColorPickerDialog(Activity activity){
        ArrayList<String> listColor = Methods.getListColor();
        ColorPicker colorPicker = new ColorPicker(activity);
        ViewCompat.setLayoutDirection(colorPicker.getDialogViewLayout(), ViewCompat.LAYOUT_DIRECTION_RTL);
        colorPicker.setTitlePadding(0, 10, 0, 10);
        colorPicker.setColors(listColor);
        colorPicker.setTitle("إختر اللون");
        TextView text =  colorPicker.getDialogViewLayout().findViewById(R.id.title);
        text.setTextSize(20);
        colorPicker.setOnFastChooseColorListener(new ColorPicker.OnFastChooseColorListener() {
            @Override
            public void setOnFastChooseColorListener(int position, int color) {
                colorize();
                Constant.color = color;
                System.out.println("my position: " + position);
                methods.setColorTheme();
                editor.putInt("color", color);
                editor.putInt("theme", Constant.theme);
                editor.commit();


                Intent intent = new Intent(getContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                if (Constant.isWered) Constant.isWered = false;
            }

            @Override
            public void onCancel() {

            }
        }).setRoundColorButton(true)
                .setColorButtonSize(50, 50)
                .setColorButtonMargin(5, 5, 5, 5)
                .setColumns(4).show();
    }

    @Override
    public void onClick(View view) {
        createColorPickerDialog(getActivity());
    }
}
