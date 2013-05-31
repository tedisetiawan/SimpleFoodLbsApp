package dlmbg.pckg.wisata.kuliner;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
public class TambahTempatMakan extends FragmentActivity
        implements OnMapClickListener, OnMapLongClickListener, OnCameraChangeListener {

	private SqliteManager sqliteDB;
	private Long id;
	public static final String SIMPAN_DATA = "simpan";
	
    private GoogleMap mMap;
    private EditText EdPosisiLatLang,EdNamaTempat,EdNamaJalan;
	String var_posisi,var_nama_tempat,var_nama_jalan;
    public LatLng SAVED_POSITION = new LatLng(3.584695,98.675079);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_tempat_makan);

        EdPosisiLatLang = (EditText) findViewById(R.id.ed_posisi_tempat_makan);
        EdNamaTempat = (EditText) findViewById(R.id.ed_nama_tempat);
        EdNamaJalan = (EditText) findViewById(R.id.ed_nama_jalan);
        
        id = null;

		if (savedInstanceState == null) {
			Bundle extras = getIntent().getExtras();
			if (extras != null && extras.containsKey(DashboardActivity.EXTRA_ROWID)) {
				id = extras.getLong(DashboardActivity.EXTRA_ROWID);
			}
			else
			{
				var_nama_tempat = extras.getString("nama_tempat");
				var_posisi = extras.getString("lat_lang");
				var_nama_jalan = extras.getString("nama_jalan");
			}
		}
		
		sqliteDB = new SqliteManager(this);
		sqliteDB.bukaKoneksi();
		
        pindahData();

		Button button = (Button) findViewById(R.id.btn_simpan_tempat_makan);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				simpan();
				finish();
			}
		});

        setUpMapIfNeeded();
    }
    
    @Override
	protected void onDestroy() {
		super.onDestroy();
		sqliteDB.tutupKoneksi();
	}

	private void pindahData() {
		if (id != null) {
			Cursor cursor = sqliteDB.bacaDataTerseleksiTempatMakan(id);
			EdNamaTempat.setText(cursor.getString(1));
			EdPosisiLatLang.setText(cursor.getString(2));
			EdNamaJalan.setText(cursor.getString(3));
			
			String replace_string_first = cursor.getString(2).replace("lat/lng: (", "");
			String replace_string_second = replace_string_first.replace(")", "");
			
			String[] split_var = replace_string_second.split(",");

			double lat = Double.parseDouble(split_var[0]);
			double lang = Double.parseDouble(split_var[1]);
			SAVED_POSITION = new LatLng(lat,lang);
			cursor.close();
		}
	}

	private void simpan() {
		String nama_tempat = EdNamaTempat.getText().toString();
		String posisi = EdPosisiLatLang.getText().toString();
		String nama_jalan = EdNamaJalan.getText().toString();

		if (id != null) {
			sqliteDB.updateData(id, sqliteDB.ambilDataTempatMakan(nama_tempat,posisi,nama_jalan),"tbl_tempat_makan","_id");
		}
		else {
			id = sqliteDB.insertData(sqliteDB.ambilDataTempatMakan(nama_tempat,posisi,nama_jalan),"tbl_tempat_makan");
		}
	}

	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putLong(SIMPAN_DATA, id);
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
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(SAVED_POSITION, 15));
	    mMap.addMarker(new MarkerOptions().position(SAVED_POSITION));
    }

    @Override
    public void onMapClick(LatLng point) {
        EdPosisiLatLang.setText(point.toString());
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
