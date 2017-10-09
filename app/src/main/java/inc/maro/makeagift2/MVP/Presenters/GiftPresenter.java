package inc.maro.makeagift2.MVP.Presenters;

import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import inc.maro.makeagift2.Containers.Gift;
import inc.maro.makeagift2.Containers.Target;
import inc.maro.makeagift2.MVP.Models.GiftModel;
import inc.maro.makeagift2.MVP.Views.GiftView;
import inc.maro.makeagift2.OttoBus.Events.DeleteGiftEvent;
import inc.maro.makeagift2.OttoBus.Events.SaveEditedGift;
import inc.maro.makeagift2.OttoBus.Events.SaveNewGiftEvent;
import inc.maro.makeagift2.OttoBus.OttoBus;
import inc.maro.makeagift2.Services.ICallBackBinder;

public class GiftPresenter {

    private GiftView view;
    private GiftModel model;

    public GiftPresenter(GiftModel giftModel, GiftView giftView) {
        view = giftView;
        model = giftModel;
    }

    @Subscribe
    public void onSaveEditedGift(SaveEditedGift event){
        if (model.isDataEmpty(event)) {
            if (model.somethingHasChanged(event)) {
                model.updateData(event);
                model.saveGiftUpdated();
            } else {
                view.showToastMessage("No ha cambiado nada");
            }
            view.goToLobby();
        } else {
            view.showToastMessage("Los campos no pueden ser vacios");
        }
    }

    @Subscribe
    public void onSaveNewGift(SaveNewGiftEvent event){
        String errors = view.validateDataGiftAndReturnErrors();
        if (!errors.isEmpty()){
            view.showToastMessage(errors);
        } else {
            model.addNewGift(event,this);
        }
    }

    @Subscribe
    public void onDeleteGift(DeleteGiftEvent event){
        model.deleteGift();
    }

    public void register(){
        OttoBus.getInstance().register(this);
    }

    public void unregister() {
        OttoBus.getInstance().unregister(this);
    }

    public void bindService(ICallBackBinder service) {
        model.bindService(service);
    }

    public void fetchTargetsNames() {
        model.fetchTargetsNames(this);
    }

    public void setCurrentGiftById(Integer currentGift) {
        model.setGiftById(currentGift,this);
    }

    // ************* CALLBACKS ***************
    public void _fillTargetNames(ArrayList<Target> targets) {
        view.setTargets(targets);
    }

    public void _createNewGift() {
        view.goToLobby();
    }

    public void _setGift(Gift toReturn) {
        model.setGift(toReturn);
        view.setGift(model.getGift());
    }
}
