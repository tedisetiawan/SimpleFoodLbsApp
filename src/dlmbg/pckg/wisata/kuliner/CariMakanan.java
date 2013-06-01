package dlmbg.pckg.wisata.kuliner;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class CariMakanan extends ListActivity {
	private SqliteManager sqliteDB;
	private SimpleCursorAdapter mCursorAdapter;
	private String cari;
	private EditText cari_et;
	SessionManager session;

    @SuppressWarnings("deprecation")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.list_makanan);
        sqliteDB = new SqliteManager(this);
        sqliteDB.bukaKoneksi();
        
		Bundle extras = getIntent().getExtras();
		cari = extras.getString("cari_data");
		Cursor cursor = sqliteDB.bacaDataPencarianMakanan(cari);
		
		cari_et = (EditText) findViewById(R.id.cari_makanan);
		cari_et.setText(cari);

		startManagingCursor(cursor);

		String[] awal = new String[] { "nama_makanan" };
		int[] tujuan = new int[] { R.id.rowtext };
		mCursorAdapter = new SimpleCursorAdapter(this, R.layout.baris, cursor, awal, tujuan);

		mCursorAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
			public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
				if (columnIndex == SqliteManager.POSISI_ID) {
					TextView textView = (TextView) view;
						textView.setText("");
					return true;
			    }
			    return false;
			}
			});

		setListAdapter(mCursorAdapter);

		session = new SessionManager(getApplicationContext());
		if (session.isLoggedIn() == true) 
		{
			registerForContextMenu(getListView());
		}
		
		Button button = (Button) findViewById(R.id.btn_cari_makanan);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
	    		CariData();
				finish();
			}
		});

    }

	@Override
	protected void onDestroy() {
		super.onDestroy();
		sqliteDB.tutupKoneksi();
	}

    public boolean onCreateOptionsMenu(Menu menu) {
    	if (session.isLoggedIn() == true) 
		{
	        MenuInflater inflater = getMenuInflater();
	        inflater.inflate(R.menu.opt_makanan, menu);
		}
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        	case R.id.tambah_makanan:
        		Intent intent = new Intent(this, TambahMakanan.class);
        		intent.putExtra("nama_tempat", "");
        		intent.putExtra("lat_lang", "");
        		intent.putExtra("nama_makanan", "");
        		intent.putExtra("harga", "");
        		intent.putExtra("gambar", "");
        		startActivity(intent);
        		return true;
        	default:
        		return super.onOptionsItemSelected(item);
        }
    }

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.list_menu, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		switch (item.getItemId()) {
			case R.id.menu_edit:
				startDetail(info.id, false);
				return true;

			case R.id.menu_delete:
				hapus(info.id);
				return true;
			default:
				return super.onContextItemSelected(item);
		}
	}

    @SuppressWarnings("deprecation")
	public void hapus(long rowId) {
    	sqliteDB.hapusData(rowId, "tbl_makanan", "_id");
		mCursorAdapter.getCursor().requery();
    }

	public static final String EXTRA_ROWID = "rowid";

	@Override
	protected void onListItemClick(ListView l, View v, int position, long rowId) {
		super.onListItemClick(l, v, position, rowId);
		tampilTempatTerseleksi(rowId);

	}

	public void tampilTempatTerseleksi(Long mRowId) {
		Cursor cursor = sqliteDB.bacaDataTerseleksiMakanan(mRowId);
		Intent intent = new Intent(this, DetailMakanan.class);
		intent.putExtra("id_makanan", cursor.getString(0));
		intent.putExtra("nama_tempat", cursor.getString(1));
		intent.putExtra("lat_lang", cursor.getString(2));
		intent.putExtra("nama_makanan", cursor.getString(3));
		intent.putExtra("harga", cursor.getString(4));
		intent.putExtra("gambar", cursor.getString(5));
		startActivity(intent);

	}

	public void startDetail(long rowId, boolean baru) {
		Intent intent = new Intent(this, TambahMakanan.class);
		if (!baru) {
			intent.putExtra(EXTRA_ROWID, rowId);
		}
		startActivity(intent);
	}

	public void CariData() {
		Intent intent = new Intent(this, CariMakanan.class);
		intent.putExtra("cari_data", cari_et.getText().toString());
		startActivity(intent);
	}
}