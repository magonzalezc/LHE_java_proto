import LHE.*;
import qmetrics.*;

import java.util.Date;


public class Test {

double max_psnr=0;
double max_percent=1;
float [] tabla=new float[101];

String MODE;
	public static void main(String [ ] args)
	{
		/**
		 * given an original image, this main program example, scales and rescales
		 * and saves into BMP 
		 * additionally it shows the PSNR metric for evaluation purposes.
		 */
		
		Test t=new Test();
		t.cargaTabla();
		//t.prueba1();
		
		
		//t.downInterpolHomo(  "./img/lena.bmp", 29.0f);
		//t.downInterpolHomo(  "./img/lena.bmp", 15f);
		//t.downInterpolHomo(  "./img/diag32.bmp",1f);
		
		t.prueba2();
		//t.testVelocidadLHE();
	/*
		while (true)
		{
			t.prueba2();
		}
		*/
		
		
		
		
		
		
		
		

			
	}
	//*************************************************************************************
	
//*************************************************************************************
		public void prueba2()
		{
			
			//String file=new String("./img/Mexbn2bn512.bmp");
		//	String file=new String("./img/kodim04bn.bmp");
		//	String file=new String("./img/kodim21bn.bmp");
		//	String file=new String("./img/kodim20bn.bmp");
		//	String file=new String("./img/maxv.bmp");
		//	String file=new String("./img/lena.bmp");
			//String file=new String("./img/white.bmp");
		//	String file=new String("./img/diag32.bmp");
			String file=new String("./img/Animals_3.bmp");
			//String file=new String("./img/Cartoons_8.bmp");
			String path_img=new String(file);
			//String path_img=new String("./img/lena.bmp");
		//	path_img=new String("./img/lenaHD.bmp");
		//	path_img=new String("./img/maxv.bmp");
			//
		//	path_img=new String("./img/Animals_3.bmp");
			//path_img=new String("./img/Cartoons_8.bmp");
		//	path_img=new String("./img/kodim21.bmp");
			//path_img=new String("./img/whitepsnr.bmp");
			//path_img=new String("./img/chess32.bmp");
			//path_img=new String("./img/chess16.bmp");
		//	path_img=new String("./img/boat.bmp");
			//path_img=new String("./img/baboon.bmp");
			//path_img=new String("./img/ruler.512.bmp");
			final ImgUtil img=new ImgUtil();
			//img.BMPtoYUV("./img/b64.bmp");
			//img.BMPtoYUV("./img/chess.bmp");
			//img.BMPtoYUV("./img/lena.bmp");
			//img.BMPtoYUV("./img/white2.bmp");
			/*for (int x=0;x<img.width;x++)
			{
				for (int y=0;y<img.height;y++)
				{
					if (img.YUV[0][y*img.width+x]==0)
					{
						img.YUV[0][y*img.width+x]=1;
					}
				}	
			}
			img.YUVtoBMP("./img/lenayuv.bmp",img.YUV[0]);
			*/
			img.BMPtoYUV(path_img);
			
			/*
			img.BMPtoYUV("./img/encoder_kodim20bn.bmp");
			//img.BMPtoYUV("./img/downsampled_lena.bmp");
			System.out.println(" el porcent es:"+img.getNumberOfNonZeroPixels());
			if (1<2) System.exit(0);
			*/
			/*
			float p=img.getNumberOfNonZeroPixels();
			System.out.println("p:"+p);
			if (2>1) System.exit(0);
			*/
			//img.YUVtoBMP("./img/kodim21bn.bmp",img.YUV[0]);
			//img.BMPtoYUV("./img/lena.bmp");
			//img.BMPtoYUV("./img/maxv.bmp");
		//	img.BMPtoYUV("./img/ajuste.bmp");
			
			//img.BMPtoYUV("./img/boat.bmp");
		//	img.BMPtoYUV("./img/baboon.bmp");
		//	img.BMPtoYUV("./img/ruler.512.bmp");
		//	img.BMPtoYUV("./img/Mexbn2bn512.bmp");
		//img.BMPtoYUV("./img/kodim21.bmp");
			
		//	img.BMPtoYUV("./img/lenaHD.bmp");
		//	img.BMPtoYUV("./img/1920x1080image.jpg");
		//	img.BMPtoYUV("./img/Animals_3.bmp");
			//img.BMPtoYUV("./img/Cartoons_8.bmp");
			
			//img.BMPtoYUV("./img/whitepsnr.bmp");
			//img.BMPtoYUV("./img/white2.bmp");
			//img.BMPtoYUV("./img/chess64"+".bmp");
			//img.BMPtoYUV("./img/white.bmp");
			//img.BMPtoYUV("./img/whiteborder.bmp");
			//img.BMPtoYUV("./img/white2.bmp");
			//img.BMPtoYUV("./img/whiteborder2.bmp");
			//
			//img.BMPtoYUV("./img/chess16.bmp");
			//img.BMPtoYUV("./img/chess32.bmp");
			//img.BMPtoYUV("./img/bars.bmp");
		//	img.BMPtoYUV("./img/kodim04.bmp");
			
			float per=30;
			float datos=getPSNR(path_img,per);
			//if (1<2) System.exit(0);
			//MODE=new String("HOMO");
			
			
			getPSNRtable(path_img);
			
			
			/*
			float tabla[]=new float[101];
			for (float f=0;f<3;f+=0.001)
				//for (float f=10;f>=0;f-=0.001)
			{
				System.out.println("----------------------"+f);
				float [] dato=
				getPercent_and_PSNR(  path_img, f);
				
				if (tabla[(int)(dato[0])]==0) tabla[(int)(dato[0])]=f;//dato[1];
				if ((int)(dato[0])==100) break;
			}
			for (int i=0;i<=100;i++)
			{
				System.out.println("tabla["+i+"]="+tabla[i]+"f;");
			}
			*/
			//getPercent_and_PSNR(  path_img, 0.115701385f);
			//cargaTabla();
			getPSNR(path_img,40f);
			//System.out.println(" para un "+per+" tenemos "+datos +"dB");
			//System.out.println(" fin");
			if (1<2) System.exit(0);
			
			Grid grid;
			float  cf=(float)Math.random()*10f;
			
			cf=2.25f;//70% 41
			cf=2.7f;//51% 35,82		
			cf=3.25f;//35% 33,2
			cf=4.65f;//17 30,7
			
			cf=4.23f;
			cf=3f;
			cf=2.48f;
			cf=2.45f;
			cf=2.5f;
			
			cf=7f;
			cf=22f;
			
			cf=34.35f;
			cf=2.5f;
			cf=44f;
			cf=3.3f;
			cf=28f;
			cf=25f;
			cf=16f; // 16.7 usando 2max/1+pr sale 28.2
			
			//cf=25f; // 16.7 usando 2max/0.5+1.5pr sale 28.5
			//cf=32f; // 16.7 usando 2max/0.25+1.75pr sale 28.5
			//cf=4.4f;//16.6 usando q anterior. sale 28.7<---- la mejor
			//cf=20f; // 16.7 usando 2max/0.75+1.25pr sale 28.3
			
			cf=40f;
			cf=16f;
			cf=4f;
			cf=3f;
			cf=2.2f;
			
			cf=9.9f;//26.1% 30db LO QUE TENGO NORMAL
			
			cf=25f;//26%  30.34
			cf=0.34f;
			
			cf=0.8f; // 7f 26.1%  29.6db
			cf=1f;// 6f 26.1%   29.7db
			cf=1.30f; //5f 25.9%  29.87db
			
			cf=1.75f; //4f  25.94 30db
			
			cf=2.48f; //3f  26.2  30db
			
			cf=3.75f;// 2f 26% 30db
			
			cf=6.2f;// 1f 26%  29.6db
			
			cf=2.05f;//3.5f  26% 30.13db
			
			cf=.51f;// 7 y cuantizacion a la mitad --> 30.06db
			
			cf=0.45f;// 7 sin cuantizar. sale igual, 30db
			
			cf=1.05f;
			
			cf=0.1f;
			//cf=15f;
			//cf=15f;
			//cf=90.21f;
			
			//cf=5.3f;//7.2f;//6;//7.2f;//7.25f;//6.5f;//.3f;//6.7f;//7.25f;//5.5f;//factor 2
			//cf=100f;
			//cf=5.8f;//factor 3
			
			//float cf2=(float)Math.pow(2,cf)/100f;
			//grid= new Grid((float)Math.random()*10);//10);
			
			grid = new Grid();//cf);
			
			grid.createGrid(img.width, img.height,cf,Block.MAX_PPP);
			System.out.println("COMP FACTOR:"+grid.compression_factor);
			img.grid=grid;
			
			//----------PR metrics----------------------
            LHEquantizer lhe=new LHEquantizer();
			lhe.img=img;
			lhe.init();
			//lhe.init();
			
			lhe.quantizeOneHopPerPixel(img.hops[0],img.LHE_YUV[0]);
			//lhe.quantizeOneHopPerPixel2(img.hops[0],img.LHE_YUV[0]);
			img.saveHopsToTxt("./img/hops.txt");
			img.YUVtoBMP("./img/LHE_YUV.bmp",img.LHE_YUV[0]);
			PRblock.img=img;
			//img.grid.computeMetrics();
			//img.grid.computeMetricsV3();
			//if (1<2)System.exit(0);
			img.grid.computeMetrics();
			//if (1<2)System.exit(0);
			img.grid.printPRstats("./img/PRstats.txt");
			
			img.grid.fromPRtoPPP(grid.compression_factor);
			//--------------------------------------------------
			//img.grid.setBpp();

			//ElasticScaling es=new ElasticScaling();
			//es.img=img;
            Block.img=img;
            
			Date date= new Date();
			//System.out.println(date.getTime());

			//--------------------------downsampling-------------------------------------
			System.out.println(" blocksH:"+img.grid.number_of_blocks_H);
			System.out.println(" blocksV:"+img.grid.number_of_blocks_V);
			for (int k=0;k<1;k++)
			{	

				//downsampling is parallelizable all blocks at the same time

				for ( int y=0 ; y<img.grid.number_of_blocks_V;y++)
				{
					for ( int x=0 ; x<img.grid.number_of_blocks_H;x++)

					{

						Block bi=img.grid.bl[y][x];
	                   
	                   
	                    //bi.pppToRectangleShape();
	                    bi.pppToRectangleShape();
	                    //bi.computeDownsampledLengths();
	                    //bi.tunePPP();
	                    bi.downsampleBlock(true);
	                    //if (1<2) break;
	                   // bi.interpolateBoundaries(img.boundaries_YUV,img.downsampled_YUV);
					//	bi.downsampleBoundaries(true,img.boundaries_YUV,img.boundaries_YUV);

					}
					//if (1<2) break;
				}

			}
			Date date2= new Date();
			float tiempo=(date2.getTime()-date.getTime());
			System.out.println("total:"+tiempo+" ms   per downsampling:"+tiempo/10000+"  ms");
			
			img.YUVtoBMP("./img/intermediate_downsampled_lena.bmp",img.intermediate_downsampled_YUV[0]);
			img.YUVtoBMP("./img/downsampled_lena.bmp",img.downsampled_YUV[0]);
			
			System.out.println("    EY: "+PSNR.printPSNR(path_img,"./img/downsampled_lena.bmp"));

			//--------------------------interpolation-------------------------------------
			System.out.println(" interpolando...");
			date= new Date();
			System.out.println(date.getTime());
			boolean bilineal=true;
			for (int k=0;k<1;k++)
			{	


				for ( int y=0 ; y<img.grid.number_of_blocks_V;y++)
				{
					for ( int x=0 ; x<img.grid.number_of_blocks_H;x++)

					{
						Block bi=img.grid.bl[y][x];
						
						//bi.computeDownsampledLengths();
						//bi.tunePPP();
						
						//bi.interpolateVfloat4(bilineal,img.downsampled_YUV,img.intermediate_interpolated_YUV);
						bi.interpolateBilinealV(img.downsampled_YUV,img.intermediate_interpolated_YUV);
						
					}
				}
				
				img.YUVtoBMP("./img/interV.bmp",img.intermediate_interpolated_YUV[0]);
				
				for ( int y=0 ; y<img.grid.number_of_blocks_V;y++)
				{
					for ( int x=0 ; x<img.grid.number_of_blocks_H;x++)

					{
						Block bi=img.grid.bl[y][x];
						//bi.computeDownsampledLengths();// YA HECHO
						//bi.tunePPP(); YA HECHO
						//bi.interpolateBlock6(bilineal);
						//bi.interpolateV(bilineal,img.downsampled_YUV,img.intermediate_interpolated_YUV);
						//bi.interpolateHfloat4(bilineal,img.intermediate_interpolated_YUV,img.interpolated_YUV);
						bi.interpolateBilinealH(img.intermediate_interpolated_YUV,img.interpolated_YUV);
					}
				}
				//System.out.println(" hecho");
				img.YUVtoBMP("./img/interH.bmp",img.interpolated_YUV[0]);
				//--------------------interpolate gaps-------------------------------------
				if (bilineal)
				{
					for ( int y=0 ; y<img.grid.number_of_blocks_V;y++)
					{
						for ( int x=0 ; x<img.grid.number_of_blocks_H;x++)

						{
							Block bi=img.grid.bl[y][x];
							Block b_left=null;
							if (x>0) b_left=img.grid.bl[y][x-1];
							bi.interpolateGapH(b_left);

						}

					}
					//System.out.println(" hecho gaph");
				    img.YUVtoBMP("./img/gaph.bmp",img.interpolated_YUV[0]);
					//----

					for ( int y=0 ; y<img.grid.number_of_blocks_V;y++)
					{
						for ( int x=0 ; x<img.grid.number_of_blocks_H;x++)

						{
							Block bi=img.grid.bl[y][x];
							Block b_up=null;
							if (y>0) b_up=img.grid.bl[y-1][x];
							bi.interpolateGapV(b_up);
							
							bi.interpolateBoundaries(img.boundaries_YUV,img.downsampled_YUV);
							bi.downsampleBoundaries(bilineal,img.boundaries_YUV,img.boundaries_YUV);
						}

					}

					//---
				}

			}
			//System.out.println(" hecho2");
			date2= new Date();
			tiempo=(date2.getTime()-date.getTime());
			System.out.println("total:"+tiempo+" ms   per interpolacion:"+tiempo/10000+"  ms");
			//img.interpolated_YUV[0]=img.interpolated_Y;

			//es.interpolateBlock2(null,true);
			// img.YUVtoBMP("./img/vert_interpolated.bmp",img.vertical_interpolated_YUV[0]);
			
			//img.YUVtoBMP("./img/vert_interpolated.bmp",img.intermediate_interpolated_YUV[0]);
			img.YUVtoBMP("./img/boundaries.bmp",img.boundaries_YUV[0]);
			img.YUVtoBMP("./img/interpolated.bmp",img.interpolated_YUV[0]);
			//img.interpolatedYUVtoBMP("./img/interpolated_lena.bmp");

			float percent=grid.getNumberOfDownsampledPixels();
			percent=100f*percent/(img.width*img.height);
		
			double psnr=PSNR.printPSNR(path_img, "./img/interpolated.bmp");
			//psnr=PSNR.printPSNR(path_img, "./img/interV.bmp");
			//psnr=PSNR.printPSNR(path_img, "./img/player_kodim20bn.bmp");
			
			//psnr=PSNR.printPSNR(path_img,"./img/downsampled_lena.bmp");
			/*float ratio=(float)(psnr*20/(percent));
		float max_ratio=(float)(max_psnr*20/(max_percent));
		//	if (ratio>max_ratio && percent<10 ) 
		if (psnr>max_psnr && percent<10 )
				{max_psnr=psnr;
				max_percent=percent;
				max_ratio=ratio;
				img.YUVtoBMP("./img/downsampled_lena15.bmp",img.downsampled_YUV[0]);
				img.YUVtoBMP("./img/interpolated15.bmp",img.interpolated_YUV[0]);
				}
				*/
	         System.out.println("percent="+percent+" PSNR: "+psnr);//+ "           MAX PSNR:"+max_psnr+"      percent:"+max_percent+"    ratio:"+max_ratio);
			System.out.println("fin");
			try{Thread.sleep(1500);
			}catch(Exception e){}
			
			//if(1<2) System.exit(0);
			float ratio_homo=100f/(percent);
			
			System.out.println(" ratio homogeneo equivalente:"+ratio_homo);
			/*
			psnr=PSNR.printPSNR(path_img, "./img/lenayuv.bmp");
			
			System.out.println("PSNR yuv: "+psnr);
			psnr=PSNR.printPSNR(path_img, "./img/downsampled_lena.bmp");
			System.out.println("PSNR down: "+psnr);
			psnr=PSNR.printPSNR(path_img, "./img/gaps.bmp");
			System.out.println("PSNR gaps: "+psnr);
		*/
			/*
		for (int x=0;x<img.width;x++)
		{
			for (int y=0;y<img.height;y++)
			{
				if (img.interpolated_YUV[0][y*img.width+x]!=img.YUV[0][y*img.width+x])
				{
					img.interpolated_YUV[0][y*img.width+x]=255;
				//	System.exit(0);
				}
			}	
		}
		
		
		img.YUVtoBMP("./img/interpolated2.bmp",img.interpolated_YUV[0]);
		*/
			System.out.println("comp_factor="+grid.compression_factor+" blocks:"+grid.blocks_of_larger_image_side);
		
			
			//PRUEBA DE VELOCIDAD
			testVelocidadDownsampling(ratio_homo, file,cf);
			
			//testVelocidadLHE();
		
	}
		
