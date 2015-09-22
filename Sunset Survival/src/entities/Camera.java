package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix3f;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public class Camera {
	
	private static final int halfHeight = Display.getHeight()/2;
	private static final int halfWidth = Display.getWidth()/2;
	
	private Vector3f position = new Vector3f(0, 0, 0);
	private float pitch;
	private float yaw;
	private float roll;
	
	public Camera() {}
	
	public void move() {
		
		pitch += (halfHeight - Mouse.getY()) * 0.2;
		yaw += (Mouse.getX() - halfWidth) * 0.2;
		Mouse.setCursorPosition(halfWidth, halfHeight);
		if(pitch > 90) {pitch = 90;}
		if(pitch < -90) {pitch = -90;}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_W)) {
			position.x += Math.sin(Math.toRadians(yaw)) * 0.2;
			position.z -= Math.cos(Math.toRadians(yaw)) * 0.2;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_S)) {
			position.x -= Math.sin(Math.toRadians(yaw)) * 0.2;
			position.z += Math.cos(Math.toRadians(yaw)) * 0.2;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_D)) {
			position.x += Math.cos(Math.toRadians(yaw)) * 0.2;
			position.z += Math.sin(Math.toRadians(yaw)) * 0.2;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_A)) {
			position.x -= Math.cos(Math.toRadians(yaw)) * 0.2;
			position.z -= Math.sin(Math.toRadians(yaw)) * 0.2;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_Q)) {
			position.y += 0.2f;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_Z)) {
			position.y -= 0.2f;
		}
		
		
		
	}

	public Vector3f getPosition() {
		return position;
	}

	public float getPitch() {
		return pitch;
	}

	public float getYaw() {
		return yaw;
	}

	public float getRoll() {
		return roll;
	}
	
	
	
}
