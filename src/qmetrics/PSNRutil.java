package qmetrics;

public class PSNRutil {

	/**
	 * esta funcion va a calcular el MSE ( error cuadratico medio) de dos se�ales, en los limites 
	 * que digamos
	 * @param src
	 * @return
	 */
	public double getMSE(int[] img1, int[] img2, int x1,int x2,int y1,int y2, int width)
	{
		
		double mse=0;
		double error_medio=0;
	    for (int y=y1;y<=y2;y++)
		{
			for (int x=x1;x<=x2;x++)
			{

				mse+=(img1[y*width+x]-img2[y*width+x])*(img1[y*width+x]-img2[y*width+x]);
			//	error_medio+=img1[y*width+x]-img2[y*width+x];
			}

		}
		mse=mse/((x2-x1+1)*(y2-y1+1));

		//System.out.println("error medio"+error_medio/(512f*512f));

		return mse;
	}
//****************************************************************************
	public double getPSNR(double mse)
	{
		double psnr=0;
		psnr=20f *Math.log10(255f/Math.sqrt(mse));
		
		return psnr;
	}
//*************************************************************************
}
