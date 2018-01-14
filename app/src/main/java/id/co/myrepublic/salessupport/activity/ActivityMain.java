package id.co.myrepublic.salessupport.activity;

import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import id.co.myrepublic.salessupport.R;
import id.co.myrepublic.salessupport.constant.AppConstant;
import id.co.myrepublic.salessupport.support.AppPermission;
import id.co.myrepublic.salessupport.util.GlobalVariables;

public class ActivityMain extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static TextView txtUser;
    public static TextView txtLoading;
    public static ProgressBar progressBar;
    public static ImageView progressIcon;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Main");

        AppPermission.requestPermission(this);

        // Get Session and set Userid
        GlobalVariables sm = GlobalVariables.getInstance();
        String userName = sm.getString(AppConstant.COOKIE_USERNAME_KEY,null);



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
        this.progressIcon = (ImageView) findViewById(R.id.content_main_progressbar_icon);

        // Call main fragment
        Fragment fragment = new FragmentMain();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        String backStateName = fragment.getClass().getName();
        ft.replace(R.id.content_frame, fragment, backStateName);
        ft.addToBackStack(backStateName);
        ft.commit();


    }



    @Override
    public void onBackPressed() {
        android.support.v4.app.FragmentManager fm = this.getSupportFragmentManager();
        // This is to clear the data in ClusterDetail if back pressed
        Fragment clusterDetailFragment = fm.findFragmentByTag(FragmentClusterDetailData.class.getName());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (fm.getBackStackEntryCount() > 1) {
            if (fm.getBackStackEntryCount() == 2) {
                setTitle("Main");
            }
            fm.popBackStack();
        } else if (fm.getBackStackEntryCount() == 1) {
            finish();
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
                fragment = new FragmentAreas();
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
    }

    private void doLogout() {
        // Clear session
        GlobalVariables sm = GlobalVariables.getInstance();
        sm.clearSession();

        Intent intent = new Intent(this, ActivityLogin.class);
        startActivity(intent);
        finish();
        return;
    }

}
