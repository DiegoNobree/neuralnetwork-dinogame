
package neuralnetworking;

import java.util.ArrayList;
import java.util.List;

public class Neuron {
	
	public float value;
	public List<Float> weights;
	
	public Neuron(int weightCount) {
		value = 0;
		weights = new ArrayList<Float>();
		for (int i = 0; i < weightCount; i++) {
			weights.add((float) (Math.random() * 2 - 1));
		}
	}
}
