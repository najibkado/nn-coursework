package neurons;

import java.util.Arrays;

public class Network
{

    /**
     *
     */
    
    private final double [] [] outputs; //Stores the outputs //First for the layer at the time and //Second for the current neuron  
    private final double [] [] [] weights; //This stores the weights //First stores the layer, // Second for the current neuron  and //Third for the neuron in the previous layer connected with the cueent neuron 
    private final double [] [] bias; //This stores the bias //Such that the first stores the layers and //Second stores bias
    
    private final double [] [] errors; //This stores the errors at previous layer and current layer
    private final double [] [] outputDerivative; //This stores the output derivative for backpropagation
    
    
    public final int[] LAYER_SIZES; // This stores the numbers of layers in terms of neurons per layer
    public final int INPUT_SIZE;  // This stores the input szie
    public final int OUTPUT_SIZE;  // This stores the output size
    public final int NETWORK_SIZE; // This stores the sizes of layers holding neurons
    
    
    
    //This initializes the sizes 
    public Network(final int... LAYER_SIZES)
    {
        this.LAYER_SIZES = LAYER_SIZES; //This initializes the layer size
        this.INPUT_SIZE = LAYER_SIZES [0]; //This initializes first indext of the input as input layer
        this.NETWORK_SIZE = LAYER_SIZES.length; //This stores the size of the layer
        this.OUTPUT_SIZE = LAYER_SIZES [NETWORK_SIZE-1]; //This initializes the last index of the input as output 
        
        this.outputs = new double[NETWORK_SIZE] [] ; //This initializes the first dimension of outputs with layer size
        this.weights = new double[NETWORK_SIZE]  [] [] ; //This initializes the first dimension of weights with layer size
        this.bias = new double[NETWORK_SIZE] []; //This initializes the first dimension of bias with layer size
        
        this.errors = new double[NETWORK_SIZE] [] ;
        this.outputDerivative = new double[NETWORK_SIZE] [] ;
        
        for (int i = 0; i < NETWORK_SIZE; i++)
        {
            //this initializez the second dimension of the output and bias 
            this.outputs[i] = new double [LAYER_SIZES[i]]; //This initializes the second dimension of outputs with layer size
            this.errors[i] = new double [LAYER_SIZES[i]]; 
            this.outputDerivative[i] = new double [LAYER_SIZES[i]];
           // this.bias[i] = new double [LAYER_SIZES[i]];
            
            this.bias[i] = Tools.createRandomArray(LAYER_SIZES[i], 0.5, 0.7);   //This initializes the second dimension of outputs with layer size a
            
            //This creates a weight array for every layer except the input layer
            if (i>0)
            {   
                weights[i] = Tools.createRandomArray(LAYER_SIZES[i], LAYER_SIZES[i-1], -0.3, 0.3);
            }
        }
    }
    
    //This method calculate the feedforward process and return an output of double array //Thus it accepts an input array of double type 
    public double [] calc (final double... input) {
        
        if(input.length != this.INPUT_SIZE) //This check the size of the input data, if it corresponds with the size of the layer
            return null; //If it doesnt corresspond, it returns nothing
  
        this.outputs[0] = input; // This assign the input data into the output array 
          
        for(int layer = 1; layer < NETWORK_SIZE; layer++) //Checks the layer size and increment once at a time
        {
            //Iterating through each neuron
            for(int neuron = 0; neuron < LAYER_SIZES[layer]; neuron++) // Checks the neuron at current layer size and increment once at a time
            {
                double sum = 0;
                sum += bias[layer][neuron]; //Adding the bias of the neuron 
                for(int previousNeuron = 0; previousNeuron < LAYER_SIZES[layer-1]; previousNeuron ++) //Checks the previous neurons working with current neuron and increment it once at a time
                {
                    //Increase the sum by output of the previous neuron at the layer
                    sum += outputs[layer-1][previousNeuron] * weights[layer][neuron][previousNeuron]; //Add the bias to X1 + Xn and multiply by weights at the current neuron that connects the previous neuron
                }
                
                //This applies the sigmoid which is the activation function and sttore it to the output of the feedforward. 
                outputs[layer][neuron] = activationFunction(sum);
                outputDerivative[layer][neuron] = outputs[layer][neuron] * (1 - outputs[layer][neuron]);
                
            }
        }
        
        //This return the output at the last layer of the outputs array
        return Arrays.copyOf(outputs[NETWORK_SIZE - 1],OUTPUT_SIZE);
       
    }
    
    
    // sigmoid function
    public double activationFunction( final double i)
    {
        return 1d/(1 + Math.exp(-i));
    }

