package inc.maro.makeagift2.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import inc.maro.makeagift2.MVP.Models.NewGiftModel;
import inc.maro.makeagift2.MVP.Presenters.NewGiftPresenter;
import inc.maro.makeagift2.MVP.Views.NewGiftView;
import inc.maro.makeagift2.R;
import inc.maro.makeagift2.Services.DatabaseService;
import inc.maro.makeagift2.Services.DatabaseServiceConnector;
import inc.maro.makeagift2.Services.ICallBackBinder;
import inc.maro.makeagift2.Services.Serviceable;

public class NewGiftActivity extends AppCompatActivity implements Serviceable {

    private DatabaseServiceConnector behaviourServerServiceConnector = new DatabaseServiceConnector(NewGiftActivity.this);
    private ICallBackBinder service = null;
    private NewGiftPresenter presenter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gift);
        connectService();
    }

    @Override
    public void setCallBackBinder(ICallBackBinder _service) {
        this.service = _service;
        presenter = new NewGiftPresenter(new NewGiftModel(_service), new NewGiftView(this));
        presenter.register();
        presenter.bindService(_service);
        presenter.fetchTargetsNames();
    }

    @Override
    public void connectService() {
        getApplicationContext().bindService(new Intent(NewGiftActivity.this, DatabaseService.class), behaviourServerServiceConnector, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onBackPressed(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.close_gift_activity));
        builder.setMessage(getResources().getString(R.string.confirm_close_gift));

        builder.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });

        builder.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){
            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
        }
        else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        if (presenter != null) {
            presenter.unregister();
            presenter.bindService(null);
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        if (presenter != null)
            presenter.register();
        if (service != null)
            presenter.bindService(service);
    }



}
