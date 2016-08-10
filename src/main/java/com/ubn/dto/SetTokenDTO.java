package com.ubn.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SetTokenDTO {

	private String token;

	private String userId;

	@JsonProperty("Token")
	public String getToken() {
		return this.token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@JsonProperty("UserID")
	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String toString() {
		return "SetTokenDTO [token=" + this.token + ", userId=" + this.userId + "]";
	}

}
