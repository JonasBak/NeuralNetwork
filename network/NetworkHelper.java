package network;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;
import matrix.IOVector;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jonas on 21.06.2017.
 */
public class NetworkHelper {



    public static double test(Network n, ArrayList<IOVector> in, List<IOVector> ex){
        if (in.size() != ex.size())
            throw new IllegalArgumentException();

        int nWrong = 0;

        for (int i = 0; i < in.size(); i++){
            IOVector out = n.giveInput(in.get(i));
            for (int e = 0; e < out.length(); e++){
                if (out.get(e) * ex.get(i).get(e) < 0){
                    nWrong++;
                    break;
                }
            }
        }

        return (double) (in.size() - nWrong) / in.size();
    }

    public static void draw(Neuron.Info info[][], Canvas canvas){

        GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        int r = 10;



        gc.setLineWidth(1);
        gc.setStroke(Paint.valueOf("#000000"));

        for (int i = 0; i < info[0][0].weights.length; i++){
            int x = (int)(canvas.getWidth() * (0.5d) / (info.length + 1));
            int y = (int)(canvas.getHeight() * (i + 0.5d)) / info[0][0].weights.length;
            gc.beginPath();
            gc.arc(x, y, r/2, r/2, 0, 360);
            gc.stroke();
            gc.closePath();
        }

        for (int d = 0; d < info.length; d++){
            int x = (int)(canvas.getWidth() * (d + 0.5d + 1)) / (info.length + 1);
            for (int i = 0; i < info[d].length; i++){
                int y = (int)(canvas.getHeight() * (i + 0.5d)) / info[d].length;

                gc.setLineWidth(1);
                gc.setStroke(Paint.valueOf("#000000"));

                gc.fillText(String.valueOf((double)((int)(info[d][i].bias*1000)) / 1000), x, y - r);

                gc.beginPath();
                gc.arc(x, y, r, r, 0, 360);
                gc.stroke();

                if (info[d][i].type == 'R') {
                    gc.setFill(Paint.valueOf("#CCCCCC"));
                    gc.fill();
                }
                else if (info[d][i].type == 'S') {
                    gc.setFill(Paint.valueOf("#00CC00"));
                    gc.fill();
                }



                gc.setFill(Paint.valueOf("#000000"));

                gc.closePath();
                for (int w = 0; w < info[d][i].weights.length; w++){
                    int x2 = (int)(canvas.getWidth() * (d + 0.5d) / (info.length + 1));
                    int y2 = (int)(canvas.getHeight() * (w + 0.5d)) / info[d][i].weights.length;

                    gc.setLineWidth(Math.abs(info[d][i].weights[w]) * 2);

                    gc.setStroke(Paint.valueOf(info[d][i].weights[w] < 0 ? "#FF0000" : "#0000FF"));

                    gc.beginPath();

                    gc.moveTo(x, y);
                    gc.lineTo(x2, y2);

                    gc.stroke();
                    gc.closePath();


                }
            }
        }
    }

    public static void mutate(Network n, double mutRange){
        int depth = (int)(Math.random() * n.network.length);
        Neuron neuron = n.network[depth][(int)(Math.random() * n.network[depth].length)];

        if (Math.random() < 0.8)
            neuron.weights[(int)(Math.random() * neuron.weights.length)] += - mutRange / 2 + Math.random() * mutRange;
        else
            neuron.bias += - mutRange / 2 + Math.random() * mutRange;
    }
}
