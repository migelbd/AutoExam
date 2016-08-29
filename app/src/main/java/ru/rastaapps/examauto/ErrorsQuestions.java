package ru.rastaapps.examauto;

import android.graphics.drawable.Drawable;

/**
 * Created by MigeL on 29.08.2016.
 */
public class ErrorsQuestions {

    Drawable IMG;
    String QUESTION;
    String GOOOD_ANSWER;
    String BAD_ANSWER;
    String COMMENT;
    int NUM_QUESTION;

    public Drawable getIMG() {
        return IMG;
    }

    public void setIMG(Drawable IMG) {
        this.IMG = IMG;
    }

    public String getQUESTION() {
        return QUESTION;
    }

    public void setQUESTION(String QUESTION) {
        this.QUESTION = QUESTION;
    }

    public String getGOOOD_ANSWER() {
        return GOOOD_ANSWER;
    }

    public void setGOOOD_ANSWER(String GOOOD_ANSWER) {
        this.GOOOD_ANSWER = GOOOD_ANSWER;
    }

    public String getBAD_ANSWER() {
        return BAD_ANSWER;
    }

    public void setBAD_ANSWER(String BAD_ANSWER) {
        this.BAD_ANSWER = BAD_ANSWER;
    }

    public String getCOMMENT() {
        return COMMENT;
    }

    public void setCOMMENT(String COMMENT) {
        this.COMMENT = COMMENT;
    }

    public int getNUM_QUESTION() {
        return NUM_QUESTION;
    }

    public void setNUM_QUESTION(int NUM_QUESTION) {
        this.NUM_QUESTION = NUM_QUESTION;
    }
}
