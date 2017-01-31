package org.qtum.mromanovsky.qtum.dataprovider.jsonrpc;

import android.util.Base64;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;


public class JSONRPCHttpClient extends JSONRPCClient
{

	private String mBaseUrl = "http://139.162.119.184:22822/";

	public JSONRPCHttpClient(){

	}

	public JSONRPCHttpClient(String url){
		mBaseUrl = url;
	}

	protected JSONObject doJSONRequest(JSONObject jsonRequest) throws JSONRPCException {
		HttpURLConnection urlConnection = null;
		BufferedReader reader = null;
		try {
			URL url = new URL(mBaseUrl);
			urlConnection = (HttpURLConnection) url.openConnection();

			String publicKey = "user:pw";
			String encodedString = Base64.encodeToString(publicKey.getBytes(), Base64.NO_WRAP);
			String basicAuth = "Basic ".concat(encodedString);
			urlConnection.setRequestProperty("Authorization", basicAuth);

			urlConnection.setDoOutput(true);
			urlConnection.setDoOutput(true);

			urlConnection.setRequestMethod("POST");
			urlConnection.setRequestProperty("Content-Type", "application/json");
			urlConnection.setRequestProperty("Accept", "application/json");

			Writer writer = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8"));
			writer.write(String.valueOf(jsonRequest));

			writer.close();

			int responseCode = urlConnection.getResponseCode();
			int i = 0;
			InputStream inputStream = urlConnection.getInputStream();

			StringBuffer buffer = new StringBuffer();
			if (inputStream == null) {
				return null;
			}
			reader = new BufferedReader(new InputStreamReader(inputStream));

			String inputLine;
			while ((inputLine = reader.readLine()) != null)
				buffer.append(inputLine + "\n");
			if (buffer.length() == 0) {
				return null;
			}
			String response = buffer.toString();

			JSONObject jsonResponse = new JSONObject(response);
			int i2 = 0;
			return jsonResponse;
		}
		catch (IOException e)
		{
			throw new JSONRPCException("IO error", e);

		}
		catch (JSONException e)
		{
			throw new JSONRPCException("Invalid JSON response", e);
		}
		finally {
			if (urlConnection != null) {
				urlConnection.disconnect();
			}
			if (reader != null) {
				try {
					reader.close();
				} catch (final IOException e) {

				}
			}
		}
	}
}
