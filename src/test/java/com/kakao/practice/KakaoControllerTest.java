package com.kakao.practice;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.kakao.practice.domain.SpreadBsVo;
import com.kakao.practice.domain.SpreadDtVo;
import com.kakao.practice.mapper.KakaoMapper;

public class KakaoControllerTest {

	@Autowired
	private KakaoMapper mapper;
	
	@Test
	public void spreadApi() {
		String token = "K".concat("T".concat("R"));
		
		//토근이 3자리인지
		assertEquals(token.length(), 3);
		
		int amtSum = 0;
		int spreadAmt = 1000;
		
		for(int i=0; i<3; i++) {
			int calcAmt = 0;
			if( i < 3) {
				calcAmt = (int)(Math.random() * (spreadAmt / 3) ) + 1;
				amtSum += calcAmt;
			}else {
				calcAmt = spreadAmt - amtSum;
			}
			
			//뿌리기 금액과 계산값 검증
			assertEquals(amtSum+calcAmt, spreadAmt);
		}
		
	}
	
	@Test
	public void receiveApi() {
		
		String token = "KTR";
		String userId = "Tmdwo0610";
		String roomId = "Room00001";
		
		SpreadBsVo selectResultVo = mapper.selectValidData(token);
		
		//본인은 받기 불가
		assertEquals(userId, selectResultVo.getReqUserId());
		
		//해당방의 뿌리기건이 맞는지
		assertEquals(roomId, selectResultVo.getReqRoomId());
		
		String date = "20200626";
		String time = "115015";
		
		//오늘이면서 10분이 지났는지
		assertEquals(date, selectResultVo.getReqDtm().subSequence(0, 8));
		assertTrue((Integer.parseInt(selectResultVo.getReqDtm().substring(10,12)) + 10) > Integer.parseInt(time.substring(2,4)) );
		
		//기존에 받은사람인지
		SpreadBsVo selectAll = mapper.selectSpreadDs(token);
		assertTrue(selectAll.getSpreadList().stream().
				anyMatch(list -> list.getSendUserId().equals(userId)));
	}
	
	@Test
	public void retrieveApi( ) {
	
		String token = "KTR";
		String userId = "Tmdwo0610";
		SpreadBsVo selectResultVo = mapper.selectSpreadDs(token);
		
		//조회데이터가 없는경우
		assertEquals(selectResultVo, null);
		
		//조회한사람이 등록한 사람이 맞는지
		assertEquals(userId, selectResultVo.getReqUserId());
		
		Date startDate = new Date();
		Date endDate   = new Date(20200701);
		long difference = (endDate.getTime() - startDate.getTime()) / (24 * 60 * 60 * 1000);
		
		//등록한지 7일이 지났는지
		assertTrue(difference > 7);
		
		
		assertEquals(selectResultVo.getSpreadList(), SpreadDtVo.class);
	}
}
