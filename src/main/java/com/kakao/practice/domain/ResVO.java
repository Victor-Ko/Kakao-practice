package com.kakao.practice.domain;

import java.util.List;

public class ResVO {
	
	private String resCd;
	
	private String tokenVal; //토큰값
	
	private String reqDtm;
	
	private int totalAmt; //금액
	
	private int completeAmt;
	
	private List<SpreadDtVo> completeList;

	public String getResCd() {
		return resCd;
	}

	public void setResCd(String resCd) {
		this.resCd = resCd;
	}

	public String getTokenVal() {
		return tokenVal;
	}

	public void setTokenVal(String tokenVal) {
		this.tokenVal = tokenVal;
	}

	public String getReqDtm() {
		return reqDtm;
	}

	public void setReqDtm(String reqDtm) {
		this.reqDtm = reqDtm;
	}

	public int getTotalAmt() {
		return totalAmt;
	}

	public void setTotalAmt(int totalAmt) {
		this.totalAmt = totalAmt;
	}

	public int getCompleteAmt() {
		return completeAmt;
	}

	public void setCompleteAmt(int completeAmt) {
		this.completeAmt = completeAmt;
	}

	public List<SpreadDtVo> getCompleteList() {
		return completeList;
	}

	public void setCompleteList(List<SpreadDtVo> completeList) {
		this.completeList = completeList;
	}

}
