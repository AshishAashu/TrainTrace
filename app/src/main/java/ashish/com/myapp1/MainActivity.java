package ashish.com.myapp1;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import ashish.com.myapp1.SystemFunction.SystemFunction;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout menudrawer;
    private NavigationView navigation;
    private ActionBarDrawerToggle menutoggle;
    private FrameLayout itemframe;
     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        menudrawer = (DrawerLayout)findViewById(R.id.drawer);
        navigation =(NavigationView)findViewById(R.id.navigation);
        itemframe = (FrameLayout)findViewById(R.id.itemframe);
        menutoggle = new ActionBarDrawerToggle(this,menudrawer,R.string.open,R.string.close);
        menudrawer.addDrawerListener(menutoggle);
        menutoggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        loadFragment(new PnrFragment());
        menuItemListener();
    }

    public void menuItemListener(){
        navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if(id == R.id.menu_pnr_status){
                    loadFragment(new PnrFragment());
                }else if(id==R.id.menu_live_train_status){
                    loadFragment(new LiveTrainFragment());
                }else if(id==R.id.menu_seat_availability){
                    loadFragment(new SeatAvailabilityFragment());
                }else if(id==R.id.menu_fare_enquiry){
                    loadFragment(new FareEnquiryFragment());
                }else if(id==R.id.menu_train_bw_station){
                    loadFragment(new TrainBwtStationFragment());
                }else if(id==R.id.menu_route){
                    loadFragment(new TrainRouteFragment());
                }
                menudrawer.closeDrawer(GravityCompat.START);
                return false;
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(menutoggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void loadFragment(Fragment fragment){
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.itemframe, fragment);
        fragmentTransaction.commit();
    }
}
