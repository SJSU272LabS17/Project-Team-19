import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ConnectionSetUp {
	
	private String apiBaseUrl;
	private String tenantId;
	private String userName;
	private String password;
	
	private String authorizationValue;
	
	public Connection(Configuration config) {
		this.apiBaseUrl = config.apiBaseUrl();
		this.tenantId = config.tenantId();
		this.userName = config.userName();
		this.password = config.password();
		String authorizationString = userName + ":" + password;
		authorizationValue = new String(Base64.encodeBase64(authorizationString.getBytes()));
	}
	
	public JSONArray getJSONArray(String urlTail) throws IOException, JSONException {
		JSONArray jsonArray = null;
		HttpURLConnection httpURLConnection = null;
		try {
			httpURLConnection = createHttpURLGetConnection(urlTail);
			InputStreamReader connectionInputStreamReader = new InputStreamReader(httpURLConnection.getInputStream());
			BufferedReader bufferedReader = new BufferedReader(connectionInputStreamReader);
			String jsonString = "";
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				jsonString += line;
			}
			jsonArray = new JSONArray(jsonString);
		} finally {
			disconnectIfRequired(httpURLConnection);
		}
		return jsonArray;				
	}

	public JSONArray getJSONArray(String urlTail, String parameters) throws IOException, JSONException {
		JSONArray jsonArray = null;
		HttpURLConnection httpURLConnection = null;
		try {
			httpURLConnection = createHttpURLGetConnection(urlTail, parameters);
			InputStreamReader connectionInputStreamReader = new InputStreamReader(httpURLConnection.getInputStream());
			BufferedReader bufferedReader = new BufferedReader(connectionInputStreamReader);
			String jsonString = "";
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				jsonString += line;
			}
			jsonArray = new JSONArray(jsonString);
		} finally {
			disconnectIfRequired(httpURLConnection);
		}
		return jsonArray;				
	}

	public JSONArray getJSONArray(String urlTail, JSONObject parameters) throws IOException, JSONException {
		JSONArray jsonArray = null;
		HttpURLConnection httpURLConnection = null;
		try {
			httpURLConnection = createHttpURLPostConnection(urlTail, parameters);
			InputStreamReader connectionInputStreamReader = new InputStreamReader(httpURLConnection.getInputStream());
			BufferedReader bufferedReader = new BufferedReader(connectionInputStreamReader);
			String jsonString = "";
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				jsonString += line;
			}
			jsonArray = new JSONArray(jsonString);
		} finally {
			disconnectIfRequired(httpURLConnection);
		}
		return jsonArray;				
	}

	public JSONObject getJSONObject(String urlTail) throws IOException, JSONException {
		JSONObject jsonObject = null;
		HttpURLConnection httpURLConnection = null;
		try {
			httpURLConnection = createHttpURLGetConnection(urlTail);
			InputStreamReader connectionInputStreamReader = new InputStreamReader(httpURLConnection.getInputStream());
			BufferedReader bufferedReader = new BufferedReader(connectionInputStreamReader);
			String jsonString = "";
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				jsonString += line;
			}
			jsonObject = new JSONObject(jsonString);
		} finally {
			disconnectIfRequired(httpURLConnection);
		}
		return jsonObject;				
	}
	
	public JSONObject getJSONObject(String urlTail, String parameters) throws IOException, JSONException {
		JSONObject jsonObject = null;
		HttpURLConnection httpURLConnection = null;
		try {
			httpURLConnection = createHttpURLGetConnection(urlTail, parameters);
			InputStreamReader connectionInputStreamReader = new InputStreamReader(httpURLConnection.getInputStream());
			BufferedReader bufferedReader = new BufferedReader(connectionInputStreamReader);
			String jsonString = "";
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				jsonString += line;
			}
			jsonObject = new JSONObject(jsonString);
		} finally {
			disconnectIfRequired(httpURLConnection);
		}
		return jsonObject;				
	}

	private HttpURLConnection createHttpURLGetConnection(String urlTail) throws IOException {
		URL url = new URL(apiBaseUrl + urlTail + "?tenant_id=" + tenantId);
		HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
		httpURLConnection.setRequestProperty("Authorization", "Basic " + authorizationValue);
		httpURLConnection.setRequestMethod("GET");
		return httpURLConnection;
	}

	private HttpURLConnection createHttpURLGetConnection(String urlTail, String parameters) throws IOException {
		URL url = new URL(apiBaseUrl + urlTail + "?tenant_id=" + tenantId + "&" + parameters);
		HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
		httpURLConnection.setRequestProperty("Authorization", "Basic " + authorizationValue);
		httpURLConnection.setRequestMethod("GET");
		return httpURLConnection;
	}
	
	private HttpURLConnection createHttpURLPostConnection(String urlTail, JSONObject parameters) throws IOException, JSONException {
		URL url = new URL(apiBaseUrl + urlTail + "?tenant_id=" + tenantId);
		HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
		httpURLConnection.setRequestProperty("Authorization", "Basic " + authorizationValue);
		httpURLConnection.setRequestMethod("POST");
		httpURLConnection.setDoOutput(true);
		OutputStreamWriter connectionOutputStreamWriter = new OutputStreamWriter(httpURLConnection.getOutputStream());
		parameters.write(connectionOutputStreamWriter);			
		return httpURLConnection;
	}
	
	private void disconnectIfRequired(HttpURLConnection connection) {
		if (connection != null) {
			connection.disconnect();
		}
	}

}
