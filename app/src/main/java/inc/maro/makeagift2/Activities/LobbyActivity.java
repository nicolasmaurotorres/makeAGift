package inc.maro.makeagift2.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import inc.maro.makeagift2.Containers.Gift;
import inc.maro.makeagift2.Helpers.ScreenSizeHelper;
import inc.maro.makeagift2.Listeners.FabOnTouchListenerListener;
import inc.maro.makeagift2.R;
import inc.maro.makeagift2.Services.BehaviourService;
import inc.maro.makeagift2.Services.BehaviourServiceConnector;
import inc.maro.makeagift2.Services.ICallBackBinder;
import inc.maro.makeagift2.Services.Serviceable;

public class LobbyActivity extends AppCompatActivity implements Serviceable {
    private BehaviourServiceConnector behaviourServerServiceConnector = new BehaviourServiceConnector(LobbyActivity.this);
    private ICallBackBinder service = null;
    private ArrayList<Gift> gifts = new ArrayList<>();

    private boolean CLEAR_TABLES = false; // OPCION DE DEBUG

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);
        connectService(); // conexion con el servicio
        ScreenSizeHelper.setActivity(this);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabNewGift);
        final ViewGroup contentLobby = (ViewGroup) findViewById(R.id.coordinatorLayout);
        fab.setOnTouchListener(new FabOnTouchListenerListener(this,null));
        fab.setBackgroundTintMode(PorterDuff.Mode.LIGHTEN);

        //checkeo si tengo que agregar algun nuevo regalo
        if (getIntent().hasExtra(Gift.NEW_GIFT)){
            Gift possibleGift = (Gift) getIntent().getExtras().get(Gift.NEW_GIFT);
            gifts.add(possibleGift);
            drawFab(contentLobby,possibleGift);
        }

        //checkeo si cambio algun regalo que se edito
        if (getIntent().hasExtra(Gift.EDITED_GIFT)){
            Gift editedGift = (Gift) getIntent().getExtras().get(Gift.EDITED_GIFT);
            gifts.get(gifts.indexOf(editedGift)).updateData(editedGift);
        }
    }

    @Override
    public void setCallBackBinder(ICallBackBinder serv){
        this.service = serv;
        if (CLEAR_TABLES)
           service.clearTables();
        service.drawAllGifts(gifts,this);
    }

    @Override
    protected void onPause(){
        super.onPause();
        service.updateGiftPositions(this.gifts); //cuando se crea un nuevo gift, se llega aca, para actualizar las posicioens en la pantalla de los regalos
    }

    @Override
    public void connectService(){
        getApplicationContext().bindService(new Intent(LobbyActivity.this, BehaviourService.class), behaviourServerServiceConnector, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void showToastMessage(String message){
        Toast.makeText(getApplicationContext(),message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){         // Checks the orientation of the screen
            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
        }
        else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        finish();
        getApplicationContext().unbindService(behaviourServerServiceConnector);
    }

    // ********************************************************************************** CALLBACKS **********************************************************************************
    public void drawGiftsCallBack(ArrayList<Gift> allGift){
        final ViewGroup contentLobby = (ViewGroup) findViewById(R.id.coordinatorLayout);
        for (Gift g: allGift){
            gifts.add(g);
            drawFab(contentLobby,g);
        }
    }

    private void drawFab(ViewGroup contentLobby, final Gift possibleGift) {
        LayoutInflater inflater = LayoutInflater.from(this.getApplicationContext());
        FloatingActionButton fab = (FloatingActionButton) inflater.inflate(R.layout.fab_button, contentLobby, false);
        fab.setOnTouchListener(new FabOnTouchListenerListener(LobbyActivity.this, possibleGift));
        fab.setX(ScreenSizeHelper.getInstance().getScreenXPosition(possibleGift.getX()));
        fab.setY(ScreenSizeHelper.getInstance().getScreenYPosition(possibleGift.getY()));
        fab.setVisibility(View.VISIBLE);
        contentLobby.addView(fab); // lo agrego al ViewGroup que lo contiene
    }
}
