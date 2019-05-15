/*
This class implements a simple point light source in 3D space
*/


public class LightSource {
	
	Point3D position;
	Vector3D color;
	double specularIntensity;
	double shadowIntensity;
	double lightRadius;
	
	public LightSource(double x, double y, double z, double r, double g, double b, double spi, double shi, double rad) {
		position = new Point3D(x,y,z);
		color = new Vector3D(r,g,b);
		specularIntensity = spi;
		shadowIntensity = shi;
		lightRadius = rad;
		
	}

}
