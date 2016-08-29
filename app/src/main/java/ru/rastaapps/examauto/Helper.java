package ru.rastaapps.examauto;

import android.app.Application;

import java.util.ArrayList;

/**
 * Created by MigeL on 28.08.2016.
 */
public class Helper extends Application {
    private static Helper singleton;
    public static Helper getInstance(){
        return singleton;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;
    }



    //Переменные
    private int TICKET_NUMBER;
    private boolean SECRET_CODE;
    private ArrayList<ErrorsQuestions> listErrors;

    public int getTICKET_NUMBER() {
        return TICKET_NUMBER;
    }

    public void setTICKET_NUMBER(int TICKET_NUMBER) {
        this.TICKET_NUMBER = TICKET_NUMBER;
    }

    public boolean isSECRET_CODE() {
        return SECRET_CODE;
    }

    public void setSECRET_CODE(boolean SECRET_CODE) {
        this.SECRET_CODE = SECRET_CODE;
    }

    public ArrayList<ErrorsQuestions> getListErrors() {
        return listErrors;
    }

    public void setListErrors(ArrayList<ErrorsQuestions> listErrors) {
        this.listErrors = listErrors;
    }
}
