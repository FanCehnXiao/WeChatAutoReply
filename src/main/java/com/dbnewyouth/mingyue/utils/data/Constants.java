package com.dbnewyouth.mingyue.utils.data;

public class Constants {

	/** 消息类型 **/
	public static final String MSG_TYPE = "xml";

	/** 微信公众平台配置的Token **/
	public static final String WECHAT_TOKEN = "mingyue3191";

    /** 发送消息的(open_id) **/
	public static final String FROM_USER_NAME_KEY ="FromUserName";
	
	/** 我方公众号 **/
	public static final String TO_USER_NAME_KEY="ToUserName";
	
	/** 创建消息时间 **/
	public static final String CREATE_TIME_KEY="CreateTime";
	
	/** 创建消息时间 **/
	public static final String CONTENT_KEY="Content";
	
	/** 当前消息类型 **/
    public static final String MSG_TYPE_KEY = "MsgType";
    public static final String MSG_TYPE_VALUE = "text";
	
	/** 当前语音格式已知的有amr MP3 **/
	public static final String FORMAT_KEY = "Format";
	
	/** 消息ID **/
	public static final String MSGID_KEY = "MsgId";
	
	/** CDK的类型 **/
	public static final String CDK_TYPE_GC_KEY = "GC";
	public static final String CDK_TYPE_JD_KEY = "JD";

	/** 领取CDK的关键词 **/
	public static final String CDK_TYPE_GC_VALUE = "公测CDK";
	public static final String CDK_TYPE_JD_VALUE = "经典CDK";
	
	/** 重复领取 **/
	public static final String CDK_MSG_RECEIVED = "您已经领取过CDK啦！";
	/** CDK前缀 **/
	public static final String CDK_MSG_RECEIVE = "感谢您关注Yy3191明月网游，CKD为：";
	/** CDK库存不足 **/
	public static final String CDK_MSG_NULL = "很抱歉，CDK没有啦，请去Yy3191联系管理吧！";
	/** 领取出现异常 **/
	public static final String CDK_MSG_OPENID = "领取出现异常，请去Yy3191联系管理吧！";

	public static final String DEFAULT_MSG = "success";
}
