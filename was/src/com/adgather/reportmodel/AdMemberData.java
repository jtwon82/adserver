package com.adgather.reportmodel;

public class AdMemberData {
	
	//사용자아이디
	private String userId;
	//홈피
	private String homepi;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getHomepi() {
		return homepi;
	}
	public void setHomepi(String homepi) {
		this.homepi = homepi;
	}
	
	@Override
	public String toString() {
		return "AdMemberData [userId=" + userId + ", homepi=" + homepi + "]";
	}
	
	
}
