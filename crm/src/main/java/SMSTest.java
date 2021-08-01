import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
 
public class SMSTest {
	
	public static void main(String a[]){
		
		List<String> lists = new ArrayList<String>();
		lists.add("1");
		lists.add("2");
		System.out.println(lists.size());
		for(String s : lists){
			System.out.println(s);
		}
		
		lists.add(0, "0");
		
		System.out.println(lists.size());
		for(String s : lists){
			System.out.println(s);
		}
	}
	
	public static void sendSms() {
		try {
			// Construct data
			String user = "username=" + "sasmita.rout@gmail.com";
			String hash = "&hash=" + "Welcome123";
			String message = "&message=" + "Test message from Sasmita";
			String sender = "&sender=" + "Sasmita";
			String numbers = "&numbers=" + "919740388348";
			
			// Send data
			HttpURLConnection conn = (HttpURLConnection) new URL("http://api.textlocal.in/send/?").openConnection();
			String data = user + hash + numbers + message + sender;
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Length", Integer.toString(data.length()));
			conn.getOutputStream().write(data.getBytes("UTF-8"));
			final BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			final StringBuffer stringBuffer = new StringBuffer();
			String line;
			while ((line = rd.readLine()) != null) {
				stringBuffer.append(line);
			}
			rd.close();
			
			JSONObject obj = new JSONObject(stringBuffer.toString());
			System.out.println(obj.get("status"));
			
			
		} catch (Exception e) {
			System.out.println("Error SMS "+e);
			
		}
	}
}