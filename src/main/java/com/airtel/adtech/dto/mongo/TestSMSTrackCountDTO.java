package com.airtel.adtech.dto.mongo;

import java.time.Instant;

public class TestSMSTrackCountDTO {

	private String id;
	private String userId;
	private Instant expireAt;
	private int remainingSms;

	public int getRemainingSms() {
		return remainingSms;
	}

	public void setRemainingSms(int remainingSms) {
		this.remainingSms = remainingSms;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Instant getExpireAt() {
		return expireAt;
	}

	public void setExpireAt(Instant expireAt) {
		this.expireAt = expireAt;
	}

	@Override
	public String toString() {
		return "TestSMSTrakcCountDTO [id=" + id + ", userId=" + userId + ", expireAt=" + expireAt + ", remainingSms="
				+ remainingSms + "]";
	}

}