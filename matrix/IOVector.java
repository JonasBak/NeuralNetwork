package matrix;

import java.util.Arrays;

/**
 * Created by Jonas on 12.06.2017.
 */
public class IOVector {
    private double data[];

    public IOVector(double ... data){
        this.data = data;
    }

    public double get(int i) {
        return data[i];
    }

    public int length(){
        return data.length;
    }

    @Override
    public String toString() {
        return "IOVector{" +
                "data=" + Arrays.toString(data) +
                '}';
    }

    public static float error(IOVector a, IOVector b){
        if (a.length() != b.length())
            throw new IllegalArgumentException("jdfjl");
        float ret = 0;
        for (int i = 0; i < a.length(); i++)
            ret += Math.abs(a.data[i] - b.data[i]);

        return ret;
    }
}
