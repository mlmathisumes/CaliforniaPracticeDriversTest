package com.example.californiapracticedriverstest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {



    private RadioGroup radioGroupLayout;
    private RadioGroup radioGroup;
    private RadioButton radioButton1;
    private RadioButton radioButton2;
    private RadioButton radioButton3;
    private LinearLayout checkBoxLayout;
    private CheckBox checkBox1;
    private CheckBox checkBox2;
    private CheckBox checkBox3;
    private EditText editText;

    private LinearLayout editTextLayout;
    private Button nextButton;
    private TextView questionTextView;
    private TextView questionCount;

    private QuestionBank questionBank;
    private int currentQuestionNum = -1;
    private double score;
    private String lastQuestionType = "";
    private String selectedRadioButton = "";
    private boolean checkedQuestion;
    private ArrayList<String> answerList = new ArrayList<String>();


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            currentQuestionNum = savedInstanceState.getInt("currentQuestionN");
        }

        radioGroupLayout = (RadioGroup) findViewById(R.id.radio_group);
        radioGroup = (RadioGroup) findViewById(R.id.radio_group);
        radioButton1 = (RadioButton) findViewById(R.id.radioButton1);
        radioButton1.setOnCheckedChangeListener(new rListener());
        radioButton2 = (RadioButton) findViewById(R.id.radioButton2);
        radioButton2.setOnCheckedChangeListener(new rListener());
        radioButton3 = (RadioButton) findViewById(R.id.radioButton3);
        radioButton3.setOnCheckedChangeListener(new rListener());

        checkBoxLayout = (LinearLayout) findViewById(R.id.checkbox_questions);
        checkBox1 = (CheckBox) findViewById(R.id.checkbox1);
        checkBox1.setOnCheckedChangeListener(new mListener());
        checkBox2 = (CheckBox) findViewById(R.id.checkbox2);
        checkBox2.setOnCheckedChangeListener(new mListener());
        checkBox3 = (CheckBox) findViewById(R.id.checkbox3);
        checkBox3.setOnCheckedChangeListener(new mListener());

        editTextLayout = (LinearLayout) findViewById(R.id.editTextLayout);
        editText = (EditText) findViewById(R.id.inputEditText);

        nextButton = findViewById(R.id.nextButton);

        questionTextView = findViewById(R.id.questionTxtView);
        questionCount = (TextView) findViewById(R.id.questionCount);

        questionBank = new QuestionBank();
        startQuiz();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putDouble("score", score);
        outState.putInt("currentQuestionN", currentQuestionNum);
        super.onSaveInstanceState(outState);
    }

    private void updateRadioQuestions() {
        // Display RadioGroup Layout with the current question
        radioGroupLayout.setVisibility(View.VISIBLE);
        questionTextView.setText(questionBank.getQuestion(currentQuestionNum));
        radioButton1.setText(questionBank.getChoice1(currentQuestionNum));
        radioButton2.setText(questionBank.getChoice2(currentQuestionNum));
        radioButton3.setText(questionBank.getChoice3(currentQuestionNum));

    }

    private void updateCheckboxQuestions() {
        // Display Checkbox Layout with the current question
        checkBoxLayout.setVisibility(View.VISIBLE);
        // Display current question in TextView
        questionTextView.setText(questionBank.getQuestion(currentQuestionNum));
        // Display possible answers in checkboxes
        checkBox1.setText(questionBank.getChoice1(currentQuestionNum));
        checkBox2.setText(questionBank.getChoice2(currentQuestionNum));
        checkBox3.setText(questionBank.getChoice3(currentQuestionNum));
    }


    private void updateEditTextQuestion() {
        // Display EditText Layout with the current question
        editTextLayout.setVisibility(View.VISIBLE);
        questionTextView.setText(questionBank.getQuestion(currentQuestionNum));
    }

    public void nextQuestion(View view) {
        currentQuestionNum++;
        if (currentQuestionNum != 0 && currentQuestionNum < questionBank.getQuestionSize()) {
            checkLastQuestion();
        }
        if (currentQuestionNum <= questionBank.getQuestionSize() - 1) {
            clearLayout();
            if (currentQuestionNum == questionBank.getQuestionSize() - 1) {
                nextButton.setText(R.string.submit);
            } else {
                nextButton.setText(R.string.next);
            }

            // Display score in TextView
            int count = currentQuestionNum + 1;
            questionCount.setText(count + "/" + questionBank.getQuestionSize());

            // Display current question in TextView
            questionTextView.setText(questionBank.getQuestion(currentQuestionNum));

            // Update Layout base on question type
            updateLayout();
        } else if (nextButton.getText().equals("RESET")) {
            clearLayout();
            currentQuestionNum = 0;
            lastQuestionType = "";
            score = 0;
            startQuiz();
        } else if (currentQuestionNum == questionBank.getQuestionSize()) {
            nextButton.setText(R.string.reset);
            int count = questionBank.getQuestionSize();
            score = score / count * 100;
            // Display final score in a Toast message
            Toast.makeText(getApplicationContext(), "You scored a " + String.format("%.2f", score) + "%", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Check to see if the previous answered selected was correct. Display toast stating if the answered entered
     * is correct or incorrect.
     *
     * @return
     */
    private void checkLastQuestion() {
        checkedQuestion = true;
        if (lastQuestionType.equals("checkBox")) {
            String[] checkAnswer = questionBank.getCorrectAnswer(currentQuestionNum - 1).split("-");
            if (answerList.isEmpty()) {
                checkedQuestion = false;
            }
            while (checkedQuestion) {
                for (String ans : checkAnswer) {
                    if (!answerList.contains(ans)) {
                        checkedQuestion = false;
                        break;
                    }
                }
                break;
            }
        } else if (lastQuestionType.equals("radioButton")) {
            String radioAnswer = questionBank.getCorrectAnswer(currentQuestionNum - 1);
            if (!selectedRadioButton.equals(radioAnswer) || selectedRadioButton.equals("")) {
                checkedQuestion = false;
            }
        } else if (lastQuestionType.equals("editText")) {
            if (!editText.getText().toString().equals(questionBank.getCorrectAnswer(currentQuestionNum - 1))) {
                checkedQuestion = false;
            }
        }

        if (checkedQuestion) {
            Toast.makeText(getApplicationContext(), getString(R.string.correct), Toast.LENGTH_SHORT).show();
            score++;
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.incorrect), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Clear and Unselected all choices. Set checkbox Layout, Radio Group Layout and EditText Layout to invisible
     *
     * @return
     */
    public void clearLayout() {
        checkBoxLayout.setVisibility(View.INVISIBLE);
        checkBox1.setOnClickListener(null);
        checkBox1.setChecked(false);
        checkBox1.setOnCheckedChangeListener(new mListener());

        checkBox2.setOnCheckedChangeListener(null);
        checkBox2.setChecked(false);
        checkBox2.setOnCheckedChangeListener(new mListener());

        checkBox3.setOnClickListener(null);
        checkBox3.setChecked(false);
        checkBox3.setOnCheckedChangeListener(new mListener());
        // Set Radio Group Layout to invisible and uncheck all radio buttons
        radioGroupLayout.setVisibility(View.INVISIBLE);
        radioGroupLayout.clearCheck();
        // Set EditText layout to invisible
        editTextLayout.setVisibility(View.INVISIBLE);
        editText.setText("");
    }
    /**
     * Start the quiz and update the Button and TextView widget
     *
     * @return
     */
    public void startQuiz() {
        if (!isQuestionBankEmpty()) {
            questionCount.setText((currentQuestionNum + 1) + "/" + questionBank.getQuestionSize());
            if (!userStartedQuiz()) {
                questionTextView.setText(R.string.start_message);
                nextButton.setText(R.string.start);
                questionCount.setText("0/" + questionBank.getQuestionSize());
            } else if(isLastQuestion()) {
                nextButton.setText(R.string.submit);
                questionTextView.setText(questionBank.getQuestion(currentQuestionNum));
                updateLayout();
            } else {
                questionTextView.setText(questionBank.getQuestion(currentQuestionNum));
                nextButton.setText(R.string.next);
                updateLayout();
            }
            nextButton.setEnabled(true);
        } else {
            // Disable the nextButton if there are no question available
            nextButton.setEnabled(false);
        }
    }

    /**
     * Check if the current question is the last question
     *
     * @return      True if the current question is the last, false if not
     */
    private boolean isLastQuestion(){
        if(currentQuestionNum == questionBank.getQuestionSize() - 1){
            return true;
        }else{
            return false;
        }
    }

    /**
     * Check if the Question bank is empty
     *
     * @return True if the question bank is empty, False if not.
     */
    private boolean isQuestionBankEmpty(){
        if(questionBank.getQuestionSize() > 0 && questionBank != null){
            return false;
        }else{
            return true;
        }
    }

    /**
     * Check if the user has started the quiz
     *
     * @return True if the user has started the quiz, False if not.
     */
    private boolean userStartedQuiz(){
        if(currentQuestionNum < 0){
            return false;
        }else{
            return true;
        }
    }

    /**
     * Update answer choices based on the current question type
     *
     * @return
     */
    public void updateLayout() {
        String type = questionBank.getQuestionType(currentQuestionNum);
        lastQuestionType = type;

        switch (type) {
            case "radioButton":
                updateRadioQuestions();
                break;
            case "checkBox":
                updateCheckboxQuestions();
                break;
            case "editText":
                updateEditTextQuestion();
                break;
        }
    }

    public class mListener implements CompoundButton.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                CheckBox c = (CheckBox) buttonView;
                answerList.add(c.getText().toString());
            }
        }
    }

    public class rListener implements RadioButton.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                selectedRadioButton = buttonView.getText().toString();
            }
        }
    }
}
