package neurons;

public class Tools {

    //This method creates array and return it with it's initial value
    public static double[] createArray(int size, double init_value){
        if(size < 1){
            return null;
        }
        double[] ar = new double[size];
        for(int i = 0; i < size; i++){
            ar[i] = init_value;
        }
        return ar;
    }

    //This method creates an array of 1 deminsion with random data ranging from the min to max value
    public static double[] createRandomArray(int size, double min_value, double max_value){
        if(size < 1){
            return null;
        }
        double[] ar = new double[size];
        for(int i = 0; i < size; i++){
            ar[i] = randomValue(min_value,max_value);
        }
        return ar;
    }

    //This method creates an array of 2 deminsion with random data ranging from the min to max value
    public static double[][] createRandomArray(int sizeX, int sizeY, double min_value, double max_value){
        if(sizeX < 1 || sizeY < 1){
            return null;
        }
        double[][] ar = new double[sizeX][sizeY];
        for(int i = 0; i < sizeX; i++){
            ar[i] = createRandomArray(sizeY, min_value, max_value);
        }
        return ar;
    }

    //This method is beign used by the array methods to generate random values and returns the values
    public static double randomValue(double min_value, double max_value){
        return Math.random()*(max_value-min_value) + min_value;
    }

    //This method returns an integer array, and all numbers are random and non repeated
    public static Integer[] randomValues(int minValue, int maxValue, int amount) {

        minValue --;

        if(amount > (maxValue-minValue)){
            return null;
        }

        Integer[] values = new Integer[amount];
        for(int i = 0; i< amount; i++){
            int n = (int)(Math.random() * (maxValue-minValue+1) + minValue);
            while(containsValue(values, n)){
                n = (int)(Math.random() * (maxValue-minValue+1) + minValue);
            }
            values[i] = n;
        }
        return values;
    }

    //This is a generic, it accepts all kind of array and check if it contains data
    public static <T extends Comparable<T>> boolean containsValue(T[] ar, T value){
        for(int i = 0; i < ar.length; i++){
            if(ar[i] != null){
                if(value.compareTo(ar[i]) == 0){
                    return true;
                }
            }

        }
        return false;
    }

    //This method checks the index of the highest value in a double array
    public static int indexOfHighestValue(double[] values){
        int index = 0;
        for(int i = 1; i < values.length; i++){
            if(values[i] > values[index]){
                index = i;
            }
        }
        return index;
    }

    /**
     * returns the accuracy of the network on the given dataset.
     * A classification is considered correct if and only if the output neuron with the highest activation has the same
     * index as the highest output value index in the expected output.
     * @param network
     * @param dataSet
     * @return
     */
    //This method calculate and get the accuracy of the network
    public static double getAccuracy(Network network, DataSet dataSet){
        double correct = 0;

        for(int i = 0; i < dataSet.size(); i++){
            double[] out = network.calc(dataSet.getInput(i));
            double[] exp = dataSet.getOutput(i);

            if(Tools.indexOfHighestValue(out) == Tools.indexOfHighestValue(exp)){
                correct++;
            }
            //System.out.println("testing:   index: " + i + "    accuracy: " + correct / (i+1));
        }
        return correct / dataSet.size();
    }

}
