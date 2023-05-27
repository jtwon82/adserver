package com.adgather.user.inclinations.cookiedef.def;

import com.adgather.constants.GlobalConstants;
import com.adgather.user.inclinations.CIFunctionController;
import com.adgather.user.inclinations.convert.inter.CodeConverter;
import com.adgather.user.inclinations.cookiedef.inter.CookieDef;
import com.adgather.user.inclinations.cookielist.ObjectSyncList;
import com.adgather.user.inclinations.cookielist.SyncList;
import com.adgather.user.inclinations.cookieval.freq.FreqDomain;
import com.adgather.user.inclinations.memory.MemoryObj;
import com.adgather.util.PropertyHandler;


/**
 * RM 프리퀀시 쿠키 정의
 * @author yhlim
 *
 */
public class DefFreqRm extends CookieDef {
	public DefFreqRm(String cookieKey) {
		super(cookieKey);
	}
	
	/** CookieDef 구현 ********************************************/
	@Override
	public Object newObj() {
		return new FreqDomain();
	}
	
	@Override
	public SyncList<?> newList() {
		return new ObjectSyncList<FreqDomain>(LIST_DELIMETER);
	}
	
	@Override
	public int getExpire() {
		return PropertyHandler.getInt("COOKIE_FREQ_RM_EXPIRE");
	}
	
	@Override
	public void infuseLimit(MemoryObj<?> memoryObj) {
		memoryObj.setMongoMaxCnt(PropertyHandler.getInt("COOKIE_FREQ_RM_COUNT"));
		memoryObj.setMongoMaxLen(PropertyHandler.getInt("COOKIE_FREQ_RM_BYTES"));
		memoryObj.setCookieMaxCnt(PropertyHandler.getInt("MONGO_FREQ_RM_COUNT"));
		memoryObj.setCookieMaxLen(PropertyHandler.getInt("MONGO_FREQ_RM_BYTES"));
	}
	
	@Override
	public boolean isFixePose() {
		return false;
	}

	@Override
	public boolean isUseMongo() {
		return PropertyHandler.isTrue("MONGO_FREQ_RM_ENABLE");
	}

	@Override
	public boolean isUseMongo(int mediaScriptNo) {
		if(isUseMongo()) {
			return true;
		}
		
		if(CIFunctionController.isUseMongFreqMs(mediaScriptNo)) {
			return CIFunctionController.isUseMongFreqMsGubun(GlobalConstants.RM);
		}
		
		return false;
	}
	
	@Override
	public CodeConverter getCodeConverter() {
		return null;
	}
}
