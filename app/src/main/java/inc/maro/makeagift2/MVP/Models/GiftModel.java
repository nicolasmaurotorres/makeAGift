package inc.maro.makeagift2.MVP.Models;

import android.util.Log;

import inc.maro.makeagift2.Containers.Gift;
import inc.maro.makeagift2.MVP.Presenters.GiftPresenter;
import inc.maro.makeagift2.OttoBus.Events.SaveEditedGift;
import inc.maro.makeagift2.OttoBus.Events.SaveNewGiftEvent;
import inc.maro.makeagift2.Services.ICallBackBinder;

/**
 * Created by hIT on 4/10/2017.
 */

public class GiftModel {

    private ICallBackBinder service;
    private Gift gift = null;

    public GiftModel(ICallBackBinder service) {
        this.service = service;
    }

    public void bindService(ICallBackBinder service) {
        this.service = service;
    }

    public void fetchTargetsNames(GiftPresenter presenter) {
        service.fetchTargetNames(presenter);
    }

    public void addNewGift(SaveNewGiftEvent saveNewGiftEvent, GiftPresenter giftPresenter) {
        //todo checkear la localizacion
        service.createNewGift(saveNewGiftEvent.getTarget(),saveNewGiftEvent.getDescription(),saveNewGiftEvent.getDate(),"",giftPresenter);
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

    public boolean isDataEmpty(SaveEditedGift gift) {
        boolean toReturn = true;
        if (gift.getDescription().isEmpty()){
            toReturn = false;
        }
        if (gift.getTarget().isEmpty()){
            toReturn = false;
        }
        return toReturn;
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

    public void setGiftById(Integer currentGift, GiftPresenter presenter) {
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
