package dlmbg.pckg.wisata.kuliner;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;

public class DashboardActivity extends Activity {
	SessionManager session;
	Button btnTempatMakan, btnMakanan, btnLogin, btnPeta, btnAbout;

	public static final String EXTRA_ROWID = "rowid";
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_layout);

        File direktori_gambar = new File(Environment.getExternalStorageDirectory().toString()+"/app_wisata_kuliner/");
        direktori_gambar.mkdirs();

        session = new SessionManager(getApplicationContext());
		
 		Button tmb_dashboard = (Button) findViewById(R.id.btn_dashboard);
 		tmb_dashboard.setOnClickListener(new View.OnClickListener() {
 			@Override
 			public void onClick(View view) {
 				StartAdmin();
 			}
 		});

		if (session.isLoggedIn() == true) 
		{
			tmb_dashboard.setText("Log Out");
		}
 		
        Button tmb_peta = (Button) findViewById(R.id.btn_peta);
        tmb_peta.setOnClickListener(new View.OnClickListener() {
 			@Override
 			public void onClick(View view) {
 				StartPeta();
 			}
 		});

        Button tmb_about = (Button) findViewById(R.id.btn_about);
 		tmb_about.setOnClickListener(new View.OnClickListener() {
 			@Override
 			public void onClick(View view) {
 				StartAbout();
 			}
 		});

        Button tmb_tempat_makan = (Button) findViewById(R.id.btn_data_tempat_makan);
        tmb_tempat_makan.setOnClickListener(new View.OnClickListener() {
 			@Override
 			public void onClick(View view) {
 				StartTempatMakan();
 			}
 		});

        Button tmb_makanan = (Button) findViewById(R.id.btn_data_makanan);
        tmb_makanan.setOnClickListener(new View.OnClickListener() {
 			@Override
 			public void onClick(View view) {
 				StartMakanan();
 			}
 		});
    }
    
    public void StartPeta() {
		Intent intent = new Intent(this, Peta.class);
		startActivity(intent);
	}
    
    public void StartAbout() {
		Intent intent = new Intent(this, About.class);
		startActivity(intent);
	}
    
    public void StartTempatMakan() {
		Intent intent = new Intent(this, TempatMakan.class);
		startActivity(intent);
	}
    
    public void StartMakanan() {
		Intent intent = new Intent(this, Makanan.class);
		startActivity(intent);
	}
    
    public void StartAdmin() {
    	if(session.isLoggedIn() == true)
    	{
    		session.logoutUser();
    		finish();
    	}
    	else
    	{
    		Intent intent = new Intent(this, Admin.class);
    		startActivity(intent);
    		finish();
    	}
	}
}
