package com.kakao.practice.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.kakao.practice.domain.ResVO;
import com.kakao.practice.domain.SpreadBsVo;
import com.kakao.practice.domain.SpreadDtVo;
import com.kakao.practice.mapper.KakaoMapper;

@Controller
@RequestMapping("/kakao")
public class KakaoController {

	@Autowired
	private KakaoMapper mapper;
	
	/**
	 * 뿌리기 API
	 * 
	 * @param request
	 * @param spreadAmt
	 * @param spreadCnt
	 * @return
	 * @throws Exception
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	@RequestMapping(value="/spread", method=RequestMethod.POST)
	public ResVO spreadApi(HttpServletRequest request, int spreadAmt, int spreadCnt) {
		
		//뿌릴 금액, 뿌릴 인원을 요청값으로 받습니다.
		//뿌리기 요청건에 대한 고유 token을 발급하고 응답값으로 내려줍니다.
		//뿌릴 금액을 인원수에 맞게 분배하여 저장합니다. (분배 로직은 자유롭게 구현해 주세요.)
		//token은 3자리 문자열로 구성되며 예측이 불가능해야 합니다
		
		ResVO resVO = new ResVO();
		int amtSum = 0;
		
		String token = "K".concat(request.getHeader("X-USER-ID").substring(0,1).concat(request.getHeader("X-ROOM-ID").substring(0,1)));
		
		SpreadBsVo spreadBsVo = new SpreadBsVo();
		
		SimpleDateFormat formatDate = new SimpleDateFormat ( "yyyyMMddHHmmss");
		Date dtm = new Date();
		
		String date = formatDate.format(dtm);
		
		spreadBsVo.setTokenVal(token);
		spreadBsVo.setTotalAmt(spreadAmt);
		spreadBsVo.setTotalUser(spreadCnt);
		spreadBsVo.setReqUserId(request.getHeader("X-USER-ID"));
		spreadBsVo.setReqRoomId(request.getHeader("X-ROOM-ID"));
		spreadBsVo.setReqDtm(date);
		
		try {
			mapper.insertSpreadBs(spreadBsVo);
		} catch (Exception e1) {
			resVO.setResCd("99"); //오류
			return resVO;
		}
		
		for(int i=0; i<spreadCnt; i++) {
			int calcAmt = 0;
			if( i < spreadCnt) {
				calcAmt = (int)(Math.random() * (spreadAmt / spreadCnt) ) + 1;
				amtSum += calcAmt;
			}else {
				calcAmt = spreadAmt - amtSum;
			}
			
			SpreadDtVo spreadDtVo = new SpreadDtVo();
			
			spreadDtVo.setTokenVal(token);
			spreadDtVo.setRegSeq(i+1);
			spreadDtVo.setReceiveAmt(calcAmt);
			spreadDtVo.setReceiveYn("N");
			spreadDtVo.setSendUserId(null);
			spreadDtVo.setSendDtm(null);
			
			try {
				mapper.insertSpreadDt(spreadDtVo);
			} catch (Exception e) {
				resVO.setResCd("99"); //오류
				return resVO;
			}
			
		}
		
		resVO.setResCd("00"); //정상
		resVO.setTokenVal(token);
		
		return resVO;
	}
	
	/**
	 * 받기Api
	 * 
	 * @param request
	 * @param token
	 * @return
	 * @throws Exception
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	@RequestMapping(value="/receive", method = RequestMethod.PUT)
	public ResVO receiveApi(HttpServletRequest request, String token) {
		
		//뿌리기 시 발급된 token을 요청값으로 받습니다.
		//token에 해당하는 뿌리기 건 중 아직 누구에게도 할당되지 않은 분배건 하나를 API를 호출한 사용자에게 할당하고, 그 금액을 응답값으로 내려줍니다.
		//뿌리기 당 한 사용자는 한번만 받을 수 있습니다.
		//자신이 뿌리기한 건은 자신이 받을 수 없습니다.
		//뿌린기가 호출된 대화방과 동일한 대화방에 속한 사용자만이 받을 수 있습니다.
		//뿌린 건은 10분간만 유효합니다. 뿌린지 10분이 지난 요청에 대해서는 받기 실패 응답이 내려가야 합니다.

		ResVO resVO = new ResVO();
		
		String userId = request.getHeader("X-USER-ID");
		String roomId = request.getHeader("X-ROOM-ID");
		
		SimpleDateFormat formatDate = new SimpleDateFormat ( "yyyyMMdd");
		SimpleDateFormat formatTime = new SimpleDateFormat ( "HHmmss");
		
		Date dtm = new Date();
		
		String date = formatDate.format(dtm);
		String time = formatTime.format(dtm);
		
		SpreadBsVo selectResultVo = mapper.selectValidData(token);
		
		if( userId.equals( selectResultVo.getReqUserId() ) ) {
			resVO.setResCd("99"); //오류
			return resVO;
		}
		
		if( roomId.equals(selectResultVo.getReqRoomId()) ) {
			resVO.setResCd("99"); //오류
			return resVO;
		}
		
		if(selectResultVo.getReqDtm().substring(0, 8).equals(date)
				&& ((Integer.parseInt(selectResultVo.getReqDtm().substring(10,12)) + 10) > Integer.parseInt(time.substring(2,4)) ) ) {
			resVO.setResCd("99"); //오류
			return resVO;
		}
		
		SpreadBsVo selectAll = mapper.selectSpreadDs(token);
		
		List<SpreadDtVo> allList = selectAll.getSpreadList();
		if(allList.stream().anyMatch(list -> list.getSendUserId().equals(userId))) {
			resVO.setResCd("99"); //오류
			return resVO;
		}
		
		SpreadDtVo recieveVo = selectResultVo.getSpreadList().get(0);
		
		try {
			SpreadDtVo spreadDtVo = new SpreadDtVo();
			spreadDtVo.setTokenVal(recieveVo.getTokenVal());
			spreadDtVo.setRegSeq(recieveVo.getRegSeq());
			spreadDtVo.setSendDtm(date.concat(time));
			spreadDtVo.setSendUserId(userId);
			spreadDtVo.setReceiveYn("Y");
			
			mapper.updateSpreadDt(spreadDtVo);
		} catch (Exception e) {
			resVO.setResCd("99"); //오류
			return resVO;
		}
		
		resVO.setResCd("00");
		resVO.setCompleteAmt(recieveVo.getReceiveAmt());
		
		return resVO;
		
	}
	
	/**
	 * 조회Api
	 * 
	 * @param request
	 * @param token
	 * @return
	 * @throws ParseException 
	 */
	@RequestMapping(value="/retrieve", method = RequestMethod.GET)
	@Transactional(readOnly = true)
	public ResVO retrieveApi(HttpServletRequest request, String token) throws ParseException {
		
		//뿌리기 시 발급된 token을 요청값으로 받습니다.
		//token에 해당하는 뿌리기 건의 현재 상태를 응답값으로 내려줍니다. 현재 상태는 다음의 정보를 포함합니다.
		//뿌린 시각, 뿌린 금액, 받기 완료된 금액, 받기 완료된 정보 ([받은 금액, 받은 사용자 아이디] 리스트)
		//뿌린 사람 자신만 조회를 할 수 있습니다. 다른사람의 뿌리기건이나 유효하지 않은 token에 대해서는 조회 실패 응답이 내려가야 합니다.
		//뿌린 건에 대한 조회는 7일 동안 할 수 있습니다.

		ResVO resVO = new ResVO();
		
		String userId = request.getHeader("X-USER-ID");
		SpreadBsVo selectResultVo = mapper.selectSpreadDs(token);
		
		if(selectResultVo == null) {
			resVO.setResCd("99"); //오류
			return resVO;
		}
		
		SimpleDateFormat formatDate = new SimpleDateFormat ( "yyyyMMdd");
		Date startDate = new Date();
		
		Date endDate = formatDate.parse(selectResultVo.getReqDtm().substring(0,8));
		long difference = (endDate.getTime() - startDate.getTime()) / (24 * 60 * 60 * 1000);
		
		if(difference > 7) {
			resVO.setResCd("99"); //오류
			return resVO;
		}
		
		if( userId.equals( selectResultVo.getReqUserId() ) ) {
			resVO.setResCd("00"); //정상
			resVO.setReqDtm(selectResultVo.getReqDtm());
			resVO.setTotalAmt(selectResultVo.getTotalAmt());
			int completeAmt = selectResultVo.getSpreadList().stream().mapToInt(SpreadDtVo::getReceiveAmt).sum();
			resVO.setCompleteAmt(completeAmt);
			resVO.setCompleteList(selectResultVo.getSpreadList());
		}else {
			resVO.setResCd("99"); //오류
			return resVO;
		}
		
		return resVO;
	}
	
}
