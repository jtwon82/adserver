package com.mobon.billing.dump.constants;

import java.util.ArrayList;
import java.util.List;

/**
 * @FileName : GlobalConstants.java
 * @Project : mobon-billing-dump
 * @Date : 2020. 3. 2. 
 * @Author dkchoi
 * @Comment : dump 상수
 */
public abstract class GlobalConstants {

	private GlobalConstants() {
		throw new AssertionError();
	}

	public final static String ABTEST_DELIMETER = "|"; // ABTEST TYPE 구분자 
	public final static String ABTEST_LOG_DISCOLLECT_MS_NO = "3464,3655,3380,3654,5437,5444,6732,7912"; // 전체 ABTEST 데이터 중 수집예외 지면 리스트 
	public final static String ABPARSTATS_COLLECT_MS_NO = ",11026,11045,22365,23218,132179,182124,284480,285116,388970,388971,388974,397277,422245,428762,432893,455581,468768,472460,482868,506557,516601,523729,542177,566540,568224,571505,597074,598454,598460,598466,598471,611653,612100,617808,617813,361526,566619,586778,586779,2782,586469,6074,9007,442109,14923,15444,16117,19277,59835,596300,88239,23222,"; // AB_PAR_STATS 수집지면 (특정광고차단 ABtest 2차)
	// public final static String ABPARSTATS_COLLECT_MS_NO = ",2780,2781,2782,8375,8377,10497,10498,10499,14635,14923,15444,16117,19277,19437,23222,31440,59835,361526,566619,586775,586778,586779,596300,60625,88239,132080,368273,442109,594322,601434,10500,31444,32050,32066,6074,9006,9007,586469,18448,32070,260052,"; // AB_PAR_STATS 수집지면 (특정광고차단 ABtest)
	// public final static String ABPARSTATS_COLLECT_MS_NO = ",41963,19277,18800,13866,21314,21533,21548,20411,17258,12615,14366,17171,20813,22256,22257,432022,132269,392449,365863,"; // AB_PAR_STATS 수집지면
	// public final static String ABADVERSTATS_COLLECT_ADVER_ID = ",mulawear,unlogistics,jdoubleu01,theaesoo,mimik,vittz1,yuanta,cnhglobal,yeorihan,kohonjin55,rara0629,dongwonmall,chicfox,bylogin11,saladcompany,davich1,"; // AB_ADVER_STATS 수집광고주
	// public final static String ABFRAMESIZE_DISCOLLECT_MS_NO = ",827,1120,1299,1533,2069,2780,2781,2782,2805,3596,3756,4165,4699,4833,5328,5606,5820,6074,6169,6817,6953,7109,7111,7308,7611,7928,8375,8377,8900,8963,9006,9007,9063,9376,9713,9757,9761,9769,9771,9776,9777,9778,9779,9780,9787,9790,9791,9793,9794,9795,9796,9797,9798,9799,9803,9986,9994,10175,10183,10481,10497,10557,10628,10641,10764,10981,11009,11049,11195,11920,12247,12248,12253,12256,12386,12450,12483,12633,12817,12827,12864,12899,12902,13004,13009,13018,13081,13322,13425,13426,13437,13443,13444,13445,13447,13448,13481,13502,13611,13616,13673,13674,13912,14056,14057,14069,14099,14114,14115,14144,14148,14149,14189,14330,14366,14392,14412,14427,14433,14443,14451,14637,14638,14664,14705,14708,14736,14937,14938,14971,15015,15018,15019,15260,15332,15458,15462,15473,15541,15626,15627,15848,15854,15855,15942,15943,16002,16131,16143,16296,16305,16342,16439,16534,16746,16749,16849,16904,16912,16913,16914,16916,16917,16929,16932,16966,17165,17166,17229,17234,17261,17262,17300,17375,17504,17513,17518,17605,17661,17692,17693,17703,17710,17733,17753,17754,17827,17828,17837,17846,17868,17869,17882,17887,17897,18003,18015,18016,18068,18106,18115,18136,18146,18254,18255,18328,18349,18386,18447,18450,18468,18470,18506,18536,18538,18546,18732,18777,18789,18859,18864,18905,18909,18948,18952,18987,18991,19015,19025,19044,19047,19072,19087,19100,19106,19186,19188,19190,19280,19281,19282,19283,19284,19285,19286,19287,19288,19289,19311,19314,19344,19345,19346,19347,19350,19368,19520,19521,19523,19524,19585,19593,19598,19618,19619,19723,19724,19727,19728,19736,19750,19752,19782,19784,19896,19940,19941,19942,19943,19944,19945,19997,19998,20052,20126,20179,20367,20380,20403,20823,20843,21091,21129,21180,21181,21254,21257,21358,21424,21463,21465,21734,21952,21967,21968,21970,21971,22236,22252,22297,22365,23040,23100,23146,23254,23256,23257,23259,23293,29678,29679,32842,36320,36321,36322,39181,40849,40850,40964,40969,40979,41003,42045,42247,42450,42451,42453,52431,58013,58764,58776,58777,58779,58780,59930,60624,60625,61545,88239,100290,117534,118289,126281,131454,131696,131992,131993,132003,132080,132090,132107,132108,132158,132159,132185,132341,132356,132441,132445,132520,133062,133124,133152,133201,133316,133364,133367,133368,133473,182073,184360,184361,184362,195079,230085,253837,274495,360417,404643,404687,407098,437108,445523,445658,480808,483321,"; // AB_COM_FRAME_SIZE 수집예외 지면
	public static List<Integer> ABFRAMESIZE_DISCOLLECT_MS_NO = new ArrayList<Integer>();
	public static List<Integer> FREQSDKDAYSTATS_SDK_MS_NO = new ArrayList<Integer>();
	public final static String ABFRAMESIZE_DISCOLLECT_TEST_MS_NO = ",1533,5678,7308,10499,12817,17846,18136,18439,18506,18536,18546,18864,18905,18987,18991,19727,21129,21952,22252,22297,23293,32842,40850,58013,117534,118289,131993,132003,132107,132185,132356,132441,132445,133062,133152,133201,133316,133364,133368,253837,"; // AB_COM_FRAME_SIZE 수집예외 지면 (테스트지면)
	public final static String ABFRAMESIZE_DISCOLLECT_ADVER_ID = ",ysmgysmg,nasign,anna13,hy8105,jungucase,newturntreerp,patchmd,takhiya,transino19,hackerseng,hdcapital1,lottetours,newtree,samsungcard1,calma,dntjr0203,glasscode3927,ICOKR,ljh1,tbroad,wevecompany,eduplex,benito,hventures,idhospital22,idskin22,interk1,p31online,thetoollab1,ulgul22,dashingdiva,brownbag,foodnamoo,lfmall,lottefoods,lush,pengsamsung,sdbio01,stockcom,ssfshop,adidas1,trudygirl7,kolonmall,orangeave,lifework,k2ci00,hansclub,daprs2015,songarak007,josephandstac,jstyle07,vigevanoec,nepa123,maxxism,jangsu9184,atny1,byecom,rainbownatu,namyangi,asobang,wadizreward,bucketplace,loloten,2001outlet,aritaum00,halfclub,boribori,ujaunni,prostj2013,"; // AB_COM_FRAME_SIZE 수집예외 광고주
	public final static int PROCESS_LOG_CNT = 1000000;	// 로그 진행 확인 단위
	public final static String MOBON_CLICKVIEW_FILE_NAME_PREFIX = "collect_freq_clickview_"; // 클리뷰파일명
	public final static String MOBON_CONVERSION_FILE_NAME_PREFIX = "collect_freq_conversion_"; // 컨버전 파일명
	public final static String MOBON_FILE_EXTENSION = ".log"; // 파일 확장자
	public final static String ABCOMSTATS_SUMMARY_RESULT = "AB_COM_STATS_SUMMARY"; // AB_COM_STATS 수집 결과
	public final static String ABPARSTATS_SUMMARY_RESULT = "AB_PAR_STATS_SUMMARY"; // AB_PAR_STATS 수집 결과
	public final static String ABADVERSTATSWEB_SUMMARY_RESULT = "AB_ADVER_STATS_WEB_SUMMARY"; // AB_ADVER_STATS_WEB 수집 결과
	public final static String ABADVERSTATSMOBILE_SUMMARY_RESULT = "AB_ADVER_STATS_MOBILE_SUMMARY"; // AB_ADVER_STATS_MOBILE 수집 결과
	public final static String ABCOMFRAMESIZE_SUMMARY_RESULT = "AB_COM_FRAME_SIZE_SUMMARY"; // AB_COM_FRAME_SIZE 수집 결과
	public final static String ABCOMBIFRAMESIZE_SUMMARY_RESULT = "AB_COMBI_FRAME_SIZE_SUMMARY"; // AB_COMBI_FRAME_SIZE 수집 결과
	public final static String ABFRAMESIZE_SUMMARY_RESULT = "AB_FRAME_SIZE_SUMMARY"; // AB_FRAME_SIZE 수집 결과
	public final static String FREQDAYSTATS_SUMMARY_RESULT = "FREQ_DAY_STATS_SUMMARY"; // FREQ_DAY_STATS 수집 결과
	public final static String FREQSDKDAYSTATS_SUMMARY_RESULT = "FREQ_SDK_DAY_STATS_SUMMARY"; // FREQ_SDK_DAY_STATS 수집 결과
	public final static String FREQCAMPDAYSTATS_SUMMARY_RESULT = "FREQ_CAMP_DAY_STATS_SUMMARY"; // FREQ_CAMP_DAY_STATS 수집 결과
	public final static String FREQMEDIASCRIPTDAYSTATS_SUMMARY_RESULT = "FREQ_MEDIA_SCRIPT_DAY_STATS_SUMMARY"; // FREQ_MEDIA_SCRIPT_DAY_STATS 수집 결과
	public final static String PLTFOM_TP_CODE_WEB = "01"; // 플랫폼 구분코드 WEB값
	public final static String PLTFOM_TP_CODE_MOBILE = "02"; // 플랫폼 구분코드 MOBILE값
	public final static int MAX_ADVERID_FIELD = 30; // 광고주ID Size 체크
	
	public static final String FRAME_TP_CODE = "01|02|03|07"; //  플랫폼 전송 tp code  


}
