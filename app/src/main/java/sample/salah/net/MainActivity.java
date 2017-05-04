package sample.salah.net;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import net.salah.locate.Locate;

public class MainActivity extends AppCompatActivity implements Locate.LocationResult {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.get).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Locate.with(MainActivity.this)
                        .addLocationResultListener(MainActivity.this)
                        .build();
            }
        });

    }



    @Override
    public void onGetLocationSuccess(double lat, double lng) {
        ((TextView)findViewById(R.id.result)).setText(getString(R.string.lat)+" : "+lat+"\n"+getString(R.string.lng)+" : "+lng);
    }

    @Override
    public void onGetLocationFailed(@Nullable String message) {
        ((TextView)findViewById(R.id.result)).setText(message);
    }




}
