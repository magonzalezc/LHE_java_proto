import java.io.File;
import java.util.Scanner;

import LHE.Block;
import LHE.Grid;
import LHE.ImgUtil;
import LHE.LHEquantizer;
import LHE.PRblock;
import LHE.VideoCompressor;
import Qmetrics.PSNR;


public class ElasticDownsamplingTester {
	float [] tabla=new float[101];//conversion table. tabla[%] = CompressionFactor
	String MODE="";
	Boolean DEBUG=false;
	float CF_ANT=0; //para tabla

	ImgUtil img;// img object
	//cf
	float [] cf_avg;//compresion factor

	public static void main(String [ ] args)
	{

		
		ElasticDownsamplingTester ed=new ElasticDownsamplingTester();
		ed.loadConversionTable();//conversion table from percentage to compression factor


		//set MODE
		//ed.MODE=new String("HOMO");
	//String file=new String("./img/man_1k.bmp");

	//	String file=new String("./img/man_512_SPS.bmp");
//String file=new String("./img/lena.bmp");
		String file=new String("./img/line3b.bmp");
		//String file=new String("./img/man.bmp");
//String file=new String("../debug/BN-kodim23.bmp");
//	String file=new String("../debug/BN-kodim03.bmp");
//	String file=new String("./img/mario137.bmp");
//	String file=new String("./img/foreman17.bmp");
	//	String file=new String("./img/boat.bmp");
		//String file=new String("./img/baboon.bmp");
	//	String file=new String("./img/peppersBN.bmp");
	//	String file=new String("./img/simple.bmp");
	//	String file=new String("../debug/BN-kodim07.bmp");
	//	String file=new String("./img/Mexbn2bn512.bmp");
	//	String file=new String("./output_img/play.bmp");
	//	String file=new String("./img/ruler.512.bmp");
		//String file=new String("./img/white.bmp");
		//String file=new String("./img/bloque_abrupto.bmp");
		//String file=new String("./img/Animals_9.bmp");
		//String file=new String("./img/noise_eye2.bmp");
	//	String file=new String("./img/noisecar.bmp");
	//	String file=new String("../debug/BN-kodim04.bmp");
	//String file=new String("../debug/BN-kodim20.bmp");//avion
	//	String file=new String("./input_video/mario99/mariob226.bmp");
	//	String file=new String("./img/mariob155.bmp");
	//String file=new String("./input_video/foreman/foreman100.bmp");
		//String file=new String("./input_video/foreman/foreman238.bmp");
	//String file=new String("./input_video/foreman/foreman239.bmp");
	//String file=new String("../debug/BN-kodim10.bmp");
		//String file=new String("../debug/BN-kodim10.bmp");
	//	String file=new String("../debug/BN-kodim21.bmp");
	//	String file=new String("../debug/BN-kodim20.bmp");
	//	String file=new String("../debug/BN-kodim24.bmp");
	//	String file=new String("./img/BN-kodim24.bmp");
		//String file=new String("./img/Cartoons_8.bmp");
		//String file=new String("./img/white.bmp");
	//	String file=new String("./img/noise.bmp");
		String path_img=new String(file);



		//	ed.getPSNRtableRow(path_img);
		
		
		//ed.doPSNRtable("","");

		//ed.createNoiseImage();
		//ed.createImageSoft();
		//ed.createImageHard();
		//ed.createImageNoise32();


		//ed.computeConversionTable();
		//float percent=8.0f;
		float percent=3f;
		percent=10f;
		percent=46f;
	percent=63f;//25% simple
	percent=39f;//25%lena
	percent=34f;//25%lena
	percent=63f;//25%lena
	
	percent=30f;//25%lena
	percent=35f;//25%lena
	
	//percent=10f;//25%lena
		//ed.getPSNR(path_img,percent);
		//ed.getPercentAndPSNR(path_img,ed.tabla[(int) percent]);
		//if (1<2) System.exit(0);
		for (int k=0;k<1;k++){
			try{
				Thread.sleep(1000);
				
			}catch(Exception e){}
		ed.img=new ImgUtil();
		//ed.compress(path_img,ed.tabla[(int) percent], ed.img, true);
		
		
		LHE.FrameCompressor fc=new LHE.FrameCompressor(1);
		System.out.println("fc creado");
		fc.DEBUG=true;
		fc.loadFrame(path_img);
		
		fc.img.YUVtoBMP("./output_debug/orig_YUV.bmp",fc.img.YUV[0]);
		//fc.MODE="HOMO";
		percent=19;
		System.out.println(" cf="+ed.tabla[(int) percent]);
		//fc.grid.SetGaussianDsitributionPR(0.29f,0.0474);
		
		//fc.compressFrame(ed.tabla[(int) percent]);
		fc.compressFrame(20f);//0.82f);
		//fc.compressFrame(10);//0.82f);
		
		
		//fc.compressFrame(1);
		//fc.compressFrame( 4);//caso homo
		ed.img=fc.img;
		//ed.compress(path_img,0.6776735f, ed.img,true);
		//ed.compress(path_img,1.05366103f, ed.img);
		//2.2260935
		//ed.compress(path_img,0.64269173f, ed.img);
		//ed.compress(path_img,ed.tabla[(int) percent], ed.img);
		
		System.out.println("creando el player");
		LHE.FramePlayer fp=new LHE.FramePlayer();
		fp.DEBUG=true;
		fp.img=fc.img;
		fp.grid=fc.grid;
		fp.playFrame( path_img,true);
		
		if (1<2) System.exit(0);
		
		//fc.loadFrame(path_img);
		//fc.img.createChess();
		//fc.img.YUVtoBMP("./img/cuadros.bmp", fc.img.YUV[0]);
		
	}
		
		
	    
		
		//if (1<2) System.exit(0);
		//ed.play(path_img, ed.img);
		VideoCompressor vc =new VideoCompressor();
		//vc.compressVideoDiffAlt(1f);
		//vc.compressVideoDiffAlt(0.8f);
		//vc.compressVideoDiffAltOne(1.0f);
		
		//vc.compressVideoDirect(0.85f);
		
		//vc.compressVideoDiffBasic(4f);//caso homo
		//vc.compressVideoDiffBasic(0.95f);
		//vc.compressVideoDiffBasic(0.75f);//11%
	
		//vc.compressVideoDiffBasic(3.0f);
		
		
		
		//vc.compressVideoDiffY4(0.8f);
	//	vc.compressVideoDiffY4(1.0f);
		//vc.compressVideoDiffY4(0.85f);
		
		//vc.compressVideoDiffY4(1.0f);
		
		// valores:
		// 1 da 6.5% en mario
		// 1 da      em foreman
		// 1.1 da 9.2% en mario
		
		//vc.compressVideoLogY3(0.7f);
		
		//vc.compressVideoDiffY3PRtrail(1.0f);
		
		
		//vc.compressVideoNONtrail(0f);//0.9f);
		vc.compressVideoNONtrail_V2(1.05f);//0.9f);
		
		
		
		//vc.compressVideoDiffBasic(1.0f);
		
		
		
	//	vc.compressVideoDiffBasicINV(0.8f);
		//vc.compressVideoDiffGob(10,0.92f);
		
		if (1<2) System.exit(0);
		
		//ed.getPercentAndPSNR(path_img,ed.tabla[(int) percent]);
		ed.doPSNRtable("","");
		if (1<2) System.exit(0);


	}

