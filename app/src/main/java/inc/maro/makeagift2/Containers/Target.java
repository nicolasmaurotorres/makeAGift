package inc.maro.makeagift2.Containers;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by hIT on 6/8/2017.
 */

public class Target{
    private int id;
    private String name;
    private List<Gift> gifts = new ArrayList<Gift>();

    public Target(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
