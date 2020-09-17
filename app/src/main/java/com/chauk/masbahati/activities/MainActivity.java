package com.chauk.masbahati.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.chauk.masbahati.R;
import com.chauk.masbahati.fragments.AboutFragment;
import com.chauk.masbahati.fragments.AzkarFragment;
import com.chauk.masbahati.fragments.HomeFragment;
import com.chauk.masbahati.fragments.NamesOfAllahFragment;
import com.chauk.masbahati.fragments.SettingsFragment;
import com.chauk.masbahati.utils.Constant;
import com.chauk.masbahati.utils.ScreenUtils;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    SharedPreferences app_preferences;
    DrawerLayout drawer;
    int appTheme;
    int themeColor;
    int appColor;
    private boolean isVibrationEnabled;
    private static Vibrator vibrator;
    HomeFragment homeFragment;
    Object currentFragment;
    TextView mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app_preferences = PreferenceManager.getDefaultSharedPreferences(this);
        appColor = app_preferences.getInt("color", 0xff3F51B5);
        appTheme = app_preferences.getInt("theme", 0);
        isVibrationEnabled = app_preferences.getBoolean("vibration", false);
        themeColor = appColor;
        Constant.color = appColor;

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (themeColor == 0) {
            setTheme(Constant.theme);
        } else if (appTheme == 0) {
            setTheme(Constant.theme);
        } else {
            setTheme(appTheme);
        }
        setContentView(R.layout.activity_main);

        ScreenUtils.lockOrientation(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setBackgroundColor(Constant.color);
        System.out.println("my color is: " + Constant.color);
        mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        toggle.setDrawerIndicatorEnabled(false);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerLayout = navigationView.getHeaderView(0);
        headerLayout.setBackgroundColor(Constant.color);

        //Create Home Fragment object
        homeFragment = new HomeFragment();
        //--------Set Home fragment as default fragment-------//
        setMyFragment(homeFragment);
    }

    public void setTitle(String title) {
        mTitle.setText(title);
    }

    @Override
    public void onBackPressed() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(Gravity.RIGHT)) {
            drawer.closeDrawer(Gravity.RIGHT);
        } else if (currentFragment instanceof HomeFragment && !Constant.isWered) {
            super.onBackPressed();
        } else {
            Constant.isWered = false;
            homeFragment = new HomeFragment();
            setMyFragment(homeFragment);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_right) {
            if (drawer.isDrawerOpen(Gravity.RIGHT)) {
                drawer.closeDrawer(Gravity.RIGHT);
            } else {
                drawer.openDrawer(Gravity.RIGHT);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        drawer.closeDrawer(Gravity.RIGHT);
        switch (id) {
            case R.id.home:
                Constant.isWered = false;
                HomeFragment homeFragment = new HomeFragment();
                setMyFragment(homeFragment);
                break;
            case R.id.names:
                NamesOfAllahFragment namesOfAllahFragment = new NamesOfAllahFragment();
                setMyFragment(namesOfAllahFragment);
                break;
            case R.id.wered:
                showAlert();
                break;
            case R.id.azkar:
                AzkarFragment azkarFragment = new AzkarFragment();
                setMyFragment(azkarFragment);
                break;
            case R.id.reset:
                resetAlert();
                break;
            case R.id.about:
                AboutFragment aboutFragment = new AboutFragment();
                setMyFragment(aboutFragment);
                break;
            case R.id.setting:
                SettingsFragment settingsFragment = new SettingsFragment();
                setMyFragment(settingsFragment);
                break;
            case R.id.share:
                share();
                break;
            case R.id.rate:
                rate();
                break;
        }
        return false;
    }

    private void setMyFragment(Fragment fragment) {
        currentFragment = (Fragment) fragment;
        //get current fragment manager
        FragmentManager fragmentManager = getSupportFragmentManager();

        //get fragment transaction
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        //set fragment animation
        fragmentTransaction.setCustomAnimations(R.anim.slide_left, R.anim.slide_right);

        //set new fragment in fragment_container (FrameLayout)
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }

    public void showAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_dialog, null);

        // Specify alert dialog is not cancelable/not ignorable
        builder.setCancelable(false);

        // Set the custom layout as alert dialog view
        builder.setView(dialogView);
        // Get the custom alert dialog view widgets reference
        Button btn_positive = (Button) dialogView.findViewById(R.id.dialog_positive_btn);
        Button btn_negative = (Button) dialogView.findViewById(R.id.dialog_negative_btn);

        btn_positive.setTextColor(Constant.color);
        btn_negative.setTextColor(Constant.color);

        final Spinner spinner = (Spinner) dialogView.findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.tasbihat, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        final EditText et_name = (EditText) dialogView.findViewById(R.id.et_name);

        // Create the alert dialog
        final AlertDialog dialog = builder.create();

        // Set positive/yes button click listener
        btn_positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Dismiss the alert dialog
                dialog.cancel();
                Constant.isWered = true;
                String name = et_name.getText().toString();
                if (name.length() != 0) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("wered_count", Integer.parseInt(name));
                    bundle.putString("tasbih_name", spinner.getSelectedItem().toString());

                    homeFragment = new HomeFragment();
                    homeFragment.setArguments(bundle);
                    setMyFragment(homeFragment);
                } else {
                    Toast.makeText(getApplication(),
                            "الرجاء إدخال عدد التسبيحات!!", Toast.LENGTH_SHORT).show();
                }


            }
        });

        // Set negative/no button click listener
        btn_negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Dismiss/cancel the alert dialog
                //dialog.cancel();
                dialog.dismiss();
                // Toast.makeText(getApplication(),
                //    "No button clicked", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme; //style id

        // Display the custom alert dialog on interface
        dialog.show();
    }

    public void resetAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_alert_dialog, null);

        // Specify alert dialog is not cancelable/not ignorable
        builder.setCancelable(false);

        // Set the custom layout as alert dialog view
        builder.setView(dialogView);
        // Get the custom alert dialog view widgets reference
        Button btn_positive = (Button) dialogView.findViewById(R.id.dialog_positive_btn);
        Button btn_negative = (Button) dialogView.findViewById(R.id.dialog_negative_btn);

        btn_positive.setTextColor(Constant.color);
        btn_negative.setTextColor(Constant.color);

        final TextView alertTitle = (TextView) dialogView.findViewById(R.id.alert_title);
        final TextView alertDescription = (TextView) dialogView.findViewById(R.id.alert_description);
        alertTitle.setText("تصفير مجموع التسبيحات");
        alertDescription.setText("هل أنت متأكد أنك تريد تصفير مجموع التسبيحات؟");
        // Create the alert dialog
        final AlertDialog dialog = builder.create();

        // Set positive/yes button click listener
        btn_positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Dismiss the alert dialog
                dialog.cancel();
                if (isVibrationEnabled) {
                    vibrator.vibrate(1000);
                }
                HomeFragment.helper.updateById(1, 0);
                homeFragment.current_counter.setText("0");
                homeFragment.clearSharedPref();
                homeFragment.total_counter.setText("0");
                HomeFragment.totalCounter = 0;
                setMyFragment(homeFragment);
            }
        });

        // Set negative/no button click listener
        btn_negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Dismiss/cancel the alert dialog
                //dialog.cancel();
                dialog.dismiss();
                // Toast.makeText(getApplication(),
                //    "No button clicked", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme; //style id
        // Display the custom alert dialog on interface
        dialog.show();
    }

    public void share() {
        String SHARE_CONTENT = "https://play.google.com/store/apps/details?id=" + Constant.packageName;
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, SHARE_CONTENT);
        intent.setType("text/plain");
        startActivity(Intent.createChooser(intent, "Share with"));
    }

    public void rate() {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=" + Constant.packageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + Constant.packageName)));
        }
    }

}
