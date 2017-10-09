package inc.maro.makeagift2.Services;


import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

/**
 * Created by hIT on 23/8/2017.
 */

public class DatabaseServiceConnector implements ServiceConnection
{
    private Serviceable localServiceActivity;

    public DatabaseServiceConnector(Serviceable asd)
    {
        this.localServiceActivity = asd;
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service){
        this.localServiceActivity.setCallBackBinder((ICallBackBinder) service);
    }

    @Override
    public void onServiceDisconnected(ComponentName name){
        this.localServiceActivity.setCallBackBinder(null);
    }
}

