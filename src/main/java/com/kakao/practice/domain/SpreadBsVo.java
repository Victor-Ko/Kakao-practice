package com.kakao.practice.domain;

import java.util.List;

public class SpreadBsVo {

	private String tokenVal; //토큰값
	
	private int totalAmt; //금액
	
	private int totalUser; //금액
	
	private String reqUserId; //요청한 사용자
	
	private String reqRoomId; //요청방 이름
	
	private String reqDtm; //요청 시각
	
	List<SpreadDtVo> spreadList;

	public String getTokenVal() {
		return tokenVal;
	}

	public void setTokenVal(String tokenVal) {
		this.tokenVal = tokenVal;
	}

	public int getTotalAmt() {
		return totalAmt;
	}

	public void setTotalAmt(int totalAmt) {
		this.totalAmt = totalAmt;
	}

	public int getTotalUser() {
		return totalUser;
	}

	public void setTotalUser(int totalUser) {
		this.totalUser = totalUser;
	}

	public String getReqUserId() {
		return reqUserId;
	}

	public void setReqUserId(String reqUserId) {
		this.reqUserId = reqUserId;
	}

	public String getReqRoomId() {
		return reqRoomId;
	}

	public void setReqRoomId(String reqRoomId) {
		this.reqRoomId = reqRoomId;
	}
	
	public String getReqDtm() {
		return reqDtm;
	}

	public void setReqDtm(String reqDtm) {
		this.reqDtm = reqDtm;
	}

	public List<SpreadDtVo> getSpreadList() {
		return spreadList;
	}

	public void setSpreadList(List<SpreadDtVo> spreadList) {
		this.spreadList = spreadList;
	}
}
