package helpers.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import entity.Job;

public class JsonHelperImpl implements JsonHelper{

	private JacksonHelperImpl jacksonHelperImpl = new JacksonHelperImpl();
	
	public String serialize(Job job) throws JsonProcessingException{
		return jacksonHelperImpl.serialize(job);
	}
	
	public Object deserialize(String str, Object j) throws JsonParseException, JsonMappingException, IOException{
		return jacksonHelperImpl.deserialize(str, j);
	}
}

