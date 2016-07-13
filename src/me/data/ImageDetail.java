package data;

import javafx.scene.image.Image;

/**
 * Created by jappatel on 7/5/16.
 */
public class ImageDetail {
	private int key;
	private String image;
	private double x;
	private double y;

	public ImageDetail(String image, double x, double y, int key){
		this.image = image;
		this.x = x;
		this.y = y;
		this.key = key;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public String toString(){
		return "Path: " + image + " x: " + x + " y: "  + y + " key: " + key;
	}

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}
}
