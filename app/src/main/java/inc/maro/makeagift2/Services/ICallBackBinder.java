package inc.maro.makeagift2.Services;

import java.util.ArrayList;

import inc.maro.makeagift2.Activities.GiftActivity;
import inc.maro.makeagift2.Activities.LobbyActivity;
import inc.maro.makeagift2.Containers.Gift;
import inc.maro.makeagift2.Containers.GiftDisplayed;
import inc.maro.makeagift2.MVP.Models.GiftModel;
import inc.maro.makeagift2.MVP.Presenters.GiftPresenter;
import inc.maro.makeagift2.MVP.Presenters.LobbyPresenter;
import inc.maro.makeagift2.MVP.Views.GiftView;
import inc.maro.makeagift2.MVP.Views.LobbyView;


/**
 * Created by hIT on 23/8/2017.
 */

public interface ICallBackBinder {

    //que acciones lleva a cabo
    void fetchTargetNames(GiftPresenter presenter);
    void createNewGift(String target, String description, String date, String where, GiftPresenter presenter);
    void updateGift(Gift modifiedGift);
    void drawAllGifts(LobbyPresenter presenter);
    void clearTables(); //metodos para debug
    void updateGiftPositions(ArrayList<GiftDisplayed> gifts);
    void deleteGift(Gift possibleGift);
    void getGiftById(Integer currentGift, GiftPresenter presenter);
}
