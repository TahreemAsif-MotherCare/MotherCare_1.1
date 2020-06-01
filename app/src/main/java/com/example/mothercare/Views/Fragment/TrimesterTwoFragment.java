package com.example.mothercare.Views.Fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.mothercare.R;
import com.example.mothercare.Views.Activities.InformationActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class TrimesterTwoFragment extends Fragment {
    public TextView triHeading, triDescription;
    public ImageView triImage;

    public static TrimesterTwoFragment newInstance() {
        TrimesterTwoFragment fragment = new TrimesterTwoFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_trimester_two, container, false);
        triHeading = view.findViewById(R.id.tri2Heading);
        triDescription = view.findViewById(R.id.tri2Desc);
        triImage = view.findViewById(R.id.tri2Image);
        ((InformationActivity) getActivity()).showHideProgress(true, "Please Wait");
        new myTask().execute();
        return view;
    }

    public class myTask extends AsyncTask<String, Integer, String> {
        String result = "";

        @Override
        protected String doInBackground(String... params) {
            try {
                Document doc = Jsoup.connect("https://www.phillyvoice.com/what-expect-during-each-trimester-pregnancy-059991/").get();
                result = doc.getElementsByClass("rich-text").select("h3").get(1) + "\n" +
                        doc.getElementsByClass("rich-text").select("p").get(4) + doc.getElementsByClass("rich-text").select("p").get(5)
                        + doc.getElementsByClass("rich-text").select("p").get(6);
                return result;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            ((InformationActivity) getActivity()).showHideProgress(false, "Please Wait");
            String[] data = s.split("\n");
            triHeading.setText(Html.fromHtml(data[0]));
            triDescription.setText(Html.fromHtml(data[1]));
        }
    }

}
