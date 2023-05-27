/*
SQLyog Ultimate - MySQL GUI v8.2 
MySQL - 5.5.5-10.2.6-MariaDB-log : Database - dreamsearch
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`dreamsearch` /*!40100 DEFAULT CHARACTER SET utf8 */;

-- USE `dreamsearch`;

/*Data for the table `MOBON_COM_CODE` */

insert  into `MOBON_COM_CODE`(`CODE_TP_ID`,`CODE_ID`,`CODE_VAL`,`USE_YN`,`CODE_DESC`,`REG_USER_ID`,`REG_DTTM`,`ALT_USER_ID`,`ALT_DTTM`) values ('ADVRTS_PRDT_CODE','01','b','Y','배너(b)','ADMIN','2017-09-27 12:16:48','01012341234',NULL),('ADVRTS_PRDT_CODE','02','s','Y','브랜드링크(s)','ADMIN','2017-09-27 12:16:48',NULL,NULL),('ADVRTS_PRDT_CODE','03','i','Y','아이커버(i)','ADMIN','2017-09-27 12:16:48',NULL,NULL),('ADVRTS_PRDT_CODE','04','c','Y','쇼콘(c)','ADMIN','2017-09-27 12:16:48',NULL,NULL),('ADVRTS_STLE_TP_CODE','01','상품화 배너','Y','상품화 배너로 송출시','ADMIN','2017-08-31 18:11:04',NULL,NULL),('ADVRTS_STLE_TP_CODE','02','고정 배너','Y','고정 배너로 송출','ADMIN','2017-08-31 18:11:24',NULL,NULL),('ADVRTS_TP_CODE','01','AD','Y','베이스(AD)','ADMIN','2017-09-27 11:51:22',NULL,NULL),('ADVRTS_TP_CODE','02','CA','Y','할인금액쇼콘(CA)','ADMIN','2017-09-27 11:51:22',NULL,NULL),('ADVRTS_TP_CODE','03','CC','Y','이벤트타겟팅(CC)','ADMIN','2017-09-27 11:51:22',NULL,NULL),('ADVRTS_TP_CODE','04','CW','Y','장바구니(CW)','ADMIN','2017-09-27 11:51:22',NULL,NULL),('ADVRTS_TP_CODE','05','HU','Y','헤비유저(HU)','ADMIN','2017-09-27 11:51:22',NULL,NULL),('ADVRTS_TP_CODE','06','KL','Y','키워드(KL)','ADMIN','2017-09-27 11:51:22',NULL,NULL),('ADVRTS_TP_CODE','07','KP','Y','키워드상품(KP)','ADMIN','2017-09-27 11:51:22',NULL,NULL),('ADVRTS_TP_CODE','08','PB','Y','프리미엄배너(PB)','ADMIN','2017-09-27 11:51:22',NULL,NULL),('ADVRTS_TP_CODE','09','PE','Y','할인율쇼콘(PE)','ADMIN','2017-09-27 11:51:22',NULL,NULL),('ADVRTS_TP_CODE','10','RC','Y','리사이클(RC)','ADMIN','2017-09-27 11:51:22',NULL,NULL),('ADVRTS_TP_CODE','11','RR','Y','자사 타게팅(RM)','ADMIN','2017-09-27 11:51:22',NULL,NULL),('ADVRTS_TP_CODE','12','RR','Y','RM리사이클(RR)','ADMIN','2017-09-27 11:51:22',NULL,NULL),('ADVRTS_TP_CODE','13','SA','Y','브랜드박스(SA)','ADMIN','2017-09-27 11:51:22',NULL,NULL),('ADVRTS_TP_CODE','14','SH','Y','브랜드박스 안 캐시백상품(SH)','ADMIN','2017-09-27 11:51:22',NULL,NULL),('ADVRTS_TP_CODE','15','SJ','Y','쇼핑입점(SJ)','ADMIN','2017-09-27 11:51:22',NULL,NULL),('ADVRTS_TP_CODE','16','SP','Y','추천(일반상품)(SP)','ADMIN','2017-09-27 11:51:22',NULL,NULL),('ADVRTS_TP_CODE','17','SR','Y','본상품(SR)','ADMIN','2017-09-27 11:51:22',NULL,NULL),('ADVRTS_TP_CODE','18','ST','Y','투데이베스트(ST)','ADMIN','2017-09-27 11:51:22',NULL,NULL),('ADVRTS_TP_CODE','19','UM','Y','성향(도메인)(UM)','ADMIN','2017-09-27 11:51:22',NULL,NULL),('ALGM_TP_CODE','01','초기최대프레임수','Y','초기최대프레임수','ADMIN','2017-10-24 10:11:24',NULL,NULL),('ALGM_TP_CODE','02','기준요구클릭수','Y','기준요구클릭수','ADMIN','2017-10-24 10:11:24',NULL,NULL),('ALGM_TP_CODE','03','기준사이클수','Y','기준사이클수','ADMIN','2017-10-24 10:11:24',NULL,NULL),('ALGM_TP_CODE','04','최소요구노출수','Y','최소요구노출수','ADMIN','2017-10-24 10:11:24',NULL,NULL),('ALGM_TP_CODE','05','상위백분율(기준 50~95%)','Y','상위백분율(기준 50~95%)','ADMIN','2017-10-24 10:11:24',NULL,NULL),('ALGM_TP_CODE','06','블랙리스트횟수','Y','블랙리스트횟수','ADMIN','2017-10-24 10:11:24',NULL,NULL),('CHRG_TP_CODE','01','정상과금','Y',NULL,'ADMIN','2017-09-18 14:09:11',NULL,NULL),('CHRG_TP_CODE','91','프리퀀시 무효클릭','Y',NULL,'ADMIN','2017-09-18 14:09:11',NULL,NULL),('CHRG_TP_CODE','92','라이브 OFF 무효클릭','Y',NULL,'ADMIN','2017-09-18 14:09:11',NULL,NULL),('CNVRS_TP_CODE','01','카카오(R)','Y',NULL,'ADMIN','2017-07-07 18:02:32',NULL,NULL),('DESC_TP_CODE','01','사이트공통설명1','Y',NULL,'ADMIN','2017-09-25 14:32:51',NULL,NULL),('DESC_TP_CODE','02','사이트공통설명2','Y',NULL,'ADMIN','2017-09-25 14:32:51',NULL,NULL),('DESC_TP_CODE','03','사이트공통설명3','Y',NULL,'ADMIN','2017-09-25 14:32:51',NULL,NULL),('DESC_TP_CODE','04','사이트공통설명4','Y',NULL,'ADMIN','2017-09-25 14:32:51',NULL,NULL),('DESC_TP_CODE','05','사이트공통설명5','Y',NULL,'ADMIN','2017-09-25 14:32:51',NULL,NULL),('DESC_TP_CODE','06','사이트공통설명6','Y',NULL,'ADMIN','2017-09-25 14:32:51',NULL,NULL),('DESC_TP_CODE','07','사이트공통설명7','Y',NULL,'ADMIN','2017-09-25 14:32:51',NULL,NULL),('DESC_TP_CODE','08','사이트공통설명8','Y',NULL,'ADMIN','2017-09-25 14:32:51',NULL,NULL),('DESC_TP_CODE','09','사이트공통설명9','Y',NULL,'ADMIN','2017-09-25 14:32:51',NULL,NULL),('DESC_TP_CODE','10','사이트공통설명10','Y',NULL,'ADMIN','2017-09-25 14:32:51',NULL,NULL),('FRME_STATE_CODE','01','사용','Y','사용','ADMIN','2017-10-24 10:27:28',NULL,NULL),('FRME_STATE_CODE','02','대기','Y','대기','ADMIN','2017-10-24 10:27:28',NULL,NULL),('FRME_STATE_CODE','03','우선추가','Y','우선추가','ADMIN','2017-10-24 10:27:28',NULL,NULL),('FRME_STATE_CODE','91','중지','Y','중지','ADMIN','2017-10-24 10:27:28',NULL,NULL),('HH_TP_CODE','01','1','Y','0~1','ADMIN','2017-10-16 14:59:48',NULL,NULL),('HH_TP_CODE','02','2','Y','1~2','ADMIN','2017-10-16 14:59:48',NULL,NULL),('HH_TP_CODE','03','3','Y','2~3','ADMIN','2017-10-16 14:59:48',NULL,NULL),('HH_TP_CODE','04','4','Y','3~4','ADMIN','2017-10-16 14:59:48',NULL,NULL),('HH_TP_CODE','05','5','Y','4~5','ADMIN','2017-10-16 14:59:48',NULL,NULL),('HH_TP_CODE','06','6','Y','5~6','ADMIN','2017-10-16 14:59:48',NULL,NULL),('HH_TP_CODE','07','7','Y','6~7','ADMIN','2017-10-16 14:59:48',NULL,NULL),('HH_TP_CODE','08','8','Y','7~8','ADMIN','2017-10-16 14:59:48',NULL,NULL),('HH_TP_CODE','09','9','Y','8~9','ADMIN','2017-10-16 14:59:48',NULL,NULL),('HH_TP_CODE','10','10','Y','9~10','ADMIN','2017-10-16 14:59:48',NULL,NULL),('HH_TP_CODE','11','11','Y','10~11','ADMIN','2017-10-16 14:59:48',NULL,NULL),('HH_TP_CODE','12','12','Y','11~12','ADMIN','2017-10-16 14:59:48',NULL,NULL),('HH_TP_CODE','13','13','Y','12~13','ADMIN','2017-10-16 14:59:48',NULL,NULL),('HH_TP_CODE','14','14','Y','13~14','ADMIN','2017-10-16 14:59:48',NULL,NULL),('HH_TP_CODE','15','15','Y','14~15','ADMIN','2017-10-16 14:59:48',NULL,NULL),('HH_TP_CODE','16','16','Y','15~16','ADMIN','2017-10-16 14:59:48',NULL,NULL),('HH_TP_CODE','17','17','Y','16~17','ADMIN','2017-10-16 14:59:48',NULL,NULL),('HH_TP_CODE','18','18','Y','17~18','ADMIN','2017-10-16 14:59:48',NULL,NULL),('HH_TP_CODE','19','19','Y','18~19','ADMIN','2017-10-16 14:59:48',NULL,NULL),('HH_TP_CODE','20','20','Y','19~20','ADMIN','2017-10-16 14:59:48',NULL,NULL),('HH_TP_CODE','21','21','Y','20~21','ADMIN','2017-10-16 14:59:48',NULL,NULL),('HH_TP_CODE','22','22','Y','21~22','ADMIN','2017-10-16 14:59:48',NULL,NULL),('HH_TP_CODE','23','23','Y','22~23','ADMIN','2017-10-16 14:59:48',NULL,NULL),('HH_TP_CODE','24','24','Y','23~24','ADMIN','2017-10-16 14:59:48',NULL,NULL),('MBIL_TP_CODE','01','모바일','Y','랜딩 타입 코드','ADMIN','2017-09-01 18:35:49',NULL,NULL),('MBIL_TP_CODE','02','Android','Y','랜딩 타입 코드','ADMIN','2017-09-01 18:36:08',NULL,NULL),('MBIL_TP_CODE','03','iOS','Y','랜딩 타입 코드','ADMIN','2017-09-01 18:36:20',NULL,NULL),('MEDIA_TP_CODE','01','언론사','Y',NULL,'ADMIN','2017-09-20 19:52:12',NULL,NULL),('MEDIA_TP_CODE','02','웹하드','Y',NULL,'ADMIN','2017-09-20 19:52:12',NULL,NULL),('MEDIA_TP_CODE','03','커뮤니티','Y',NULL,'ADMIN','2017-09-20 19:52:12',NULL,NULL),('MEDIA_TP_CODE','04','블로그','Y',NULL,'ADMIN','2017-09-20 19:52:12',NULL,NULL),('MEDIA_TP_CODE','05','포털','Y',NULL,'ADMIN','2017-09-20 19:52:12',NULL,NULL),('MEDIA_TP_CODE','06','기타','Y',NULL,'ADMIN','2017-09-20 19:52:12',NULL,NULL),('MEDIA_TP_CODE','07','바콘','Y',NULL,'ADMIN','2017-09-20 19:52:12',NULL,NULL),('MEDIA_TP_CODE','08','중요매체','Y',NULL,'ADMIN','2017-09-20 19:52:12',NULL,NULL),('MEDIA_TP_CODE','09','쇼콘','Y',NULL,'ADMIN','2017-09-20 19:52:12',NULL,NULL),('MEDIA_TP_CODE','10','쇼핑app','Y',NULL,'ADMIN','2017-09-20 19:52:12',NULL,NULL),('MEDIA_TP_CODE','11','플로팅','Y',NULL,'ADMIN','2017-09-20 19:52:12',NULL,NULL),('MEDIA_TP_CODE','12','스타샵','Y',NULL,'ADMIN','2017-09-20 19:52:12',NULL,NULL),('MEDIA_TP_CODE','13','APP','Y',NULL,'ADMIN','2017-09-20 19:52:12',NULL,NULL),('MEDIA_TP_CODE','90','미분류','Y',NULL,'ADMIN','2017-09-20 19:52:12',NULL,NULL),('PLTFOM_TP_CODE','01','WEB','Y',NULL,'ADMIN','2017-07-07 18:02:31',NULL,NULL),('PLTFOM_TP_CODE','02','MOBILE','Y',NULL,'ADMIN','2017-07-07 18:02:31',NULL,NULL),('PRDT_TP_CODE','01','상품','Y','상품','ADMIN','2017-10-23 20:38:29',NULL,NULL),('PRDT_TP_CODE','02','비상품','Y','비상품','ADMIN','2017-10-23 20:38:49',NULL,NULL),('REST_RSN_CODE','01','실광고주전환','Y','실광고주전환','ADMIN','2017-06-01 17:02:49',NULL,NULL),('REST_RSN_CODE','02','환불','Y','환불','ADMIN','2017-06-01 17:02:49',NULL,NULL),('REST_RSN_CODE','03','망고스타일','Y','망고스타일','ADMIN','2017-06-01 17:02:49',NULL,NULL),('REST_RSN_CODE','04','보너스충전','Y','보너스충전','ADMIN','2017-06-01 17:02:49',NULL,NULL),('REST_RSN_CODE','05','테스트충전','Y','테스트충전','ADMIN','2017-06-01 17:02:49',NULL,NULL),('REST_RSN_CODE','06','네이트','Y','네이트','ADMIN','2017-06-01 17:02:49',NULL,NULL),('REST_RSN_CODE','07','잔액이전','Y','잔액이전','ADMIN','2017-06-01 17:02:49',NULL,NULL),('REST_RSN_CODE','99','기타','Y','기타사유','ADMIN','2017-06-01 17:02:49',NULL,NULL),('SDK_TP_CODE','01','ending','Y','엔딩커버','ADMIN','2017-07-14 14:33:52',NULL,NULL),('SDK_TP_CODE','02','intro','Y','인트로','ADMIN','2017-07-14 14:33:52',NULL,NULL),('SDK_TP_CODE','03','banner','Y','띠배너','ADMIN','2017-07-14 14:33:52',NULL,NULL),('SDK_TP_CODE','04','json_s1','Y','기타1','ADMIN','2017-07-14 14:33:52',NULL,NULL),('SDK_TP_CODE','05','json_s2','Y','기타2','ADMIN','2017-07-14 14:33:52',NULL,NULL),('SETLE_PSNCH_CODE','01','광고주','Y','광고주','ADMIN','2017-06-01 17:02:49',NULL,NULL),('SETLE_PSNCH_CODE','02','대행사','Y','대행사','ADMIN','2017-06-01 17:02:49',NULL,NULL),('SETLE_TY_CODE','01','선불','Y','선불결제','ADMIN','2017-06-01 17:02:49',NULL,NULL),('SETLE_TY_CODE','02','후불','Y','후불결제','ADMIN','2017-06-01 17:02:49',NULL,NULL);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;