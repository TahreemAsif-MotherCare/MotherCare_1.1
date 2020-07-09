package com.example.mothercare.Views.Activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.mothercare.Adapters.ViewPagerAdapter;
import com.example.mothercare.BaseActivity;
import com.example.mothercare.Models.Symptoms;
import com.example.mothercare.Models.SymptomsQuestions;
import com.example.mothercare.R;
import com.skyhope.materialtagview.model.TagModel;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;

public class SymptomsActivity extends BaseActivity implements ViewPagerAdapter.SymtomsResponse {
    //    TagView tagView;
    private Button analyzeSymptoms;
    TextView proceed, answerMoreQuestions;
    ArrayList<Symptoms> symptomsArayList;
    ArrayList<TagModel> selectedTags = new ArrayList<>();
    ArrayList<SymptomsQuestions> questions = new ArrayList<>();
    ViewPager viewPager;
    ViewPagerAdapter adapter;
    RecyclerView tagsRV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        answerMoreQuestions = findViewById(R.id.answerMoreQuestions);
        proceed = findViewById(R.id.proceed);
        analyzeSymptoms = findViewById(R.id.identifySymptoms);
        tagsRV = findViewById(R.id.tagsRV);
        // set a StaggeredGridLayoutManager with 3 number of columns and horizontal orientation
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(3, LinearLayoutManager.HORIZONTAL);
        tagsRV.setLayoutManager(staggeredGridLayoutManager); // set LayoutManager to RecyclerView
//        tagView = findViewById(R.id.tag_view_test);
/*
        tagView.setHint("Add your skill");
        tagView.setHint("Add additional symptoms if you have any.");
        tagView.addTagLimit(6);
        String[] tagList = new String[]{"Fever", "Bleeding", "Cramps", "Weakness", "Back Pain", "Stomach Pain"};
        tagView.setTagList(tagList);
*/

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    showHideProgress(true, "Please Wait");
                    readExcelFile();
                } catch (IOException | InvalidFormatException e) {
                    e.printStackTrace();
                }
            }
        });

        analyzeSymptoms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    showHideProgress(true, "Please Wait");
                    JSONObject jsonObject = new JSONObject();
                    for (SymptomsQuestions symptomsQuestions : questions) {
                        if (symptomsQuestions.questions.equalsIgnoreCase("Fever")) {
                            jsonObject.put("fever", symptomsQuestions.questionStatus);
                        } else if (symptomsQuestions.questions.equalsIgnoreCase("Bleeding")) {
                            jsonObject.put("Bleading (light /heavy)", symptomsQuestions.questionStatus);
                        } else if (symptomsQuestions.questions.equalsIgnoreCase("Cramps")) {
                            jsonObject.put("svere cramps", symptomsQuestions.questionStatus);
                        } else if (symptomsQuestions.questions.equalsIgnoreCase("Weakness")) {
                            jsonObject.put("Weakness", symptomsQuestions.questionStatus);
                        } else if (symptomsQuestions.questions.equalsIgnoreCase("Back Pain")) {
                            jsonObject.put("svere back pain", symptomsQuestions.questionStatus);
                        } else if (symptomsQuestions.questions.equalsIgnoreCase("Stomach Pain")) {
                            jsonObject.put("abdominal pain", symptomsQuestions.questionStatus);
                        } else if (symptomsQuestions.questions.toLowerCase().contains("20 days")) {
                            jsonObject.put("pregnancy<20days", symptomsQuestions.questionStatus);
                        } else if (symptomsQuestions.questions.toLowerCase().contains("miscarriage")) {
                            jsonObject.put("previous history of miscarriage?", symptomsQuestions.questionStatus);
                        } else if (symptomsQuestions.questions.toLowerCase().contains("35")) {
                            jsonObject.put("age>35", symptomsQuestions.questionStatus);
                        } else if (symptomsQuestions.questions.toLowerCase().contains("41 weeks")) {
                            jsonObject.put("more than 41 weeks pregnant", symptomsQuestions.questionStatus);
                        } else if (symptomsQuestions.questions.toLowerCase().contains("placenta disorder")) {
                            jsonObject.put("placenta disorder(mother blood supply disorder", symptomsQuestions.questionStatus);
                        } else if (symptomsQuestions.questions.toLowerCase().contains("diabities")) {
                            jsonObject.put("diabtities", symptomsQuestions.questionStatus);
                        } else if (symptomsQuestions.questions.toLowerCase().contains("kidney")) {
                            jsonObject.put("kidney problem", symptomsQuestions.questionStatus);
                        } else if (symptomsQuestions.questions.toLowerCase().contains("high blood pressure")) {
                            jsonObject.put("highblood pressure", symptomsQuestions.questionStatus);
                        } else if (symptomsQuestions.questions.toLowerCase().contains("fewer movements")) {
                            jsonObject.put("fewer movements of baby", symptomsQuestions.questionStatus);
                        } else if (symptomsQuestions.questions.toLowerCase().contains("water")) {
                            jsonObject.put("water has broken", symptomsQuestions.questionStatus);
                        } else if (symptomsQuestions.questions.toLowerCase().contains("contractions not started")) {
                            jsonObject.put("contraction not started yet", symptomsQuestions.questionStatus);
                        } else if (symptomsQuestions.questions.toLowerCase().contains("twins")) {
                            jsonObject.put("you may have twins", symptomsQuestions.questionStatus);
                        } else if (symptomsQuestions.questions.toLowerCase().contains("nausea")) {
                            jsonObject.put("Nausea ", symptomsQuestions.questionStatus);
                        } else if (symptomsQuestions.questions.toLowerCase().contains("vomitting")) {
                            jsonObject.put("Vomiting", symptomsQuestions.questionStatus);
                        } else if (symptomsQuestions.questions.toLowerCase().contains("abdominal cramps")) {
                            jsonObject.put("sharp abdominal cramps", symptomsQuestions.questionStatus);
                        } else if (symptomsQuestions.questions.toLowerCase().contains("pain on one side")) {
                            jsonObject.put("PAIN on one side", symptomsQuestions.questionStatus);
                        } else if (symptomsQuestions.questions.toLowerCase().contains("dizziness")) {
                            jsonObject.put("Dizziness", symptomsQuestions.questionStatus);
                        } else if (symptomsQuestions.questions.toLowerCase().contains("pain in shoulder")) {
                            jsonObject.put("Pain in sholder/neck/rectum", symptomsQuestions.questionStatus);
                        } else if (symptomsQuestions.questions.toLowerCase().contains("bowl movement")) {
                            jsonObject.put("difficulty in bowl movement", symptomsQuestions.questionStatus);
                        } else if (symptomsQuestions.questions.toLowerCase().contains("lighheaded")) {
                            jsonObject.put("lightheaded", symptomsQuestions.questionStatus);
                        } else if (symptomsQuestions.questions.toLowerCase().contains("sick")) {
                            jsonObject.put("feeling sick", symptomsQuestions.questionStatus);
                        } else if (symptomsQuestions.questions.toLowerCase().contains("food down")) {
                            jsonObject.put("can't keep  food down", symptomsQuestions.questionStatus);
                        } else if (symptomsQuestions.questions.toLowerCase().contains("weightloss")) {
                            jsonObject.put("weightloss", symptomsQuestions.questionStatus);
                        } else if (symptomsQuestions.questions.toLowerCase().contains("low blood pressure")) {
                            jsonObject.put("low blood pressure", symptomsQuestions.questionStatus);
                        } else if (symptomsQuestions.questions.toLowerCase().contains("confusion")) {
                            jsonObject.put("confusion", symptomsQuestions.questionStatus);
                        } else if (symptomsQuestions.questions.toLowerCase().contains("jaundice")) {
                            jsonObject.put("jaundice", symptomsQuestions.questionStatus);
                        } else if (symptomsQuestions.questions.toLowerCase().contains("headache")) {
                            jsonObject.put("headache  ", symptomsQuestions.questionStatus);
                        } else if (symptomsQuestions.questions.toLowerCase().contains("premature")) {
                            jsonObject.put("history of premature birth/preterm labor", symptomsQuestions.questionStatus);
                        } else if (symptomsQuestions.questions.toLowerCase().contains("vaginal bleeding")) {
                            jsonObject.put("vaginal bleeding", symptomsQuestions.questionStatus);
                        } else if (symptomsQuestions.questions.toLowerCase().contains("harmonal changes")) {
                            jsonObject.put("harmonal changes", symptomsQuestions.questionStatus);
                        } else if (symptomsQuestions.questions.toLowerCase().contains("uterus")) {
                            jsonObject.put("uterus streching", symptomsQuestions.questionStatus);
                        } else if (symptomsQuestions.questions.toLowerCase().contains("plug of mucus")) {
                            jsonObject.put("plug of mucus in your cervix?", symptomsQuestions.questionStatus);
                        } else if (symptomsQuestions.questions.toLowerCase().contains("last month")) {
                            jsonObject.put("last month", symptomsQuestions.questionStatus);
                        } else if (symptomsQuestions.questions.toLowerCase().contains("aby")) {
                            jsonObject.put("aby drops", symptomsQuestions.questionStatus);
                        } else if (symptomsQuestions.questions.toLowerCase().contains("cervix dilates")) {
                            jsonObject.put("cervix dilates", symptomsQuestions.questionStatus);
                        } else if (symptomsQuestions.questions.toLowerCase().contains("loose-feeling")) {
                            jsonObject.put("Loose-feeling joints", symptomsQuestions.questionStatus);
                        } else if (symptomsQuestions.questions.toLowerCase().contains("weight gain")) {
                            jsonObject.put("Weight gain stops", symptomsQuestions.questionStatus);
                        } else if (symptomsQuestions.questions.toLowerCase().contains("fatigue")) {
                            jsonObject.put("Fatigue", symptomsQuestions.questionStatus);
                        } else if (symptomsQuestions.questions.toLowerCase().contains("vaginal discharge")) {
                            jsonObject.put("Vaginal discharge changes color and consistency", symptomsQuestions.questionStatus);
                        } else if (symptomsQuestions.questions.toLowerCase().contains("frequent contractions")) {
                            jsonObject.put("frequent contractions", symptomsQuestions.questionStatus);
                        } else if (symptomsQuestions.questions.toLowerCase().contains("3 months")) {
                            jsonObject.put("stomach pain till3 months", symptomsQuestions.questionStatus);
                        } else if (symptomsQuestions.questions.toLowerCase().contains("37 weeks")) {
                            jsonObject.put("<37 week", symptomsQuestions.questionStatus);
                        } else if (symptomsQuestions.questions.toLowerCase().contains("blackout")) {
                            jsonObject.put("blackout", symptomsQuestions.questionStatus);
                        } else if (symptomsQuestions.questions.toLowerCase().contains("loss of consiousness ")) {
                            jsonObject.put("loss of consiousness", symptomsQuestions.questionStatus);
                        } else if (symptomsQuestions.questions.toLowerCase().contains("breathing")) {
                            jsonObject.put("breathind difficulty", symptomsQuestions.questionStatus);
                        } else if (symptomsQuestions.questions.toLowerCase().contains("chest")) {
                            jsonObject.put("CHEST Pain", symptomsQuestions.questionStatus);
                        } else if (symptomsQuestions.questions.toLowerCase().contains("urinating")) {
                            jsonObject.put("PAIN WHILE UURINATING", symptomsQuestions.questionStatus);
                        }
                    }
                    showHideProgress(true, "Please Wait");

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
                                        showAlertDialog("Symptom Analyzer Result", "Symptom analyzer has analyzed that: " + response.getString("msg"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
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

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_symptoms;
    }

    public void readExcelFile() throws IOException, InvalidFormatException {
        symptomsArayList = new ArrayList<>();
        InputStream inputStream = getAssets().open("testdata.xlsx");
        File file = getFile(inputStream);
        Workbook wb = WorkbookFactory.create(file);
        Sheet mySheet = wb.getSheetAt(0);
        Log.d("TAG", "readExcelFile: " + mySheet.getPhysicalNumberOfRows());
        Iterator<Row> rowIter = mySheet.rowIterator();
        System.out.println(mySheet.getRow(1).getCell(1));
        for (int i = 1; i <= mySheet.getPhysicalNumberOfRows() - 2; i++) {
            mySheet.getRow(i).getPhysicalNumberOfCells();
            Symptoms symptoms = new Symptoms();
            symptoms.setBleeding(mySheet.getRow(i).getCell(1).toString().equals("1.0"));
            symptoms.setSevereCramps(mySheet.getRow(i).getCell(2).toString().equals("1.0"));
            symptoms.setWeakness(mySheet.getRow(i).getCell(3).toString().equals("1.0"));
            symptoms.setAbdominalPain(mySheet.getRow(i).getCell(4).toString().equals("1.0"));
            symptoms.setSevereBackPain(mySheet.getRow(i).getCell(5).toString().equals("1.0"));
            symptoms.setFever(mySheet.getRow(i).getCell(6).toString().equals("1.0"));
            symptoms.setPregnancyLessThan20Days(mySheet.getRow(i).getCell(7).toString().equals("1.0"));
            symptoms.setPreviousHistoryOfMiscarriage(mySheet.getRow(i).getCell(8).toString().equals("1.0"));
            symptoms.setAgeLessThan35(mySheet.getRow(i).getCell(9).toString().equals("1.0"));
            symptoms.setPregnancyWeeksMoreThan41(mySheet.getRow(i).getCell(10).toString().equals("1.0"));
            symptoms.setPlacentaDisorder(mySheet.getRow(i).getCell(11).toString().equals("1.0"));
            symptoms.setDiabities(mySheet.getRow(i).getCell(12).toString().equals("1.0"));
            symptoms.setKidneyProblem(mySheet.getRow(i).getCell(13).toString().equals("1.0"));
            symptoms.setHighBloodPressure(mySheet.getRow(i).getCell(14).toString().equals("1.0"));
            symptoms.setFewerMovementsOfBaby(mySheet.getRow(i).getCell(15).toString().equals("1.0"));
            symptoms.setWaterBroken(mySheet.getRow(i).getCell(16).toString().equals("1.0"));
            symptoms.setContractionStarted(mySheet.getRow(i).getCell(17).toString().equals("1.0"));
            symptoms.setMayHaveTwins(mySheet.getRow(i).getCell(18).toString().equals("1.0"));
            symptoms.setNausea(mySheet.getRow(i).getCell(19).toString().equals("1.0"));
            symptoms.setVomiting(mySheet.getRow(i).getCell(20).toString().equals("1.0"));
            symptoms.setSharpAbdominalCramps(mySheet.getRow(i).getCell(21).toString().equals("1.0"));
            symptoms.setPainOnOneSide(mySheet.getRow(i).getCell(22).toString().equals("1.0"));
            symptoms.setPainOnOneSide(mySheet.getRow(i).getCell(23).toString().equals("1.0"));
            symptoms.setDizziness(mySheet.getRow(i).getCell(24).toString().equals("1.0"));
            symptoms.setPaintInShoulder(mySheet.getRow(i).getCell(25).toString().equals("1.0"));
            symptoms.setDifficultyInBowlMovement(mySheet.getRow(i).getCell(26).toString().equals("1.0"));
            symptoms.setLightHeaded(mySheet.getRow(i).getCell(27).toString().equals("1.0"));
            symptoms.setFeelingSick(mySheet.getRow(i).getCell(28).toString().equals("1.0"));
            symptoms.setCantKeepFoodDown(mySheet.getRow(i).getCell(29).toString().equals("1.0"));
            symptoms.setWeightLoss(mySheet.getRow(i).getCell(30).toString().equals("1.0"));
            symptoms.setLowBloodPressure(mySheet.getRow(i).getCell(31).toString().equals("1.0"));
            symptoms.setConfusion(mySheet.getRow(i).getCell(32).toString().equals("1.0"));
            symptoms.setJaundice(mySheet.getRow(i).getCell(33).toString().equals("1.0"));
            symptoms.setHeadache(mySheet.getRow(i).getCell(34).toString().equals("1.0"));
            symptoms.setHistoryOfPrematureBirth(mySheet.getRow(i).getCell(35).toString().equals("1.0"));
            symptoms.setVaginalBleeding(mySheet.getRow(i).getCell(36).toString().equals("1.0"));
            symptoms.setHarmonalChanges(mySheet.getRow(i).getCell(37).toString().equals("1.0"));
            symptoms.setUterusStrectching(mySheet.getRow(i).getCell(38).toString().equals("1.0"));
            symptoms.setPlugOfMucusInCervix(mySheet.getRow(i).getCell(39).toString().equals("1.0"));
            symptoms.setLastMonth(mySheet.getRow(i).getCell(40).toString().equals("1.0"));
            symptoms.setAbyDrops(mySheet.getRow(i).getCell(41).toString().equals("1.0"));
            symptoms.setCervixDilates(mySheet.getRow(i).getCell(42).toString().equals("1.0"));
            symptoms.setLooseFeelingJoints(mySheet.getRow(i).getCell(43).toString().equals("1.0"));
            symptoms.setWeightGainStops(mySheet.getRow(i).getCell(44).toString().equals("1.0"));
            symptoms.setFatigue(mySheet.getRow(i).getCell(45).toString().equals("1.0"));
            symptoms.setVaginalDischargeChanges(mySheet.getRow(i).getCell(46).toString().equals("1.0"));
            symptoms.setFrequestContractions(mySheet.getRow(i).getCell(47).toString().equals("1.0"));
            symptoms.setStomachPainTill3Months(mySheet.getRow(i).getCell(48).toString().equals("1.0"));
            symptoms.setBlackOut(mySheet.getRow(i).getCell(49).toString().equals("1.0"));
            symptoms.setLossOfConsiousness(mySheet.getRow(i).getCell(50).toString().equals("1.0"));
            symptoms.setBreathingDifficulty(mySheet.getRow(i).getCell(52).toString().equals("1.0"));
            symptoms.setChestPain(mySheet.getRow(i).getCell(53).toString().equals("1.0"));
            symptoms.setPainWhileUrinating(mySheet.getRow(i).getCell(54).toString().equals("1.0"));
            symptomsArayList.add(symptoms);
        }
//        prepareQuestions();
    }

    public File getFile(InputStream inputStream) throws IOException {
        File file = null;
        try {
            file = new File(getCacheDir(), "testdata.xlsx");
            try (OutputStream output = new FileOutputStream(file)) {
                byte[] buffer = new byte[4 * 1024]; // or other buffer size
                int read;

                while ((read = inputStream.read(buffer)) != -1) {
                    output.write(buffer, 0, read);
                }

                output.flush();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } finally {
            inputStream.close();
        }
        return file;
    }

//    private void prepareQuestions() {
//        selectedTags = (ArrayList) tagView.getSelectedTags();
//        ArrayList<Symptoms> selectedTagsSymptomsArrayList = new ArrayList<>();
//
//
//        for (int i = 0; i < symptomsArayList.size(); i++) {
//            Symptoms symptoms = symptomsArayList.get(i);
//            if (selectedTags.isEmpty()) {
//                if ((!symptoms.isBleeding()) && (!symptoms.isFever()) && (!symptoms.isSevereBackPain()) && (!symptoms.isSevereCramps()) && (!symptoms.isWeakness()) &&
//                        (!symptoms.isAbdominalPain())) {
//                    selectedTagsSymptomsArrayList.add(symptoms);
//                }
//            } else {
//                for (TagModel tagModel : selectedTags) {
//                    if (tagModel.getTagText().equalsIgnoreCase("Fever")) {
//                        if (symptoms.isFever()) {
//                            selectedTagsSymptomsArrayList.add(symptoms);
//                        }
//                    } else if (tagModel.getTagText().equalsIgnoreCase("Bleeding")) {
//                        if (symptoms.isBleeding()) {
//                            selectedTagsSymptomsArrayList.add(symptoms);
//                        }
//                    } else if (tagModel.getTagText().equalsIgnoreCase("Cramps")) {
//                        if (symptoms.isSevereCramps()) {
//                            selectedTagsSymptomsArrayList.add(symptoms);
//                        }
//                    } else if (tagModel.getTagText().equalsIgnoreCase("Weakness")) {
//                        if (symptoms.isWeakness()) {
//                            selectedTagsSymptomsArrayList.add(symptoms);
//                        }
//                    } else if (tagModel.getTagText().equalsIgnoreCase("Back Pain")) {
//                        if (symptoms.isSevereBackPain()) {
//                            selectedTagsSymptomsArrayList.add(symptoms);
//                        }
//                    } else if (tagModel.getTagText().equalsIgnoreCase("Stomach Pain")) {
//                        if (symptoms.isAbdominalPain()) {
//                            selectedTagsSymptomsArrayList.add(symptoms);
//                        }
//                    } else if (tagModel.getTagText().toLowerCase().contains("20 days")) {
//                        if (symptoms.isPregnancyLessThan20Days()) {
//                            selectedTagsSymptomsArrayList.add(symptoms);
//                        }
//                    } else if (tagModel.getTagText().toLowerCase().contains("miscarriage")) {
//                        if (symptoms.isPreviousHistoryOfMiscarriage()) {
//                            selectedTagsSymptomsArrayList.add(symptoms);
//                        }
//                    } else if (tagModel.getTagText().toLowerCase().contains("35")) {
//                        if (symptoms.isAgeLessThan35()) {
//                            selectedTagsSymptomsArrayList.add(symptoms);
//                        }
//                    } else if (tagModel.getTagText().toLowerCase().contains("41 weeks")) {
//                        if (symptoms.isPregnancyWeeksMoreThan41()) {
//                            selectedTagsSymptomsArrayList.add(symptoms);
//                        }
//                    } else if (tagModel.getTagText().toLowerCase().contains("placenta disorder")) {
//                        if (symptoms.isPlacentaDisorder()) {
//                            selectedTagsSymptomsArrayList.add(symptoms);
//                        }
//                    } else if (tagModel.getTagText().toLowerCase().contains("diabities")) {
//                        if (symptoms.isDiabities()) {
//                            selectedTagsSymptomsArrayList.add(symptoms);
//                        }
//                    } else if (tagModel.getTagText().toLowerCase().contains("kidney")) {
//                        if (symptoms.isKidneyProblem()) {
//                            selectedTagsSymptomsArrayList.add(symptoms);
//                        }
//                    } else if (tagModel.getTagText().toLowerCase().contains("high blood pressure")) {
//                        if (symptoms.isHighBloodPressure()) {
//                            selectedTagsSymptomsArrayList.add(symptoms);
//                        }
//                    } else if (tagModel.getTagText().toLowerCase().contains("fewer movements")) {
//                        if (symptoms.isFewerMovementsOfBaby()) {
//                            selectedTagsSymptomsArrayList.add(symptoms);
//                        }
//                    } else if (tagModel.getTagText().toLowerCase().contains("water")) {
//                        if (symptoms.isWaterBroken()) {
//                            selectedTagsSymptomsArrayList.add(symptoms);
//                        }
//                    } else if (tagModel.getTagText().toLowerCase().contains("contractions not started")) {
//                        if (symptoms.isContractionStarted()) {
//                            selectedTagsSymptomsArrayList.add(symptoms);
//                        }
//                    } else if (tagModel.getTagText().toLowerCase().contains("twins")) {
//                        if (symptoms.isMayHaveTwins()) {
//                            selectedTagsSymptomsArrayList.add(symptoms);
//                        }
//                    } else if (tagModel.getTagText().toLowerCase().contains("nausea")) {
//                        if (symptoms.isNausea()) {
//                            selectedTagsSymptomsArrayList.add(symptoms);
//                        }
//                    } else if (tagModel.getTagText().toLowerCase().contains("vomitting")) {
//                        if (symptoms.isVomiting()) {
//                            selectedTagsSymptomsArrayList.add(symptoms);
//                        }
//                    } else if (tagModel.getTagText().toLowerCase().contains("abdominal cramps")) {
//                        if (symptoms.isSharpAbdominalCramps()) {
//                            selectedTagsSymptomsArrayList.add(symptoms);
//                        }
//                    } else if (tagModel.getTagText().toLowerCase().contains("pain on one side")) {
//                        if (symptoms.isPainOnOneSide()) {
//                            selectedTagsSymptomsArrayList.add(symptoms);
//                        }
//                    } else if (tagModel.getTagText().toLowerCase().contains("dizziness")) {
//                        if (symptoms.isDizziness()) {
//                            selectedTagsSymptomsArrayList.add(symptoms);
//                        }
//                    } else if (tagModel.getTagText().toLowerCase().contains("pain in shoulder")) {
//                        if (symptoms.isPaintInShoulder()) {
//                            selectedTagsSymptomsArrayList.add(symptoms);
//                        }
//                    } else if (tagModel.getTagText().toLowerCase().contains("bowl movement")) {
//                        if (symptoms.isDifficultyInBowlMovement()) {
//                            selectedTagsSymptomsArrayList.add(symptoms);
//                        }
//                    } else if (tagModel.getTagText().toLowerCase().contains("lighheaded")) {
//                        if (symptoms.isLightHeaded()) {
//                            selectedTagsSymptomsArrayList.add(symptoms);
//                        }
//                    } else if (tagModel.getTagText().toLowerCase().contains("sick")) {
//                        if (symptoms.isFeelingSick()) {
//                            selectedTagsSymptomsArrayList.add(symptoms);
//                        }
//                    } else if (tagModel.getTagText().toLowerCase().contains("food down")) {
//                        if (symptoms.isCantKeepFoodDown()) {
//                            selectedTagsSymptomsArrayList.add(symptoms);
//                        }
//                    } else if (tagModel.getTagText().toLowerCase().contains("weightloss")) {
//                        if (symptoms.isWeightLoss()) {
//                            selectedTagsSymptomsArrayList.add(symptoms);
//                        }
//                    } else if (tagModel.getTagText().toLowerCase().contains("low blood pressure")) {
//                        if (symptoms.isLowBloodPressure()) {
//                            selectedTagsSymptomsArrayList.add(symptoms);
//                        }
//                    } else if (tagModel.getTagText().toLowerCase().contains("confusion")) {
//                        if (symptoms.isConfusion()) {
//                            selectedTagsSymptomsArrayList.add(symptoms);
//                        }
//                    } else if (tagModel.getTagText().toLowerCase().contains("jaundice")) {
//                        if (symptoms.isJaundice()) {
//                            selectedTagsSymptomsArrayList.add(symptoms);
//                        }
//                    } else if (tagModel.getTagText().toLowerCase().contains("headache")) {
//                        if (symptoms.isHeadache()) {
//                            selectedTagsSymptomsArrayList.add(symptoms);
//                        }
//                    } else if (tagModel.getTagText().toLowerCase().contains("premature")) {
//                        if (symptoms.isHistoryOfPrematureBirth()) {
//                            selectedTagsSymptomsArrayList.add(symptoms);
//                        }
//                    } else if (tagModel.getTagText().toLowerCase().contains("vaginal bleeding")) {
//                        if (symptoms.isVaginalBleeding()) {
//                            selectedTagsSymptomsArrayList.add(symptoms);
//                        }
//                    } else if (tagModel.getTagText().toLowerCase().contains("harmonal changes")) {
//                        if (symptoms.isHarmonalChanges()) {
//                            selectedTagsSymptomsArrayList.add(symptoms);
//                        }
//                    } else if (tagModel.getTagText().toLowerCase().contains("uterus")) {
//                        if (symptoms.isUterusStrectching()) {
//                            selectedTagsSymptomsArrayList.add(symptoms);
//                        }
//                    } else if (tagModel.getTagText().toLowerCase().contains("plug of mucus")) {
//                        if (symptoms.isPlugOfMucusInCervix()) {
//                            selectedTagsSymptomsArrayList.add(symptoms);
//                        }
//                    } else if (tagModel.getTagText().toLowerCase().contains("last month")) {
//                        if (symptoms.isLastMonth()) {
//                            selectedTagsSymptomsArrayList.add(symptoms);
//                        }
//                    } else if (tagModel.getTagText().toLowerCase().contains("aby")) {
//                        if (symptoms.isAbyDrops()) {
//                            selectedTagsSymptomsArrayList.add(symptoms);
//                        }
//                    } else if (tagModel.getTagText().toLowerCase().contains("cervix dilates")) {
//                        if (symptoms.isCervixDilates()) {
//                            selectedTagsSymptomsArrayList.add(symptoms);
//                        }
//                    } else if (tagModel.getTagText().toLowerCase().contains("loose-feeling")) {
//                        if (symptoms.isLooseFeelingJoints()) {
//                            selectedTagsSymptomsArrayList.add(symptoms);
//                        }
//                    } else if (tagModel.getTagText().toLowerCase().contains("weight gain")) {
//                        if (symptoms.isWeightGainStops()) {
//                            selectedTagsSymptomsArrayList.add(symptoms);
//                        }
//                    } else if (tagModel.getTagText().toLowerCase().contains("fatigue")) {
//                        if (symptoms.isFatigue()) {
//                            selectedTagsSymptomsArrayList.add(symptoms);
//                        }
//                    } else if (tagModel.getTagText().toLowerCase().contains("vaginal discharge")) {
//                        if (symptoms.isVaginalDischargeChanges()) {
//                            selectedTagsSymptomsArrayList.add(symptoms);
//                        }
//                    } else if (tagModel.getTagText().toLowerCase().contains("frequent contractions")) {
//                        if (symptoms.isFrequestContractions()) {
//                            selectedTagsSymptomsArrayList.add(symptoms);
//                        }
//                    } else if (tagModel.getTagText().toLowerCase().contains("3 months")) {
//                        if (symptoms.isStomachPainTill3Months()) {
//                            selectedTagsSymptomsArrayList.add(symptoms);
//                        }
//                    } else if (tagModel.getTagText().toLowerCase().contains("37 weeks")) {
//                        if (symptoms.isLessThan37Weeks()) {
//                            selectedTagsSymptomsArrayList.add(symptoms);
//                        }
//                    } else if (tagModel.getTagText().toLowerCase().contains("blackout")) {
//                        if (symptoms.isBlackOut()) {
//                            selectedTagsSymptomsArrayList.add(symptoms);
//                        }
//                    } else if (tagModel.getTagText().toLowerCase().contains("loss of consiousness")) {
//                        if (symptoms.isLossOfConsiousness()) {
//                            selectedTagsSymptomsArrayList.add(symptoms);
//                        }
//                    } else if (tagModel.getTagText().toLowerCase().contains("breathing")) {
//                        if (symptoms.isBreathingDifficulty()) {
//                            selectedTagsSymptomsArrayList.add(symptoms);
//                        }
//                    } else if (tagModel.getTagText().toLowerCase().contains("chest pain")) {
//                        if (symptoms.isChestPain()) {
//                            selectedTagsSymptomsArrayList.add(symptoms);
//                        }
//                    } else if (tagModel.getTagText().toLowerCase().contains("pain while urinating")) {
//                        if (symptoms.isPainWhileUrinating()) {
//                            selectedTagsSymptomsArrayList.add(symptoms);
//                        }
//                    }
//                }
//            }
//        }
//        createSymptomsObject(selectedTagsSymptomsArrayList);
//
//    }

    private void showQuestions(Symptoms symptoms) {
//        answerMoreQuestions.setVisibility(View.VISIBLE);

        if (symptoms.isFever()) {
            questions.add(new SymptomsQuestions("Do you have Fever?", ""));
        }
        if (symptoms.isWeakness()) {
            questions.add(new SymptomsQuestions("Do you have Weakness?", ""));
        }
        if (symptoms.isBleeding()) {
            questions.add(new SymptomsQuestions("Are you bleeding?", ""));
        }
        if (symptoms.isAbdominalPain()) {
            questions.add(new SymptomsQuestions("Do you have abdominal pain?", ""));
        }
        if (symptoms.isSevereBackPain()) {
            questions.add(new SymptomsQuestions("Do you have severe back pain?", ""));
        }
        if (symptoms.isSevereCramps()) {
            questions.add(new SymptomsQuestions("Do you have severe cramps?", ""));
        }
        if (symptoms.isPregnancyLessThan20Days()) {
            questions.add(new SymptomsQuestions("Is you pregnancy more than 20 days?", ""));
        }
        if (symptoms.isPreviousHistoryOfMiscarriage()) {
            questions.add(new SymptomsQuestions("Do you have previous history of miscarriage?", ""));
        }
        if (symptoms.isAgeLessThan35()) {
            questions.add(new SymptomsQuestions("Is your age less than 35 years?", ""));
        }
        if (symptoms.isPregnancyWeeksMoreThan41()) {
            questions.add(new SymptomsQuestions("Are you pregnant for more than 41 weeks?", ""));
        }
        if (symptoms.isPlacentaDisorder()) {
            questions.add(new SymptomsQuestions("Do you have placental disorder?", ""));
        }
        if (symptoms.isDiabities()) {
            questions.add(new SymptomsQuestions("Do you have diabities?", ""));
        }
        if (symptoms.isKidneyProblem()) {
            questions.add(new SymptomsQuestions("Do you have kidney problem?", ""));
        }
        if (symptoms.isHighBloodPressure()) {
            questions.add(new SymptomsQuestions("Do you have high blood pressure problem?", ""));
        }
        if (symptoms.isFewerMovementsOfBaby()) {
            questions.add(new SymptomsQuestions("Do you feel fewer movements of baby?", ""));
        }
        if (symptoms.isWaterBroken()) {
            questions.add(new SymptomsQuestions("Is your water broken?", ""));
        }
        if (symptoms.isContractionStarted()) {
            questions.add(new SymptomsQuestions("Do the contractions started?", ""));
        }
        if (symptoms.isNausea()) {
            questions.add(new SymptomsQuestions("Do you have nausea?", ""));
        }
        if (symptoms.isVomiting()) {
            questions.add(new SymptomsQuestions("Do you have vomiting?", ""));
        }
        if (symptoms.isSharpAbdominalCramps()) {
            questions.add(new SymptomsQuestions("Do you have sharp abdominal cramps?", ""));
        }
        if (symptoms.isPainOnOneSide()) {
            questions.add(new SymptomsQuestions("Do you have pain on one side?", ""));
        }
        if (symptoms.isDizziness()) {
            questions.add(new SymptomsQuestions("Do you feel dizziness?", ""));
        }
        if (symptoms.isPaintInShoulder()) {
            questions.add(new SymptomsQuestions("Do you have pain in shoulder?", ""));
        }
        if (symptoms.isFeelingSick()) {
            questions.add(new SymptomsQuestions("Are you feeling sick?", ""));
        }
        if (symptoms.isDifficultyInBowlMovement()) {
            questions.add(new SymptomsQuestions("Do you have difficulty in bowl movement?", ""));
        }
        if (symptoms.isLightHeaded()) {
            questions.add(new SymptomsQuestions("Do you have feel light headed?", ""));
        }
        if (symptoms.isCantKeepFoodDown()) {
            questions.add(new SymptomsQuestions("Can't you keep food down?", ""));
        }
        if (symptoms.isWeightLoss()) {
            questions.add(new SymptomsQuestions("Did you lose weight?", ""));
        }
        if (symptoms.isLowBloodPressure()) {
            questions.add(new SymptomsQuestions("Do you have low blood pressure?", ""));
        }
        if (symptoms.isConfusion()) {
            questions.add(new SymptomsQuestions("Do you often confuse?", ""));
        }
        if (symptoms.isJaundice()) {
            questions.add(new SymptomsQuestions("Do you have jaundice?", ""));
        }
        if (symptoms.isHeadache()) {
            questions.add(new SymptomsQuestions("Do you have headache?", ""));
        }
        if (symptoms.isHistoryOfPrematureBirth()) {
            questions.add(new SymptomsQuestions("Do you have history of premature birth?", ""));
        }
        if (symptoms.isVaginalBleeding()) {
            questions.add(new SymptomsQuestions("Do you have vaginal bleeding?", ""));
        }
        if (symptoms.isHarmonalChanges()) {
            questions.add(new SymptomsQuestions("Do you often feel harmonal changes?", ""));
        }
        if (symptoms.isUterusStrectching()) {
            questions.add(new SymptomsQuestions("Is your uterus stretching?", ""));
        }
        if (symptoms.isPlugOfMucusInCervix()) {
            questions.add(new SymptomsQuestions("Plug of mucus in cervix?", ""));
        }
        if (symptoms.isLastMonth()) {
            questions.add(new SymptomsQuestions("Is this your last month of pregnancy?", ""));
        }
        if (symptoms.isAbyDrops()) {
            questions.add(new SymptomsQuestions("Aby drops?", ""));
        }
        if (symptoms.isCervixDilates()) {
            questions.add(new SymptomsQuestions("Cervix Dilates?", ""));
        }
        if (symptoms.isLooseFeelingJoints()) {
            questions.add(new SymptomsQuestions("Loose-feeling joints?", ""));
        }
        if (symptoms.isWeightGainStops()) {
            questions.add(new SymptomsQuestions("Weight gain stopped?", ""));
        }
        if (symptoms.isFatigue()) {
            questions.add(new SymptomsQuestions("Do you feel fatigued?", ""));
        }
        if (symptoms.isVaginalDischargeChanges()) {
            questions.add(new SymptomsQuestions("Vaginal discharge changes color and consistency?", ""));
        }
        if (symptoms.isPregnancyWeeksMoreThan41()) {
            questions.add(new SymptomsQuestions("Are you pregnant for more than 41 weeks?", ""));
        }
        if (symptoms.isFrequestContractions()) {
            questions.add(new SymptomsQuestions("Frequent Contractions?", ""));
        }
        if (symptoms.isStomachPainTill3Months()) {
            questions.add(new SymptomsQuestions("Do you have stomach pain for 3 months?", ""));
        }
        if (symptoms.isLessThan37Weeks()) {
            questions.add(new SymptomsQuestions("Are you pregnant for less than 37 weeks?", ""));
        }
        if (symptoms.isBlackOut()) {
            questions.add(new SymptomsQuestions("Do you often feel like blackout?", ""));
        }
        if (symptoms.isLossOfConsiousness()) {
            questions.add(new SymptomsQuestions("Do you have loss of consciousness?", ""));
        }
        if (symptoms.isBreathingDifficulty()) {
            questions.add(new SymptomsQuestions("Do you feel difficulty while breathing?", ""));
        }
        if (symptoms.isChestPain()) {
            questions.add(new SymptomsQuestions("Do you feel chest pain?", ""));
        }
        if (symptoms.isChestPain()) {
            questions.add(new SymptomsQuestions("Do you feel pain while urinating?", ""));
        }
        adapter = new ViewPagerAdapter(questions, this, this);

        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter((PagerAdapter) adapter);
        viewPager.setPadding(60, 20, 60, 20);
        viewPager.setVisibility(View.VISIBLE);

        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        showHideProgress(false, "");
    }

    private void createSymptomsObject(ArrayList<Symptoms> selectedTagsSymptomsArrayList) {
        Symptoms finalSymptoms = new Symptoms();
        for (Symptoms symptoms : selectedTagsSymptomsArrayList) {
            if (!finalSymptoms.isFever()) {
                if (symptoms.isFever())
                    finalSymptoms.setFever(true);
            }
            if (!finalSymptoms.isBleeding()) {
                if (symptoms.isBleeding())
                    finalSymptoms.setBleeding(true);
            }
            if (!finalSymptoms.isSevereCramps()) {
                if (symptoms.isSevereCramps())
                    finalSymptoms.setSevereCramps(true);
            }
            if (!finalSymptoms.isWeakness()) {
                if (symptoms.isWeakness())
                    finalSymptoms.setWeakness(true);
            }
            if (!finalSymptoms.isAbdominalPain()) {
                if (symptoms.isAbdominalPain())
                    finalSymptoms.setAbdominalPain(true);
            }
            if (!finalSymptoms.isSevereBackPain()) {
                if (symptoms.isSevereBackPain())
                    finalSymptoms.setSevereBackPain(true);
            }
            if (!finalSymptoms.isPregnancyLessThan20Days()) {
                if (symptoms.isPregnancyLessThan20Days())
                    finalSymptoms.setPregnancyLessThan20Days(true);
            }
            if (!finalSymptoms.isPreviousHistoryOfMiscarriage()) {
                if (symptoms.isPreviousHistoryOfMiscarriage())
                    finalSymptoms.setPreviousHistoryOfMiscarriage(true);
            }
            if (!finalSymptoms.isAgeLessThan35()) {
                if (symptoms.isAgeLessThan35())
                    finalSymptoms.setAgeLessThan35(true);
            }
            if (!finalSymptoms.isPregnancyWeeksMoreThan41()) {
                if (symptoms.isPregnancyWeeksMoreThan41())
                    finalSymptoms.setPregnancyWeeksMoreThan41(true);
            }
            if (!finalSymptoms.isPlacentaDisorder()) {
                if (symptoms.isPlacentaDisorder())
                    finalSymptoms.setPlacentaDisorder(true);
            }
            if (!finalSymptoms.isDiabities()) {
                if (symptoms.isDiabities())
                    finalSymptoms.setDiabities(true);
            }
            if (!finalSymptoms.isKidneyProblem()) {
                if (symptoms.isKidneyProblem())
                    finalSymptoms.setKidneyProblem(true);
            }
            if (!finalSymptoms.isHighBloodPressure()) {
                if (symptoms.isHighBloodPressure())
                    finalSymptoms.setHighBloodPressure(true);
            }
            if (!finalSymptoms.isFewerMovementsOfBaby()) {
                if (symptoms.isFewerMovementsOfBaby())
                    finalSymptoms.setFewerMovementsOfBaby(true);
            }
            if (!finalSymptoms.isWaterBroken()) {
                if (symptoms.isWaterBroken())
                    finalSymptoms.setWaterBroken(true);
            }
            if (!finalSymptoms.isContractionStarted()) {
                if (symptoms.isContractionStarted())
                    finalSymptoms.setContractionStarted(true);
            }
            if (!finalSymptoms.isMayHaveTwins()) {
                if (symptoms.isMayHaveTwins())
                    finalSymptoms.setMayHaveTwins(true);
            }
            if (!finalSymptoms.isNausea()) {
                if (symptoms.isNausea())
                    finalSymptoms.setNausea(true);
            }
            if (!finalSymptoms.isVomiting()) {
                if (symptoms.isVomiting())
                    finalSymptoms.setVomiting(true);
            }
            if (!finalSymptoms.isSharpAbdominalCramps()) {
                if (symptoms.isSharpAbdominalCramps())
                    finalSymptoms.setSharpAbdominalCramps(true);
            }
            if (!finalSymptoms.isPainOnOneSide()) {
                if (symptoms.isPainOnOneSide())
                    finalSymptoms.setPainOnOneSide(true);
            }
            if (!finalSymptoms.isDizziness()) {
                if (symptoms.isDizziness())
                    finalSymptoms.setDizziness(true);
            }
            if (!finalSymptoms.isPaintInShoulder()) {
                if (symptoms.isPaintInShoulder())
                    finalSymptoms.setPaintInShoulder(true);
            }
            if (!finalSymptoms.isDifficultyInBowlMovement()) {
                if (symptoms.isDifficultyInBowlMovement())
                    finalSymptoms.setDifficultyInBowlMovement(true);
            }
            if (!finalSymptoms.isLightHeaded()) {
                if (symptoms.isLightHeaded())
                    finalSymptoms.setLightHeaded(true);
            }
            if (!finalSymptoms.isFeelingSick()) {
                if (symptoms.isFeelingSick())
                    finalSymptoms.setFeelingSick(true);
            }
            if (!finalSymptoms.isCantKeepFoodDown()) {
                if (symptoms.isCantKeepFoodDown())
                    finalSymptoms.setCantKeepFoodDown(true);
            }
            if (!finalSymptoms.isWeightLoss()) {
                if (symptoms.isWeightLoss())
                    finalSymptoms.setWeightLoss(true);
            }
            if (!finalSymptoms.isLowBloodPressure()) {
                if (symptoms.isLowBloodPressure())
                    finalSymptoms.setLowBloodPressure(true);
            }
            if (!finalSymptoms.isConfusion()) {
                if (symptoms.isConfusion())
                    finalSymptoms.setConfusion(true);
            }
            if (!finalSymptoms.isJaundice()) {
                if (symptoms.isJaundice())
                    finalSymptoms.setJaundice(true);
            }
            if (!finalSymptoms.isHeadache()) {
                if (symptoms.isHeadache())
                    finalSymptoms.setHeadache(true);
            }
            if (!finalSymptoms.isHistoryOfPrematureBirth()) {
                if (symptoms.isHistoryOfPrematureBirth())
                    finalSymptoms.setHistoryOfPrematureBirth(true);
            }
            if (!finalSymptoms.isVaginalBleeding()) {
                if (symptoms.isVaginalBleeding())
                    finalSymptoms.setVaginalBleeding(true);
            }
            if (!finalSymptoms.isHarmonalChanges()) {
                if (symptoms.isHarmonalChanges())
                    finalSymptoms.setHarmonalChanges(true);
            }
            if (!finalSymptoms.isUterusStrectching()) {
                if (symptoms.isUterusStrectching())
                    finalSymptoms.setUterusStrectching(true);
            }
            if (!finalSymptoms.isPlugOfMucusInCervix()) {
                if (symptoms.isPlugOfMucusInCervix())
                    finalSymptoms.setPlugOfMucusInCervix(true);
            }
            if (!finalSymptoms.isLastMonth()) {
                if (symptoms.isLastMonth())
                    finalSymptoms.setLastMonth(true);
            }
            if (!finalSymptoms.isAbyDrops()) {
                if (symptoms.isAbyDrops())
                    finalSymptoms.setAbyDrops(true);
            }
            if (!finalSymptoms.isCervixDilates()) {
                if (symptoms.isCervixDilates())
                    finalSymptoms.setCervixDilates(true);
            }
            if (!finalSymptoms.isLooseFeelingJoints()) {
                if (symptoms.isLooseFeelingJoints())
                    finalSymptoms.setLooseFeelingJoints(true);
            }
            if (!finalSymptoms.isWeightGainStops()) {
                if (symptoms.isWeightGainStops())
                    finalSymptoms.setWeightGainStops(true);
            }
            if (!finalSymptoms.isFatigue()) {
                if (symptoms.isFatigue())
                    finalSymptoms.setFatigue(true);
            }
            if (!finalSymptoms.isVaginalDischargeChanges()) {
                if (symptoms.isVaginalDischargeChanges())
                    finalSymptoms.setVaginalDischargeChanges(true);
            }
            if (!finalSymptoms.isFrequestContractions()) {
                if (symptoms.isFrequestContractions())
                    finalSymptoms.setFrequestContractions(true);
            }
            if (!finalSymptoms.isStomachPainTill3Months()) {
                if (symptoms.isStomachPainTill3Months())
                    finalSymptoms.setStomachPainTill3Months(true);
            }
            if (!finalSymptoms.isLessThan37Weeks()) {
                if (symptoms.isLessThan37Weeks())
                    finalSymptoms.setLessThan37Weeks(true);
            }
            if (!finalSymptoms.isBlackOut()) {
                if (symptoms.isBlackOut())
                    finalSymptoms.setBlackOut(true);
            }
            if (!finalSymptoms.isLossOfConsiousness()) {
                if (symptoms.isLossOfConsiousness())
                    finalSymptoms.setLossOfConsiousness(true);
            }
            if (!finalSymptoms.isBreathingDifficulty()) {
                if (symptoms.isBreathingDifficulty())
                    finalSymptoms.setBreathingDifficulty(true);
            }
            if (!finalSymptoms.isChestPain()) {
                if (symptoms.isChestPain())
                    finalSymptoms.setChestPain(true);
            }
            if (!finalSymptoms.isPainWhileUrinating()) {
                if (symptoms.isPainWhileUrinating())
                    finalSymptoms.setPainWhileUrinating(true);
            }
        }
        showQuestions(finalSymptoms);
    }

    @Override
    public void symptomResponse(int position, String response) {
        questions.get(position).questionStatus = response;
        viewPager.setCurrentItem(position + 1);
    }
}