	//************************************************************************************
	/**
	 * This function is for experimental use.
	 * generates a table for each image of a directory.
	 * each row of this table contains the PSNR for several percentage values
	 * 
	 *            5% ; 10% ; 15% ; 20% ; 25% ; 30% ; 35% ;...; 95%; 100% 
	 *    file-1
	 *    file-2
	 *    file-n
	 *    
	 * @param directory
	 * @param output_csv_file
	 */
	public void doPSNRtable(String directory, String output_csv_file)
	{
		//dado un dir generamos una linea por cada file
		System.out.println ("Type directory name:");	
		Scanner teclado = new Scanner (System.in);		
		String directorio =  teclado.next();
		teclado.close();

		//read directory
		File file = new File(directorio);
		if (!file.exists()) {
			System.out.println("El directorio no existe");
			System.exit(0);
		}
		String [] ficherosEnDirectorio = file.list();
		for (int i=0;i<ficherosEnDirectorio.length;i++) {
			//System.out.println(ficherosEnDirectorio[i]);
		}


		ImgUtil img=new ImgUtil();
		
		//for each file
		//primero pasamos el file a yuvbnç
		String[] rows=new String[ficherosEnDirectorio.length];
		for (int i=0;i<ficherosEnDirectorio.length;i++)
		{
			System.out.println(" processing file "+ficherosEnDirectorio[i]+" ...");
			img.BMPtoYUV(directorio+"/"+ficherosEnDirectorio[i]);
			String bn_file=new String("../debug/BN-"+ficherosEnDirectorio[i]);
			img.YUVtoBMP(bn_file,img.YUV[0]);

			//ahora generamos row
			CF_ANT=0;
			rows[i]=getPSNRtableRow(bn_file);

		}

		for (int i=0;i<ficherosEnDirectorio.length;i++)
		{
			//cf_avg[i]=cf_avg[i]/(float)ficherosEnDirectorio.length;
			//System.out.println ("tabla[")
		}



		System.out.println("file;5;10;15;20;25;30;35;40;45;50;55;60;65;70;75;80;85;90;95;100");
		for (int i=0;i<ficherosEnDirectorio.length;i++)
		{
			System.out.println(ficherosEnDirectorio[i]+rows[i]);

		}
		//img.BMPtoYUV("./img/ajuste.bmp");
	}
	//************************************************************************************
/**
 * this function is invoked by doPSNRtable(), in order to built one row of the final table
 * @param path_img
 * @return
 */
	public String getPSNRtableRow( String path_img)
	{
		//pairs percent, PSNR
		float[][] pair=new float[20][2];


		cf_avg=new float[20];

		// fill the % as input parameter
		float percent=5;
		for (int i=0 ;i<20;i++)
		{
			pair[i][0]=percent;
			percent+=5;
		}
		// fill the psnr for each % .  reach 100% at i=20
		for (int i=0 ;i<20;i++)
		{
			pair[i][1]=getPSNR(  path_img, pair[i][0]);
			cf_avg[i]+=CF_ANT;
		}

		//print the table into a variable
		String row=new String();
		System.out.println("");
		for (int i=0 ;i<20;i++)
		{
			System.out.print(""+pair[i][0]+" = "+pair[i][1]+";");
			row+=";"+pair[i][1];
		}
		System.out.println("");
		//return pair;
		return row;

	}


