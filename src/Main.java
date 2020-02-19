//
// how to add support for javafx:
//  https://www.jetbrains.com/help/idea/javafx.html
//
//images, javafx, and pixels: https://www.tutorialspoint.com/javafx/javafx_images.htm
//
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start ( Stage stage ) throws Exception {
        Parent root = FXMLLoader.load( getClass().getResource("main.fxml") );
        Scene scene = new Scene( root );
        stage.setTitle( "JFXImageViewer" );
        stage.setScene( scene );
        stage.setX( MainController.stageX );
        stage.setY( MainController.stageY );
        stage.show();
    }
    //-----------------------------------------------------------------------
    public static void main ( String[] args ) {
        launch( args );
    }

}
