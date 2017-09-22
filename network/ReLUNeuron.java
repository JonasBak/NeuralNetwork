package network;

import matrix.IOVector;

/**
 * Created by Jonas on 14.06.2017.
 */
public class ReLUNeuron extends Neuron {
    public ReLUNeuron(int nInput){
        super(nInput);

        type = 'R';
    }

    @Override
    public double forward(IOVector input){
        super.forward(input);

        output = output < 0 ? 0 : output;

        return output;
    }

    @Override
    public double[] backward(double pull, double der, double stepLength){
        double derNext[] = new double[weights.length];

        for (int i = 0; i < derNext.length; i++){
            derNext[i] = output == 0 ? 0 : der * weights[i];
        }

        for (int i = 0; i < weights.length; i++){
            double derW = der * input.get(i) * pull;// - weights[i] * stepLength;
            weights[i] += output == 0 ? 0 : derW * stepLength;
        }

        bias += output == 0 ? 0 : der * stepLength * pull;

        return derNext;
    }
}
