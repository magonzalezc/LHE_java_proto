package LHE;

import java.util.Calendar;
import java.util.Date;

import kanzi.test.MySSIM;
import Qmetrics.PSNR;

public class FramePlayer {

	public boolean DEBUG=false;
	public String INTERPOL="BICUBIC";//[ "NN"| "BILINEAL"|"BICUBIC"]
	public Grid grid;
	public ImgUtil img;
	public String MODE="ELASTIC";//"ELASTIC"; // [ELASTIC | HOMO] : elastic downsampling or homogeneous downsampling

	public static String output_directory="./output_img";

	public static boolean ssim_active=true;
	//*******************************************************
	/**
	 * this function interprete a lhe file and save a bmp file
	 * in addition returns % and PSNR
	 * @param path_img
	 * @param img
	 * @return % , psnr
	 */
	public float[] playFrame(String path_img)//, boolean lineal)
	{
		float[] result=new float[3]; //bpp , psnr,ssim
		
		//boolean bilineal=lineal;
		//INTERPOL="NN";//"BICUBIC";//"BILINEAL";//"NN";
		//DEBUG=true;
		//bilineal = false;
		//-----------------------------------------------
		//debo leer la imagen

		//ahora llamamos al decoder
		//--------------------------------------------------------

		//ahora interpretamos los hops+ la malla
		//---------------------------------------------------------	
		//cada vez que interpretamos un bloque, calculamos sus boundaries para interpretar los siguientes
		
		//System.out.println(" salvando");
		//img.YUVtoBMP("./output_video"+"/cosa.bmp",img.downsampled_YUV[0]);
		//System.out.println(" hecho");
		
		//ahora ya tenemos a downsampled LHE. procedemos calcular el PSNR de cada bloque escalado
		//---------------------------------------------------------------------------------------
		/*
		Qmetrics.PSNRutil my_psnr=new Qmetrics.PSNRutil() ;
		for ( int y=0 ; y<grid.number_of_blocks_V;y++)
		{
			for ( int x=0 ; x<grid.number_of_blocks_H;x++)

			{
				Block bi=grid.bl[y][x];
				
				double mse=my_psnr.getMSE(img.downsampled_LHE_YUV[0],img.downsampled_YUV[0],bi.xini,bi.downsampled_xfin,bi.yini,bi.downsampled_yfin,img.width);
			
				 
			}
		}
	
				*/

		//ahora ya tenemos a downsampled LHE. procedemos a reescalar
		//-------------------------------------------------------------
		//puedo hacer V+H en cada bloque o bien primero V en todos y luego H en todos. da igual
		//por razones de debug mejor lo hago todo v y luego todo h y asi genero la imagen intermedia
		//y veo como va
		if (DEBUG) System.out.println("re-scaling...");
		//---------------------------------------------------
		Block.img=img;
		
		//interpolamos los boundaries izq/up para despues usarlos al hacer la interpolacion de gaps
		//ojo por que de momento estoy interpolando los gaps en bicubic siempre
		
		if (INTERPOL.equals("BICUBIC"))
		{
		for ( int y=0 ; y<grid.number_of_blocks_V;y++)
		{
			for ( int x=0 ; x<grid.number_of_blocks_H;x++)

			{
				Block bi=grid.bl[y][x];
				
		//bi.interpolateBoundariesIniHneighbour( img.downsampled_LHE_YUV, img.boundaries_ini_interH_YUV);
		bi.interpolateBoundariesIniVneighbour( img.downsampled_LHE_YUV, img.boundaries_ini_interV_YUV);
			}
		}
		}
		
		//ahora downsampling e interpolacion de fronteras en todos los tipos de interpol
		{
			for ( int y=0 ; y<grid.number_of_blocks_V;y++)
			{
				for ( int x=0 ; x<grid.number_of_blocks_H;x++)

				{
					Block bi=grid.bl[y][x];
					Block up=null;
					if (y>0) up=grid.bl[y-1][x];
					
					Block down=null;
					if (y<grid.number_of_blocks_V-1) down=grid.bl[y+1][x];
					
					//bi.interpolateBoundariesVneighbour(img.downsampled_LHE_YUV,img.boundaries_inter_YUV);
					//bi.interpolateBoundariesHneighbour(img.downsampled_LHE_YUV,img.boundaries_inter_YUV);
					
					//esto interpola en H el bloque y lo deja en frontierH
			        bi.interpolateNeighbourH( img.downsampled_LHE_YUV, img.frontierInterH_YUV);
	 		        //ahora hay que hacer un down de sus dos primeras filas pero con los ppp del bloque up
			        if (up!=null) bi.downsampleRow( img.frontierDownH_YUV, img.frontierInterH_YUV,up.ppp[0][2],up.ppp[0][3],bi.yini, up.lx_sc);
			        if (up!=null) bi.downsampleRow( img.frontierDownH_YUV, img.frontierInterH_YUV,up.ppp[0][2],up.ppp[0][3],bi.yini+1,up.lx_sc);
			        //ahora hay que hacer un down de sus dos ultimas filas pero con los ppp del bloque down
			        if (down!=null) bi.downsampleRow( img.frontierDownH_YUV, img.frontierInterH_YUV,down.ppp[0][0],down.ppp[0][1],bi.downsampled_yfin-1,down.lx_sc);
			        if (down!=null) bi.downsampleRow( img.frontierDownH_YUV, img.frontierInterH_YUV,down.ppp[0][0],down.ppp[0][1],bi.downsampled_yfin, down.lx_sc);
			        
			        //ahora copio las dos ultimas lineas al final. tienen la longitud del down, no de este bloque (bi)
			        if (down!=null)
			        for (int xi=down.xini;xi<=down.downsampled_xfin;xi++)
			           {
			        	img.frontierDownH_YUV[0][bi.yfin*img.width+xi]=img.frontierDownH_YUV[0][bi.downsampled_yfin*img.width+xi];
			        	img.frontierDownH_YUV[0][(bi.yfin-1)*img.width+xi]=img.frontierDownH_YUV[0][(bi.downsampled_yfin-1)*img.width+xi];
			           }
			        
			        /*
			        for (int xi=bi.xini;xi<=bi.xfin;xi++)
			           {
			        	img.frontierH_YUV[0][bi.yfin*img.width+xi]=img.frontierH_YUV[0][bi.downsampled_yfin*img.width+xi];
			        	img.frontierH_YUV[0][(bi.yfin-1)*img.width+xi]=img.frontierH_YUV[0][(bi.downsampled_yfin-1)*img.width+xi];
			           }
			           */
			//bi.interpolateBoundariesIniVneighbour( img.downsampled_LHE_YUV, img.boundaries_ini_interV_YUV);
				}
			}
			}
		
		
		
		//img.YUVtoBMP("./output_debug/iniboundH2.bmp",img.boundaries_ini_interH_YUV[0]);
		//ademas de interpolar los boundaries hay que downsamplearlos y luego interpretar los hops
		
		
		//vamos a interpolar V y luego h
		long lDateTime1 = new Date().getTime();
		System.out.println("Date() - Time in milliseconds: " + lDateTime1);
		//for (int cosa=1;cosa<1000;cosa++)
		{
		
		for ( int y=0 ; y<grid.number_of_blocks_V;y++)
		{
			for ( int x=0 ; x<grid.number_of_blocks_H;x++)

			{
				Block bi=grid.bl[y][x];
				
				
				//bi.bilineal=bilineal;//esto no vale para nada
				
				//bi.interpolateIniBoundariesHneighbour( img.downsampled_LHE_YUV, img.boundaries_ini_inter_YUV);
				//bi.interpolateIniBoundariesVneighbour( img.downsampled_LHE_YUV, img.boundaries_ini_inter_YUV);
				
			    //if (bi.bilineal) bi.interpolateBilinealV(img.downsampled_LHE_YUV,img.intermediate_interpolated_YUV);
				
				
				
				
				if (INTERPOL.equals("BICUBIC")) bi.interpolateBicubicV(img.downsampled_LHE_YUV,img.intermediate_interpolated_YUV);
				else if (INTERPOL.equals("BILINEAL"))bi.interpolateBilinealV(img.downsampled_LHE_YUV,img.intermediate_interpolated_YUV);
				else if (INTERPOL.equals("NN"))bi.interpolateNeighbourV(img.downsampled_LHE_YUV,img.intermediate_interpolated_YUV);
				else if (INTERPOL.equals("EXPERIMENTAL"))bi.interpolateAdaptV(img.downsampled_LHE_YUV,img.intermediate_interpolated_YUV);
			
				
				
				//if (bi.bilineal) bi.interpolateBicubicV(img.downsampled_LHE_YUV,img.intermediate_interpolated_YUV);
			//else	bi.interpolateNeighbourV(img.downsampled_LHE_YUV,img.intermediate_interpolated_YUV);

			}
		}
		//DEBUG=true;
		//if (DEBUG) System.out.println("Vertical interpolation ok");
		if (DEBUG) img.YUVtoBMP("./output_debug/interV.bmp",img.intermediate_interpolated_YUV[0]);
		if (DEBUG) img.YUVtoBMP("./output_debug/iniboundH.bmp",img.boundaries_ini_interH_YUV[0]);
		if (DEBUG) img.YUVtoBMP("./output_debug/iniboundV.bmp",img.boundaries_ini_interV_YUV[0]);
		
		
		
		
		
		
		for ( int y=0 ; y<grid.number_of_blocks_V;y++)
		{
			for ( int x=0 ; x<grid.number_of_blocks_H;x++)

			{
				Block bi=grid.bl[y][x];
				
				//boolean pr;
				/*if (bi.PRavg<0.3f) {
					//System.out.println("jjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjj");
					bi.bilineal=false;
					
				}
				*/
				
				
				//if (bi.bilineal)bi.interpolateBilinealH(img.intermediate_interpolated_YUV,img.interpolated_YUV);
				//if (bi.PRavg<0.25f)
				/*
				if (bi.bilineal) {
					//if (bi.PRavg<0.125f)bi.interpolateBilinealH(img.intermediate_interpolated_YUV,img.interpolated_YUV);
					//else
					bi.interpolateBicubicH(img.intermediate_interpolated_YUV,img.interpolated_YUV);
				}
				else bi.interpolateNeighbourH(img.intermediate_interpolated_YUV,img.interpolated_YUV);
				*/
				if (INTERPOL.equals("BICUBIC")) bi.interpolateBicubicH(img.intermediate_interpolated_YUV,img.interpolated_YUV);
				else if (INTERPOL.equals("BILINEAL"))bi.interpolateBilinealH(img.intermediate_interpolated_YUV,img.interpolated_YUV);
				else if (INTERPOL.equals("NN"))bi.interpolateNeighbourH(img.intermediate_interpolated_YUV,img.interpolated_YUV);
				else if (INTERPOL.equals("EXPERIMENTAL"))bi.interpolateAdaptH(img.intermediate_interpolated_YUV,img.interpolated_YUV);
				
				
			}
		}
		
		}
		long lDateTime2 = new Date().getTime();
		System.out.println("Date() - Time in milliseconds: " + lDateTime2);
		System.out.println("total: " + (lDateTime2-lDateTime1));
		
		if (DEBUG) System.out.println("Horizontal interpolation ok");
		if (DEBUG) img.YUVtoBMP("./output_debug/interH.bmp",img.interpolated_YUV[0]);
		//--------------------interpolate gaps-------------------------------------

		//if (DEBUG) System.out.println("bilineal es "+bilineal);
		
		//Block.img=img;// SIN ESTO FALLAAAAAA
		
		
		
		
		if (!INTERPOL.equals("NN"))
		//if (bilineal)//bilineal
		{
			for ( int y=0 ; y<grid.number_of_blocks_V;y++)
			{
				for ( int x=0 ; x<grid.number_of_blocks_H;x++)

				{
					Block bi=grid.bl[y][x];
					Block b_left=null;
					if (x>0) b_left=grid.bl[y][x-1];
					//if (DEBUG) System.out.println("interpolando GAPH bloque:"+x+","+y);
					//if (bi.bilineal) 
						bi.interpolateGapH(b_left);
					if (INTERPOL.equals("BICUBIC")) bi.interpolateGapHBicubic(b_left);
					//else if (INTERPOL.equals("BILINEAL"))bi.interpolateGapH(b_left);
					 
					

				}

			}
			//System.out.println(" hecho gaph");
			//img.YUVtoBMP("./img/gaph.bmp",img.interpolated_YUV[0]);
			//----
			if (DEBUG) System.out.println("interpolateGapH ok");
			if (DEBUG) img.YUVtoBMP("./output_debug/gaph.bmp",img.interpolated_YUV[0]);
			
			//img.YUVtoBMP("./output_debug/gaph.bmp",img.interpolated_YUV[0]);
			
			for ( int y=0 ; y<grid.number_of_blocks_V;y++)
			{
				for ( int x=0 ; x<grid.number_of_blocks_H;x++)

				{
					Block bi=grid.bl[y][x];
					Block b_up=null;
					if (y>0) b_up=grid.bl[y-1][x];
					//if (bi.bilineal) 
						//bi.interpolateGapV(b_up);
						
						//bi.interpolateGapVBicubic(b_up);
						if (INTERPOL.equals("BICUBIC")) bi.interpolateGapVBicubic(b_up);
						else if (INTERPOL.equals("BILINEAL"))bi.interpolateGapV(b_up);
					//bi.interpolateBoundaries(img.boundaries_YUV,img.downsampled_YUV);
					//bi.downsampleBoundaries(bilineal,img.boundaries_YUV,img.boundaries_YUV);
				}

			}

			//---
		}

		//---------------------------------------------------
		//saving file
		if (!MODE.equals("HOMO"))
			img.YUVtoBMP(output_directory+"/play.bmp",img.interpolated_YUV[0]);
		else
			img.YUVtoBMP(output_directory+"/play_homo.bmp",img.interpolated_YUV[0]);
		//img.interpolatedYUVtoBMP("./img/interpolated_lena.bmp");

		float percent=grid.getNumberOfDownsampledPixels();
		percent=100f*percent/(img.width*img.height);
		result[0]=percent;

		//---------------------------------------------------
		//calculating psnr
		//para que sea en gris, voy a generar primero una imagen en gris
		//ImgUtil grey=new ImgUtil();
		//grey.BMPtoYUV(path_img);
		//grey.YUVtoBMP("./output_debug/orig_YUV.bmp",grey.YUV[0]);
		
		double psnr=PSNR.printPSNR(path_img, output_directory+"/play.bmp");
		
		
		//double psnr=PSNR.printPSNR("./output_debug/orig_YUV.bmp", output_directory+"/play.bmp");
		System.out.println(" PSNR:"+psnr+"    percent:"+percent+"           file:"+path_img);
		System.out.println(" ---------------------------------------------------------------");
		result[1]=(float)psnr;

		float ssim=0;//
		if (ssim_active) ssim=MySSIM.getSSIM(path_img, output_directory+"/play.bmp");
		result[2]=ssim;
		
		//double psnrab=PSNR.printPSNR("./psnr/a.bmp", "./psnr/b.bmp");
		//System.out.println(" PSNR AB:"+psnrab);
		//System.out.println("hola");
		/*
   	    double mse=my_psnr.getMSE(img.interpolated_YUV[0],img.YUV[0],0,img.width-1,0,img.height-1,img.width);
		double psnr_mse=my_psnr.getPSNR(mse);
   	    System.out.println("total mse:"+mse+"   psnr:"+psnr_mse);
		*/
		
		
		
		return result;

		
		
		
	}
	//*************************************************************************
	public float[] playFrameDiffNOUSO(String path_img)
	{
		float[] result=new float[2];
		boolean bilineal=true;
		//bilineal = false;
		//-----------------------------------------------
		//debo leer la imagen

		//ahora llamamos al decoder
		//--------------------------------------------------------

		//ahora interpretamos los hops+ la malla
		//---------------------------------------------------------	
		//cada vez que interpretamos un bloque, calculamos sus boundaries para interpretar los siguientes
		
		
		
	//	for (int i=0;i<(img.width*img.height);i++) img.interpolated_YUV[0][i]=0;
		
		
		//ahora ya tenemos a downsampled LHE. procedemos calcular el PSNR de cada bloque escalado
		//---------------------------------------------------------------------------------------
		Qmetrics.PSNRutil my_psnr=new Qmetrics.PSNRutil() ;
		for ( int y=0 ; y<grid.number_of_blocks_V;y++)
		{
			for ( int x=0 ; x<grid.number_of_blocks_H;x++)

			{
				Block bi=grid.bl[y][x];
				
				float mse=my_psnr.getMSE(img.downsampled_LHE_YUV[0],img.downsampled_YUV[0],bi.xini,bi.downsampled_xfin,bi.yini,bi.downsampled_yfin,img.width);
				/*
			//	System.out.println(" block:"+y+","+x+":"+mse);
				float pravg=(grid.prbl[y][x].PRx+grid.prbl[y][x].PRy+
						     grid.prbl[y][x+1].PRx+grid.prbl[y][x+1].PRy+
						     grid.prbl[y+1][x].PRx+grid.prbl[y+1][x].PRy+
						     grid.prbl[y+1][x+1].PRx+grid.prbl[y+1][x+1].PRy)/8f;
				//if (mse>20)
				//System.out.println(" block:"+y+","+x+"     PRavg:"+pravg+"  MSE:"+mse +"  "+img.grid.prbl[y][x].PRx+","+img.grid.prbl[y][x].PRy+","+img.grid.prbl[y][x+1].PRx+","+img.grid.prbl[y][x+1].PRy+","+img.grid.prbl[y+1][x].PRx+","+img.grid.prbl[y+1][x].PRy+","+img.grid.prbl[y+1][x+1].PRx+","+img.grid.prbl[y+1][x+1].PRy);
				//if (pravg<0.5f)	bi.kinint=40;
				 */
				 
			}
		}
	
				

		//ahora ya tenemos a downsampled LHE. procedemos a reescalar
		//-------------------
	 
		//---------------------------------------------------
		for ( int y=0 ; y<grid.number_of_blocks_V;y++)
		{
			for ( int x=0 ; x<grid.number_of_blocks_H;x++)

			{
				Block bi=grid.bl[y][x];
			if (bilineal) bi.interpolateBilinealV(img.downsampled_LHE_YUV,img.intermediate_interpolated_YUV);
				
			else	bi.interpolateNeighbourV(img.downsampled_YUV,img.intermediate_interpolated_YUV);

			}
		}
		if (DEBUG) img.YUVtoBMP("./img/interV.bmp",img.intermediate_interpolated_YUV[0]);
		for ( int y=0 ; y<grid.number_of_blocks_V;y++)
		{
			for ( int x=0 ; x<grid.number_of_blocks_H;x++)

			{
				Block bi=grid.bl[y][x];
				if (bilineal)bi.interpolateBilinealH(img.intermediate_interpolated_YUV,img.interpolated_YUV);
				else bi.interpolateNeighbourH(img.intermediate_interpolated_YUV,img.interpolated_YUV);
			}
		}

		if (DEBUG) img.YUVtoBMP("./img/interH.bmp",img.interpolated_YUV[0]);
		//--------------------interpolate gaps-------------------------------------

		
		
		
		if (bilineal)//bilineal
		{
			for ( int y=0 ; y<grid.number_of_blocks_V;y++)
			{
				for ( int x=0 ; x<grid.number_of_blocks_H;x++)

				{
					Block bi=grid.bl[y][x];
					Block b_left=null;
					if (x>0) b_left=grid.bl[y][x-1];
					//bi.interpolateGapHNeighbour(b_left);
					bi.interpolateGapH(b_left);
				}

			}
			//System.out.println(" hecho gaph");
			//img.YUVtoBMP("./img/gaph.bmp",img.interpolated_YUV[0]);
			//----

			for ( int y=0 ; y<grid.number_of_blocks_V;y++)
			{
				for ( int x=0 ; x<grid.number_of_blocks_H;x++)

				{
					Block bi=grid.bl[y][x];
					Block b_up=null;
					if (y>0) b_up=grid.bl[y-1][x];
					bi.interpolateGapV(b_up);
					//bi.interpolateGapVNeighbour(b_up);
					//bi.interpolateBoundaries(img.boundaries_YUV,img.downsampled_YUV);
					//bi.downsampleBoundaries(bilineal,img.boundaries_YUV,img.boundaries_YUV);
				}

			}

			//---
		}
		

		//---------------------------------------------------
		//saving file
		if (!MODE.equals("HOMO"))
			img.YUVtoBMP(output_directory+"/play.bmp",img.interpolated_YUV[0]);
		else
			img.YUVtoBMP(output_directory+"/play_homo.bmp",img.interpolated_YUV[0]);
		//img.interpolatedYUVtoBMP("./img/interpolated_lena.bmp");

		float percent=grid.getNumberOfDownsampledPixels();
		percent=100f*percent/(img.width*img.height);
		result[0]=percent;

		//---------------------------------------------------
		//calculating psnr
		
		double psnr=PSNR.printPSNR(path_img, output_directory+"/play.bmp");
		System.out.println(" PSNR:"+psnr+"    percent:"+percent+"           file:"+path_img);
		System.out.println(" --------sss-------------------------------------------------------");
		result[1]=(float)psnr;

		
   	    float mse=my_psnr.getMSE(img.interpolated_YUV[0],img.YUV[0],0,img.width-1,0,img.height-1,img.width);
		double psnr_mse=my_psnr.getPSNR(mse);
   	    System.out.println("total mse:"+mse+"   psnr:"+psnr_mse);
		
		
		
		
		return result;

		
		
		
	}
	//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
	public float[] playFrameFrontierNOUSO(String path_img, boolean lineal)
	{
		float[] result=new float[2];
		boolean bilineal=lineal;
		//INTERPOL="NN";//"BICUBIC";//"BILINEAL";//"NN";
		//DEBUG=true;
		//bilineal = false;
		//-----------------------------------------------
		//debo leer la imagen

		//ahora llamamos al decoder
		//--------------------------------------------------------

		//ahora interpretamos los hops+ la malla
		//---------------------------------------------------------	
		//cada vez que interpretamos un bloque, calculamos sus boundaries para interpretar los siguientes
		
		//System.out.println(" salvando");
		//img.YUVtoBMP("./output_video"+"/cosa.bmp",img.downsampled_YUV[0]);
		//System.out.println(" hecho");
		
		//ahora ya tenemos a downsampled LHE. procedemos calcular el PSNR de cada bloque escalado
		//---------------------------------------------------------------------------------------
		/*
		Qmetrics.PSNRutil my_psnr=new Qmetrics.PSNRutil() ;
		for ( int y=0 ; y<grid.number_of_blocks_V;y++)
		{
			for ( int x=0 ; x<grid.number_of_blocks_H;x++)

			{
				Block bi=grid.bl[y][x];
				
				double mse=my_psnr.getMSE(img.downsampled_LHE_YUV[0],img.downsampled_YUV[0],bi.xini,bi.downsampled_xfin,bi.yini,bi.downsampled_yfin,img.width);
			
				 
			}
		}
	
				*/

		//ahora ya tenemos a downsampled LHE. procedemos a reescalar
		//-------------------------------------------------------------
		//puedo hacer V+H en cada bloque o bien primero V en todos y luego H en todos. da igual
		//por razones de debug mejor lo hago todo v y luego todo h y asi genero la imagen intermedia
		//y veo como va
		if (DEBUG) System.out.println("re-scaling...");
		//---------------------------------------------------
		Block.img=img;
		
		//interpolamos los boundaries izq/up para despues usarlos al hacer la interpolacion de gaps
		//ojo por que de momento estoy interpolando los gaps en bicubic siempre
		//if (INTERPOL.equals("BICUBIC"))
		{
		for ( int y=0 ; y<grid.number_of_blocks_V;y++)
		{
			for ( int x=0 ; x<grid.number_of_blocks_H;x++)

			{
				Block bi=grid.bl[y][x];
				Block up=null;
				if (y>0) up=grid.bl[y-1][x];
				
				Block down=null;
				if (y<grid.number_of_blocks_V-1) down=grid.bl[y+1][x];
				
				//bi.interpolateBoundariesVneighbour(img.downsampled_LHE_YUV,img.boundaries_inter_YUV);
				//bi.interpolateBoundariesHneighbour(img.downsampled_LHE_YUV,img.boundaries_inter_YUV);
				
				//esto interpola en H el bloque y lo deja en frontierH
		        bi.interpolateNeighbourH( img.downsampled_LHE_YUV, img.frontierInterH_YUV);
 		        //ahora hay que hacer un down de sus dos primeras filas pero con los ppp del bloque up
		        if (up!=null) bi.downsampleRow( img.frontierDownH_YUV, img.frontierInterH_YUV,up.ppp[0][2],up.ppp[0][3],bi.yini, up.lx_sc);
		        if (up!=null) bi.downsampleRow( img.frontierDownH_YUV, img.frontierInterH_YUV,up.ppp[0][2],up.ppp[0][3],bi.yini+1,up.lx_sc);
		        //ahora hay que hacer un down de sus dos ultimas filas pero con los ppp del bloque down
		        if (down!=null) bi.downsampleRow( img.frontierDownH_YUV, img.frontierInterH_YUV,down.ppp[0][0],down.ppp[0][1],bi.downsampled_yfin-1,down.lx_sc);
		        if (down!=null) bi.downsampleRow( img.frontierDownH_YUV, img.frontierInterH_YUV,down.ppp[0][0],down.ppp[0][1],bi.downsampled_yfin, down.lx_sc);
		        
		        //ahora copio las dos ultimas lineas al final. tienen la longitud del down, no de este bloque (bi)
		        if (down!=null)
		        for (int xi=down.xini;xi<=down.downsampled_xfin;xi++)
		           {
		        	img.frontierDownH_YUV[0][bi.yfin*img.width+xi]=img.frontierDownH_YUV[0][bi.downsampled_yfin*img.width+xi];
		        	img.frontierDownH_YUV[0][(bi.yfin-1)*img.width+xi]=img.frontierDownH_YUV[0][(bi.downsampled_yfin-1)*img.width+xi];
		           }
		        
		        /*
		        for (int xi=bi.xini;xi<=bi.xfin;xi++)
		           {
		        	img.frontierH_YUV[0][bi.yfin*img.width+xi]=img.frontierH_YUV[0][bi.downsampled_yfin*img.width+xi];
		        	img.frontierH_YUV[0][(bi.yfin-1)*img.width+xi]=img.frontierH_YUV[0][(bi.downsampled_yfin-1)*img.width+xi];
		           }
		           */
		//bi.interpolateBoundariesIniVneighbour( img.downsampled_LHE_YUV, img.boundaries_ini_interV_YUV);
			}
		}
		}
		//img.YUVtoBMP("./output_debug/iniboundH2.bmp",img.boundaries_ini_interH_YUV[0]);
		//ademas de interpolar los boundaries hay que downsamplearlos y luego interpretar los hops
		img.YUVtoBMP("./output_debug/frontierDownH.bmp",img.frontierDownH_YUV[0]);
		System.out.println(" holaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
		//if (1<2) System.exit(0);
		
		//vamos a interpolar V y luego h
		long lDateTime1 = new Date().getTime();
		System.out.println("Date() - Time in milliseconds: " + lDateTime1);
		//for (int cosa=1;cosa<1000;cosa++)
		{
		
		for ( int y=0 ; y<grid.number_of_blocks_V;y++)
		{
			for ( int x=0 ; x<grid.number_of_blocks_H;x++)

			{
				Block bi=grid.bl[y][x];
				
				
				//bi.bilineal=bilineal;//esto no vale para nada
				
				//bi.interpolateIniBoundariesHneighbour( img.downsampled_LHE_YUV, img.boundaries_ini_inter_YUV);
				//bi.interpolateIniBoundariesVneighbour( img.downsampled_LHE_YUV, img.boundaries_ini_inter_YUV);
				
			    //if (bi.bilineal) bi.interpolateBilinealV(img.downsampled_LHE_YUV,img.intermediate_interpolated_YUV);
				
				
				Block b_down=null;
				if (y<grid.number_of_blocks_V-1) b_down=grid.bl[y+1][x];
				System.out.println(y);
				Block b_up=null;
				if (y>0) b_up=grid.bl[y-1][x];
				
				
				if (INTERPOL.equals("BICUBIC")) bi.interpolateBicubicV(img.downsampled_LHE_YUV,img.intermediate_interpolated_YUV);
				//else if (INTERPOL.equals("BILINEAL"))bi.interpolateBilinealVF(img.downsampled_LHE_YUV,img.intermediate_interpolated_YUV);
				else if (INTERPOL.equals("BILINEAL"))bi.interpolateBilinealVF2(img.downsampled_LHE_YUV,img.intermediate_interpolated_YUV,b_up,b_down);
				else if (INTERPOL.equals("NN"))bi.interpolateNeighbourV(img.downsampled_LHE_YUV,img.intermediate_interpolated_YUV);
				
				if (!INTERPOL.equals("NN")) bi.fillFrontierV(img.intermediate_interpolated_YUV);
				
				
				//if (bi.bilineal) bi.interpolateBicubicV(img.downsampled_LHE_YUV,img.intermediate_interpolated_YUV);
			//else	bi.interpolateNeighbourV(img.downsampled_LHE_YUV,img.intermediate_interpolated_YUV);

			}
		}
		//DEBUG=true;
		//if (DEBUG) System.out.println("Vertical interpolation ok");
		//if (DEBUG) 
			img.YUVtoBMP("./output_debug/interV.bmp",img.intermediate_interpolated_YUV[0]);
			img.YUVtoBMP("./output_debug/frontierV.bmp",img.frontierInterV_YUV[0]);
		//if (DEBUG) img.YUVtoBMP("./output_debug/iniboundH.bmp",img.boundaries_ini_interH_YUV[0]);
		//if (DEBUG) img.YUVtoBMP("./output_debug/iniboundV.bmp",img.boundaries_ini_interV_YUV[0]);
		//if (1<2) System.exit(0);
		//img.YUVtoBMP("./output_debug/frontierV.bmp",img.frontierV_YUV[0]);
		
		for ( int y=0 ; y<grid.number_of_blocks_V;y++)
		{
			for ( int x=0 ; x<grid.number_of_blocks_H;x++)

			{
				Block bi=grid.bl[y][x];
				
				//boolean pr;
				/*if (bi.PRavg<0.3f) {
					//System.out.println("jjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjj");
					bi.bilineal=false;
					
				}
				*/
				
				
				//if (bi.bilineal)bi.interpolateBilinealH(img.intermediate_interpolated_YUV,img.interpolated_YUV);
				//if (bi.PRavg<0.25f)
				/*
				if (bi.bilineal) {
					//if (bi.PRavg<0.125f)bi.interpolateBilinealH(img.intermediate_interpolated_YUV,img.interpolated_YUV);
					//else
					bi.interpolateBicubicH(img.intermediate_interpolated_YUV,img.interpolated_YUV);
				}
				else bi.interpolateNeighbourH(img.intermediate_interpolated_YUV,img.interpolated_YUV);
				*/
				if (INTERPOL.equals("BICUBIC")) bi.interpolateBicubicH(img.intermediate_interpolated_YUV,img.interpolated_YUV);
				else if (INTERPOL.equals("BILINEAL"))bi.interpolateBilinealHF(img.intermediate_interpolated_YUV,img.interpolated_YUV);
				else if (INTERPOL.equals("NN"))bi.interpolateNeighbourH(img.intermediate_interpolated_YUV,img.interpolated_YUV);
				
				
				
			}
		}
		
		}
		long lDateTime2 = new Date().getTime();
		System.out.println("Date() - Time in milliseconds: " + lDateTime2);
		System.out.println("total: " + (lDateTime2-lDateTime1));
		
		if (DEBUG) System.out.println("Horizontal interpolation ok");
		//if (DEBUG)
			img.YUVtoBMP("./output_debug/interH.bmp",img.interpolated_YUV[0]);
		//--------------------interpolate gaps-------------------------------------

		//if (DEBUG) System.out.println("bilineal es "+bilineal);
		
		//Block.img=img;// SIN ESTO FALLAAAAAA
		
		if (!INTERPOL.equals("NN"))
		//if (bilineal)//bilineal
		{
			for ( int y=0 ; y<grid.number_of_blocks_V;y++)
			{
				for ( int x=0 ; x<grid.number_of_blocks_H;x++)

				{
					Block bi=grid.bl[y][x];
					Block b_left=null;
					if (x>0) b_left=grid.bl[y][x-1];
					//if (DEBUG) System.out.println("interpolando GAPH bloque:"+x+","+y);
					//if (bi.bilineal) 
					//	bi.interpolateGapH(b_left);
					if (INTERPOL.equals("BICUBIC")) bi.interpolateGapHBicubic(b_left);
					else if (INTERPOL.equals("BILINEAL"))bi.interpolateGapH(b_left);
					 
					

				}

			}
			//System.out.println(" hecho gaph");
			//img.YUVtoBMP("./img/gaph.bmp",img.interpolated_YUV[0]);
			//----
			if (DEBUG) System.out.println("interpolateGapH ok");
			if (DEBUG) img.YUVtoBMP("./output_debug/gaph.bmp",img.interpolated_YUV[0]);
			
			//img.YUVtoBMP("./output_debug/gaph.bmp",img.interpolated_YUV[0]);
			
			for ( int y=0 ; y<grid.number_of_blocks_V;y++)
			{
				for ( int x=0 ; x<grid.number_of_blocks_H;x++)

				{
					Block bi=grid.bl[y][x];
					Block b_up=null;
					if (y>0) b_up=grid.bl[y-1][x];
					//if (bi.bilineal) 
						//bi.interpolateGapV(b_up);
						
						//bi.interpolateGapVBicubic(b_up);
						if (INTERPOL.equals("BICUBIC")) bi.interpolateGapVBicubic(b_up);
						else if (INTERPOL.equals("BILINEAL"))bi.interpolateGapV(b_up);
					//bi.interpolateBoundaries(img.boundaries_YUV,img.downsampled_YUV);
					//bi.downsampleBoundaries(bilineal,img.boundaries_YUV,img.boundaries_YUV);
				}

			}

			//---
		}

		//---------------------------------------------------
		//saving file
		if (!MODE.equals("HOMO"))
			img.YUVtoBMP(output_directory+"/play.bmp",img.interpolated_YUV[0]);
		else
			img.YUVtoBMP(output_directory+"/play_homo.bmp",img.interpolated_YUV[0]);
		//img.interpolatedYUVtoBMP("./img/interpolated_lena.bmp");

		float percent=grid.getNumberOfDownsampledPixels();
		percent=100f*percent/(img.width*img.height);
		result[0]=percent;

		//---------------------------------------------------
		//calculating psnr
		
		double psnr=PSNR.printPSNR(path_img, output_directory+"/play.bmp");
		System.out.println(" PSNR:"+psnr+"    percent:"+percent+"           file:"+path_img);
		System.out.println(" ---------------------------------------------------------------");
		result[1]=(float)psnr;

		//System.out.println("hola");
		/*
   	    double mse=my_psnr.getMSE(img.interpolated_YUV[0],img.YUV[0],0,img.width-1,0,img.height-1,img.width);
		double psnr_mse=my_psnr.getPSNR(mse);
   	    System.out.println("total mse:"+mse+"   psnr:"+psnr_mse);
		*/
		
		
		
		return result;

		
		
		
	}
	//*************************************************************************
	public void playCosturas(String filename)
	{
		//cargo la imagen en img
		img.BMPtoYUV(filename);
		Grid grid=new Grid();
		grid.createGrid(img.width, img.height);
		//voy a rellenar los PPP de la grid
		
		//hacemos como que la hemos interpolado
		img.interpolated_YUV=img.YUV; 
		
		Block.img=img;
		
		//si uso bicubica no funciona porque necesita las muestras extra
		INTERPOL="BILINEAL";
		
		//ya esta cargada. ahora interpolamos las costuras
		for ( int y=0 ; y<grid.number_of_blocks_V;y++)
		{
			for ( int x=0 ; x<grid.number_of_blocks_H;x++)

			{
				Block bi=grid.bl[y][x];
				Block b_left=null;
				if (x>0) b_left=grid.bl[y][x-1];
				
				//voy a rellenar PPP
				for (int i=0;i<2;i++)
				{
					for (int j=0;j<2;j++)
					{
				    bi.ppp[i][j]=Block.MAX_PPP;
				    if (b_left!=null) b_left.ppp[i][j]=Block.MAX_PPP;
					}
				}
				//if (DEBUG) System.out.println("interpolando GAPH bloque:"+x+","+y);
				//if (bi.bilineal) 
				//	bi.interpolateGapH(b_left);
				if (INTERPOL.equals("BICUBIC")) bi.interpolateGapHBicubic(b_left);
				else if (INTERPOL.equals("BILINEAL"))bi.interpolateGapH(b_left);
				 
				

			}

		}
		//System.out.println(" hecho gaph");
		//img.YUVtoBMP("./img/gaph.bmp",img.interpolated_YUV[0]);
		//----
		if (DEBUG) System.out.println("interpolateGapH ok");
		if (DEBUG) img.YUVtoBMP("./output_debug/gaph_costuras.bmp",img.interpolated_YUV[0]);
		
		//img.YUVtoBMP("./output_debug/gaph.bmp",img.interpolated_YUV[0]);
		
		for ( int y=0 ; y<grid.number_of_blocks_V;y++)
		{
			for ( int x=0 ; x<grid.number_of_blocks_H;x++)

			{
				Block bi=grid.bl[y][x];
				Block b_up=null;
				if (y>0) b_up=grid.bl[y-1][x];
				//if (bi.bilineal) 
					//bi.interpolateGapV(b_up);
					
					//bi.interpolateGapVBicubic(b_up);
				//voy a rellenar PPP
				for (int i=0;i<2;i++)
				{
					for (int j=0;j<2;j++)
					{
				    bi.ppp[i][j]=Block.MAX_PPP;
				    if (b_up!=null) b_up.ppp[i][j]=Block.MAX_PPP;
					}
				}
				
				
				if (INTERPOL.equals("BICUBIC")) bi.interpolateGapVBicubic(b_up);
					else if (INTERPOL.equals("BILINEAL"))bi.interpolateGapV(b_up);
				//bi.interpolateBoundaries(img.boundaries_YUV,img.downsampled_YUV);
				//bi.downsampleBoundaries(bilineal,img.boundaries_YUV,img.boundaries_YUV);
			}

		}
		
		img.YUVtoBMP(output_directory+"/play.bmp",img.interpolated_YUV[0]);
		
		
	}
	
}
