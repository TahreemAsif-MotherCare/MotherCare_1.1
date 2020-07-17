package com.example.mothercare.Views.Activities.webCrawler;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import com.example.mothercare.R;

import net.cachapa.expandablelayout.ExpandableLayout;

public class TrimesterTwoFragment extends Fragment {
    private ExpandableLayout expandableLayout0, bodyChangesEL, fetusDevEL, whatToDoEL;
    private LinearLayout whatisFirstTrimester, bodyChanges, fetusDev, whatToDo;
    private ImageView firstTrimesterIV, bodyChangesIV, fetusDevIV, whatToDoIV;

    public static TrimesterTwoFragment newInstance() {
        TrimesterTwoFragment fragment = new TrimesterTwoFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_trimester_two, container, false);
        expandableLayout0 = view.findViewById(R.id.expandable_layout);
        bodyChangesEL = view.findViewById(R.id.bodyChangesEL);
        fetusDevEL = view.findViewById(R.id.fetusDevEL);
        whatToDoEL = view.findViewById(R.id.whatToDoEL);

        whatisFirstTrimester = view.findViewById(R.id.whatIsFirstTrimester);
        bodyChanges = view.findViewById(R.id.FTBodyChanges);
        fetusDev = view.findViewById(R.id.FTFetusDev);
        whatToDo = view.findViewById(R.id.FTWhatToDo);

        bodyChangesIV = view.findViewById(R.id.bodyChangesIV);
        firstTrimesterIV = view.findViewById(R.id.firstTrimesterIV);
        fetusDevIV = view.findViewById(R.id.fetusDevIV);
        whatToDoIV = view.findViewById(R.id.whatToDoIV);

        whatisFirstTrimester.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (expandableLayout0.isExpanded()) {
                    expandableLayout0.collapse(true);
                    firstTrimesterIV.setImageDrawable(getResources().getDrawable(R.drawable.ic_add));
                } else {
                    expandableLayout0.expand(true);
                    firstTrimesterIV.setImageDrawable(getResources().getDrawable(R.drawable.ic_collapse));
                }
            }
        });
        fetusDev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fetusDevEL.isExpanded()) {
                    fetusDevEL.collapse(true);
                    fetusDevIV.setImageDrawable(getResources().getDrawable(R.drawable.ic_add));
                } else {
                    fetusDevEL.expand(true);
                    fetusDevIV.setImageDrawable(getResources().getDrawable(R.drawable.ic_collapse));
                }
            }
        });

        bodyChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bodyChangesEL.isExpanded()) {
                    bodyChangesEL.collapse(true);
                    bodyChangesIV.setImageDrawable(getResources().getDrawable(R.drawable.ic_add));
                } else {
                    bodyChangesEL.expand(true);
                    bodyChangesIV.setImageDrawable(getResources().getDrawable(R.drawable.ic_collapse));
                }
            }
        });
        whatToDo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (whatToDoEL.isExpanded()) {
                    whatToDoEL.collapse(true);
                    whatToDoIV.setImageDrawable(getResources().getDrawable(R.drawable.ic_add));
                } else {
                    whatToDoEL.expand(true);
                    whatToDoIV.setImageDrawable(getResources().getDrawable(R.drawable.ic_collapse));
                }
            }
        });
        return view;
    }
}
