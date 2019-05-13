package com.example.californiapracticedriverstest;

public class QuestionBank {


    private String[] questions = {"You may drive off of the paved roadway to pass another vehicle:",
            "You just sold your vehicle. You must notify the DMV within____ days.",
            "It is illegal for a person 21 years of age or older to drive with a blood alcohol concentration(BAC) that is ___ or higher",
            "When driving in fog, you should use your:",
            "You are approaching a railroad crossing with no warning devices and are unable to see 400 feet down the tracks in one direction. The speed limit is:",
            "When parking your vehicle parallel to the curb on a level street:"
    };
    private String[][] choices = {
            {"If the shoulder is wide enough to accommodate your vehicle", "If the vehicle ahead of you is turning left", "Under no circumstances" },
            {"5", "10", "15"},
            {"0.08%", "0.10%", "0.05%"},
            {"Fog lights only", "High beams", "Low beams"},
            {"N/A"},
            {"Your front wheels must be turned toward the street", "Your wheels must be within 18 inches of the curb", "One of your rear wheels must touch the curb"}
    };
    private String[] correctAnswers = {"Under no circumstances", "5", "0.08%-0.10%", "Low beams", "15 MPH", "Your wheels must be within 18 inches of the curb"};

    private String[] questionType = {"radioButton", "radioButton", "checkBox", "radioButton", "editText", "radioButton"};


    public String getQuestion(int num){
        return questions[num];
    }

    public int getQuestionSize(){
        return  questions.length;
    }

    public String getChoice1(int num){
        return choices[num][0];

    }
    public String getChoice2(int num){
         return choices[num][1];

    }
    public String getChoice3(int num){

        return choices[num][2];

    }

    public String getCorrectAnswer(int num){
        return correctAnswers[num];
    }

    public String getQuestionType(int num){
        return questionType[num];
    }


}