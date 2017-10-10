package inc.maro.makeagift2.MVP.Models;

import inc.maro.makeagift2.MVP.Presenters.NewGiftPresenter;
import inc.maro.makeagift2.OttoBus.Events.SaveNewGiftEvent;
import inc.maro.makeagift2.Services.ICallBackBinder;

public class NewGiftModel extends AbstractGiftModel {

    private ICallBackBinder service = null;

    public NewGiftModel(ICallBackBinder service) {
        this.service = service;
    }

    public void addNewGift(SaveNewGiftEvent event, NewGiftPresenter newGiftPresenter) {
        //todo checkear la localizacion
        if (isDataEmpty(event)) {
            service.createNewGift(event.getTarget(), event.getDescription(), event.getDate(), "", newGiftPresenter);
        }
    }
}
