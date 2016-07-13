package gui;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.SelectionModel;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Polygon;

/**
 * Created by jappatel on 7/13/16.
 */
public abstract class SelectablePolygons extends Group{
	private SelectionModel<Node> selectionModel;

	public SelectablePolygons(){
		selectionModel = new SingleSelectionModel<Node>() {
			@Override
			protected Node getModelItem(int index) {
				return getChildren().get(index);
			}

			@Override
			protected int getItemCount() {
				return getChildren().size();
			}
		};
	}

	public void add(Node node){
		getChildren().add(node);
		node.setOnMouseClicked(e -> {
			if(e.getClickCount() == 1){
				setSelected((Node)e.getSource());
			}
			onMouseClickedHook(e);
		});
	}

	public abstract void onMouseClickedHook(MouseEvent e);

	public void setSelected(Node node){
		Node selectedItem = selectionModel.getSelectedItem();
		if(selectedItem!=null) {
			((Polygon)selectedItem).setFill(((Polygon)node).getFill());
		}
		((Polygon)node).setFill(Paint.valueOf("#000000"));
		selectionModel.select(node);
	}

	public void setSelected(int index) {
		Node selectedItem = selectionModel.getSelectedItem();
		Node node = getChildren().get(index);
		if(selectedItem!=null) {
			((Polygon)selectedItem).setFill(((Polygon)node).getFill());
		}
		((Polygon)node).setFill(Paint.valueOf("#000000"));
		selectionModel.select(node);
	}

	public SelectionModel<Node> getSelectionModel(){
		return selectionModel;
	}

}
