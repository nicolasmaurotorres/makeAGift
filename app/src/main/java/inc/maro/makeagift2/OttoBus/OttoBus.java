package inc.maro.makeagift2.OttoBus;

/**
 * Created by hIT on 4/10/2017.
 */

public class OttoBus {
    private static final OttoBus ourInstance = new OttoBus();

    public static OttoBus getInstance() {
        return ourInstance;
    }

    private OttoBus() {
    }
}
