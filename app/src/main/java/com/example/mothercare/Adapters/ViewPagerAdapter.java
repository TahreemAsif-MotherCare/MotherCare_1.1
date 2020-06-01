package com.example.mothercare.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.mothercare.Models.SymptomsQuestions;
import com.example.mothercare.R;

import java.util.ArrayList;

public class ViewPagerAdapter extends PagerAdapter {

    private ArrayList<SymptomsQuestions> symptoms;
    private LayoutInflater layoutInflater;
    private Context context;
    SymtomsResponse symtomsResponse;

    public ViewPagerAdapter(ArrayList<SymptomsQuestions> symptoms, Context context, SymtomsResponse symtomsResponse) {
        this.symptoms = symptoms;
        this.context = context;
        this.symtomsResponse = symtomsResponse;
    }


    @Override
    public int getCount() {
        return symptoms.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.viewpager_item, container, false);

        TextView title;
        RadioGroup radioGroup;
        RadioButton yes, no;
        title = view.findViewById(R.id.q1);
        radioGroup = view.findViewById(R.id.q1RadioGroup);
        yes = view.findViewById(R.id.yes);
        no = view.findViewById(R.id.no);
        title.setText(symptoms.get(position).questions);

        if (symptoms.get(position).questionStatus.equals("0")) {
            no.setChecked(true);
        } else if (symptoms.get(position).questionStatus.equals("1")) {
            yes.setChecked(true);
        }
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.no) {
                    symtomsResponse.symptomResponse(position, "0");
                    symptoms.get(position).questionStatus = "0";
                } else if (checkedId == R.id.yes) {
                    symtomsResponse.symptomResponse(position, "1");
                    symptoms.get(position).questionStatus = "1";
                }
            }
        });
        container.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    public interface SymtomsResponse {
        void symptomResponse(int position, String response);
    }
}