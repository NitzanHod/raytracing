import java.awt.Transparency;
import java.awt.color.*;
import java.awt.image.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

/**
 *  Main class for ray tracing exercise.
 */
public class RayTracer {

	public int imageWidth;
	public int imageHeight;
	
	//camera parameters
	public Point3D cameraPos;
	public Point3D cameraLookAt;
	public double screenDist;
	public double screenWidth;
	public Vector3D cameraUpDir;
	
	//general settings
	public Vector3D backgroundColor;
	public short numShadowRays;
	public short maxRecursionLevel;
	public short superSamplingLevel=1;
	
	//scene
	private List<Element> elementList = new ArrayList<Element>();
	private List<LightSource> lights = new ArrayList<LightSource>();
	private List<Material> materials = new ArrayList<Material>();

	/**
	 * Runs the ray tracer. Takes scene file, output image file and image size as input.
	 */
	public static void main(String[] args) {

		try {

			RayTracer tracer = new RayTracer();

                        // Default values:
			tracer.imageWidth = 500;
			tracer.imageHeight = 500;

			if (args.length < 2)
				throw new RayTracerException("Not enough arguments provided. Please specify an input scene file and an output image file for rendering.");

			String sceneFileName = args[0];
			String outputFileName = args[1];

			if (args.length > 3)
			{
				tracer.imageWidth = Integer.parseInt(args[2]);
				tracer.imageHeight = Integer.parseInt(args[3]);
			}


			// Parse scene file:
			tracer.parseScene(sceneFileName);

			// Render scene:
			tracer.renderScene(outputFileName);

//		} catch (IOException e) {
//			System.out.println(e.getMessage());
		} catch (RayTracerException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}


	}

	/**
	 * Parses the scene file and creates the scene. Change this function so it generates the required objects.
	 */
	public void parseScene(String sceneFileName) throws IOException, RayTracerException
	{
		FileReader fr = new FileReader(sceneFileName);

		BufferedReader r = new BufferedReader(fr);
		String line = null;
		int lineNum = 0;
		System.out.println("Started parsing scene file " + sceneFileName);



		while ((line = r.readLine()) != null)
		{
			line = line.trim();
			++lineNum;

			if (line.isEmpty() || (line.charAt(0) == '#'))
			{  // This line in the scene file is a comment
				continue;
			}
			else
			{
				String code = line.substring(0, 3).toLowerCase();
				// Split according to white space characters:
				String[] params = line.substring(3).trim().toLowerCase().split("\\s+");

				if (code.equals("cam"))
				{
                                        // Add code here to parse camera parameters
					cameraPos = new Point3D(Double.parseDouble(params[0]),Double.parseDouble(params[1]),Double.parseDouble(params[2])); 
					cameraLookAt = new Point3D(Double.parseDouble(params[3]),Double.parseDouble(params[4]),Double.parseDouble(params[5]));
					cameraUpDir = new Vector3D(Double.parseDouble(params[6]),Double.parseDouble(params[7]),Double.parseDouble(params[8]));
					screenDist = Double.parseDouble(params[9]);
					screenWidth = Double.parseDouble(params[10]);
					System.out.println(String.format("Parsed camera parameters (line %d)", lineNum));
				}
				else if (code.equals("set"))
				{
                                        // Add code here to parse general settings parameters
					backgroundColor = new Vector3D(Double.parseDouble(params[0]),Double.parseDouble(params[1]),Double.parseDouble(params[2])); 
					numShadowRays = Short.parseShort(params[3]);
					maxRecursionLevel = Short.parseShort(params[4]);
					System.out.println(String.format("Parsed general settings (line %d)", lineNum));
				}
				else if (code.equals("mtl"))
				{
                                        // Add code here to parse material parameters
					Material temp = new Material();
					temp.diffuseColor = new Vector3D(Double.parseDouble(params[0]),Double.parseDouble(params[1]),Double.parseDouble(params[2])); 
					temp.specularColor = new Vector3D(Double.parseDouble(params[3]),Double.parseDouble(params[4]),Double.parseDouble(params[5])); 
					temp.reflectionColor = new Vector3D(Double.parseDouble(params[6]),Double.parseDouble(params[7]),Double.parseDouble(params[8])); 
					temp.phongSpecCoeff = Double.parseDouble(params[9]);
					temp.transperancy = Double.parseDouble(params[10]);
					materials.add(temp);
					System.out.println(String.format("Parsed material (line %d)", lineNum));
				}
				else if (code.equals("sph"))
				{
                                        // Add code here to parse sphere parameters

                                        // Example (you can implement this in many different ways!):
					// Sphere sphere = new Sphere();
                                        // sphere.setCenter(params[0], params[1], params[2]);
                                        // sphere.setRadius(params[3]);
                                        // sphere.setMaterial(params[4]);
					Sphere sphere = new Sphere(Integer.parseInt(params[4]),Double.parseDouble(params[0]),
							Double.parseDouble(params[1]),Double.parseDouble(params[2]),Double.parseDouble(params[3]));
					elementList.add(sphere);
					System.out.println(String.format("Parsed sphere (line %d)", lineNum));
				}
				else if (code.equals("pln"))
				{
                                        // Add code here to parse plane parameters

					System.out.println(String.format("Parsed plane (line %d)", lineNum));
				}
				else if (code.equals("lgt"))
				{
                                        // Add code here to parse light parameters
					LightSource light = new LightSource(Double.parseDouble(params[0]),Double.parseDouble(params[1]),Double.parseDouble(params[2]),
							Double.parseDouble(params[3]),Double.parseDouble(params[4]),Double.parseDouble(params[5]),
							Double.parseDouble(params[6]),Double.parseDouble(params[7]),
							Double.parseDouble(params[8]));
					
					lights.add(light);
					System.out.println(String.format("Parsed light (line %d)", lineNum));
				}
				else
				{
					System.out.println(String.format("ERROR: Did not recognize object: %s (line %d)", code, lineNum));
				}
			}
		}

                // It is recommended that you check here that the scene is valid,
                // for example camera settings and all necessary materials were defined.

		System.out.println("Finished parsing scene file " + sceneFileName);

	}
	
