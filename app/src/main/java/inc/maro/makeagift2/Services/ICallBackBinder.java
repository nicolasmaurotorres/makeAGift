package inc.maro.makeagift2.Services;

import java.util.ArrayList;

import inc.maro.makeagift2.Activities.GiftActivity;
import inc.maro.makeagift2.Activities.LobbyActivity;
import inc.maro.makeagift2.Containers.Gift;


/**
 * Created by hIT on 23/8/2017.
 */

public interface ICallBackBinder {

    //que acciones lleva a cabo
    void fillTargetNames(GiftActivity activity);
    void createNewGift(String target, String description, String date, String where, GiftActivity activity);
    void updateGift(Gift modifiedGift);
    void createNewTarget(Gift possibleGift, GiftActivity activity);
    void updateTarget(long idGift, int idTarget);
    void drawAllGifts(ArrayList<Gift> notThisOnes, LobbyActivity activity);
    void clearTables(); //metodos para debug
    void updateGiftPositions(ArrayList<Gift> gifts);
}
