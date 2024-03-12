package com.example.googlemapsintegrationjava;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.googlemapsintegrationjava.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.model.PolygonOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private ArrayAdapter<String> mapTypeAdapter;
    private final String[] mapTypes = {"Normal", "Satellite", "Terrain", "Hybrid"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Type Through Autocomplete Text View
        mapTypeAdapter = new ArrayAdapter<>(this, R.layout.item_atv_list, mapTypes);
        binding.autoTvSelectMapType.setAdapter(mapTypeAdapter);

        binding.autoTvSelectMapType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String map_index = parent.getItemAtPosition(position).toString();

                // Change map type based on user selection
                switch (map_index) {
                    case "Normal":
                        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                        break;
                    case "Satellite":
                        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                        break;
                    case "Terrain":
                        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                        break;
                    case "Hybrid":
                        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                        break;
                }
            }
        });

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Bhakkar, Pakistan.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Bhakkar and move the camera
        LatLng bk = new LatLng(31.633333, 71.066666);
        mMap.addMarker(new MarkerOptions().position(bk).title("Bhakkar"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(bk));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(bk, 16f));

        // Overlays
        // Circle
        mMap.addCircle(new CircleOptions()
                .center(bk)
                .radius(100)
                .fillColor(Color.GREEN)
                .strokeColor(Color.DKGRAY)
        );

        // Polygon
        // Define your list of LatLng points
        List<LatLng> polygonPoints = new ArrayList<>();
        polygonPoints.add(new LatLng(31.635, 71.066666));  // Point 1
        polygonPoints.add(new LatLng(31.635, 71.069));     // Point 2
        polygonPoints.add(new LatLng(31.64, 71.069));      // Point 3
        polygonPoints.add(new LatLng(31.64, 71.066666));   // Point 4// Point 4


        // Create a polygon overlay using the points
         mMap.addPolygon(new PolygonOptions()
                .addAll(polygonPoints)
                .fillColor(Color.YELLOW)
                .strokeColor(Color.DKGRAY)
         );

         // Image Overlay
         mMap.addGroundOverlay(new GroundOverlayOptions()
                 .position(bk, 100f, 100f)
                 .image(BitmapDescriptorFactory.fromResource(R.drawable.mi))
                 .clickable(true)
         );

         mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        // Geocoder (Getting Data from Location)
         mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
             @Override
             public void onMapClick(@NonNull LatLng latLng) {

                 mMap.addMarker(new MarkerOptions().position(latLng).title("Clicked here.."));

                 Geocoder geocoder = new Geocoder(MapsActivity.this);
                 try {
                     ArrayList<Address> arrAddress = (ArrayList<Address>) geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                     Log.d("Addr", arrAddress.get(0).getAddressLine(0));
                 } catch (IOException e) {
                     e.printStackTrace();
                 }
             }
         });




    }
}