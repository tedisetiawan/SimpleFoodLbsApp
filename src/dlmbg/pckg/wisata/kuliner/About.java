package dlmbg.pckg.wisata.kuliner;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/*
 * Gede Lumbung - 2013
 * http://gedelumbung.com
 */

public class About extends Activity {

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.about);

 		TextView txt_nama = (TextView) findViewById(R.id.txt_nama_about);
 		TextView txt_npm = (TextView) findViewById(R.id.txt_npm_about);
 		TextView txt_jurusan = (TextView) findViewById(R.id.txt_jurusan_about);
 		TextView txt_kampus = (TextView) findViewById(R.id.txt_kampus_about);

 		txt_nama.setText("Rathika Dwinasari");
 		txt_npm.setText("08350068");
 		txt_jurusan.setText("Teknik Informatika");
 		txt_kampus.setText("STT Harapan Medan");

    }
}
