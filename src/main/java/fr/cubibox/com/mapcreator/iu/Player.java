package fr.cubibox.com.mapcreator.iu;

import fr.cubibox.com.mapcreator.Main;
import fr.cubibox.com.mapcreator.maths.Vector2F;

public class Player {
	private float posX;
	private float posY;
	private float screenX; 
	private float screenY; 
	private Vector2F vector;

	public Player(float posX, float posY) {
		this.setPosX(posX);
		this.setPosY(posY);
		this.setScreenX(Main.toScreenX(posX));
		this.setScreenY(Main.toScreenY(posY));
		this.setPoint(new Vector2F(posX,posY));
	}

	public float getPosX() {
		return posX;
	}

	public void setPosX(float posX) {
		this.posX = posX;
	}

	public float getPosY() {
		return posY;
	}

	public void setPosY(float posY) {
		this.posY = posY;
	}

	public float getScreenX() {
		return screenX;
	}

	public void setScreenX(float screenX) {
		this.screenX = screenX;
	}

	public float getScreenY() {
		return screenY;
	}

	public void setScreenY(float screenY) {
		this.screenY = screenY;
	}


	public Vector2F getPoint() {
		return this.vector;
	}

	public void setPoint(Vector2F vector) {
		this.vector = vector;
	}
}
