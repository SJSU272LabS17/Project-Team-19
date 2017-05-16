

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import com.ibm.iotf.client.device.DeviceClient;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class SendSensorData {

	private static final int SLEEP_INTERVAL = 1000;

	public static void main(String[] args) throws Exception {

		Properties deviceProps = new Properties();
		try {
			deviceProps.load(new FileInputStream ("config/device.prop"));
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

		String inputFilename = "data/CarProbeSample.json";

		DeviceClient deviceClient = null;
		try {
			deviceClient = new DeviceClient(deviceProps);
			deviceClient.connect();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(inputFilename));
			JsonParser parsor = new JsonParser();
			JsonArray jsonArray = parsor.parse(br).getAsJsonArray();	
			for (int i = 0; i < jsonArray.size(); i++) {
				JsonObject jsonObject = (JsonObject) jsonArray.get(i); 
				boolean status = deviceClient.publishEvent("load", jsonObject);
				System.out.println(jsonObject);
				Thread.sleep(SLEEP_INTERVAL);
				if (!status) {
					throw new Exception("Failed to publish the event: " + jsonObject);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (br != null) {
				br.close();
				System.exit(0);
			}
		}
		System.out.println("Completed!!");
	}
}
