public abstract class Element {
	
	int matIdx;
	
	Element(int matIdx) {
		this.matIdx = matIdx;
	}
	
	public abstract double IntersectWithRay(Point3D origin, Vector3D dir);
	
	public abstract boolean IntersectsWithLine(Point3D origin, Point3D target);
	
	public abstract Vector3D getNormalAt(Point3D p);

	
	 

}
