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
import com.example.mothercare.Views.Activities.webCrawler.InformationActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.Objects;

public class FoodToEatInThirdTrimesterFragment extends Fragment {
    public TextView thirdTrimesterFoodHeading, thirdTrimesterFoodDescription;
    public ImageView thirdTrimesterFoodImage;

    public static FoodToEatInThirdTrimesterFragment newInstance() {
        FoodToEatInThirdTrimesterFragment fragment = new FoodToEatInThirdTrimesterFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_food_to_eat_in_third_trimester, container, false);
        thirdTrimesterFoodHeading = view.findViewById(R.id.thirdTrimesterFoodHeading);
        thirdTrimesterFoodDescription = view.findViewById(R.id.thirdTrimesterFoodDesc);
        thirdTrimesterFoodImage = view.findViewById(R.id.thirdTrimesterFoodImage);
        ((InformationActivity) Objects.requireNonNull(getActivity())).showHideProgress(true, "Please Wait");
        new myTask().execute();
        return view;
    }

    public class myTask extends AsyncTask<String, Integer, String> {
        String result = "";

        @Override
        protected String doInBackground(String... params) {
            try {
                Document doc = Jsoup.connect("https://guardian.ng/features/food-and-drinks/prenatal-nutrition-what-to-eat-trimester-by-trimester/").get();
                result = doc.select("p").get(8).text() + doc.select("p").get(9).text() + doc.select("p").get(10).text();
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
            thirdTrimesterFoodHeading.setText("What to Eat in Third Trimester");
            thirdTrimesterFoodDescription.setText(Html.fromHtml(s));
        }
    }

}
