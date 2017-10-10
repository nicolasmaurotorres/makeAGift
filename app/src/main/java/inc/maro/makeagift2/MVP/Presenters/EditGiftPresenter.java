package inc.maro.makeagift2.MVP.Presenters;

import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import inc.maro.makeagift2.Containers.Gift;
import inc.maro.makeagift2.Containers.Target;
import inc.maro.makeagift2.MVP.Models.EditGiftModel;
import inc.maro.makeagift2.MVP.Views.EditGiftView;
import inc.maro.makeagift2.OttoBus.Events.DeleteGiftEvent;
import inc.maro.makeagift2.OttoBus.Events.SaveEditedGift;
import inc.maro.makeagift2.OttoBus.OttoBus;
import inc.maro.makeagift2.Services.Bindeable;
import inc.maro.makeagift2.Services.ICallBackBinder;

public class EditGiftPresenter implements Bindeable {

    private EditGiftView view;
    private EditGiftModel model;

    public EditGiftPresenter(EditGiftModel editGiftModel, EditGiftView editGiftView) {
        view = editGiftView;
        model = editGiftModel;
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
    @Override
    public void _fillTargetNames(ArrayList<Target> targets) {
        view.setTargets(targets);
    }

    public void _setGift(Gift toReturn) {
        model.setGift(toReturn);
        view.setGift(model.getGift());
    }
}
