package egovframework.example.sample.scheduler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.ejb.Schedule;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Scheduler {

	
	//초 분 시 일 월 요일
	@Scheduled(cron="0 */1 * * * *") 
	public void autoUpdate() {
		System.out.println("스케쥴: 1분에 한번");
		
		JSONObject json = readFromUrl("https://link.kostat.go.kr/SOPOpenAPI/OpenAPI3/addr/rgeocode.json?accessToken=af1b5059-f502-4f19-9b02-99bc26856e80&addr_type=20&x_coor=972482&y_coor=1821229&bnd_year=2022");
		
		System.out.println(json);
		System.out.println(json.get("result"));
		
		
	}
	
	//fixedDelay 밀리세컨드
	@Scheduled(fixedDelay = 10000) 
	public void autoUpdate2() {
		System.out.println("스케쥴: 10초에 한번");
	}
	
	public JSONObject readFromUrl(String url_addr) {
		HttpURLConnection conn = null;
        JSONObject responseJson = null;

        try {
            URL url = new URL(url_addr);

            java.lang.System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");
            conn = (HttpURLConnection)url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            conn.setRequestMethod("GET");
            //conn.setDoOutput(true);

            JSONObject commands = new JSONObject();

            int responseCode = conn.getResponseCode();
            if (responseCode == 400 || responseCode == 401 || responseCode == 500 ) {
                System.out.println(responseCode + " Error!");
            } else {
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line = "";
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                responseJson = new JSONObject(sb.toString());
                //System.out.println(responseJson);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            System.out.println("not JSON Format response");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return responseJson;
        
	}
}
