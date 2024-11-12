package oop;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public interface BackgroundLoader
{
    void setBackgroundImageVBOX(VBox pane, String imagePath);
    void setBackgroundImageGRID(GridPane grid, String ImagePath);
    void setBackgroundImage(StackPane pane, String imagePath);
}
