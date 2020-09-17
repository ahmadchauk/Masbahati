package com.chauk.masbahati.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.chauk.masbahati.R;
import com.chauk.masbahati.utils.Constant;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private final Item[] mItems;
    private final Context mContext;
    private final String mLongest;

    public Adapter(@NonNull Context context, @NonNull Item[] objects, boolean isMulticolumn) {
        mContext = context;
        mItems = objects;
        if (isMulticolumn) {
            String longest = "";
            for (Item i : objects) {
                if (i.desc != null && i.desc.length() > longest.length())
                    longest = i.desc;
            }
            mLongest = longest;
        } else {
            mLongest = null;
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.names_of_allah_row, parent, false);
//        ((RelativeLayout) v.findViewById(R.id.myview)).setBackgroundColor(Constant.color);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder vh, int pos) {
        Item i = mItems[pos];
        if (i.pos == 0) {
            vh.arabicImg.setVisibility(View.VISIBLE);
            vh.arabicImg.setImageResource(R.drawable.allah_);
            vh.arabic.setText(i.arabic);
            vh.name.setVisibility(View.GONE);
        } else {
            vh.arabicImg.setImageDrawable(null);
            vh.arabicImg.setVisibility(View.GONE);
            vh.arabic.setText(i.arabic);
            if (i.name == null) {
                vh.name.setVisibility(View.GONE);
            } else {
                vh.name.setText(i.name);
                vh.name.setVisibility(View.VISIBLE);
            }
        }

        if (i.desc == null) {
            vh.meaning.setVisibility(View.GONE);
            vh.meaningInv.setVisibility(View.GONE);
        } else {
            vh.meaning.setText(i.desc);
            vh.meaningInv.setText((mLongest == null || pos == 0) ? i.desc : mLongest);
        }

    }

    @Override
    public int getItemCount() {
        return mItems.length;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) return 0;
        else return 1;
    }

     public static class Item {
        public  int pos;
        public  String arabic;
        public  String name;
        public  String desc;

        @NonNull
        @Override
        public String toString() {
            return arabic + " " + name + " " + desc;
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView meaningInv;
        private final TextView name;
        private final TextView meaning;
        private final TextView arabic;
        private final ImageView arabicImg;

        ViewHolder(View convertView) {
            super(convertView);
            name = convertView.findViewById(R.id.name);
            name.setTextColor(Constant.color);
            arabicImg = convertView.findViewById(R.id.arabicImg);
            arabic = convertView.findViewById(R.id.arabicTxt);
            arabic.setTextColor(Constant.color);
            meaning = convertView.findViewById(R.id.meaning);
            meaningInv = convertView.findViewById(R.id.meaningInvisible);
        }
    }
}