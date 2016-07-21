package gui;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.SelectionModel;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.effect.InnerShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Polygon;

/**
 * Created by jappatel on 7/13/16.
 */
public abstract class SelectableNode extends Group{
	private SelectionModel<Node> selectionModel;
	private Effect effect;

	public SelectableNode(Effect effect){
		selectionModel = new SingleSelectionModel<Node>() {
			@Override
			protected Node getModelItem(int index) {
				if(index < 0)
					return getChildren().get(getChildren().size()-1);
				return getChildren().get(index);
			}

			@Override
			protected int getItemCount() {
				return getChildren().size();
			}
		};
		this.effect = effect;
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
			selectedItem.setEffect(null);
		}
		node.setEffect(effect);
		selectionModel.select(node);
	}

	public void setSelected(int index) {
		Node selectedItem = selectionModel.getSelectedItem();
		Node node = getChildren().get(index);
		if(selectedItem!=null) {
				selectedItem.setEffect(null);
		}
		node.setEffect(effect);
		selectionModel.select(node);
	}

	public void deselect(){
		(selectionModel.getSelectedItem()).setEffect(null);
	}

	public SelectionModel<Node> getSelectionModel(){
		return selectionModel;
	}

}
