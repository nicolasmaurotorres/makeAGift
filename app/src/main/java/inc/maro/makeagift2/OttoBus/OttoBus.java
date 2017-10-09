package inc.maro.makeagift2.OttoBus;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

/**
 * Created by hIT on 4/10/2017.
 */

public class OttoBus {
    private static final Bus ourInstance = new Bus(ThreadEnforcer.ANY);

    public static Bus getInstance() {
        return ourInstance;
    }

}
