package handlers.elastic;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.apache.logging.log4j.core.Logger;
import org.apache.lucene.search.Query;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory.*;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.search.MultiMatchQuery.QueryBuilder;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import entity.Job;
import entity.Manager;
import helpers.json.JsonHelper;
import helpers.json.JsonHelperImpl;
import helpers.redis.RedisHelper;
import helpers.redis.RedisHelperImpl;

public class ElasticHandler {
//	private static final Logger LOGGER; 
	
	private TransportClient client;
	
	private RedisHelper redis = new RedisHelperImpl();
	
	private JsonHelper helper = new JsonHelperImpl(); 
	
	private Manager manager;
	
	private String json = redis.lpop("aaa");
	
	ObjectMapper jackson;
		
	public ElasticHandler() throws IOException {
		prepareConnection();
	}

	public void addToIndex(String name, String type, String id, String json) throws IOException{
		
		IndexResponse ir = client.prepareIndex(name, type, id).setSource(json, XContentType.JSON).get();
		System.out.println(json+" is added to "+name+" index with id "+id);
	}
	
	public void createIndex(String name) throws IOException{
		
		CreateIndexResponse cir = client.admin().indices().prepareCreate(name).get();
		System.err.println("Index "+name+" created !!");
		
		
	}
	
	public void prepareConnection() {
	try {
		client = new PreBuiltTransportClient(Settings.EMPTY)
				.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));
	} catch (UnknownHostException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}
	
	public void closeConnection(){
		if(client != null){
			client.close();
		}
	}
	
	public void searchIndex(String name){
	try{
	SearchResponse searchResponse = client.prepareSearch(name).get();
	System.out.println(searchResponse);
	}catch (Exception e) {
		System.err.println(e.toString());
	}
	}
	
	public void searchById(String name, String type, String id){
		GetResponse response = client.prepareGet(name, type, id).get();
		if(response.isExists()){
			System.out.println(response.getSourceAsString());
		}
	}
	
	public void deleteIndex(String name, String type, String id){
		DeleteResponse deleteResponse = client.prepareDelete(name, type, id).get();
		System.out.println("DELETED from "+name+" with id "+id);
	}
	
}
