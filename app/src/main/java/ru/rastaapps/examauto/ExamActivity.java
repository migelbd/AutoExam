package ru.rastaapps.examauto;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ExamActivity extends AppCompatActivity {

    private static final int CODE_GOOD = 100;
    private static final int CODE_BAD = 200;
    private static final int CODE_SKIP = 300;
    private static final int CODE_BTN_SELECTED = 400;
    //ИД действия при смене вопроса

    private boolean SKIP_Q_MODE = false;
    private final String TAG = "ExamActivity"; //Тэг для лога
    CountDownTimer cdt;
    private long time = 1800000; //Тайме экзамена 30 минут
    AssetHelper assetHelper; //Класс доступа к asset файлам
    TextView tvQuestion;

    ListView listAnswers;
    RelativeLayout ImgLay;

    private ArrayList<ErrorsQuestions> listErrors;

    private ArrayList<String> arrayAns;
    private int good_answers_count = 0;
    private int skip_answers_count = 0;
    private int bad_answers_count = 0; //счетчики правильных, не правильных и пропущеных вопросов

    private boolean MODE_MIX = false;
    private int QUESTION_COUNT = 1; //текущий вопрос
    private int QUESTION_NUMBER = 1; //номер вопроса в билете
    private int TICKET_NUMBER = 1; //номер билета

    private int _SKIPING_Q_COUNT = 0; //счетчик при ответах на пропущеные вопросы

    private int[] TICKETS; //масив номеров билетов для режима Микс
    private int[] QUESTIONS; //масив номеров вопросов

    View vHeader;
    private HorizontalScrollView hScroll;
    private ArrayList<Integer> skiping_questions; //масив пропущенных вопросов

    //Кнопки вопросов
    int[] QUESTIONS_BTNS = {R.id.v1, R.id.v2, R.id.v3, R.id.v4, R.id.v5, R.id.v6, R.id.v7, R.id.v8, R.id.v9, R.id.v10,
            R.id.v11, R.id.v12, R.id.v13, R.id.v14, R.id.v15, R.id.v16, R.id.v17, R.id.v18, R.id.v19, R.id.v20}; //масив ИД кнопок для индикации и переключения вопросов

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);
        TICKET_NUMBER = Helper.getInstance().getTICKET_NUMBER();
        assetHelper = new AssetHelper(this, "ru");
        listErrors = new ArrayList<>();
        hScroll = (HorizontalScrollView)findViewById(R.id.hScroll);
        Intent intent = getIntent();
        MODE_MIX = intent.getBooleanExtra("mode", false);
        if(MODE_MIX == true){
            //наполнение масивов номерами билетов и вопросов в режиме Микс
            TICKETS = new int[20];
            QUESTIONS = new int[20];
            for(int i = 0;i<20;i++){
                TICKETS[i] = assetHelper.getTicketRandom();
            }
            for(int a = 0;a<20;a++){
                QUESTIONS[a] = assetHelper.getQuestRandom();
            }

            TICKET_NUMBER = TICKETS[QUESTION_COUNT - 1];
            QUESTION_NUMBER = QUESTIONS[QUESTION_COUNT - 1];
        }
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.spinner_titles_quest, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);



        listAnswers = (ListView)findViewById(R.id.lvAnswers);


        listAnswers.setOnItemClickListener(listener);

        cdt = new CountDownTimer(time, 1000) {
            @Override
            public void onTick(long l) {
                SimpleDateFormat sd = new SimpleDateFormat("mm:ss");
                ActionBar b = getSupportActionBar();


                if(BuildConfig.DEBUG || Helper.getInstance().isSECRET_CODE()){
                    b.setTitle(sd.format(l) + " OK:" + assetHelper.getGoodAns(TICKET_NUMBER, QUESTION_NUMBER) + " T:" + TICKET_NUMBER);
                } else {
                    b.setTitle(sd.format(l));
                }
            }

            @Override
            public void onFinish() {
                ShowDialog("Упс..", "Закончилось время", getApplicationContext());
            }
        };
        cdt.start();
        skiping_questions = new ArrayList<>();
        //
        for(int i = 0;i<QUESTIONS_BTNS.length;i++){  //задает слушателя действия для кнопок
            ((Button)findViewById(QUESTIONS_BTNS[i])).setOnClickListener(bt_listener);
            ((Button)findViewById(QUESTIONS_BTNS[i])).setBackgroundColor(getResources().getColor(R.color.colorBtnQuestion));
        }


        getQuestionToLayout(TICKET_NUMBER, QUESTION_NUMBER);
    }

    private void getQuestionToLayout(int _ticket, int _question){ //поулчение вопроса


        vHeader = getLayoutInflater().inflate(R.layout.lv_header, null);
        ImgLay = (RelativeLayout)vHeader.findViewById(R.id.ImgLayout);
        tvQuestion = (TextView)vHeader.findViewById(R.id.tvQuestion);

        Log.i(TAG, "Getting question");
        ImageView img = new ImageView(this);
        Drawable d = assetHelper.getImg(_ticket, _question);
        if(d != null){
            img.setImageDrawable(d);
            img.setMinimumWidth(getWindowManager().getDefaultDisplay().getWidth());


            img.setMinimumHeight(600);

            ImgLay.addView(img);
        } else {
            ImgLay.removeAllViews();
        }

        tvQuestion.setText(assetHelper.getQuestion(_ticket, _question));

        arrayAns = assetHelper.getListAnswers(_ticket, _question);
//        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayAns);
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.item_answer, R.id._textItem, arrayAns);
        listAnswers.setAdapter(adapter);

        listAnswers.addHeaderView(vHeader);

        Log.i(TAG, "OK question");




    }

    private AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            if(BuildConfig.DEBUG){

                Log.d(TAG, "ID=" + i);
            }

            int goodAns = assetHelper.getGoodAns(TICKET_NUMBER, QUESTION_NUMBER);
            if (i != 0) {
                if(i == goodAns){ //проверка правильного ответа
                    //SnackBar
                    Snackbar snack = Snackbar.make(((FrameLayout)findViewById(R.id.parentFrame)), getResources().getString(R.string.snack_good), Snackbar.LENGTH_SHORT);
                    View v = snack.getView();

                    TextView text = (TextView)v.findViewById(android.support.design.R.id.snackbar_text);
                    text.setTextColor(getResources().getColor(R.color.colorGood));
                    snack.show();
                    NextConfig(CODE_GOOD); //переключение на следующий вопрос с сообщением о том что отет верный


                } else {
                    //SnackBar
                    Snackbar snack = Snackbar.make(((FrameLayout)findViewById(R.id.parentFrame)), getResources().getString(R.string.snack_bad), Snackbar.LENGTH_SHORT);
                    View v = snack.getView();
                    TextView text = (TextView)v.findViewById(android.support.design.R.id.snackbar_text);
                    text.setTextColor(getResources().getColor(R.color.colorBad));
                    snack.show();

                    ErrorsQuestions er = new ErrorsQuestions();
                    er.setIMG(assetHelper.getImg(TICKET_NUMBER, QUESTION_NUMBER));
                    er.setQUESTION(assetHelper.getQuestion(TICKET_NUMBER, QUESTION_NUMBER));
                    er.setGOOOD_ANSWER(assetHelper.getAnswer(TICKET_NUMBER, QUESTION_NUMBER, goodAns));
                    er.setBAD_ANSWER(assetHelper.getAnswer(TICKET_NUMBER, QUESTION_NUMBER, i));
                    er.setCOMMENT(assetHelper.getComment(TICKET_NUMBER, QUESTION_NUMBER));
                    er.setNUM_QUESTION(QUESTION_COUNT);
                    listErrors.add(er);
                    Helper.getInstance().setListErrors(listErrors);

                    NextConfig(CODE_BAD); //переключение на следующий вопрос с сообщением о том что отет не верный

            }
            }
        }
    };




    private View.OnClickListener bt_listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            for(int i = 0;i<19;i++){
                SetQuestionButtonColor(i + 1, getResources().getColor(R.color.colorBtnQuestion));
            }

            switch (view.getId()){
                default: break;
                case R.id.v1: QUESTION_COUNT = 1; NextConfig(CODE_BTN_SELECTED); break; //переключение на выбранные вопрос с помощью кнопок
                case R.id.v2: QUESTION_COUNT = 2; NextConfig(CODE_BTN_SELECTED); break;
                case R.id.v3: QUESTION_COUNT = 3; NextConfig(CODE_BTN_SELECTED); break;
                case R.id.v4: QUESTION_COUNT = 4; NextConfig(CODE_BTN_SELECTED); break;
                case R.id.v5: QUESTION_COUNT = 5; NextConfig(CODE_BTN_SELECTED); break;
                case R.id.v6: QUESTION_COUNT = 6; NextConfig(CODE_BTN_SELECTED); break;
                case R.id.v7: QUESTION_COUNT = 7; NextConfig(CODE_BTN_SELECTED); break;
                case R.id.v8: QUESTION_COUNT = 8; NextConfig(CODE_BTN_SELECTED); break;
                case R.id.v9: QUESTION_COUNT = 9; NextConfig(CODE_BTN_SELECTED); break;
                case R.id.v10: QUESTION_COUNT = 10; NextConfig(CODE_BTN_SELECTED); break;
                case R.id.v11: QUESTION_COUNT = 11; NextConfig(CODE_BTN_SELECTED); break;
                case R.id.v12: QUESTION_COUNT = 12; NextConfig(CODE_BTN_SELECTED); break;
                case R.id.v13: QUESTION_COUNT = 13; NextConfig(CODE_BTN_SELECTED); break;
                case R.id.v14: QUESTION_COUNT = 14; NextConfig(CODE_BTN_SELECTED); break;
                case R.id.v15: QUESTION_COUNT = 15; NextConfig(CODE_BTN_SELECTED); break;
                case R.id.v16: QUESTION_COUNT = 16; NextConfig(CODE_BTN_SELECTED); break;
                case R.id.v17: QUESTION_COUNT = 17; NextConfig(CODE_BTN_SELECTED); break;
                case R.id.v18: QUESTION_COUNT = 18; NextConfig(CODE_BTN_SELECTED); break;
                case R.id.v19: QUESTION_COUNT = 19; NextConfig(CODE_BTN_SELECTED); break;
                case R.id.v20: QUESTION_COUNT = 20; NextConfig(CODE_BTN_SELECTED); break;

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

        listAnswers.removeHeaderView(vHeader); //очищает экран для следующего вопроса
        if(CODE == CODE_GOOD){
            //верный ответ
            SetQuestionButtonColor(QUESTION_COUNT, getResources().getColor(R.color.colorGood)); //для кнопки задается цвет
            if(!SKIP_Q_MODE) { //проверка если в режиме отвтеа на пропущенные вопросы
                QUESTION_COUNT += 1;
            } else {
                _SKIPING_Q_COUNT += 1;
                skip_answers_count -= 1;
                if (_SKIPING_Q_COUNT < skiping_questions.size()) {
                    QUESTION_COUNT = skiping_questions.get(_SKIPING_Q_COUNT); //переключение на пропущенные вопросы

                } else {
                    CheckResult();

                }
            }
            good_answers_count += 1;

            if (QUESTION_COUNT != 21) {
                if(MODE_MIX){
                    QUESTION_NUMBER = QUESTIONS[QUESTION_COUNT - 1];
                    TICKET_NUMBER = TICKETS[QUESTION_COUNT - 1];

                } else {
                    QUESTION_NUMBER = QUESTION_COUNT;
                }
                getQuestionToLayout(TICKET_NUMBER, QUESTION_NUMBER);


            }


        } else if(CODE == CODE_BAD){

            SetQuestionButtonColor(QUESTION_COUNT, getResources().getColor(R.color.colorBad));
            if(!SKIP_Q_MODE){
                QUESTION_COUNT += 1;
            } else {
                _SKIPING_Q_COUNT += 1;
                skip_answers_count -= 1;
                if (_SKIPING_Q_COUNT < skiping_questions.size()) {
                    QUESTION_COUNT = skiping_questions.get(_SKIPING_Q_COUNT);

                } else {
                    CheckResult();

                }
            }
            bad_answers_count += 1;

            if (QUESTION_COUNT != 21) {
                if(MODE_MIX){
                    QUESTION_NUMBER = QUESTIONS[QUESTION_COUNT - 1];
                    TICKET_NUMBER = TICKETS[QUESTION_COUNT - 1];

                } else {
                    QUESTION_NUMBER = QUESTION_COUNT;
                }
                getQuestionToLayout(TICKET_NUMBER, QUESTION_NUMBER);

            }

        } else if(CODE == CODE_SKIP){
            SetQuestionButtonColor(QUESTION_COUNT, getResources().getColor(R.color.colorSkip));
            SetSkipQuestions(QUESTION_COUNT); //запись в масив номера пропущенного вопроса
            skip_answers_count += 1;
            QUESTION_COUNT += 1;
            if (QUESTION_COUNT != 21) {
                if(MODE_MIX){
                    QUESTION_NUMBER = QUESTIONS[QUESTION_COUNT - 1];
                    TICKET_NUMBER = TICKETS[QUESTION_COUNT - 1];

                } else {
                    QUESTION_NUMBER = QUESTION_COUNT;
                }
                getQuestionToLayout(TICKET_NUMBER, QUESTION_NUMBER);
            }

        } else if(CODE == CODE_BTN_SELECTED){

            if (QUESTION_COUNT != 21) {
                if(MODE_MIX){
                    QUESTION_NUMBER = QUESTIONS[QUESTION_COUNT - 1];
                    TICKET_NUMBER = TICKETS[QUESTION_COUNT - 1];

                } else {
                    QUESTION_NUMBER = QUESTION_COUNT;
                }
                getQuestionToLayout(TICKET_NUMBER, QUESTION_NUMBER);
            }

        } else {
            Log.e(TAG, "Error CODE");
        }
        if (QUESTION_COUNT != 21) {
            SetQuestionButtonColor(QUESTION_COUNT, getResources().getColor(R.color.colorBtnSelected));
        }

        if(QUESTION_COUNT < 20) hScroll.smoothScrollTo(GetButtonX(QUESTION_COUNT), 0);

        CheckResult(); // проверка результатов







    }

    private void CheckResult(){
        if(QUESTION_COUNT > 20 && skip_answers_count > 3){ //если номера вопроса 20, а пропущенных вопросов больше 3, то происходит переключение в режим ответа пропущенных вопросов
            ShowDialog("Упс..", "Вы еще не ответили на некоторые вопросы.", getApplicationContext());
            SKIP_Q_MODE = true;
            QUESTION_COUNT = skiping_questions.get(_SKIPING_Q_COUNT);
            NextConfig(CODE_BTN_SELECTED);
        } else if(good_answers_count >= 17 && QUESTION_COUNT > 20){ // если ответов верных больше 17 и вопрос 20 то открывается экран с резульататами
            Intent i = new Intent(this, ResultActivity.class);
            i.putExtra("result", true);
            i.putExtra("good", good_answers_count);
            i.putExtra("bad", bad_answers_count);
            i.putExtra("skip", skip_answers_count);
            startActivity(i);
            finish();
        } else if(bad_answers_count > 3){ //если больше 3 ошибок, открывается экран с результатами
            Intent i = new Intent(this, ResultActivity.class);
            i.putExtra("result", false);
            i.putExtra("good", good_answers_count);
            i.putExtra("bad", bad_answers_count);
            i.putExtra("skip", skip_answers_count);
            startActivity(i);
            finish();
        } else if(good_answers_count >= 17 && skip_answers_count < 3){
            Intent i = new Intent(this, ResultActivity.class);
            i.putExtra("result", true);
            i.putExtra("good", good_answers_count);
            i.putExtra("bad", bad_answers_count);
            i.putExtra("skip", skip_answers_count);
            startActivity(i);
            finish();
        }
    }

    private int GetButtonX(int btn){ //получение координат кнопки для автоматической прокрутки полосы с кнопками
        if(Build.VERSION.SDK_INT >= 11){
            return (int)((Button)findViewById(QUESTIONS_BTNS[btn - 1])).getX() - 500; //для версии SDK выше 11
        } else {
            return ((Button)findViewById(QUESTIONS_BTNS[btn - 1])).getLeft() - 500; 
        }

    }
    private void SetQuestionButtonColor(int btn, int color){
        ((Button)findViewById(QUESTIONS_BTNS[btn - 1])).setBackgroundColor(color);
    }

    private void SetSkipQuestions(int _question_count){
        skiping_questions.add(_question_count);
    }
    private void ShowDialog(String titile, String msg, Context applicationContext){
        AlertDialog.Builder adb = new AlertDialog.Builder(applicationContext);
        adb.setTitle(titile);
        adb.setMessage(msg);
        adb.setNeutralButton("OK", null);


        adb.show();
    }
}
