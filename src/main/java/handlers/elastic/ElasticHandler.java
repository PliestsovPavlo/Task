package handlers.elastic;

import java.io.IOException;

public interface ElasticHandler extends Runnable{
	
	void addToIndex(String name, String type, String id, String json) throws IOException;
	void createIndex(String name) throws IOException;
	void prepareConnection();
	void closeConnection();
	void searchIndex(String name);
	void searchById(String name, String type, String id);
	void deleteByIndexById(String name, String type, String id);
	void deleteIndexByName(String index);

}
