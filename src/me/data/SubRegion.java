package data;

import javafx.scene.shape.Polygon;

import java.util.ArrayList;

/**
 * Created by jappatel on 6/29/16.
 */
public class SubRegion {
	private ArrayList<Polygon> polygons;
	private String name;
	private String leader;
	private String capital;

	public SubRegion(){
		polygons = new ArrayList<>();

	}

	public SubRegion(String name, String leader, String capital){
		this.name = name;
		this.leader = leader;
		this.capital = capital;
		polygons = new ArrayList<>();
	}

	public ArrayList<Polygon> getPolygons() {
		return polygons;
	}

	public void setPolygons(ArrayList<Polygon> polygons) {
		this.polygons = polygons;
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
}