	//************************************************************************************
	/**
	 * this function get the PSNR for a given image (path_img) and given percentage of preserved spatial info
	 * the function moves from a reference Compression factor for this percentage (taken from conversion_table)
	 * to higher CF or lower CF in order to reach the desired percentage
	 * 
	 * @param path_img
	 * @param percent
	 * @return
	 */
	public float getPSNR( String path_img, float percent)
	{

		//from 100% to 0%
		float percent2=200f;
		float percent1=200f;
		float psnr2=0;
		float psnr1=0;


		//optimizacion: vamos a empezar en un psnr aproximado
		// si nos han invocado para hacer tabla podemos coger el ultimo anterior
		float cf=tabla[(int)percent];
		System.out.println("  precalculated CompressionFactor is "+cf);

		// si nos han invocado para hacer la fila de todos los % podemos coger el anterior CF
		if (CF_ANT!=0) cf=CF_ANT;
		if (percent==1) {percent=2;} // 1% is not valid. minimum is 1.5%

		System.out.println(" el CF es "+cf+ "  CF_ANT:"+CF_ANT);
		float[] result;

		//there are two modes: homogeneous downsampling and elastic downsampling
		System.out.println(" MODE:"+ MODE);
		if (MODE.equals("HOMO")) 
		{

			percent=percent/100f;

			cf=(float)Math.sqrt(1f/percent);
			cf=1f/percent;//esto es el ratio
			System.out.println(" HOMOGENEO: PPP="+ cf+"    percent:"+percent*100+"%");
		}

		//result=getPercentAndPSNR( path_img,  cf);
		
		//new strategy
		/*
		img=new ImgUtil();
		compress(path_img,cf, img, true);
		
		result=play(path_img,img);
		*/
		//renew strategy
		LHE.FrameCompressor fc=new LHE.FrameCompressor(1);
		fc.DEBUG=false;
		fc.loadFrame(path_img);
		fc.compressFrame(cf);
		
		LHE.FramePlayer fp=new LHE.FramePlayer();
		fp.img=fc.img;
		fp.grid=fc.grid;
		result=fp.playFrame( path_img);
		//--end renew strategy
		
		if (result[1]==0) result[1]=50;//PSNR maximo
		percent1=result[0];
		psnr1=result[1];
		float cf1=cf;

		float inc=0;
		//caso A: el percent1 es mayor de lo esperado
		if (MODE.equals("HOMO")) percent=percent*100f;
		if (percent1>=percent)
		{
			System.out.println("caso A percent1 ("+percent1+") > percent ("+percent+") THEN inc="+inc);
			if (!MODE.equals("HOMO")) inc=-0.01f;
			else inc=0.1f;
		}
		else 
		{//caso b
			if (!MODE.equals("HOMO")) inc=+0.01f;
			else inc=-0.1f;
			//inc=+0.01f;
			System.out.println("caso B percent1 < percent THEN inc="+inc);
		}
		cf+=inc;
		boolean condicion=false;
		//while (percent1>=percent && cf>=0)


		float cf2=cf1;
		//cf=0;
		while (condicion==false)
		{



			percent2=percent1;
			psnr2=psnr1;
			cf2=cf1;
			System.out.println("-------------------------------------------------------  next cf:"+cf);
			System.out.println("target:"+percent);
			//float[] 
			//result=getPercentAndPSNR( path_img,  cf);
			
			//old strategy
			/*
			compress(path_img,cf, img,true);
			result=play(path_img,img);
			*/
			//renew
			//renew strategy
			//LHE.FrameCompressor fc=new LHE.FrameCompressor(1);
			fc.DEBUG=false;
			fc.loadFrame(path_img);
			fc.compressFrame(cf);
			
			//LHE.FramePlayer fp=new LHE.FramePlayer();
			fp.img=fc.img;
			fp.grid=fc.grid;
			result=fp.playFrame( path_img);
			//--end renew
			
			if (result[1]==0) result[1]=50;
			if (result[0]!=percent1 || result[1]!=psnr1)
			{percent1=result[0];
			psnr1=result[1];
			cf1=cf;
			}
			//cf+=inc;//-=0.1f;
			//if (cf<0) cf=0;
			//wait 200ms for hard drive . Operating System must flush pending data
			try{
				//Thread.sleep(500);
				Thread.sleep(300);
			}catch(Exception e){}

			if (percent<=percent1 && percent>=percent2 && percent2!=200 && cf>=0) condicion=true;
			else if  (percent>=percent1 && percent<=percent2 && percent2!=200 && cf>=0) condicion=true;
			else if  (cf<=0) condicion=true;

			System.out.println("  tested cf="+(float)cf+ "   cf1:"+cf1+"  cf2:"+cf2);
			cf+=inc;//-=0.1f;
			if (cf<0) cf=0;
		}
		//now we have two values for interpolation or extrapolation
		System.out.println(" cf is "+cf);
		CF_ANT=cf;
		float resultado=0;
		if (percent2==percent1) resultado=psnr1;
		else{

			float alfa=(psnr2-psnr1)/(percent2-percent1);
			resultado=psnr1+alfa*(percent-percent1);
			float alfacf=(cf2-cf1)/(percent2-percent1);
			cf=cf1+alfacf*(percent-percent1);
			CF_ANT=cf;
		}
		System.out.println(" ");
		System.out.println("  RESULTADO INTERPOLADO %:"+percent+" ="+resultado+" dB    cf:"+cf);

		return resultado;





	}
	//************************************************************************************
	public float[] getPercentAndPSNR( String path_img, float cf)
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
		System.out.println(" computing PR metrics...");
		img.grid.computeMetrics();

		//img.grid.printPRstats("./img/PRstats.txt");
		//MODE=new String("");

		if (!MODE.equals("HOMO")) 
			img.grid.fromPRtoPPP(grid.compression_factor);
		//img.grid.fromPRtoPPP_NORMAL(grid.compression_factor);
		else 
		{
			float ratio=cf;//already computed in getPSNR()
			grid.setPPPHomogeneousDownsamplingRatio(ratio);

			//System.out.println("homo");
			//if (2>1) System.exit(0);

		}
		//--------------------------------------------------

		//downsampling
		System.out.print(" downsampling...");
		Block.img=img;
		for ( int y=0 ; y<img.grid.number_of_blocks_V;y++)
		{
			for ( int x=0 ; x<img.grid.number_of_blocks_H;x++)

			{
 //System.out.println(" block:"+x+",+y");
				Block bi=img.grid.bl[y][x];
				bi.pppToRectangleShape();
				bi.downsampleBlock(true);
				// bi.interpolateBoundaries(img.boundaries_YUV,img.downsampled_YUV);
				//	bi.downsampleBoundaries(true,img.boundaries_YUV,img.boundaries_YUV);

			}

		}
		System.out.println(" ok");
		//---------------------------------------------------------
		if (DEBUG)
		{
			if (!MODE.equals("HOMO"))
				img.YUVtoBMP("./img/downsampled_lena.bmp",img.downsampled_YUV[0]);
			else img.YUVtoBMP("./img/downsampled_lena_homo.bmp",img.downsampled_YUV[0]);
		}	


