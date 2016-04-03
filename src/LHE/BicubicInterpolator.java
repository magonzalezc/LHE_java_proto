package LHE;

public class BicubicInterpolator 
{ 
	
	//este array es privado. No hay que rellenarlo
	private double[] arr = new double[4];

      //funcion de interpolacion cubica
      public static double getValue (double[] p, double x) {
		return p[1] + 0.5 * x*(p[2] - p[0] + x*(2.0*p[0] - 5.0*p[1] + 4.0*p[2] - p[3] + x*(3.0*(p[1] - p[2]) + p[3] - p[0])));
	}

      //funcion de interpolacion cubica
      public static int getValue (float[] p, float x) {
		return (int)( 0.5f+ p[1] + 0.5 * x*(p[2] - p[0] + x*(2.0*p[0] - 5.0*p[1] + 4.0*p[2] - p[3] + x*(3.0*(p[1] - p[2]) + p[3] - p[0]))));
	}
  //funcion de interpolacion bicubica
  //el parametro de entrada es p, que es un array de 4x4
  //la x y la y es un valor entre 0 y 1
	public double getValue (double[][] p, double x, double y) {
		arr[0] = getValue(p[0], y);
		arr[1] = getValue(p[1], y);
		arr[2] = getValue(p[2], y);
		arr[3] = getValue(p[3], y);
		return getValue(arr, x);
	}
		
}//end class