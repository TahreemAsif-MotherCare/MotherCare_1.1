package com.example.mothercare.Views.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.mothercare.Adapters.SymptomsAdapter;
import com.example.mothercare.Adapters.ViewPagerAdapter;
import com.example.mothercare.BaseActivity;
import com.example.mothercare.Models.SymptomsQuestions;
import com.example.mothercare.R;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class SymptomsActivity extends BaseActivity implements ViewPagerAdapter.SymtomsResponse, SearchView.OnQueryTextListener {
    private Button analyzeSymptoms;
    TextView proceed, answerMoreQuestions;
    ArrayList<String> symptoms = new ArrayList<>();
    ArrayList<String> tags = new ArrayList<>();
    ArrayList<SymptomsQuestions> questions = new ArrayList<>();
    ViewPager viewPager;
    TextView remainingQuestions;
    ViewPagerAdapter adapter;
    SymptomsAdapter symptomsAdapter;
    SearchView editsearch;
    List<HashMap<String, Boolean>> symptomsHash = new ArrayList<>();
    RecyclerView recyclerView;
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        answerMoreQuestions = findViewById(R.id.answerMoreQuestions);
        proceed = findViewById(R.id.proceed);
        analyzeSymptoms = findViewById(R.id.identifySymptoms);
        recyclerView = (RecyclerView) findViewById(R.id.tagsRV);
        editsearch = (SearchView) findViewById(R.id.searchTag);
        remainingQuestions = (TextView) findViewById(R.id.remainingQuestions);
        editsearch.setOnQueryTextListener(this);
        prepareSymptomsArrayList();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        symptomsAdapter = new SymptomsAdapter(symptoms, tags, SymptomsActivity.this);
        recyclerView.setAdapter(symptomsAdapter);
        int searchCloseButtonId = editsearch.getContext().getResources()
                .getIdentifier("android:id/search_close_btn", null, null);
        ImageView closeButton = (ImageView) this.editsearch.findViewById(searchCloseButtonId);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editsearch.setQuery("", false);
                editsearch.setIconified(true);
                editsearch.clearFocus();
                tags = symptomsAdapter.getSelectedTags();
                symptomsAdapter = new SymptomsAdapter(symptoms, tags, SymptomsActivity.this);
                recyclerView.setAdapter(symptomsAdapter);
            }
        });

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHideProgress(true, "Please Wait");
                tags = symptomsAdapter.getSelectedTags();
                ObjectMapper mapper = new ObjectMapper();
                String json = getTermsString();
                try {
                    JSONArray array = new JSONArray(json);
                    for (int i = 0; i < array.length(); i++) {
                        new JSONObject();
                        JSONObject jsonObject;
                        jsonObject = array.getJSONObject(i);

                        HashMap<String, Boolean> map = new HashMap<>();
                        map.put("Bleading", jsonObject.get("Bleading").equals("1"));
                        map.put("Severe Cramps", jsonObject.get("sverecramps").equals("1"));
                        map.put("Weakness", jsonObject.get("Weakness").equals("1"));
                        map.put("Abdominal Pain", jsonObject.get("abdominalpain").equals("1"));
                        map.put("Severe Back Pain", jsonObject.get("sverebackpain").equals("1"));
                        map.put("Fever", jsonObject.get("fever").equals("1"));
                        map.put("Pregnancy Less than 20 Days", jsonObject.get("pregnancy20days").equals("1"));
                        map.put("Previous History of Miscarriage", jsonObject.get("previoushistoryofmiscarriage").equals("1"));
                        map.put("Age less than 35", jsonObject.get("age35").equals("1"));
                        map.put("Pregnant for more than 41 weeks", jsonObject.get("morethan41weekspregnant").equals("1"));
                        map.put("Placenta Order", jsonObject.get("placentadisorder").equals("1"));
                        map.put("Diabetes", jsonObject.get("diabtities").equals("1"));
                        map.put("Kidney Problem", jsonObject.get("kidneyproblem").equals("1"));
                        map.put("High-Blood Pressure", jsonObject.get("highbloodpressure").equals("1"));
                        map.put("Fewer Movements of Baby", jsonObject.get("fewermovementsofbaby").equals("1"));
                        map.put("Water has Broken", jsonObject.get("waterhasbroken").equals("1"));
                        map.put("Contraction not started yet", jsonObject.get("contractionnotstartedyet").equals("1"));
                        map.put("You may have twins", jsonObject.get("youmayhavetwins").equals("1"));
                        map.put("Vomiting", jsonObject.get("Vomiting").equals("1"));
                        map.put("Sharp Abdominal Cramps", jsonObject.get("sharpabdominalcramps").equals("1"));
                        map.put("Pain on one side", jsonObject.get("PAINononeside").equals("1"));
                        map.put("Dizziness", jsonObject.get("Dizziness").equals("1"));
                        map.put("Pain in shoulder", jsonObject.get("Paininsholder").equals("1"));
                        map.put("Difficulty in bowl movement", jsonObject.get("difficultyinbowlmovement").equals("1"));
                        map.put("Light Headed", jsonObject.get("lightheaded").equals("1"));
                        map.put("Feeling Sick", jsonObject.get("feelingsick").equals("1"));
                        map.put("Can't keep food down", jsonObject.get("cantkeepfooddown").equals("1"));
                        map.put("Weight Loss", jsonObject.get("weightloss").equals("1"));
                        map.put("Low Blood Pressure", jsonObject.get("lowbloodpressure").equals("1"));
                        map.put("Confusion", jsonObject.get("confusion").equals("1"));
                        map.put("Jaundice", jsonObject.get("jaundice").equals("1"));
                        map.put("Headache", jsonObject.get("headache").equals("1"));
                        map.put("History of premature birth", jsonObject.get("historyofprematurebirth").equals("1"));
                        map.put("Vaginal Bleeding", jsonObject.get("vaginalbleeding").equals("1"));
                        map.put("Harmonal Changes", jsonObject.get("harmonalchanges").equals("1"));
                        map.put("Uterus Stretching", jsonObject.get("uterusstreching").equals("1"));
                        map.put("Plug of mucus in your cervix", jsonObject.get("plugofmucusinyourcervix").equals("1"));
                        map.put("Last Month", jsonObject.get("lastmonth").equals("1"));
                        map.put("Cervix Dilates", jsonObject.get("cervixdilates").equals("1"));
                        map.put("Loose Feeling Joints", jsonObject.get("Loosefeelingjoints").equals("1"));
                        map.put("Weight gain stops", jsonObject.get("Weightgainstops").equals("1"));
                        map.put("Fatigue", jsonObject.get("Fatigue").equals("1"));
                        map.put("Vaginal Discharge", jsonObject.get("Vaginaldischargechangescolorandconsistency").equals("1"));
                        map.put("Frequent Contractions", jsonObject.get("frequentcontractions").equals("1"));
                        map.put("Stomach pain till 3 months", jsonObject.get("stomachpaintill3months").equals("1"));
                        map.put("Less than 37 weeks", jsonObject.get("37week").equals("1"));
                        map.put("Blackout", jsonObject.get("blackout").equals("1"));
                        map.put("Loss of consiousness", jsonObject.get("lossofconsiousness").equals("1"));
                        map.put("Breath in difficulty", jsonObject.get("breathinddifficulty").equals("1"));
                        map.put("Chest pain", jsonObject.get("CHESTPain").equals("1"));
                        map.put("Paint While Urinating", jsonObject.get("PAINWHILEUURINATING").equals("1"));
                        symptomsHash.add(map);
                    }
                    for (int i = 0; i <= symptomsHash.size(); i++) {
                        ArrayList<String> count = new ArrayList<>();
                        for (String tag : tags) {
                            if (symptomsHash.get(i).get(tag)) {
                                count.add(tag);
                            }
                        }
                        if (count.size() == tags.size()) {
                            showQuestions(symptomsHash.get(i));
                            break;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        analyzeSymptoms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    showHideProgress(true, "Please Wait");
                    JSONObject jsonObject = getJsonObject();
                    for (SymptomsQuestions symptomsQuestions : questions) {
                        if (symptomsQuestions.questions.equalsIgnoreCase("Fever")) {
                            jsonObject.remove("fever");
                            jsonObject.put("fever", symptomsQuestions.questionStatus);
                        } else if (symptomsQuestions.questions.equalsIgnoreCase("Bleading")) {
                            jsonObject.remove("Bleading (light /heavy)");
                            jsonObject.put("Bleading (light /heavy)", symptomsQuestions.questionStatus);
                        } else if (symptomsQuestions.questions.equalsIgnoreCase("Cramps")) {
                            jsonObject.remove("svere cramps");
                            jsonObject.put("svere cramps", symptomsQuestions.questionStatus);
                        } else if (symptomsQuestions.questions.equalsIgnoreCase("Weakness")) {
                            jsonObject.remove("Weakness");
                            jsonObject.put("Weakness", symptomsQuestions.questionStatus);
                        } else if (symptomsQuestions.questions.equalsIgnoreCase("Back Pain")) {
                            jsonObject.remove("svere back pain");
                            jsonObject.put("svere back pain", symptomsQuestions.questionStatus);
                        } else if (symptomsQuestions.questions.equalsIgnoreCase("Stomach Pain")) {
                            jsonObject.remove("abdominal pain");
                            jsonObject.put("abdominal pain", symptomsQuestions.questionStatus);
                        } else if (symptomsQuestions.questions.toLowerCase().contains("20 days")) {
                            jsonObject.remove("pregnancy<20days");
                            jsonObject.put("pregnancy<20days", symptomsQuestions.questionStatus);
                        } else if (symptomsQuestions.questions.toLowerCase().contains("miscarriage")) {
                            jsonObject.remove("previous history of miscarriage?");
                            jsonObject.put("previous history of miscarriage?", symptomsQuestions.questionStatus);
                        } else if (symptomsQuestions.questions.toLowerCase().contains("35")) {
                            jsonObject.remove("age>35");
                            jsonObject.put("age>35", symptomsQuestions.questionStatus);
                        } else if (symptomsQuestions.questions.toLowerCase().contains("41 weeks")) {
                            jsonObject.remove("more than 41 weeks pregnant");
                            jsonObject.put("more than 41 weeks pregnant", symptomsQuestions.questionStatus);
                        } else if (symptomsQuestions.questions.toLowerCase().contains("placenta disorder")) {
                            jsonObject.remove("placenta disorder(mother blood supply disorder");
                            jsonObject.put("placenta disorder(mother blood supply disorder", symptomsQuestions.questionStatus);
                        } else if (symptomsQuestions.questions.toLowerCase().contains("diabities")) {
                            jsonObject.remove("diabtities");
                            jsonObject.put("diabtities", symptomsQuestions.questionStatus);
                        } else if (symptomsQuestions.questions.toLowerCase().contains("kidney")) {
                            jsonObject.remove("kidney problem");
                            jsonObject.put("kidney problem", symptomsQuestions.questionStatus);
                        } else if (symptomsQuestions.questions.toLowerCase().contains("high blood pressure")) {
                            jsonObject.remove("highblood pressure");
                            jsonObject.put("highblood pressure", symptomsQuestions.questionStatus);
                        } else if (symptomsQuestions.questions.toLowerCase().contains("fewer movements")) {
                            jsonObject.remove("fewer movements of baby");
                            jsonObject.put("fewer movements of baby", symptomsQuestions.questionStatus);
                        } else if (symptomsQuestions.questions.toLowerCase().contains("water")) {
                            jsonObject.remove("water has broken");
                            jsonObject.put("water has broken", symptomsQuestions.questionStatus);
                        } else if (symptomsQuestions.questions.toLowerCase().contains("contractions not started")) {
                            jsonObject.remove("contraction not started yet");
                            jsonObject.put("contraction not started yet", symptomsQuestions.questionStatus);
                        } else if (symptomsQuestions.questions.toLowerCase().contains("twins")) {
                            jsonObject.remove("you may have twins");
                            jsonObject.put("you may have twins", symptomsQuestions.questionStatus);
                        } else if (symptomsQuestions.questions.toLowerCase().contains("nausea")) {
                            jsonObject.remove("Nausea");
                            jsonObject.put("Nausea ", symptomsQuestions.questionStatus);
                        } else if (symptomsQuestions.questions.toLowerCase().contains("vomitting")) {
                            jsonObject.remove("Vomiting");
                            jsonObject.put("Vomiting", symptomsQuestions.questionStatus);
                        } else if (symptomsQuestions.questions.toLowerCase().contains("abdominal cramps")) {
                            jsonObject.remove("sharp abdominal cramps");
                            jsonObject.put("sharp abdominal cramps", symptomsQuestions.questionStatus);
                        } else if (symptomsQuestions.questions.toLowerCase().contains("pain on one side")) {
                            jsonObject.remove("PAIN on one side");
                            jsonObject.put("PAIN on one side", symptomsQuestions.questionStatus);
                        } else if (symptomsQuestions.questions.toLowerCase().contains("dizziness")) {
                            jsonObject.remove("Dizziness");
                            jsonObject.put("Dizziness", symptomsQuestions.questionStatus);
                        } else if (symptomsQuestions.questions.toLowerCase().contains("pain in shoulder")) {
                            jsonObject.remove("Pain in sholder/neck/rectum");
                            jsonObject.put("Pain in sholder/neck/rectum", symptomsQuestions.questionStatus);
                        } else if (symptomsQuestions.questions.toLowerCase().contains("bowl movement")) {
                            jsonObject.remove("difficulty in bowl movement");
                            jsonObject.put("difficulty in bowl movement", symptomsQuestions.questionStatus);
                        } else if (symptomsQuestions.questions.toLowerCase().contains("lighheaded")) {
                            jsonObject.remove("lightheaded");
                            jsonObject.put("lightheaded", symptomsQuestions.questionStatus);
                        } else if (symptomsQuestions.questions.toLowerCase().contains("sick")) {
                            jsonObject.remove("feeling sick");
                            jsonObject.put("feeling sick", symptomsQuestions.questionStatus);
                        } else if (symptomsQuestions.questions.toLowerCase().contains("food down")) {
                            jsonObject.remove("can't keep  food down");
                            jsonObject.put("can't keep  food down", symptomsQuestions.questionStatus);
                        } else if (symptomsQuestions.questions.toLowerCase().contains("weightloss")) {
                            jsonObject.remove("weightloss");
                            jsonObject.put("weightloss", symptomsQuestions.questionStatus);
                        } else if (symptomsQuestions.questions.toLowerCase().contains("low blood pressure")) {
                            jsonObject.remove("low blood pressure");
                            jsonObject.put("low blood pressure", symptomsQuestions.questionStatus);
                        } else if (symptomsQuestions.questions.toLowerCase().contains("confusion")) {
                            jsonObject.remove("confusion");
                            jsonObject.put("confusion", symptomsQuestions.questionStatus);
                        } else if (symptomsQuestions.questions.toLowerCase().contains("jaundice")) {
                            jsonObject.remove("jaundice");
                            jsonObject.put("jaundice", symptomsQuestions.questionStatus);
                        } else if (symptomsQuestions.questions.toLowerCase().contains("headache")) {
                            jsonObject.remove("headache");
                            jsonObject.put("headache  ", symptomsQuestions.questionStatus);
                        } else if (symptomsQuestions.questions.toLowerCase().contains("premature")) {
                            jsonObject.remove("history of premature birth/preterm labor");
                            jsonObject.put("history of premature birth/preterm labor", symptomsQuestions.questionStatus);
                        } else if (symptomsQuestions.questions.toLowerCase().contains("vaginal bleeding")) {
                            jsonObject.remove("vaginal bleeding");
                            jsonObject.put("vaginal bleeding", symptomsQuestions.questionStatus);
                        } else if (symptomsQuestions.questions.toLowerCase().contains("harmonal changes")) {
                            jsonObject.remove("harmonal changes");
                            jsonObject.put("harmonal changes", symptomsQuestions.questionStatus);
                        } else if (symptomsQuestions.questions.toLowerCase().contains("uterus")) {
                            jsonObject.remove("uterus streching");
                            jsonObject.put("uterus streching", symptomsQuestions.questionStatus);
                        } else if (symptomsQuestions.questions.toLowerCase().contains("plug of mucus")) {
                            jsonObject.remove("plug of mucus in your cervix?");
                            jsonObject.put("plug of mucus in your cervix?", symptomsQuestions.questionStatus);
                        } else if (symptomsQuestions.questions.toLowerCase().contains("last month")) {
                            jsonObject.remove("last month");
                            jsonObject.put("last month", symptomsQuestions.questionStatus);
                        } else if (symptomsQuestions.questions.toLowerCase().contains("aby")) {
                            jsonObject.remove("aby drops");
                            jsonObject.put("aby drops", symptomsQuestions.questionStatus);
                        } else if (symptomsQuestions.questions.toLowerCase().contains("cervix dilates")) {
                            jsonObject.remove("cervix dilates");
                            jsonObject.put("cervix dilates", symptomsQuestions.questionStatus);
                        } else if (symptomsQuestions.questions.toLowerCase().contains("loose-feeling")) {
                            jsonObject.remove("Loose-feeling joints");
                            jsonObject.put("Loose-feeling joints", symptomsQuestions.questionStatus);
                        } else if (symptomsQuestions.questions.toLowerCase().contains("weight gain")) {
                            jsonObject.remove("Weight gain stops");
                            jsonObject.put("Weight gain stops", symptomsQuestions.questionStatus);
                        } else if (symptomsQuestions.questions.toLowerCase().contains("fatigue")) {
                            jsonObject.remove("Fatigue");
                            jsonObject.put("Fatigue", symptomsQuestions.questionStatus);
                        } else if (symptomsQuestions.questions.toLowerCase().contains("vaginal discharge")) {
                            jsonObject.remove("Vaginal discharge changes color and consistency");
                            jsonObject.put("Vaginal discharge changes color and consistency", symptomsQuestions.questionStatus);
                        } else if (symptomsQuestions.questions.toLowerCase().contains("frequent contractions")) {
                            jsonObject.remove("frequent contractions");
                            jsonObject.put("frequent contractions", symptomsQuestions.questionStatus);
                        } else if (symptomsQuestions.questions.toLowerCase().contains("3 months")) {
                            jsonObject.remove("stomach pain till3 months");
                            jsonObject.put("stomach pain till3 months", symptomsQuestions.questionStatus);
                        } else if (symptomsQuestions.questions.toLowerCase().contains("37 weeks")) {
                            jsonObject.remove("<37 week");
                            jsonObject.put("<37 week", symptomsQuestions.questionStatus);
                        } else if (symptomsQuestions.questions.toLowerCase().contains("blackout")) {
                            jsonObject.remove("blackout");
                            jsonObject.put("blackout", symptomsQuestions.questionStatus);
                        } else if (symptomsQuestions.questions.toLowerCase().contains("loss of consiousness ")) {
                            jsonObject.remove("loss of consiousness");
                            jsonObject.put("loss of consiousness", symptomsQuestions.questionStatus);
                        } else if (symptomsQuestions.questions.toLowerCase().contains("breathing")) {
                            jsonObject.remove("breathind difficulty");
                            jsonObject.put("breathind difficulty", symptomsQuestions.questionStatus);
                        } else if (symptomsQuestions.questions.toLowerCase().contains("chest")) {
                            jsonObject.remove("CHEST Pain");
                            jsonObject.put("CHEST Pain", symptomsQuestions.questionStatus);
                        } else if (symptomsQuestions.questions.toLowerCase().contains("urinating")) {
                            jsonObject.remove("PAIN WHILE UURINATING");
                            jsonObject.put("PAIN WHILE UURINATING", symptomsQuestions.questionStatus);
                        }
                    }
                    showHideProgress(false, "Please Wait");

                    //showAlertDialog("Request", jsonObject.toString());
                    ////////////////////////////////////////////////////////////////////////////////
                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    String url = "http://192.168.10.2:5002/predict";
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        showHideProgress(false, "");
                                        Intent intent = new Intent(SymptomsActivity.this, SymptomsOutputActivity.class);
                                        intent.putExtra("output", "Symptom analyzer has analyzed that: " + response.getString("msg"));
                                        startActivity(intent);
                                    } catch (JSONException e) {
                                        showHideProgress(false, "Please Wait");
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            showHideProgress(false, "Please Wait");
                            showAlertDialog("Response", "String Response : " + error.toString());
                        }
                    });
                    requestQueue.add(jsonObjectRequest);
                    ////////////////////////////////////////////////////////////////////////////////

                } catch (Exception e) {
                    showHideProgress(false, "Please Wait");
                    showAlertDialog("Error", e.getLocalizedMessage());
                }
                //Log. d("JSON", "Error!");
            }
        });
    }

    private void prepareSymptomsArrayList() {
        symptoms.add("Bleading");
        symptoms.add("Severe Cramps");
        symptoms.add("Weakness");
        symptoms.add("Abdominal Pain");
        symptoms.add("Severe Back Pain");
        symptoms.add("Fever");
        symptoms.add("Pregnancy Less than 20 Days");
        symptoms.add("Previous History of Miscarriage");
        symptoms.add("Age less than 35");
        symptoms.add("Pregnant for more than 41 weeks");
        symptoms.add("Placenta Order");
        symptoms.add("Diabetes");
        symptoms.add("Kidney Problem");
        symptoms.add("High-Blood Pressure");
        symptoms.add("Fewer Movements of Baby");
        symptoms.add("Water has Broken");
        symptoms.add("Contraction not started yet");
        symptoms.add("You may have twins");
        symptoms.add("Vomiting");
        symptoms.add("Sharp Abdominal Cramps");
        symptoms.add("Pain on one side");
        symptoms.add("Dizziness");
        symptoms.add("Pain in shoulder");
        symptoms.add("Difficulty in bowl movement");
        symptoms.add("Light Headed");
        symptoms.add("Feeling Sick");
        symptoms.add("Can't keep food down");
        symptoms.add("Weight Loss");
        symptoms.add("Low Blood Pressure");
        symptoms.add("Confusion");
        symptoms.add("Jaundice");
        symptoms.add("Headache");
        symptoms.add("History of premature birth");
        symptoms.add("Vaginal Bleeding");
        symptoms.add("Harmonal Changes");
        symptoms.add("Uterus Stretching");
        symptoms.add("Plug of mucus in your cervix");
        symptoms.add("Last Month");
        symptoms.add("Cervix Dilates");
        symptoms.add("Loose Feeling Joints");
        symptoms.add("Weight gain stops");
        symptoms.add("Fatigue");
        symptoms.add("Vaginal Discharge");
        symptoms.add("Frequent Contractions");
        symptoms.add("Stomach pain till 3 months");
        symptoms.add("Less than 37 weeks");
        symptoms.add("Blackout");
        symptoms.add("Loss of consiousness");
        symptoms.add("Breath in difficulty");
        symptoms.add("Chest pain");
        symptoms.add("Paint While Urinating");
    }

    private String getTermsString() {
        StringBuilder termsString = new StringBuilder();
        BufferedReader reader;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(getAssets().open("JsonString.txt")));

            String str;
            while ((str = reader.readLine()) != null) {
                termsString.append(str);
            }

            reader.close();
            return termsString.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_symptoms;
    }

    @SuppressLint("ClickableViewAccessibility")
    private void showQuestions(HashMap<String, Boolean> symptoms) {
        Iterator it = symptoms.entrySet().iterator();
        while (it.hasNext()) {
            HashMap.Entry pair = (HashMap.Entry) it.next();
            questions.add(new SymptomsQuestions(pair.getKey().toString(), ""));
            it.remove();
        }
        adapter = new ViewPagerAdapter(questions, this, this);
        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter((PagerAdapter) adapter);
        viewPager.setPadding(60, 20, 60, 20);
        viewPager.setVisibility(View.VISIBLE);
        count = questions.size();
        remainingQuestions.setText("Remaining Questions: " + String.valueOf(count) + "/" + questions.size());
        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        showHideProgress(false, "");
    }

    @Override
    public void symptomResponse(int position, String response) {
        questions.get(position).questionStatus = response;
        viewPager.setCurrentItem(position + 1);
        count--;
        remainingQuestions.setText("Remaining Questions: " + String.valueOf(count) + "/" + questions.size());
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String text = newText;
        symptomsAdapter.filter(text);
        return false;
    }

    public JSONObject getJsonObject() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("fever", "0");
            jsonObject.put("Bleading (light /heavy)", "0");
            jsonObject.put("svere cramps", "0");
            jsonObject.put("Weakness", "0");
            jsonObject.put("svere back pain", "0");
            jsonObject.put("abdominal pain", "0");
            jsonObject.put("pregnancy<20days", "0");
            jsonObject.put("previous history of miscarriage?", "0");
            jsonObject.put("age>35", "0");
            jsonObject.put("more than 41 weeks pregnant", "0");
            jsonObject.put("placenta disorder(mother blood supply disorder", "0");
            jsonObject.put("diabtities", "0");
            jsonObject.put("kidney problem", "0");
            jsonObject.put("highblood pressure", "0");
            jsonObject.put("fewer movements of baby", "0");
            jsonObject.put("water has broken", "0");
            jsonObject.put("contraction not started yet", "0");
            jsonObject.put("you may have twins", "0");
            jsonObject.put("Nausea ", "0");
            jsonObject.put("Vomiting", "0");
            jsonObject.put("sharp abdominal cramps", "0");
            jsonObject.put("PAIN on one side", "0");
            jsonObject.put("Dizziness", "0");
            jsonObject.put("Pain in sholder/neck/rectum", "0");
            jsonObject.put("difficulty in bowl movement", "0");
            jsonObject.put("lightheaded", "0");
            jsonObject.put("feeling sick", "0");
            jsonObject.put("can't keep  food down", "0");
            jsonObject.put("weightloss", "0");
            jsonObject.put("low blood pressure", "0");
            jsonObject.put("confusion", "0");
            jsonObject.put("jaundice", "0");
            jsonObject.put("headache  ", "0");
            jsonObject.put("history of premature birth/preterm labor", "0");
            jsonObject.put("vaginal bleeding", "0");
            jsonObject.put("harmonal changes", "0");
            jsonObject.put("uterus streching", "0");
            jsonObject.put("plug of mucus in your cervix?", "0");
            jsonObject.put("last month", "0");
            jsonObject.put("aby drops", "0");
            jsonObject.put("cervix dilates", "0");
            jsonObject.put("Loose-feeling joints", "0");
            jsonObject.put("Weight gain stops", "0");
            jsonObject.put("Fatigue", "0");
            jsonObject.put("Vaginal discharge changes color and consistency", "0");
            jsonObject.put("frequent contractions", "0");
            jsonObject.put("stomach pain till3 months", "0");
            jsonObject.put("<37 week", "0");
            jsonObject.put("blackout", "0");
            jsonObject.put("loss of consiousness", "0");
            jsonObject.put("breathind difficulty", "0");
            jsonObject.put("CHEST Pain", "0");
            jsonObject.put("PAIN WHILE UURINATING", "0");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

}
