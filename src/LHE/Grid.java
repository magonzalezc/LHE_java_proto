package LHE;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;


/**
 * 
 * @author Jose Javier Garcia Aranda 
 * 2014
 */
public class Grid {

public boolean DEBUG=false;
	
	//compression factor: set at creation
	public float compression_factor;

	//magic number is the number of blocks in the larger side
	public float blocks_of_larger_image_side=32;

	//array of blocks
	public Block[][] bl;//block list
	public int number_of_blocks_H;
	public int number_of_blocks_V;

	//array of PRblocks
	public PRblock[][] prbl;//preceptual relevance block list

	//block size of the sides ( floating value). 
	//real blocks are integer (but not necessarily identical)  
	public float sizeh;
	public float sizev;

	//max, min number of blocks in the larger side of the image
	public final static int MAX_BLOCKS=32;//32;//32;//32;//32;//1;//32;//1;//32;//32;//32;//32;//32;//64;//32;//32;//32;//32;//16;//
	//public final static int MIN_BLOCKS=32;
	public final static int MIN_LEN_BLOCK=16;//16;//16;//16;//16; hace que haya menos de 32 bloques si width<512


	/**
	 * constructor
	 * grid is created based on image dimensions and magic number
	 * magic number is the number of blocks in the larger side 
	 * 
	 * @param image_width
	 * @param image_high
	 */
	public Grid()//float comp_factor)
	{
		/*
		//comp factor is a floating value from 0 to 10
		compression_factor=(float)Math.pow(2,comp_factor)/100f; 



		//computation of  number of blocks
		blocks_of_larger_image_side=MIN_BLOCKS+(int)((1f-comp_factor/10f)*(MAX_BLOCKS-MIN_BLOCKS));	

		System.out.println(" number of blocks at larger image side ="+blocks_of_larger_image_side);
		//magic_number=3;
		// 32 seria  el limite para pppmax=16 (32*16=512) osea 1 pix por bloque. eso no va, al menos hacen falta dos pix

	//	blocks_of_larger_image_side=10;//6;//6;//10;;
		 */


	}
	//*************************************************************************************************
	/**
	 * create an array of blocks. 
	 * blocks are not identical, unless image width and high are divisible by the block length 
	 * vertical size of blocks may be different from horizontal size
	 * the grid must cover the entire image
	 * the magic number is configurable (number of blocks in larger side) , but 32 is default value
	 */
	
	//******************************************************************************
	public int getNumberOfDownsampledPixels()
	{
		int down_counter=0;
		int counter=0;
		for ( int y=0 ; y<number_of_blocks_V;y++)
		{
			for ( int x=0 ; x<number_of_blocks_H;x++)

			{
				Block b=bl[y][x];
				down_counter+=b.lx_sc *b.ly_sc ;
				counter+=b.lx*b.ly;
			}
		}
		float percent=100*(float)down_counter/(float)counter;
		if (DEBUG) System.out.println(" image is downsampled to "+down_counter+" pixels, "+percent+" %");
		return down_counter;
	}
	//*******************************************************************************
	/**
	 * ratio must belong to 1..256 corresponding to 1:1 to 1:256
	 * 
	 * @param ratio
	 */
	public void setPPPHomogeneousDownsamplingRatio(float ratio)
	{
		//ratio=10;
		System.out.println ("Homogeneous downsampling ratio 1:"+ratio);
		
		for ( int y=0 ; y<number_of_blocks_V;y++)
		{
			for ( int x=0 ; x<number_of_blocks_H;x++)

			{
				Block b=bl[y][x];
				for (int coord=0;coord<2;coord++)
					for (int corner=0;corner<4; corner++)
					{
						//b.ppp[coord][corner]=4;
						b.ppp[coord][corner]=(float) Math.sqrt(ratio);

						if (b.ppp[coord][corner]>Block.MAX_PPP) b.ppp[coord][corner]=Block.MAX_PPP;
						else if (b.ppp[coord][corner]<1) b.ppp[coord][corner]=1;
						//System.out.println(" bl["+y+"]["+x+"].ppp["+coord+"]["+corner+"]="+ b.ppp[coord][corner]+"f;");
					}

			}
		}

	}
	//************************************************************************
	public void setBpp()
	{

		fromPRtoPPP(compression_factor);

		//fromPRtoPPP(8);
		if (2>1) return; 

		for ( int y=0 ; y<number_of_blocks_V;y++)
		{
			for ( int x=0 ; x<number_of_blocks_H;x++)

			{
				Block b=bl[y][x];
				for (int coord=0;coord<2;coord++)
					for (int corner=0;corner<4; corner++)
					{
						//b.ppp[coord][corner]=4;
						b.ppp[coord][corner]=1+(float)Math.random()*7f;

						//System.out.println(" bl["+y+"]["+x+"].ppp["+coord+"]["+corner+"]="+ b.ppp[coord][corner]+"f;");

						b.ppp[coord][0]= 1f;
						b.ppp[coord][1]= 8f;;
						b.ppp[coord][2]= 8f;;
						b.ppp[coord][3]= 1f;;

						/*


					b.ppp[0][0]= 2;
					b.ppp[0][1]= 2;
					b.ppp[0][2]= 2;
					b.ppp[0][3]= 2;

					b.ppp[1][0]= 1;
					b.ppp[1][1]= 4;
					b.ppp[1][2]= 4;
					b.ppp[1][3]= 1;
						 */

						/*
					b.ppp[0][0]= 1;
					b.ppp[0][1]= 4;
					b.ppp[0][2]= 1;
					b.ppp[0][3]= 4;

					b.ppp[1][0]= 1;
					b.ppp[1][1]= 1;
					b.ppp[1][2]= 4;
					b.ppp[1][3]= 4;
						 */



					}
				//b.setPreferredDimension();
				//b.pppToRectangleShape();
			}

		}


	}

