package inc.maro.makeagift2.Services;

import java.util.ArrayList;

import inc.maro.makeagift2.Containers.Target;

/**
 * Created by hIT on 10/10/2017.
 */

public interface Bindeable {
    void register();
    void unregister();
    void bindService(ICallBackBinder service);

    void fetchTargetsNames();
    void _fillTargetNames(ArrayList<Target> targets);
}
