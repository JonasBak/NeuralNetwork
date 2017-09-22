package network;

import matrix.IOVector;

/**
 * Created by Jonas on 14.06.2017.
 */
public class SigmoidNeuron extends Neuron{
    private static double sigmoid(double a){
        return 1 / (1 + Math.exp(-a));
    }

    private static double sigmoidDer(double a){
        return sigmoid(a) * (1 - sigmoid(a));
    }

    public SigmoidNeuron(int nInput){
        super(nInput);

        type = 'S';
    }

    @Override
    public double forward(IOVector input){
        super.forward(input);

        output = sigmoid(output);

        return output;
    }

    @Override
    public double[] backward(double pull, double der, double stepLength){
        double derNext[] = new double[weights.length];

        for (int i = 0; i < derNext.length; i++){
            derNext[i] = sigmoidDer(input.get(i)) * der * weights[i];//output == 0 ? 0 : der * weights[i];
        }

        for (int i = 0; i < weights.length; i++){
            double derW = sigmoidDer(input.get(i)) * der * pull;// - weights[i] * stepLength;
            weights[i] += derW * stepLength;//output == 0 ? 0 : derW * stepLength;
        }

        bias += der * stepLength * pull;

        return derNext;
    }
}
