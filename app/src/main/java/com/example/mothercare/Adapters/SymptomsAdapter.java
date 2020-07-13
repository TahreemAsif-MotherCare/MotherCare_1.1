package com.example.mothercare.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.mothercare.R;
import com.example.mothercare.Utilities.FirebaseUtil;
import com.veinhorn.tagview.TagView;

import java.util.ArrayList;
import java.util.Locale;

public class SymptomsAdapter extends RecyclerView.Adapter<SymptomsAdapter.MyViewHolder> {

    private ArrayList<String> symptomsArrayList = new ArrayList<>();
    ArrayList<String> tags;
    private FirebaseUtil firebaseUtil;
    private ArrayList<String> arraylist;
    private Context context;
    public int arraySize = 0;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TagView tagView;

        public MyViewHolder(View view) {
            super(view);
            tagView = view.findViewById(R.id.tagView);
        }
    }

    public SymptomsAdapter(ArrayList<String> symptomsArrayList, ArrayList<String> tags, Context context) {
        this.symptomsArrayList = symptomsArrayList;
        this.context = context;
        this.arraylist = new ArrayList<String>();
        firebaseUtil = new FirebaseUtil(context);
        this.arraylist.addAll(symptomsArrayList);
        this.arraySize = symptomsArrayList.size();
        this.tags = tags;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.symptoms_tags_layout_row, parent, false);
        return new MyViewHolder(itemView);
    }

    public ArrayList<String> getSelectedTags() {
        return this.tags;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        if (tags.isEmpty() || tags != null) {
            holder.tagView.setText(symptomsArrayList.get(position));
            if (tags.contains(symptomsArrayList.get(position))) {
                holder.tagView.setTagColor(context.getResources().getColor(R.color.colorAccent));
            }
        }

        holder.tagView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tags.contains(symptomsArrayList.get(position))) {
                    tags.remove(symptomsArrayList.get(position));
                    holder.tagView.setTagColor(context.getResources().getColor(R.color.colorPrimaryDark));
                } else {
                    tags.add(symptomsArrayList.get(position));
                    holder.tagView.setTagColor(context.getResources().getColor(R.color.colorAccent));
                }
            }
        });
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        symptomsArrayList.clear();
        if (charText.length() == 0) {
            symptomsArrayList.addAll(arraylist);
        } else {
            for (String wp : arraylist) {
                if (wp.toLowerCase(Locale.getDefault()).contains(charText)) {
                    symptomsArrayList.add(wp);
                }
            }
            arraySize = symptomsArrayList.size();
            notifyDataSetChanged();
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        /*if (arraySize != 51)
            return symptomsArrayList.size();
        else*/
        return symptomsArrayList.size();
    }
}