	public void SetGaussianDsitributionPR(double avg, double stdev)
	{
		
		float totalblocks=(number_of_blocks_V+1)*(number_of_blocks_H+1);
		
	//	float block=0;
		//first we compute metrics at each PRblock
		float numerator=(float)(1f/(stdev*2.5f));
	//	float residuo=0;
		int contablock=0;
		int x=0;
		int y=0;
		float area=0;
		for (float pr=0;pr<=3.0f;pr+=0.01f) //pr never is > threshold
		{
			float dx=0.01f;	
			area+=	dx*numerator*(float)Math.exp(-0.5f*Math.pow((pr-avg)/stdev,2));
		}
		//pr axis

		System.out.println(" area:"+area);// area is always 1
		//area=1.0f;
		float blockvalue=totalblocks/area;//factor per block
		int c=0;
		
		//ahora recorro los bloques a segmentos, mientras recorro pr
		
		for (float pr=0;pr<=3.0;pr+=0.01f)
		{
			//para este valor de pr veamos cuantos elementos debe haber
			float dx=0.01f;
			float darea=dx*numerator*(float)Math.exp(-0.5f*Math.pow((pr-avg)/stdev,2));
			
			
			float n=darea*blockvalue;
			
			float prfinal=0;
			if (pr>0 && pr<=0.5f) prfinal=pr;
			else if (pr>0.5f) prfinal=0.5f;
			
			int nint=(int)(n);//+residuo);
			//residuo=0;//n-nint;
			
			System.out.println(nint);
			//segmento de bloques con el mismo pr
			for (int i=contablock;i<=contablock+nint;i++)
			{
				//System.out.println("x"+x+"  y:"+y+ "max:"+(number_of_blocks_V+1));
				prbl[y][x].PRx=prfinal;
				prbl[y][x].PRy=prfinal;
				
				//System.out.println(prbl[y][x].PRx);
				c++;
				x++;
				if (x==number_of_blocks_H+1){ x=0;y++;}
				if (y==number_of_blocks_V+1){ break;}
			}
			contablock=contablock+nint;
			if (y==number_of_blocks_V+1){ break;}
			
		}
		
		
		
		System.out.println(" bloques ajustados:"+c);
		/*
		
		
		for (int y=0;y<number_of_blocks_V+1;y++)
		{
			for (int x=0;x<number_of_blocks_H+1;x++)
			{
				
				float i=block/totalblocks;
				float num=(float)(1f/(stdev*2.5f));
				//prbl[y][x].PRx=(float)(num)*(float)Math.exp(-0.5f*Math.pow((i-avg)/stdev,2));
				//if (prbl[y][x].PRx>1) prbl[y][x].PRx=1f;
				//if (prbl[y][x].PRx>0.5) prbl[y][x].PRx=0.5f;
				//prbl[y][x].PRy=prbl[y][x].PRx;
				block+=1;
				System.out.println(prbl[y][x].PRx);
			}//x
		}//y
		*/
	}
	
	
	//**************************************************************************************
	/**
	 * this function compute PRmetrics for all PRblocks of the grid
	 * 
	 * first we compute metrics at each PRblock
	 * then we equalize PR histogram
	 * later on, we quantize PR using 4 levels
	 * 
	 */
public void topaPR(float threshold)
{
	for (int y=0;y<number_of_blocks_V+1;y++)
	{
		for (int x=0;x<number_of_blocks_H+1;x++)
		{
			if (prbl[y][x].PRx>threshold) prbl[y][x].PRx=threshold;
			if (prbl[y][x].PRy>threshold) prbl[y][x].PRy=threshold;
		}//x
	}//y
}
//****************************************************************************
	public void computeMetrics()
	{
		//esta no usa lo de almacenar los valores de PR sin cuantizar, util para el video
		//en video se usa la otra. de ese modo se implementa la remanencia
		
		//first we compute metrics at each PRblock
		for (int y=0;y<number_of_blocks_V+1;y++)
		{
			for (int x=0;x<number_of_blocks_H+1;x++)
			{
				prbl[y][x].computePRmetrics();
			}//x
		}//y

		
		
		
		//SetGaussianDsitributionPR(0.29f,0.0474);
		//SetGaussianDsitributionPR(0.45f,0.069f);
		//topaPR(0.5f);
		//SetGaussianDsitributionPR(0.25f,0.23f);
		//expandHistogramPR();
		
		//analisys on PR and decision about best equalization
		//---------------------------------------------------
		//this computes PR average and standard deviation
		if (DEBUG)
		{
		float[] PRstats=new float[3];
		PRstats=computePRavg();
		float PRdev=computePRdev(PRstats[0]);
		
		
		
		System.out.println("--- PR STATS BEFORE EQUALIZATION ---");
		System.out.println("   >PR average:"+PRstats[0]+"    , max:"+PRstats[1]+"    , min:"+PRstats[2]);
		System.out.println("   >PR std deviation:"+PRdev);
		
		}
				
		

		//then we equalize PR histogram
		//------------------------------
		if (DEBUG) System.out.println(" equalizing...");
		
		//expandHistogramPR_02(PRstats);
		
		//expandHistogramPR_03(PRstats[0],PRdev);
		
		//printHistogramPR();
		
		if (DEBUG) System.out.println(" setting upper limit at PR=0.5");
		
		topaPR(0.5f);//llllllllllllllllllllllllllllllll
		
		if (DEBUG) System.out.println(" expanding histogram...");
		
		expandHistogramPR_04();//PRstats[0],PRdev);
		
		
		if (DEBUG)
		{
		float[] PRstats=new float[3];
		PRstats=computePRavg();
		float PRdev=computePRdev(PRstats[0]);
		System.out.println("--- PR STATS  AFTER EQUALIZATION ---");
		System.out.println("   >PR average:"+PRstats[0]+"    , max:"+PRstats[1]+"    , min:"+PRstats[2]);
		System.out.println("   >PR std deviation:"+PRdev);
		}
		//equalizeHistogramPR();

		if (DEBUG) System.out.println(" quantizing...");
		// the last step: quantization of PR
		//-----------------------------------------
		for (int y=0;y<number_of_blocks_V+1;y++)
		{
			for (int x=0;x<number_of_blocks_H+1;x++)
			{
				
				prbl[y][x].quantizeGeometricalPR();
				
			}//x
		}//y

		
		
	}
	//*******************************************************************
	public void expandHistogramPR(float k, float avg)
	{
		for (int y=0;y<number_of_blocks_V+1;y++)
		{
			for (int x=0;x<number_of_blocks_H+1;x++)
			{
				prbl[y][x].PRx= k*(prbl[y][x].PRx-avg)+ avg;
				if (prbl[y][x].PRx>1 )prbl[y][x].PRx=1;
				if (prbl[y][x].PRx<0 )prbl[y][x].PRx=0;
				
				prbl[y][x].PRy= k*(prbl[y][x].PRy-avg)+ avg;
				if (prbl[y][x].PRy>1 )prbl[y][x].PRy=1;
				if (prbl[y][x].PRy<0 )prbl[y][x].PRy=0;
			}
		}
	}
	//*******************************************************************
	public void expandHistogramPR_03(float avg,float dev)
	{
		
		float max=avg+2*dev;
		if (max>1) max=1;
		float min=avg-2*dev;
		if (min<0)min =0;
		
		float current_minPR=min;
		float current_maxPR=max;
		
		System.out.println(" maxPRsigma:"+current_maxPR+"   minPRsigma:"+current_minPR+"   avg:"+avg);
		
		
		for (int y=0;y<number_of_blocks_V+1;y++)
		{

			for (int x=0;x<number_of_blocks_H+1;x++)
			{
				//prbl[y][x].PRx=(new_maxPRx-new_minPRx)*(prbl[y][x].PRx-current_minPRx)/(current_maxPRx-current_minPRx)+new_minPRx;
				
				prbl[y][x].PRx=(prbl[y][x].PRx-current_minPR)/(current_maxPR-current_minPR);
				if (prbl[y][x].PRx<0)prbl[y][x].PRx=0;
				if (prbl[y][x].PRx>1)prbl[y][x].PRx=1;
				
				//prbl[y][x].PRy=(new_maxPRy-new_minPRy)*(prbl[y][x].PRy-current_minPRy)/(current_maxPRy-current_minPRy)+new_minPRy;
				prbl[y][x].PRy=(prbl[y][x].PRy-current_minPR)/(current_maxPR-current_minPR);
				
				if (prbl[y][x].PRy<0)prbl[y][x].PRy=0;
				if (prbl[y][x].PRy>1)prbl[y][x].PRy=1;
				
			}//x

		}//y
	}
	//*******************************************************************
	public void printHistogramPR()
	{
		int[] hist=new int[101];
		for (int y=1;y<number_of_blocks_V;y++)
		{

			for (int x=1;x<number_of_blocks_H;x++)
			{
			hist[(int)(100f*prbl[y][x].PRx)]++;
			hist[(int)(100f*prbl[y][x].PRy)]++;
			
			}
		}
		for (int i=0;i<=100;i++)
		{
			System.out.println(hist[i]);
		}
	}

	
	//*******************************************************************
		public void expandHistogramPR_04()
		{
			
			//lets compute max and min
			//DISTRIBUTION is not gaussian -> 2xsigma is not valid
			/*
			int[] counter=new int[100];
			float min=1;
			float max=0;
			//only consider entire blocks ( not first and last rows/columns)
			
			float cosa=0;
			for (int y=1;y<number_of_blocks_V;y++)
			{

				for (int x=1;x<number_of_blocks_H;x++)
				{
					//prbl[y][x].PRx=0.29f;
					//prbl[y][x].PRy=0.29f;
					
					cosa+=prbl[y][x].PRx;//=0.29f;;
					cosa+=prbl[y][x].PRy;//=0.29f;
					int value=(int)(prbl[y][x].PRx*100);
					//value=29;
					for (int i=0;i<value;i++)
					{
					counter[i]++;	
					}//i
					value=(int)(prbl[y][x].PRy*100);
					//value=29;
					for (int i=0;i<value;i++)
					{
					counter[i]++;	
					}//i
				}//x
			}//y
			
			
			//now counter have the computation
			int total_samples=2*(number_of_blocks_V-1)*(number_of_blocks_H-1);
			//System.out.println ("cosa:"+cosa+ "    samples:"+total_samples+ " cada:"+(cosa/(float)total_samples));
			for (int i=0;i<100;i++)
			{
			//System.out.println("counter["+i+"]="+(float)counter[i]/(float)total_samples);
			}//i
			//set limits into 5% and 95%
			//----------------------------
			
			for (int i=0;i<100;i++)
			{
			float por=(float)counter[i]/(float)total_samples;
			if (por<0.95f) {min=(float)i/100f;break;}	
			//if (por<1f) {min=(float)i/100f;break;}
			}//i
			for (int i=99;i>=0;i--)
			{
			float por=(float)counter[i]/(float)total_samples;
			if (por>0.05f) {max=(float)i/100f;break;}	
			//if (por>0.00f) {max=(float)i/100f;break;}
			}//i
			//----------------------------
			
			//MAXIMOS Y MINIMOS DE PR ANTES DE EXPANDIR
			//en video deben ser forzados
			
			//min=0.10f;
			//max=0.5f;
			*/
			float min=0.2f;//0.2f;//0.2f;//125f;
			float max=0.5f;//0.5f;

			float current_minPR=min;
			//if (min<0.1f) min=0.1f;
			float current_maxPR=max;
			
			//System.out.println(" maxPR90:"+current_maxPR+"   minPR10:"+current_minPR);
			//System.out.println(" maxPR:"+current_maxPR+"   minPR:"+current_minPR);
			
			for (int y=0;y<number_of_blocks_V+1;y++)
			{

				for (int x=0;x<number_of_blocks_H+1;x++)
				{
					//prbl[y][x].PRx=(new_maxPRx-new_minPRx)*(prbl[y][x].PRx-current_minPRx)/(current_maxPRx-current_minPRx)+new_minPRx;
					
					//prbl[y][x].PRx=0.29f;
					//prbl[y][x].PRy=0.29f;
					
					prbl[y][x].PRx=(prbl[y][x].PRx-current_minPR)/(current_maxPR-current_minPR);
					if (prbl[y][x].PRx<0)prbl[y][x].PRx=0;
					if (prbl[y][x].PRx>1)prbl[y][x].PRx=1;
					
					//prbl[y][x].PRy=(new_maxPRy-new_minPRy)*(prbl[y][x].PRy-current_minPRy)/(current_maxPRy-current_minPRy)+new_minPRy;
					prbl[y][x].PRy=(prbl[y][x].PRy-current_minPR)/(current_maxPR-current_minPR);
					
					if (prbl[y][x].PRy<0)prbl[y][x].PRy=0;
					if (prbl[y][x].PRy>1)prbl[y][x].PRy=1;
					
				}//x

			}//y
		}
		//*******************************************************************
	//*******************************************************************
	public void expandHistogramPR_02(float[] stats)
	{
		float avg=stats[0];
		float max=stats[1];
		float min=stats[2];
		
		//float k3=0;
		//first move avg to 0.5
		//k3=0.5f/avg;
		
		//then expand
		float avg2=0.5f;
		float k1=0;
		if (avg>min) k1=0.5f/(avg-min);// k1<1
		//else k1=0;
		float k2=1;
		if (avg<max) k2=(0.5f)/(max-avg);//k2>1
		
		
		
		
		for (int y=0;y<number_of_blocks_V+1;y++)
		{
			for (int x=0;x<number_of_blocks_H+1;x++)
			{
				//prbl[y][x].PRx= k*(prbl[y][x].PRx-avg)+ avg;
				
				if (prbl[y][x].PRx<=avg)	prbl[y][x].PRx=0.5f - (k1*(avg-prbl[y][x].PRx));
				else prbl[y][x].PRx=k2*(prbl[y][x].PRx-avg)+0.5f;
					
					
				if (prbl[y][x].PRx>1 )prbl[y][x].PRx=1;
				if (prbl[y][x].PRx<0 )prbl[y][x].PRx=0;
				
				//prbl[y][x].PRy= k*(prbl[y][x].PRy-avg)+ avg;
				
				if (prbl[y][x].PRy<=avg)	prbl[y][x].PRy=0.5f - (k1*(avg-prbl[y][x].PRy));
				else prbl[y][x].PRy=k2*(prbl[y][x].PRy-avg)+0.5f;
				
				
				if (prbl[y][x].PRy>1 )prbl[y][x].PRy=1;
				if (prbl[y][x].PRy<0 )prbl[y][x].PRy=0;
			}
		}
	}
	//*******************************************************************
	
