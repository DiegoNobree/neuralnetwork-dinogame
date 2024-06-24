
package genetic;

import java.util.ArrayList;
import java.util.List;

import neuralnetworking.NeuralNetwork;

public class Population {

	public List<Genotype> genomes;
	
	public Population(int populationSize) {
		genomes = new ArrayList<Genotype>();
		for (int i = 0; i < populationSize; i++) {
			genomes.add(new Genotype());
		}
	}
	
	public void evolve(float elitism, float randomness, float mutationRate, float mutationStdDev, int childCount) {
		normalFitnessDistribution();
		sortByFitness();
		List<Genotype> nextGeneration = new ArrayList<Genotype>();
		int eliteCount = Math.round(elitism*genomes.size());
		for (int i = 0; i < eliteCount; i++) {
			nextGeneration.add(new Genotype(genomes.get(i)));
		}
		int randomCount = Math.round(randomness*genomes.size());
		for (int i = 0; i < randomCount; i++) {
			NeuralNetwork.FlattenNetwork cNet  = genomes.get(0).dino.cactusNet.flatten();
			for (int j = 1; j < cNet.weights.size(); j++) {
				cNet.weights.set(j, (float) (Math.random()*2 - 1));
			}
			NeuralNetwork.FlattenNetwork bNet = genomes.get(0).dino.birdNet.flatten();
			for (int j = 1; j < bNet.weights.size(); j++) {
				bNet.weights.set(j, (float) (Math.random()*2 - 1));
			}
			nextGeneration.add(new Genotype(cNet, bNet));
		}

		int max = 0;
		while (true) {
			for (int i = 0; i < max; i++) {
				List<Genotype> children = Genotype.breed(genomes.get(i), genomes.get(max), childCount, mutationRate, mutationStdDev);
				for (Genotype child: children) {
					nextGeneration.add(child);
					if (nextGeneration.size() >= genomes.size()) {
						genomes = nextGeneration;
						return;
					}
				}
			}
			max++;
			max = max >= genomes.size()-1 ? 0 : max;
		}
	}

	private void normalFitnessDistribution() {
		float sum = 0f;
		for (Genotype genome: genomes) {
			sum += genome.dino.score;
		}
		for (Genotype genome: genomes) {
			genome.fitness = genome.dino.score / sum;
		}
	}
	

	private void sortByFitness() {
		for (int i = 0; i < genomes.size()-1; i++) {
			int bestIndex = i;
			for (int j = i+1; j < genomes.size(); j++) {
				if (genomes.get(j).fitness > genomes.get(bestIndex).fitness) {
					bestIndex = j;
				}
			}
			if (bestIndex != i) {
				Genotype temp = genomes.get(bestIndex);
				genomes.set(bestIndex, genomes.get(i));
				genomes.set(i, temp);
			}
		}
	}
}
