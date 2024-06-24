
package genetic;

import java.util.List;

import game.factory.DinoFactory;
import game.model.Dino;
import game.model.Obstacle;

public class GeneticAlgorithm {
	
	public class ObstacleInfo {
		public float distance;
		public Obstacle closestObstacle;
		
		public ObstacleInfo(float distance, Obstacle closestObstacle) {
			this.distance = distance;
			this.closestObstacle = closestObstacle;
		}
	}

	public Population population;
	public int alive;
	public int generation;
	
	public int populationSize = 1000;
	public float elitism = 0.2f;
	public float mutationRate = 0.1f;
	public float mutationStdDev = 0.5f;
	public float randomness = 0.2f;
	public int childCount = 1;
	
	private Dino bestGenome;
	
	public GeneticAlgorithm() {
		population = new Population(populationSize);
		bestGenome = population.genomes.get(0).dino;
		alive = populationSize;
		generation = 1;
	}
	
	public void updatePopulation(List<Obstacle> obstacles) {
		ObstacleInfo data = getClosestObstacle(obstacles);
		for (Genotype genome: population.genomes) {
			if (!genome.dino.isDead) {
				genome.dino.feed(data.closestObstacle, data.distance);
				genome.dino.update();
			}
		}
	}
	
	public void evolvePopulation() {
		alive = populationSize;
		generation++;
		population.evolve(elitism, randomness, mutationRate, mutationStdDev, childCount);
		bestGenome = population.genomes.get(0).dino;
	}
	
	public Dino getBestGenome() {
		return bestGenome;
	}
	
	public int getBestScore() {
		int best = 0;
		for (Genotype genome: population.genomes) {
			if (genome.dino.score > best) {
				best = genome.dino.score;
			}
		}
		return best;
	}
	
	public boolean populationDead() {
		for (Genotype genome: population.genomes) {
			if (!genome.dino.isDead) {
				return false;
			}
		}
		return true;
	}
	
	private ObstacleInfo getClosestObstacle(List<Obstacle> obstacles) {
		Obstacle closestObstacle = null;
		float distance = Float.MAX_VALUE;
		for (Obstacle obstacle: obstacles) {
			distance = obstacle.x - DinoFactory.getSpawnX();
			if (distance > 0) {
				closestObstacle = obstacle;
				break;
			}
		}
		return new ObstacleInfo(distance, closestObstacle);
	}
}
