import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.json.JSONException;
import org.json.JSONObject;

public class Configuration {

	private String apiBaseUrl;
	private String tenantId;
	private String userName;
	private String password;
	
	public Configuration(String configFileName) throws JSONException, IOException {
		File configFile = null;
		FileInputStream fileInputStream = null;
		InputStreamReader inputStreamReader = null;
		BufferedReader bufferedReader = null;
		try {
			configFile = new File(configFileName);
			fileInputStream = new FileInputStream(configFile);
			inputStreamReader = new InputStreamReader(fileInputStream);
			bufferedReader = new BufferedReader(inputStreamReader);
			String jsonString = "";
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				jsonString += line;
			}
			JSONObject jsonObject = new JSONObject(jsonString);
			this.apiBaseUrl = jsonObject.getString("api");
			this.tenantId = jsonObject.getString("tenant_id");
			this.userName = jsonObject.getString("username");
			this.password = jsonObject.getString("password");
		} finally {
			if (bufferedReader != null) {
				bufferedReader.close();
			}
			if (inputStreamReader != null) {
				inputStreamReader.close();				
			}
			if (fileInputStream != null) {
				fileInputStream.close();
			}
		}
	}
	
	public String apiBaseUrl() {
		return apiBaseUrl;
	}

	public String tenantId() {
		return tenantId;
	}

	public String userName() {
		return userName;
	}

	public String password() {
		return password;
	}
	
}
