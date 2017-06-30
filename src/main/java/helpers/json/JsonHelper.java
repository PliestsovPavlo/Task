package helpers.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import entity.Job;

public interface JsonHelper {

	String serialize(Job job) throws JsonProcessingException;
	public Object deserialize(String str, Object j) throws JsonParseException, JsonMappingException, IOException;
}
