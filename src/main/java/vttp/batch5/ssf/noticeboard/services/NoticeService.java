package vttp.batch5.ssf.noticeboard.services;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import vttp.batch5.ssf.noticeboard.models.Notice;
import vttp.batch5.ssf.noticeboard.repositories.NoticeRepository;
import vttp.batch5.ssf.noticeboard.util.Util;

@Service
public class NoticeService {

	@Autowired
	NoticeRepository noticeRepository;

	// TODO: Task 3
	// You can change the signature of this method by adding any number of parameters
	// and return any type
	public List<String> postToNoticeServer(Notice notice) {

		JsonObject jsonData = constructNoticeJson(notice);
		List<String> responseTerms = new ArrayList<>();
		
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> responseEntity;
		try {
			RequestEntity<String> requestEntity = RequestEntity.post(Util.publishingServerUrl+"/notice").contentType(MediaType.APPLICATION_JSON).body(jsonData.toString(), String.class);
			responseEntity = restTemplate.exchange(requestEntity, String.class);
			
			// ========== wrong req entity for testing. delete this ==========
			// RequestEntity<JsonObject> requestEntity2 = RequestEntity.post(Util.publishingServerUrl+"/notice").contentType(MediaType.APPLICATION_JSON).body(jsonData);
			// responseEntity = restTemplate.exchange(requestEntity2, String.class);
			// ========== delete this ==========
			
			JsonObject jsonResponse = generateRespnseJson(responseEntity);
			String id = jsonResponse.getString("id");
			noticeRepository.insertNotices(id, jsonResponse.toString());

			responseTerms.add(String.valueOf(responseEntity.getStatusCode().value()));
			responseTerms.add(id);

		} catch (RestClientException e) {
			e.printStackTrace();

			String error = e.getMessage();
			String errorAttributes = error.subSequence(error.indexOf("{"), error.indexOf("}")+1).toString();

			InputStream is = new ByteArrayInputStream(errorAttributes.getBytes());
			JsonReader jsonReader = Json.createReader(is);
			JsonObject jsonError = jsonReader.readObject();

			String errorMsg = jsonError.getString("message");
			String statusCode = error.substring(0, 3);
			responseTerms.add(statusCode);
			responseTerms.add(errorMsg);
		}

		return responseTerms;
	}

	public boolean healthCheck(){

		try {
			noticeRepository.getRandomKey();
			return true;
		} catch (Exception e) {
			// e.printStackTrace();
			System.out.println(e.getMessage());
			return false;
		}
	}


	private JsonObject constructNoticeJson(Notice notice){
		String[] terms = notice.toString().split(Util.delimiter);

		// convert categories entered into a json array
		String[] categories = terms[4].substring(1, terms[4].length()-1).replace(" ", "").split(",");
		JsonArrayBuilder jab = Json.createArrayBuilder();
		for (String s : categories){
			jab.add(s);
		}
		JsonArray catArray = jab.build();
		
		JsonObject jsonData = Json.createObjectBuilder()
								.add("title", terms[0])
								.add("poster", terms[1])
								.add("postDate", Long.parseLong(terms[2]))
								.add("categories", catArray)
								.add("text", terms[3])
								.build();

		return jsonData;
	}

	@SuppressWarnings("null")
	private JsonObject generateRespnseJson(ResponseEntity<String> responseEntity){
        
        String respBody = responseEntity.getBody();
        InputStream is = new ByteArrayInputStream(respBody.getBytes());
        JsonReader jsonReader = Json.createReader(is);
        JsonObject jsonData = jsonReader.readObject();
        jsonReader.close();

        return jsonData;
	}
}