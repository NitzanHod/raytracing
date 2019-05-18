public class Sphere extends Element {

	Sphere(int matIdx, double px, double py, double pz, double r) {
		super(matIdx);
		position = new Point3D(px,py,pz);
		radius = r;
		// TODO Auto-generated constructor stub
	}
   

	public Point3D position;
	public double radius;
	
        @Override
	public double IntersectWithRay(Point3D origin, Vector3D dir) {

		//check that distance between vector and line is less than the radius
		Vector3D helper = new Vector3D(origin,position); //E-C == vector from center of sphere to camera position
		//System.out.println("helper"+helper.x+" "+helper.y+" "+helper.z);
		//System.out.println("dir"+dir.x+" "+dir.y+" "+dir.z);
		double a = dir.dot(dir); //must always be positive
		double b = 2*(dir.dot(helper));
		double c = helper.dot(helper)-radius*radius;
		//System.out.println("sphere ray intersection "+a+" "+b+" "+c);
		double discreminant = b*b-4*a*c;
		//System.out.println("discreminant"+discreminant);
		if (discreminant<0) {return -1;}
		
		double t1 = (-b+Math.sqrt(discreminant))/(2*a);
		
		double t2 = (-b-Math.sqrt(discreminant))/(2*a);
		
		if (t1<0) {
			return -1;
		}
		if (t2>0) {
			return Math.min(t1, t2);
		}
		
		return t1;
		
	}

	@Override
	public boolean IntersectsWithLine(Point3D origin, Point3D target) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Vector3D getNormalAt(Point3D p) {
		// TODO Auto-generated method stub
		 
		Vector3D temp =	new Vector3D(p.x-position.x,p.y-position.y,p.z-position.z);
		temp.normalize();
			
		return temp;
	}
	
}
