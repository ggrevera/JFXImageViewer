/**
    \file   ImageData.java
    \brief  contains ImageData class definition (note that this class is 
            abstract)
    \author George J. Grevera, Ph.D., ggrevera@sju.edu

    Copyright (C) 2006, George J. Grevera

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
    USA or from http://www.gnu.org/licenses/gpl.txt.

    This General Public License does not permit incorporating this
    code into proprietary programs.  (So a hypothetical company such
    as GH (Generally Hectic) should NOT incorporate this code into
    their proprietary programs.)
 */

import javafx.scene.image.Image;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelReader;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
//----------------------------------------------------------------------
/** \brief class containing the actual pixel data values (note that this
 *  class is abstract)
 *
 *  This class contains the actual image pixel data.
 *  Note that this class is abstract.
 */
abstract public class ImageData {
    protected boolean  mIsColor;          ///< true if color, false if grey
    protected boolean  mIsAudio;          ///< true if audio; false if color or ordinary gray
    protected boolean  mImageModified;    ///< true if image has been modified
    protected int      mW;                ///< image width
    protected int      mH;                ///< image height
    protected int      mMin;              ///< min image pixel value
    protected int      mMax;              ///< max image pixel value
    protected String   mFname;            ///< (optional) file name
    protected int      mRate;             ///< samples per sec (audio only)

    /** \brief Actual unpacked (1 component per array entry) image data.
      *
      *  If the image data are gray, each entry in this array represents a
      *  gray pixel value.  So mImageData[0] is the first pixel's gray
      *  value, *  mImageData[1] is the second pixel's gray value, and so
      *  on.  Each value may be 8-bits or 16-bits.  16 bits allows for
      *  values in the range [0..65535].
      *  <br/>
      *  If the image data are color, triples of entries (i.e., 3) represent
      *  each color rgb value.  So each value is in [0..255] for 24-bit
      *  color where each component is 8-bits.  So mImageData[0] is the
      *  first pixel's red value, mImageData[1] is the first pixel's green
      *  value, mImageData[2] is the first pixel's blue value, mImageData[3]
      *  is the second pixel's red value, and so on.
      */
    protected int[]  mOriginalData;

    /** \brief Almost displayable ("almost" because it may be used to
     *  create a BufferedImage but only a BufferedImage may be actually
     *  drawn in a window) packed (each array element contains an rgb
     *  value) version of the above unpacked pixel data.
     */
    public int[]  mDisplayData;

    public Image  displayImage;
    public BufferedImage  mDisplayImage  = null;  ///< \brief (possibly modified input) image drawn on screen
    //----------------------------------------------------------------------
    /** \brief Given the name of an input image file, this method determine
     *  the type of image and then invokes the appropriate constructor.
     *
     *  Note that this static function returns the appropriate subclass of
     *  ImageData depending upon the type of image data (color or gray).
     *  \param fileName name of input image file
     *  \returns an instance of the ImageData class
     */
    public static ImageData load ( String fileName ) {
        //load the image
        String up = fileName.toUpperCase();
        if (up.endsWith(".PPM") || up.endsWith(".PNM") || up.endsWith(".PGM")) {
            PNMHelper p = new PNMHelper( fileName );
            if (p.mMin < 0) {
                JOptionPane.showMessageDialog( null,
                        "Warning: \n\nMin value of " + p.mMin + " is less than 0. \n ",
                        "Warning", JOptionPane.WARNING_MESSAGE );
            }
            if (p.mMax > 255) {
                JOptionPane.showMessageDialog( null,
                        "Warning: \n\nMax value of " + p.mMax + " exceeds limit of 255. \n ",
                        "Warning", JOptionPane.WARNING_MESSAGE );
            }
            if (p.mSamplesPerPixel == 1) {
                return new GrayImageData( p.mData, p.mW, p.mH );
            } else {
                assert p.mSamplesPerPixel == 3;
                return new ColorImageData( p.mData, p.mW, p.mH );
            }
        }

        //Image image = new Image( fileName );
        Image image = null;
        try {
            image = new Image(new FileInputStream(fileName));
        } catch (Exception e) {
            System.out.println( "error opening " + fileName );
        }
        PixelReader pr = image.getPixelReader();
        PixelFormat.Type type = pr.getPixelFormat().getType();
        switch (type) {
            case BYTE_BGRA:
                System.out.println( "pixel type: BYTE_BGRA" );
                break;
            case BYTE_BGRA_PRE:
                System.out.println( "pixel type: BYTE_BGRA_PRE" );
                break;
            case BYTE_INDEXED:
                System.out.println( "pixel type: BYTE_INDEXED" );
                break;
            case BYTE_RGB:
                System.out.println( "pixel type: BYTE_RGB" );
                break;
            case INT_ARGB:
                System.out.println( "pixel type: INT_ARGB" );
                break;
            case INT_ARGB_PRE:
                System.out.println( "pixel type: INT_ARGB_PRE" );
                break;
            default:
                System.err.println( "unrecognized image pixel type: " + type );
                break;
        }
/*
        File f = new File( fileName );
        BufferedImage bi = null;
        try {
            bi = ImageIO.read( f );
        } catch (Exception e) {
            System.err.println( "error reading file" );
        }
        assert bi != null;
        int w  = bi.getWidth();
        int h  = bi.getHeight();
        if ( bi.getType() == BufferedImage.TYPE_USHORT_GRAY ||
             bi.getType() == BufferedImage.TYPE_BYTE_GRAY   ||
             bi.getType() == BufferedImage.TYPE_BYTE_BINARY ) {
            return new GrayImageData( bi, w, h );
        }
        //otherwise, it must be color
        //assert bi.getType() == BufferedImage.TYPE_INT_RGB;
        return new ColorImageData( bi, w, h );
*/
        return new ColorImageData( image );
    }

}
//----------------------------------------------------------------------

