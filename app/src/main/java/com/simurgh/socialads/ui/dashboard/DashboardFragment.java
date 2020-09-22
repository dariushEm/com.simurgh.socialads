package com.simurgh.socialads.ui.dashboard;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.simurgh.socialads.DB.DBManager;
import com.simurgh.socialads.DB.OpenHelper;
import com.simurgh.socialads.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;

    private String TAG = "DashboardFragment_log";

    DemoCollectionPagerAdapter demoCollectionPagerAdapter;
    ViewPager viewPager;


    TextView txt_name;
    TextView txt_biography;
    TextView txt_following;
    TextView txt_follower;
    ImageView img_profile;
    private DBManager db;

    private String profile_pic_url;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        db = new DBManager(getActivity());

        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        txt_name = root.findViewById(R.id.txt_name);
        txt_biography = root.findViewById(R.id.txt_biography);
        txt_following = root.findViewById(R.id.txt_following);
        txt_follower = root.findViewById(R.id.txt_follower);
        img_profile = root.findViewById(R.id.img_profile);

        setInfoOnDB();

        String otherPage = "profiles_98";
        new DashboardViewModel.JsonTask(getActivity(), txt_name, txt_biography,
                txt_following, txt_follower, img_profile)
                .execute("https://www.instagram.com/." + otherPage + "/?__a=1");

        dashboardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });
        return root;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        demoCollectionPagerAdapter = new DemoCollectionPagerAdapter(getChildFragmentManager());
        viewPager = view.findViewById(R.id.pager);
        viewPager.setAdapter(demoCollectionPagerAdapter);
    }


    private void setInfoOnDB() {

        db.open();


        Cursor cursor = db.getAllRecords(OpenHelper.TBL_INFO_USER);

        Log.d(TAG, "cursor.getCount():" + cursor.getCount());


        if (cursor.getCount() > 0) {

            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {

                String id = cursor.getString(cursor.getColumnIndex(OpenHelper.COL_ID_CON));
                String status = cursor.getString(cursor.getColumnIndex(OpenHelper.COL_STATUS_CON));
                String name = cursor.getString(cursor.getColumnIndex(OpenHelper.COL_NAME_CON));
                String bio = cursor.getString(cursor.getColumnIndex(OpenHelper.COL_BIOGRAPHY_CON));
                String follower = cursor.getString(cursor.getColumnIndex(OpenHelper.COL_FOLLOWER_CON));
                String following = cursor.getString(cursor.getColumnIndex(OpenHelper.COL_FOLLOWING_CON));

                txt_name.setText(name);
                txt_biography.setText(bio);
                txt_follower.setText(follower);
                txt_following.setText(following);

                db.close();
            }
        }
    }


}