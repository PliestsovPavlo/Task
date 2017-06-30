package entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Job implements Serializable{
	
	private int id;
	
	private String body;

	private Set<String>apis = new HashSet<>();
	
	public String getBody() {
		return body;
	}

	
	public void setBody(String body) {
		this.body = body;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Set<String> getApis() {
		return apis;
	}

	public void setApis(Set<String> apis) {
		this.apis = apis;
	}

	@Override
	public String toString() {
		return "Job [id=" + id + ", body=" + body + ", apis=" + apis + "]";
	}


	public Job(int id, String body, Set<String> apis) {
		super();
		this.id = id;
		this.body = body;
		this.apis = apis;
	}


	public Job() {
		super();
	}

}
