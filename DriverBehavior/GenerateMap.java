import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONException;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.ShapeFeature;
import de.fhpotsdam.unfolding.data.Feature.FeatureType;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.SimpleLinesMarker;
import de.fhpotsdam.unfolding.providers.OpenStreetMap.OpenStreetMapProvider;
import de.fhpotsdam.unfolding.utils.MapUtils;

import processing.core.PApplet;

@SuppressWarnings("serial")
public class GenerateMap extends PApplet {
	
	private static final String CONFIG_FILE = "config/mapview.json";
	private static final String INPUT_FILE = "data/CarProbeSample.json";

	
	private int WIDTH = 1000;
	private int HEIGHT = 700;
	
	private static List<CarProbe> carProbes;
	private static List<DriverBehaviorDetail> driverBehaviorDetails;
	
	private UnfoldingMap map;
	private List<ToolTipMarker> toolTipMarkers;
	
	public void setup() {
		size(WIDTH, HEIGHT, P3D);
		map = new UnfoldingMap(this, new OpenStreetMapProvider());		
		DriverBehaviorDetail first = driverBehaviorDetails.get(0);		
		map.zoomAndPanTo(14, new Location(first.startLatitude, first.startLongitude));	
		MapUtils.createDefaultEventDispatcher(this, map);
		
		traceTrip();
		traceBehavior();
		markBehaviorStartEnd();
	}
	
	public void draw() {
		map.draw();
	}
	
	public void mouseMoved() {
		for (ToolTipMarker marker: toolTipMarkers) {
			marker.setShowToolTip(marker.isInside(map, mouseX, mouseY));
		}
	}

	private void traceTrip() {
	    ShapeFeature tripFeature = new ShapeFeature(FeatureType.LINES);
		for (CarProbe carProbe: carProbes) {
			Location location = new Location(carProbe.latitude, carProbe.longitude);
			tripFeature.addLocation(location);
		}
		SimpleLinesMarker tripMarker = new SimpleLinesMarker(tripFeature.getLocations());
	    tripMarker.setColor(color(255, 64, 64, 200));
	    tripMarker.setStrokeWeight(5);
	    map.addMarker(tripMarker);		
	}
	
	private void traceBehavior() {
		for (int i = 0; i < driverBehaviorDetails.size(); i++) {
			ShapeFeature behaviorFeature = new ShapeFeature(FeatureType.LINES);
			DriverBehaviorDetail behavior = driverBehaviorDetails.get(i);
			Location startLocation = new Location(behavior.startLatitude, behavior.startLongitude);
			behaviorFeature.addLocation(startLocation);
			Location endLocation = new Location(behavior.endLatitude, behavior.endLongitude);
			behaviorFeature.addLocation(endLocation);
			SimpleLinesMarker behaviorMarker = new SimpleLinesMarker(behaviorFeature.getLocations());
		    behaviorMarker.setColor(color(128, 255, 128, 200));
		    behaviorMarker.setStrokeWeight(5);
		    map.addMarker(behaviorMarker);
		}		
	}
	
	private void markBehaviorStartEnd() {
		toolTipMarkers = new ArrayList<ToolTipMarker>();
		for (int i = 0; i < driverBehaviorDetails.size(); i++) {
			DriverBehaviorDetail behavior = driverBehaviorDetails.get(i);
			
			List<ContextFeature> contextFeatures = behavior.contextFeatures;
			String contextFeaturesString = "";
			for (int j = 0; j < contextFeatures.size(); j++) {
				contextFeaturesString += contextFeatures.get(j) + "\n";
			}
			
			Location startLocation = new Location(behavior.startLatitude, behavior.startLongitude);
			ToolTipMarker startToolTipMarker = new ToolTipMarker(
					startLocation,
					contextFeaturesString + behavior.behaviorName + " start ");		
			map.addMarker(startToolTipMarker);
			toolTipMarkers.add(startToolTipMarker);
			
			Location endLocation = new Location(behavior.endLatitude, behavior.endLongitude);
			ToolTipMarker endToolTipMarker = new ToolTipMarker(
					endLocation,
					contextFeaturesString + behavior.behaviorName + " end ");		
			toolTipMarkers.add(endToolTipMarker);
			map.addMarker(endToolTipMarker);
		}		
	}
	
	private String friendly(String time) {
		Format formater = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		Date date = new Date(Long.parseLong(time));
		return formater.format(date);
	}
	
	public static void main(String[] args) {
		String inputFile = null;
		String jobId = null;
		for (int i = 0; i < args.length; i++) {
			if ("--input_file".equalsIgnoreCase(args[i])) {
				inputFile = args[i + 1];
			} else if ("--job_id".equalsIgnoreCase(args[i])) {
				jobId = args[i + 1];
			}
		}
		if (inputFile == null) {
			inputFile = INPUT_FILE;
		}
		if (jobId == null) {
			System.err.println("usage: java -jar mapview.jar --job_id <job ID> [--input_file <driving data JSON file>]");
			return;
		}
		
		Configuration config = null;
		try {
			config = new Configuration(CONFIG_FILE);
			
			InputData inputData = new InputData(inputFile);
			carProbes = inputData.getCarProbes();
			
			DriverService service = new DriverService(config);
			
			List<String> tripUuIds = service.getAllTripUuIds(jobId);
			String tripUuId = tripUuIds.get(0);
			driverBehaviorDetails = service.getAllDriverBehaviorDetails(tripUuId);
			
			String[] arguments = { MapView.class.getName() };
			PApplet.main(arguments);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}

}
