package entity;

import java.util.concurrent.Callable;

public class Api1 implements Callable<Job>{
	
	private Job job;

	@Override
	public Job call() throws Exception {
		Thread.sleep(1000);
		job.setBody("from api 1");
		return job;
	}

	public Api1(Job job) {
		super();
		this.job = job;
	}

	public Job getJob() {
		return job;
	}

	public void setJob(Job job) {
		this.job = job;
	}

	

}
