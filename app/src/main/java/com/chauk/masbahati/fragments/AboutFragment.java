package com.chauk.masbahati.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.chauk.masbahati.R;
import com.chauk.masbahati.activities.MainActivity;
import com.chauk.masbahati.utils.Constant;
import com.chauk.masbahati.utils.Methods;
import com.google.android.material.snackbar.Snackbar;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.sql.Struct;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AboutFragment} factory method to
 * create an instance of this fragment.
 */
public class AboutFragment extends Fragment implements View.OnClickListener {

    private ImageButton checkForUpdate;
    public AboutFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_about, container, false);
        ((TextView) root.findViewById(R.id.version)).setText(Methods.getVersion(getContext()));

        checkForUpdate = (ImageButton) root.findViewById(R.id.check_update);
        ImageButton contactUs = (ImageButton) root.findViewById(R.id.contact);

        checkForUpdate.setOnClickListener(this);
        contactUs.setOnClickListener(this);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        //Set title of this fragment
        if (getActivity() != null) {
            ((MainActivity) getActivity()).setTitle(getString(R.string.app_about));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.check_update) {
            if(Methods.isNetworkAvailable(Objects.requireNonNull(AboutFragment.this.getActivity()))){
                GetVersionCode getVersionCode = new GetVersionCode(getActivity(), Methods.getVersion(getContext()));
                getVersionCode.execute();
            }else{
                Snackbar snackbar = Snackbar.make(getActivity().findViewById(R.id.drawer_layout),R.string.app_connectivity,Snackbar.LENGTH_LONG)
                        .setAction("المحاولة مجددا", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                checkForUpdate.callOnClick();
                            }
                        });
                ViewCompat.setLayoutDirection(snackbar.getView(),ViewCompat.LAYOUT_DIRECTION_RTL);
                snackbar.show();
            }

        } else if (id == R.id.contact) {
            sendEmail();
        }
    }

    private void sendEmail() {
        Intent email = new Intent(Intent.ACTION_SEND);
        email.putExtra(Intent.EXTRA_EMAIL, new String[]{"ahmad.chauk@hotmail.com"});
        email.putExtra(Intent.EXTRA_SUBJECT, "");
        email.putExtra(Intent.EXTRA_TEXT, "");
        email.setType("message/rfc822");
        try {
            startActivity(Intent.createChooser(email, "Send email with...?"));
        } catch (android.content.ActivityNotFoundException exception) {
            Toast.makeText(AboutFragment.this.getActivity(), "No email clients installed on device!", Toast.LENGTH_LONG).show();
        }
    }
}

class GetVersionCode extends AsyncTask<Void, String, String> {

    @SuppressLint("StaticFieldLeak")
    private Activity context;
    private String currentVersion;
    private ProgressDialog dialog;

    GetVersionCode(Activity context, String currentVersion) {
        this.context = context;
        this.currentVersion = currentVersion;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog = ProgressDialog.show(context, "",
                "جاري البحث عن تحديث....", true);
    }

    @Override
    protected String doInBackground(Void... voids) {
        String newVersion = null;
        try {
            Document document = Jsoup.connect("https://play.google.com/store/apps/details?id=" + Constant.packageName + "&hl=en")
                    .timeout(30000)
                    .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                    .referrer("http://www.google.com")
                    .get();
            if (document != null) {
                Elements element = document.getElementsContainingOwnText("Current Version");
                for (Element ele : element) {
                    if (ele.siblingElements() != null) {
                        Elements sibElemets = ele.siblingElements();
                        for (Element sibElemet : sibElemets) {
                            newVersion = sibElemet.text();
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return newVersion;

    }


    @Override
    protected void onPostExecute(String onlineVersion) {
        super.onPostExecute(onlineVersion);
        if (dialog.isShowing()) dialog.dismiss();
        if (onlineVersion != null && !onlineVersion.isEmpty()) {

            if (Float.parseFloat(currentVersion) < Float.parseFloat(onlineVersion)) {
                //show anything
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(false);
                builder.setMessage("يوجد نسخة جديدة من التطبيق اضغط على زر موافق لتحميلها");
                builder.setPositiveButton("موافق", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try {
                            context.startActivity(new Intent(Intent.ACTION_VIEW,
                                    Uri.parse("market://details?id=" + Constant.packageName)));
                        } catch (android.content.ActivityNotFoundException anfe) {
                            context.startActivity(new Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse("http://play.google.com/store/apps/details?id=" + Constant.packageName)));
                        }
                    }
                }).setNegativeButton("إلغاء", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                final AlertDialog alertDialog = builder.create();
                alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dlg) {
                        try {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                                alertDialog.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL); // set title and message direction to RTL
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

                alertDialog.show();
            }

        } else {
            //show anything
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setCancelable(false);
            builder.setMessage("لديك بالفعل آخر نسخة من التطبيق");
            builder.setPositiveButton("موافق", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });
            final AlertDialog alertDialog = builder.create();
            alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dlg) {
                    try {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                            alertDialog.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL); // set title and message direction to RTL
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            alertDialog.show();
        }

        Log.d("update", "Current version " + currentVersion + " playstore version " + onlineVersion);

    }
}