		System.out.print(" interpolating...");
		//interpolation
		//---------------------------------------------------
		for ( int y=0 ; y<img.grid.number_of_blocks_V;y++)
		{
			for ( int x=0 ; x<img.grid.number_of_blocks_H;x++)

			{
				Block bi=img.grid.bl[y][x];
			bi.interpolateBilinealV(img.downsampled_YUV,img.intermediate_interpolated_YUV);
				//bi.interpolateV(false,img.downsampled_YUV,img.intermediate_interpolated_YUV);
			//	bi.interpolateNeighbourV(img.downsampled_YUV,img.intermediate_interpolated_YUV);

			}
		}
		System.out.println(" ok");
		
		if (DEBUG) img.YUVtoBMP("./img/interV.bmp",img.intermediate_interpolated_YUV[0]);
		for ( int y=0 ; y<img.grid.number_of_blocks_V;y++)
		{
			for ( int x=0 ; x<img.grid.number_of_blocks_H;x++)

			{
				Block bi=img.grid.bl[y][x];
				bi.interpolateBilinealH(img.intermediate_interpolated_YUV,img.interpolated_YUV);
				//bi.interpolateNeighbourH(img.intermediate_interpolated_YUV,img.interpolated_YUV);
			}
		}
		System.out.println(" interpolating gaps...");
		System.out.println("   interpolating gaps H...");
		if (DEBUG) img.YUVtoBMP("./img/interH.bmp",img.interpolated_YUV[0]);
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
			System.out.println("   interpolating gaps V...");
			for ( int y=0 ; y<img.grid.number_of_blocks_V;y++)
			{
				for ( int x=0 ; x<img.grid.number_of_blocks_H;x++)

				{
					//System.out.println("   interpolating gaps V ["+x+"]["+y+"]");
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
		System.out.println(" interpolation of gaps ok");
		//---------------------------------------------------
		//saving file
		if (!MODE.equals("HOMO"))
			img.YUVtoBMP("./img/interpolated.bmp",img.interpolated_YUV[0]);
		else
			img.YUVtoBMP("./img/interpolated_homo.bmp",img.interpolated_YUV[0]);
		//img.interpolatedYUVtoBMP("./img/interpolated_lena.bmp");

		float percent=grid.getNumberOfDownsampledPixels();
		percent=100f*percent/(img.width*img.height);
		result[0]=percent;

		//---------------------------------------------------
		//calculating psnr
		double psnr=PSNR.printPSNR(path_img, "./img/interpolated.bmp");
		System.out.println(" PSNR:"+psnr+"    percent:"+percent+"           file:"+path_img);
		result[1]=(float)psnr;

		return result;

	}
	//*************************************************************************
	/**
	 * this function loads the conversion table from percentage to compression factor
	 * 
	 */
	public void loadConversionTable()
	{
		tabla[0]=0.0f;
		tabla[1]=0.0f;
		tabla[2]=0.4f;
		tabla[3]=0.5378937f;
		tabla[4]=0.59701455f;
		tabla[5]=0.64269173f;
		tabla[6]=0.6731134f;
		tabla[7]=0.7022789f;
		tabla[8]=0.7348014f;
		tabla[9]=0.7543773f;
		tabla[10]=0.7784053f;
		tabla[11]=0.79426396f;
		tabla[12]=0.8155117f;
		tabla[13]=0.83016294f;
		tabla[14]=0.84678876f;
		tabla[15]=0.85694605f;
		tabla[16]=0.8778514f;
		tabla[17]=0.88661474f;
		tabla[18]=0.89976704f;
		tabla[19]=0.90973157f;
		tabla[20]=0.9242367f;
		tabla[21]=0.9331481f;
		tabla[22]=0.9469764f;
		tabla[23]=0.958918f;
		tabla[24]=0.96598613f;
		tabla[25]=0.978477f;
		tabla[26]=0.98879606f;
		tabla[27]=0.99748856f;
		tabla[28]=1.0102568f;
		tabla[29]=1.0207045f;
		tabla[30]=1.032589f;
		tabla[31]=1.0496538f;
		tabla[32]=1.062331f;
		tabla[33]=1.0727216f;
		tabla[34]=1.0880578f;
		tabla[35]=1.1014465f;
		tabla[36]=1.1107638f;
		tabla[37]=1.123708f;
		tabla[38]=1.1358336f;
		tabla[39]=1.1486772f;
		tabla[40]=1.1560276f;
		tabla[41]=1.1640393f;
		tabla[42]=1.1766534f;
		tabla[43]=1.1919882f;
		tabla[44]=1.1982985f;
		tabla[45]=1.2078973f;
		tabla[46]=1.2249894f;
		tabla[47]=1.2303311f;
		tabla[48]=1.236993f;
		tabla[49]=1.24838f;
		tabla[50]=1.258347f;
		tabla[51]=1.266312f;
		tabla[52]=1.2761617f;
		tabla[53]=1.2872934f;
		tabla[54]=1.2934122f;
		tabla[55]=1.3006483f;
		tabla[56]=1.3113868f;
		tabla[57]=1.3191463f;
		tabla[58]=1.3260832f;
		tabla[59]=1.3364913f;
		tabla[60]=1.3547909f;
		tabla[61]=1.3709114f;
		tabla[62]=1.3872771f;
		tabla[63]=1.4118905f;
		tabla[64]=1.4217594f;
		tabla[65]=1.432612f;
		tabla[66]=1.4444156f;
		tabla[67]=1.4650196f;
		tabla[68]=1.4711701f;
		tabla[69]=1.4852989f;
		tabla[70]=1.5038551f;
		tabla[71]=1.5204974f;
		tabla[72]=1.5349523f;
		tabla[73]=1.550543f;
		tabla[74]=1.5673906f;
		tabla[75]=1.575899f;
		tabla[76]=1.5956541f;
		tabla[77]=1.6046178f;
		tabla[78]=1.6110612f;
		tabla[79]=1.6176966f;
		tabla[80]=1.6312203f;
		tabla[81]=1.6539882f;
		tabla[82]=1.6598699f;
		tabla[83]=1.6716441f;
		tabla[84]=1.6796416f;
		tabla[85]=1.7024566f;
		tabla[86]=1.7189964f;
		tabla[87]=1.7459736f;
		tabla[88]=1.7767988f;
		tabla[89]=1.7852225f;
		tabla[90]=1.8149711f;
		tabla[91]=1.8572156f;
		tabla[92]=1.8760521f;
		tabla[93]=1.9070567f;
		tabla[94]=1.9222002f;
		tabla[95]=1.9503284f;
		tabla[96]=1.9607618f;
		tabla[97]=1.9913905f;
		tabla[98]=2.0161982f;
		tabla[99]=2.0345511f;
		tabla[100]=2.0545511f;

		/*
				System.out.println("");
				for (int i=0;i<101;i++)
					System.out.print(tabla[i]+";");
				System.out.println("");
		 */
	}
	//************************************************************************************************
	/**
	 * this function creates the conversionTable based on the set of files stored in a given directory
	 * the conversion table maps from percentage to compressionfactor			
	 */
	//public void Percent2CF()
	public void computeConversionTable()
	{
		/*
		//ElasticDownsamplingTester ed=new ElasticDownsamplingTester();
		String file=new String("../debug/BN-kodim15.bmp");
		//String file=new String("./img/Cartoons_8.bmp");
		String path_img=new String(file);
		 */

		System.out.println ("Type directory name:");	
		Scanner teclado = new Scanner (System.in);		
		String directorio =  teclado.next();
		teclado.close();

		//read directory
		File file = new File(directorio);
		if (!file.exists()) {
			System.out.println("El directorio no existe");
			System.exit(0);
		}
		String [] ficherosEnDirectorio = file.list();
		for (int i=0;i<ficherosEnDirectorio.length;i++) {
			//System.out.println(ficherosEnDirectorio[i]);
		}


		ImgUtil img=new ImgUtil();
		//for each file
		//primero pasamos el file a yuvbnç

		String[] rows=new String[ficherosEnDirectorio.length];
		for (int j=0;j<101;j++)
		{
			tabla[j]=0;
		}
		for (int i=0;i<ficherosEnDirectorio.length;i++)
		{
			System.out.println(" processing file "+ficherosEnDirectorio[i]+" ...");
			img.BMPtoYUV(directorio+"/"+ficherosEnDirectorio[i]);
			String bn_file=new String("../debug/BN-"+ficherosEnDirectorio[i]);
			img.YUVtoBMP(bn_file,img.YUV[0]);

			//ahora generamos row
			CF_ANT=0;


			//start at 2%
			for (int j=2;j<101;j++)
			{
				//getPSNR(path_img,j);
				getPSNR(bn_file,j);
				tabla[j]+=CF_ANT;
				//System.out.println("tabla["+i+"]="+tabla[i]+"f;"+ CF_ANT);
			}
		}//for directorio
		for (int i=0;i<101;i++)
		{
			System.out.println("tabla["+i+"]="+(tabla[i]/(float)ficherosEnDirectorio.length)+"f;");
			//System.out.println("tabla["+i+"]="+tabla[i]/(float)ficherosEnDirectorio.length+"f;");

		}
		System.out.println("");
		for (int i=0;i<101;i++)
			System.out.print(tabla[i]+";");
		System.out.println("");

	}

	//*********************************************************************
	public void createImageSoft()
	{

		ImgUtil img=new ImgUtil();
		img.BMPtoYUV("./img/block32.bmp");
		int inc=20;
		for (int y=0;y<32;y++)
			for (int x=0;x<32;x++)
			{
				int left=0;
				if (x==0) {left=0;inc=20;}
				else left=img.YUV[0][y*img.width+x-1];
				img.YUV[0][y*img.width+x]=left+ inc;

				if (img.YUV[0][y*img.width+x]>=255) {img.YUV[0][y*img.width+x]=255;inc=-inc;}
				if (img.YUV[0][y*img.width+x]<=0) {img.YUV[0][y*img.width+x]=0;inc=-inc;}



			}

		img.YUVtoBMP("./img/soft32.bmp", img.YUV[0]);

		//creation of grid
		Grid grid;
		grid = new Grid();
		grid.createGrid(img.width, img.height,1,Block.MAX_PPP);
		img.grid=grid;

		//ahora medimos sus metricas
		LHEquantizer lhe=new LHEquantizer();

		lhe.img=img;
		lhe.init();
		lhe.quantizeOneHopPerPixel(img.hops[0],img.LHE_YUV[0]);
		img.saveHopsToTxt("./img/hops.txt");
		img.YUVtoBMP("./img/soft.bmp",img.LHE_YUV[0]);
		PRblock.img=img;
		img.grid.computeMetrics();
		if (1<2) System.exit(0);
	}

	//**********************************************************************
	public void createImageHard()
	{

		ImgUtil img=new ImgUtil();
		img.BMPtoYUV("./img/block32.bmp");
		int inc=20;
		for (int y=0;y<32;y++)
			for (int x=0;x<32;x++)
			{
				int left=0;
				if (x==0) {left=0;inc=20;}
				else left=img.YUV[0][y*img.width+x-1];
				if (x<12) img.YUV[0][y*img.width+x]=0;
				if (x>=12 && x<16) img.YUV[0][y*img.width+x]=255;
				if (x>=16) img.YUV[0][y*img.width+x]=0;
				if (x>=20) img.YUV[0][y*img.width+x]=255;

				if (img.YUV[0][y*img.width+x]>=255) {img.YUV[0][y*img.width+x]=255;inc=-inc;}
				if (img.YUV[0][y*img.width+x]<=0) {img.YUV[0][y*img.width+x]=0;inc=-inc;}



			}

		img.YUVtoBMP("./img/hard32.bmp", img.YUV[0]);

		//creation of grid
		Grid grid;
		grid = new Grid();
		grid.createGrid(img.width, img.height,1,Block.MAX_PPP);
		img.grid=grid;

		//ahora medimos sus metricas
		LHEquantizer lhe=new LHEquantizer();

		lhe.img=img;
		lhe.init();
		lhe.quantizeOneHopPerPixel(img.hops[0],img.LHE_YUV[0]);
		img.saveHopsToTxt("./img/hops.txt");
		img.YUVtoBMP("./img/soft.bmp",img.LHE_YUV[0]);
		PRblock.img=img;
		img.grid.computeMetrics();
		if (1<2) System.exit(0);
	}
	//*********************************************************************
	public void createImageNoise32()
	{

		ImgUtil img=new ImgUtil();
		img.BMPtoYUV("./img/block32.bmp");
		int inc=20;
		for (int y=0;y<32;y++)
			for (int x=0;x<32;x++)
			{
				img.YUV[0][y*img.width+x]=(int)(255*Math.random());




			}

		img.YUVtoBMP("./img/noise32.bmp", img.YUV[0]);

		//creation of grid
		Grid grid;
		grid = new Grid();
		grid.createGrid(img.width, img.height,1,Block.MAX_PPP);
		img.grid=grid;

		//ahora medimos sus metricas
		LHEquantizer lhe=new LHEquantizer();

		lhe.img=img;
		lhe.init();
		lhe.quantizeOneHopPerPixel(img.hops[0],img.LHE_YUV[0]);
		img.saveHopsToTxt("./img/hops.txt");
		img.YUVtoBMP("./img/soft.bmp",img.LHE_YUV[0]);
		PRblock.img=img;
		img.grid.computeMetrics();
		if (1<2) System.exit(0);
	}
	//*****************************************************************
	/**
	 * this function creates a noise image for experimental purposes
	 */
	public void createNoiseImage()
	{

		ImgUtil img=new ImgUtil();
		img.BMPtoYUV("./img/lena.bmp");

		for (int y=0;y<512;y++)
			for (int x=0;x<512;x++)
				img.YUV[0][y*img.width+x]=(int)(Math.random()*255f);
		//img.YUV[0][y*img.width+x]=128-32+(int)(Math.random()*64);

		img.YUVtoBMP("./img/noise.bmp", img.YUV[0]);

		//creation of grid
		Grid grid;
		grid = new Grid();
		grid.createGrid(img.width, img.height,1,Block.MAX_PPP);
		img.grid=grid;

		//ahora medimos sus metricas
		LHEquantizer lhe=new LHEquantizer();

		lhe.img=img;
		lhe.init();
		lhe.quantizeOneHopPerPixel(img.hops[0],img.LHE_YUV[0]);
		//img.saveHopsToTxt("./img/hops.txt");
		//img.YUVtoBMP("./img/LHE_YUV.bmp",img.LHE_YUV[0]);
		PRblock.img=img;
		img.grid.computeMetrics();
		if (1<2) System.exit(0);
	}
//******************************************************************************************
 public void compress( String path_img, float cf, ImgUtil img, boolean LHE)
	{
		float[] result=new float[2];
		//-----------------------------------------------
		//load image into memory, transforming into YUV format
		//final ImgUtil img=new ImgUtil();
		if (img==null)  img=new ImgUtil();
		img.BMPtoYUV(path_img);

		
		//-----------------------------------------------
		//creation of grid and computation of PPP_MAX
		Grid grid;
		grid = new Grid();
		grid.createGrid(img.width, img.height,cf,Block.MAX_PPP);
		img.grid=grid;

		//-----------------------------------------------
		//First LHE quantization
		LHEquantizer lhe=new LHEquantizer();
		lhe.img=img;
		//lhe.init();
		//lhe.initK();
		//lhe.initGeom(2.5f);//rmax=2.5
		lhe.initGeomR();
		//codificando por bloques en lugar de globalmente, esta parte seria paralelizable N2-->2N+1
		//lhe.quantizeOneHopPerPixel(img.hops[0],img.LHE_YUV[0]);
		lhe.quantizeOneHopPerPixel_R(img.hops[0],img.LHE_YUV[0]);
		//img.saveHopsToTxt("./img/hops.txt");
		//img.YUVtoBMP("./img/LHE_YUV.bmp",img.LHE_YUV[0]);
		
		//-------------------------------------------------
		//PR metrics and PPP assignment
		PRblock.img=img;
		System.out.println(" computing PR metrics...");
		img.grid.computeMetrics();//compute metrics of all Prblocks, equalize & quantize

		
		//average PR
		img.grid.computePRavgPerBlock();
		
		
		//img.grid.printPRstats("./img/PRstats.txt");
		//MODE=new String("");

		if (!MODE.equals("HOMO")) 
			img.grid.fromPRtoPPP(grid.compression_factor);
		//img.grid.fromPRtoPPP_NORMAL(grid.compression_factor);
		else 
		{
			float ratio=cf;//already computed in getPSNR()
			grid.setPPPHomogeneousDownsamplingRatio(ratio);
		}
		//--------------------------------------------------
 System.out.println(" downsampling...");
		//downsampling. esta parte es paralelizable N2-->2N+1
		Block.img=img;
		for ( int y=0 ; y<img.grid.number_of_blocks_V;y++)
		{
			for ( int x=0 ; x<img.grid.number_of_blocks_H;x++)

			{

				
				
				
				
				//take the block
				Block bi=img.grid.bl[y][x];
				//System.out.println(" block: x="+x+", y="+y);
				
				//adapt corner's PPP to rectangle shape
				bi.pppToRectangleShape();
				//downsampling the block
				//bi.computeDownsampledLengths();
				bi.downsampleBlock(true);
				//lets go for 2nd LHE
				
				//lhe.quantizeDownsampledBlock(bi, img.hops[0],img.downsampled_LHE_YUV[0], img);
				//img.downsampled_LHE_YUV[0]=img.downsampled_YUV[0];
				
				
				
				//antes de cuantizar el bloque calculo sus boundaries pues los necesito. 
				//suponemos que ya estan interpolados.
				bi.downsampleBoundariesH_FIX(img.boundaries_YUV,img.boundaries_inter_YUV);//, img.grid.bl[y+1][x]);
				bi.downsampleBoundariesV_FIX(img.boundaries_YUV,img.boundaries_inter_YUV);//,img.grid.bl[y][x+1]);
				
				
				//antes de cuantizar , establecemos rmax
				/*
				float rmax=2.5f;
				if (bi.PRavg<=0.25) rmax=1.5f;
				else if (bi.PRavg<=0.5) rmax=1.75f;//1,0.5,0.5,0.5
				else if (bi.PRavg<=0.75) rmax=2f;//1,1,0.5,0.5
				else if (bi.PRavg<0.85) rmax=2.5f;//1,1,1,0.5
				else  rmax=2.75f;
				rmax=2.5f;
				if (bi.PRavg==1.0) rmax=3f;//bordes abruptos, iconos, ruido 
				else if (bi.PRavg>=0.625) rmax=2.5f;//bordes 1,1,1,0.5
				else rmax=2f;//suaves
				lhe.initGeom(rmax);
				*/
				//LHE=false;dd
				if (LHE==true)
				 // lhe.quantizeDownsampledBlock(bi, img.hops[0],img.downsampled_LHE_YUV[0], img.downsampled_YUV[0],img.boundaries_YUV[0] );
					 lhe.quantizeDownsampledBlock_R(bi, img.hops[0],img.downsampled_LHE_YUV[0], img.downsampled_YUV[0],img.boundaries_YUV[0] );
				else
				  img.downsampled_LHE_YUV[0]=img.downsampled_YUV[0];
				
                //Block bi=img.grid.bl[y][x];
				
				Qmetrics.PSNRutil my_psnr=new Qmetrics.PSNRutil() ;
				/*
				float mse=my_psnr.getMSE(img.downsampled_LHE_YUV[0],img.downsampled_YUV[0],bi.xini,bi.downsampled_xfin,bi.yini,bi.downsampled_yfin,img.width);
			//	System.out.println(" block:"+y+","+x+":"+mse);
				float pravg=(img.grid.prbl[y][x].PRx+img.grid.prbl[y][x].PRy+
						     img.grid.prbl[y][x+1].PRx+img.grid.prbl[y][x+1].PRy+
						     img.grid.prbl[y+1][x].PRx+img.grid.prbl[y+1][x].PRy+
						     img.grid.prbl[y+1][x+1].PRx+img.grid.prbl[y+1][x+1].PRy)/8f;
				//if (mse>20)
				System.out.println(" block:"+y+","+x+"     PRavg:"+pravg+"  MSE:"+mse +"  "+img.grid.prbl[y][x].PRx+","+img.grid.prbl[y][x].PRy+","+img.grid.prbl[y][x+1].PRx+","+img.grid.prbl[y][x+1].PRy+","+img.grid.prbl[y+1][x].PRx+","+img.grid.prbl[y+1][x].PRy+","+img.grid.prbl[y+1][x+1].PRx+","+img.grid.prbl[y+1][x+1].PRy);
				//if (pravg>0.7f) bi.kinint=35;
				
				
				//if (pravg<=0.25f) bi.kinint=40;
				//boa
				//lhe.quantizeDownsampledBlock_k(bi, img.hops[0],img.downsampled_LHE_YUV[0], img.downsampled_YUV[0],img.boundaries_YUV[0] );
				*/
				//ahora hacemos los boundaries para poder seguir. usando como origen no downsampled_YUV sino  downsampled_LHE_YUV
				//bi.interpolateBoundariesVlinear(img.boundaries_inter_YUV,img.downsampled_LHE_YUV);
				//bi.interpolateBoundariesHlinear(img.boundaries_inter_YUV,img.downsampled_LHE_YUV);
				
				bi.interpolateBoundariesVneighbour(img.downsampled_LHE_YUV,img.boundaries_inter_YUV);
				//bi.interpolateBoundariesVneighbour(img.downsampled_YUV,img.boundaries_inter_YUV);
				
				
				
				bi.interpolateBoundariesHneighbour(img.downsampled_LHE_YUV,img.boundaries_inter_YUV);
				//bi.interpolateBoundariesHneighbour(img.downsampled_YUV,img.boundaries_inter_YUV);
				
			//	bi.interpolateNeighbourV(img.downsampled_YUV,img.boundaries_inter2_YUV);
			//    bi.interpolateNeighbourH(img.boundaries_inter2_YUV,img.boundaries_inter_YUV);
			   // hola
			}
System.out.print(".");
		}
		System.out.println(" donwsampling fisnihed");
		
		
		//salvamos los boundaries. la imagen contiene los interpolados y los escalados
		//-------------------------
		//if (DEBUG) 
		img.YUVtoBMP("./img/boundariesLHE.bmp",img.boundaries_YUV[0]);
		img.YUVtoBMP("./img/boundaries_inter_LHE.bmp",img.boundaries_inter_YUV[0]);
		//voy a ver que se ha generado por 2nd LHE
		
				img.YUVtoBMP("./img/downsampled_LHE_YUV.bmp",img.downsampled_LHE_YUV[0]);
				img.YUVtoBMP("./img/downsampled_YUV.bmp",img.downsampled_YUV[0]);
				
		
		//salvamos la imagen escalada
		//---------------------------------------------------------
		if (DEBUG)
			if (!MODE.equals("HOMO"))
			img.YUVtoBMP("./img/downsampled_image.bmp",img.downsampled_YUV[0]);
			else 
			img.YUVtoBMP("./img/downsampled_image_homo.bmp",img.downsampled_YUV[0]);

		//ahora salvo la info de malla
		//---------------------------------------------------------
		if (DEBUG) img.grid.saveGridTXT("./img/grid.txt");	
			
			System.out.println(" imagen comprimida");
			
		
		//ahora llamamos al encoder para convertir hops en bits	
		//--------------------------------------------------------
			
		
			
			
	}//end funcion

	
	public float[] play(String path_img, ImgUtil img)
	{
		float[] result=new float[2];
		//-----------------------------------------------
		//debo leer la imagen

		//ahora llamamos al decoder
		//--------------------------------------------------------

		//ahora interpretamos los hops+ la malla
		//---------------------------------------------------------	
		//cada vez que interpretamos un bloque, calculamos sus boundaries para interpretar los siguientes
		
		
		
		
		//ahora ya tenemos a downsampled LHE. procedemos calcular el PSNR de cada bloque escalado
		//---------------------------------------------------------------------------------------
		Qmetrics.PSNRutil my_psnr=new Qmetrics.PSNRutil() ;
		for ( int y=0 ; y<img.grid.number_of_blocks_V;y++)
		{
			for ( int x=0 ; x<img.grid.number_of_blocks_H;x++)

			{
				Block bi=img.grid.bl[y][x];
				
				float mse=my_psnr.getMSE(img.downsampled_LHE_YUV[0],img.downsampled_YUV[0],bi.xini,bi.downsampled_xfin,bi.yini,bi.downsampled_yfin,img.width);
			//	System.out.println(" block:"+y+","+x+":"+mse);
				float pravg=(img.grid.prbl[y][x].PRx+img.grid.prbl[y][x].PRy+
						     img.grid.prbl[y][x+1].PRx+img.grid.prbl[y][x+1].PRy+
						     img.grid.prbl[y+1][x].PRx+img.grid.prbl[y+1][x].PRy+
						     img.grid.prbl[y+1][x+1].PRx+img.grid.prbl[y+1][x+1].PRy)/8f;
				//if (mse>20)
				//System.out.println(" block:"+y+","+x+"     PRavg:"+pravg+"  MSE:"+mse +"  "+img.grid.prbl[y][x].PRx+","+img.grid.prbl[y][x].PRy+","+img.grid.prbl[y][x+1].PRx+","+img.grid.prbl[y][x+1].PRy+","+img.grid.prbl[y+1][x].PRx+","+img.grid.prbl[y+1][x].PRy+","+img.grid.prbl[y+1][x+1].PRx+","+img.grid.prbl[y+1][x+1].PRy);
				//if (pravg<0.5f)	bi.kinint=40;
			}
		}
	
				

		//ahora ya tenemos a downsampled LHE. procedemos a reescalar
		//-------------------
	 
		//---------------------------------------------------
		for ( int y=0 ; y<img.grid.number_of_blocks_V;y++)
		{
			for ( int x=0 ; x<img.grid.number_of_blocks_H;x++)

			{
				Block bi=img.grid.bl[y][x];
			bi.interpolateBilinealV(img.downsampled_LHE_YUV,img.intermediate_interpolated_YUV);
				
			//	bi.interpolateNeighbourV(img.downsampled_YUV,img.intermediate_interpolated_YUV);

			}
		}
		if (DEBUG) img.YUVtoBMP("./img/interV.bmp",img.intermediate_interpolated_YUV[0]);
		for ( int y=0 ; y<img.grid.number_of_blocks_V;y++)
		{
			for ( int x=0 ; x<img.grid.number_of_blocks_H;x++)

			{
				Block bi=img.grid.bl[y][x];
				bi.interpolateBilinealH(img.intermediate_interpolated_YUV,img.interpolated_YUV);
				//bi.interpolateNeighbourH(img.intermediate_interpolated_YUV,img.interpolated_YUV);
			}
		}

		if (DEBUG) img.YUVtoBMP("./img/interH.bmp",img.interpolated_YUV[0]);
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
		if (!MODE.equals("HOMO"))
			img.YUVtoBMP("./img/play.bmp",img.interpolated_YUV[0]);
		else
			img.YUVtoBMP("./img/play_homo.bmp",img.interpolated_YUV[0]);
		//img.interpolatedYUVtoBMP("./img/interpolated_lena.bmp");

		float percent=img.grid.getNumberOfDownsampledPixels();
		percent=100f*percent/(img.width*img.height);
		result[0]=percent;

		//---------------------------------------------------
		//calculating psnr
		
		double psnr=PSNR.printPSNR(path_img, "./img/play.bmp");
		System.out.println(" PSNR:"+psnr+"    percent:"+percent+"           file:"+path_img);
		System.out.println(" --------sss-------------------------------------------------------");
		result[1]=(float)psnr;

		
   	    float mse=my_psnr.getMSE(img.interpolated_YUV[0],img.YUV[0],0,img.width-1,0,img.height-1,img.width);
		double psnr_mse=my_psnr.getPSNR(mse);
   	    System.out.println("total mse:"+mse+"   psnr:"+psnr_mse);
		
		
		
		
		return result;

		
		
		
	}
	
	//*************************************************************************
 public void play( String path_img)
	{
	 //lee info extrae:
	 //hops : si estan disponibles
	 //imagen down: debe recostruir a partir de hops. si aun no estan los hops, extraera esto
	 //info malla
	 
	 //decodifica hops
	 //interpreta malla y asi sabe el tamaño de cada bloque down
	 //interpreta hops bloque a bloque, haciendo boundaries para interpretar los siguientes bloques
	 //una vez que tenemos imagen down, interpola
	 
	 
	 
	 
	}
	
}
