package com.mobon.billing.branchUM.service;

import java.util.LinkedList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.adgather.constants.G;
import com.mobon.billing.branchUM.schedule.TaskKnoUMScriptNoData;
import com.mobon.billing.branchUM.schedule.TaskKnoUMSiteCodeData;
import com.mobon.billing.branchUM.schedule.TaskKnoKpiData;
import com.mobon.exschedule.model.TaskData;

public class WorkQueueTaskData {
	
	private static final Logger		logger	= LoggerFactory.getLogger(WorkQueueTaskData.class);
	
	private String				queueName;
	private int					nThreads = 1;
	private final PoolWorker[]	threads;
	private final LinkedList	queue;

	@Autowired
	private TaskKnoUMSiteCodeData		taskKnoUMSiteCodeData;
	@Autowired
	private TaskKnoUMScriptNoData		taskKnoUMScriptNoData;
	@Autowired
	private TaskKnoKpiData 			TaskKnoKpiData;
	
	public WorkQueueTaskData(String _queueName) {
		logger.debug("queueName - {} START", _queueName);
		queueName	= _queueName;
		
		queue = new LinkedList();

		threads = new PoolWorker[nThreads];
		for (int i = 0; i < nThreads; i++) {
			threads[i] = new PoolWorker();
			threads[i].start();
		}
	}
	
	public WorkQueueTaskData(String _queueName, int nThreads) {
		logger.debug("queueName - {} START", _queueName);
		queueName	= _queueName;
		
		this.nThreads = nThreads;
		queue = new LinkedList();
		threads = new PoolWorker[nThreads];

		for (int i = 0; i < nThreads; i++) {
			threads[i] = new PoolWorker();
			threads[i].start();
		}
	}

	public void execute(TaskData r) {
		synchronized (queue) {
			queue.addLast(r);
			queue.notify();
		}
	}
	
	public int getQueueSize() {
		int size = 0;
		synchronized (queue) {
			size = queue.size();
		}
		return size; 
	}
	public int getThreadsCount() {
		return threads.length;
	}


	

	private class PoolWorker extends Thread {
		public void run() {
			TaskData r;

			while (true) {
				synchronized (queue) {
					while ( queue.isEmpty() ) {
						try {
							queue.wait();
						} catch (InterruptedException ignored) {
						}
					}
					
					//logger.info("{} queue before - {}", queueName, queue.size() );
					r = (TaskData) queue.removeFirst();
					logger.info("{} queue after - {}", queueName, queue.size() );
				}

				// If we don't catch RuntimeException,
				// the pool could leak threads
				if (r.getTask().equals(G.KnoUMSiteCodeData) ) {
					if(taskKnoUMSiteCodeData!=null)
						taskKnoUMSiteCodeData.mongoToMariaV3(r);
					else
						logger.error("taskKnoUMSiteCodeData nothing");
					
				} else if (r.getTask().equals(G.KnoUMScriptNoData) ) {
					if(taskKnoUMScriptNoData!=null)
						taskKnoUMScriptNoData.mongoToMariaV3(r);
					else
						logger.error("taskKnoUMScriptNoData nothing");
					
				} 
				else if (r.getTask().equals(G.KnoKpiData)) {
					if(TaskKnoKpiData!=null)
						TaskKnoKpiData.mongoToMariaV3(r);
					else
						logger.error("taskKnoUMScriptNoData nothing");
				}
			}
		}
	}
}
