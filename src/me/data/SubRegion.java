package data;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

import java.util.ArrayList;

/**
 * Created by jappatel on 6/29/16.
 */
public class SubRegion {
	private double[][] subPoints;
	private StringProperty name;
	private StringProperty leader;
	private StringProperty capital;
	private double color;

	public SubRegion(){
		name = new SimpleStringProperty("");
		leader = new SimpleStringProperty("");
		capital = new SimpleStringProperty("");
		color = 255;
	}

	public SubRegion(String name, String leader, String capital){
		this.name.set(name);
		this.leader.set(leader);
		this.capital.set(capital);
	}

	public double[][] getSubPoints() {
		return subPoints;
	}

	public void setSubPoints(double[][] subPoints) {
		this.subPoints = subPoints;
	}

	public String getName() {
		return name.get();
	}

	public StringProperty nameProperty() {
		return name;
	}

	public void setName(String name) {
		this.name.set(name);
	}

	public String getLeader() {
		return leader.get();
	}

	public StringProperty leaderProperty() {
		return leader;
	}

	public void setLeader(String leader) {
		this.leader.set(leader);
	}

	public String getCapital() {
		return capital.get();
	}

	public StringProperty capitalProperty() {
		return capital;
	}

	public void setCapital(String capital) {
		this.capital.set(capital);
	}

	public double getColor() {
		return color;
	}

	public void setColor(double color) {
		this.color = color;
	}

	public String toString(){
		return name.getValue() + " " + leader.getValue() + " " + capital.get() + " " + color;
	}
}
