package com.example.quickdeals.start;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.quickdeals.R;
import com.example.quickdeals.utils.Utils;

import java.util.Calendar;

import me.itangqi.waveloadingview.WaveLoadingView;

public class StartFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private WaveLoadingView mWaveLoadingView;
    private Drawable mActionBarBackgroundDrawable;
    private  ActionBar actionBar;

    public StartFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StartFragment.
     */
    public static StartFragment newInstance(String param1, String param2) {
        StartFragment fragment = new StartFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_start, container, false);
        Calendar c = Calendar.getInstance();
        mWaveLoadingView = view.findViewById(R.id.waveLoadingView);
        mWaveLoadingView.setShapeType(WaveLoadingView.ShapeType.CIRCLE);
        mWaveLoadingView.setProgressValue(-30);
        mWaveLoadingView.setAmplitudeRatio(60);
        mWaveLoadingView.setWaveColor(Color.parseColor(Utils.getWaves(c)));
        mWaveLoadingView.setTopTitleStrokeWidth(3);
        mWaveLoadingView.setAnimDuration(3000);
        mWaveLoadingView.startAnimation();
        mWaveLoadingView.setVisibility(View.VISIBLE);
        final ImageView wishImageView = view.findViewById(R.id.wish_image_view);
        wishImageView.setImageDrawable(getResources().getDrawable(Utils.getStartActivityText(c), null));
        wishImageView.animate().alpha(1).setDuration(1500).withEndAction(new Runnable() {
            @Override
            public void run() {
                final Handler handler = new Handler();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        int i = 0;
                        while (i < 100) {
                            Log.i("Wave", String.valueOf(i));
                            i+=1;
                            final int finalI = i;
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    mWaveLoadingView.setProgressValue(finalI);
                                }
                            });
                        }
                    }
                }).start();
                wishImageView.animate().alpha(0).setDuration(1500).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        mWaveLoadingView.animate().alpha(0).setDuration(300).withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                Navigation.findNavController(view).navigate(R.id.action_startFragment_to_shablonFragment, null, Utils.getNavOptions());
                            }
                        }).start();
                    }
                }).start();
            }
        }).start();
        return view;
    }
}
