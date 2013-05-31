package dlmbg.pckg.wisata.kuliner;

import java.io.File;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

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
public class TambahMakanan extends FragmentActivity
        implements OnMapClickListener, OnMapLongClickListener, OnCameraChangeListener {

	private SqliteManager sqliteDB;
	private Long id;
	public static final String SIMPAN_DATA = "simpan";
	
	private ImageView mImageView;	
	private Uri mImageCaptureUri;	
	
	private static final int PICK_FROM_CAMERA = 1;
	private static final int PICK_FROM_FILE = 2;
	
    private GoogleMap mMap;
    private EditText EdPosisiLatLang,EdNamaTempat,EdNamaMakanan,EdHarga,EdGambar;
	String var_posisi,var_nama_makanan,var_nama_jalan,var_harga,var_gambar;
    public LatLng SAVED_POSITION = new LatLng(3.584695,98.675079);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_makanan);

        EdPosisiLatLang = (EditText) findViewById(R.id.ed_posisi_makanan);
        EdNamaTempat = (EditText) findViewById(R.id.ed_nama_tempat_makanan);
        EdNamaMakanan = (EditText) findViewById(R.id.ed_nama_makanan);
        EdHarga = (EditText) findViewById(R.id.ed_harga_makanan);
        EdGambar = (EditText) findViewById(R.id.ed_gambar_makanan);
        
		mImageView = (ImageView) findViewById(R.id.iv_pic);
        
        id = null;

		if (savedInstanceState == null) {
			Bundle extras = getIntent().getExtras();
			if (extras != null && extras.containsKey(Makanan.EXTRA_ROWID)) {
				id = extras.getLong(Makanan.EXTRA_ROWID);
			}
			else
			{
				var_nama_makanan = extras.getString("nama_makanan");
				var_posisi = extras.getString("lat_lang");
				var_nama_jalan = extras.getString("nama_jalan");
				var_harga = extras.getString("harga");
				var_gambar = extras.getString("gambar");
			}
		}
		
		sqliteDB = new SqliteManager(this);
		sqliteDB.bukaKoneksi();
		
        pindahData();

		Button button = (Button) findViewById(R.id.btn_simpan_makanan);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				simpan();
				finish();
			}
		});
		

		final String [] items			= new String [] {"From Camera", "From SD Card"};				
		ArrayAdapter<String> adapter	= new ArrayAdapter<String> (this, android.R.layout.select_dialog_item,items);
		AlertDialog.Builder builder		= new AlertDialog.Builder(this);
		
		builder.setTitle("Select Image");
		builder.setAdapter( adapter, new DialogInterface.OnClickListener() {
			public void onClick( DialogInterface dialog, int item ) {
				if (item == 0) {
					Intent intent 	 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					File file		 = new File(Environment.getExternalStorageDirectory().toString(),
							   			"app_wisata_kuliner/tmp_avatar_" + String.valueOf(System.currentTimeMillis()) + ".jpg");
					mImageCaptureUri = Uri.fromFile(file);

					try {			
						intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
						intent.putExtra("return-data", true);
						
						startActivityForResult(intent, PICK_FROM_CAMERA);
					} catch (Exception e) {
						e.printStackTrace();
					}			
					
					dialog.cancel();
				} else {
					Intent intent = new Intent();
					
	                intent.setType("image/*");
	                intent.setAction(Intent.ACTION_GET_CONTENT);
	                
	                startActivityForResult(Intent.createChooser(intent, "Complete action using"), PICK_FROM_FILE);
				}
			}
		} );

        setUpMapIfNeeded();
		
		final AlertDialog dialog = builder.create();
		
		((Button) findViewById(R.id.btn_choose)).setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				dialog.show();
			}
		});
    }
	
    @Override
 	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
 	    if (resultCode != RESULT_OK) return;
 	   
 		Bitmap bitmap 	= null;
 		String path		= "";
 		
 		if (requestCode == PICK_FROM_FILE) {
 			mImageCaptureUri = data.getData(); 
 			path = getRealPathFromURI(mImageCaptureUri); //from Gallery 
 		
 			if (path == null)
 				path = mImageCaptureUri.getPath(); //from File Manager
 			
 			if (path != null) 
 				bitmap 	= BitmapFactory.decodeFile(path);
 		} else {
 			path	= mImageCaptureUri.getPath();
 			bitmap  = BitmapFactory.decodeFile(path);
 		}
 		EdGambar.setText(path);
 		mImageView.setImageBitmap(bitmap);		
 	}
 	
 	public String getRealPathFromURI(Uri contentUri) {
         String [] proj 		= {MediaStore.Images.Media.DATA};
         @SuppressWarnings("deprecation")
 		Cursor cursor 		= managedQuery( contentUri, proj, null, null,null);
         
         if (cursor == null) return null;
         
         int column_index 	= cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
         
         cursor.moveToFirst();

         return cursor.getString(column_index);
 	}
    
    @Override
	protected void onDestroy() {
		super.onDestroy();
		sqliteDB.tutupKoneksi();
	}

	private void pindahData() {
		if (id != null) {
			Cursor cursor = sqliteDB.bacaDataTerseleksiMakanan(id);
			EdNamaTempat.setText(cursor.getString(1));
			EdPosisiLatLang.setText(cursor.getString(2));
			EdNamaMakanan.setText(cursor.getString(3));
			EdHarga.setText(cursor.getString(4));
			EdGambar.setText(cursor.getString(5));
			
			Bitmap bitmap = BitmapFactory.decodeFile(cursor.getString(5));
			mImageView.setImageBitmap(bitmap);
			
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
		String nama_makanan = EdNamaMakanan.getText().toString();
		String gambar = EdGambar.getText().toString();
		String harga = EdHarga.getText().toString();

		if (id != null) {
			sqliteDB.updateData(id, sqliteDB.ambilDataMakanan(nama_tempat, posisi, nama_makanan, harga, gambar),"tbl_makanan","_id");
		}
		else {
			id = sqliteDB.insertData(sqliteDB.ambilDataMakanan(nama_tempat, posisi, nama_makanan, harga, gambar),"tbl_makanan");
		}
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
