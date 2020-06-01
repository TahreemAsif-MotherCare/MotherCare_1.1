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

public class FoodToEatInFirstTrimesterFragment extends Fragment {
    public TextView firstTrimesterFoodHeading, firstTrimesterFoodDescription;
    public ImageView firstTrimesterFoodImage;

    public static FoodToEatInFirstTrimesterFragment newInstance() {
        FoodToEatInFirstTrimesterFragment fragment = new FoodToEatInFirstTrimesterFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_food_to_eat_in_first_trimester, container, false);
        firstTrimesterFoodHeading = view.findViewById(R.id.firstTrimesterFoodHeading);
        firstTrimesterFoodDescription = view.findViewById(R.id.firstTrimesterFoodDesc);
        firstTrimesterFoodImage = view.findViewById(R.id.firstTrimesterFoodImage);
        ((InformationActivity) getActivity()).showHideProgress(true, "Please Wait");
        new myTask().execute();
        return view;
    }

    public class myTask extends AsyncTask<String, Integer, String> {
        String result = "";

        @Override
        protected String doInBackground(String... params) {
            try {
                Document doc = Jsoup.connect("https://guardian.ng/features/food-and-drinks/prenatal-nutrition-what-to-eat-trimester-by-trimester/").get();
                result = doc.select("p").get(3).text() + doc.select("p").get(4).text() + doc.select("p").get(5).text();
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
            firstTrimesterFoodHeading.setText("What to Eat in First Trimester");
            firstTrimesterFoodDescription.setText(Html.fromHtml(s));
        }
    }

}
