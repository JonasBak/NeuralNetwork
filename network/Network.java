package network;

import matrix.IOVector;
import matrix.NMatrix;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Created by Jonas on 12.06.2017.
 */
public class Network {
    Neuron[][] network;

    private final Config config;
    private final int nInput;

    public static class Config{
        int nNeuron[] = new int[0];
        char types[] = new char[0];

        public void setnNeuron(int ... nNeuron){
            this.nNeuron = nNeuron;
        }

        public void setTypes(char ... types){
            this.types = types;
        }
    }

    public Network(Network n){
        network = new Neuron[n.config.nNeuron.length][];

        config = n.config;
        nInput = n.nInput;

        for (int d = 0; d < network.length; d++){
            network[d] = new Neuron[config.nNeuron[d]];
            for (int i = 0; i < config.nNeuron[d]; i++) {
                network[d][i] = new Neuron(n.network[d][i]);
            }
        }
    }

    public Network(int nInput, Config config){
        network = new Neuron[config.nNeuron.length][];

        this.config = config;
        this.nInput = nInput;

        for (int d = 0; d < network.length; d++){
            network[d] = new Neuron[config.nNeuron[d]];
            for (int i = 0; i < config.nNeuron[d]; i++) {
                int in = d == 0 ? nInput : config.nNeuron[d - 1];
                Neuron neuron;
                if (d > config.types.length - 1)
                    neuron = new Neuron(in);
                else{
                    switch (config.types[d]){
                        case 'R':
                            neuron = new ReLUNeuron(in);
                            break;
                        case 'S':
                            neuron = new SigmoidNeuron(in);
                            break;
                        default:
                            neuron = new Neuron(in);
                    }
                }

                network[d][i] = neuron;
            }

        }
    }


    public IOVector giveInput(IOVector in){
        return giveInput(in, 0);
    }

    private IOVector giveInput(IOVector in, int depth){
        if (depth < network.length) {
            double output[] = new double[network[depth].length];
            for (int i = 0; i < output.length; i++)
                output[i] = network[depth][i].forward(in);
            return giveInput(new IOVector(output), depth + 1);
        }
        else
            return in;
    }

    public void train(IOVector in, IOVector expect){

        double e0 = getError(in, expect);

        IOVector out = giveInput(in);


        for (int p = 0; p < out.length(); p++){

            double step = 0.01;

            double pull = 0.0;
            if (expect.get(p) == 1 && out.get(p) < 1)
                pull = 1;
            if(expect.get(p) == -1 && out.get(p) > -1)
                pull = -1;

            double der[] = network[network.length-1][p].backward(pull, 1, step);
            for (int d = network.length-2; d >= 0 ; d--){

                double nextDer[] = null;
                if (d > 0)
                    nextDer = new double[network[d - 1].length];
                for (int i = 0; i < der.length; i++){
                    double tmp []= network[d][i].backward(pull, der[i], step);
                    if (nextDer != null)
                        for (int t = 0; t < tmp.length; t++)
                            nextDer[t] += tmp[t];
                }

                if (nextDer != null)
                    der = nextDer.clone();

            }

        }
    }

    private double getError(IOVector in, IOVector expect){
        return IOVector.error(expect, giveInput(in));
    }

    public void train(ArrayList<IOVector> in, List<IOVector> ex){
        if (in.size() != ex.size())
            throw new IllegalArgumentException();

        for (int i = 0; i < in.size(); i++){
            train(in.get(i), ex.get(i));
        }
    }



    public static void main(String[] args){
        Config config = new Config();

        config.setnNeuron(2, 1);
        config.setTypes('R', 'N');

        Network n = new Network(2, config);

        Collection<IOVector> in = new ArrayList<>();
        in.add(new IOVector(1.2, 0.7));
        in.add(new IOVector(-0.3, -0.5));
        in.add(new IOVector(3, 0.1));
        in.add(new IOVector(-0.1, -1.0));
        in.add(new IOVector(-1, 1.1));
        in.add(new IOVector(2.1, -3));

        int ex[] = new int[]{1,-1,1,-1,-1,1};

        for (int i = 0; i < 500; i++){
            int o = 0;
            for (IOVector v : in){
                n.train(v, new IOVector(ex[o]));

                o++;
            }
        }
    }

    public Neuron.Info[][] getInfo(){
        Neuron.Info info[][] = new Neuron.Info[network.length][];

        for (int d = 0; d < network.length; d++){
            info[d] = new Neuron.Info[network[d].length];
            for (int i = 0; i < network[d].length; i++){
                info[d][i] = network[d][i].getInfo();
            }
        }

        return info;
    }

}
