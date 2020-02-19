import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

class MainView {

    private MainController parent;
    boolean mouseMoveValid;
    double mouseX;
    double mouseY;
    double zoom = 2.0;
    //-----------------------------------------------------------------------
    MainView ( MainController p ) {
        System.out.println( "MainView" );
        this.parent = p;
    }
    //-----------------------------------------------------------------------
    void paint ( ) {
        GraphicsContext gc = this.parent.canvas.getGraphicsContext2D();
        //set background color
        gc.setFill( Color.DARKGRAY );
        if (this.parent.bp.getWidth()>0 || this.parent.bp.getHeight()>0)
            gc.fillRect( 0, 0, this.parent.bp.getWidth(), this.parent.bp.getHeight() );
        else
            gc.fillRect( 0, 0, this.parent.bp.getPrefWidth(), this.parent.bp.getPrefHeight() );

        if (this.parent.image != null)
            gc.drawImage( this.parent.image, 0, 0, this.parent.image.getWidth() *this.zoom,
                                                          this.parent.image.getHeight()*this.zoom );

        if (this.mouseMoveValid) {
            System.out.println();
            System.out.println( this.parent.sp.getVvalue() );  // in [0.0 .. 1.0]
            //System.out.println( this.parent.sp.getViewportBounds() );
            //System.out.println( this.parent.sp.getTranslateY() );
            //System.out.println( this.parent.sp.getLocalToSceneTransform() );
            //System.out.println( this.parent.sp.getLocalToParentTransform() );
            //System.out.println( this.parent.sp.getLayoutY() );
            System.out.println( this.parent.sp.getLayoutBounds() );
            double xOff = 0;
            double yOff = 0;
            if (this.parent.image != null) {
                xOff = this.parent.sp.getHvalue() * this.parent.image.getWidth();
                yOff = this.parent.sp.getVvalue() * this.parent.image.getHeight();
            }
            String s = "(" + this.mouseX + "," + this.mouseY + ")";
            gc.setFill( Color.BLACK );
            gc.fillText( s, 20+xOff, 40+yOff );
            gc.setFill( Color.WHITE );
            gc.fillText( s, 21+xOff, 41+yOff );
            System.out.println( s );
        }
    }

}

