package helpers.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import entity.Job;

public class JacksonHelperImpl {
	public String serialize(Job job) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		String object = mapper.writeValueAsString(job);
		return object;
	}

	public Object deserialize(String str, Object j) throws JsonParseException, JsonMappingException, IOException {

		ObjectMapper mapper = new ObjectMapper();
		Object job = null;
		if (str != null) {
			job = mapper.readValue(str, j.getClass());
			System.out.println(job);
		}
		return job;
	}
}
