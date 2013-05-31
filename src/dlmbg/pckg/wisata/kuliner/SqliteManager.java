package dlmbg.pckg.wisata.kuliner;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/*
 * Gede Lumbung - 2013
 * http://gedelumbung.com
 * Just Simple Android CRUD App with Parent Child Content
 */

public class SqliteManager {
	public static final int VERSI_DATABASE= 1;
	public static final String NAMA_DATABASE = "DbWisataKuliner";
	public static final int POSISI_ID = 0;

	public static final String[] FIELD_TABEL_TEMPAT_MAKAN ={"_id","nama_tempat","lat_lang","nama_jalan"};
	public static final String[] FIELD_TABEL_MAKANAN ={"_id", "nama_tempat","lat_lang", "nama_makanan", "harga", "gambar"};

	private Context crudContext;
	private SQLiteDatabase crudDatabase;
	private SqliteManagerHelper crudHelper;

	private static class SqliteManagerHelper extends SQLiteOpenHelper {

		private static final String TABEL_TEMPAT_MAKAN =
			"CREATE TABLE IF NOT EXISTS tbl_tempat_makan (" +
			"_id integer primary key autoincrement," +
			"nama_tempat text NOT NULL," +
			"lat_lang text NOT NULL," +
			"nama_jalan text NOT NULL)";

		private static final String TABEL_MAKANAN =
			"CREATE TABLE IF NOT EXISTS tbl_makanan (" +
			"_id integer primary key autoincrement," +
			"nama_tempat text NOT NULL," +
			"lat_lang text NOT NULL," +
			"nama_makanan text NOT NULL," +
			"harga text NOT NULL," +
			"gambar text NOT NULL)";
		
		public SqliteManagerHelper(Context context) {
			super(context, NAMA_DATABASE, null, VERSI_DATABASE);
		}

		@Override
		public void onCreate(SQLiteDatabase database) {
			database.execSQL(TABEL_TEMPAT_MAKAN);
			database.execSQL(TABEL_MAKANAN);
		}

		@Override
		public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {}
	}

	public SqliteManager(Context context) {
		crudContext = context;
	}

	public void bukaKoneksi() throws SQLException {
		crudHelper = new SqliteManagerHelper(crudContext);
		crudDatabase = crudHelper.getWritableDatabase();
	}

	public void tutupKoneksi() {
		crudHelper.close();
		crudHelper = null;
		crudDatabase = null;
	}

	public long insertData(ContentValues values, String nama_tabel) {
		return crudDatabase.insert(nama_tabel, null, values);
	}

	public boolean updateData(long rowId, ContentValues values, String nama_tabel, String id_param) {
		return crudDatabase.update(nama_tabel, values,
				id_param + "=" + rowId, null) > 0;
	}

	public boolean hapusData(long rowId, String nama_tabel, String id_param) {
		return crudDatabase.delete(nama_tabel,
				id_param + "=" + rowId, null) > 0;
	}

	public Cursor bacaDataTempatMakan() {
		return crudDatabase.query("tbl_tempat_makan",FIELD_TABEL_TEMPAT_MAKAN,null, null,null, null,"nama_tempat ASC");
	}

	public Cursor bacaDataMakanan() {
		return crudDatabase.query("tbl_makanan",FIELD_TABEL_MAKANAN,null, null,null, null,"nama_makanan ASC");
	}

	public Cursor bacaDataPencarianTempatMakan(String cari) {
		
		return crudDatabase.query("tbl_tempat_makan", FIELD_TABEL_TEMPAT_MAKAN, "nama_tempat like '%"+cari+"%'", null, null, null, null);
	}

	public Cursor bacaDataPencarianMakanan(String cari) {
		
		return crudDatabase.query("tbl_makanan", FIELD_TABEL_MAKANAN, "nama_makanan like '%"+cari+"%'", null, null, null, null);
	}

	public Cursor bacaDataTerseleksiTempatMakan(long rowId) throws SQLException {
		Cursor cursor = crudDatabase.query(true, "tbl_tempat_makan",FIELD_TABEL_TEMPAT_MAKAN,"_id =" + rowId,null, null, null, null, null);
		cursor.moveToFirst();
		return cursor;
	}

	public Cursor bacaDataTerseleksiMakanan(long rowId) throws SQLException {
		Cursor cursor = crudDatabase.query(true, "tbl_makanan",FIELD_TABEL_MAKANAN,"_id =" + rowId,null, null, null, null, null);
		cursor.moveToFirst();
		return cursor;
	}

	public ContentValues ambilDataTempatMakan(String nama_tempat, String lat_lang, String nama_jalan) {
		ContentValues values = new ContentValues();
		values.put("nama_tempat", nama_tempat);
		values.put("lat_lang", lat_lang);
		values.put("nama_jalan", nama_jalan);
		return values;
	}

	public ContentValues ambilDataMakanan(String nama_tempat, String lat_lang, String nama_makanan, String harga, String gambar) {
		ContentValues values = new ContentValues();
		values.put("nama_tempat", nama_tempat);
		values.put("lat_lang", lat_lang);
		values.put("nama_makanan", nama_makanan);
		values.put("harga", harga);
		values.put("gambar", gambar);
		return values;
	}
}
