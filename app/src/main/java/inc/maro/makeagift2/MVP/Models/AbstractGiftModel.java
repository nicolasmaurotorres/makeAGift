package inc.maro.makeagift2.MVP.Models;

import inc.maro.makeagift2.MVP.Presenters.EditGiftPresenter;
import inc.maro.makeagift2.OttoBus.Events.GiftEventSave;
import inc.maro.makeagift2.Services.Bindeable;
import inc.maro.makeagift2.Services.ICallBackBinder;

public abstract class AbstractGiftModel {
    private ICallBackBinder service = null;

    public void bindService(ICallBackBinder service) {
        this.service = service;
    }

    public ICallBackBinder getService(){
        return service;
    }

    public void fetchTargetsNames(Bindeable presenter) {
        if (service != null) {
            service.fetchTargetNames(presenter);
        }
    }

    public boolean isDataEmpty(GiftEventSave gift) {
        boolean toReturn = true;
        if (gift.getDescription().isEmpty()){
            toReturn = false;
        }
        if (gift.getTarget().isEmpty()){
            toReturn = false;
        }
        return toReturn;
    }


}