	public void expandHistogramPR()
	{
		//this function works fine

		float current_maxPRx=0;
		float current_minPRx=1000;
		float current_maxPRy=0;
		float current_minPRy=1000;
		
		//for (int y=0;y<number_of_blocks_V+1;y++)
	    for (int y=1;y<number_of_blocks_V;y++)
		{
			//for (int x=0;x<number_of_blocks_H+1;x++)
				for (int x=1;x<number_of_blocks_H;x++)
			{
				if (prbl[y][x].PRx>current_maxPRx) current_maxPRx=prbl[y][x].PRx;
				if (prbl[y][x].PRy>current_maxPRy) current_maxPRy=prbl[y][x].PRy;
				
				if (prbl[y][x].PRx<current_minPRx) current_minPRx=prbl[y][x].PRx;
				
				if (prbl[y][x].PRy<current_minPRy) current_minPRy=prbl[y][x].PRy;

				if (prbl[y][x].PRy==0) System.out.println(" 0 en PRy en x="+x+"  y="+y);
			}//x

		}//y

		float new_maxPRx=1;
		float new_minPRx=0;

		float new_maxPRy=1;
		float new_minPRy=0;

System.out.println(" maxPRx:"+current_maxPRx+"   minPRx:"+current_minPRx);
System.out.println(" maxPRy:"+current_maxPRy+"   minPRy:"+current_minPRy);

//ajuste por si acaso hay un bloque con pr=0 que daña la estadistica
if (current_minPRx<0.1f) current_minPRx=0.1f;
if (current_minPRy<0.1f) current_minPRy=0.1f;
//current_minPRy=0.25f;

		for (int y=0;y<number_of_blocks_V+1;y++)
		{

			for (int x=0;x<number_of_blocks_H+1;x++)
			{
				//prbl[y][x].PRx=(new_maxPRx-new_minPRx)*(prbl[y][x].PRx-current_minPRx)/(current_maxPRx-current_minPRx)+new_minPRx;
				
				prbl[y][x].PRx=(prbl[y][x].PRx-current_minPRx)/(current_maxPRx-current_minPRx);
				if (prbl[y][x].PRx<0)prbl[y][x].PRx=0;
				//prbl[y][x].PRy=(new_maxPRy-new_minPRy)*(prbl[y][x].PRy-current_minPRy)/(current_maxPRy-current_minPRy)+new_minPRy;
				prbl[y][x].PRy=(prbl[y][x].PRy-current_minPRy)/(current_maxPRy-current_minPRy);
				
				if (prbl[y][x].PRy<0)prbl[y][x].PRy=0;
				
				
			}//x

		}//y
	}
	//********************************************************************************
	public void printPRstats(String path_file)
	{
		//int prblocks=number_of_blocks_H*number_of_blocks_V;


		System.out.println("-----  PRx stats --------------------");
		System.out.println(" blocks H:"+number_of_blocks_H+1);
		System.out.println(" blocks V:"+number_of_blocks_V+1);
		try{
			//System.out.println("Entrando en salvaTXT");
			DataOutputStream d = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(path_file)));

