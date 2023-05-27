package com.adgather.test;

public class ThreadTest {
	public static int cnt=0;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try{
			DataPushJobThread a = new DataPushJobThread();
			a.start();
			
			ThreadMonitor b = new ThreadMonitor();
			b.start();
		}catch(Exception e){}
	}

}
class DataPushJobThread extends Thread{
	DataPushJobThread() {
		super();
		try{
		}catch(Exception e){
		}
	} 
	
	public void run() {
		ThreadTest.cnt++;
		try{
			Thread.sleep(1000);
		}catch(Exception e){
			System.out.println( e );
		}
		ThreadTest.cnt--;
	}
	
}
class ThreadMonitor extends Thread{
	
	public void run() {
		try{
			while( true ){
				System.out.println( ThreadTest.cnt );
				Thread.sleep(1000);
			}
		}catch(Exception e){}
	}
}
