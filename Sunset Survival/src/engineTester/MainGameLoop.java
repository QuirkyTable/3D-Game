package engineTester;

import java.util.Random;

import models.RawModel;
import models.TexturedModel;
import objConverter.ModelData;
import objConverter.OBJFileLoader;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Entity;
import entities.Light;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import terrains.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;

public class MainGameLoop {

	public static void main(String[] args) {

		DisplayManager.createDisplay();

		Loader loader = new Loader();

		Random random = new Random();

		// ******* TERRAIN TEXTURE LOADING ******* //

		TerrainTexture backgroundTexture = new TerrainTexture(
				loader.loadTexture("TerrainGrass"));
		TerrainTexture rTexture = new TerrainTexture(
				loader.loadTexture("TerrainDirt"));
		TerrainTexture gTexture = new TerrainTexture(
				loader.loadTexture("TerrainSand"));
		TerrainTexture bTexture = new TerrainTexture(
				loader.loadTexture("TerrainRoad"));

		TerrainTexturePack texturePack = new TerrainTexturePack(
				backgroundTexture, rTexture, gTexture, bTexture);
		
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));
		
		/**
		 * CREATING FERNS
		 */
		ModelData fernData = OBJFileLoader.loadOBJ("fern");
		// Load the RawModel
		RawModel fernModel = loader.loadToVAO(fernData.getVertices(),
				fernData.getTextureCoords(), fernData.getNormals(),
				fernData.getIndices());
		// Load the ModelTexture
		ModelTexture fernTexture = new ModelTexture(loader.loadTexture("fern"));
		// Load the texturedModel
		TexturedModel fernTexturedModel = new TexturedModel(fernModel,
				fernTexture);
		// Set some details about the texture
		fernTexture.setHasTransparency(true);
		fernTexture.setShineDamper(30);
		fernTexture.setReflectivity(0.2f);
		fernTexture.setUseFakeLighting(false);
		// Create an array of ferns
		Entity[] ferns = new Entity[200];
		for (int i = 0; i < ferns.length; i++) {
			int locX = random.nextInt(800);
			int locZ = random.nextInt(800);
			ferns[i] = new Entity(fernTexturedModel,
					new Vector3f(locX, 0, locZ), 0, 0, 0, 1);
		}

		/**
		 * CREATING GRASS
		 */
		ModelData grassData = OBJFileLoader.loadOBJ("grassModel");
		// Load the RawModel
		RawModel grassModel = loader.loadToVAO(grassData.getVertices(),
				grassData.getTextureCoords(), grassData.getNormals(),
				grassData.getIndices());
		// Load the ModelTexture
		ModelTexture grassTexture = new ModelTexture(
				loader.loadTexture("grassTexture"));
		// Load the texturedModel
		TexturedModel grassTexturedModel = new TexturedModel(grassModel,
				grassTexture);
		// Set some details about the texture
		grassTexturedModel.getTexture().setHasTransparency(true);
		grassTexture.setShineDamper(30);
		grassTexture.setReflectivity(0.2f);
		grassTexture.setUseFakeLighting(true);
		// Create an array of ferns
		int k = 0;
		Entity[] grass = new Entity[32 * 32];
		for (int i = 0; i < grass.length; i++) {
			float locX = random.nextFloat() * 800;
			float locZ = random.nextFloat() * 800;
			grass[k++] = new Entity(grassTexturedModel, new Vector3f(locX, 0,
					locZ), 0, random.nextFloat() * 360, 0, 1);
		}

		Light light = new Light(new Vector3f(0, 0, -20), new Vector3f(1, 1, 1));

		Terrain terrain = new Terrain(0, 0, loader, texturePack, blendMap);

		Camera camera = new Camera();

		MasterRenderer renderer = new MasterRenderer();
		while (!Display.isCloseRequested()) {
			camera.move();
			renderer.processTerrain(terrain);
			for (int i = 0; i < ferns.length; i++) {
				renderer.processEntity(ferns[i]);
			}
			for (int i = 0; i < grass.length; i++) {
				renderer.processEntity(grass[i]);
			}
			renderer.render(light, camera);
			DisplayManager.updateDisplay();

		}

		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();

	}

}
