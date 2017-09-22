package network;

import matrix.IOVector;

/**
 * Created by Jonas on 13.06.2017.
 */
public class Neuron {
    private boolean bounded;
    private int wMin, wMax;

    protected double output;
    protected IOVector input;

    protected double weights[];

    protected double bias;


    protected char type;
    /*public Neuron(int nInput, int wMin, int wMax){
        bounded = true;
        this.wMin = wMin;
        this.wMax = wMax;
        init(nInput);
    }*/
    public class Info{
        public final double bias;
        public final double weights[];
        public final char type;
        private Info(Neuron n){
            bias = n.bias;
            weights = n.weights.clone();
            type = n.type;
        }
    }


    public Neuron(Neuron n){
        type = n.type;

        weights = n.weights.clone();

        bias = n.bias;
    }

    public Neuron(int nInput){
        type = 'N';
        bounded = false;
        init(nInput);
    }

    private void init(int nInput){
        weights = new double[nInput];

        for (int i = 0; i < nInput; i++)
            weights[i] = bounded ? Math.random() * (wMax - wMin) + wMin : Math.random() - 0.5;

        bias = Math.random() - 0.5;
    }

    public double forward(IOVector input){
        if (input.length() != weights.length)
            throw new IllegalArgumentException("");

        this.input = input;

        output = 0;

        for (int i = 0; i < weights.length; i++){
            output += weights[i] * input.get(i);
        }

        output += bias;

        return output;
    }

    public double[] backward(double pull, double der, double stepLength){
        double derNext[] = new double[weights.length];

        for (int i = 0; i < derNext.length; i++){
            derNext[i] = der * weights[i];
        }

        for (int i = 0; i < weights.length; i++){
            double derW = der * input.get(i) * pull;// - weights[i] * stepLength;
            weights[i] += derW * stepLength;
        }

        bias += der * stepLength * pull;

        return derNext;
    }

    public Info getInfo(){
        return new Info(this);
    }
}
