package entity;

import java.io.Serializable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Worker implements Runnable, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	private String name;
	private Job job;
	private Adaptor adaptor;
	private boolean isBuzy;
	private int limitCalls = 2;
	private ExecutorService executorService;

	public Worker(Adaptor adaptor) {
		this.adaptor = adaptor;
	}

	public Job getJob() {
		return job;
	}

	public void setJob(Job job) {
		this.job = job;
	}

	@Override
	public void run() {
		try {
			if (job != null) {
				synchronized (job) {
					this.setBuzy(true);
					limitCalls--;
					executorService = Executors.newFixedThreadPool(job.getApis().size());

					Future<Job> fJob = null;
					for (String api : job.getApis()) {
						if (api.equalsIgnoreCase("google")) {
							fJob = executorService.submit(new Api1(job));
						}
						if (api.equalsIgnoreCase("yandex")) {
							fJob = executorService.submit(new Api2(job));
						}
					}

					System.err.println(fJob.get() + "\n");

					this.setBuzy(false);
					executorService.shutdown();

					synchronized (adaptor) {
						adaptor.notify();
//						System.out.println("worker notify adaptor");
					}
					synchronized (this) {
//						System.out.println("worker sleep");
						this.wait();
					}
				}
			}
		} catch (InterruptedException e) {
			System.err.println(e.toString());
		} catch (ExecutionException e) {
			System.err.println(e.toString());
		}
	}

	public boolean isBuzy() {
		return isBuzy;
	}

	public void setBuzy(boolean isBuzy) {
		this.isBuzy = isBuzy;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Adaptor getAdaptor() {
		return adaptor;
	}

	public void setAdaptor(Adaptor adaptor) {
		this.adaptor = adaptor;
	}

	public int getLimitCalls() {
		return limitCalls;
	}

	public void setLimitCalls(int limitCalls) {
		this.limitCalls = limitCalls;
	}

	@Override
	public String toString() {
		return "Worker [name=" + name + ", job=" + job + ", adaptor=" + adaptor + ", isBuzy=" + isBuzy + ", limitCalls="
				+ limitCalls + ", executorService=" + executorService + "]";
	}

}
