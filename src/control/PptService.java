package control;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.poi.hslf.HSLFSlideShow;
import org.apache.poi.hslf.model.Slide;
import org.apache.poi.hslf.model.TextRun;
import org.apache.poi.hslf.usermodel.SlideShow;
import org.apache.poi.xslf.XSLFSlideShow;
import org.apache.poi.xslf.usermodel.XMLSlideShow;

public class PptService {
	public int translate(String path)
	{
		int i=0;
		try{
        	SlideShow ppt=null;
        	XMLSlideShow pptx=null;
        //if(path.charAt(path.length()-1)=='t')
        ppt = new SlideShow(new HSLFSlideShow(path));
        Dimension pgsize = ppt.getPageSize();
        Slide[] slide = ppt.getSlides();
        for (i = 0; i < slide.length; i++) {
        BufferedImage img = new BufferedImage(pgsize.width, pgsize.height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = img.createGraphics();
        //clear the drawing area
        graphics.setPaint(Color.white);
        graphics.fill(new Rectangle2D.Float(0, 0, pgsize.width, pgsize.height));     //render
  	    slide[i].draw(graphics);     
  	    FileOutputStream out = new FileOutputStream("./src/image/"+String.valueOf(i+1) + ".png");
        javax.imageio.ImageIO.write(img, "png", out);
        out.close();
        System.out.println(i+1);
        }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();}
		return i;
	}
}
