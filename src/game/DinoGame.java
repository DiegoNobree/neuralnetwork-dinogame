package game;

import java.util.LinkedList;
import java.util.List;

import game.factory.DinoFactory;
import game.factory.ObstacleFactory;
import game.model.BirdObstacle;
import game.model.CactusObstacle;
import game.model.CactusObstacle.CactusType;
import game.model.Dino;
import game.model.Obstacle;
import game.model.Obstacle.ObstacleType;
import genetic.GeneticAlgorithm;
import genetic.Genotype;
import neuralnetworking.NeuralNetwork;
import processing.core.PApplet;
import processing.core.PImage;
import util.Screen;

public class DinoGame extends PApplet {
	
	List<Obstacle> obstacles;
	GeneticAlgorithm agent;
	
	int groundLevel;
	int tickCount = 0;
	int spawnRate = 140;
	int speedupRate = 1000;
	
	int score = 0;
	int highscore = 0;
	int speed = 1;
	int maxSpeed = 10;
	
	PImage dinoRun1Image;
	PImage dinoRun2Image;
	PImage dinoDuckImage;
	PImage dinoJumpImage;
	PImage cactusLargeImage;
	PImage birdImage;
	PImage birdSecondImage;
	PImage cactusMediumImage;
	PImage cactusSmallImage;
	PImage groundImage;
	
	@Override
	public void settings() {
		size(1366, 768);
		Screen.setDimensions(width, height);
		groundLevel = height - 100;
		obstacles = new LinkedList<Obstacle>();
		DinoFactory.init(100, groundLevel);
		ObstacleFactory.init(0.3f, width, groundLevel);
		agent = new GeneticAlgorithm();
	}
	
	@Override
	public void setup() {
		clearScreen();
		surface.setTitle("Neuroevolution Chrome Dino");
		cactusLargeImage = loadImage("resources/cactusLarge.png");
		cactusMediumImage = loadImage("resources/cactusMedium.png");
		cactusSmallImage = loadImage("resources/cactusSmall.png");
		birdImage = loadImage("resources/enemy1.png");
		groundImage = loadImage("resources/ground.png");
		birdSecondImage = loadImage("resources/enemy2.png");
		dinoRun1Image = loadImage("resources/dinoRun1.png");
		dinoRun2Image = loadImage("resources/dinoRun2.png");
		dinoJumpImage = loadImage("resources/dinoJump.png");
		dinoDuckImage = loadImage("resources/dinoDuck.png");
	}
	
	@Override
	public void draw() {
		for (int i = 0; i < speed; i++) {
			tickCount++;
			clearScreen();
			drawGenerationInfo();
			renderGround();
			obstacles.forEach(obstacle -> renderObstacle(obstacle));
			agent.population.genomes.forEach(genome -> renderDino(genome.dino));
			renderNeuralNetwork(agent.getBestGenome().cactusNet, 850);
			renderNeuralNetwork(agent.getBestGenome().birdNet, 1100);
			obstacles.removeIf(obstacle -> obstacle.isInvisible());
			for (Genotype genome: agent.population.genomes) {
				for (Obstacle obstacle: obstacles) {
					checkCollision(obstacle, genome.dino);
				}
			}
			if (tickCount % spawnRate == 0) {
				spawnObstacle();
			}
			
			if (tickCount % speedupRate == 0) {
				Obstacle.speedUp();
			}
			
			obstacles.forEach(obstacle -> obstacle.update());
			agent.updatePopulation(obstacles);
			if (agent.populationDead()) {
				reset();
			}
		}
	}
	
	public void keyPressed() {
		if (key == CODED) {
			if (keyCode == UP) {
				speed = min(maxSpeed, ++speed);
			} else if (keyCode == DOWN) {
				speed = max(1, --speed);
			}
		}
	}
	
	private void reset() {
		obstacles.clear();
		tickCount = 0;
		agent.evolvePopulation();
		score = 0;
		spawnRate = 140;
	}
	
	private void checkCollision(Obstacle obstacle, Dino dino) {
		if (!dino.isDead) {
			if (obstacle.checkDinoCollision(dino)) {
				dino.isDead = true;
				agent.alive--;
			}
		}
	}
	
	private void spawnObstacle() {
		obstacles.add(ObstacleFactory.getObstacle());
	}
	
	private void renderDino(Dino dino) {
		if (!dino.isDead) {
			if (agent.alive > 1) {
				stroke(0);
				strokeWeight(1);
				fill(125, 125, 125, 50);
				rect(dino.x, dino.y-dino.height, dino.width, dino.height);
			} else {
				if (dino.isDucking) {
					image(dinoDuckImage, dino.x, dino.y-dino.height, dino.width, dino.height);
				} else if (dino.y < groundLevel) {
					image(dinoJumpImage, dino.x, dino.y-dino.height, dino.width, dino.height);
				} else if (dino.state == 0) {
					image(dinoRun1Image, dino.x, dino.y-dino.height, dino.width, dino.height);
				} else {
					image(dinoRun2Image, dino.x, dino.y-dino.height, dino.width, dino.height);
				}
			}
		}
	}
	
	private void renderObstacle(Obstacle obstacle) {
		if (obstacle.type == ObstacleType.BIRD) {
			renderObstacle((BirdObstacle) obstacle);
		} else {
			renderObstacle((CactusObstacle) obstacle);
		}
	}
	
	private void renderObstacle(BirdObstacle obstacle) {
		if (obstacle.state == 0) {
			image(birdImage, obstacle.x, obstacle.y - obstacle.height, obstacle.width, obstacle.height);
		} else {
			image(birdSecondImage, obstacle.x, obstacle.y - obstacle.height, obstacle.width, obstacle.height);
		}
	}
	
	private void renderObstacle(CactusObstacle obstacle) {
		if (obstacle.cactusType == CactusType.LARGE) {
			image(cactusLargeImage, obstacle.x, obstacle.y - obstacle.height, obstacle.width, obstacle.height);
		} else if (obstacle.cactusType == CactusType.MEDIUM) {
			image(cactusMediumImage, obstacle.x, obstacle.y - obstacle.height, obstacle.width, obstacle.height);
		} else {
			image(cactusSmallImage, obstacle.x, obstacle.y - obstacle.height, obstacle.width, obstacle.height);
		}
	}
	
	private void drawGenerationInfo() {
		score = agent.getBestScore();
		highscore = Math.max(score, highscore);
		textSize(22);
		fill(0);
		text("Score: " + score, 20, 50);
		text("Generation: " + agent.generation, 20, 80);
		text("Alive: " + agent.alive + " / " + agent.populationSize, 20, 110);
		text("Highscore: " + highscore, 20, 140);

	}
	
	private void renderNeuralNetwork(NeuralNetwork net, float beginx) {

	}
	
	private void renderGround() {
		image(groundImage, 0, groundLevel-10, width, 20);
	}
	
	private void clearScreen() {
		background(255);
	}
}
