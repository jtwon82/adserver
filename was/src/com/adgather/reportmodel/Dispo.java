package com.adgather.reportmodel;

public class Dispo implements Comparable<Dispo>{
	private String name;
	private int cnt;
	
	public Dispo(String n, int c){
		this.name=n;
		this.cnt=c;
	}

	public int compareTo(Dispo dispo){
		if( this.cnt<dispo.cnt){
			return 1;
		} else if ( this.cnt==dispo.cnt ) {
			return 0;
		} else {
			return -1;
		}
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getCnt() {
		return cnt;
	}
	public void setCnt(int cnt) {
		this.cnt = cnt;
	}

}
