package fr.cubibox.com.mapcreator.iu;

import fr.cubibox.com.mapcreator.Main;

public class Player {
	private float posX;
	private float posY;
	private float screenX; 
	private float screenY; 
	private Point point;

	public Player(float posX, float posY) {
		this.setPosX(posX);
		this.setPosY(posY);
		this.setScreenX(Main.toScreenX(posX));
		this.setScreenY(Main.toScreenY(posY));
		this.setPoint(new Point(posX,posY));
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


	public Point getPoint() {
		return this.point;
	}

	public void setPoint(Point point) {
		this.point = point;
	}
}
