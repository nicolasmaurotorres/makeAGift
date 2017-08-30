package inc.maro.makeagift2.Services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;

import inc.maro.makeagift2.Activities.GiftActivity;
import inc.maro.makeagift2.Activities.LobbyActivity;
import inc.maro.makeagift2.Containers.Gift;
import inc.maro.makeagift2.Helpers.DatabaseHelper;

import static android.content.ContentValues.TAG;

/**
 * Created by hIT on 23/8/2017.
 */

public class BehaviourService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent arg0)
    {
        return new BehaviourCallBackBinder(this);
    }


    @Override
    public void onCreate(){
        super.onCreate();
    }

    public void fillTargetNames(GiftActivity activity)
    {
        activity.callBackFillTargetNames(DatabaseHelper.getInstance(this).getTargets());
    }

    public void createNewGift(String target, String description, String date, String where, GiftActivity activity)
    {
        long id = DatabaseHelper.getInstance(this).createGift(target,description,date,where);
        activity.callBackNewGift(new Gift(id,target,description,where,date));
    }

    public void updateModifiedGift(Gift modifiedGift)
    {
        DatabaseHelper.getInstance(this).updateGifts(modifiedGift);
    }

    public void creteNewTarget(Gift possibleGift, GiftActivity activity)
    {
        long idTarget = DatabaseHelper.getInstance(this).createTarget(possibleGift.getTarget());
        if (idTarget !=  -1)
        {
            possibleGift.setId(idTarget);
            activity.callBackNewTarget(possibleGift);
        }
        else
        {
            Log.d(TAG,"Error al agregar un nuevo target");
        }
    }

    public void updateIdTargetGift(long idGift, int idTarget)
    {
        DatabaseHelper.getInstance(this).updateIdTargetGift(idGift,idTarget);
    }

    public void drawAllGifts(ArrayList<Gift> notThisOnes, LobbyActivity activity)
    {
        ArrayList<Gift> giftsNotDrawn = DatabaseHelper.getInstance(this).getAllGift(notThisOnes);
        activity.drawGiftsCallBack(giftsNotDrawn);
    }

    public void clearTables()
    {
        DatabaseHelper.getInstance(this).clearTables();
    }

    public void updateGiftPositions(ArrayList<Gift> gifts) {
        DatabaseHelper.getInstance(this).updateGiftsPositions(gifts);
    }

    public void deleteGift(Gift possibleGift) {
        DatabaseHelper.getInstance(this).deleteGift(possibleGift);
    }
}
