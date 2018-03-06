/*
 * CS2852 - 021
 * Spring 2018
 * Lab 1 - Dot 2 Dot Generator
 * Name: Rock Boynton
 * Created: 3/6/2018
 */

package boyntonrl;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.logging.Level;

/**
 * Controller class for the Dot 2 Dot Generator application
 */
public class Controller {

    @FXML
    private MenuItem openMenuItem;
    @FXML
    private MenuItem closeMenuItem;
    @FXML
    private Canvas canvas;
    @FXML
    private MenuItem linesOnly;
    @FXML
    private MenuItem dotsOnly;

    @FXML
    private void open(ActionEvent e) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Dot Files", "*.dot"));
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            image = ImageIO.read(file);
            imageView.setImage(image);
        } else {
            LOGGER.log(Level.INFO, "User canceled loading image");
        }
    }

    @FXML
    private void close(ActionEvent e) {
        Platform.exit();
    }

    @FXML
    private void setLinesOnly(ActionEvent e) {

    }

    @FXML
    private void setDotsOnly(ActionEvent e) {

    }
}
