package inc.maro.makeagift2.MVP.Presenters;

import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import inc.maro.makeagift2.Containers.Target;
import inc.maro.makeagift2.MVP.Models.NewGiftModel;
import inc.maro.makeagift2.MVP.Views.NewGiftView;
import inc.maro.makeagift2.OttoBus.Events.SaveNewGiftEvent;
import inc.maro.makeagift2.OttoBus.OttoBus;
import inc.maro.makeagift2.Services.Bindeable;
import inc.maro.makeagift2.Services.ICallBackBinder;


public class NewGiftPresenter implements Bindeable {

    private NewGiftView view;
    private NewGiftModel model;

    public NewGiftPresenter(NewGiftModel model, NewGiftView view){
        this.model = model;
        this.view = view;
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

    @Override
    public void register() {
        OttoBus.getInstance().register(this);
    }

    @Override
    public void unregister() {
        OttoBus.getInstance().unregister(this);
    }

    @Override
    public void bindService(ICallBackBinder service) {
        model.bindService(service);
    }

    public void fetchTargetsNames() {
        model.fetchTargetsNames(this);
    }

    @Override
    public void _fillTargetNames(ArrayList<Target> targets) {
        view.setTargets(targets);
    }

    public void _createNewGift() {
        view.goToLobby();
    }
}