		//*****************************************************
		public void testVelocidadLHE()
		{
			System.out.println(""); 
			System.out.println("************ LHE COMPUTING TIME ********************");
			String path_img=new String("./img/lena.bmp");
		    //path_img=new String("./img/baboon.bmp");
			final ImgUtil img=new ImgUtil();
			img.BMPtoYUV("./img/lena.bmp");
			//img.BMPtoYUV("./img/baboon.bmp");
			
			Grid grid;
			float  cf=0f;// numero de bloques es maximo con 0
			grid = new Grid();//cf);
			
			grid.createGrid(img.width, img.height,cf,Block.MAX_PPP);
			//System.out.println("COMP FACTOR:"+" cf:"+cf+"--2pow -->"+grid.compression_factor);
			img.grid=grid;
			
             LHEquantizer lhe2=new LHEquantizer();
			
			lhe2.img=img;
			lhe2.init();
			//lhe2.init();
		    Date d1=new Date();
		    int bucles=1;
		    System.out.println("executing "+ bucles+" times...please wait");
			for (int i=0;i<bucles;i++){
				lhe2.quantizeOneHopPerPixel(img.hops[0],img.LHE_YUV[0]);
				//lhe2.quantizeOneHopPerPixel2(img.hops[0],img.LHE_YUV[0]);
			}
			Date d2= new Date();
			float t=(d2.getTime()-d1.getTime());
			System.out.println("total time  per LHE:"+t/(float)bucles+"  ms");
			img.YUVtoBMP("./img/LHE_YUV.bmp",img.LHE_YUV[0]);
			double psnr=PSNR.printPSNR(path_img, "./img/LHE_YUV.bmp");
			System.out.println(" psnr lhe: "+psnr);
			System.out.println("************ END LHE COMPUTING TIME ********************");
		}
		
		
		//***********************************************************************
		public void testVelocidadDownsampling(float ratio_homo, String file, float cfinput)
		{
			System.out.println(""); 
			System.out.println("************ DOWNSAMPLING COMPUTING TIME ********************");
		
			String path_img=new String(file);//"./img/lena.bmp");
		//	path_img=new String("./img/Animals_3.bmp");
			//path_img=new String("./img/Cartoons_8.bmp");
		//	path_img=new String("./img/kodim20.bmp");
			final ImgUtil img=new ImgUtil();
			img.BMPtoYUV(file);//"./img/lena.bmp");
			//img.BMPtoYUV("./img/Animals_3.bmp");
			//img.BMPtoYUV("./img/Cartoons_8.bmp");
			//img.BMPtoYUV("./img/kodim20.bmp");
			Grid grid = new Grid();//cf);
			float  cf=cfinput;//0f;// numero de bloques es maximo con 0
			grid.createGrid(img.width, img.height,cf,Block.MAX_PPP);
			//System.out.println("COMP FACTOR:"+" cf:"+cf+"--2pow -->"+grid.compression_factor);
			img.grid=grid;
			float ratio=ratio_homo;
			System.out.println(" RATIO  1:"+ratio);
			grid.setPPPHomogeneousDownsamplingRatio(ratio);
			Block.img=img;
			System.out.println(" blocksH:"+img.grid.number_of_blocks_H);
			System.out.println(" blocksV:"+img.grid.number_of_blocks_V);
			Date date= new Date();
			for (int k=0;k<1;k++)
			{	

				//downsampling is parallelizable all blocks at the same time
				for ( int y=0 ; y<img.grid.number_of_blocks_V;y++)
				{
					for ( int x=0 ; x<img.grid.number_of_blocks_H;x++)

					{

						Block bi=img.grid.bl[y][x];
	                    bi.pppToRectangleShape();
	                    //bi.computeDownsampledLengths();
	                    //bi.tunePPP();
	                    bi.downsampleBlock(true);
	                    bi.interpolateBoundaries(img.boundaries_YUV,img.downsampled_YUV);
						bi.downsampleBoundaries(true,img.boundaries_YUV,img.boundaries_YUV);

					}
				}

			}//endfor k
			Date date2= new Date();
			float tiempo=(date2.getTime()-date.getTime());
			System.out.println("total:"+tiempo+" ms   per downsampling:"+tiempo/1000+"  ms");
			
			//img.YUVtoBMP("./img/intermediate_downsampled_lena.bmp",img.intermediate_downsampled_YUV[0]);
			img.YUVtoBMP("./img/downsampled_homo.bmp",img.downsampled_YUV[0]);
           
			//----------------------------------------------------------------------------
			System.out.println(" interpolando...");
			date= new Date();
			System.out.println("timestamp:"+date.getTime());
			boolean bilineal=true;
			for (int k=0;k<1;k++)
			{	


				for ( int y=0 ; y<img.grid.number_of_blocks_V;y++)
				{
					for ( int x=0 ; x<img.grid.number_of_blocks_H;x++)

					{
						Block bi=img.grid.bl[y][x];
						//bi.computeDownsampledLengths();
						//bi.tunePPP();
						//bi.interpolateBlock6(bilineal);
					//	bi.interpolateV(bilineal,img.downsampled_YUV,img.intermediate_interpolated_YUV);
						bi.interpolateBilinealV(img.downsampled_YUV,img.intermediate_interpolated_YUV);
						//bi.interpolateH(bilineal,img.intermediate_interpolated_YUV,img.interpolated_YUV);
					}
				}
				
				//img.YUVtoBMP("./img/interV.bmp",img.intermediate_interpolated_YUV[0]);
				
				for ( int y=0 ; y<img.grid.number_of_blocks_V;y++)
				{
					for ( int x=0 ; x<img.grid.number_of_blocks_H;x++)

					{
						Block bi=img.grid.bl[y][x];
						//bi.computeDownsampledLengths();// YA HECHO
						//bi.tunePPP(); YA HECHO
						//bi.interpolateBlock6(bilineal);
						//bi.interpolateV(bilineal,img.downsampled_YUV,img.intermediate_interpolated_YUV);
						//bi.interpolateH(bilineal,img.intermediate_interpolated_YUV,img.interpolated_YUV);
						bi.interpolateBilinealH(img.intermediate_interpolated_YUV,img.interpolated_YUV);
					}
				}
				//System.out.println(" hecho");
				//img.YUVtoBMP("./img/interH_homo.bmp",img.interpolated_YUV[0]);
				//--------------------interpolate gaps-------------------------------------
				if (bilineal)
				{
					for ( int y=0 ; y<img.grid.number_of_blocks_V;y++)
					{
						for ( int x=0 ; x<img.grid.number_of_blocks_H;x++)

						{
							Block bi=img.grid.bl[y][x];
							Block b_left=null;
							if (x>0) b_left=img.grid.bl[y][x-1];
							bi.interpolateGapH(b_left);

						}

					}
					//System.out.println(" hecho gaph");
				   // img.YUVtoBMP("./img/gaph_homo.bmp",img.interpolated_YUV[0]);
					//----

					for ( int y=0 ; y<img.grid.number_of_blocks_V;y++)
					{
						for ( int x=0 ; x<img.grid.number_of_blocks_H;x++)

						{
							Block bi=img.grid.bl[y][x];
							Block b_up=null;
							if (y>0) b_up=img.grid.bl[y-1][x];
							bi.interpolateGapV(b_up);
							
							bi.interpolateBoundaries(img.boundaries_YUV,img.downsampled_YUV);
							bi.downsampleBoundaries(bilineal,img.boundaries_YUV,img.boundaries_YUV);
						}

					}

					//---
				}

			}
			//System.out.println(" hecho2");
			date2= new Date();
			tiempo=(date2.getTime()-date.getTime());
			System.out.println("total:"+tiempo+" ms   per interpolacion:"+tiempo/1000+"  ms");
			//img.interpolated_YUV[0]=img.interpolated_Y;

			//es.interpolateBlock2(null,true);
			// img.YUVtoBMP("./img/vert_interpolated.bmp",img.vertical_interpolated_YUV[0]);
			
			//img.YUVtoBMP("./img/vert_interpolated.bmp",img.intermediate_interpolated_YUV[0]);
			//img.YUVtoBMP("./img/boundaries.bmp",img.boundaries_YUV[0]);
			img.YUVtoBMP("./img/interpolated_homo.bmp",img.interpolated_YUV[0]);
			
			
			float percent=grid.getNumberOfDownsampledPixels();
			percent=100f*percent/(img.width*img.height);
			double psnr=PSNR.printPSNR(path_img, "./img/interpolated_homo.bmp");
			  System.out.println("HOMOGENEOUS DOWN percent="+percent+" PSNR: "+psnr);
			
	
		}// end function
	//***************************************************************************
		public void TestElasticVersusHomo()
		{
			/*
			float percent;
			
			String directory;
			
			for (each picture)
			{
			
			
			float[] psnr_elastic=new float[100];// from 0 to 9.9 step 0.1
			float[] psnr_homo=new float[100];// from 0 to 9.9 step 0.1
			float cf=0;// initial compression factor
			while (cf<10)
			{
			int index=cf*10;//from 0 to 99
			
			//compute % and psnr of elastic
			
			//check if any milestone can be interpolated
			
			//compute % and psnr of homo
			
			//check if any milestone can be interpolated
			
			
			
			
			cf+=0.1;
			}
			
			//save line
			
			//next image
		}
		*/
		}//end function
		
