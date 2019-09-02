package com.baiwang.dataplatform.entity;

public class CountBean {
	private int id;
	private String contractCode;
	private String createTime;

	public CountBean() {
	}

	public CountBean(int id, String contractCode, String createTime) {
		this.id = id;
		this.contractCode = contractCode;
		this.createTime = createTime;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getContractCode() {
		return contractCode;
	}

	public void setContractCode(String contractCode) {
		this.contractCode = contractCode;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	@Override
	public String toString() {
		return "CountBean{" +
				"id=" + id +
				", contractCode='" + contractCode + '\'' +
				", createTime='" + createTime + '\'' +
				'}';
	}
}
