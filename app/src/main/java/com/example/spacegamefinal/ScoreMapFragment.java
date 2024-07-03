package com.example.spacegamefinal;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ScoreMapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_score_map, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // כאן תוכל להוסיף סימונים למפה עבור מיקומי השיאים
        // לדוגמה:
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("High Score Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    public void updateMapLocation(double lat, double lng) {
        if (mMap != null) {
            LatLng location = new LatLng(lat, lng);
            mMap.clear();
            mMap.addMarker(new MarkerOptions().position(location).title("High Score Location"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 10));
        }
    }
}
