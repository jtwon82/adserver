DELIMITER $$

-- USE `dreamsearch`$$

DROP PROCEDURE IF EXISTS `sp_ico_view_point`$$

CREATE PROCEDURE `sp_ico_view_point`(
	  IN $sdate		VARCHAR (8)	CHARACTER SET euckr	/* 등록날짜 */
	, IN $platform		VARCHAR (1)	CHARACTER SET euckr	/* 웹모바일 구분 */
	, IN $product		VARCHAR (10)	CHARACTER SET euckr	/* 상품구분 */
	, IN $ad_gubun		VARCHAR (2)	CHARACTER SET euckr	/* 광고구분 */
	, IN $site_code		VARCHAR (32)	CHARACTER SET euckr	/* 사이트코드 */
	, IN $advertiser_id	VARCHAR (20)	CHARACTER SET euckr	/* 광고주 아이디 */
	, IN $scriptuserid	VARCHAR (50)	CHARACTER SET euckr	/* 매체ID */
	, IN $media_script_no	INT (11)	/* 매체스크립트번호 */
	, IN $kno		BIGINT	/* 키워드, 성향에서 유입경로를 알수있는 no값 */
	, IN $POINT		FLOAT (11)	/* 광고단가 */ 
	, IN $mpoint		FLOAT (11)	/* 매체정산단가 */ 
	, IN $tview_cnt		INT (11)	/* 총노출 */
)
BEGIN
	-- 아이커버 노출 처리하는 프로시져

	IF $kno IS NULL THEN 
		SET $kno = 0;
	END IF;
	IF $media_script_no IS NULL THEN
		SET $media_script_no = 0;
	END IF;

	IF $POINT>0 THEN
		UPDATE admember_TEST SET POINT=POINT-$POINT, lastupdate=NOW() WHERE userid = $advertiser_id;
		UPDATE iadsite_TEST SET usedmoney=usedmoney+$POINT WHERE site_code=$site_code AND svc_type='';
		UPDATE iadsite_TEST SET usedmoney=usedmoney+$POINT WHERE site_code_s=$site_code AND svc_type='' AND gubun = $ad_gubun;
	END IF;

	IF $mpoint>0 THEN
		UPDATE admember_TEST SET mpoint=mpoint+$mpoint, lastupdate=NOW() WHERE userid = $scriptuserid;
	END IF;

END$$

DELIMITER ;
