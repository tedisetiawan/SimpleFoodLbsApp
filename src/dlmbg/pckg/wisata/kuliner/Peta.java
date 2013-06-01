package dlmbg.pckg.wisata.kuliner;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class Peta extends  Activity {
	  static final LatLng MEDAN = new LatLng(3.584695,98.675079);
	  private GoogleMap map;

	  @Override
	  protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.peta_online);
	    map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
	    Marker denpasar = map.addMarker(new MarkerOptions()
	        .position(MEDAN)
	        .title("Medan")
	        .snippet("Kota Medan - Kota Yang Indah"));

	    map.moveCamera(CameraUpdateFactory.newLatLngZoom(MEDAN, 15));

	    map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
	  }

	  @Override
	  public boolean onCreateOptionsMenu(Menu menu) {
	    getMenuInflater().inflate(R.menu.main, menu);
	    return true;
	  }

	}
