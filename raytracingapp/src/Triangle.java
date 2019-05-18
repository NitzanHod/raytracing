public class Triangle extends Element {

	private	Vector3D	v0, v1, v2;
	private Vector3D normal;

	Triangle(int matIdx) {
		super(matIdx);
		// TODO Auto-generated constructor stub
	}

	Triangle (int matIdx, Point3D a, Point3D b, Point3D c){
		super(matIdx);
		v0 = new Vector3D(a);
		v1 = new Vector3D(b);
		v2 = new Vector3D(c);
		normal = (new Vector3D((v1.subtract(v0)).cross(v2.subtract(v0)).normalize()));
	}


	@Override
	public double IntersectWithRay(Point3D origin, Vector3D dir) {
		double a = v0.x - v1.x, b = v0.x - v2.x, c = dir.x, d = v0.x - origin.x;
		double e = v0.y - v1.y, f = v0.y - v2.y, g = dir.y, h = v0.y - origin.y;
		double i = v0.z - v1.z, j = v0.z - v2.z, k = dir.z, l = v0.z - origin.z;

		double m = f * k - g * j, n = h * k - g * l, p = f * l - h * j;
		double q = g * i - e * k, s = e * j - f * i;

		double inv_denom  = 1.0 / (a * m + b * q + c * s);

		double e1 = d * m - b * n - c * p;
		double beta = e1 * inv_denom;

		if (beta < 0.0) {
			return -1;  // no intersection
		}

		double r = e * l - h * i;
		double e2 = a * n + d * q + c * r;
		double gamma = e2 * inv_denom;

		if (gamma < 0.0 ) {
			return -1;  // no intersection
		}

		if (beta + gamma > 1.0) {
			return -1;  // no intersection
		}

		double e3 = a * p - b * r + d * s;
		double t = e3 * inv_denom;

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
		return normal;
	}



	public boolean equals(Object obj) {
		if (obj == null || this.getClass() != obj.getClass()) {
			return false;
		}
		else {
			Triangle other = (Triangle)obj;
			return (super.equals(obj)
					&& v0.equals(other.v0) && v1.equals(other.v1)
					&& v2.equals(other.v2) && normal.equals(other.normal));
		}
	}

	public String toString() {
		return "triangle: [" + v0 + ", "+ v1 + ", " + v2 + "] with " + normal;
	}

}