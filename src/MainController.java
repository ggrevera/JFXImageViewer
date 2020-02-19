import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

public class MainController implements Initializable {

    @FXML BorderPane bp;        ///< outermost (holds sp)
    @FXML ScrollPane sp;        ///< holds drawing canvas
    @FXML Canvas     canvas;    ///< drawing canvas
    private MainView mv;        ///< view in MVC

    //ImageData image;
    Image image;
    //better to migrate to file as opposed to using windows registry.
    // see http://www.davidc.net/programming/java/java-preferences-using-file-backing-store
    private static Preferences prefs = Preferences.userRoot();  ///< for user preferences ("dir" is last dir)
    static int stageX = 100;    ///< x position of window
    static int stageY = 100;    ///< y position of window
    //-----------------------------------------------------------------------
    @Override
    public void initialize ( URL location, ResourceBundle resources ) {
        this.mv = new MainView( this );
        this.mv.paint();
    }
    //-----------------------------------------------------------------------
    @FXML
    void onFileNew ( ) {
        Parent root = null;
        try {
            root = FXMLLoader.load( getClass().getResource("main.fxml") );
        } catch (Exception e) {  System.out.println( "exception: " + e );  }

        Stage stage = new Stage();
        Scene scene = new Scene( root );
        stage.setScene( scene );
        stage.setTitle( "JFXImageViewer: <empty>" );
        //set position of new window (so it doesn't entirely occlude another one)
        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
        stage.setX( (MainController.stageX += 100) % (0.80 * bounds.getWidth())  );
        stage.setY( (MainController.stageY += 100) % (0.80 * bounds.getHeight()) );

        stage.show();
    }
    //-----------------------------------------------------------------------
    @FXML
    void onFileOpen ( ) {
        //System.out.println( "onFileOpen" );
        FileChooser fc = new FileChooser();
        fc.setTitle( "Open image file(s)" );
        fc.getExtensionFilters().add( new FileChooser.ExtensionFilter("Image Files", "*.bmp", "*.gif", "*.ico", "*.jpg", "*.png", "*.pgm", "*.pnm", "*.ppm", "*.tif") );
        fc.getExtensionFilters().add( new FileChooser.ExtensionFilter("Audio Files", "*.wav") );
        fc.getExtensionFilters().add( new FileChooser.ExtensionFilter("All Files",   "*.*") );

        List<File> list = fc.showOpenMultipleDialog( null );
        if (list == null || list.size() < 1)    return;
        for (File f : list) {
            System.out.println( f );
            try {
                this.image = new Image( new FileInputStream(f) );
                this.canvas.setWidth(  this.image.getWidth()  * this.mv.zoom );
                this.canvas.setHeight( this.image.getHeight() * this.mv.zoom );

                //this.sp.setVmax( this.image.getHeight() );

                this.mv.paint();
                break;
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println( "exception: error reading " + f + " " + e );
            }
        }

        Parent root = null;
        FXMLLoader loader = new FXMLLoader( getClass().getResource("main.fxml") );
        try {
            root = loader.load();
        } catch (Exception e) {  System.out.println( "exception: " + e );  }

        MainController c = loader.getController();

        Stage stage = new Stage();
        Scene scene = new Scene( root );
        stage.setScene( scene );
        stage.setTitle( "JFXImageViewer" );
        //set position of new window (so it doesn't entirely occlude another one)
        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
        stage.setX( (MainController.stageX += 100) % (0.80 * bounds.getWidth())  );
        stage.setY( (MainController.stageY += 100) % (0.80 * bounds.getHeight()) );

        stage.show();
    }
    //-----------------------------------------------------------------------
    @FXML
    void onExit ( ) {
        //System.exit( 0 );
        Platform.exit();
    }
    //-----------------------------------------------------------------------
    @FXML
    void onMouseMoved ( MouseEvent me ) {
        //System.out.println( "onMouseMoved: " + me );
        this.mv.mouseX = me.getX();
        this.mv.mouseY = me.getY();
        this.mv.mouseMoveValid = true;
        this.mv.paint();
    }

}

