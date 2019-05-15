/*
This class implements a simple point in 3D space
*/
public class Point3D {
	
	double x; double y; double z;
	
	public Point3D(double x,double y, double z) {
		this.x=x;
		this.y=y;
		this.z=z;
	}
	
        // calculates the new point acheived by offsetting the point by a given vector
	public Point3D Move(Vector3D vec) {
		return new Point3D (x+vec.x,y+vec.y,z+vec.z);
	}

}
