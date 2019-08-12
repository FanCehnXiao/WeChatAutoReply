### 写在前面

最近跟朋友搞了个网游公会，主体运作是《御龙在天》这款游戏，因为公会做了微信公众号，所以提出这个需求：用户在公众号发送关键字，然后领取CDK，每个用于不可重复领取。多方资料查阅，发现这方面的资料很少，所以就独立的自己研究开发了一下，不存在任何商业利益，纯粹个人的爱好和贡献。

### 注册订阅号

前往微信公众平台注册，这里就不多说了。点击下面机票，go！

[微信公众平台](https://mp.weixin.qq.com/)

[微信公众号开发文档](https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1445241432)

### 开发前准备

服务器：阿里云或者华为云都可以，配置不需要很高<br />
域名：已备案的域名，或者你的服务器没有其它服务，80端口给即将开发的程序用<br />
Nginx：这个东西没什么好说的。
MySQL：数据库，用来存储CDK信息。

### 架构设计

涉及技术：Spring Boot、Spring Mvc、MyBatis、Druid、MySQL、Lombok、Dom4j、Xstream <br />
Spring Boot、Spring Mvc、MyBatis不需要多说，经典的ssm架构；Druid是阿里爸爸的数据库连接池；Lombok是用来简化开发的框架，比如快速省略日志，无需生成Get/Set方法；Dom4j主要用来解析Xml消息；Xstream用来将消息转换为Xml；

### 新建项目，创建数据表

初始化一个Spring Boot项目就不在这提了，不会的点击下面机票，查看另外一篇文章；

[初始化一个Spring Boot项目](http://zhuxinfeng.com/2019/03/03/java-shi-yong-md5-jia-mi-gong-ju-lei-1/)

创建数据表：
```
CREATE TABLE `cdk_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `cdk` varchar(50) NOT NULL DEFAULT '' COMMENT 'CDK',
  `type` varchar(50) DEFAULT '' COMMENT 'CDK类型',
  `open_id` varchar(50) DEFAULT '' COMMENT '微信的openid',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `modify_time` timestamp NULL DEFAULT NULL COMMENT '修改时间',
  `dr` int(10) DEFAULT '1' COMMENT '删除标识: [1 有效; 0 删除;]',
  PRIMARY KEY (`id`,`cdk`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COMMENT='CDK信息表';
```

Maven依赖：
```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter</artifactId>
</dependency>

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>

<dependency>
    <groupId>org.mybatis</groupId>
    <artifactId>mybatis</artifactId>
    <version>3.4.6</version>
</dependency>
<dependency>
    <groupId>org.mybatis</groupId>
    <artifactId>mybatis-spring</artifactId>
    <version>1.3.2</version>
</dependency>

<dependency>
    <groupId>org.mybatis.spring.boot</groupId>
    <artifactId>mybatis-spring-boot-starter</artifactId>
    <version>1.3.2</version>
</dependency>

<!-- alibaba的druid数据库连接池 -->
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>druid-spring-boot-starter</artifactId>
    <version>1.1.9</version>
</dependency>

<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>5.1.46</version>
</dependency>

<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <version>1.18.6</version>
    <scope>provided</scope>
</dependency>

<!-- 解析xml -->
<dependency>
    <groupId>dom4j</groupId>
    <artifactId>dom4j</artifactId>
    <version>1.6</version>
</dependency>

<!-- 转xml -->
<dependency>
    <groupId>com.thoughtworks.xstream</groupId>
    <artifactId>xstream</artifactId>
    <version>1.4</version>
</dependency>
```

### 创建微信消息实体类

```
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
```

### 创建XML与JavaBean转换工具类

```
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
```

### 开发服务器基本配置

登录微信公众平台后，基本配置 -> 服务器配置 -> 修改配置

URL：配置一个api接口，用来接收服务器推送的消息，url自定义，一会项目中用的接口
> 例如：http://wx.dbnewyouth.com/api/v1/cdk/msg

Token：Token不是校验用的access_token，是单独校验服务器的token，自定义即可，与服务器配置相同

EncodingAESKey：随机生成即可，因为是个人开发使用，所以消息加解密是明文，这个用处不大。

消息加解密方式：选择明文模式，个人开发完，就不去解密了。

![服务器配置](http://img.dbnewyouth.cn/20190812231039.png)

### 接收公众号推送的消息

> 主要思路：接收公众号的消息推送 -> 解析`xml`消息体，识别消息类型 -> 处理`text`文本消息，其它消息直接返回`success` -> 获取`xml`消息体中的`content`内容，与设定的关键词进行对比,符合进行下一步，不符合直接返回`success` ->  获取`FromUserName`，其实就是`openid`，查询数据库中对应类型的cdk是否领取过，领取过返回消息，没领取过返回cdk消息 -> 封装`xml`消息体，返回。

> PS：需要注意，在返回`xml`消息体的时候，要将接收的消息体`FromUserName`与`ToUserName`相反设置，即将接收方设置为用户，发送方设置为开发者。

```
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
```

实现类：

```
@Override
public String getCdk(String openId,String cdkType){
    // 参数校验
    if(StringUtil.isBlank(openId)){
        return Constants.CDK_MSG_OPENID;
    }
    // 查询是否领取过
    CdkInfoBean params = new CdkInfoBean();
    params.setOpenId(openId);
    params.setType(cdkType);
    CdkInfoBean bean = cdkInfoMapper.queryByOpenId(params);
    // 如果是空 则该用户领取过CDK
    if(bean != null){
        return Constants.CDK_MSG_RECEIVED;
    }
    // 获取一个新的CDK，如果拿不到，说明没有cdk了。
    CdkInfoBean cdk = cdkInfoMapper.queryByAsc(cdkType);
    if(cdk == null){
        return Constants.CDK_MSG_NULL;
    }
    cdk.setOpenId(openId);
    // 更新为使用状态
    cdkInfoMapper.update(cdk);
    // 返回cdk信息
    return Constants.CDK_MSG_RECEIVE + cdk.getCdk();
}
```

常量类：

```
public class Constants {

	/** 消息类型 **/
	public static final String MSG_TYPE = "xml";

	/** 微信公众平台配置的Token **/
	public static final String WECHAT_TOKEN = "xxxxxxxx";

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

    /** 默认回复消息，如果不是你想回复的消息，默认回复success，防止公众号5s重试 **/
	public static final String DEFAULT_MSG = "success";
}
```

### 实际效果测试

![效果测试](http://img.dbnewyouth.cn/20190812232953.jpg)