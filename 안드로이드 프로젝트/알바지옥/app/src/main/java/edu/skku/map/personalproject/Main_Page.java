package edu.skku.map.personalproject;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.crowdfire.cfalertdialog.CFAlertDialog;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;



public class Main_Page extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, MapView.CurrentLocationEventListener, MapView.POIItemEventListener {

    DrawerLayout drawerLayout;
    String username = "";
    ImageButton gps;

    private DatabaseReference mPostReference;

    MapPoint currentMapPoint;
    MapView mapView;
    private int tagNum = 0;

    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    String[] REQUIRED_PERMISSIONS  = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    boolean check_perm = false;;
    int cnt = 0;

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("check_perm", check_perm);
        outState.putInt("cnt", cnt);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_page);

        if(savedInstanceState!=null){
            check_perm = savedInstanceState.getBoolean("check_perm");
            cnt = savedInstanceState.getInt("cnt");
        }
        if(!checkStatus()){
            showDialogSetting();
        }
        else{
            checkPerm();
        }

        mPostReference = FirebaseDatabase.getInstance().getReference();

        RelativeLayout mLoaderLayout = findViewById(R.id.map_layout);

        mapView = new MapView(this);
        mapView.setPOIItemEventListener(this);
        ViewGroup mapViewContainer = (ViewGroup) findViewById(R.id.map_view);
        mapViewContainer.addView(mapView);

        mPostReference.child("Store").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        FireBaseStore get = postSnapshot.getValue(FireBaseStore.class);
                        MapPOIItem marker = new MapPOIItem();
                        marker.setItemName(get.store_name);
                        marker.setTag(tagNum++);
                        double x = Double.parseDouble(String.valueOf(get.latitude));
                        double y = Double.parseDouble(String.valueOf(get.longitude));

                        MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(x, y);
                        marker.setMapPoint(mapPoint);
                        marker.setMarkerType(MapPOIItem.MarkerType.BluePin);
                        marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);
                        mapView.addPOIItem(marker);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        mapView.setCurrentLocationEventListener(this);
        if(!check_perm){
            Toast.makeText(getApplicationContext(),"위치설정을 확인해 주세요.",Toast.LENGTH_LONG).show();
        }
        else if(cnt == 0){
            Toast.makeText(getApplicationContext(),"Loading...",Toast.LENGTH_LONG).show();
            mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);
            cnt = 1;
        }
        mLoaderLayout.setVisibility(View.VISIBLE);

        mapView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapView.bringToFront();
            }
        });

        Toolbar tb = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(tb);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.drawer);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, tb, R.string.app_name, R.string.app_name);
        drawerToggle.syncState();

        View header_view = navigationView.getHeaderView(0);
        if(getIntent().getExtras() != null){
            Intent loginIntent = getIntent();
            username = loginIntent.getStringExtra("Username");

            TextView header_text = (TextView)header_view.findViewById(R.id.drawer_username);
            header_text.setText("Hello," +username);
        }

        gps = (ImageButton)findViewById(R.id.gpsbtn);
        gps.bringToFront();
        gps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!check_perm){
                    Toast.makeText(getApplicationContext(),"위치설정을 확인해 주세요.",Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Loading...",Toast.LENGTH_LONG).show();
                    mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent login = new Intent(Main_Page.this, MainActivity.class);
        login.putExtra("Username", username);
        startActivity(login);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayout.closeDrawer(GravityCompat.START);

        switch (item.getItemId()){
            case R.id.navigationPostStore:
                Intent poststore = new Intent(Main_Page.this, Post_Store.class);
                poststore.putExtra("Username", username);
                startActivity(poststore);
                break;

            case R.id.navigationMyStore:
                Intent mystore = new Intent(Main_Page.this, MyStore.class);
                mystore.putExtra("Username", username);
                startActivity(mystore);
                break;

            case R.id.navigationMyResume:
                Intent myresume = new Intent(Main_Page.this, MyResume.class);
                myresume.putExtra("Username", username);
                startActivity(myresume);
                break;
        }


        return false;
    }


    @Override
    public void onRequestPermissionsResult(int permsRequestCode, @NonNull String[] permissions, @NonNull int[] grandResults) {

        if (permsRequestCode == PERMISSIONS_REQUEST_CODE && grandResults.length == REQUIRED_PERMISSIONS.length) {
            boolean check = true;

            for (int result : grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check = false;
                    break;
                }
            }
            if(check && !check_perm){
                check_perm = true;
                Toast.makeText(getApplicationContext(),"Loading...",Toast.LENGTH_LONG).show();
                mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);
            }
            else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0]) || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[1])) {
                    Toast.makeText(Main_Page.this, "위치정보 설정이 거부되었습니다. 앱을 다시 실행하여 위치정보를 허용해주세요.", Toast.LENGTH_LONG).show();
                    finish();
                }
                else{
                    Toast.makeText(Main_Page.this, "위치정보 설정이 거부되었습니다. 설정에서 위치정보를 허용해주세요. ", Toast.LENGTH_LONG).show();
                }
            }

        }
    }


    @Override
    public void onCurrentLocationUpdate(MapView mapView, MapPoint mapPoint, float v) {
        MapPoint.GeoCoordinate mapPointGeo = mapPoint.getMapPointGeoCoord();
        currentMapPoint = MapPoint.mapPointWithGeoCoord(mapPointGeo.latitude, mapPointGeo.longitude);
        mapView.setMapCenterPoint(currentMapPoint, true);
        mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOff);
    }

    @Override
    public void onCurrentLocationDeviceHeadingUpdate(MapView mapView, float v) {

    }

    @Override
    public void onCurrentLocationUpdateFailed(MapView mapView) {

    }

    @Override
    public void onCurrentLocationUpdateCancelled(MapView mapView) {

    }

    @Override
    public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {

    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) {

    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, final MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {
        final double latit = mapPOIItem.getMapPoint().getMapPointGeoCoord().latitude;
        final double longi = mapPOIItem.getMapPoint().getMapPointGeoCoord().longitude;
        final CFAlertDialog.Builder builder = new CFAlertDialog.Builder(this);
        builder.setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT);
        mPostReference.child("Store").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        FireBaseStore get = postSnapshot.getValue(FireBaseStore.class);
                        if(get.store_name.equals(mapPOIItem.getItemName()) && Math.abs(latit - get.latitude) < 0.00001 && Math.abs(longi-get.longitude)  < 0.00001){
                            builder.setTitle(get.store_name);
                            builder.setMessage("임금: "+get.pay + "\n\n" + "직종: "+get.job_type + "\n\n" + "번호: "+get.phone_number + "\n\n" + "위치: " + get.location + "\n\n" + "내용: "+get.content);
                            builder.addButton("이력서 제출", -1, -1, CFAlertDialog.CFAlertActionStyle.POSITIVE, CFAlertDialog.CFAlertActionAlignment.END, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent submit_resume = new Intent(Main_Page.this, Submit_Resume.class);
                                    submit_resume.putExtra("Username", username);
                                    submit_resume.putExtra("Store", mapPOIItem.getItemName());
                                    startActivity(submit_resume);
                                }
                            });

                            builder.addButton("취소", -1, -1, CFAlertDialog.CFAlertActionStyle.POSITIVE, CFAlertDialog.CFAlertActionAlignment.END, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });
                            builder.show();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {

    }

    public boolean checkStatus() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    private void showDialogSetting() {
        Log.d("perm", String.valueOf(check_perm));
        AlertDialog.Builder builder = new AlertDialog.Builder(Main_Page.this);
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                check_perm = true;
                Intent gpsSettingIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(gpsSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }


    void checkPerm(){

        int Fine_Location = ContextCompat.checkSelfPermission(Main_Page.this, Manifest.permission.ACCESS_FINE_LOCATION);
        int Coarse_Location = ContextCompat.checkSelfPermission(Main_Page.this, Manifest.permission.ACCESS_COARSE_LOCATION);


        if (Fine_Location == PackageManager.PERMISSION_GRANTED && Coarse_Location == PackageManager.PERMISSION_GRANTED) {
            check_perm = true;
        }
        else {
            if(ActivityCompat.shouldShowRequestPermissionRationale(Main_Page.this, REQUIRED_PERMISSIONS[0])) {
                Toast.makeText(Main_Page.this, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_LONG).show();
            }
            ActivityCompat.requestPermissions(Main_Page.this, REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE);

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GPS_ENABLE_REQUEST_CODE) {
            if (checkStatus()) {
                if (checkStatus()) {
                    checkPerm();
                }
            }
        }
    }
}