	public Vector3D getColor (Point3D origin, Vector3D rayDir, int currentElementIdx, int numOfIterations) {
		
		double min_t = -1;
		double temp_t;
		int minElement=-1;

		//intersect with objects, and save object color with closest intersection point
		for (int k=0; k<elementList.size(); k++) {
			if (k==currentElementIdx) {continue;}
			temp_t = elementList.get(k).IntersectWithRay(origin, rayDir);
			if ((temp_t>0 && temp_t<min_t) || min_t==-1) {
				min_t = temp_t;
				minElement=k;
			}
		}
		
		//color = (background_color)*transparency + (diffuse+specular)*(1-transparency) + reflection_color
		//for each intersection 
		//calculate reflection color
		//calculate transparency color
		// define function that calculates color given a point, element, and ray direction
		
		
		Point3D intersectionPoint = origin.Move(rayDir.scale(min_t));
		
		Vector3D DiffueSpecOutputColor = new Vector3D(0,0,0);
		
		int matIdx = elementList.get(minElement).matIdx-1;
		
		if (min_t<0 || numOfIterations>=maxRecursionLevel) { //no hits or max recursion level
			return backgroundColor;
		} 
		
		//send ray from point of intersection in object to light sources to calculate lighting
			
		//calculate diffuse+specular
		//TODO: implement soft shadows
		for (int l=0; l<lights.size(); l++) {
			
			boolean shadowFlag = false;
			
			//calculate t required for full segment
			Vector3D dir = new Vector3D(intersectionPoint,lights.get(l).position);
			double req_t = dir.length();
			dir.normalize();
			
			for (int k=0; k<elementList.size(); k++) {
				if (k==minElement) {
					continue;
				}
				
				if (elementList.get(k).IntersectWithRay(lights.get(l).position, dir)>0 && elementList.get(k).IntersectWithRay(lights.get(l).position, dir)<req_t) {
					shadowFlag = true;
					break;
				}
			}

			
			if (!shadowFlag) { //if light hits point
				
				//light hits object, calculate lighting using phong model
				Vector3D L = new Vector3D(intersectionPoint,lights.get(l).position);
				L.normalize();
				Vector3D N = elementList.get(minElement).getNormalAt(intersectionPoint);
				Vector3D V = new Vector3D(cameraPos,intersectionPoint);
				V.normalize();
				Vector3D R = N.scale(2*N.dot(L)).subtract(L); 
				R.normalize();
				double specular = V.dot(R);
				
				if ( N.dot(L)>0 && lights.get(l).shadowIntensity>0 ) { //self occlusion
					DiffueSpecOutputColor = DiffueSpecOutputColor.add(materials.get(matIdx).diffuseColor.scale(1-lights.get(l).shadowIntensity));
				} else {
					DiffueSpecOutputColor = DiffueSpecOutputColor.add((lights.get(l).color.multByVec(materials.get(matIdx).diffuseColor).add
						(materials.get(matIdx).specularColor.scale(lights.get(l).specularIntensity*Math.pow(specular,materials.get(matIdx).phongSpecCoeff))))); 

				}
			} else if (lights.get(l).shadowIntensity>0) {
				DiffueSpecOutputColor = DiffueSpecOutputColor.add(materials.get(matIdx).diffuseColor.scale(1-lights.get(l).shadowIntensity));
			}	
	}	
		
		//calculate reflection color
		Vector3D N = elementList.get(minElement).getNormalAt(intersectionPoint);
		Vector3D reflectedDir = N.scale(2*N.dot(rayDir.scale(-1))).subtract(rayDir.scale(-1)); 
		reflectedDir.normalize();
		Vector3D reflectionColor = getColor(intersectionPoint,reflectedDir,minElement,numOfIterations+1);
		reflectionColor = reflectionColor.multByVec(materials.get(matIdx).reflectionColor);
		//TODO: do not reflect background
		
		//calculate transparency Color
		Vector3D transColor = backgroundColor;
		if (materials.get(matIdx).transperancy>0) {
		 transColor = getColor(intersectionPoint,rayDir,minElement,numOfIterations+1);
		 
		}
		
		double transVal = materials.get(matIdx).transperancy;
		return transColor.scale(transVal).add(DiffueSpecOutputColor.scale(1.0-transVal)).add(reflectionColor);
	//	return DiffueSpecOutputColor;
	}

