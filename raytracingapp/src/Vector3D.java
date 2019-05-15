/*
This class implements a simple vector in 3D space
*/
public class Vector3D extends Point3D{
	
	double x;
	double y;
	double z;
	
        // construct a vector with the origin as its tail
        //and a given point as its head
	Vector3D (double d, double e, double f) {
		super(d,e,f);
	}
     

        // construct a vector by 2 given points
	Vector3D (Point3D p1, Point3D p2) { //p1- arrow, p2-tail
		this(p1.x-p2.x, p1.y-p2.y, p1.z-p2.z);
	}
	
	// vector addition
	public Vector3D add(Vector3D vec) {
		
		return new Vector3D(this.x+vec.x,this.y+vec.y,this.z+vec.z);
		
	}
	
        // vector subtraction
	public Vector3D subtract(Vector3D vec) {
		
		return new Vector3D(this.x-vec.x,this.y-vec.y,this.z-vec.z);
		
	}
        
        // normalize a vector to a unit length
	public Vector3D normalize() {
		
		double size = length();
		
		x = x/size; y=y/size; z=z/size;
                
                return this;
		
	}
	
        // scale a vector by a factor
	public Vector3D scale(double d) {
		return new Vector3D(x*d,y*d,z*d);
	}
	
        // cross product with a given vector
	public Vector3D cross(Vector3D vec) {
		return new Vector3D(y*vec.z-z*vec.y,(z*vec.x-x*vec.z),x*vec.y-y*vec.x);
	}
	
        // elementwise multiplication with a given vector
	public Vector3D multByVec (Vector3D vec) {
		return new Vector3D(x*vec.x,y*vec.y,z*vec.z);
	}
	
        // dot product with a given vector
	public double dot (Point3D vec) {
		return (vec.x*x+vec.y*y+vec.z*z);
	}
	
	public double length() {
		return Math.sqrt(x*x+y*y+z*z);
	}

}
