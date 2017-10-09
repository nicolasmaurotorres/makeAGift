package inc.maro.makeagift2.Services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;

import inc.maro.makeagift2.Activities.GiftActivity;
import inc.maro.makeagift2.Containers.Gift;
import inc.maro.makeagift2.Containers.GiftDisplayed;
import inc.maro.makeagift2.Helpers.DatabaseHelper;
import inc.maro.makeagift2.MVP.Presenters.GiftPresenter;
import inc.maro.makeagift2.MVP.Presenters.LobbyPresenter;

import static android.content.ContentValues.TAG;

/**
 * Created by hIT on 23/8/2017.
 */

public class DatabaseService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent arg0)
    {
        return new DatabaseCallBackBinder(this);
    }


    @Override
    public void onCreate(){
        super.onCreate();
    }

    public void fillTargetNames(GiftPresenter presenter){
        presenter._fillTargetNames(DatabaseHelper.getInstance(this).getTargets());
    }

    public void createNewGift(String target, String description, String date, String where, GiftPresenter presenter){
        DatabaseHelper.getInstance(this).createGift(target,description,date,where);
        presenter._createNewGift();
    }

    public void updateModifiedGift(Gift modifiedGift){
        DatabaseHelper.getInstance(this).updateGifts(modifiedGift);
    }

    public void drawAllGifts(LobbyPresenter presenter){
        presenter._drawAllGifts(DatabaseHelper.getInstance(this).getGifts());
    }

    public void clearTables()
    {
        DatabaseHelper.getInstance(this).clearTables();
    }

    public void updateGiftPositions(ArrayList<GiftDisplayed> gifts) {
        DatabaseHelper.getInstance(this).updateGiftsPositions(gifts);
    }

    public void deleteGift(Gift possibleGift) {
        DatabaseHelper.getInstance(this).deleteGift(possibleGift);
    }

    public void getGiftById(Integer currentGift, GiftPresenter presenter) {
        Gift toReturn = DatabaseHelper.getInstance(this).getGiftById(currentGift);
        presenter._setGift(toReturn);
    }
}