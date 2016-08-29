package ru.rastaapps.examauto;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {


    LinearLayout lnResult, lnErr;
    TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        tvResult = (TextView)findViewById(R.id.tvResult);
        lnResult = (LinearLayout)findViewById(R.id.lnResult);
        lnErr = (LinearLayout)findViewById(R.id.lnErrLay);


        Intent i = getIntent();
        if(i.getBooleanExtra("result", false)){
            tvResult.setText(getResources().getString(R.string.txt_res_winner));
            tvResult.setTextColor(getResources().getColor(R.color.colorGood));



        } else {
            tvResult.setText(getResources().getString(R.string.txt_res_looser));
            tvResult.setTextColor(getResources().getColor(R.color.colorBad));


        }
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView good = new TextView(this);
        TextView bad = new TextView(this);
        TextView skip = new TextView(this);

        good.setText(getResources().getString(R.string.txt_res_good) + " " + i.getIntExtra("good", 0));
        bad.setText(getResources().getString(R.string.txt_res_err) + " " + i.getIntExtra("bad", 0));
        skip.setText(getResources().getString(R.string.txt_res_no_ans) + " " + i.getIntExtra("skip", 0));

        good.setLayoutParams(params);
        bad.setLayoutParams(params);
        skip.setLayoutParams(params);

        good.setGravity(Gravity.CENTER_HORIZONTAL);
        bad.setGravity(Gravity.CENTER_HORIZONTAL);
        skip.setGravity(Gravity.CENTER_HORIZONTAL);

        good.setTextSize(16);
        bad.setTextSize(16);
        skip.setTextSize(16);

        good.setTextColor(getResources().getColor(R.color.colorGood));
        bad.setTextColor(getResources().getColor(R.color.colorBad));
        skip.setTextColor(getResources().getColor(R.color.colorSkip));
        lnResult.addView(good);
        lnResult.addView(bad);
        lnResult.addView(skip);



        FillItemsResult();
    }


    private void FillItemsResult(){


        for(ErrorsQuestions e : Helper.getInstance().getListErrors()){
            View item = getLayoutInflater().inflate(R.layout.item_error_result, null);
            if (e.getIMG() != null) {
                ImageView img = (ImageView)item.findViewById(R.id._img);
                img.setMinimumWidth(getWindowManager().getDefaultDisplay().getWidth());
                img.setMinimumHeight(600);
                img.setImageDrawable(e.getIMG());
            }

            ((TextView)item.findViewById(R.id._tvNumQuestion)).setText(getResources().getString(R.string.txt_question) + " " + e.getNUM_QUESTION());
            ((TextView)item.findViewById(R.id._tvQuestion)).setText(e.getQUESTION());
            ((TextView)item.findViewById(R.id._tvBad)).setText(e.getBAD_ANSWER());
            ((TextView)item.findViewById(R.id._tvGood)).setText(e.getGOOOD_ANSWER());
            ((TextView)item.findViewById(R.id._tvComment)).setText(e.getCOMMENT());
            lnErr.addView(item);
        }
    }
}
