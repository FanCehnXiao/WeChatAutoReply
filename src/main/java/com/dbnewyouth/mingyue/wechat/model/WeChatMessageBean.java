package com.dbnewyouth.mingyue.wechat.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 微信公众号消息实体
 * @date 2019/8/12 11:23
 */
@Getter
@Setter
@ToString
public class WeChatMessageBean implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 开发者微信号 **/
    private String ToUserName;
    /** 发送方帐号（一个OpenID） **/
    private String FromUserName;
    /** 消息创建时间 （整型） **/
    private Long CreateTime;
    /** 消息类型，文本为text **/
    private String MsgType;
    /** 文本消息内容 **/
    private String Content;
    /** 消息id，64位整型 **/
    private String MsgId;
}
