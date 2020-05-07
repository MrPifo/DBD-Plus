package sperlich;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.scene.text.Text;
import javafx.scene.image.ImageView;

import java.io.File;
import java.nio.Buffer;

public class Window {
    /*public Text killerName;
    public ImageView killerPic;
    @Override
    public void start(Stage stage) throws Exception {
        System.out.println("Render Window");
        Parent root = FXMLLoader.load(getClass().getResource("window.fxml"));
        Scene scene = new Scene(root);
        stage.setTitle("DBD Assistance");
        stage.getIcons().add(new Image("file:dbd_skillcheck.png"));
        killerName = (Text)root.lookup("#killerId");
        killerPic = (ImageView)root.lookup("#killerPic");
        Image pic = new Image(new File("src/assets/charSprites/unknown.png").toURI().toString());
        killerPic.setImage(pic);
        Main.window = this;
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
    public void setKillerPic(int id) throws ExceptionHandler {
        if (id > 0) {
            File f = new File("src/assets/charSprites/slasher_" + id + ".png");
            if (f.exists()) {
                killerPic.setImage(new Image(f.toURI().toString()));
            } else {
                throw new ExceptionHandler("Killer Image not found");
            }
        }
    }*/
}
