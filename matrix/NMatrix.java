package matrix;

/**
 * Created by Jonas on 11.06.2017.
 */
public class NMatrix {
    private final int nInput, nOutput;

    private double sigmoid(double a){
        if (!sig)
            return 2.0f/(1 + Math.exp(-a)) - 1;
        return 1.0f/(1 + Math.exp(-a));
    }

    public boolean sig = false;

    public int getnInput() {
        return nInput;
    }

    public int getnOutput() {
        return nOutput;
    }

    private double weights[];

    public NMatrix(int nInput, int nOutput){
        this.nInput = nInput;
        this.nOutput = nOutput;

        weights = new double[nInput * nOutput];

    }

    public void initRandom(){
        for(int i = 0; i < nInput * nOutput; i++){
            weights[i] = Math.random() * 2 - 1;

        }
    }

    public IOVector giveInput(IOVector v){
        if (v.length() != nInput)
            throw new IllegalArgumentException("lkajfljf");
        double data[] = new double[nOutput];
        for (int o = 0; o < nOutput; o++){
            for (int i = 0; i < nInput; i++){
                data[o] += weights[o * nInput + i] * v.get(i);
            }
            data[o] = sigmoid(data[o]);
        }

        return new IOVector(data);
    }

    public void changeWeight(int in, int out, double dW){
        weights[in + out * nInput] += dW;
        if (weights[in + out * nInput] < -1)
            weights[in + out * nInput] = -1;
        if (weights[in + out * nInput] > 1)
            weights[in + out * nInput] = 1;
    }

    public void setWeight(int in, int out, double w){
        weights[in + out * nInput] = w;
    }
    public double getWeight(int in, int out){
        return weights[in + out * nInput];
    }

}
