package engineTester;

import java.util.List;
import java.util.Random;

import models.RawModel;
import models.TexturedModel;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Entity;
import entities.Light;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import renderEngine.EntityRenderer;
import shaders.StaticShader;
import terrains.Terrain;
import textures.ModelTexture;

public class MainGameLoop {

	public static void main(String[] args) {
		
		DisplayManager.createDisplay();
		
		Loader loader = new Loader();
		
		// Load the rawModel
		RawModel model = OBJLoader.loadObjModel("fern", loader);
		// Load the texturedModel
		TexturedModel staticModel = new TexturedModel(model, new ModelTexture(loader.loadTexture("fern")));
		// Set some details about the texture
		ModelTexture texture = staticModel.getTexture();
		texture.setShineDamper(20);
		texture.setReflectivity(0.5f);
		// Create an array of ferns
		Entity[] ferns = new Entity[200];
		Random random = new Random();
		for(int i = 0; i < ferns.length; i++) {
			int locX = random.nextInt(800);
			int locZ = random.nextInt(800);
			ferns[i] = new Entity(staticModel, new Vector3f(locX, 0, locZ), 0, 0, 0, 1);
		}
		
		Light light = new Light(new Vector3f(0, 0, -20), new Vector3f(1, 1, 1));
		
		Terrain terrain = new Terrain(0, 0, loader, new ModelTexture(loader.loadTexture("grass")));
		Terrain terrain2 = new Terrain(1, 0, loader, new ModelTexture(loader.loadTexture("grass")));
		
		Camera camera = new Camera();
		
		
		MasterRenderer renderer = new MasterRenderer();
		while(!Display.isCloseRequested()) {
			camera.move();
			renderer.processTerrain(terrain);
			renderer.processTerrain(terrain2);
			for(int i = 0; i < ferns.length; i++) {
				renderer.processEntity(ferns[i]);
			}
			renderer.render(light, camera);
			DisplayManager.updateDisplay();
			
		}
		
		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
		
	}

}
