package LHE;


/**
 * PRblock
 * 
 * @author josejavg
 *
 * PRBlock stores PR values of the center ( float values)
 *
 * after quantization, 4 possible values for each coordinate are allowed: 
 * therefore, 2 bits x 2 coord = 4 bits per PRblock
 * 
 *     +---------------------+
 *     |                     |
 *     |                     |
 *     |                     |
 *     |     (PRx,PRy)       |
 *     |         .           |
 *     |                     |
 *     |                     |
 *     |                     |
 *     +---------------------+
 *     
 */

public class PRblock {

	//static
	public static ImgUtil img;

	//shape
	public int xini;
	public int yini;
	public int xfin;
	public int yfin;

	//PR at the center , normalized values from 0..1
	public float PRx;
	public float PRy;
	
	//PR values non quantized
	public float nqPRx;
	public float nqPRy;
	//PR values non quantized
	public float[] HistnqPRx;
	public float[] HistnqPRy;
	//******************************************************************
	public void quantizeGeometricalPR()
	{
		
		//previous backup of non-quantized PR values
		
		//nqPRx=PRx;
		//nqPRy=PRy;
		
		
		//System.out.println(" PR:"+PRx);
		//quantization based on equi-distant thresholds
		float levelnull=0.125f;//125f;//125f;//125f;//125f;//325f;//0.125f;//0.125f;//125f;//125f;//125f;//125f;//0.5f;//0.25f;
		//float levelmax=0.875f;//0.5f;//0.25f;
		
		float level0=0.25f;//25f;//0.5f;//0.25f;
		float level1=0.5f;//0.5f;//1.4f;//0.50f;
		float level2=0.75f;//0.75f;//1.5f;//0.75f;

		//de este modo cada intervalo gasta lo mismo que el anterior. doble rango pero mitad de ppp
		//level0=0.40f;//0.5f;//0.35f;//0.40f;//0.5f;//0.5f;//0.40f;
		//level1=0.65f;//0.75f;//0.70f;//0.65f;//0.7f;////0.75f;//0.7
		//level2=0.85f;//0.875f;//0.90f;//0.85f;//0.95f;//0.90f;//0.9
		
		
		float cuanto0=0.0f;
		float cuanto1=0.125f;//25f;
		float cuanto2=0.25f;//0.25f;;
		float cuanto3=0.5f;
		float cuanto4=1f;
		/*
		 cuanto0=(levelnull)/2f;;
		 cuanto1=(level0-levelnull)/2f;;
		 cuanto2=(level1-level0)/2f;;
		 cuanto3=(level2-level1)/2f;;
		 cuanto4=(1f-level2)/2f;
		*/
		/* cuanto0=0.0f;
		 cuanto1=0.11f;
		 cuanto2=0.33f;
		 cuanto3=0.66f;
		 cuanto4=1f;
		 */
		//PRx=(float)Math.sqrt(PRx);
		//PRy=(float)Math.sqrt(PRy);
		
		
		
		
		if (PRx<levelnull)PRx=cuanto0;//0.00f;
		else if (PRx<level0) PRx=cuanto1;//0.125f;//level0/2f;
		else if (PRx<level1) PRx=cuanto2;//0.25f;
		else if (PRx<level2) PRx=cuanto3;//0.5f;
		//else if (PRx<levelmax) PRx=0.75f;
		else PRx=cuanto4;//1f;//(1f+level2)/2f;

		if (PRy<levelnull)PRy=cuanto0;//0.00f;
		else if (PRy<level0) PRy=cuanto1;//0.125f;//level0/2f;
		else if (PRy<level1) PRy=cuanto2;//0.25f;
		else if (PRy<level2) PRy=cuanto3;//0.5f;
		//else if (PRy<levelmax) PRy=0.75f;
		else PRy=cuanto4;//1f;//(1f+level2)/2f;

		
		
		
		
	}
	//******************************************************************
	//******************************************************************
		public void quantizeGeometricalPR_ok()
		{
			//quantization based on equi-distant thresholds
			float level0=0.25f;
			float level1=0.50f;
			float level2=0.75f;

			//de este modo cada intervalo gasta lo mismo que el anterior. doble rango pero mitad de ppp
			//level0=0.40f;//0.5f;//0.35f;//0.40f;//0.5f;//0.5f;//0.40f;
			//level1=0.65f;//0.75f;//0.70f;//0.65f;//0.7f;////0.75f;//0.7
			//level2=0.85f;//0.875f;//0.90f;//0.85f;//0.95f;//0.90f;//0.9
			
			if (PRx<level0) PRx=0.125f;//level0/2f;
			else if (PRx<level1) PRx=0.25f;
			else if (PRx<level2) PRx=0.5f;
			else PRx=1f;//(1f+level2)/2f;

			if (PRy<level0) PRy=0.125f;//level0/2f;
			else if (PRy<level1) PRy=0.25f;
			else if (PRy<level2) PRy=0.5f;
			else PRy=1f;//(1f+level2)/2f;

		}
		//******************************************************************
	public void quantizeLinearPR()
	{
		//quantization based on geometrical progression of thresholds with ratio=2
		float level0=0.25f;
		float level1=0.50f;
		float level2=0.75f;

		if (PRx<level0) PRx=level0/2f;
		else if (PRx<level1) PRx=(level1+level0)/2f;
		else if (PRx<level2) PRx=(level2+level1)/2f;
		else PRx=1f;//(1f+level2)/2f;

		if (PRy<level0) PRy=level0/2f;
		else if (PRy<level1) PRy=(level1+level0)/2f;
		else if (PRy<level2) PRy=(level2+level1)/2f;
		else PRy=1f;//(1f+level2)/2f;

	}
	public void quantizePureLinearPR()
	{
		//quantization based on geometrical progression of thresholds with ratio=2
		float level0=0.25f;
		float level1=0.50f;
		float level2=0.75f;

		if (PRx<level0) PRx=level0/2f;
		else if (PRx<level1) PRx=(level1+level0)/2f;
		else if (PRx<level2) PRx=(level2+level1)/2f;
		else PRx=(1f+level2)/2f;

		if (PRy<level0) PRy=level0/2f;
		else if (PRy<level1) PRy=(level1+level0)/2f;
		else if (PRy<level2) PRy=(level2+level1)/2f;
		else PRy=(1f+level2)/2f;

	}
	//*********************
	//******************************************************************
		public void quantizeRarePR()
		{
			//quantization based on geometrical progression of thresholds with ratio=2
			float level0=0.25f;
			float level1=0.50f;
			float level2=0.75f;

			if (PRx<level0) PRx=0.1f;
			else if (PRx<level1) PRx=0.3f;
			else if (PRx<level2) PRx=0.6f;
			else PRx=1f;//(1f+level2)/2f;

			if (PRy<level0) PRy=0.1f;
			else if (PRy<level1) PRy=0.3f;
			else if (PRy<level2) PRy=0.6f;
			else PRy=1f;//(1f+level2)/2f;

		}
	//******************************************************************	
	public void computePRmetrics()
	{
		//PR computation normalized to 0..1

		//Grid class creates a grid of Blocks and PRBlocks.
		//PRBlocks coordinates are in 0..width and 0..height
		int last_hop=0;
		int hop=0;
		boolean hop_sign=true;
		boolean last_hop_sign=true;
		PRx=0;
		PRy=0;

		//float tune=1.0f;
		//PRx

		float Cx=0;
		float Cy=0; 


		for (int y=yini;y<=yfin;y++)
		{
			if (y>0)
			{
				last_hop=img.hops[0][(y-1)*img.width+xini]-4;
				//last_hop_sign=(last_hop>0);
				last_hop_sign=(last_hop>=0); //NUEVO 19/3/2015 
				//System.out.println(" signo:"+last_hop_sign+ "hop:"+last_hop);
				
			}
			//horizontal scanlines
			for (int x=xini;x<=xfin;x++)
			{
				
				hop=img.hops[0][y*img.width+x]-4;//[0] is lumminance (Y)
				// hop value: -4....0....+4
				if (hop==0) continue; //h0 no sign
				//hop_sign=(hop>0);
				hop_sign=(hop>=0);//NUEVO 19/3/2015 
				//if (hop>0) hop_sign=true;
				//else hop_sign=false;
				//if (hop_sign!=last_hop_sign && last_hop!=0) {

				if ((hop_sign!=last_hop_sign && last_hop!=0) || hop==4 || hop==-4) {//NUEVO 19/3/2015 
				
				//if ((hop_sign!=last_hop_sign && last_hop!=0) || hop>=3 || hop<=-3) {//NUEVO 19/3/2015 
						
					int weight=hop;
					if (weight<0)weight=-weight;
					//int weight=hop;
					//if (hop%2!=0) weight--; // possitive and negative must weigh the same [0..2..4..6]
					//weight=8-hop; //from 2 to 8
					//if (weight>7 ) tune=1;
					//else tune=1;
					//antes era 0 2 4 6, osea...max 6
					//int w=0;
					//if (weight!=0) w=4;
					//PRx+=w;//weight;
					PRx+=weight;
					//System.out.println ("hop:"+hop+"  lasth:"+last_hop+ "x:"+x);
					Cx++;

				}
				//System.out.println("scan");
				last_hop=hop;
				last_hop_sign=hop_sign;
			}
			//System.out.println("scan");
		}

		hop=0;
		hop_sign=true;
		last_hop=0;
		last_hop_sign=true;

		//System.out.println(" processing vscanlines  img.width"+img.width+"   xini:"+xini+"   xfin:"+xfin+"  yini:"+yini+"    yfin:"+yfin);
		for (int x=xini;x<=xfin;x++)
		{
			//System.out.println("x:"+x);
			if (x>0)
			{
			    //left pixel
				last_hop=img.hops[0][(yini)*img.width+x-1]-4;
				//last_hop_sign=(last_hop>0);
				last_hop_sign=(last_hop>=0);//NUEVO 19/3/2015 
			}
			//vertical scanlines
			for (int y=yini;y<=yfin;y++)
			{

				hop=img.hops[0][y*img.width+x]-4;//[0] is lumminance (Y)
				if (hop==0) continue; //h0 no sign
				//hop_sign=(hop>0);
				hop_sign=(hop>=0);//NUEVO 19/3/2015 
				//if (hop%2!=last_hop%2 && last_hop!=8) {
				//if (hop_sign!=last_hop_sign && last_hop!=0) {
				
				if ((hop_sign!=last_hop_sign && last_hop!=0) || hop==4 || hop==-4){
				//if ((hop_sign!=last_hop_sign && last_hop!=0) || hop>=3 || hop<=-3){
					//if ((hop_sign!=last_hop_sign && last_hop!=0) || hop!=4){
							
					
					int weight=hop;
					if (weight<0)weight=-weight;
					//if (hop%2!=0) weight--; // possitive and negative must weigh the same [0..2..4..6]
					//weight=8-hop; //from 2 to 8
					//int w=0;
					//if (weight!=0) w=4;
					//PRy+=w;//weight;
					PRy+=weight;
					//System.out.println ("hop:"+hop+"  lasth:"+last_hop);
					Cy++;
				}
				last_hop=hop;
				last_hop_sign=hop_sign;
			}

		}

		//System.out.println("numerador:"+PRx+" denominador:"+Cx*4);
		if (PRx>0) PRx=PRx/(Cx*4);
		if (PRy>0) PRy=PRy/(Cy*4);

		//if (PRx>0) PRx=PRx/((xfin-xini)*(yfin-yini)*4);
		//if (PRy>0) PRy=PRy/((xfin-xini)*(yfin-yini)*4);
		
		//if (PRx<0.01f || PRy<0.01f) System.out.println("NOTHING");
		if (PRx>1 || PRy>1) {
			System.out.println(" failure. imposible PR value > 1");
			System.exit(0);
		}

		//if (PRx==0.0) {
			//System.out.println("ha salido cero");
			//System.exit(0);
		//}
		//else System.out.println("no es cero");
		
    	//System.out.println("PRx:"+PRx+"  , PRy:"+PRy+"   xini:"+xini+"  xfin:"+xfin+"  yini:"+yini+"  yfin:"+yfin);
		//System.out.println((int)(PRx*100));
		//System.out.println((int)(PRy*100));
		float umbral=10.5f;//0.5f; //LATER
		if (PRx>umbral) PRx=0.5f;//1;
		if (PRy>umbral) PRy=0.5f;//1;
		
		//PRx=1-PRx;
		//PRy=1-PRy;
		
		//nqPRx=PRx;
		//nqPRy=PRy;
	}
	//*******************************************
	public void quantizeNaturalPR()
	{
		//quantization based on geometrical progression of thresholds with ratio=2
		float level0=0.2f;
		float level1=0.3f;
		float level2=0.4f;

		if (PRx<level0) PRx=0.125f;//level0/2f;
		else if (PRx<level1) PRx=0.25f;
		else if (PRx<level2) PRx=0.35f;
		else PRx=1f;//(1f+level2)/2f;

		if (PRy<level0) PRy=0.125f;//level0/2f;
		else if (PRy<level1) PRy=0.25f;
		else if (PRy<level2) PRy=0.35f;
		else PRy=1f;//(1f+level2)/2f;

	}
}
