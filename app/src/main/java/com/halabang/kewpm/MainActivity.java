package com.halabang.kewpm;

import android.content.ContentValues;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import android.widget.ArrayAdapter;

import com.daimajia.swipe.SwipeLayout;
import com.google.firebase.iid.FirebaseInstanceId;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ListView listView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> taskList;
    private TextView totalClassmates;
    private SwipeLayout swipeLayout;

    private final static String TAG = MainActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        listView = (ListView)findViewById(R.id.task_lists);
        //init list view
        taskList = new ArrayList<>();
        getDataFromJSON();
        setListViewHeader();
        setListViewAdapter();

        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void getDataFromJSON() {
        try {
            taskList.add("123");
            taskList.add("223");
            taskList.add("323");
        } catch (Exception e) {
            //log the exception
//            e.printStackTrace();
        } finally {
//            if (reader != null) {
//                try {
//                    reader.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
        }
    }
    private void setListViewHeader() {
        LayoutInflater inflater = getLayoutInflater();
        View header = inflater.inflate(R.layout.swipelist_header, listView, false);
        totalClassmates = (TextView) header.findViewById(R.id.total);
        swipeLayout = (SwipeLayout)header.findViewById(R.id.swipe_layout);
        setSwipeViewFeatures();
        listView.addHeaderView(header);
    }
    private void setSwipeViewFeatures() {
        //set show mode.
        swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);

        //add drag edge.(If the BottomView has 'layout_gravity' attribute, this line is unnecessary)
        swipeLayout.addDrag(SwipeLayout.DragEdge.Left, findViewById(R.id.bottom_wrapper));

        swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
            @Override
            public void onClose(SwipeLayout layout) {
                Log.i(TAG, "onClose");
            }

            @Override
            public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {
                Log.i(TAG, "on swiping");
            }

            @Override
            public void onStartOpen(SwipeLayout layout) {
                Log.i(TAG, "on start open");
            }

            @Override
            public void onOpen(SwipeLayout layout) {
                Log.i(TAG, "the BottomView totally show");
            }

            @Override
            public void onStartClose(SwipeLayout layout) {
                Log.i(TAG, "the BottomView totally close");
            }

            @Override
            public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {
                //when user's hand released.
            }
        });
    }
    private void setListViewAdapter() {
        adapter = new TaskAdapter(this, R.layout.task_listitem, taskList);
        listView.setAdapter(adapter);

        totalClassmates.setText("(" + taskList.size() + ")");
    }
    public void updateAdapter() {
        adapter.notifyDataSetChanged(); //update adapter
        totalClassmates.setText("(" + taskList.size() + ")"); //update total friends in list
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (FirebaseInstanceId.getInstance().getToken() == null) {
            menu.getItem(1).setVisible(false);
        } else {
            menu.getItem(1).setVisible(true);
        }
        return super.onPrepareOptionsMenu(menu);
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
        } else if (id == R.id.action_updateToken) {
            if (FirebaseInstanceId.getInstance().getToken() != null) {
                if(Utility.isConnectingToInternet(this)) {
                    ContentValues obj = new ContentValues();
                    obj.put("Token", FirebaseInstanceId.getInstance().getToken());
                    //obj.put("Token", "dJiXGiNaBHc:APA91bE-qeGTscZCRczRy9axnRAK64g4pysQ7_OoeM-eTP6Y-aui8OkHy_KpRS0lhlpK5gTT7StwWH-5tJLB-s3JmrzQ1L_nP7JmtUzwSTWthBohL-ktIi86JNylk3850zTyvRldf8X5");
                    obj.put("TokenType","Firebase Cloud Message Token");
                    obj.put("OS",android.os.Build.MODEL);
                    obj.put("OSVersion",android.os.Build.VERSION.RELEASE);
                    obj.put("UserID","13999d69-3bfc-4fed-a7a3-2a54af49f32b");
                    HttpUtility.useHttpUrlConnectionGetThread("http://pm.exchangepromise.com/Api/TokenIdentity/CreateTokenRecord", obj);
                    Utility.showMsg(this, "Token Updated");
                } else {
                    Utility.showMsg(this, "No internet connection");
                }
            } else {
                Utility.showMsg(this, "No token found, please reinstall the app");
            }
        }

        return super.onOptionsItemSelected(item);
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
