package com.lqlsoftware.fuckchat.utils;


import com.lqlsoftware.fuckchat.utils.TokenModel;

/**
 * 对 token 进行操作的接口
 */
interface ITokenManager {
    /**
     * 创建一个 token 关联上指定用户
     * @param userId 指定用户的 id
     * @return 生成的 token
     */
    TokenModel createToken(String userId);
    /**
     * 检查 token 是否有效
     * @param model token
     * @return 是否有效
     */
    boolean checkToken(TokenModel model);
    /**
     * 从字符串中解析 token
     * @param authentication 加密后的字符串
     * @return TokenModel对象
     */
    TokenModel getToken(String authentication);
    /**
     * 清除 token
     * @param userId 登录用户的 id
     */
    void deleteToken(String userId);
}