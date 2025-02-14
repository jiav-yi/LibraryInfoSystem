package com.jnu.constants;

/**
 * 枚举、常量定义
 */
public class ConfigConstants {
    /**
     * 响应码相关
     */
    // 成功响应码
    public static final int SUCCESS = 20000;
    // 失败响应码
    public static final int FAIL = 20001;

    /**
     * 文字识别相关
     */
    // 使用百度OCR服务时的Token
    public static final String OCR_TOKEN = "24.73e9c81fa2fb1a4dc3ab5a20b2306349.2592000.1741601729.282335-117382515";
    // 调用百度OCR服务的api
    public static final String OCR_URL = "https://aip.baidubce.com/rest/2.0/ocr/v1/general_basic";

    /**
     * 大模型相关
     */
    public static final String HOST_URL = "https://spark-api.xf-yun.com/v1.1/chat";
    public static final String DOMAIN = "lite";
    public static final String APP_ID = "6870d580";
    public static final String API_SECRET = "MThlMzYxMTM0Njg5ZTM5OTU0NWU2NmUy";
    public static final String API_KEY = "cca95262288af7daa8e3278a4dc64a78";
    // 用于获得图书信息的问题
    public static final String SCAN_BOOK_QUESTION = "请根据此信息,给出图书标题、作者、ISBN号、简介，并以这样的{ title: 'Book Name', author: 'Author Name', isbn: 'ISBN Number', introduction: 'Book Introduction'}json串回答";
}
