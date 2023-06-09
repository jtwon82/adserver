package com.mobon.billing.core.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.adgather.constants.G;
import com.adgather.util.old.DateUtils;
import com.mobon.billing.core.service.dao.ShopInfoDataDao;
import com.mobon.billing.model.v15.BaseCVData;
import com.mobon.billing.model.v15.ShopInfoData;
import com.mobon.billing.util.ConsumerFileUtils;

@Service
public class ShopInfoDataToMariaDB {
	private static final Logger	logger	= LoggerFactory.getLogger(ShopInfoDataToMariaDB.class);

	@Autowired
	private ShopInfoDataDao	shopInfoDataDao;
	
	@Value("${log.path}")
	private String	logPath;
	
	
	
	
	public boolean intoMariaShopInfoDataV3(String _id, List<ShopInfoData> aggregateList, boolean toMongodb) {
		boolean result = false;
		
		long start_millis = System.currentTimeMillis();
		if (aggregateList != null){
			HashMap<String, ArrayList<ShopInfoData>> flushMap = makeFlushMap(aggregateList);
			
			if ( flushMap.keySet().size() != 0) {
				if( toMongodb ){
					try {
						String writeFileName = String.format("insertIntoError_%s_%s", Thread.currentThread().getName(), DateUtils.getDate("yyyyMMdd_HHmm") );
						ConsumerFileUtils.writeLine( logPath +"retry/", writeFileName, G.shop_info, aggregateList);
					} catch (IOException e) {
						logger.error("err - {}", e);
					}
					
					logger.info("chking fail ShopInfoData fileWriteOk flushMap.keySet() - {}", flushMap.keySet() );
					result = true;
				} else {
					result = shopInfoDataDao.transectionRuningV2( flushMap );
					logger.info("succ intoMariaShopInfoData _id - {}, size - {}, during - {}", _id, flushMap.keySet().size(), System.currentTimeMillis() - start_millis );
				}
			} else {
				result = true;
			}
		} else {
			result = true;
		}
		return result;
	}
	
	public HashMap<String, ArrayList<ShopInfoData>> makeFlushMap(List<ShopInfoData> aggregateList){
		HashMap<String, ArrayList<ShopInfoData>> flushMap = new HashMap();
		
		for ( ShopInfoData vo : aggregateList) {
			logger.debug("vo - {}", vo);
			try {
				if (vo != null) {
					if(vo.getPlatform()==null && "".equals(vo.getPlatform())){
						logger.debug("Missing required platform vo - {}", vo);
						continue;
					}
					if( !StringUtils.isNumeric(vo.getPlatform()) ) vo.setPlatform(G.convertPLATFORM_CODE(vo.getPlatform()));
					
					if( "01".equals(vo.getPlatform()) ){
						if( vo.isbHandlingStatsMobon() ) {
							//add(flushMap, "sp_shop_info_NEW_W", vo);
						} else {
							add(flushMap, "sp_shop_info_NEW_W_billing", vo);
						}
					}
					else if( "02".equals(vo.getPlatform()) ){
						if(vo.isbHandlingStatsMobon()) {
							//add(flushMap, "sp_shop_info_NEW_M", vo);
						} else {
							add(flushMap, "sp_shop_info_NEW_M_billing", vo);
						}
					}
					else {
						if(vo.isbHandlingStatsMobon()) {
							//add(flushMap, "sp_shop_info_NEW_D", vo);
						} else {
							add(flushMap, "sp_shop_info_NEW_D_billing", vo);
						}
					}
				}
			} catch (Exception e) {
				logger.error("err item - {}, msg - {}, ", vo, e);
			}
		}
		return flushMap;
	}
	private void add(HashMap<String, ArrayList<ShopInfoData>> flushMap, String key, ShopInfoData vo) {
		if(flushMap.get(key)==null){
			flushMap.put(key, new ArrayList());
		}
		ArrayList<ShopInfoData> l = flushMap.get(key);
		l.add(vo);
	}
}
