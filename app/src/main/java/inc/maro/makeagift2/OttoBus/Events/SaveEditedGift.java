package inc.maro.makeagift2.OttoBus.Events;

public class SaveEditedGift extends GiftEventSave{

    public SaveEditedGift(String description, String target, String date) {
        setDescription(description);
        setTarget(target);
        setDate(date);
    }
}