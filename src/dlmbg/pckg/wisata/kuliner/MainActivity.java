package dlmbg.pckg.wisata.kuliner;

import java.io.File;

import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * This shows how to listen to some {@link GoogleMap} events.
 */
public class MainActivity extends FragmentActivity
        implements OnMapClickListener, OnMapLongClickListener, OnCameraChangeListener {

    private GoogleMap mMap;
    private TextView mTapTextView;
    static final LatLng DENPASAR = new LatLng(-8.658075,115.211563);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        File direktori_gambar = new File(Environment.getExternalStorageDirectory().toString()+"/app_wisata_kuliner/");
        direktori_gambar.mkdirs();
        
        mTapTextView = (TextView) findViewById(R.id.tap_text);

        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        if (mMap == null) {
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        mMap.setOnMapClickListener(this);
        mMap.setOnMapLongClickListener(this);
        mMap.setOnCameraChangeListener(this);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(DENPASAR, 14));
    }

    @Override
    public void onMapClick(LatLng point) {
        mTapTextView.setText(point.toString());
        mMap.clear();
        mMap.addMarker(new MarkerOptions()
        .position(point));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 15));
    }

    @Override
    public void onMapLongClick(LatLng point) {
    	//ABSTRACT METHOD
    }

    @Override
    public void onCameraChange(final CameraPosition position) {
    	//ABSTRACT METHOD
    }
}
