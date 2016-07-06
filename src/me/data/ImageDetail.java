package data;

import javafx.scene.image.Image;

/**
 * Created by jappatel on 7/5/16.
 */
public class ImageDetail {
	private String image;
	private double x;
	private double y;

	public ImageDetail(String image, double x, double y){
		this.image = image;
		this.x = x;
		this.y = y;
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
}
