package inc.maro.makeagift2.MVP.Presenters;

import java.util.ArrayList;

import inc.maro.makeagift2.Containers.GiftDisplayed;
import inc.maro.makeagift2.MVP.Models.LobbyModel;
import inc.maro.makeagift2.MVP.Views.LobbyView;

import inc.maro.makeagift2.Services.ICallBackBinder;

public class LobbyPresenter {

    private LobbyModel model;
    private LobbyView view;

    public LobbyPresenter(LobbyModel lobbyModel, LobbyView lobbyView) {
        model = lobbyModel;
        view = lobbyView;
    }

    public void bindService(ICallBackBinder serv) {
        model.bindService(serv);
    }

    public void drawAllGifts(){
        model.getAllGifts(this);
    }

    public void _drawAllGifts(ArrayList<GiftDisplayed> serverGifts) {
        model.setCurrentGifts(serverGifts);
        for (GiftDisplayed g : serverGifts) {
            view.drawFab(g);
        }
    }

    public void updateGiftPositions() {
        model.updateGiftPositions();
    }
}

