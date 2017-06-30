package entity;

import java.util.concurrent.Callable;

public class Api2 implements Callable<Job> {

	private Job job;

	public Api2(Job job) {
		super();
		this.job = job;
	}

	@Override
	public Job call() throws Exception {
		Thread.sleep(1000);
		job.setId(job.getId() + 100);
		return job;
	}

	public Job getJob() {
		return job;
	}

	public void setJob(Job job) {
		this.job = job;
	}
}
