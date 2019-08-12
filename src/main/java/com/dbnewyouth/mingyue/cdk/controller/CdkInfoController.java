package com.dbnewyouth.mingyue.cdk.controller;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dbnewyouth.mingyue.cdk.service.ICdkInfoService;
import com.dbnewyouth.mingyue.utils.StringUtil;
import com.dbnewyouth.mingyue.utils.WeixinUtil;
import com.dbnewyouth.mingyue.utils.XmlConverterUtil;
import com.dbnewyouth.mingyue.utils.data.Constants;
import com.dbnewyouth.mingyue.wechat.model.WeChatMessageBean;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * CDK信息表
 * 
 * @author jfastgen
 */
@RestController
@RequestMapping("/api/v1/cdk")
public class CdkInfoController implements Serializable {

    private static final long serialVersionUID = 1L;
    private static Logger logger = LoggerFactory.getLogger(CdkInfoController.class);

    @Autowired
    private ICdkInfoService cdkInfoService;

    @RequestMapping("/test")
    public String test() {
        String cdk = cdkInfoService.getCdk("123456", "JD");
        return cdk;
    }

    /**
     * 获取cdk
     *
     * @param signature 签名
     * @param timestamp 时间戳
     * @param nonce     随机数
     * @param echostr   密文
     * @param request   微信公众平台的Http请求
     * @return 返回xml数据或接收成功（success）
     * @date 2019/8/12 11:26
     */
    @RequestMapping("/msg")
    public String msg(@Param("signature") String signature, @Param("timestamp") String timestamp,
            @Param("nonce") String nonce, @Param(value = "echostr") String echostr, HttpServletRequest request) {
        // 校验签名和token
        if (StringUtil.isNotEmpty(echostr)) {
            logger.info("正在校验签名等参数");
            if (WeixinUtil.checkSignature(signature, timestamp, nonce)) {
                return echostr;
            }
            return "";
        } else {
            try {
                // 将request中的xml信息转换为map
                Map<String, String> map = XmlConverterUtil.xmlToMap(request);
                // 消息类型
                String msgType = map.get(Constants.MSG_TYPE_KEY);
                // 发送方账号openid
                String fromUserName = map.get(Constants.FROM_USER_NAME_KEY);
                // 开发者微信号
                String toUserName = map.get(Constants.TO_USER_NAME_KEY);
                // 用户发送的消息
                String content = map.get(Constants.CONTENT_KEY);
                // 标记数值
                String msgId = map.get(Constants.MSGID_KEY);
                logger.info("消息类型为：{} ，消息内容为：{}", msgType, content);
                // 如果是文本消息则进行如下处理
                if (msgType.equalsIgnoreCase(Constants.MSG_TYPE_VALUE)) {
                    // 如果关键词不是“公测CDK”或者“经典CDK”，直接返回success
                    if (!Constants.CDK_TYPE_GC_VALUE.equalsIgnoreCase(content) && !Constants.CDK_TYPE_JD_VALUE.equalsIgnoreCase(content)) {
                        return Constants.DEFAULT_MSG;
                    }
                    // 通过content校验出cdk类型，GC或者JD
                    String cdkType = checkCdkType(content);
                    // 获取cdk
                    String cdk = cdkInfoService.getCdk(fromUserName, cdkType);
                    // 返回信息给用户
                    WeChatMessageBean weChatMessage = new WeChatMessageBean();
                    weChatMessage.setContent(cdk);
                    weChatMessage.setMsgType(msgType);
                    weChatMessage.setToUserName(fromUserName);
                    weChatMessage.setFromUserName(toUserName);
                    weChatMessage.setCreateTime(System.currentTimeMillis());
                    weChatMessage.setMsgId(msgId);
                    // 将消息实体转换为xml
                    String xml = XmlConverterUtil.objectToXml(weChatMessage);
                    logger.info("消息发送成功，时间：{}", getTimeStr());
                    return xml;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            logger.info("消息发送成功，时间：{}", getTimeStr());
            return Constants.DEFAULT_MSG;
        }
    }


    /**
     * 校验关键字获取的CDK类型
     *
     * @param content 消息内容
     * @return 返回CDK类型 GC或JD
     * @date 2019/8/12 11:26
     */
    private String checkCdkType(String content) {
        String cdkType;
        if (Constants.CDK_TYPE_GC_VALUE.equalsIgnoreCase(content)) {
            cdkType = Constants.CDK_TYPE_GC_KEY;
        } else {
            cdkType = Constants.CDK_TYPE_JD_KEY;
        }
        return cdkType;
    }

    /**
     * 获取指定格式的当前时间
     * 格式：yyyy-MM-dd HH:mm:ss
     *
     * @return 返回当前时间
     * @date 2019/8/12 11:26
     */
    private String getTimeStr() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }
}