    // Accepts data set , number of cycle and learning rate as an input
    public void train(DataSet set,  int epochs, double learningRate)
    {
        //This iterates the training procces through the number of cycles
        for (int i = 0; i < epochs; i++)
        {
            double loss = 0; //Stores the initial value of loss per cycle
            //This iterates throught the complete data set
            for (int j = 0; j < set.size(); j++)
            {
                //Calculate the loss in each cycle
                loss+=this.train(set.getInput(j), learningRate, set.getOutput(j));
            }
            //Prints the loss size at each cycle
            System.out.println("Cycle " + i + "   loss " + loss/set.size());
        }
    }
    
    //This method trains the Neural Network
    public double train(final double [] input, final double learningRate, final double [] targetOutput)
    {
       if(input.length != INPUT_SIZE || targetOutput.length != OUTPUT_SIZE) //This checks the size of the input
           return 1;
       calc(input); //This calculate the output of each neuron taking input as parameter 
       double loss = backpropagate(targetOutput); //Stores backpropagated target output for loss calculation
       //System.out.println(loss);
       changeWeights(learningRate); //changes the weights
       return loss; //return the loss
    }
    
    public double backpropagate(final double [] targetOutput)
    {
        double loss = 0;
        for (int neuron = 0; neuron < LAYER_SIZES[NETWORK_SIZE-1]; neuron+=1)
        {
            errors[NETWORK_SIZE-1][neuron] = (outputs[NETWORK_SIZE-1][neuron] - targetOutput[neuron]) * outputDerivative[NETWORK_SIZE-1][neuron];
            //System.out.println(outputs[NETWORK_SIZE-1][neuron] - targetOutput[neuron]);
            loss += (outputs[NETWORK_SIZE-1][neuron] - targetOutput[neuron]) * (outputs[NETWORK_SIZE-1][neuron] - targetOutput[neuron]);
        }
        for (int layer = NETWORK_SIZE-2; layer > 0; layer --)
        {
            //This loops through each neuron in each layer from the -output layer to the -input layer
            for (int neuron = 0; neuron < LAYER_SIZES[layer]; neuron++)
            {
                double total = 0;
                //This goes through each neuron in each layer where error is beign calculated
                for (int neuronNext = 0; neuronNext < LAYER_SIZES[layer+1]; neuronNext ++)
                {
                    total += weights[layer +1][neuronNext][neuron] * errors[layer+1][neuronNext];
                }
                this.errors[layer][neuron] = total * outputDerivative[layer][neuron]; 
            }
        }
        return loss / (2*targetOutput.length);
    }
    
    //This method updates the weights
    public void changeWeights(final double learningRate)
    {
        for(int layer = 1; layer < NETWORK_SIZE; layer++)
        {
            for (int neuron = 0; neuron < LAYER_SIZES[layer]; neuron++)
            {
                for(int previousNeuron = 0; previousNeuron < LAYER_SIZES[layer-1]; previousNeuron++)
                {
                    //for each weights in current layer, connected with the neurons and the previous neurons
                    final double d = -learningRate * outputs[layer-1][previousNeuron] * errors[layer][neuron];
                    weights[layer][neuron][previousNeuron]+= d;
                }
                //This increases the size of the bias with the delta value
                final double d = -learningRate * errors[layer][neuron];
                bias[layer][neuron] += d;
            }
        }
    }

    
    public static void main(final String[] args) 
    {
        DataSet trainSet = DataSet.readFromCSV("res/cw2DataSet1.csv",10,16);
        DataSet testSet = DataSet.readFromCSV("res/cw2DataSet2.csv",10,16);


        final Network network = new Network(trainSet.INPUT_SIZE, 40,30, trainSet.OUTPUT_SIZE);


        //----training---------------------------------------------------
        network.train(trainSet, 50, 0.1);
    

        //----testing----------------------------------------------------
        System.out.println("-----------------------Training Data-------------------------");
        System.out.println("accuracy on training data: " + Tools.getAccuracy(network, trainSet));
        System.out.println("-------------------------Test Data---------------------------");
        System.out.println("accuracy on test data: " + Tools.getAccuracy(network, testSet));

    }
    
    
}


