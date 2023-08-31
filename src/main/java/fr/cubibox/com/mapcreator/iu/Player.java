package fr.cubibox.com.mapcreator.iu;

import fr.cubibox.com.mapcreator.Main;
import fr.cubibox.com.mapcreator.maths.Vector2F;

public class Player {
	private float posX;
	private float posY;
	private Vector2F vector;

	public Player(float posX, float posY) {
		this.setPosX(posX);
		this.setPosY(posY);
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


	public Vector2F getPoint() {
		return this.vector;
	}

	public void setPoint(Vector2F vector) {
		this.vector = vector;
	}
}