		//*******************************************************************************************
	//	testVelocidadDownsampling( 31.640625 %, file,cf);
		
		public void downInterpolHomo(String file, float cf)
		{
			System.out.println(""); 
			System.out.println("************ downInterpolHomo ********************");
		
			String path_img=new String(file);//"./img/lena.bmp");
			final ImgUtil img=new ImgUtil();
			img.BMPtoYUV(file);//"./img/lena.bmp");
			Grid grid = new Grid();//cf);
			
			grid.createGrid(img.width, img.height,cf,Block.MAX_PPP);
			img.grid=grid;
			float percent2=cf;//ratio_homo;
			float ratio_homo=100f/(percent2);
			//System.out.println(" RATIO  1:"+ratio);
			grid.setPPPHomogeneousDownsamplingRatio(ratio_homo);
			Block.img=img;
			System.out.println(" blocksH:"+img.grid.number_of_blocks_H);
			System.out.println(" blocksV:"+img.grid.number_of_blocks_V);
			
			System.out.println("      downsampling homogeneo...");
				

				//downsampling is parallelizable all blocks at the same time
				for ( int y=0 ; y<img.grid.number_of_blocks_V;y++)
				{
					for ( int x=0 ; x<img.grid.number_of_blocks_H;x++)

					{

						Block bi=img.grid.bl[y][x];
						
	                    //bi.pppToRectangleShape();
						bi.pppToRectangleShape();
	                    bi.computeDownsampledLengths();
	                   // System.out.println("-------- Boque:"+y+","+x);
	                    
	                   // bi.tunePPP(); YA NO EXISTE
	                    
	                    bi.downsampleBlock(true);
	                    bi.interpolateBoundaries(img.boundaries_YUV,img.downsampled_YUV);
						bi.downsampleBoundaries(true,img.boundaries_YUV,img.boundaries_YUV);

					}
				}

			
			img.YUVtoBMP("./img/downsampled_homo.bmp",img.downsampled_YUV[0]);
           
			//----------------------------------------------------------------------------
			System.out.println("   interpolando...");
			//grid.setHomogeneousDonwnsamplingRatio(ratio_homo);
			boolean bilineal=true;
			
				for ( int y=0 ; y<img.grid.number_of_blocks_V;y++)
				{
					for ( int x=0 ; x<img.grid.number_of_blocks_H;x++)

					{
						Block bi=img.grid.bl[y][x];
						bi.computeDownsampledLengths();
					
						
						//bi.tunePPP(); //en una real habria que invocar
						
						
						//bi.interpolateBlock6(bilineal);
						//bi.interpolateVfloat(bilineal,img.downsampled_YUV,img.intermediate_interpolated_YUV);
						
						//correccion
						bi.interpolateBilinealV(img.downsampled_YUV,img.intermediate_interpolated_YUV);
						//bi.interpolateH(bilineal,img.intermediate_interpolated_YUV,img.interpolated_YUV);
					}
				
				}
				img.YUVtoBMP("./img/interV_homo.bmp",img.intermediate_interpolated_YUV[0]);
				for ( int y=0 ; y<img.grid.number_of_blocks_V;y++)
				{
					for ( int x=0 ; x<img.grid.number_of_blocks_H;x++)

					{
						Block bi=img.grid.bl[y][x];
						//bi.computeDownsampledLengths();// YA HECHO
						//bi.tunePPP(); YA HECHO
						//bi.interpolateBlock6(bilineal);
						//bi.interpolateV(bilineal,img.downsampled_YUV,img.intermediate_interpolated_YUV);
						bi.interpolateBilinealH(img.intermediate_interpolated_YUV,img.interpolated_YUV);
					}
				}
				//System.out.println(" hecho");
				img.YUVtoBMP("./img/interH_homo.bmp",img.interpolated_YUV[0]);
				//--------------------interpolate gaps-------------------------------------
				if (bilineal)
				{
					for (int  y=0 ; y<img.grid.number_of_blocks_V;y++)
					{
						for ( int x=0 ; x<img.grid.number_of_blocks_H;x++)

						{
							Block bi=img.grid.bl[y][x];
							Block b_left=null;
							if (x>0) b_left=img.grid.bl[y][x-1];
							bi.interpolateGapH(b_left);

						}

					}
					//System.out.println(" hecho gaph");
				    img.YUVtoBMP("./img/gaph_homo.bmp",img.interpolated_YUV[0]);
					//----

					for ( int y=0 ; y<img.grid.number_of_blocks_V;y++)
					{
						for ( int x=0 ; x<img.grid.number_of_blocks_H;x++)

						{
							Block bi=img.grid.bl[y][x];
							Block b_up=null;
							if (y>0) b_up=img.grid.bl[y-1][x];
							bi.interpolateGapV(b_up);
							
							bi.interpolateBoundaries(img.boundaries_YUV,img.downsampled_YUV);
							bi.downsampleBoundaries(bilineal,img.boundaries_YUV,img.boundaries_YUV);
						}

					}

					//---
				}

			
			//System.out.println(" hecho2");
			
			img.YUVtoBMP("./img/interpolated_homo.bmp",img.interpolated_YUV[0]);
			
			
			float percent=grid.getNumberOfDownsampledPixels();
			percent=100f*percent/(img.width*img.height);
			double psnr=PSNR.printPSNR(path_img, "./img/interpolated_homo.bmp");
			 System.out.println("HOMOGENEOUS DOWN percent="+percent+" PSNR: "+psnr);
			
	
		}
		//************************************************************************************
		public void doPSNRtable(String directory, String output_csv_file)
		{
		//dado un dir generamos una linea por cada file
			
		//primero pasamos el file a yuvbn
			
		}
		//************************************************************************************
		public float[][] getPSNRtable( String path_img)
		{
			
			float[][] pair=new float[20][2];
			
            // fill the % as input parameter
			float percent=5;
			for (int i=0 ;i<20;i++)
			{
			pair[i][0]=percent;
			percent+=5;
			}
			// fill the psnr for each %
			for (int i=0 ;i<20;i++)
			{
			pair[i][1]=getPSNR(  path_img, pair[i][0]);
			}
			
			//print the table
			System.out.println("");
			for (int i=0 ;i<20;i++)
			{
			System.out.print(""+pair[i][0]+" = "+pair[i][1]+";");
			}
			System.out.println("");
			return pair;
			
		}
		
		
		//************************************************************************************
		public float getPSNR( String path_img, float percent)
		{

			//from 100% to 0%
			float percent2=200f;
			float percent1=200f;
			float psnr2=0;
			float psnr1=0;
			float cf=5;
			
			//optimizacion: vamos a empezar en un psnr aproximado
			cf=tabla[(int)percent];
			
			System.out.println(" el CF es "+cf);
			float[] result=getPercent_and_PSNR( path_img,  cf);
			percent1=result[0];
			psnr1=result[1];
			float inc=0;
			//caso A: el percent1 es mayor de lo esperado
			if (percent1>=percent)
			{
				System.out.println("caso A percent1 > percent");
				 inc=0.001f;
			}
				else 
				{//caso b
			inc=-0.001f;
			System.out.println("caso B percent1 < percent");
				}
			boolean condicion=false;
			//while (percent1>=percent && cf>=0)
			while (condicion==false)
			{
				
				percent2=percent1;
				psnr2=psnr1;
				System.out.println("-------------------------------------------------------: "+cf);
				//float[] 
						result=getPercent_and_PSNR( path_img,  cf);
				if (result[0]!=percent1 || result[1]!=psnr1)
				{percent1=result[0];
				psnr1=result[1];
				}
				cf+=inc;//-=0.1f;
				//wait 200ms for hard drive . SO must flush pending data
				try{Thread.sleep(200);
				}catch(Exception e){}
				
				if (percent<=percent1 && percent>=percent2 && percent2!=200 && cf>=0) condicion=true;
				else if  (percent>=percent1 && percent<=percent2 && percent2!=200 && cf>=0) condicion=true;
				else if  (cf>=0) condicion=true;
					
				
				
			}
			//now we have two values for interpolation or extrapolation
			
			float resultado=0;
			if (percent2==percent1) resultado=psnr1;
			else{
				
				float alfa=(psnr2-psnr1)/(percent2-percent1);
			    resultado=psnr1+alfa*(percent-percent1);
		}
			System.out.println(" ");
				System.out.println("  RESULTADO INTERPOLADO:"+resultado+" dB");
					
				return resultado;
			
		
			
			
			
		}
		//************************************************************************************
		public float[] getPercent_and_PSNR( String path_img, float cf)
		{
			float[] result=new float[2];
			//-----------------------------------------------
			//load image into memory, transforming into YUV format
			final ImgUtil img=new ImgUtil();
			img.BMPtoYUV(path_img);

			//-----------------------------------------------
			//creation of grid
			Grid grid;
			grid = new Grid();
			grid.createGrid(img.width, img.height,cf,Block.MAX_PPP);
			img.grid=grid;

			//-----------------------------------------------
			//PR metrics and PPP assignment
			LHEquantizer lhe=new LHEquantizer();
			lhe.img=img;
			lhe.init();
			lhe.quantizeOneHopPerPixel(img.hops[0],img.LHE_YUV[0]);
			//img.saveHopsToTxt("./img/hops.txt");
			//img.YUVtoBMP("./img/LHE_YUV.bmp",img.LHE_YUV[0]);
			PRblock.img=img;
			img.grid.computeMetrics();
			//img.grid.printPRstats("./img/PRstats.txt");
			MODE=new String("");
			if (!MODE.equals("HOMO")) img.grid.fromPRtoPPP(grid.compression_factor);
			else grid.setPPPHomogeneousDownsamplingRatio(cf/2);
			//--------------------------------------------------
			//downsampling
			Block.img=img;
			for ( int y=0 ; y<img.grid.number_of_blocks_V;y++)
			{
				for ( int x=0 ; x<img.grid.number_of_blocks_H;x++)

				{

					Block bi=img.grid.bl[y][x];
					bi.pppToRectangleShape();
					bi.downsampleBlock(true);
					// bi.interpolateBoundaries(img.boundaries_YUV,img.downsampled_YUV);
					//	bi.downsampleBoundaries(true,img.boundaries_YUV,img.boundaries_YUV);

				}

			}
			//---------------------------------------------------------
			//interpolation
			for ( int y=0 ; y<img.grid.number_of_blocks_V;y++)
			{
				for ( int x=0 ; x<img.grid.number_of_blocks_H;x++)

				{
					Block bi=img.grid.bl[y][x];
					bi.interpolateBilinealV(img.downsampled_YUV,img.intermediate_interpolated_YUV);
					
				}
			}
			//img.YUVtoBMP("./img/interV.bmp",img.intermediate_interpolated_YUV[0]);
			for ( int y=0 ; y<img.grid.number_of_blocks_V;y++)
			{
				for ( int x=0 ; x<img.grid.number_of_blocks_H;x++)

				{
					Block bi=img.grid.bl[y][x];
					bi.interpolateBilinealH(img.intermediate_interpolated_YUV,img.interpolated_YUV);
				}
			}
			
			//img.YUVtoBMP("./img/interH.bmp",img.interpolated_YUV[0]);
			//--------------------interpolate gaps-------------------------------------
			if (true)//bilineal
			{
				for ( int y=0 ; y<img.grid.number_of_blocks_V;y++)
				{
					for ( int x=0 ; x<img.grid.number_of_blocks_H;x++)

					{
						Block bi=img.grid.bl[y][x];
						Block b_left=null;
						if (x>0) b_left=img.grid.bl[y][x-1];
						bi.interpolateGapH(b_left);

					}

				}
				//System.out.println(" hecho gaph");
			    //img.YUVtoBMP("./img/gaph.bmp",img.interpolated_YUV[0]);
				//----

				for ( int y=0 ; y<img.grid.number_of_blocks_V;y++)
				{
					for ( int x=0 ; x<img.grid.number_of_blocks_H;x++)

					{
						Block bi=img.grid.bl[y][x];
						Block b_up=null;
						if (y>0) b_up=img.grid.bl[y-1][x];
						bi.interpolateGapV(b_up);
						//bi.interpolateBoundaries(img.boundaries_YUV,img.downsampled_YUV);
						//bi.downsampleBoundaries(bilineal,img.boundaries_YUV,img.boundaries_YUV);
					}

				}

				//---
			}
			//---------------------------------------------------
			//saving file
			img.YUVtoBMP("./img/interpolated.bmp",img.interpolated_YUV[0]);
			//img.interpolatedYUVtoBMP("./img/interpolated_lena.bmp");

			float percent=grid.getNumberOfDownsampledPixels();
			percent=100f*percent/(img.width*img.height);
			result[0]=percent;
			
			//---------------------------------------------------
			//calculating psnr
			double psnr=PSNR.printPSNR(path_img, "./img/interpolated.bmp");
			System.out.println(" PSNR:"+psnr+"    percent:"+percent);
			result[1]=(float)psnr;
			
			return result;

		}
		//*************************************************************************
		public void cargaTabla()
		{
			tabla[0]=0.0f;
			tabla[1]=0.001f;
			tabla[2]=0.0050000004f;
			tabla[3]=0.011000001f;
			tabla[4]=0.017f;
			tabla[5]=0.020000001f;
			tabla[6]=0.025000002f;
			tabla[7]=0.031000003f;
			tabla[8]=0.033f;
			tabla[9]=0.03899999f;
			tabla[10]=0.04399998f;
			tabla[11]=0.047999974f;
			tabla[12]=0.052999966f;
			tabla[13]=0.058999956f;
			tabla[14]=0.06299995f;
			tabla[15]=0.065999955f;
			tabla[16]=0.070999965f;
			tabla[17]=0.07399997f;
			tabla[18]=0.07899998f;
			tabla[19]=0.08299999f;
			tabla[20]=0.085999995f;
			tabla[21]=0.09500001f;
			tabla[22]=0.100000024f;
			tabla[23]=0.10400003f;
			tabla[24]=0.111000046f;
			tabla[25]=0.116000056f;
			tabla[26]=0.12500007f;
			tabla[27]=0.12800008f;
			tabla[28]=0.12900008f;
			tabla[29]=0.1380001f;
			tabla[30]=0.14500012f;
			tabla[31]=0.14800012f;
			tabla[32]=0.15700014f;
			tabla[33]=0.16100015f;
			tabla[34]=0.17400017f;
			tabla[35]=0.18300019f;
			tabla[36]=0.1890002f;
			tabla[37]=0.19900022f;
			tabla[38]=0.20700024f;
			tabla[39]=0.21200025f;
			tabla[40]=0.21200025f;
			tabla[41]=0.22100027f;
			tabla[42]=0.2370003f;
			tabla[43]=0.24800032f;
			tabla[44]=0.2530003f;
			tabla[45]=0.25800022f;
			tabla[46]=0.27300003f;
			tabla[47]=0.28899983f;
			tabla[48]=0.29499975f;
			tabla[49]=0.30399963f;
			tabla[50]=0.31699947f;
			tabla[51]=0.31699947f;
			tabla[52]=0.33099928f;
			tabla[53]=0.3369992f;
			tabla[54]=0.3459991f;
			tabla[55]=0.35699895f;
			tabla[56]=0.37899867f;
			tabla[57]=0.3929985f;
			tabla[58]=0.3989984f;
			tabla[59]=0.40999827f;
			tabla[60]=0.42799804f;
			tabla[61]=0.44199786f;
			tabla[62]=0.44199786f;
			tabla[63]=0.4539977f;
			tabla[64]=0.4769974f;
			tabla[65]=0.4929972f;
			tabla[66]=0.5049971f;
			tabla[67]=0.53699666f;
			tabla[68]=0.5499965f;
			tabla[69]=0.5729962f;
			tabla[70]=0.588996f;
			tabla[71]=0.59099597f;
			tabla[72]=0.6189956f;
			tabla[73]=0.6289955f;
			tabla[74]=0.62999547f;
			tabla[75]=0.6589951f;
			tabla[76]=0.75599384f;
			tabla[77]=0.75599384f;
			tabla[78]=0.80199325f;
			tabla[79]=0.8429927f;
			tabla[80]=0.9449914f;
			tabla[81]=0.9449914f;
			tabla[82]=1.0129913f;
			tabla[83]=1.1009954f;
			tabla[84]=1.1539979f;
			tabla[85]=1.2780037f;
			tabla[86]=1.3840086f;
			tabla[87]=1.3840086f;
			tabla[88]=1.4270107f;
			tabla[89]=1.5940185f;
			tabla[90]=1.6350204f;
			tabla[91]=1.6930231f;
			tabla[92]=1.8390299f;
			tabla[93]=1.9070331f;
			tabla[94]=1.9070331f;
			tabla[95]=2.200023f;
			tabla[96]=2.200023f;
			tabla[97]=2.364011f;
			tabla[98]=2.4220068f;
			tabla[99]=2.5140002f;
			tabla[100]=2.5140002f;
		}
}
