package gui;

import data.ImageDetail;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Created by jappatel on 7/12/16.
 */
public class DraggableImageView extends ImageView {
	private double mouseX ;
	private double mouseY ;
	public DraggableImageView(Image image, ImageDetail id) {
		super(image);
		setOnMousePressed(event -> {
			mouseX = getX() - event.getX();
			mouseY = getY() - event.getY();
		});
		setOnMouseDragged(event -> {
			setX(event.getX() + mouseX);
			setY(event.getY() + mouseY);
			id.setX(getX());
			id.setY(getY());
		});
	}

}
