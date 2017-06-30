package helpers.json;


public interface JsonHelper {

	public String serialize(Object job);
	public Object deserialize(String str, Object j);
}
