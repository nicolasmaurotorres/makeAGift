package inc.maro.makeagift2.OttoBus.Events;

import inc.maro.makeagift2.Containers.Gift;

public class EditGiftEvent {

    private Gift gift = null;

    public EditGiftEvent(Gift gift) {
        this.gift = gift;
    }

    public Gift getGift() {
        return gift;
    }
}
