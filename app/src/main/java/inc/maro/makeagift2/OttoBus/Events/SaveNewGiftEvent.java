package inc.maro.makeagift2.OttoBus.Events;

/**
 * Created by hIT on 4/10/2017.
 */

public class SaveNewGiftEvent {

    private String description = null;
    private String target = null;
    private String date = null;

    public String getDescription() {
        return description;
    }

    public String getTarget() {
        return target;
    }

    public String getDate() {
        return date;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
