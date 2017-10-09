package inc.maro.makeagift2.Containers;

import android.os.Parcel;
import android.os.Parcelable;

public class GiftDisplayed /*implements Parcelable*/ {
    // se usa para cuando se visualizan los gifs en lobbyActivity
    public static final String GIFT_ID = "GIFT_ID";

    private int id;
    private float x;
    private float y;

    public GiftDisplayed(int id, float x, float y) {
        this.id  = id;
        this.x = x;
        this.y = y;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }
/*
    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i){
        parcel.writeStringArray(new String[] {String.valueOf(getId()),
                String.valueOf(getX()),
                String.valueOf(getY())});
    }

    private GiftDisplayed(Parcel input){
        String[] data = new String[8];
        input.readStringArray(data);        // the order needs to be the same as in writeToParcel() method
        setId(Integer.valueOf(data[0]));
        setX(Float.valueOf(data[1]));
        setY(Float.valueOf(data[2]));
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator(){
        public GiftDisplayed createFromParcel(Parcel in)
        {
            return new GiftDisplayed(in);
        }

        public GiftDisplayed[] newArray(int size)
        {
            return new GiftDisplayed[size];
        }
    };*/

}
