package Renderer;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.stream.IntStream;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;


public class GuassianBlur {
		
	private static JFrame frame;
	private static JLabel label;
	public static void display(BufferedImage image){
	   if(frame==null){
	       frame=new JFrame();
	       frame.setTitle("stained_image");
	       frame.setSize(image.getWidth(), image.getHeight());
	       frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	       label=new JLabel();
	       label.setIcon(new ImageIcon(image));
	       frame.getContentPane().add(label,BorderLayout.CENTER);
	       frame.setLocationRelativeTo(null);
	       frame.pack();
	       frame.setVisible(true);
	   }else label.setIcon(new ImageIcon(image));
	}
	
	 public static BufferedImage blur(BufferedImage image, int[] matrix, int matrixWidth) {

	        final int width = image.getWidth();
	        final int height = image.getHeight();
	        final int sum = IntStream.of(matrix).sum();
	        
	        //Returns an array of integer pixels in the default RGB color model
	        int[] pixels_initial = image.getRGB(0, 0, width, height, null, 0, width);
	        int[] pixels_final = new int[pixels_initial.length];

	        final int pixelIndexOffset = width - matrixWidth;
	        final int centerOffsetX = matrixWidth / 2;
	        final int centerOffsetY = matrix.length / matrixWidth / 2;
	        
	        for (int h = height - matrix.length / matrixWidth + 1, w = width - matrixWidth + 1, y = 0; y < h; y++) {
	            for (int x = 0; x < w; x++) {
	                int r = 0;
	                int g = 0;
	                int b = 0;
	                for (int filterIndex = 0, pixelIndex = y * width + x;filterIndex < matrix.length;pixelIndex += pixelIndexOffset) {
	                    for (int fx = 0; fx < matrixWidth; fx++, pixelIndex++, filterIndex++) {
	                        int col = pixels_initial[pixelIndex];
	                        int factor = matrix[filterIndex];
	                        r += ((col >>> 16) & 0xFF) * factor;
	                        g += ((col >>> 8) & 0xFF) * factor;
	                        b += (col & 0xFF) * factor;
	                    }
	                }
	                r /= sum;
	                g /= sum;
	                b /= sum;
	                pixels_final[x + centerOffsetX + (y + centerOffsetY) * width] = (r << 16) | (g << 8) | b | 0xFF000000;
	            }
	        }
	        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	        result.setRGB(0, 0, width, height, pixels_final, 0, width);
	        return result;
	    }
	 
	 
	public static void main(String[] args) {
				
		try
		{
			//image src
			BufferedImage picture = ImageIO.read(new File("picture2.png"));
		    int[] matrix = {1, 2, 1, 2, 4, 2, 1, 2, 1};
			int matrixWidth = 3;
		    BufferedImage blurred = blur(picture, matrix, matrixWidth);
		    display(blurred);
		}
		catch (IOException e)
		{
		    String workingDir = System.getProperty("user.dir");
		    System.out.println("Current working directory : " + workingDir);
		    e.printStackTrace();
		}
		
		
		
	}

}
