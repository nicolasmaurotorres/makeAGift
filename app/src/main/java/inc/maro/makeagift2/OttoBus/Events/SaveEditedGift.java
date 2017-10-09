package inc.maro.makeagift2.OttoBus.Events;

import inc.maro.makeagift2.Containers.Gift;

/**
 * Created by hIT on 6/10/2017.
 */

public class SaveEditedGift {

    private String description ="";
    private String target ="";
    private String date =""; //todo modificar a date

    public SaveEditedGift(String description, String target, String date) {
        this.description = description;
        this.target = target;
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
