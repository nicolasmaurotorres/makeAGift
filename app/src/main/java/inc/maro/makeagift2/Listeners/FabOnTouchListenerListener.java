package inc.maro.makeagift2.Listeners;

import android.content.Intent;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import inc.maro.makeagift2.Activities.GiftActivity;
import inc.maro.makeagift2.Activities.LobbyActivity;
import inc.maro.makeagift2.Containers.GiftDisplayed;
import inc.maro.makeagift2.Helpers.ScreenSizeHelper;

/**
 * Created by hIT on 25/8/2017.
 */

public class FabOnTouchListenerListener implements View.OnTouchListener
{
    private static LobbyActivity lobbyActivity;
    private float startX;
    private float startY;
    private float startRawX;
    private float startRawY;
    private int lastAction;
    private GiftDisplayed theGift = null;
    private int viewWidth = -1;
    private int viewHeight = -1;

    public FabOnTouchListenerListener(LobbyActivity lobbyActivity, GiftDisplayed gift){
        this.lobbyActivity = lobbyActivity;
        this.theGift = gift;
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        ScreenSizeHelper.getInstance().setWidthView(viewWidth);
        ScreenSizeHelper.getInstance().setHeightView(viewHeight);
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

                if (valueX > ScreenSizeHelper.getInstance().getScreenWidth())
                    valueX = ScreenSizeHelper.getInstance().getScreenWidth();
                if (valueX < 0)
                    valueX = 0;
                if (valueY > ScreenSizeHelper.getInstance().getScreenHeight())
                    valueY = ScreenSizeHelper.getInstance().getScreenHeight();
                if (valueY < 0)
                    valueY = 0;

                view.setY(valueY);
                view.setX(valueX);
                if (theGift != null) {
                    theGift.setX(ScreenSizeHelper.getInstance().getXPercentage(valueX)); // seteo los nuevos valores de donde esta en la pantalla en porcentaje a los limites, tanto de x como de y
                    theGift.setY(ScreenSizeHelper.getInstance().getYPercentage(valueY));
                }
                lastAction = MotionEvent.ACTION_MOVE;
                break;

            case MotionEvent.ACTION_UP:
                float distance = (float) Math.pow(Math.pow(event.getRawX() - startRawX, 2) + Math.pow(event.getRawY() - startRawY, 2), 2);
                if (Math.abs(distance) < 1 && lastAction == MotionEvent.ACTION_DOWN){
                    Intent i = new Intent(lobbyActivity.getApplicationContext(), GiftActivity.class);
                    i.putExtra(GiftDisplayed.GIFT_ID,theGift.getId());
                    lobbyActivity.startActivity(i);
                }
                break;

            default:
                return false;
        }
        return true;
    }
}
