package inc.maro.makeagift2.MVP.Models;

import java.util.ArrayList;

import inc.maro.makeagift2.Containers.Gift;
import inc.maro.makeagift2.Containers.GiftDisplayed;
import inc.maro.makeagift2.MVP.Presenters.LobbyPresenter;
import inc.maro.makeagift2.Services.ICallBackBinder;

public class LobbyModel {
    private ICallBackBinder service = null;
    private ArrayList<GiftDisplayed> displayedGifts = new ArrayList<>();

    public LobbyModel(ICallBackBinder service) {
        this.service = service;
    }

    public void bindService(ICallBackBinder serv) {
        this.service = serv;
    }

    public void getAllGifts(LobbyPresenter presenter) {
        if (service != null) {
             service.drawAllGifts(presenter);
        }
    }

    public void deleteGift(Gift gift) {
        if (service != null) {
             service.deleteGift(gift);
        }
    }

    public void updateGiftPositions() {
        if (service != null)
            service.updateGiftPositions(displayedGifts); //cuando se crea un nuevo gift, se llega aca, para actualizar las posicioens en la pantalla de los regalos
    }

    public void setCurrentGifts(ArrayList<GiftDisplayed> currentGifts) {
        displayedGifts.clear();
        displayedGifts.addAll(currentGifts);
    }
}
