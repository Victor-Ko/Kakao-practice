package com.kakao.practice.mapper;

import com.kakao.practice.domain.SpreadBsVo;
import com.kakao.practice.domain.SpreadDtVo;

public interface KakaoMapper {

	//뿌리기기본 저장
	/*
	 * INSERT INTO SPREADBS
	 * VALUE(
	 * 	  vo.tokenVal
     *  , vo.totalAmt
	 *  , vo.totalUser
	 *  , vo.reqUserId
	 *  , vo.reqRoomId
	 *  , vo.reqDtm
	 * )
	 */
	public void insertSpreadBs(SpreadBsVo vo) throws Exception;
	//뿌리기내역 저장
	/*
	 * INSERT INTO SPREADDT
	 * VALUE(
	 *    vo.tokenVal
     *  , vo.regSeq
	 *  , vo.receiveAmt
	 *  , vo.sendUserId
	 *  , vo.sendDtm
	 *  , vo.receiveYn
	 * )
	 */
	public void insertSpreadDt(SpreadDtVo vo) throws Exception;
	
	//내역 조회
	/*
	 * SELECT *
	 * FROM   SPREADBS A
	 *      , SPREADDT B
	 * WHERE  A.tokenVal = B.tokenVal
	 * AND    A.tokenVal = in.tokenVal
	 */
	public SpreadBsVo selectSpreadDs(String token);

	//받은 내역 수정
	/*
	 * UPDATE SPREADDT
	 * SET    sendDtm    = in.sendDtm
	 *      , sendUserId = in.sendUserId
	 *      , receiveYn  = in.receiveYn
	 * WHERE  tokenVal = in.tokenVal
	 * AND    regSeq   = in.regSeq
	 */
	public void updateSpreadDt(SpreadDtVo vo) throws Exception;
	
	//받기 완료되지않은 내역 조회
	/*
	 * SELECT *
	 * FROM   SPREADBS A
	 *      , SPREADDT B
	 * WHERE  A.tokenVal = B.tokenVal
	 * AND    A.tokenVal = in.tokenVal
	 * AMD    A.receiveYn = 'N'
	 */
	public SpreadBsVo selectValidData(String token);

	
}
