package ru.rastaapps.examauto;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ExamActivity extends AppCompatActivity {

    private static final int CODE_GOOD = 100;
    private static final int CODE_BAD = 200;
    private static final int CODE_SKIP = 300;
    private final String TAG = "ExamActivity";
    AssetHelper ahelp;
    TextView tvQuestion, txtGood, txtSkip, txtBad;

    ListView listAnswers;
    RelativeLayout ImgLay;
    Spinner spinner;
//    LinearLayout BtnsLay, BtnsLay_2;
    private ArrayList<String> arrayAns;
    private int good_answers_count = 0;
    private int skip_answers_count = 0;
    private int bad_answers_count = 0;

    private boolean MODE_MIX = false;
    private int QUESTION_COUNT = 1;
    private int QUESTION_NUMBER = 1;
    private int TICKET_NUMBER = 1;

    private int[] TICKETS;
    private int[] QUESTIONS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);
        TICKET_NUMBER = Helper.getInstance().getTICKET_NUMBER();
        ahelp = new AssetHelper(this, "ru");
        tvQuestion = (TextView)findViewById(R.id.tvQuestion);
       spinner = (Spinner)findViewById(R.id.spin_questions);
        Intent intent = getIntent();
        MODE_MIX = intent.getBooleanExtra("mode", false);
        if(MODE_MIX){

            for(int i = 0;i<19;i++){
                TICKETS[i] = ahelp.getTicketRandom();
            }
            for(int a = 0;a<19;a++){
                QUESTIONS[a] = ahelp.getQuestRandom();
            }

            TICKET_NUMBER = TICKETS[QUESTION_COUNT - 1];
            QUESTION_NUMBER = QUESTIONS[QUESTION_COUNT - 1];
        }
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.spinner_titles_quest, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                QUESTION_NUMBER = i + 1;
                Log.i(TAG, "Selected question" + (i + 1));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        listAnswers = (ListView)findViewById(R.id.lvAnswers);

        ImgLay = (RelativeLayout)findViewById(R.id.ImgLayout);
        listAnswers.setOnItemClickListener(listener);

        getQuestionToLayout(TICKET_NUMBER, QUESTION_NUMBER);
    }

    private void getQuestionToLayout(int _ticket, int _question){



        Log.i(TAG, "Getting question");
        ImageView img = new ImageView(this);
        Drawable d = ahelp.getImg(_ticket, _question);
        if(d != null){
            img.setImageDrawable(d);
            img.setMinimumWidth(getWindowManager().getDefaultDisplay().getWidth());
            img.setMinimumHeight(650);

            ImgLay.addView(img);
        }

        tvQuestion.setText(ahelp.getQuestion(_ticket, _question));

        arrayAns = ahelp.getListAnswers(_ticket, _question);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayAns);
        listAnswers.setAdapter(adapter);

        Log.i(TAG, "OK question");




    }

    private AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            if(BuildConfig.DEBUG){
                Toast.makeText(getApplicationContext(), "ID=" + i + " P=" + l, Toast.LENGTH_SHORT).show(); //debug
                Log.d(TAG, "ID=" + i + " P=" + l);
            }



            if(i + 1 == ahelp.getGoodAns(TICKET_NUMBER, QUESTION_NUMBER)){
                //SnackBar
                Snackbar snack = Snackbar.make(listAnswers, getResources().getString(R.string.snack_good), Snackbar.LENGTH_SHORT);
                View v = snack.getView();
                TextView text = (TextView)v.findViewById(android.support.design.R.id.snackbar_text);
                text.setTextColor(Color.GREEN);
                snack.show();

                NextConfig(CODE_GOOD);

            } else {
                //SnackBar
                Snackbar snack = Snackbar.make(listAnswers, getResources().getString(R.string.snack_bad), Snackbar.LENGTH_SHORT);
                View v = snack.getView();
                TextView text = (TextView)v.findViewById(android.support.design.R.id.snackbar_text);
                text.setTextColor(Color.RED);
                snack.show();


                NextConfig(CODE_BAD);

            }
        }
    };




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.exam_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.action_skip){
            NextConfig(CODE_SKIP);
        }
        return super.onOptionsItemSelected(item);
    }

    private void NextConfig(int CODE){



        if(CODE == CODE_GOOD){
            good_answers_count += 1;
            QUESTION_COUNT += 1;
            if(MODE_MIX){
                QUESTION_NUMBER = QUESTIONS[QUESTION_COUNT - 1];
                TICKET_NUMBER = TICKETS[QUESTION_COUNT - 1];

            } else {
                QUESTION_NUMBER = QUESTION_COUNT;
            }
            getQuestionToLayout(TICKET_NUMBER, QUESTION_NUMBER);
        } else if(CODE == CODE_BAD){
            bad_answers_count += 1;
            QUESTION_COUNT += 1;
            if(MODE_MIX){
                QUESTION_NUMBER = QUESTIONS[QUESTION_COUNT - 1];
                TICKET_NUMBER = TICKETS[QUESTION_COUNT - 1];

            } else {
                QUESTION_NUMBER = QUESTION_COUNT;
            }
            getQuestionToLayout(TICKET_NUMBER, QUESTION_NUMBER);
        } else if(CODE == CODE_SKIP){
            skip_answers_count += 1;
            QUESTION_COUNT += 1;
            if(MODE_MIX){
                QUESTION_NUMBER = QUESTIONS[QUESTION_COUNT - 1];
                TICKET_NUMBER = TICKETS[QUESTION_COUNT - 1];

            } else {
                QUESTION_NUMBER = QUESTION_COUNT;
            }
            getQuestionToLayout(TICKET_NUMBER, QUESTION_NUMBER);
        } else {
            Log.e(TAG, "Error CODE");
        }
    }
}
