
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DriverService {

	private Connection connection;
	
	public DriverService(Configuration config) {
		this.connection = new Connection(config);
	}
	
	public List<String> getAllJobIds() throws IOException, JSONException {
		JSONArray jsonArray = connection.getJSONArray("/jobcontrol/jobList");
		List<String> jobIds = new ArrayList<String>();
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject jobInfo = jsonArray.getJSONObject(i);
			String jobId = jobInfo.getString("job_id");
			jobIds.add(jobId);
		}
		return jobIds;
	}

	public List<String> getAllTripUuIds(String jobId) throws IOException, JSONException {
		JSONArray jsonArray = connection.getJSONArray("/drbresult/tripSummaryList", "job_id=" + jobId);
		List<String> tripUuIds = new ArrayList<String>();
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject tripSummary = jsonArray.getJSONObject(i);
			JSONObject id = tripSummary.getJSONObject("id");
			String tripUuId = id.getString("trip_uuid");
			tripUuIds.add(tripUuId);
		}
		return tripUuIds;
	}

	public class DriverBehaviorDetail {
		public List<ContextFeature> contextFeatures;
		public String behaviorName;
		public double startLatitude;
		public double startLongitude;
		public double endLatitude;
		public double endLongitude;
		public String startTime;
		public String endTime;
		
		public DriverBehaviorDetail(
				List<ContextFeature> contextFeatures,
				String behaviorName,
				String startLatitude,
				String startLongitude,
				String endLatitude,
				String endLongitude,
				String startTime,
				String endTime) {
			this.contextFeatures = contextFeatures;
			this.behaviorName = behaviorName;
			this.startLatitude = Double.parseDouble(startLatitude);
			this.startLongitude = Double.parseDouble(startLongitude);
			this.endLatitude = Double.parseDouble(endLatitude);
			this.endLongitude = Double.parseDouble(endLongitude);
			this.startTime = startTime;
			this.endTime = endTime;
		}
		
		public String toString() {
			String contextFeaturesString = "";
			for (int i = 0; i < contextFeatures.size(); i++) {
				contextFeaturesString += contextFeatures.get(i) + ", ";
			}
			return contextFeaturesString +
					"behaviorName: "	+ behaviorName		+ ", " +
					"startLatitude: "	+ startLatitude		+ ", " +
					"startLongitude: "	+ startLongitude	+ ", " +
					"endLatitude: "		+ endLatitude		+ ", " +
					"endLongitude: "	+ endLongitude		+ ", " +
					"startTime: "		+ startTime			+ ", " +
					"endTime: "			+ endTime;
		}
	}
	
	public class ContextFeature {
		public String category;
		public String name;
		
		public ContextFeature(String category, String name) {
			this.category = category;
			this.name = name;
		}
		
		public String toString() {
			return category + ": " + name;
		}
	}
	
	public List<DriverBehaviorDetail> getAllDriverBehaviorDetails(String tripUuId) throws IOException, JSONException {
		JSONObject jsonObject = connection.getJSONObject("/drbresult/trip", "trip_uuid=" + tripUuId);
		JSONArray ctxSubTrips = jsonObject.getJSONArray("ctx_sub_trips");
		List<DriverBehaviorDetail> driverBehaviorDetails = new ArrayList<DriverBehaviorDetail>();  
		for (int i = 0; i < ctxSubTrips.length(); i++) {
			JSONObject ctxSubTrip = ctxSubTrips.getJSONObject(i);
			JSONArray features = ctxSubTrip.getJSONArray("ctx_features");
			List<ContextFeature> contextFeatures = new ArrayList<ContextFeature>();
			for (int j = 0; j < features.length(); j++) {
				JSONObject feature = features.getJSONObject(j);
				String category = feature.getString("context_category");
				String name = feature.getString("context_name");
				ContextFeature contextFeature = new ContextFeature(category, name);
				contextFeatures.add(contextFeature);
			}
			JSONArray details = ctxSubTrip.getJSONArray("driving_behavior_details");
			for (int k = 0; k < details.length(); k++) {
				JSONObject detail = details.getJSONObject(k);
				DriverBehaviorDetail driverBehaviorDetail = new DriverBehaviorDetail(
						contextFeatures,
						detail.getString("behavior_name"),
						String.valueOf(detail.getDouble("start_latitude")),
						String.valueOf(detail.getDouble("start_longitude")),
						String.valueOf(detail.getDouble("end_latitude")),
						String.valueOf(detail.getDouble("end_longitude")),
						String.valueOf(detail.getLong("start_time")),
						String.valueOf(detail.getLong("end_time")));
				driverBehaviorDetails.add(driverBehaviorDetail);
			}
		}
		return driverBehaviorDetails;
	}
	
}
