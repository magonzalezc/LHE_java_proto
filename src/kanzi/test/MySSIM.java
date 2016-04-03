/*
Copyright 2011-2013 Frederic Langlet
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
you may obtain a copy of the License at
                http://www.apache.org/licenses/LICENSE-2.0
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package kanzi.test;

import java.util.Arrays;
import java.util.Random;
import javax.imageio.ImageIO;

import LHE.FrameCompressor;
import kanzi.util.ImageQualityMonitor;
import java.io.*;
import java.awt.image.*;//BufferedImage

/*
 * esto solo funciona con png
 */
public class MySSIM
{
	
   public static float getSSIM(String image_orig, String image_final )
   {
	   float fssim=0;
	   
	   // imagen original
	   //-----------------
	   BufferedImage image_aux=null;
	   try{
		   image_aux= ImageIO.read(new File(image_orig));
	   }catch(Exception e){}
	   javax.swing.ImageIcon icon1 = new javax.swing.ImageIcon(image_aux);
	   java.awt.Image image1 = icon1.getImage();
       int w = image1.getWidth(null) & -8;
       int h = image1.getHeight(null) & -8;
       java.awt.GraphicsDevice gs = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0];
       java.awt.GraphicsConfiguration gc = gs.getDefaultConfiguration();
       java.awt.image.BufferedImage img1 = gc.createCompatibleImage(w, h, java.awt.Transparency.OPAQUE);
       java.awt.image.BufferedImage img2 = gc.createCompatibleImage(w, h, java.awt.Transparency.OPAQUE);
       img1.getGraphics().drawImage(image1, 0, 0, null);
       
       //imagen a comparar
       //------------------
      
       BufferedImage image_aux2=null;
	   try{
		   image_aux2= ImageIO.read(new File(image_final));
	   }catch(Exception e){}
	   javax.swing.ImageIcon icon2 = new javax.swing.ImageIcon(image_aux2);
	   java.awt.Image image2 = icon2.getImage();
       img2.getGraphics().drawImage(image2, 0, 0, null);
       
       //ahora ya tenemos las dos imagenes listas para calcular
       //-----------------------------------------------------
       int[] rgb1 = new int[w*h];
       int[] rgb2 = new int[w*h];
       ImageQualityMonitor monitor;
       int psnr, ssim;
      
       // Do NOT use img.getRGB(): it is more than 10 times slower than
       // img.getRaster().getDataElements()
       img1.getRaster().getDataElements(0, 0, w, h, rgb1);
       
       //nuevo
       //img1.getRaster().getDataElements(0, 0, w, h, rgb2);
       img2.getRaster().getDataElements(0, 0, w, h, rgb2);
      
       
       //monitor = new ImageQualityMonitor(w, h);//, kanzi.ColorModelType.RGB);
     
       monitor = new ImageQualityMonitor(w, h, w, 1);//esto significa downsampled2x2. default imageJ puglin lo hace asi
       monitor.grey=true;//fundamental. es una modificacion para que solo considere luminancia
      // psnr = monitor.computePSNR(rgb1, rgb2);
       
       ssim = monitor.computeSSIM(rgb1, rgb2);//,0,0,w/2,h/2);
      
       //printResults("PSNR: ", psnr, "SSIM: ", ssim);

