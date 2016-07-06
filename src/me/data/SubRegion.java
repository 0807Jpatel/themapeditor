package data;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

import java.util.ArrayList;

/**
 * Created by jappatel on 6/29/16.
 */
public class SubRegion {
	double[][] subPoints;
	private String name;
	private String leader;
	private String capital;
	private Color color;

	public SubRegion(){
		name = "";
		leader = "";
		capital = "";
		color = new Color(1, 1, 1, 1);
	}

	public SubRegion(String name, String leader, String capital){
		this.name = name;
		this.leader = leader;
		this.capital = capital;
	}

	public double[][] getSubPoints() {
		return subPoints;
	}

	public void setSubPoints(double[][] subPoints) {
		this.subPoints = subPoints;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLeader() {
		return leader;
	}

	public void setLeader(String leader) {
		this.leader = leader;
	}

	public String getCapital() {
		return capital;
	}

	public void setCapital(String capital) {
		this.capital = capital;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
}
