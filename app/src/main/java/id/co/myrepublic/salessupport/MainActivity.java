package id.co.myrepublic.salessupport;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import id.co.myrepublic.salessupport.constant.AppConstant;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static TextView txtUser;
    public static TextView txtLoading;
    public static ProgressBar progressBar;
    private LinearLayout layoutLogo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Main");


        // Get Session and set Userid
        SharedPreferences sp = getSharedPreferences(AppConstant.SESSION_KEY, Context.MODE_PRIVATE);
        String userName = sp.getString(AppConstant.COOKIE_USERNAME_KEY,null);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        navigationView.setNavigationItemSelectedListener(this);

        txtUser = (TextView) headerView.findViewById(R.id.nav_header_txt_status_value);
        txtUser.setText(userName);

        this.txtLoading = (TextView) findViewById(R.id.content_main_progressbar_text);
        this.progressBar = (ProgressBar) findViewById(R.id.content_main_progressbar);

        this.layoutLogo = (LinearLayout) findViewById(R.id.content_main_layout_logo);

        Animation animation = AnimationUtils.loadAnimation(this,R.anim.fade_in);
        layoutLogo.startAnimation(animation);
    }



    @Override
    public void onBackPressed() {
        android.support.v4.app.FragmentManager fm = this.getSupportFragmentManager();
        List<Fragment> fragments = fm.getFragments();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (fm.getBackStackEntryCount() > 1) {
            fm.popBackStack();
        } else if (fm.getBackStackEntryCount() == 1) {
            Animation animation = AnimationUtils.loadAnimation(this,R.anim.fade_in);
            layoutLogo.startAnimation(animation);
            layoutLogo.setVisibility(View.VISIBLE);
            setTitle("Main");
            fm.popBackStack();
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
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            doLogout();
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        //calling the method displayselectedscreen and passing the id of selected menu
        displaySelectedScreen(item.getItemId());
        //make this method blank
        return true;
    }


    private void displaySelectedScreen(int itemId) {

        //creating fragment object
        Fragment fragment = null;

        //initializing the fragment object which is selected
        switch (itemId) {
            case R.id.data:
                fragment = new AreasFragment();
                break;
            case R.id.logout:
                doLogout();
                return;
        }

        //replacing the fragment

        String backStateName = fragment.getClass().getName();


        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment, backStateName);
            ft.addToBackStack(backStateName);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        layoutLogo.setVisibility(View.GONE);
    }

    private void doLogout() {
        // Clear session
        SharedPreferences sp = getSharedPreferences(AppConstant.SESSION_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
        return;
    }

}