       fssim=ssim/1024f;
       System.out.println("SSIM: "+ ssim+"   ssim:"+fssim);
	   return fssim;
   }
   

   public static void main(String[] args)
   {
	   
	   getSSIM("./img/lena.bmp", "./img/lena0.2.bmp" );
	   
        //String fileName = (args.length > 0) ? args[0] : "c:\\temp\\lena.jpg";
	   args=new String[3];
	   String fileName =new String("./img/lena.png");
	   //args[1]=new String("./img/lena0.2.png");
	   /*
	   FrameCompressor fc=new LHE.FrameCompressor(1);
	   fc.DEBUG=false;
		fc.MODE=new String("ELASTIC");
		fc.loadFrame(fileName);//esto crea la grid
	   */
	   
	   BufferedImage image=null;
	   try{
	   image= ImageIO.read(new File("./img/lena.bmp"));
	   }catch(Exception e){}
	   javax.swing.ImageIcon icon1 = new javax.swing.ImageIcon(image);
	   
        //javax.swing.ImageIcon icon1 = new javax.swing.ImageIcon(fileName);
        java.awt.Image image1 = icon1.getImage();

        int w = image1.getWidth(null) & -8;
        int h = image1.getHeight(null) & -8;
        java.awt.GraphicsDevice gs = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0];
        java.awt.GraphicsConfiguration gc = gs.getDefaultConfiguration();
        java.awt.image.BufferedImage img1 = gc.createCompatibleImage(w, h, java.awt.Transparency.OPAQUE);
        java.awt.image.BufferedImage img2 = gc.createCompatibleImage(w, h, java.awt.Transparency.OPAQUE);

        
        
        img1.getGraphics().drawImage(image1, 0, 0, null);
        
        //nuevo
        javax.swing.ImageIcon icon2 = new javax.swing.ImageIcon("./img/lena0.2.png");
        //javax.swing.ImageIcon icon2 = new javax.swing.ImageIcon("./img/jpeglena0.2.png");
        java.awt.Image image2 = icon2.getImage();
        img2.getGraphics().drawImage(image2, 0, 0, null);
        
        
        
        int[] rgb1 = new int[w*h];
        int[] rgb2 = new int[w*h];
        ImageQualityMonitor monitor;
        int psnr, ssim;
        Random rnd = new Random();

        // Do NOT use img.getRGB(): it is more than 10 times slower than
        // img.getRaster().getDataElements()
        img1.getRaster().getDataElements(0, 0, w, h, rgb1);
        
        //nuevo
        //img1.getRaster().getDataElements(0, 0, w, h, rgb2);
        img2.getRaster().getDataElements(0, 0, w, h, rgb2);

        {
           img2.getRaster().setDataElements(0, 0, w, h, rgb2);
           System.out.println("\nSame images");
           monitor = new ImageQualityMonitor(w, h);
           //downsampled 2x2
           //filter width 11
           monitor = new ImageQualityMonitor(w, h, w, 1);
           psnr = monitor.computePSNR(rgb1, rgb2);
           ssim = monitor.computeSSIM(rgb1, rgb2);
           printResults("PSNR: ", psnr, "SSIM: ", ssim);
        }


        {
           for (int i=0; i<((w*h+500)/1000); i++)
              rgb2[Math.abs(rnd.nextInt())/(w*h)] = rnd.nextInt();

           img2.getRaster().setDataElements(0, 0, w, h, rgb2);
           System.out.println("\nRandom noise (0.1% samples)");
           monitor = new ImageQualityMonitor(w, h, w);
           psnr = monitor.computePSNR(rgb1, rgb2);
           ssim = monitor.computeSSIM(rgb1, rgb2);
           printResults("PSNR: ", psnr, "SSIM: ", ssim);
           monitor = new ImageQualityMonitor(w, h, w, 1);
           psnr = monitor.computePSNR(rgb1, rgb2);
           ssim = monitor.computeSSIM(rgb1, rgb2);
           printResults("PSNR (subsampled by 2x2): ", psnr, "SSIM (subsampled by 2x2): ", ssim);
           monitor = new ImageQualityMonitor(w, h, w, 2);
           psnr = monitor.computePSNR(rgb1, rgb2);
           ssim = monitor.computeSSIM(rgb1, rgb2);
           printResults("PSNR (subsampled by 4x4): ", psnr, "SSIM (subsampled by 4x4): ", ssim);
        }


        {
           for (int i=0; i<((w*h+50)/100); i++)
              rgb2[Math.abs(rnd.nextInt())/(w*h)] = rnd.nextInt();

           img2.getRaster().setDataElements(0, 0, w, h, rgb2);
           System.out.println("\nRandom noise (1% samples)");
           monitor = new ImageQualityMonitor(w, h, w);
           psnr = monitor.computePSNR(rgb1, rgb2);
           ssim = monitor.computeSSIM(rgb1, rgb2);
           printResults("PSNR: ", psnr, "SSIM: ", ssim);
           monitor = new ImageQualityMonitor(w, h, w, 1);
           psnr = monitor.computePSNR(rgb1, rgb2);
           ssim = monitor.computeSSIM(rgb1, rgb2);
           printResults("PSNR (subsampled by 2x2): ", psnr, "SSIM (subsampled by 2x2): ", ssim);
           monitor = new ImageQualityMonitor(w, h, w, 2);
           psnr = monitor.computePSNR(rgb1, rgb2);
           ssim = monitor.computeSSIM(rgb1, rgb2);
           printResults("PSNR (subsampled by 4x4): ", psnr, "SSIM (subsampled by 4x4): ", ssim);
        }


        {
           for (int i=0; i<((w*h+5)/10); i++)
              rgb2[Math.abs(rnd.nextInt())/(w*h)] = rnd.nextInt();

           img2.getRaster().setDataElements(0, 0, w, h, rgb2);
           System.out.println("\nRandom noise (10% samples)");
           monitor = new ImageQualityMonitor(w, h, w);
           psnr = monitor.computePSNR(rgb1, rgb2);
           ssim = monitor.computeSSIM(rgb1, rgb2);
           printResults("PSNR: ", psnr, "SSIM: ", ssim);
           monitor = new ImageQualityMonitor(w, h, w, 1);
           psnr = monitor.computePSNR(rgb1, rgb2);
           ssim = monitor.computeSSIM(rgb1, rgb2);
           printResults("PSNR (subsampled by 2x2): ", psnr, "SSIM (subsampled by 2x2): ", ssim);
           monitor = new ImageQualityMonitor(w, h, w, 2);
           psnr = monitor.computePSNR(rgb1, rgb2);
           ssim = monitor.computeSSIM(rgb1, rgb2);
           printResults("PSNR (subsampled by 4x4): ", psnr, "SSIM (subsampled by 4x4): ", ssim);
        }


        {
           Arrays.fill(rgb2, 0);
           System.arraycopy(rgb1, 0, rgb2, 0, w*h/2);
           img2.getRaster().setDataElements(0, 0, w, h, rgb2);
           System.out.println("\nSecond image: half empty + half initial image");
           monitor = new ImageQualityMonitor(w, h, w);
           psnr = monitor.computePSNR(rgb1, rgb2);
           ssim = monitor.computeSSIM(rgb1, rgb2);
           printResults("PSNR: ", psnr, "SSIM: ", ssim);
           monitor = new ImageQualityMonitor(w, h, w, 1);
           psnr = monitor.computePSNR(rgb1, rgb2);
           ssim = monitor.computeSSIM(rgb1, rgb2);
           printResults("PSNR (subsampled by 2x2): ", psnr, "SSIM (subsampled by 2x2): ", ssim);
           monitor = new ImageQualityMonitor(w, h, w, 2);
           psnr = monitor.computePSNR(rgb1, rgb2);
           ssim = monitor.computeSSIM(rgb1, rgb2);
           printResults("PSNR (subsampled by 4x4): ", psnr, "SSIM (subsampled by 4x4): ", ssim);
       }

       {
           Arrays.fill(rgb2, 0);
           img2.getRaster().setDataElements(0, 0, w, h, rgb2);
           System.out.println("\nSecond image: empty");
           monitor = new ImageQualityMonitor(w, h, w);
           psnr = monitor.computePSNR(rgb1, rgb2);
           ssim = monitor.computeSSIM(rgb1, rgb2);
           printResults("PSNR: ", psnr, "SSIM: ", ssim);
           monitor = new ImageQualityMonitor(w, h, w, 1);
           psnr = monitor.computePSNR(rgb1, rgb2);
           ssim = monitor.computeSSIM(rgb1, rgb2);
           printResults("PSNR (subsampled by 2x2): ", psnr, "SSIM (subsampled by 2x2): ", ssim);
           monitor = new ImageQualityMonitor(w, h, w, 2);
           psnr = monitor.computePSNR(rgb1, rgb2);
           ssim = monitor.computeSSIM(rgb1, rgb2);
           printResults("PSNR (subsampled by 4x4): ", psnr, "SSIM (subsampled by 4x4): ", ssim);
       }

//        javax.swing.JFrame frame = new javax.swing.JFrame("Image1");
//        frame.setBounds(50, 30, w, h);
//        frame.add(new javax.swing.JLabel(icon1));
//        frame.setVisible(true);
//        javax.swing.JFrame frame2 = new javax.swing.JFrame("Image2");
//        frame2.setBounds(600, 30, w, h);
//        frame2.add(new javax.swing.JLabel(icon2));
//        frame2.setVisible(true);

//        try
//        {
//            Thread.sleep(35000);
//        }
//        catch (Exception e)
//        {
//        }

        System.exit(0);
   }


   private static void printResults(String titlePSNR, int psnr, String titleSSIM, int ssim)
   {
      if (psnr != 0)
         System.out.println(titlePSNR+(float) psnr/1024);
      else
         System.out.println(titlePSNR+"Infinite");

      System.out.println(titleSSIM+(float) ssim/1024);
   }
}