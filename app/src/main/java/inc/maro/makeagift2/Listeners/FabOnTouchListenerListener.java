package inc.maro.makeagift2.Listeners;

import android.content.Context;
import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;

import inc.maro.makeagift2.Activities.GiftActivity;
import inc.maro.makeagift2.Activities.LobbyActivity;
import inc.maro.makeagift2.Containers.Gift;
import inc.maro.makeagift2.Helpers.ScreenSizeHelper;
import inc.maro.makeagift2.Services.BehaviourService;
import inc.maro.makeagift2.Services.ICallBackBinder;
import inc.maro.makeagift2.Services.Serviceable;

/**
 * Created by hIT on 25/8/2017.
 */

public class FabOnTouchListenerListener implements View.OnTouchListener
{
    private static LobbyActivity lobbyActivity;
    private static float maxX = ScreenSizeHelper.getInstance().getScreenWidth(); // ancho de la pantalla
    private static float maxY = ScreenSizeHelper.getInstance().getScreenHeight(); // alto de la pantalla
    private float paddingY = 50f;
    private float startX;
    private float startY;
    private float startRawX;
    private float startRawY;
    private int lastAction;
    private Gift theGift = null;
    private float valueX = -1;
    private float valueY = -1;
    private int viewWidth = -1;
    private int viewHeight = -1;

    public FabOnTouchListenerListener(LobbyActivity _lobbyActivity, Gift _gift){
        lobbyActivity = _lobbyActivity;
        theGift = _gift;
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        switch (event.getActionMasked()){
            case MotionEvent.ACTION_DOWN:
                startX = view.getX() - event.getRawX();
                startRawX = event.getRawX();
                startRawY = event.getRawY();
                startY = view.getY() - event.getRawY();
                lastAction = MotionEvent.ACTION_DOWN;
                break;

            case MotionEvent.ACTION_MOVE:
                float valueX = event.getRawX() + startX;
                float valueY = event.getRawY() + startY;
                viewWidth = view.getWidth();
                viewHeight = view.getHeight();

                if (valueX > maxX - view.getWidth())
                    valueX = maxX - view.getWidth();
                if (valueX < 0)
                    valueX = 0;
                if (valueY > maxY - view.getHeight() - paddingY)
                    valueY = maxY - view.getHeight() - paddingY;
                if (valueY < 0)
                    valueY = 0;

                view.setY(valueY);
                view.setX(valueX);
                if (theGift != null) {
                    theGift.setX(valueX / (maxX - viewWidth)); // seteo los nuevos valores de donde esta en la pantalla en porcentaje a los limites, tanto de x como de y
                    theGift.setY(valueY / (maxY - viewHeight - paddingY));
                }
                lastAction = MotionEvent.ACTION_MOVE;
                break;

            case MotionEvent.ACTION_UP:
                float distance = (float) Math.pow(Math.pow(event.getRawX() - startRawX, 2) + Math.pow(event.getRawY() - startRawY, 2), 2);
                if (Math.abs(distance) < 1 && lastAction == MotionEvent.ACTION_MOVE){
                    Intent i = new Intent(lobbyActivity.getApplicationContext(), GiftActivity.class);
                    if (theGift != null){
                        ScreenSizeHelper.getInstance().setWidthView(viewWidth);
                        ScreenSizeHelper.getInstance().setHeightView(viewHeight);
                        i.putExtra(Gift.SAVED_GIFT, theGift);
                    }
                    lobbyActivity.startActivity(i);
                }
                break;
            default:
                return false;
        }
        return true;
    }
}
