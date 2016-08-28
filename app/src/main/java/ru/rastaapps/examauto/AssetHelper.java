package ru.rastaapps.examauto;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

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

    public int getCountAnswers(int _ticket, int _question){

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
        Drawable d = null;
        try {
            InputStream ims = ctx.getAssets().open("ru/" + _ticket + "/" + _question + "/" + "img.jpg");

            d = Drawable.createFromStream(ims, null);

        }
        catch(IOException ex) {
//            InputStream ims = null;
//            try {
//                ims = ctx.getAssets().open("img_def.png");
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            d = Drawable.createFromStream(ims, null);


        }
        return d;
    }

    public String getComment(int _ticket, int _question){
        String file = locale + "/" + _ticket + "/" + _question + "/n.txt";

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

    public int getGoodAns(int _ticket, int _question){
        switch (_ticket){
            default: return 0;
            case 1: return ctx.getResources().getIntArray(R.array.bl_1)[_question - 1];
            case 2: return ctx.getResources().getIntArray(R.array.bl_2)[_question - 1];
            case 3: return ctx.getResources().getIntArray(R.array.bl_3)[_question - 1];
            case 4: return ctx.getResources().getIntArray(R.array.bl_4)[_question - 1];
            case 5: return ctx.getResources().getIntArray(R.array.bl_5)[_question - 1];
            case 6: return ctx.getResources().getIntArray(R.array.bl_6)[_question - 1];
            case 7: return ctx.getResources().getIntArray(R.array.bl_7)[_question - 1];
            case 8: return ctx.getResources().getIntArray(R.array.bl_8)[_question - 1];
            case 9: return ctx.getResources().getIntArray(R.array.bl_9)[_question - 1];
            case 10: return ctx.getResources().getIntArray(R.array.bl_10)[_question - 1];
            case 11: return ctx.getResources().getIntArray(R.array.bl_11)[_question - 1];
            case 12: return ctx.getResources().getIntArray(R.array.bl_12)[_question - 1];
            case 13: return ctx.getResources().getIntArray(R.array.bl_13)[_question - 1];
            case 14: return ctx.getResources().getIntArray(R.array.bl_14)[_question - 1];
            case 15: return ctx.getResources().getIntArray(R.array.bl_15)[_question - 1];
            case 16: return ctx.getResources().getIntArray(R.array.bl_16)[_question - 1];
            case 17: return ctx.getResources().getIntArray(R.array.bl_17)[_question - 1];
            case 18: return ctx.getResources().getIntArray(R.array.bl_18)[_question - 1];
            case 19: return ctx.getResources().getIntArray(R.array.bl_19)[_question - 1];
            case 20: return ctx.getResources().getIntArray(R.array.bl_20)[_question - 1];
            case 21: return ctx.getResources().getIntArray(R.array.bl_21)[_question - 1];
            case 22: return ctx.getResources().getIntArray(R.array.bl_22)[_question - 1];
            case 23: return ctx.getResources().getIntArray(R.array.bl_23)[_question - 1];
            case 24: return ctx.getResources().getIntArray(R.array.bl_24)[_question - 1];
            case 25: return ctx.getResources().getIntArray(R.array.bl_25)[_question - 1];
            case 26: return ctx.getResources().getIntArray(R.array.bl_26)[_question - 1];
            case 27: return ctx.getResources().getIntArray(R.array.bl_27)[_question - 1];
            case 28: return ctx.getResources().getIntArray(R.array.bl_28)[_question - 1];
            case 29: return ctx.getResources().getIntArray(R.array.bl_29)[_question - 1];
            case 30: return ctx.getResources().getIntArray(R.array.bl_30)[_question - 1];
            case 31: return ctx.getResources().getIntArray(R.array.bl_31)[_question - 1];
            case 32: return ctx.getResources().getIntArray(R.array.bl_32)[_question - 1];
            case 33: return ctx.getResources().getIntArray(R.array.bl_33)[_question - 1];
            case 34: return ctx.getResources().getIntArray(R.array.bl_34)[_question - 1];
            case 35: return ctx.getResources().getIntArray(R.array.bl_35)[_question - 1];
        }
    }


    public ArrayList<String> getListAnswers(int _ticket, int _question){
        ArrayList<String> list = new ArrayList<>();
        int count = getCountAnswers(_ticket, _question);
        for(int i = 0;i<count;i++){
            list.add(i + 1 + ". " + getAnswer(_ticket, _question, i + 1));
        }
        return list;
    }

    public int getTicketRandom(){
        Random r = new Random();
        return r.nextInt(34) + 1;
    }
    public int getQuestRandom(){
        Random r = new Random();
        return r.nextInt(19) + 1;
    }

}
