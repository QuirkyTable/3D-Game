package shaders;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

public abstract class ShaderProgram {
	
	private int programID;
	private int vertexShaderID;
	private int fragmentShaderID;
	
	public ShaderProgram(String vertexFile, String fragmentFile) {
		vertexShaderID = loadShader(vertexFile, GL20.GL_VERTEX_SHADER);
		fragmentShaderID = loadShader(fragmentFile, GL20.GL_FRAGMENT_SHADER);
		programID = GL20.glCreateProgram();
		GL20.glAttachShader(programID, vertexShaderID);
		GL20.glAttachShader(programID, fragmentShaderID);
		GL20.glLinkProgram(programID);
		GL20.glValidateProgram(programID);
		bindAttributes();
	}
	
	public void start() {
		GL20.glUseProgram(programID);
	}
	
	public void stop() {
		GL20.glUseProgram(0);
	}
	
	public void cleanUp() {
		stop();
		GL20.glDetachShader(programID, vertexShaderID);
		GL20.glDetachShader(programID, fragmentShaderID);
		GL20.glDeleteShader(vertexShaderID);
		GL20.glDeleteShader(fragmentShaderID);
		GL20.glDeleteProgram(programID);
	}
	
	protected void bindAttribute(int attribute, String variableName) {
		GL20.glBindAttribLocation(programID, attribute, variableName);
	}
	
	protected abstract void bindAttributes();
	
	private static int loadShader(String file, int type) {
		// StringBuilder to build a string out of the read file
		StringBuilder shaderSource = new StringBuilder();
		try {
			// BufferedReader(FileReader) to read the shader file
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;
			// Building the shader string
			while((line = reader.readLine()) != null) {
				shaderSource.append(line).append("\n");
			}
			reader.close();
		} catch(IOException e) {
			// Error handling, in case file readers fail
			System.err.println("Could not read file!");
			e.printStackTrace();
			System.exit(-1);
		}
		// Create new shader and attach source string
		int shaderID = GL20.glCreateShader(type);
		GL20.glShaderSource(shaderID,  shaderSource);
		GL20.glCompileShader(shaderID);
		// If GL_COMPILE_STATUS is FALSE, print error log
		if(GL20.glGetShaderi(shaderID,  GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
			System.out.println(GL20.glGetShaderInfoLog(shaderID, 500));
			System.err.println("Could not compile shader.");
			System.exit(-1);
		}
		return shaderID;
	}
	
}