	/**
	 * Renders the loaded scene and saves it to the specified file location.
	 */
	public void renderScene(String outputFileName)
	{
		long startTime = System.currentTimeMillis();

		// Create a byte array to hold the pixel data:
		byte[] rgbData = new byte[this.imageWidth * this.imageHeight * 3];

                // Put your ray tracing code here!
                //
                // Write pixel color values in RGB format to rgbData:
                // Pixel [x, y] red component is in rgbData[(y * this.imageWidth + x) * 3]
                //            green component is in rgbData[(y * this.imageWidth + x) * 3 + 1]
                //             blue component is in rgbData[(y * this.imageWidth + x) * 3 + 2]
                //
                // Each of the red, green and blue components should be a byte, i.e. 0-255

		Vector3D t = new Vector3D(cameraLookAt.x-cameraPos.x,cameraLookAt.y-cameraPos.y,cameraLookAt.z-cameraPos.z);
		t.normalize();
		
		//calculate screen height using aspect ratio
		
		//calculate screen direction vectors - which are the up vector and the cross product between the up vector and center ray
		
		Vector3D b = cameraUpDir.cross(t);
		b.normalize();
		
		Vector3D v = t.cross(b);
		
		double gx = screenWidth/2; double gy=imageHeight/imageWidth*gx;
		
		Vector3D qx = b.scale(2*gx/(imageWidth-1)); Vector3D qy = v.scale(2*gy/(imageHeight-1));
		
		Vector3D p1m = t.scale(screenDist).subtract(b.scale(gx)).subtract(v.scale(gy));
	
				
		//for each pixel in the screen
			for (int i=0; i<this.imageWidth; i++) {
				for (int j=0; j<this.imageHeight; j++) {
										
					// calculate ray from camera that passes through points in pixel
					Vector3D pij = p1m.add(qx.scale(i)).add(qy.scale(j));
					pij.normalize(); //=rij
					
					Vector3D outputColor = getColor(cameraPos,pij,-1,0);

					int x=this.imageWidth-1-i;
					int y=this.imageHeight-1-j;
					rgbData[(y * this.imageWidth + x) * 3] = (byte) (outputColor.x*255<=255? outputColor.x*255:255);
					rgbData[(y * this.imageWidth + x) * 3+1] = (byte) (outputColor.y*255<=255? outputColor.y*255:255);
					rgbData[(y * this.imageWidth + x) * 3+2] = (byte) (outputColor.z*255<=255? outputColor.z*255:255);
					
				}
			}

		long endTime = System.currentTimeMillis();
		Long renderTime = endTime - startTime;

                // The time is measured for your own conveniece, rendering speed will not affect your score
                // unless it is exceptionally slow (more than a couple of minutes)
		System.out.println("Finished rendering scene in " + renderTime.toString() + " milliseconds.");

                // This is already implemented, and should work without adding any code.
		saveImage(this.imageWidth, rgbData, outputFileName);

		System.out.println("Saved file " + outputFileName);

	}




	//////////////////////// FUNCTIONS TO SAVE IMAGES IN PNG FORMAT //////////////////////////////////////////

	/*
	 * Saves RGB data as an image in png format to the specified location.
	 */
	public static void saveImage(int width, byte[] rgbData, String fileName)
	{
		try {

			BufferedImage image = bytes2RGB(width, rgbData);
			ImageIO.write(image, "png", new File(fileName));

		} catch (IOException e) {
			System.out.println("ERROR SAVING FILE: " + e.getMessage());
		}

	}

	/*
	 * Producing a BufferedImage that can be saved as png from a byte array of RGB values.
	 */
	public static BufferedImage bytes2RGB(int width, byte[] buffer) {
	    int height = buffer.length / width / 3;
	    ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_sRGB);
	    ColorModel cm = new ComponentColorModel(cs, false, false,
	            Transparency.OPAQUE, DataBuffer.TYPE_BYTE);
	    SampleModel sm = cm.createCompatibleSampleModel(width, height);
	    DataBufferByte db = new DataBufferByte(buffer, width * height);
	    WritableRaster raster = Raster.createWritableRaster(sm, db, null);
	    BufferedImage result = new BufferedImage(cm, raster, false, null);

	    return result;
	}

	public static class RayTracerException extends Exception {
		public RayTracerException(String msg) {  super(msg); }
	}


}
