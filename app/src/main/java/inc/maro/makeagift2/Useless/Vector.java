package inc.maro.makeagift2.Useless;

/**
 * Created by hIT on 24/8/2017.
 */

public class Vector
{
    private Float[] values = null;

    public Vector(Float... vals)
    {
        if (vals != null && vals.length > 1 )
        {
            int count = 0;
            values = new Float[vals.length];
            for (Float i : vals)
            {
                values[count] = i;
                count++;
            }
        }
    }

    public float scalarProduct(Vector other)
    {
        float data = 0;
        if (other.dimentions() == this.dimentions() && this.dimentions() != -1)
        {
            for (int i = 0; i < this.dimentions();i++)
            {
                data += (getIndex(i) * other.getIndex(i));
            }
        }
        return data;
    }

    private int dimentions()
    {
        return (values != null)? values.length : -1;
    }

    private float getIndex(int index)
    {
       return (values != null && index < values.length) ? values[index] : -1;
    }

    public void translate(Float... trans)
    {
        if (trans != null && trans.length > 1 && trans.length == this.dimentions())
        {
            for (int i = 0; i < this.dimentions(); i++)
            {
                values[i] += trans[i];
            }
        }
    }



}
