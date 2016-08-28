package ru.rastaapps.examauto;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



public class PddFragment extends Fragment {

    public PddFragment() {
        // Required empty public constructor
    }


    public static PddFragment newInstance() {
        PddFragment fragment = new PddFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_pdd, container, false);



        return v;
    }

}
