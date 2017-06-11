package com.lqlsoftware.XPM.utils;
/**
 * Token 的 Model 类，可以增加字段提高安全性，例如时间戳、url 签名
 * @author ScienJus
 * @date 2015/7/31.
 */
public class TokenModel {
    // 用户 id
    private String userId;
    // 随机生成的 uuid
    private String token;
    // 连接id和token
    private String authentication;

    public TokenModel (String userId, String token) {
        this.userId = userId;
        this.token = token;
        this.authentication = userId + "_" + token;
    }

    public String getUserId () {
        return userId;
    }

    public void setUserId (String userId) {
        this.userId = userId;
    }

    public String getToken () {
        return token;
    }

    public void setToken (String token) {
        this.token = token;
    }

	public String getAuthentication() {
		return authentication;
	}
}