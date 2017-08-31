package inc.maro.makeagift2.Containers;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * Created by hIT on 6/8/2017.
 */

public class Gift implements Parcelable {
    public static final String NEW_GIFT = "newGift";
    public static final String SAVED_GIFT = "savedGift";
    public static final String EDITED_GIFT = "editedGift";
    public static final String DELETED_GIFT = "deletedGift";
    public static final float DEFAULT_PERCENTAGE_X = 0.20f;
    public static final float DEFAULT_POSITION_Y = 0.30f;


    private String whereToBuy = "";
    private String whenToGift = ""; // todo parsear el string como Date al momento de usarlo
    private String description ="";
    private String target = "";
    private int idTarget = -1;
    private long id = -1;
    private float x = DEFAULT_PERCENTAGE_X;
    private float y = DEFAULT_POSITION_Y;

    public Gift(){}

    public Gift(long _id, String _target, String _description, String _whereToBuy, String _whenToGift)
    {
        id = _id;
        target = _target;
        description = _description;
        whereToBuy = _whereToBuy;
        whenToGift = _whenToGift;
    }

    public String getTarget()
    {
        return target;
    }

    public void setTarget(String target)
    {
        this.target = target;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String desc) {
        this.description = desc;
    }

    public String getWhereToBuy() {
        return whereToBuy;
    }

    public void setWhereToBuy(String whereToBuy) {
        this.whereToBuy = whereToBuy;
    }

    public String getWhenGift() {
        return whenToGift;
    }

    public void setWhenToGift(String whenToGift) {
        this.whenToGift = whenToGift;
    }

    public int getIdTarget() {
        return idTarget;
    }

    public void setIdTarget(int idTarget) {
        this.idTarget = idTarget;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    //Parcelable things
    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i){
        parcel.writeStringArray(new String[] {String.valueOf(getTarget()),
                                                             getDescription(),
                                                             getWhereToBuy(),
                                                             getWhenGift(),
                                              String.valueOf(getId()),
                                              String.valueOf(getIdTarget()),
                                              Float.toString(getX()),
                                              Float.toString(getY())});
    }

    private Gift(Parcel input){
        String[] data = new String[8];
        input.readStringArray(data);        // the order needs to be the same as in writeToParcel() method
        setTarget(data[0]);
        setDescription(data[1]);
        setWhereToBuy(data[2]);
        setWhenToGift(data[3]);
        setId(Integer.valueOf(data[4]));
        setIdTarget(Integer.valueOf(data[5]));
        setX(Float.valueOf(data[6]));
        setY(Float.valueOf(data[7]));
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator(){
        public Gift createFromParcel(Parcel in)
        {
            return new Gift(in);
        }

        public Gift[] newArray(int size)
        {
            return new Gift[size];
        }
    };

    // al momento de buscar un gift se edito uso el equals
    @Override
    public boolean equals(Object o){
        if (o instanceof Gift) {
            Gift aux = (Gift) o;
            return (aux.getId() == this.getId());
        }
        return false;
    }

    public void updateData(Gift editedGift){
        setTarget(editedGift.getTarget());
        setWhenToGift(editedGift.getWhenGift());
        setDescription(editedGift.getDescription());
        setWhereToBuy(editedGift.getWhereToBuy());
        setIdTarget(editedGift.getIdTarget());
        setX(editedGift.getX());
        setY(editedGift.getY());
    }
}
