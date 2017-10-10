package inc.maro.makeagift2.OttoBus.Events;



public abstract class GiftEventSave {

    private String description ="";
    private String target ="";
    private String date =""; //todo modificar a date

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
