package com.mobon.billing.core.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.adgather.constants.G;
import com.adgather.util.old.DateUtils;
import com.adgather.util.old.StringUtils;
import com.mobon.billing.core.service.dao.ClickViewOpenrtbDataDao;
import com.mobon.billing.core.service.dao.SelectDao;
import com.mobon.billing.model.v15.BaseCVData;
import com.mobon.billing.util.ConsumerFileUtils;

@Service
public class ClickViewOpenrtbDataToMariaDB {
	private static final Logger logger = LoggerFactory.getLogger(ClickViewOpenrtbDataToMariaDB.class);

	@Value("${log.path}")
	private String	logPath;
	
	@Autowired
	private ClickViewOpenrtbDataDao	clickViewOpenrtbDataDao;
	@Autowired
	private SelectDao			selectDao;
	
	
	

//	public void sp_clickviewx( ArrayList<BaseCVData> list ) {
//		HashMap<String, ArrayList<BaseCVData>> flushMap = makeFlushMap(list);
//		clickViewDataDao.transectionRunning(flushMap);
//	}	
	
	public boolean intoMariaClickViewDataV3(String _id, List<BaseCVData> aggregateList, boolean toMongodb) {
		boolean result = false;
		long start_millis = System.currentTimeMillis();

		if (aggregateList != null) {
			
			HashMap<String, ArrayList<BaseCVData>> flushMap = makeFlushMap(aggregateList);
			
			if ( flushMap.keySet().size() != 0) {
				if (toMongodb) {
					try {
						String writeFileName = String.format("insertIntoError_%s_%s", Thread.currentThread().getName(), DateUtils.getDate("yyyyMMdd_HHmm") );
						ConsumerFileUtils.writeLine( logPath +"retry/", writeFileName, G.click_view_openrtb, aggregateList);
					} catch (IOException e) {
						logger.error("err - {}", e);
					}
					
					logger.info("chking fail ClickViewData fileWriteOk flushMap.keySet() - {}", flushMap.keySet() );
					result = true;
				} else {
					//OpenRtbDB Insert
					result = clickViewOpenrtbDataDao.transectionRuningV2( flushMap );

					if(result) {
						List<BaseCVData> pointList= new ArrayList();
						for (Entry<String, ArrayList<BaseCVData>> obj: flushMap.entrySet()) {
							ArrayList<BaseCVData> list= obj.getValue();
							for(BaseCVData vo:list) {
								if( vo.getHandlingPointData().equals("clickView") 
										&& (vo.getPoint() > 0 || vo.getMpoint() > 0)) {
									vo.setPointChargeAble("true");
									pointList.add(vo);
								}
							}
						}
						HashMap<String, ArrayList<BaseCVData>> pointFlushMap= makeFlushMap(pointList);
						boolean resultPoint= clickViewOpenrtbDataDao.transectionPointRuningV2( pointFlushMap );
						if(!resultPoint) {
							logger.info("pointFlushMap {}", pointFlushMap);
						}
					}
					//logger.info("succ intoMariaClickViewData _id - {}, size - {}, during - {}", _id, flushMap.keySet().size(), System.currentTimeMillis() - start_millis );
				}
			} else {
				result = true;
			}
		} else {
			result = true;
		}
		return result;
	}
	
	public HashMap<String, ArrayList<BaseCVData>> makeFlushMap(List<BaseCVData> aggregateList){
		HashMap<String, ArrayList<BaseCVData>> flushMap = new HashMap();
		
		for (BaseCVData vo : aggregateList) {
			try {
				if (vo != null) {
					if (vo.getYyyymmdd()==null || vo.getPlatform()==null || vo.getAdGubun()==null 
						|| vo.getSiteCode()==null || vo.getScriptNo()==0 || vo.getAdvertiserId()==null || vo.getType()==null) {
						logger.error("Missing required, vo - {}", vo.toString());
						continue;
					}
					
					if( StringUtils.isEmpty(vo.getProduct()) || StringUtils.isEmpty(vo.getScriptUserId()) ) {
						BaseCVData minfo = selectDao.selectMediaInfo(vo);
						if( minfo!=null ) {
							logger.error("map - {}", minfo);
							
							if( StringUtils.isEmpty(vo.getProduct()) ) {
								vo.setProduct( StringUtils.trimToNull2(minfo.getProduct()));	// 모비온에서 넘어온값으로 써야됨
								logger.error("chking vo - {}", vo);
							}
							if( StringUtils.isEmpty(vo.getScriptUserId()) ) {
								vo.setScriptUserId( StringUtils.trimToNull2(minfo.getScriptUserId())); //map.get("scriptUserId")) );
								logger.error("chking vo - {}", vo);
							}
						}
					}
					
					if( !StringUtils.isNumeric(vo.getPlatform()) ) vo.setPlatform(G.convertPLATFORM_CODE(vo.getPlatform()));
					if( !StringUtils.isNumeric(vo.getProduct()) ) vo.setProduct(G.convertPRDT_CODE(vo.getProduct()));
					if( !StringUtils.isNumeric(vo.getAdGubun()) ) vo.setAdGubun(G.convertTP_CODE(vo.getAdGubun()));
					//if( !StringUtils.isNumeric(vo.getProduct()) ) vo.setProduct(selectDao.selectMobonComCodeAdvrtsPrdtCode(vo.getProduct()));
					//if( !StringUtils.isNumeric(vo.getAdGubun()) ) vo.setAdGubun(selectDao.convertAdvrtsTpCode(vo.getAdGubun()));

					//상품 타겟팅 여부 확인용 메소드
					if (vo.getAdGubun() != null) {
						vo.setTargetYn(G.checkTargetYN(vo.getAdGubun()));
					}

					// 아이커버의경우 C 로넘어오는경우 C는 V와도 같다.
					logger.debug("vo - {}", vo);
					
					if (!Arrays.asList("01","03").contains(vo.getInterlock())) {
						add(flushMap, "SQL_INSERT_OPEN_RTB_STATIS_INFO", vo);
						add(flushMap, "SQL_INSERT_BILLING_STATS", vo);
						if (vo.getPointChargeAble().equals("true") && (vo.getPoint() > 0 || vo.getMpoint() > 0)) { // if(vo.getPoint()>0 || vo.getMpoint()>0 ){
							add(flushMap, "sp_point_NEW", vo);
						}
					}
				}
			} catch (Exception e) {
				logger.error("err item - {}, msg - {}", vo, e);
			}
		}
		return flushMap;
	}

	private void add(HashMap<String, ArrayList<BaseCVData>> flushMap, String key, BaseCVData vo) {
		if(flushMap.get(key)==null){
			flushMap.put(key, new ArrayList());
		}
		ArrayList<BaseCVData> l = flushMap.get(key);
		logger.debug("s - {}, statYn - {}", vo.getScriptNo(), vo.getStatYn());
		if("N".equals(vo.getStatYn())) {
			if(key.indexOf("point")>0) {
				l.add(vo);
			}
		}
		else {
			l.add(vo);
		}
	}
}