			int counter=0;			
			for (int y=0;y<number_of_blocks_V+1;y++)
			{
				for (int x=0;x<number_of_blocks_H+1;x++)
				{
					//System.out.println(prbl[j][i].PRx+";"+prbl[j][i].PRy);
					d.writeBytes(new String(counter+";"+prbl[y][x].PRx+";"+prbl[y][x].PRy));
					d.writeBytes("\n");
					counter++;
				}
			}
			d.close();
		}catch(Exception e){ System.out.println(" CIELOS!!!!"+e); System.exit(0);}

	}
	//********************************************************************************************
	/**
	 * aqui entramos con el PR ya cuantizado
	 * @param cf
	 */
	//public void fromPRtoPPP(float compression_factor)
	public void fromPRtoPPP(float cf)
	{
		//COMENTAMOS ESTO
		//compression_factor=1f/(cf*cf);//OJO 20150107
		compression_factor=cf;
		
		//compression_factor=0.125f;//maxima calidad
		//compression_factor=9f;
		
		if (DEBUG) System.out.println(" Compression factor is 1/(cf*cf)  = "+compression_factor);
		
		
		
		/** 
		 * this function transforms PR coeficients in the range [0..1] into
		 * "pixels_per_pixel"  which is the number of original pixels that a scaled pixel represents
		 * This is the first step just before scaling 
		 */

		/**
		 * PR is an array of PR coeficients corresponding to an array of PR blocks 
		 * each PR block's center is the common corner of 4 blocks of the grid
		 * 
		 * PR blocks = (number_of_blocks_H +1) * ( number_of_blocks_V +1) 
		 * 
		 * PR has 2 values: PRx and PRy, like PPP
		 * Therefore PRx and PRy takes into account number of fluctuations and size of hops 
		 * 
		 * These are the PR metrics:
		 * 
		 * PR[block][x || y]
		 * 
		 * PRx,y is a couple of floating values. 
		 * 
		 * 
		 * 
		 */


		for (int by=0;by<number_of_blocks_V;by++)
		{

			for (int bx=0;bx<number_of_blocks_H;bx++)
			{
				Block b=bl[by][bx];


				//float PRmin=0f;
/*
				float k=Block.MAX_PPP-1;
				float k2=Block.MAX_PPP;

				b.ppp[0][0]=(k2/ (1f+k*prbl[by][bx].PRx))*compression_factor;
				b.ppp[0][1]=(k2/ (1f+k*prbl[by][bx+1].PRx))*compression_factor;
				b.ppp[0][2]=(k2/ (1f+k*prbl[by+1][bx].PRx))*compression_factor;
				b.ppp[0][3]=(k2/ (1f+k*prbl[by+1][bx+1].PRx))*compression_factor;


				b.ppp[1][0]=(k2/ (1f+k*prbl[by][bx].PRy))*compression_factor;
				b.ppp[1][1]=(k2/ (1f+k*prbl[by][bx+1].PRy))*compression_factor;
				b.ppp[1][2]=(k2/ (1f+k*prbl[by+1][bx].PRy))*compression_factor;
				b.ppp[1][3]=(k2/ (1f+k*prbl[by+1][bx+1].PRy))*compression_factor;
*/

				//this is an optimization to avoid some multiplications
				float k=Block.MAX_PPP-1;
				float k2=Block.MAX_PPP*compression_factor;

				b.ppp[0][0]=(k2/ (1f+k*prbl[by][bx].PRx));
				b.ppp[0][1]=(k2/ (1f+k*prbl[by][bx+1].PRx));
				b.ppp[0][2]=(k2/ (1f+k*prbl[by+1][bx].PRx));
				b.ppp[0][3]=(k2/ (1f+k*prbl[by+1][bx+1].PRx));


				b.ppp[1][0]=(k2/ (1f+k*prbl[by][bx].PRy));
				b.ppp[1][1]=(k2/ (1f+k*prbl[by][bx+1].PRy));
				b.ppp[1][2]=(k2/ (1f+k*prbl[by+1][bx].PRy));
				b.ppp[1][3]=(k2/ (1f+k*prbl[by+1][bx+1].PRy));
				
				
				//nuevo control de informacion 2014 11 25 CONTROL DE CUANTO NULO
				//--------------------------------------------------------------
				
				if (prbl[by][bx].PRx==0)b.ppp[0][0]=Block.MAX_PPP;
				if (prbl[by][bx+1].PRx==0)b.ppp[0][1]=Block.MAX_PPP;
				if (prbl[by+1][bx].PRx==0)b.ppp[0][2]=Block.MAX_PPP;
				if (prbl[by+1][bx+1].PRx==0)b.ppp[0][3]=Block.MAX_PPP;
				
				if (prbl[by][bx].PRy==0)b.ppp[1][0]=Block.MAX_PPP;
				if (prbl[by][bx+1].PRy==0)b.ppp[1][1]=Block.MAX_PPP;
				if (prbl[by+1][bx].PRy==0)b.ppp[1][2]=Block.MAX_PPP;
				if (prbl[by+1][bx+1].PRy==0)b.ppp[1][3]=Block.MAX_PPP;
				
				//nuevo control de informacion 2014 11 25 CONTROL DE CUANTO 0.125
				//--------------------------------------------------------------
				/*
				float maxq1=2;//Block.MAX_PPP/2;
				float q1=0.25f;
				if (prbl[by][bx].PRx<=q1 && b.ppp[0][0]<maxq1) b.ppp[0][0]=maxq1;
				if (prbl[by][bx+1].PRx<=q1 && b.ppp[0][1]<maxq1) b.ppp[0][1]=maxq1;
				if (prbl[by+1][bx].PRx<=q1 && b.ppp[0][2]<maxq1) b.ppp[0][2]=maxq1;
				if (prbl[by+1][bx+1].PRx<=q1 && b.ppp[0][3]<maxq1) b.ppp[0][3]=maxq1;
				
				if (prbl[by][bx].PRy<=q1 && b.ppp[1][0]<maxq1) b.ppp[1][0]=maxq1;
				if (prbl[by][bx+1].PRy<=q1 && b.ppp[1][1]<maxq1) b.ppp[1][1]=maxq1;
				if (prbl[by+1][bx].PRy<=q1 && b.ppp[1][2]<maxq1) b.ppp[1][2]=maxq1;
				if (prbl[by+1][bx+1].PRy<=q1 && b.ppp[1][3]<maxq1) b.ppp[1][3]=maxq1;
				*/
				// CONTROL DE MAXIMA ELASTICIDAD
				//---------------------------------
				
				//System.out.println("b.ppp[1][3]:"+k2+" PR:"+prbl[by+1][bx+1].PRy+"   MAX:"+k);
				//nueva regla 02/10/2014
				float minppp=Block.MAX_PPP;
				
				for (int coord=0;coord<2;coord++)
					for (int corner=0;corner<4;corner++)
					{
						if (b.ppp[coord][corner]>Block.MAX_PPP)b.ppp[coord][corner]=Block.MAX_PPP;
						if (b.ppp[coord][corner]<1)b.ppp[coord][corner]=1;
						
						if (minppp>b.ppp[coord][corner]) minppp=b.ppp[coord][corner];
						
						//System.out.println(" bl["+by+"]["+bx+"].ppp["+coord+"]["+corner+"]="+ b.ppp[coord][corner]+"f;");
						//System.out.println("comp:"+compression_factor);
					}
				
				
				//esto es del 02/10/2014
				//limitacion de la elasticidad a 3 veces
				//------------------------------------------
				float max_elastic=3f;// con un valor elevado estaria desactivado, pej 300
				float maxppp=minppp*max_elastic;
				for (int coord=0;coord<2;coord++)
					for (int corner=0;corner<4;corner++)
					{
						if (b.ppp[coord][corner]>maxppp)b.ppp[coord][corner]=maxppp;
						
					}
				
				//end 02102014

			}//bx
		}//by

	}
	//**************************************************************************************
	public void equalizeHistogramPR()
	{
		float N=(number_of_blocks_H+1)*(number_of_blocks_V+1);
		//cumulated histograms. Levels= 100, from 0.0 to 1.0, steps 0.01
		int[] cumulated_histx=new int[1000+1];
		int[] cumulated_histy=new int[1000+1];
		/*
	System.out.println("********************BEFORE EQUALIZATION*******************************");
	for (int y=0;y<number_of_blocks_V+1;y++)
	{
	for (int x=0;x<number_of_blocks_H+1;x++)
	{

	System.out.println(""+prbl[y][x].PRx);
	}
	}
		 */

		//float umbral=0.5f;//0.55f;
		float umbral=1.0f;//0.55f;// no hace falta umbral. ya esta puesto a 1 en PRblock si supera umbral
		// el 1.0 (el umbral) no debo tenerlo en cuenta
		//basta con no meter en la ecualizacion a 1.0
		float Nx=0;
		float Ny=0;
		//----------------------------------------
		for (int y=0;y<number_of_blocks_V+1;y++)
		{

			for (int x=0;x<number_of_blocks_H+1;x++)
			{
				//for each block
				/*
			for (int j=0;j<=(int)(prbl[y][x].PRx*1000f);j++)
				cumulated_histx[(int)(prbl[y][x].PRx*1000f)]++;

			for (int j=0;j<=(int)(prbl[y][x].PRy*1000f);j++)
				cumulated_histy[(int)(prbl[y][x].PRy*1000f)]++;
				 */
				if (prbl[y][x].PRx<umbral)
				{
					Nx++;//only computes non saturated blocks

					for (int j=(int)(prbl[y][x].PRx*1000f);j<=1000;j++) // <1000 para no meter a 1.0. da igual. con el if ya no lo mete

					{
						//N++;
						cumulated_histx[j]++;
					}
				}


				if (prbl[y][x].PRy<umbral)
				{
					Ny++;

					for (int j=(int)(prbl[y][x].PRy*1000f);j<=1000;j++)
				
					{
						//N++;
						cumulated_histy[j]++;
					}
				}

			}
		}

		//for (int k=0;k<1000;k++) System.out.println(cumulated_histx[k]);

		for (int y=0;y<number_of_blocks_V+1;y++)
		{

			for (int x=0;x<number_of_blocks_H+1;x++)
			{
				if (prbl[y][x].PRx!=1)
					//prbl[y][x].PRx=(1f/Nx)*(float)cumulated_histx[(int)(prbl[y][x].PRx*1000f)];
				    prbl[y][x].PRx=(1f/Nx)*(float)cumulated_histx[(int)(prbl[y][x].PRx*1000f)];

				if (prbl[y][x].PRy!=1)
					//prbl[y][x].PRy=(1f/Ny)*(float)cumulated_histy[(int)(prbl[y][x].PRy*1000f)];
					prbl[y][x].PRy=(1f/Ny)*(float)cumulated_histy[(int)(prbl[y][x].PRy*1000f)];

			}
		}



		//System.out.println("********************AFTER*******************************");
		for (int y=0;y<number_of_blocks_V+1;y++)
		{
			for (int x=0;x<number_of_blocks_H+1;x++)
			{

				//System.out.println(""+(int)(prbl[y][x].PRx*100));
				//System.out.println(""+(int)(prbl[y][x].PRy*100));
			}
		}

		//if (2>1) System.exit(0);


	}
	//************************************************************
	/**
	 * this function saves the grid into a .txt file
	 * for experimental purposes
	 * 
	 * @param filename
	 */
	public void saveGridTXT(String path_file)
	{
		
		try{
			if (DEBUG) System.out.println("Entrando en saveGridTXT");
			DataOutputStream d = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(path_file)));
			

			//primero escribo el numero de bloques de la malla
			d.writeBytes((number_of_blocks_H+1)+"\n");
			d.writeBytes((number_of_blocks_V+1)+"\n");
			
			if (DEBUG) System.out.println("");
		for (int y=0;y<number_of_blocks_V+1;y++)
		{

			for (int x=0;x<number_of_blocks_H+1;x++)
			{
				
				float datox= ((float) ((int)(prbl[y][x].PRx*100)))/100f;
				float datoy= ((float) ((int)(prbl[y][x].PRy*100)))/100f;
				
				d.writeBytes(datox+";");
				d.writeBytes(datoy+";");
				//System.out.print(datox+";");System.out.print(datoy+";");
				
			}
			d.writeBytes("\n");	
			//System.out.println("");
			
		}
		d.close();
	}catch (Exception e){System.out.println("failure saving grid.txt");}
	}	
	

	
	//*************************************************************************************************
	/**
	 * 
	 * First, this function compute the maximum PPP value for a given image. MAX_PPP value must be >=8
	 *   this value must allow 32 blocks of a side of 8pix lenght or higher lenght
	 *   if not possible, number of blocks is set below 32 and MAX_PPP is set to 8
	 * 
	 * Then, this function creates an array of PRblocks and another array of Blocks.
	 * 
	 *   center of the PRblocks are ther corners of Blocks
	 *   blocks are not identical, unless image width and high are divisible by the block length 
	 *   vertical size of blocks may be different from horizontal size
	 *   the grid must cover the entire image
	 *   
	 */
	//public void createGrid(int image_width, int image_height, float comp_factor, float MAX_PPP)
	//public void createGrid(int image_width, int image_height,  float MAX_PPP)
	public void createGrid(int image_width, int image_height)
	{

			//compression_factor=1f/(comp_factor*comp_factor);
			//System.out.println(" Compression factor is 1/(cf*cf)  = "+compression_factor);

			//determinacion del lado mayor
			int larger_side=Math.max(image_width, image_height);
			int shorter_side=Math.min(image_width, image_height);
			
			//computation of number of blocks for larger image side
			//minimum block side length is 16 pixels (minimum MAX PPP=8)
			if (DEBUG) System.out.println(" initial blocks_of_larger_image_side="+MAX_BLOCKS);
			blocks_of_larger_image_side=(float)(MAX_BLOCKS);
			float size1=Math.max((float)(larger_side)/blocks_of_larger_image_side, MIN_LEN_BLOCK);
			//float size2=Math.max((float)(shorter_side)/(int) ((float)(shorter_side)/size1 +0.5f), 16);
			
			float size2=Math.max((float)(shorter_side)/(int) ((float)(shorter_side)/size1), MIN_LEN_BLOCK);
			
			if (larger_side==image_width) {sizeh=size1; sizev=size2;}
			else {sizeh=size2;sizev=size1;}
			if (DEBUG) System.out.println(" grid sizeh="+sizeh+" grid sizev="+sizev);
			blocks_of_larger_image_side=(int)(0.5f+larger_side/size1); //from 16 to 32 blocks
			if (DEBUG) System.out.println(" final blocks_of_larger_image_side="+blocks_of_larger_image_side);
			
			//tunning PPP
			float current_ppp=0.5f*larger_side/blocks_of_larger_image_side;
			if (DEBUG) System.out.println(" MAX_PPP="+current_ppp);
			Block.MAX_PPP=current_ppp;
			
			//number_of_blocks_H=(int) (0.5f+(float)image_width/sizeh);// 0.5 to avoid this:  (int)7.999 is 7 
			//number_of_blocks_V=(int) (0.5f+(float)image_height/sizev);
			
			number_of_blocks_H=(int) (0.5f+(float)image_width/sizeh);// 0.5 to avoid this:  (int)7.999 is 7 
			number_of_blocks_V=(int) (0.5f+(float)image_height/sizev);
			
			if (DEBUG) System.out.println(" blocks H:"+number_of_blocks_H);
			if (DEBUG) System.out.println(" blocks V:"+number_of_blocks_V);
	        
			
			//System.out.println(" blocks H:"+number_of_blocks_H+ "  width:"+image_width);
			//System.out.println(" blocks V:"+number_of_blocks_V+"  height:"+image_height);
			//int total_blocks=number_of_blocks_H*number_of_blocks_V;

			// instantiating block list...
			//bl=new ArrayList(total_blocks);
			bl=new Block[number_of_blocks_V][number_of_blocks_H];

			float coordx=0;
			float coordy=0;
			// bucle filling blocks information 


				int n=0;
			for (int by=0;by<number_of_blocks_V;by++)
			{

				for (int bx=0;bx<number_of_blocks_H;bx++)
				{
					Block b=new Block();
					b.xini=(int)(0.5f+coordx);
					b.xfin=(int)(-0.5f+coordx+sizeh);
					if (b.xfin>image_width-1) b.xfin=image_width-1;
					b.yini=(int)(0.5f+coordy);
					b.yfin=(int)(-0.5f+coordy+sizev);
					if (b.yfin>image_height-1) b.yfin=image_height-1;

					b.lx=b.xfin-b.xini+1;
					b.ly=b.yfin-b.yini+1;


					//bl.add(b);
					bl[by][bx]=b;
					//	System.out.println("block "+n+" lx:"+b.lx+ " ly:"+b.ly+" xini:"+b.xini+" xfin:"+b.xfin+" yini:"+b.yini+" yfin:"+b.yfin);
					coordx+=sizeh;	
							n++;
				}//bx
				coordy+=sizev;
				coordx=0;
			}//by


			// bucle filling PRblocks information
			coordx=-sizeh/2;
			coordy=-sizev/2;
			prbl=new PRblock[number_of_blocks_V+1][number_of_blocks_H+1];
			for (int y=0;y<number_of_blocks_V+1;y++)
			{

				for (int x=0;x<number_of_blocks_H+1;x++)
				{
					PRblock prb=new PRblock();
					
					prb.HistnqPRx=new float[10];
					prb.HistnqPRy=new float[10];
					
					
					//System.out.println("PRxq:"+prb.PRxq);
					prb.yini=(int)(coordy+0.5);
					prb.xini=(int)(coordx+0.5);
					
					//prb.yini=(int)(coordy);
					//prb.xini=(int)(coordx);
					
					prb.yfin=(int)(coordy+sizev);
					prb.xfin=(int)(coordx+sizeh);

					if (prb.yini<0)prb.yini=0;
					if (prb.xini<0)prb.xini=0;
					if (prb.yfin>image_height-1) prb.yfin=image_height-1;
					if (prb.xfin>image_width-1)  prb.xfin=image_width-1;

					coordx+=sizeh;
					prbl[y][x]=prb;
					//System.out.println("PRbl["+y+"]["+x+"]:"+ " xini="+prb.xini+ " yini="+prb.yini+"  xfin="+prb.xfin+"   yfin="+prb.yfin);
				}//x
				coordy+=sizev;
				coordx=-sizeh/2;
			}//y

			//TO DO

		}
	//******************************************************************************	
	public void setOptimalKini()
	{
	for (int by=0;by<number_of_blocks_V;by++)
	{

		for (int bx=0;bx<number_of_blocks_H;bx++)
		{
			Block b=bl[by][bx];



			//this is an optimization to avoid some multiplications
			float k=Block.MAX_PPP-1;
			float k2=Block.MAX_PPP*compression_factor;

			float PRavg=0;
			
			PRavg+=prbl[by][bx].PRx;
			PRavg+=prbl[by][bx+1].PRx;
			PRavg+=prbl[by+1][bx].PRx;
			PRavg+=prbl[by+1][bx+1].PRx;
			
			PRavg+=prbl[by][bx].PRy;
			PRavg+=prbl[by][bx+1].PRy;
			PRavg+=prbl[by+1][bx].PRy;
			PRavg+=prbl[by+1][bx+1].PRy;
			
			
			PRavg=PRavg/8f;
			
			

		}//bx
	}//by

}
//**************************************************************************************
	//******************************************************************************	
		public float[] computePRavg()
		{
			float avg=0;
			float max=0;
			float min=1;
			for (int by=0;by<number_of_blocks_V+1;by++)
			{

				for (int bx=0;bx<number_of_blocks_H+1;bx++)
				{
					if (prbl[by][bx].PRx> max && prbl[by][bx].PRx!=10) max=prbl[by][bx].PRx;
					if (prbl[by][bx].PRx< min) min=prbl[by][bx].PRx;
					
					if (prbl[by][bx].PRy> max && prbl[by][bx].PRy!=10) max=prbl[by][bx].PRy;
					if (prbl[by][bx].PRy< min) min=prbl[by][bx].PRy;
					
					//System.out.println(" max:"+max);
			avg+=prbl[by][bx].PRx;
			avg+=prbl[by][bx].PRy;
			
				}
			}
			avg=avg/(2*(number_of_blocks_V+1)*(number_of_blocks_H+1));
		
			float[] result=new float[3];
			//return avg;
			result[0]=avg;
			result[1]=max;
			result[2]=min;
			return result;
		
		}
		public float computePRdev(float avg)
		{
			float dev=0;
			//System.out.println(" avg es "+avg);
			for (int by=0;by<number_of_blocks_V+1;by++)
			{

				for (int bx=0;bx<number_of_blocks_H+1;bx++)
				{
			dev+=(prbl[by][bx].PRx-avg)*(prbl[by][bx].PRx-avg);
			dev+=(prbl[by][bx].PRy-avg)*(prbl[by][bx].PRy-avg);
			
				}
			}
			dev=(float) Math.sqrt(dev / (2*(number_of_blocks_V+1)*(number_of_blocks_H+1)));
		return dev;	
		}	
	//******************************************************************************	
	public void computePRavgPerBlock()
	{
	for (int by=0;by<number_of_blocks_V;by++)
	{

		for (int bx=0;bx<number_of_blocks_H;bx++)
		{
			Block b=bl[by][bx];



		
			float PRavgx=0;
			float PRavgy=0;
			float PRavg=0;
			
			PRavg+=prbl[by][bx].PRx;
			PRavg+=prbl[by][bx+1].PRx;
			PRavg+=prbl[by+1][bx].PRx;
			PRavg+=prbl[by+1][bx+1].PRx;
			
			PRavgx=PRavg;
			
			PRavg+=prbl[by][bx].PRy;
			PRavg+=prbl[by][bx+1].PRy;
			PRavg+=prbl[by+1][bx].PRy;
			PRavg+=prbl[by+1][bx+1].PRy;
			
			PRavgy=PRavg-PRavgx;
			
			b.PRavg=PRavg/8f;
			
			b.PRavgx=PRavgx/4f;
			b.PRavgy=PRavgy/4f;

		}//bx
	}//by

}
	//***************************************************************
	public void sumadown(int[][] y3, int[][] y3b)
	{
		
		//deberia sumar solo los bloques, no toda la imagen
		for (int by=0;by<number_of_blocks_V;by++)
		{

			for (int bx=0;bx<number_of_blocks_H;bx++)
			{
				Block b=bl[by][bx];
				
		for (int component=0;component<3;component++)
		{
			for (int y=b.yini;y<=b.downsampled_yfin;y++)
			{
			for (int x=b.xini;x<b.downsampled_xfin;x++)
			{
				int pos=y*Block.img.width+x;
				Block.img.downsampled_YUV[component][pos]=y3[component][pos]-y3b[component][pos];
			
				if (Block.img.downsampled_YUV[component][pos]>255) Block.img.downsampled_YUV[component][pos]=255;
				if (Block.img.downsampled_YUV[component][pos]<0) Block.img.downsampled_YUV[component][pos]=0;
			}
		    }
		}
	}
		}
	}
	
	
	public void createGridPlusNOSE_USA(int image_width, int image_height,int plus)
	{

			//compression_factor=1f/(comp_factor*comp_factor);
			//System.out.println(" Compression factor is 1/(cf*cf)  = "+compression_factor);

			//determinacion del lado mayor
			int larger_side=Math.max(image_width, image_height);
			int shorter_side=Math.min(image_width, image_height);
			
			//computation of number of blocks for larger image side
			//minimum block side length is 16 pixels (minimum MAX PPP=8)
			if (DEBUG) System.out.println(" initial blocks_of_larger_image_side="+MAX_BLOCKS);
			blocks_of_larger_image_side=MAX_BLOCKS;
			float size1=Math.max((float)(larger_side)/blocks_of_larger_image_side, 16);
			float size2=Math.max((float)(shorter_side)/(int) ((float)(shorter_side)/size1 +0.5f), 16);
			if (larger_side==image_width) {sizeh=size1; sizev=size2;}
			else {sizeh=size2;sizev=size1;}
			if (DEBUG) System.out.println(" grid sizeh="+sizeh+" grid sizev="+sizev);
			blocks_of_larger_image_side=larger_side/size1; //from 16 to 32 blocks
			if (DEBUG) System.out.println(" final blocks_of_larger_image_side="+blocks_of_larger_image_side);
			
			//tunning PPP
			float current_ppp=0.5f*larger_side/blocks_of_larger_image_side;
			if (DEBUG) System.out.println(" MAX_PPP="+current_ppp);
			Block.MAX_PPP=current_ppp;
			
			number_of_blocks_H=(int) (0.5f+(float)image_width/sizeh);// 0.5 to avoid this:  (int)7.999 is 7 
			number_of_blocks_V=(int) (0.5f+(float)image_height/sizev);
			
			
			number_of_blocks_H=number_of_blocks_H-plus;
			number_of_blocks_V=number_of_blocks_V-plus;
			sizeh=(float)image_width/(float)number_of_blocks_H;
			sizev=(float)image_height/(float)number_of_blocks_V;
			Block.MAX_PPP=0.5f*larger_side/number_of_blocks_H;
			
			
			
			
			if (DEBUG) System.out.println(" blocks H:"+number_of_blocks_H);
			if (DEBUG) System.out.println(" blocks V:"+number_of_blocks_V);
	        
			
			System.out.println(" blocks H:"+number_of_blocks_H+ "  width:"+image_width);
			System.out.println(" blocks V:"+number_of_blocks_V+"  height:"+image_height);
			//int total_blocks=number_of_blocks_H*number_of_blocks_V;

			// instantiating block list...
			//bl=new ArrayList(total_blocks);
			bl=new Block[number_of_blocks_V][number_of_blocks_H];

			float coordx=0;
			float coordy=0;
			// bucle filling blocks information 


			//	int n=0;
			for (int by=0;by<number_of_blocks_V;by++)
			{

				for (int bx=0;bx<number_of_blocks_H;bx++)
				{
					Block b=new Block();
					b.xini=(int)(0.5f+coordx);
					b.xfin=(int)(-0.5f+coordx+sizeh);
					if (b.xfin>image_width-1) b.xfin=image_width-1;
					b.yini=(int)(0.5f+coordy);
					b.yfin=(int)(-0.5f+coordy+sizev);
					if (b.yfin>image_height-1) b.yfin=image_height-1;

					b.lx=b.xfin-b.xini+1;
					b.ly=b.yfin-b.yini+1;


					//bl.add(b);
					bl[by][bx]=b;
					//	System.out.println("block "+n+" lx:"+b.lx+" xini:"+b.xini+" xfin:"+b.xfin+" yini:"+b.yini+" yfin:"+b.yfin);
					coordx+=sizeh;	
					//		n++;
				}//bx
				coordy+=sizev;
				coordx=0;
			}//by


			// bucle filling PRblocks information
			coordx=-sizeh/2;
			coordy=-sizev/2;
			prbl=new PRblock[number_of_blocks_V+1][number_of_blocks_H+1];
			for (int y=0;y<number_of_blocks_V+1;y++)
			{

				for (int x=0;x<number_of_blocks_H+1;x++)
				{
					PRblock prb=new PRblock();
					//System.out.println("PRxq:"+prb.PRxq);
					prb.yini=(int)(coordy+0.5);
					prb.xini=(int)(coordx+0.5);
					prb.yfin=(int)(coordy+sizev);
					prb.xfin=(int)(coordx+sizeh);

					if (prb.yini<0)prb.yini=0;
					if (prb.xini<0)prb.xini=0;
					if (prb.yfin>image_height-1) prb.yfin=image_height-1;
					if (prb.xfin>image_width-1)  prb.xfin=image_width-1;

					coordx+=sizeh;
					prbl[y][x]=prb;
					//System.out.println("PRbl["+y+"]["+x+"]:"+ " xini="+prb.xini+ " yini="+prb.yini+"  xfin="+prb.xfin+"   yfin="+prb.yfin);
				}//x
				coordy+=sizev;
				coordx=-sizeh/2;
			}//y

			//TO DO

		}
	//******************************************************************************
	//****************************************************************************
		public void computeMetrics(Grid grid_ant)//no se usa grid_ant
		{
			
			
			//first we compute metrics at each PRblock
			for (int y=0;y<number_of_blocks_V+1;y++)
			{
				for (int x=0;x<number_of_blocks_H+1;x++)
				{
					prbl[y][x].computePRmetrics();
					
				}//x
			}//y
			
			
			//analisys on PR and decision about best equalization
			//---------------------------------------------------
			//this computes PR average and standard deviation
			if (DEBUG)
			{
			
			float[] PRstats=new float[3];
			PRstats=computePRavg();
			float PRdev=computePRdev(PRstats[0]);
			
			System.out.println("--- PR STATS BEFORE EQUALIZATION ---");
			System.out.println("   >PR average:"+PRstats[0]+"    , max:"+PRstats[1]+"    , min:"+PRstats[2]);
			System.out.println("   >PR std deviation:"+PRdev);
			}
			
					
			

			//then we equalize PR histogram
			//------------------------------
			if (DEBUG) System.out.println(" equalizing...");
			
			//expandHistogramPR_02(PRstats);
			
			//expandHistogramPR_03(PRstats[0],PRdev);
			
			//printHistogramPR();
			
			topaPR(0.5f);
			
			expandHistogramPR_04();//PRstats[0],PRdev);
			
			storeNQPR();//grid_ant); no usa grid_ant
			
			if (DEBUG)
			{
			float[] PRstats=new float[3];
			PRstats=computePRavg();
			float PRdev=computePRdev(PRstats[0]);
			System.out.println("--- PR STATS  AFTER EQUALIZATION ---");
			System.out.println("   >PR average:"+PRstats[0]+"    , max:"+PRstats[1]+"    , min:"+PRstats[2]);
			System.out.println("   >PR std deviation:"+PRdev);
			}
			//equalizeHistogramPR();

			if (DEBUG) System.out.println(" quantizing...");
			// the last step: quantization of PR
			//-----------------------------------------
			for (int y=0;y<number_of_blocks_V+1;y++)
			{
				for (int x=0;x<number_of_blocks_H+1;x++)
				{
					prbl[y][x].quantizeGeometricalPR();
					
				}//x
			}//y

			
			
		}
	//*******************************************************************
	public void storeNQPR()//Grid grid_ant)
	{
		//se invoca despues de expandir y antes de cuantizar
		
		for (int y=0;y<number_of_blocks_V+1;y++)
		{
			for (int x=0;x<number_of_blocks_H+1;x++)
			{
				prbl[y][x].nqPRx=prbl[y][x].PRx;
				prbl[y][x].nqPRy=prbl[y][x].PRy;
				//System.out.println("TUNEANDO antes:"+grid_ant.prbl[y][x].nqPRx+"      despues:"+prbl[y][x].PRx);
				
				
				//if (prbl[y][x].PRx>=0) prbl[y][x].PRx=0.75f; 
				//if (prbl[y][x].PRy>=0) prbl[y][x].PRy=0.75f;
				//nueva historica
				//---------------------
				float sumx=0;
				float sumy=0;
				
				for (int i=3;i>0;i--)
				{
					sumx+=prbl[y][x].HistnqPRx[i];
					sumy+=prbl[y][x].HistnqPRy[i];
					
					prbl[y][x].HistnqPRx[i]=prbl[y][x].HistnqPRx[i-1];
					prbl[y][x].HistnqPRy[i]=prbl[y][x].HistnqPRy[i-1];
				}
				//prbl[y][x].HistnqPRx[0]=prbl[y][x].PRx;
				//prbl[y][x].HistnqPRy[0]=prbl[y][x].PRy;
			    //---------------
				//limite a la bajada
				float u=0.25f;
				//if (prbl[y][x].PRx+u<prbl[y][x].HistnqPRx[1]) {prbl[y][x].PRx=prbl[y][x].HistnqPRx[1]-u;}
				//if (prbl[y][x].PRy+u<prbl[y][x].HistnqPRy[1]) {prbl[y][x].PRy=prbl[y][x].HistnqPRy[1]-u;}
				
				//no se permite bajar salvo si
				//if (prbl[y][x].HistnqPRx[1]>0.25 && prbl[y][x].HistnqPRx[1]<0.75 && prbl[y][x].PRx <prbl[y][x].HistnqPRx[1] ) prbl[y][x].PRx=prbl[y][x].HistnqPRx[1];
				//if (prbl[y][x].HistnqPRy[1]>0.25 && prbl[y][x].HistnqPRy[1]<0.75 && prbl[y][x].PRy <prbl[y][x].HistnqPRy[1] ) prbl[y][x].PRx=prbl[y][x].HistnqPRy[1];
				//limite a la bajada
				//if (prbl[y][x].PRx+0.25f<prbl[y][x].HistnqPRx[1]) {prbl[y][x].PRx=prbl[y][x].HistnqPRx[1]-0.25f;}
				//if (prbl[y][x].PRy+0.25f<prbl[y][x].HistnqPRy[1]) {prbl[y][x].PRy=prbl[y][x].HistnqPRy[1]-0.25f;}
				
				//if (sumx==0)prbl[y][x].PRx=0.5f;//regenera cada n frames
				//if (sumy==0)prbl[y][x].PRy=0.5f;
				prbl[y][x].HistnqPRx[0]=prbl[y][x].PRx;
				prbl[y][x].HistnqPRy[0]=prbl[y][x].PRy;
			    
				//if (prbl[y][x].HistnqPRx[0]>0 && prbl[y][x].HistnqPRx[1]==0) prbl[y][x].PRx=0.75f; 
				//if (prbl[y][x].HistnqPRx[0]>0 && prbl[y][x].HistnqPRx[1]==0) prbl[y][x].PRx=0.75f;
				
				
					//modifico PRx pero no la historia, selectivo por cuantos, aguantando si cambia de cuanto
				/*
				if (prbl[y][x].HistnqPRx[0]<prbl[y][x].HistnqPRx[1] ||
						prbl[y][x].HistnqPRy[0]<prbl[y][x].HistnqPRy[1]
						)
				{
					boolean hecho=false;
					float u=0.74f;
					if (!hecho && prbl[y][x].HistnqPRx[1]>=u && prbl[y][x].HistnqPRx[0]<u) {prbl[y][x].PRx=u+1.1f;hecho=true;}//prbl[y][x].HistnqPRx[1];
					if (!hecho && prbl[y][x].HistnqPRy[1]>=u && prbl[y][x].HistnqPRy[0]<u) {prbl[y][x].PRy=u+1.1f;hecho=true;}//prbl[y][x].HistnqPRy[1];
					
					u=0.49f;
					if (!hecho && prbl[y][x].HistnqPRx[1]>=u && prbl[y][x].HistnqPRx[0]<u) {prbl[y][x].PRx=u+1.1f;hecho=true;}//0.75f;//prbl[y][x].HistnqPRx[1];
					if (!hecho && prbl[y][x].HistnqPRy[1]>=u && prbl[y][x].HistnqPRy[0]<u) {prbl[y][x].PRy=u+1.1f;hecho=true;}//0.75f;//prbl[y][x].HistnqPRy[1];
					u=0.24f;
					if (!hecho && prbl[y][x].HistnqPRx[1]>=u && prbl[y][x].HistnqPRx[0]<u) {prbl[y][x].PRx=u+1.1f;hecho=true;}//0.5f;//prbl[y][x].HistnqPRx[1];
					if (!hecho && prbl[y][x].HistnqPRy[1]>=u && prbl[y][x].HistnqPRy[0]<u) {prbl[y][x].PRy=u+1.1f;hecho=true;}//0.5f;//prbl[y][x].HistnqPRy[1];
				}
				
				*/
				
				/*
				// DE MOMENTO ESTO ES LO MEJOR
				if (prbl[y][x].HistnqPRx[0]< prbl[y][x].HistnqPRx[1] && prbl[y][x].HistnqPRx[1]>0.25f && prbl[y][x].HistnqPRx[0]<0.75f) //si pr menor que la ant
				{
				//if ((prbl[y][x].PRx<0.5 && prbl[y][x].HistnqPRx[1] >=0.5) ||
				//		(prbl[y][x].PRx<0.75 && prbl[y][x].HistnqPRx[1] >=7.5) )
				prbl[y][x].PRx=0.75f;//prbl[y][x].HistnqPRx[1]+0.26f;;//igualamos
				//prbl[y][x].PRy=prbl[y][x].HistnqPRy[1];
				//prbl[y][x].HistnqPRx[0]=prbl[y][x].HistnqPRx[1];//igualamos la hist para la proxima
				
				}
				if (prbl[y][x].HistnqPRy[0]<prbl[y][x].HistnqPRy[1]&& prbl[y][x].HistnqPRy[1]>0.25f && prbl[y][x].HistnqPRy[0]<0.75f)
				{
				//	if ((prbl[y][x].PRy<0.5 && prbl[y][x].HistnqPRy[1] >=0.5) ||
					//(prbl[y][x].PRy<0.75 && prbl[y][x].HistnqPRy[1] >=7.5) )
					//prbl[y][x].PRx=prbl[y][x].HistnqPRx[1];
					prbl[y][x].PRy=0.75f;//prbl[y][x].HistnqPRy[1]+0.26f;
					//prbl[y][x].HistnqPRy[0]=prbl[y][x].HistnqPRy[1];
					
				}
				//-----------------END LO MEJOR
				*/
				/*
				float level0=0.25f;//0.5f;//0.25f;
				float level1=0.5f;//1.4f;//0.50f;
				float level2=0.75f;//
				if (prbl[y][x].PRx<level0) prbl[y][x].PRx=0.125f;//level0/2f;
				else if (prbl[y][x].PRx<level1) prbl[y][x].PRx=0.25f;
				else if (prbl[y][x].PRx<level2) prbl[y][x].PRx=0.5f;
				else prbl[y][x].PRx=1f;//(1f+level2)/2f;

				if (prbl[y][x].PRy<level0) prbl[y][x].PRy=0.125f;//level0/2f;
				else if (prbl[y][x].PRy<level1) prbl[y][x].PRy=0.25f;
				else if (prbl[y][x].PRy<level2) prbl[y][x].PRy=0.5f;
				else prbl[y][x].PRy=1f;//(1f+level2)/2f;
				*/
				//cuantizamos
				/*
				if (prbl[y][x].HistnqPRx[0]>0 && prbl[y][x].HistnqPRx[0]<1)
				{
					if (prbl[y][x].HistnqPRx[0]>=0.75) prbl[y][x].HistnqPRx[0]=0.75f;
					else if (prbl[y][x].HistnqPRx[0]>=0.5) prbl[y][x].HistnqPRx[0]=0.5f;
					else if (prbl[y][x].HistnqPRx[0]>=0.25) prbl[y][x].HistnqPRx[0]=0.25f;
					else if (prbl[y][x].HistnqPRx[0]>0) prbl[y][x].HistnqPRx[0]=0.125f;
				}
				if (prbl[y][x].HistnqPRy[0]>0 && prbl[y][x].HistnqPRy[0]<1)
				{
					if (prbl[y][x].HistnqPRy[0]>=0.75) prbl[y][x].HistnqPRy[0]=0.75f;
					else if (prbl[y][x].HistnqPRy[0]>=0.5) prbl[y][x].HistnqPRy[0]=0.5f;
					else if (prbl[y][x].HistnqPRy[0]>=0.25) prbl[y][x].HistnqPRy[0]=0.25f;
					else if (prbl[y][x].HistnqPRy[0]>0) prbl[y][x].HistnqPRy[0]=0.125f;
				}
				*/
				//upgrade comienzo de mov, para evitar manchas
				
				//no permito dos escalones hacia arriba en un solo frame
				/*
				if (prbl[y][x].HistnqPRx[0]>prbl[y][x].HistnqPRx[1])
				  {	
				  if (prbl[y][x].HistnqPRx[0]>prbl[y][x].HistnqPRx[1]+0.25f)
					{
					  prbl[y][x].HistnqPRx[0]=prbl[y][x].HistnqPRx[1]+0.25f;
					  prbl[y][x].PRx=prbl[y][x].HistnqPRx[0];
					}
				  else
				    {
					//  prbl[y][x].HistnqPRx[0]=prbl[y][x].HistnqPRx[1]; //no sube nada?
				    }
				  }
				if (prbl[y][x].HistnqPRy[0]>prbl[y][x].HistnqPRy[1])
				  {  
				  if (prbl[y][x].HistnqPRy[0]>prbl[y][x].HistnqPRy[1]+0.25f)
				    {
					  prbl[y][x].HistnqPRy[0]=prbl[y][x].HistnqPRy[1]+0.25f;
				      prbl[y][x].PRy=prbl[y][x].HistnqPRy[0];
				    }
				  else
				    {
					 // prbl[y][x].HistnqPRy[0]=prbl[y][x].HistnqPRy[1]; //no subir 
				    }
				  }
				  */
			   //aceptamos subidas pero no bajadas a menos que sea quieto (=0)
				//ojo. <0.25 es max compresion pero no es cero y estropea. Comprobamos >0 y no >025 
				//problema: una subida solo queda bien si lo que estaba parado era liso
				
				/*
				if (prbl[y][x].HistnqPRx[0]>0 && prbl[y][x].HistnqPRx[0]<prbl[y][x].HistnqPRx[1])
				  {
				  prbl[y][x].PRx=prbl[y][x].HistnqPRx[1];
				  prbl[y][x].HistnqPRx[0]=prbl[y][x].PRx;
				  
				  }
				if (prbl[y][x].HistnqPRy[0]>0  && prbl[y][x].HistnqPRy[0]<prbl[y][x].HistnqPRy[1]) 
				  {
				  prbl[y][x].PRy=prbl[y][x].HistnqPRy[1];
				  prbl[y][x].HistnqPRy[0]=prbl[y][x].PRy;
				  }
				*/
				
				//justo antes de entrar en parada subimos
				//if (prbl[y][x].HistnqPRx[0]==0 && prbl[y][x].HistnqPRx[1]>0 )prbl[y][x].PRx=0.75f;
				//if (prbl[y][x].HistnqPRy[0]==0 && prbl[y][x].HistnqPRy[1]>0 )prbl[y][x].PRy=0.75f;
				//if (prbl[y][x].HistnqPRx[0]==0 )prbl[y][x].PRx=0.75f;
				//if (prbl[y][x].HistnqPRy[0]==0  )prbl[y][x].PRy=0.75f;
				//if (prbl[y][x].HistnqPRy[0]>=0.25 )prbl[y][x].PRy=0.75f;
				
				//prbl[y][x].PRy=0.5f;
				//else {prbl[y][x].HistnqPR[0]=0;prbl[y][x].PRx=0;}
				//if (prbl[y][x].HistnqPRy[0]<prbl[y][x].HistnqPRy[1]&& prbl[y][x].HistnqPRy[1]>0.25) {prbl[y][x].PRy=1;}
				//else {prbl[y][x].HistnqPRy[0]=0;prbl[y][x].PRy=0;}
				
				/*
				
				if (prbl[y][x].HistnqPRx[0]>prbl[y][x].HistnqPRx[1] &&  prbl[y][x].HistnqPRx[1]<=0.25 )
					{
					
					prbl[y][x].PRx=1;
					prbl[y][x].HistnqPRx[0]=1;
					}
				if (prbl[y][x].HistnqPRy[0]> prbl[y][x].HistnqPRy[1] &&  prbl[y][x].HistnqPRy[1]<=0.25 )
				{
				prbl[y][x].PRy=1;
				prbl[y][x].HistnqPRy[0]=1;
				}
				
				//upgrade fin de mov
				if (prbl[y][x].HistnqPRx[0]<0.125 && prbl[y][x].HistnqPRx[1]>0  && prbl[y][x].HistnqPRx[1]<1)
					{
					
					prbl[y][x].PRx=1;
					prbl[y][x].HistnqPRx[0]=1;
					}
				if (prbl[y][x].HistnqPRy[0]<0.125 && prbl[y][x].HistnqPRy[1]>0  && prbl[y][x].HistnqPRy[1]<1)
				{
				prbl[y][x].PRy=1;
				prbl[y][x].HistnqPRy[0]=1;
				}
			    //upgrade normal
				if (prbl[y][x].HistnqPRx[0]>0.0 && prbl[y][x].HistnqPRx[0]<prbl[y][x].HistnqPRx[1] && prbl[y][x].HistnqPRx[1]<0.75)
				{
					prbl[y][x].PRx=1;
					prbl[y][x].HistnqPRx[0]=1;
					}	
				if (prbl[y][x].HistnqPRy[0]>0.0 && prbl[y][x].HistnqPRy[0]<prbl[y][x].HistnqPRy[1] && prbl[y][x].HistnqPRy[1]<0.75)
				{
					prbl[y][x].PRy=1;
					prbl[y][x].HistnqPRy[0]=1;
					}	
				
				*/
				
				
				
				/*
				//countdown hacia una mejora 
				//si desactivada y estamos debajo de 0.75 activamos
				if (prbl[y][x].HistnqPRx[9]==0 && prbl[y][x].HistnqPRx[0]<0.75 && prbl[y][x].HistnqPRx[0]>0.25) prbl[y][x].HistnqPRx[9]=1;//activa countdown]
				else if (prbl[y][x].HistnqPRx[0]>=0.75) {prbl[y][x].HistnqPRx[9]=0;prbl[y][x].HistnqPRx[8]=0;}//desactiva countdown
				if (prbl[y][x].HistnqPRx[9]==1) prbl[y][x].HistnqPRx[8]+=1;
				
				if (prbl[y][x].HistnqPRy[9]==0 && prbl[y][x].HistnqPRy[0]<0.75 && prbl[y][x].HistnqPRy[0]>0.25) prbl[y][x].HistnqPRy[9]=1;//activa countdown]
				else if (prbl[y][x].HistnqPRy[0]>=0.75)  {prbl[y][x].HistnqPRy[9]=0;prbl[y][x].HistnqPRy[8]=0;}//desactiva countdown
				if (prbl[y][x].HistnqPRy[9]==1)	prbl[y][x].HistnqPRy[8]+=1;
				
				
				//activacion de upgrade
				if (prbl[y][x].HistnqPRx[0]>prbl[y][x].HistnqPRx[1] && prbl[y][x].HistnqPRx[1] <0.75) prbl[y][x].HistnqPRx[4]=-1;
				if (prbl[y][x].HistnqPRy[0]>prbl[y][x].HistnqPRy[1] && prbl[y][x].HistnqPRy[1] <0.75) prbl[y][x].HistnqPRy[4]=-1;
				
				//ajuste de PR para poner limite a la bajada
				if (prbl[y][x].HistnqPRx[0]>0.75f) prbl[y][x].HistnqPRx[0]=0.75f;
				if (prbl[y][x].HistnqPRy[0]>0.75f) prbl[y][x].HistnqPRy[0]=0.75f;
				
				//limite a la bajada
				if (prbl[y][x].HistnqPRx[0]+0.25f<prbl[y][x].HistnqPRx[1]) prbl[y][x].HistnqPRx[0]=prbl[y][x].HistnqPRx[1]-0.25f;
				if (prbl[y][x].HistnqPRy[0]+0.25f<prbl[y][x].HistnqPRy[1]) prbl[y][x].HistnqPRy[0]=prbl[y][x].HistnqPRy[1]-0.25f;
			    //-------de momento esto es lo mejor. TRatamiento x e y separado pero relacionado
				//upgrade fin de mov
				
				if (prbl[y][x].HistnqPRx[0]==0 && prbl[y][x].HistnqPRx[1]>0 && prbl[y][x].HistnqPRx[1]<0.75)
					{
                    prbl[y][x].HistnqPRx[4]=0;//desactivacion
					prbl[y][x].HistnqPRx[9]=0;//counterdown activator
					prbl[y][x].HistnqPRx[8]=0;//counterdown counter
					
					prbl[y][x].PRx=0.75f;
					prbl[y][x].HistnqPRx[0]=0.75f;
					}
				if (prbl[y][x].HistnqPRy[0]==0 && prbl[y][x].HistnqPRy[1]>0 && prbl[y][x].HistnqPRy[1]<0.75)
				{
                    prbl[y][x].HistnqPRy[4]=0;
					prbl[y][x].HistnqPRy[9]=0;//counterdown activator
					prbl[y][x].HistnqPRy[8]=0;//counterdown counter	
					
				prbl[y][x].PRy=0.75f;
				prbl[y][x].HistnqPRy[0]=0.75f;
				}
				
				int countdown=1000000;
				//if (prbl[y][x].HistnqPRx[0]>0.5f) prbl[y][x].PRx=0.75f;else prbl[y][x].PRx=0.0f;
				//if (prbl[y][x].HistnqPRy[0]>0.5f) prbl[y][x].PRy=0.75f;else prbl[y][x].PRy=0.0f;
				
				if ((	prbl[y][x].HistnqPRx[4]==-1 &&//activo
						prbl[y][x].HistnqPRy[4]==-1 &&// no se porque pero ahorra un 2% y queda igual
						
						prbl[y][x].HistnqPRx[0]>=0.25 &&
						(prbl[y][x].HistnqPRx[0]<prbl[y][x].HistnqPRx[1]&& prbl[y][x].HistnqPRx[1]>=0.25 && prbl[y][x].HistnqPRx[1]<0.75)//||
						
						) || (prbl[y][x].HistnqPRx[8]>=countdown &&  prbl[y][x].HistnqPRx[1]<0.75))
				{
					
					//upgradex=true;
					
					
					prbl[y][x].HistnqPRx[4]=0;//desactivacion
					
					
					prbl[y][x].HistnqPRx[9]=0;//counterdown activator
					prbl[y][x].HistnqPRx[8]=0;//counterdown counter
					
					prbl[y][x].PRx=0.75f;//correccion
				    prbl[y][x].HistnqPRx[0]=0.75f;
				
				    
				    
				
				}
				if ((
						prbl[y][x].HistnqPRy[4]==-1 &&
						prbl[y][x].HistnqPRx[4]==-1 && //ahorra 2% y queda igual
						
						prbl[y][x].HistnqPRy[0]>=0.25 &&
						(prbl[y][x].HistnqPRy[0]<prbl[y][x].HistnqPRy[1] && prbl[y][x].HistnqPRy[1]>=0.25 && prbl[y][x].HistnqPRy[1]<0.75)  //||
					
				) || (prbl[y][x].HistnqPRy[8]>=countdown  &&  prbl[y][x].HistnqPRy[1]<0.75))
				{
					//upgradey=true;
					
					prbl[y][x].HistnqPRy[4]=0;
					
					prbl[y][x].HistnqPRy[9]=0;//counterdown activator
					prbl[y][x].HistnqPRy[8]=0;//counterdown counter
					
					prbl[y][x].PRy=0.75f;
					prbl[y][x].HistnqPRy[0]=0.75f;
					
					
					
				}
				 
				*/
				//-----------------
				/*
				if (
						prbl[y][x].HistnqPRx[4]==-1 &&
						prbl[y][x].HistnqPRy[4]==-1 &&
						
						prbl[y][x].HistnqPRx[0]>=0.25 &&
						(prbl[y][x].HistnqPRx[0]<prbl[y][x].HistnqPRx[1]&& prbl[y][x].HistnqPRx[1]>=0.25 && prbl[y][x].HistnqPRx[1]<0.75)//||
								
						&&
						prbl[y][x].HistnqPRy[0]>=0.25 &&
						(prbl[y][x].HistnqPRy[0]<prbl[y][x].HistnqPRy[1] && prbl[y][x].HistnqPRy[1]>=0.25 && prbl[y][x].HistnqPRy[1]<0.75)  //||
						
	                           )
						
				{
				
					prbl[y][x].HistnqPRx[4]=0;
					
					prbl[y][x].PRx=0.75f;//prbl[y][x].HistnqPRx[1]+0.26f;;//igualamos
				    prbl[y][x].HistnqPRx[0]=0.75f;
				
				
				
					prbl[y][x].HistnqPRy[4]=0;
					prbl[y][x].PRy=0.75f;
					prbl[y][x].HistnqPRy[0]=0.75f;
					
				}
				
				*/
				
				
				
				
				
				    //modifico PRx pero no la historia
				    // no selectivo por cuantos. si baja, retenemos,
				    //si sumo 0.25 me voy al cuanto superior. no es corregir 100%, como usar 1, pero se parece
			
				
				
				/*
				boolean hechox=false;
				boolean hechoy=false;
				   for (float u=0.75f;u>0;u-=0.25f)
				   {  
					if (prbl[y][x].HistnqPRx[0]< u && prbl[y][x].HistnqPRx[1]>u &&
							prbl[y][x].HistnqPRx[0]<prbl[y][x].HistnqPRx[1]) //si pr menor que la ant
					{
					prbl[y][x].PRx=0.75f;//prbl[y][x].HistnqPRx[1]+0.26f;;//igualamos
					//prbl[y][x].PRy=prbl[y][x].HistnqPRy[1];
					//prbl[y][x].HistnqPRx[0]=prbl[y][x].HistnqPRx[1];//igualamos la hist para la proxima
					hechox=true;
					}
					if (prbl[y][x].HistnqPRy[0]< u && prbl[y][x].HistnqPRy[1]>u &&
							prbl[y][x].HistnqPRy[0]<prbl[y][x].HistnqPRy[1])
					{
						//prbl[y][x].PRx=prbl[y][x].HistnqPRx[1];
						prbl[y][x].PRy=0.75f;//prbl[y][x].HistnqPRy[1]+0.26f;
						//prbl[y][x].HistnqPRy[0]=prbl[y][x].HistnqPRy[1];
						hechoy=true;
					}
						if (hechox && hechoy) break;
				   }
				*/
				
				/*
				float umbral=0.01f;
				float PRxant=grid_ant.prbl[y][x].nqPRx;
				if (prbl[y][x].PRx<PRxant)
				{
					if (prbl[y][x].PRx<PRxant-umbral) {
						prbl[y][x].PRx=PRxant-umbral;
						prbl[y][x].nqPRx=prbl[y][x].PRx;
					}
				}
				float PRyant=grid_ant.prbl[y][x].nqPRy;
				if (prbl[y][x].PRy<PRyant)
				{
					if (prbl[y][x].PRy<PRyant-umbral) 
						{
						prbl[y][x].PRy=PRyant-umbral;
						prbl[y][x].nqPRy=prbl[y][x].PRy;
						}
				}
				*/
				/*
				//otra forma: que la primera vez que baja no baje
				if (prbl[y][x].PRx<PRxant-0.25)
				{
						prbl[y][x].nqPRx=prbl[y][x].PRx;
					
				}
				*/
				
				
				
				
				
				
				//System.out.println("TUNEADO :"+prbl[y][x].nqPRx);//+"      despues:"+prbl[y][x].PRx);
				
			}//x
		}//y	
	}
//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
	public void setMaxPR()
	{
		// bucle filling PRblocks information
			
			for (int y=0;y<number_of_blocks_V+1;y++)
			{

				for (int x=0;x<number_of_blocks_H+1;x++)
				{
					
					prbl[y][x].PRx=0.5f;
					//prbl[y][x].nqPRx=1.0f;
					prbl[y][x].PRy=0.5f;
					//prbl[y][x].nqPRy=1.0f;
					
				}//x
				
			}//y

			//TO DO

		}
	//******************************************************************************
}//end class Grid
