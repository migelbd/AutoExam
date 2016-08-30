package ru.rastaapps.examauto;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class MasterActivity extends AppCompatActivity {


    private SectionsPagerAdapter mSectionsPagerAdapter;
    private int LANG_CODE;
    private int LANG_RU = 100;
    private int LANG_RO = 200;
    private int LANG_DEFAULT = 300;
    private SharedPreferences sPref;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master);
        sPref = getPreferences(MODE_PRIVATE);
        LANG_CODE = sPref.getInt("langcode", LANG_DEFAULT);

        if(LANG_CODE == LANG_RO){
            setLang(LANG_RO, false);
        } else if(LANG_CODE == LANG_RU){
            setLang(LANG_RU, false);
        }




        Locale loc = Locale.getDefault();
        Log.d("Maseter", loc.getLanguage());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.app_name));
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        Intent i = getIntent();
        Helper.getInstance().setSECRET_CODE(i.getBooleanExtra("secretcode", false));



    }


    private void setLang(int l_code, boolean restart){
        Locale l = null;
        if(l_code == LANG_RU) l = new Locale("ru");
        else if(l_code == LANG_RO) l = new Locale("ro");

        Locale.setDefault(l);

        Configuration config = new Configuration();
        if(Build.VERSION.SDK_INT >= 17){
            config.setLocale(l);
        } else {
            config.locale = l;
        }
        getBaseContext().getResources().updateConfiguration(config, null);
        if (restart) {
            Intent i = getBaseContext().getPackageManager()
                    .getLaunchIntentForPackage( getBaseContext().getPackageName() );
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_master, menu);
        if(LANG_CODE == LANG_RO) menu.findItem(R.id.action_lang).setIcon(R.mipmap.ic_action_lang_ro);
        else if(LANG_CODE == LANG_RU) menu.findItem(R.id.action_lang).setIcon(R.mipmap.ic_action_lang_ru);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if(id == R.id.action_lang){
            if(LANG_CODE == LANG_RU){
                sPref = getPreferences(MODE_PRIVATE);
                SharedPreferences.Editor ed = sPref.edit();
                ed.putInt("langcode", LANG_RO);
                ed.commit();
                item.setIcon(R.mipmap.ic_action_lang_ro);
                setLang(LANG_RO, true);
            } else if(LANG_CODE == LANG_RO){
                sPref = getPreferences(MODE_PRIVATE);
                SharedPreferences.Editor ed = sPref.edit();
                ed.putInt("langcode", LANG_RU);
                ed.commit();
                item.setIcon(R.mipmap.ic_action_lang_ru);
                setLang(LANG_RU, true);
            }
//            Toast.makeText(getApplicationContext(), "Временно не доступно", Toast.LENGTH_SHORT).show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public static class PlaceholderFragment extends Fragment implements View.OnClickListener{

        Button btnStart, btnStartMix;
        Spinner spin;
        public PlaceholderFragment() {
        }

        public static PlaceholderFragment newInstance() {
            PlaceholderFragment fragment = new PlaceholderFragment();

            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.fragment_master, container, false);
            spin = (Spinner)v.findViewById(R.id.spinner);
            btnStart = (Button)v.findViewById(R.id.btn_start);
            btnStartMix = (Button)v.findViewById(R.id.btn_mix_start);

            btnStart.setOnClickListener(this);
            btnStartMix.setOnClickListener(this);

            final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity().getApplicationContext(), R.array.spinner_titles_ticket, R.layout.spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spin.setAdapter(adapter);
            spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    Helper.getInstance().setTICKET_NUMBER(i + 1);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            return v;
        }

        @Override
        public void onClick(View view) {

            Intent i = new Intent(getActivity().getApplicationContext(), ExamActivity.class);
            switch (view.getId()){
                default: break;
                case R.id.btn_start:
                    i.putExtra("mode", false);
                    startActivity(i);
                    break;
                case R.id.btn_mix_start:
                    i.putExtra("mode", true);
                    startActivity(i);
                    break;
            }
        }
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position){
                case 0: return PlaceholderFragment.newInstance();
                case 1: return PddFragment.newInstance();
                default: return null;
            }

        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getResources().getString(R.string.page_title_exam);
                case 1:
                    return getResources().getString(R.string.page_title_pdd);

            }
            return null;
        }
    }
}
