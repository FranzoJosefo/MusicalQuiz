package com.example.android.musicalquiz;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ArrayList<View> questionOneCheckBoxList = new ArrayList<>();
    ArrayList<View> questionTwoCheckBoxList = new ArrayList<>();
    ArrayList<EditText> editTextQuestionList = new ArrayList<>();
    ArrayList<String> editTextAnswersList = new ArrayList<>();
    List<View> radioGroupList = new ArrayList<>();
    List<View> selectedRadioButtonList = new ArrayList<>();
    ArrayList<ArrayList<View>> checkBoxQuestionsList = new ArrayList<>();
    List<View> modifiedQuestionLinearLayouts = new ArrayList<>();
    private int score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_main);
        generateLists();
    }

    private void generateLists() {
        fillRadioGroupListRecursive((ViewGroup) findViewById(R.id.mainParent));
        fillCheckBoxList();
        fillTextEditList();
    }

    /**
     * Gets all the RadioGroups in all nested levels recursively and stores them in a View Array.
     *
     * @param parent is the parent view containing all needed childs.
     */
    private void fillRadioGroupListRecursive(ViewGroup parent) {
        for (int i = 0; i < parent.getChildCount(); i++) {
            final View child = parent.getChildAt(i);
            if (child instanceof ViewGroup && !(child instanceof RadioGroup)) {
                fillRadioGroupListRecursive((ViewGroup) child);
            } else {
                if (child instanceof RadioGroup)
                    radioGroupList.add(child);
            }
        }
    }

    private void fillTextEditList() {
        editTextQuestionList.add((EditText) findViewById(R.id.richardwagner));
        editTextAnswersList.add("richard");
        editTextQuestionList.add((EditText) findViewById(R.id.chalumeau));
        editTextAnswersList.add("chalumeau");
    }

    private void fillCheckBoxList() {
        questionOneCheckBoxList.add(findViewById(R.id.guitar_c));
        questionOneCheckBoxList.add(findViewById(R.id.djembe_w));
        questionOneCheckBoxList.add(findViewById(R.id.violin_c));
        questionOneCheckBoxList.add(findViewById(R.id.piano_c));
        questionOneCheckBoxList.add(findViewById(R.id.stick_c));
        questionOneCheckBoxList.add(findViewById(R.id.cymbal_w));
        checkBoxQuestionsList.add(questionOneCheckBoxList);
        questionTwoCheckBoxList.add(findViewById(R.id.guthrie_c));
        questionTwoCheckBoxList.add(findViewById(R.id.vinnie_w));
        questionTwoCheckBoxList.add(findViewById(R.id.menza_w));
        questionTwoCheckBoxList.add(findViewById(R.id.petrucci_c));
        questionTwoCheckBoxList.add(findViewById(R.id.paco_c));
        questionTwoCheckBoxList.add(findViewById(R.id.ponty_w));
        questionTwoCheckBoxList.add(findViewById(R.id.malsteem_c));
        questionTwoCheckBoxList.add(findViewById(R.id.giardino_c));
        checkBoxQuestionsList.add(questionTwoCheckBoxList);
    }

    public void submitResults(View view) {
        //If it was able to check all the answers it display results
        if (allQuestionsAnswered()) {
            validateRadioButtons();
            validateCheckBoxes();
            validateEditText();
            setEditTextState(false);
            setRadioButtonsState(false);
            setCheckBoxesState(false);
            disableSubmitButton();
            displayResults();
        } else {// If validateAnswers returned FALSE, answers were missing!.
            Toast.makeText(getApplicationContext(), "You are missing answers!", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean allQuestionsAnswered() {
        if (fillSelectedRadioButtonList() && allCheckBoxesSelected() && allEditTextAnswered()) {
            return true;
        }
        return false;
    }

    /**
     * Fills the userAnswersList.
     * Gets the ID of the selected radioButton in the current radioGroup AKA user's choice.
     * If the ID is - 1 then one of the RadioButtons is not checked.
     * The loop is broken and false is returned via flag.
     */

    private boolean fillSelectedRadioButtonList() {
        selectedRadioButtonList.clear();
        Boolean flag = true;
        int count = radioGroupList.size();
        for (int i = 0; i < count && flag != false; i++) {
            int currentRadioButtonID = ((RadioGroup) radioGroupList.get(i)).getCheckedRadioButtonId();
            if (currentRadioButtonID == -1) {
                //If the ID is - 1 then one of the RadioButtons is not checked.
                flag = false;
            } else {
                selectedRadioButtonList.add(findViewById(currentRadioButtonID));
            }
        }
        return flag;
    }

    private boolean allCheckBoxesSelected() {
        for (ArrayList<View> currentQuestion : checkBoxQuestionsList) {
            boolean selected = false;
            for (View currentCheckBoxView : currentQuestion) {
                if (((CheckBox) currentCheckBoxView).isChecked()) {
                    selected = true;
                }
            }
            if (selected == false) {
                return false;
            }

        }
        return true;
    }

    private boolean allEditTextAnswered() {
        for (int i = 0; i < editTextQuestionList.size(); i++) {
            EditText currentView = editTextQuestionList.get(i);
            if (currentView.getText().equals("Enter Answer Here") || currentView.getText().equals("")) {
                return false;
            }
        }
        return true;
    }

    private void validateEditText() {
        for (int i = 0; i < editTextQuestionList.size(); i++) {
            EditText currentView = editTextQuestionList.get(i);
            String currentAnswer = editTextAnswersList.get(i);
            String currentUserAnswer = currentView.getText().toString().toLowerCase();
            if (currentAnswer.equals(currentUserAnswer)) {
                currentView.setBackgroundResource(R.drawable.edittext_correct);
                score++;
            } else {
                currentView.setBackgroundResource(R.drawable.edittext_incorrect);
            }

        }
    }

    private void validateCheckBoxes() {
        for (ArrayList<View> currentQuestion : checkBoxQuestionsList) {
            boolean allAnsweredCorrectly = true;
            for (View currentCheckBoxView : currentQuestion) {
                String currentCheckBoxID = getResources().getResourceEntryName(currentCheckBoxView.getId());
                if (((CheckBox) currentCheckBoxView).isChecked()) {
                    if (currentCheckBoxID.substring(currentCheckBoxID.length() - 1).equals("c")) {
                        currentCheckBoxView.setBackgroundResource(R.drawable.checkbox_correct);
                    } else {
                        currentCheckBoxView.setBackgroundResource(R.drawable.checkbox_incorrect);
                    }
                } else {
                    if (currentCheckBoxID.substring(currentCheckBoxID.length() - 1).equals("c")) {
                        allAnsweredCorrectly = false;
                    }
                }
            }
            CheckBox checkBox = (CheckBox) currentQuestion.get(0);
            if (allAnsweredCorrectly) {
                score++;
                LinearLayout questionLinearLayout = (LinearLayout) (checkBox.getParent()).getParent();
                questionLinearLayout.setBackgroundResource(R.drawable.checkbox_correct);
                modifiedQuestionLinearLayouts.add(questionLinearLayout);
            } else {
                LinearLayout questionLinearLayout = (LinearLayout) (checkBox.getParent()).getParent();
                questionLinearLayout.setBackgroundResource(R.drawable.checkbox_incorrect);
                modifiedQuestionLinearLayouts.add(questionLinearLayout);
            }
        }
    }


    private void validateRadioButtons() {
        for (View currentRadioButtonView : selectedRadioButtonList) {
            String currentRadioButtonID = getResources().getResourceEntryName(currentRadioButtonView.getId());
            if (currentRadioButtonID.substring(currentRadioButtonID.length() - 1).equals("c")) {
                score++;
                currentRadioButtonView.setBackgroundResource(R.drawable.button_correct);
            } else {
                currentRadioButtonView.setBackgroundResource(R.drawable.button_incorrect);
            }
        }
    }

    private void displayResults() {
        TextView resultTextView = (TextView) findViewById(R.id.results);
        resultTextView.setText("Your final score is!: " + calculatePercentageScore() + "%");
        resultTextView.setVisibility(View.VISIBLE);
        resultTextView.requestFocus();
        Toast.makeText(getApplicationContext(), "You've completed the quiz! Your score is: " + score, Toast.LENGTH_SHORT).show();
        score = 0;
    }

    private int calculatePercentageScore() {
        int maxScore = radioGroupList.size() + checkBoxQuestionsList.size();
        int percentageScore = (score * 100) / maxScore;
        return percentageScore;
    }

    public void resetApp(View view) {
        enableRadioButtons();
        enableCheckBoxes();
        enableEditText();
        enableSubmitButton();
        checkBoxQuestionsList.clear();
        modifiedQuestionLinearLayouts.clear();
        selectedRadioButtonList.clear();
        radioGroupList.clear();
        editTextQuestionList.clear();
        resetResultTextView();
        generateLists();
        ScrollView scrollView = (ScrollView) findViewById(R.id.mainParent);
        scrollView.fullScroll(ScrollView.FOCUS_UP);
    }

    private void disableSubmitButton() {
        Button submitButton = (Button) findViewById(R.id.submitButton);
        submitButton.setEnabled(false);
        submitButton.setBackgroundResource(R.drawable.button_grayedout);
        submitButton.setTextColor(Color.DKGRAY);
    }

    private void setEditTextState(boolean state) {
        for (EditText currentQuestion : editTextQuestionList) {
            currentQuestion.setEnabled(state);
        }
    }

    private void enableSubmitButton() {
        Button submitButton = (Button) findViewById(R.id.submitButton);
        submitButton.setEnabled(true);
        submitButton.setBackgroundResource(R.drawable.shadow_button);
        submitButton.setTextColor(Color.BLACK);
    }

    private void resetResultTextView() {
        TextView resultTextView = (TextView) findViewById(R.id.results);
        resultTextView.setText("");
        resultTextView.setVisibility(View.GONE);
    }

    private void enableRadioButtons() {
        for (View currentRadioButton : selectedRadioButtonList) {
            currentRadioButton.setBackgroundResource(R.drawable.shadow_radiobutton);
        }
        for (View currentView : radioGroupList) {
            RadioGroup currentRadioGroup = (RadioGroup) currentView;
            currentRadioGroup.clearCheck();
            int childCount = currentRadioGroup.getChildCount();
            for (int z = 0; z < childCount; z++) {
                currentRadioGroup.getChildAt(z).setEnabled(true);
            }
        }
    }

    public void setTextBox(View view) {
        EditText textBox = (EditText) view;
        String text = textBox.getText().toString();
        if (text.equals("Enter Answer Here")) {
            textBox.setText("");
            textBox.setTextColor(Color.BLACK);
            textBox.setEnabled(true);
        }
    }

    private void setRadioButtonsState(boolean state) {
        for (View currentView : radioGroupList) {
            RadioGroup currentRadioGroup = (RadioGroup) currentView;
            int childCount = currentRadioGroup.getChildCount();
            for (int z = 0; z < childCount; z++) {
                currentRadioGroup.getChildAt(z).setEnabled(state);
            }
        }
    }

    private void setCheckBoxesState(boolean state) {
        for (ArrayList<View> currentQuestion : checkBoxQuestionsList) {
            for (View currentCheckBoxView : currentQuestion) {
                currentCheckBoxView.setEnabled(state);
            }
        }
    }

    private void enableEditText() {
        for (EditText currentQuestion : editTextQuestionList) {
            currentQuestion.setEnabled(true);
            currentQuestion.setText("Enter Answer Here");
            currentQuestion.setTextColor(getResources().getColor(android.R.color.darker_gray));
            currentQuestion.setBackgroundResource(R.drawable.edittext_background);
        }
    }

    private void enableCheckBoxes() {
        for (View currentLayout : modifiedQuestionLinearLayouts) {
            currentLayout.setBackground(null);
        }
        for (ArrayList<View> currentQuestion : checkBoxQuestionsList) {
            for (View currentCheckBoxView : currentQuestion) {
                currentCheckBoxView.setEnabled(true);
                currentCheckBoxView.setBackgroundResource(R.drawable.shadowcheckbox);
                ((CheckBox) currentCheckBoxView).setChecked(false);
            }
        }
    }
}
