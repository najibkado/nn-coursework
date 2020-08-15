package neurons;

import java.io.*;
import java.util.ArrayList;


public class DataSet{
    public final int INPUT_SIZE;
    public final int OUTPUT_SIZE;

    //double[][] <- index1: 0 = input, 1 = output || index2: index of element
    private ArrayList<double[][]> data = new ArrayList<>();

    public DataSet(int INPUT_SIZE, int OUTPUT_SIZE) {
        this.INPUT_SIZE = INPUT_SIZE;
        this.OUTPUT_SIZE = OUTPUT_SIZE;
    }

    public void addData(double[] in, double[] expected) {
        if(in.length != INPUT_SIZE || expected.length != OUTPUT_SIZE) return;
        data.add(new double[][]{in, expected});
    }

    public int size() {
        return data.size();
    }

    public double[] getInput(int index) {
        if(index >= 0 && index < size())
            return data.get(index)[0];
        else return null;
    }

    public double[] getOutput(int index) {
        if(index >= 0 && index < size())
            return data.get(index)[1];
        else return null;
    }

    public int getINPUT_SIZE() {
        return INPUT_SIZE;
    }

    public int getOUTPUT_SIZE() {
        return OUTPUT_SIZE;
    }


    /**
     * creates a dataset from the csv file.
     * all entries except the last are used as input data.
     * the last entry is used as output data.
     * the network will have 1 output neuron for each possible output value
     *
     *
     *
     * @param csv
     * @param outputSize        -different values for output value
     * @param maxValue          -maximum value in the input data
     * @return
     */
    //This method reads the data set and return it
    public static DataSet readFromCSV(String csv, int outputSize, double maxValue){
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(csv)));
            String line;
            DataSet dataSet = null;
            while((line = br.readLine()) != null){
                String[] data = line.trim().split(",");
                if(dataSet == null){
                    dataSet = new DataSet( data.length-1,outputSize);
                }
                double[] in = new double[data.length-1];
                double[] out = new double[outputSize];
                for(int i = 0; i < data.length-1; i++){
                    in[i] = Double.parseDouble(data[i]) / maxValue;
                }
                out[Integer.parseInt(data[data.length-1])] = 1;
                dataSet.addData(in,out);
            }
            return dataSet;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}