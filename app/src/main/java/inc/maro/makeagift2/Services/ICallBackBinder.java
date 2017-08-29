package inc.maro.makeagift2.Services;

import inc.maro.makeagift2.Activities.GiftActivity;
import inc.maro.makeagift2.Containers.Gift;


/**
 * Created by hIT on 23/8/2017.
 */

public interface ICallBackBinder {

    //que acciones lleva a cabo
    void arrangeFloatingActions();
    void fillTargetNames(GiftActivity activity);
    void createNewGift(String target, String description, String date, String where, GiftActivity activity);
    void updateGift(Gift modifiedGift);
    void createNewTarget(Gift possibleGift, GiftActivity activity);
    void updateTarget(long idGift, int idTarget);
}
