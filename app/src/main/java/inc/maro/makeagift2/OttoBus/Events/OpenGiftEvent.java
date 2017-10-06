package inc.maro.makeagift2.OttoBus.Events;

import inc.maro.makeagift2.Containers.Gift;

public class OpenGiftEvent {

    private Gift gift;

    public  OpenGiftEvent(Gift gift) {
        this.gift = gift;
    }

    public Gift getGift() {
        return gift;
    }
}
