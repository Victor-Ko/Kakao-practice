package com.kakao.practice.domain;

public class SpreadDtVo {

	private String tokenVal; //토큰값
	
	private int regSeq; //등록순번
	
	private int receiveAmt; //수령금액
	
	private String sendUserId; //받은 사용자
	
	private String sendDtm; //받은시각
	
	private String receiveYn; //수령여부 

	public String getTokenVal() {
		return tokenVal;
	}

	public void setTokenVal(String tokenVal) {
		this.tokenVal = tokenVal;
	}

	public int getRegSeq() {
		return regSeq;
	}

	public void setRegSeq(int regSeq) {
		this.regSeq = regSeq;
	}

	public int getReceiveAmt() {
		return receiveAmt;
	}

	public void setReceiveAmt(int receiveAmt) {
		this.receiveAmt = receiveAmt;
	}

	public String getSendUserId() {
		return sendUserId;
	}

	public void setSendUserId(String sendUserId) {
		this.sendUserId = sendUserId;
	}

	public String getSendDtm() {
		return sendDtm;
	}

	public void setSendDtm(String sendDtm) {
		this.sendDtm = sendDtm;
	}

	public String getReceiveYn() {
		return receiveYn;
	}

	public void setReceiveYn(String receiveYn) {
		this.receiveYn = receiveYn;
	}
	
}
