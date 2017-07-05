package handlers.elastic;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import helpers.redis.RedisHelper;
import helpers.redis.RedisHelperImpl;

public class ElasticHandlerImpl implements ElasticHandler{
	
//	private static final Logger LOGGER = Logger.getLogger(ElasticHandlerImpl.class);
		
	private RedisHelper redis = new RedisHelperImpl();
	private String json = redis.lpop("aaa");
	
	private TransportClient client;
	
	private static ElasticHandlerImpl elasticHandlerImpl;
	
	private ElasticHandlerImpl() throws IOException {
		prepareConnection();
	}
	
	public static synchronized ElasticHandlerImpl getInstance() throws IOException{
		if(elasticHandlerImpl == null){
			elasticHandlerImpl = new ElasticHandlerImpl();
		}
		return elasticHandlerImpl;
	}

	//add json to index 
	public void addToIndex(String name, String type, String id, String json) throws IOException{
		
		IndexResponse ir = client.prepareIndex(name, type, id).setSource(json, XContentType.JSON).get();
		System.out.println(json+" is added to "+name+" index with id "+id);
	}
	
	//create a index if it dosn`t exist
	public void createIndex(String name) throws IOException{
		
		try{	
			CreateIndexResponse cir = client.admin().indices().prepareCreate(name).get();
			System.err.println("Index "+name+" created !!");
		}catch (Exception e) {
			System.err.println(e.toString());
		}
		
		
	}
	
	// connect to server
	public void prepareConnection() {
	try {
		client = new PreBuiltTransportClient(Settings.EMPTY)
				.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));
	} catch (UnknownHostException e) {
		System.err.println(e.toString());
	}
	}

	//close connection	
	public void closeConnection(){
		if(client != null){
			client.close();
		}
	}
	
	//search index
	public void searchIndex(String name){
	try{
	SearchResponse searchResponse = client.prepareSearch(name).get();
	System.out.println(searchResponse);
	}catch (Exception e) {
		System.err.println(e.toString());
	}
	}
	
	//search by id in index "name"
	public void searchById(String name, String type, String id){
		GetResponse response = client.prepareGet(name, type, id).get();
		if(response.isExists()){
			System.out.println(response.getSourceAsString());
//			LOGGER.info("info");
		}
	}
	
	//delete one record by id in index "name"
	public void deleteByIndexById(String name, String type, String id){
		DeleteResponse deleteResponse = client.prepareDelete(name, type, id).get();
		System.out.println("Record with id "+id+" was DELETED from index "+name);
	}
	
	//delete INDEX by name
	public void deleteIndexByName(String index){
		IndicesAdminClient adminClient = client.admin().indices();
		DeleteIndexResponse response = adminClient.delete(new DeleteIndexRequest(index)).actionGet();
		if(response.isAcknowledged()){
			System.out.println("Index "+index+" DELETED!!!");
		}
	}

	@Override
	public void run() {
		try {
			prepareConnection();
			createIndex("new_index");
			searchIndex("new_index");
			addToIndex("new_index", "some_type", "1", json);
			deleteIndexByName("new_index");
			
			closeConnection();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
