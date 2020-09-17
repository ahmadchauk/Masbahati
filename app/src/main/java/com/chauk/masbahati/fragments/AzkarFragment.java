package com.chauk.masbahati.fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.chauk.masbahati.R;
import com.chauk.masbahati.activities.ListOfAzkar;
import com.chauk.masbahati.activities.MainActivity;
import com.chauk.masbahati.adapters.CustomAdapter;
import java.util.List;
import static com.chauk.masbahati.utils.JsonUtils.getCateories;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AzkarFragment} factory method to
 * create an instance of this fragment.
 */
public class AzkarFragment extends Fragment implements CustomAdapter.ItemClickListener {

    private List<String> listOfCategories;

    public AzkarFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_azkar, container, false);
        RecyclerView recyclerView = root.findViewById(R.id.rvCategories);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        listOfCategories = getCateories(getContext());
        CustomAdapter customAdapter = new CustomAdapter(getContext(), listOfCategories);
        customAdapter.setClickListener(this);
        recyclerView.setAdapter(customAdapter);

//        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
//                ((LinearLayoutManager) layoutManager).getOrientation());
//        recyclerView.addItemDecoration(dividerItemDecoration);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        //Set title of this fragment
        if (getActivity() != null) {
            ((MainActivity) getActivity()).setTitle(getString(R.string.app_azkar));
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(getContext(), ListOfAzkar.class);
        intent.putExtra("category", listOfCategories.get(position));
        startActivity(intent);
//        Toast.makeText(getContext(), listOfCategories.get(position), Toast.LENGTH_SHORT).show();
    }


}
