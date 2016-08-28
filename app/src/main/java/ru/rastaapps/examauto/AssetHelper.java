package ru.rastaapps.examauto;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by MigeL on 28.08.2016.
 */
public class AssetHelper {

    Context ctx;
    String locale;

    public AssetHelper(Context ctx, String _locale) {
        this.ctx = ctx;
        this.locale = _locale;
    }

    public String getQuestion(int _ticket, int _question){
        InputStream is;
        String file = locale + "/" + _ticket + "/" + _question + "/v.txt";
        byte[] buffer = null;

        try {
            is = ctx.getAssets().open(file);
            int size = is.available();
            buffer = new byte[size];
            is.read(buffer);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String str = new String(buffer);
        return str;
    }

    public String getAnswer(int _ticket, int _question, int ans){
        String file = locale + "/" + _ticket + "/" + _question + "/ans/" + ans + ".txt";



        InputStream is;

        byte[] buffer = null;

        try {
            is = ctx.getAssets().open(file);
            int size = is.available();
            buffer = new byte[size];
            is.read(buffer);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String str = new String(buffer);
        return str;
    }

    public int getCountAnswers(int _question, int _ticket){

        int files_count = 0;
        try {
            AssetManager am = ctx.getAssets();
            files_count = am.list(locale + "/" + _ticket + "/" + _question + "/ans").length;
        } catch (IOException e){
            e.printStackTrace();
        }
        return files_count;
    }

    public Drawable getImg(int _ticket, int _question){
        Drawable d;
        try {
            InputStream ims = ctx.getAssets().open("ru/" + _ticket + "/" + _question + "/" + "img.jpg");

            d = Drawable.createFromStream(ims, null);

        }
        catch(IOException ex) {
            InputStream ims = null;
            try {
                ims = ctx.getAssets().open("img_def.png");
            } catch (IOException e) {
                e.printStackTrace();
            }
            d = Drawable.createFromStream(ims, null);


        }
        return d;
    }
}
