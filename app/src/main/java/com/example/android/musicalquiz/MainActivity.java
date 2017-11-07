package com.example.android.musicalquiz;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    List<View> radioGroupList = new ArrayList<>();
    List<View> userAnswersList = new ArrayList<>();
    private int score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initRadioGroupListRecursive((ViewGroup) findViewById(R.id.mainParent));
        //radioGroupList = createRadioGroupList();
    }

    private void initRadioGroupListRecursive(ViewGroup parent) {
        for (int i = 0; i < parent.getChildCount(); i++) {
            final View child = parent.getChildAt(i);
            if (child instanceof ViewGroup && !(child instanceof RadioGroup)) {
                initRadioGroupListRecursive((ViewGroup) child);
            } else {
                if (child instanceof RadioGroup)
                    radioGroupList.add(child);
            }
        }
    }

    /*private List<View> createRadioGroupList() {
        List<View> radioGroupList = new ArrayList<>();
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearViewGroup);
        int count = linearLayout.getChildCount();
        for (int i = 0; i < count; i++) {
            View o = linearLayout.getChildAt(i);
            if (o instanceof RadioGroup) {
                radioGroupList.add(o);
            }
        }
        return radioGroupList;
    }*/

    private boolean verifyAnswers(List<View> radioGroupList) {
        boolean flag = true;
        int count = radioGroupList.size();
        for (int i = 0; i < count && flag != false; i++) {
            //Gets the ID of the selected radioButton in the current radioGroup AKA user's choice.
            int currentRadioButtonID = ((RadioGroup) radioGroupList.get(i)).getCheckedRadioButtonId();
            //The one below crashed.
            //String currentRadioButtonName = currentRadioButtonView.getTag().toString();
            if (currentRadioButtonID == -1) {
                //If the ID is - 1 then one of the RadioButtons is not checked.
                flag = false;
            } else {
                calculateScore(currentRadioButtonID);
                //stores the view to pass to the highlights methods.
                RadioButton currentRadioButtonView = (RadioButton) findViewById(currentRadioButtonID);
                //While checking correct answers it also creates a list with the users answers to be used later on in resetApp()
                userAnswersList.add(currentRadioButtonView);
            }
        }
        return flag;
    }

    /*private boolean checkCorrectAnswers(List<View> radioGroupList) {
        boolean flag = true;
        int count = radioGroupList.size();
        for (int i = 0; i < count && flag != false; i++) {
            int userCurrentAnswerID = ((RadioGroup) radioGroupList.get(i)).getCheckedRadioButtonId();
            RadioButton currentRadioButtonView = (RadioButton) findViewById(userCurrentAnswerID);
            if (userCurrentAnswerID == -1) {
                flag = false;
            } else {
                for (int currentCorrectAnswer : correctAnswerList) {
                    if (userCurrentAnswerID == currentCorrectAnswer) {
                        score++;
                        highlightCorrectAnswer(currentRadioButtonView);
                        break;
                        //todo replace break with flag?
                    } else {
                        highlightWrongAnswer(currentRadioButtonView);
                    }
                }
                //While checking correct answers it also creates a list with the users answers to be used later on.
                userAnswersList.add(currentRadioButtonView);
            }

        }
        return flag;
    }*/

    public void submitResults(View view) {
        //If it was able to check all the answers it display results
        //resetApp(view); (If you want to enable choosing different answers without using RESET button).
        if (verifyAnswers(radioGroupList)) {
            //Only disable if all Radio Buttons were selected.
            highlightRadioButtons();
            disableRadioButtons();
            displayResults();
        } else {// If checkCorrectAnswers returned FALSE, something went wrong!.
            Toast.makeText(getApplicationContext(), "You are missing answers!", Toast.LENGTH_SHORT).show();
        }
    }

    private void highlightRadioButtons() {
        for (View currentRadioButtonView : userAnswersList) {
            String currentRadioButtonName = getResources().getResourceEntryName(currentRadioButtonView.getId());
            if (currentRadioButtonName.substring(currentRadioButtonName.length() - 1).equals("c")) {
                currentRadioButtonView.setBackgroundColor(Color.parseColor("#ad2fe10b"));
            } else {
                currentRadioButtonView.setBackgroundColor(Color.parseColor("#ade10b10"));
            }
        }
    }

    private void disableRadioButtons() {
        for (View currentView : radioGroupList) {
            RadioGroup currentRadioGroup = (RadioGroup) currentView;
            int childCount = currentRadioGroup.getChildCount();
            for (int z = 0; z < childCount; z++) {
                currentRadioGroup.getChildAt(z).setEnabled(false);
            }
        }
    }

    private void enableRadioButtons() {
        for (View currentView : radioGroupList) {
            RadioGroup currentRadioGroup = (RadioGroup) currentView;
            int childCount = currentRadioGroup.getChildCount();
            for (int z = 0; z < childCount; z++) {
                currentRadioGroup.getChildAt(z).setEnabled(true);
            }
        }
    }

    private void displayResults() {
        TextView resultTextView = (TextView) findViewById(R.id.results);
        resultTextView.setText("Your final score is!: " + calculatePercentageScore() + "%");
        score = 0;
    }

    private int calculatePercentageScore() {
        int maxScore = radioGroupList.size();
        int percentageScore = (score * 100) / maxScore;
        return percentageScore;
    }

    public void resetApp(View view) {
        for (View currentRadioButton : userAnswersList) {
            currentRadioButton.setBackgroundColor(Color.parseColor("#FAFAFA"));
        }
        for (View currentView : radioGroupList) {
            RadioGroup currentRadioGroup = (RadioGroup) currentView;
            currentRadioGroup.clearCheck();
        }
        userAnswersList.clear();//empties the answers list to re-use from scratch the next time!
        enableRadioButtons();
        TextView resultTextView = (TextView) findViewById(R.id.results);
        resultTextView.setText("");
    }

    private void calculateScore(int radioButtonID) {
        //Gets the Resource Entry name "android:id="@+id/string" of the currentRadioButton
        String currentRadioButtonName = getResources().getResourceEntryName(radioButtonID);
        // ID names have a c at the end when they are correct, if else they won't have name at all.
        if (currentRadioButtonName.substring(currentRadioButtonName.length() - 1).equals("c")) {
            //I didn't made any method cause its too simple to count score!
            score++;
        }
    }

}
