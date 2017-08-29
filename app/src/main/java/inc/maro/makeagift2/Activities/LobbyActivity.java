package inc.maro.makeagift2.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
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

public class LobbyActivity extends AppCompatActivity implements Serviceable
{
    private BehaviourServiceConnector behaviourServerServiceConnector = new BehaviourServiceConnector(LobbyActivity.this);
    private ICallBackBinder service = null;
    private static ArrayList<Gift> gifts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);
        connectService();

        ScreenSizeHelper.setActivity(this);
        final FloatingActionButton fab2 = (FloatingActionButton) findViewById(R.id.fabNewGift);
        final ViewGroup contentLobby = (ViewGroup) findViewById(R.id.coordinatorLayout);
        //checkeo si tengo que agregar algun nuevo regalo
        if (getIntent().hasExtra(Gift.NEW_GIFT))
        {
            Gift possibleGift = (Gift) getIntent().getExtras().get(Gift.NEW_GIFT);
            gifts.add(possibleGift);
            LayoutInflater inflater = LayoutInflater.from(this.getApplicationContext());
            FloatingActionButton fab  = (FloatingActionButton) inflater.inflate(R.layout.fab_button,contentLobby, false);
            fab.setOnTouchListener(new FabOnTouchListenerListener(LobbyActivity.this,possibleGift));
            contentLobby.addView(fab); // lo agrego al ViewGroup que lo contiene
        }

        //checkeo si cambio algun regalo que se edito
        if (getIntent().hasExtra(Gift.EDITED_GIFT))
        {
            Gift editedGift = (Gift) getIntent().getExtras().get(Gift.EDITED_GIFT);
            gifts.get(gifts.indexOf(editedGift)).updateData(editedGift);
        }
        fab2.setOnTouchListener(new FabOnTouchListenerListener(this,null));
    }

    @Override
    public void setCallBackBinder(ICallBackBinder serv)
    {
        this.service = serv;
        service.arrangeFloatingActions();
    }

    @Override
    public void connectService()
    {
        getApplicationContext().bindService(new Intent(LobbyActivity.this, BehaviourService.class), behaviourServerServiceConnector, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void showToastMessage(String message)
    {
        Toast.makeText(getApplicationContext(),message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        finish();
        getApplicationContext().unbindService(behaviourServerServiceConnector);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE)
        {
            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();

        }
        else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT)
        {
            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
        }
    }
}
