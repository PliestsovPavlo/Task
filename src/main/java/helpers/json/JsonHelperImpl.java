package helpers.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class JsonHelperImpl implements JsonHelper{

    private ObjectMapper jackson;

	public JsonHelperImpl(){
		init();
	}
	
    private void init()
    {
	this.jackson = new ObjectMapper();
	this.jackson.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);
	this.jackson.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Override
    public Object deserialize(String string, Object object)
    {
	try
	{
	    return this.jackson.readValue(string, object.getClass());
	} catch (Exception ex)
	{
	    return null;
	}
    }


	@Override
	public String serialize(Object job)
	{
		try 
		{
			return jackson.writeValueAsString(job);
		} catch (JsonProcessingException e)
		{
			e.printStackTrace();
		}
		return null;
	}
}

