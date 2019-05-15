public class Plane extends Element {

        private final double a, b, c, d;
	private final Vector3D normal;
        
	Plane(int matIdx, Vector3D normal, double offset) {
		super(matIdx);
                this.normal = normal;
                this.a = normal.x;
                this.b = normal.y;
                this.c = normal.z;
                this.d = offset;
		
	}
        
        Plane(int matIdx, double a, double b, double c, double d) {
		super(matIdx);
                this.normal = new Vector3D(a,b,c).normalize();
                this.a = a;
                this.b = b;
                this.c = c;
                this.d = d;
	}
        

	@Override
	public double IntersectWithRay(Point3D origin, Vector3D dir) {
		double denominator = normal.dot(dir);
		
                if(denominator == 0.0) return -1;

		double t = - (normal.dot(origin) + d) / denominator;

		if(t < 0) return -1;
                
		return t;
	}

	@Override
	public boolean IntersectsWithLine(Point3D origin, Point3D target) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Vector3D getNormalAt(Point3D p) {
		// TODO Auto-generated method stub
		return null;
	}

}
