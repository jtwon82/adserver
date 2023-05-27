package com.adgather.util;

public class MemoryCheck{
	static long free;
	static long total;
	static long usedRatio;
	static long unusedRatio;
	protected static MemoryCheck m_instance;
	public static MemoryCheck getInstance() throws Exception{
		
		if (m_instance == null ){
			m_instance=new MemoryCheck();
		}
		return m_instance;
	}		
	public MemoryCheck(){
		init();
	}
	public void init(){
		Runtime rt = Runtime.getRuntime();	 
		free = rt.freeMemory();
		total = rt.totalMemory();
		long usedRatio = (total - free) * 100 / total;
		long unusedRatio = free * 100 / total;	
	}
	public long getTotalMomory(){
		init();
		return total/1024;
	}
	public long getUsedMemory(){
		init();
		return (total - free)/1024;
	}
	public long getFreeMemory(){
		init();
		return free/1024;
	}
}

