package com.chauk.masbahati.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chauk.masbahati.R;
import com.chauk.masbahati.activities.MainActivity;
import com.chauk.masbahati.adapters.Adapter;
import com.chauk.masbahati.utils.Methods;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class NamesOfAllahFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private Adapter.Item[] mValues;
    private int mCols;

    public NamesOfAllahFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_names_of_allah, container, false);
        mRecyclerView = v.findViewById(R.id.namesOfAllah);
        DisplayMetrics dimension = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dimension);

        float w = Methods.convertPixelsToDp(dimension.widthPixels);

        mCols = (int) (w / 300);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), mCols);

        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return position == 0 ? mCols : 1;
            }
        });
        mRecyclerView.setLayoutManager(layoutManager);
        mValues = new Adapter.Item[99];

        String[] ar = getResources().getStringArray(R.array.names_ar);
        String[] name = getResources().getStringArray(R.array.names_name);
        String[] desc = getResources().getStringArray(R.array.names_desc);

        for (int i = 0; i < 99; i++) {
            Adapter.Item item = new Adapter.Item();
            item.arabic = ar[i];
            item.pos = i;
//            if (name.length > i) item.name = name[i];
//            if (desc.length > i) item.desc = desc[i];
            mValues[i] = item;
        }
        mRecyclerView.setAdapter(new Adapter(getActivity(), mValues, mCols > 1));
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        //Set title of this fragment
        if (getActivity() != null) {
            ((MainActivity)getActivity()).setTitle(getString(R.string.app_names));
        }

    }
}
