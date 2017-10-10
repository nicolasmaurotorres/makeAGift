package inc.maro.makeagift2.MVP.Models;

import android.util.Log;

import inc.maro.makeagift2.Containers.Gift;
import inc.maro.makeagift2.MVP.Presenters.EditGiftPresenter;
import inc.maro.makeagift2.OttoBus.Events.SaveEditedGift;
import inc.maro.makeagift2.Services.ICallBackBinder;

public class EditGiftModel extends AbstractGiftModel {

    private ICallBackBinder service;
    private Gift gift = null;

    public EditGiftModel(ICallBackBinder service) {
        this.service = service;
    }

    public boolean somethingHasChanged(SaveEditedGift newDataGift) {
        boolean somethingHasChanged = false;
        if (!newDataGift.getDescription().equals(gift.getDescription())) {
            somethingHasChanged = true;
        }
        if (!somethingHasChanged && !newDataGift.getTarget().equals(gift.getTarget())) {
            somethingHasChanged = true;
        }
        if (!somethingHasChanged && !newDataGift.getDate().equals(gift.getDate())) {
            somethingHasChanged = true;
        }
        return somethingHasChanged;
    }

    public void saveGiftUpdated() {
        if (service != null){
            service.updateGift(gift);
        }
    }

    public void deleteGift() {
        if (service != null){
            if (gift != null) {
                service.deleteGift(gift);
            } else {
                Log.e("ERROR AL BORRAR", "REGALO NULO, checkear todos los pasos de eliminacion");
            }
        }
    }

    public void setGiftById(Integer currentGift, EditGiftPresenter presenter) {
        if (service != null){
            service.getGiftById(currentGift,presenter);
        }

    }

    public void setGift(Gift gift) {
        this.gift = gift;
    }

    public Gift getGift() {
        return gift;
    }

    public void updateData(SaveEditedGift event) {
        gift.setDescription(event.getDescription());
        gift.setTarget(event.getTarget());
        gift.setDate(event.getDate());
    }
}
