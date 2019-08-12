package com.dbnewyouth.mingyue.utils;

import com.dbnewyouth.mingyue.utils.data.Constants;
import com.dbnewyouth.mingyue.wechat.model.WeChatMessageBean;
import com.thoughtworks.xstream.XStream;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XmlConverterUtil implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 微信公众号使用：XML转换为Map
     * 使用dom4j进行xml读取
     *
     * @param request HTTP请求
     * @return 返回map
     * @date 2019/8/12 11:14
     */
    public static Map<String, String> xmlToMap(HttpServletRequest request) throws Exception {
        Map<String, String> map = new HashMap<>();
        SAXReader reader = new SAXReader();
        ServletInputStream inputStream = request.getInputStream();
        Document document = reader.read(inputStream);
        // 获取跟节点
        Element rootElement = document.getRootElement();
        // 获取根下所有节点
        List<Element> elements = rootElement.elements();
        for (Element element : elements) {
            map.put(element.getName(), element.getText());
        }
        // 关闭流
        inputStream.close();
        return map;
    }

    /**
     * 微信公众号使用：对象转换为XML
     *
     * @param weChatMessage 消息体
     * @return 返回字符串
     * @date 2019/8/12 11:18
     */
    public static String objectToXml(WeChatMessageBean weChatMessage){
        XStream xStream = new XStream();
        xStream.alias(Constants.MSG_TYPE,weChatMessage.getClass());
        return xStream.toXML(weChatMessage);
    }
}
