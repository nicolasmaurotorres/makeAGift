package inc.maro.makeagift2.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.widget.Toast;

import inc.maro.makeagift2.MVP.Models.LobbyModel;
import inc.maro.makeagift2.MVP.Presenters.LobbyPresenter;
import inc.maro.makeagift2.MVP.Views.LobbyView;
import inc.maro.makeagift2.R;
import inc.maro.makeagift2.Services.DatabaseService;
import inc.maro.makeagift2.Services.DatabaseServiceConnector;
import inc.maro.makeagift2.Services.ICallBackBinder;
import inc.maro.makeagift2.Services.Serviceable;

public class LobbyActivity extends AppCompatActivity implements Serviceable {
    private DatabaseServiceConnector behaviourServerServiceConnector = new DatabaseServiceConnector(LobbyActivity.this);
    private ICallBackBinder service = null;

    private LobbyPresenter presenter =  null;

    private boolean CLEAR_TABLES = false; // OPCION DE DEBUG

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);
        connectService(); // conexion con el servicio
    }

    @Override
    public void setCallBackBinder(ICallBackBinder service){
        this.service = service;
        if (CLEAR_TABLES) {
            this.service.clearTables();
        }
        presenter = new LobbyPresenter(new LobbyModel(service), new LobbyView(this));
        presenter.drawAllGifts();
    }

    @Override
    protected void onPause(){
        super.onPause();
        if (presenter != null) {
            presenter.updateGiftPositions();
            presenter.bindService(null);
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        if (service != null)
            presenter.bindService(service);
    }

    @Override
    public void connectService(){
        getApplicationContext().bindService(new Intent(LobbyActivity.this, DatabaseService.class), behaviourServerServiceConnector, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){         // Checks the orientation of the screen
            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        presenter.updateGiftPositions();
        presenter.bindService(null);
        finish();
        getApplicationContext().unbindService(behaviourServerServiceConnector);
    }
}
