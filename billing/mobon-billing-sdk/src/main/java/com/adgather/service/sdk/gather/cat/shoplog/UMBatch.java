package com.adgather.service.sdk.gather.cat.shoplog;

import java.util.List;

import com.adgather.service.sdk.gather.batch.IBatch;
import com.adgather.service.sdk.gather.batch.SourceBean;
import com.adgather.user.inclinations.cookiedef.CookieDefRepository;
import com.adgather.user.inclinations.cookieval.inct.ctr.InctHuCtr;
import com.adgather.user.inclinations.cookieval.minct.MInctHU;
import com.adgather.user.inclinations.cookieval.minct.ctr.MInctHUCtr;

public class UMBatch
					implements
					IBatch {

	private IBatch batch = null;

	public UMBatch() {
		// TODO Auto-generated constructor stub
	}

	public UMBatch(
		IBatch batch) {
		// TODO Auto-generated constructor stub
		this.batch = batch;
	}

	public void setBatch(IBatch batch) {
		// TODO Auto-generated constructor stub
		this.batch = batch;
	}

	@Override
	public void batch(Object bean) {
		SourceBean _bean = (SourceBean) bean; 	
		List<String> data = (List<String>) _bean.getData();
		int count = (data.size() > 20) ? 20 : data.size();
		for(int i=0; i<count; i++) {	
			String[] row = data.get(i).split("_");
			
			MInctHU mhu = new MInctHU();
			if(row.length == 2) {
				mhu.setPkg(row[1]);
				mhu.setUpdDate(row[0]);
			} else {
				mhu.setPkg(data.get(i));
				mhu.setUpdDate(MInctHUCtr.getUpdDate());
			}
//			mhu.setFromApp("Y");
			_bean.getCi().addCookie(CookieDefRepository.M_UM, mhu, false);
		}
//		_bean.getCi().add
		
		if (this.batch != null) {
			this.batch.batch(bean);
		}
	}

}
