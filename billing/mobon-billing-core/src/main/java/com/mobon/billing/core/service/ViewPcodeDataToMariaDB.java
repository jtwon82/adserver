package com.mobon.billing.core.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.adgather.constants.G;
import com.adgather.util.old.DateUtils;
import com.adgather.util.old.StringUtils;
import com.mobon.billing.core.service.dao.SelectDao;
import com.mobon.billing.core.service.dao.ViewPcodeDataDao;
import com.mobon.billing.model.v15.BaseCVData;
import com.mobon.billing.util.ConsumerFileUtils;

@Service
public class ViewPcodeDataToMariaDB {

	private static final Logger logger = LoggerFactory.getLogger(ViewPcodeDataToMariaDB.class);

	@Value("${log.path}")
	private String	logPath;

	@Autowired
	private ViewPcodeDataDao	viewPcodeDataDao;
	@Autowired
	private SelectDao			selectDao;


	public boolean intoMariaViewPcodeDataV3(String id, List<BaseCVData> aggregateList, boolean toMongodb) {
		boolean result = false;

		if (aggregateList != null) {

			HashMap<String, ArrayList<BaseCVData>> flushMap = makeFlushMap(aggregateList);

			if ( flushMap.keySet().size() != 0) {
				if (toMongodb) {
					try {
						String writeFileName = String.format("insertIntoError_%s_%s", Thread.currentThread().getName(), DateUtils.getDate("yyyyMMdd_HHmm") );
						ConsumerFileUtils.writeLine( logPath +"retry/", writeFileName, G.view_pcode, aggregateList);
					} catch (IOException e) {
						logger.error("err - {}", e);
					}

					logger.info("chking fail viewPcodeData fileWriteOk flushMap.keySet() - {}", flushMap.keySet() );
					result = true;
				} else {
					result = viewPcodeDataDao.transectionRuningV2( flushMap );
				}
			} else {
				result = true;
			}
		} else {
			result = true;
		}
		return result;
	}


	public  HashMap<String, ArrayList<BaseCVData>> makeFlushMap(List<BaseCVData> aggregateList) {
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

					logger.debug("vo - {}", vo);
					this.add(flushMap, "INSERT_MOB_PCODE_ADVER_STATS", vo);
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
