package com.example.medicalschedulingapp;

import android.app.ActionBar;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.medicalschedulingapp.MapActivity.Location.LocationRecyclerViewAdapter;
import com.example.medicalschedulingapp.ui.home.MainActivity;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.geocoder.GeocoderCriteria;
import com.mapbox.geojson.Feature;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.navigation.NavType.SerializableArrayType;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import androidx.recyclerview.widget.SnapHelper;

import com.mapbox.geocoder.MapboxGeocoder;
import com.mapbox.geocoder.service.models.GeocoderFeature;
import com.mapbox.geocoder.service.models.GeocoderResponse;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class MapActivity extends AppCompatActivity implements PermissionsListener {
    private MapView mapView;
    private PermissionsManager permissionsManager;
    private MapboxMap mapboxMap;
    private double currLat;
    private double currLng;
    private static final String SOURCE_ID = "SOURCE_ID";
    private static final String ICON_ID = "ICON_ID";
    private static final String LAYER_ID = "LAYER_ID";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.access_token));
        setContentView(R.layout.activity_map);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull MapboxMap mapboxMap) {
                MapActivity.this.mapboxMap = mapboxMap;
                mapboxMap.setStyle(new Style.Builder().fromUri("mapbox://styles/mapbox/cjerxnqt3cgvp2rmyuxbeqme7"),
                        new Style.OnStyleLoaded() {
                            @Override
                            public void onStyleLoaded(@NonNull Style style) {
                                enableLocationComponent(style);
                            }
                        });
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
         finish();
         return true;
    }

    @SuppressWarnings( {"MissingPermission"})
    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
        if (PermissionsManager.areLocationPermissionsGranted(this)) {
            LocationComponent locationComponent = mapboxMap.getLocationComponent();
            locationComponent.activateLocationComponent(
                    LocationComponentActivationOptions.builder(this, loadedMapStyle).build());
            locationComponent.setLocationComponentEnabled(true);
            locationComponent.setCameraMode(CameraMode.TRACKING);
            locationComponent.setRenderMode(RenderMode.COMPASS);
            currLat = locationComponent.getLastKnownLocation().getLatitude();
            currLng = locationComponent.getLastKnownLocation().getLongitude();
            locationComponent.getLastKnownLocation().getLongitude();
            CameraPosition position = new CameraPosition.Builder()
            	.target(new LatLng(currLat, currLng))
            	.zoom(10)
            	.tilt(20)
            	.build();
            mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position));
            Bundle bundle = getIntent().getExtras();
            String location = bundle.getString("key");
            String trueLoc = location;
            if(location.equals("Hospital")){trueLoc = trueLoc + "s";}
            getSupportActionBar().setTitle("5 Closest " + trueLoc);
            getSupportActionBar().setCustomView(R.layout.titlebar);
            MapboxGeocoder client = new MapboxGeocoder.Builder()
                    .setProximity(currLng, currLat)
                    .setLocation(location)
                    .setAccessToken(getString(R.string.access_token))
                    .build();
            client.enqueue(new Callback<GeocoderResponse>() {
                @Override
                public void onResponse(Response<GeocoderResponse> response, Retrofit retrofit) {
                    List<GeocoderFeature> results = response.body().getFeatures();
                    Log.d("MapActivity", "Results size: " + results.size());
                    if (results.size() > 0) {
                        createLocations(results);
                        initRecyclerView(results);
                    } else {
                        // No result for your request were found.
                        Log.d("MapActivity", "Hello: No result found");
                    }
                }
                @Override
                public void onFailure(Throwable t) {

                }
            });
        } else {
            permissionsManager = new PermissionsManager( this);
            permissionsManager.requestLocationPermissions(this);
        }
    }


    private List<Location> createLocations(List<GeocoderFeature> gc) {
        ArrayList<Location> locationList = new ArrayList<>();
        for (int x = 0; x < 5; x++) {
            Location singleLocation = new Location();
            GeocoderFeature curr = gc.get(x);
            String address = curr.toAddress(Locale.US).getAddressLine(0);
            LatLng currCoords = new LatLng(curr.getLatitude(),curr.getLongitude());
            mapboxMap.addMarker(new MarkerOptions().position(currCoords));
            singleLocation.setName(curr.getText());
            singleLocation.setAddress(address.substring(address.indexOf(",")+1).trim());
            singleLocation.setLocationCoordinates(currCoords);
            locationList.add(singleLocation);
        }
        return locationList;

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void initRecyclerView(List<GeocoderFeature> results) {
        RecyclerView recyclerView = findViewById(R.id.rv_on_top_of_map);
        LocationRecyclerViewAdapter locationAdapter =
                new LocationRecyclerViewAdapter(createLocations(results), mapboxMap);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.HORIZONTAL, true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(locationAdapter);
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);
    }

    public static void newAppointment(Context cxt, String name){
        Intent intent = new Intent(cxt, NewAppointmentActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("key", name);
        intent.putExtras(bundle);
        cxt.startActivity(intent);
    }



    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {

    }

    @Override
    public void onPermissionResult(boolean granted) {

    }

    static class Location {
        private String name;
        private String address;
        private LatLng locationCoordinates;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public LatLng getLocationCoordinates() {
            return locationCoordinates;
        }

        public void setLocationCoordinates(LatLng locationCoordinates) {
            this.locationCoordinates = locationCoordinates;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        static class LocationRecyclerViewAdapter extends
                RecyclerView.Adapter<LocationRecyclerViewAdapter.MyViewHolder> {

            private List<Location> locationList;
            private MapboxMap map;
            private static Context mContext;

            public LocationRecyclerViewAdapter(List<Location> locationList, MapboxMap mapBoxMap) {
                this.locationList = locationList;
                this.map = mapBoxMap;
            }

            @Override
            public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.map_card, parent, false);
                return new MyViewHolder(itemView);
            }

            @Override
            public void onBindViewHolder(MyViewHolder holder, int position) {
                Location singleRecyclerViewLocation = locationList.get(position);
                holder.name.setText(singleRecyclerViewLocation.getName());
                holder.address.setText(singleRecyclerViewLocation.getAddress());
                holder.setClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        LatLng selectedLocationLatLng = locationList.get(position).getLocationCoordinates();
                        CameraPosition newCameraPosition = new CameraPosition.Builder()
                                .target(selectedLocationLatLng)
                                .zoom(14)
                                .build();
                        map.easeCamera(CameraUpdateFactory.newCameraPosition(newCameraPosition));
                    }
                });
            }

            @Override
            public int getItemCount() {
                return locationList.size();
            }

            class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
                TextView name;
                TextView address;
                CardView singleCard;
                ItemClickListener clickListener;
                Button add;


                MyViewHolder(View view) {
                    super(view);
                    name = view.findViewById(R.id.location_title_tv);
                    address = view.findViewById(R.id.address_tv);
                    singleCard = view.findViewById(R.id.single_location_cardview);
                    singleCard.setOnClickListener(this);
                    add = view.findViewById(R.id.add_from_map);
                    add.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            newAppointment(view.getContext(),name.toString());
                        }
                    });
                }

                public void setClickListener(ItemClickListener itemClickListener) {
                    this.clickListener = itemClickListener;
                }


                @Override
                public void onClick(View view) {
                    clickListener.onClick(view, getLayoutPosition());
                }




            }
        }

        public interface ItemClickListener {
            void onClick(View view, int position);
        }
    }





}


