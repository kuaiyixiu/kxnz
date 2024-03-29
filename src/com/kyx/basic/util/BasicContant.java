package com.kyx.basic.util;

import java.util.List;
import java.util.Map;
import com.kyx.basic.shops.model.Shops;

/**
 * 基础常量
 * 
 * @author 严大灯
 * @data 2019-4-12 上午9:54:52
 * @Descripton
 */
public class BasicContant {
  public static String initPwd = "111111";

  public static Integer SHOP_USER_COUNT = 2;

  // 车辆提醒里程数为：保养提醒里程-CAR_REMIND_MILEAGE
  public static final int CAR_REMIND_MILEAGE = 300;

  // 邀约服务展示天数
  public static final int INVITATION_SERVER_SHOW_DAY = 7;

  // 邀约车辆/交强险/车检/商业险展示天数
  public static final int INVITATION_CAR_SHOW_DAY = 30;

  // 保养展示天数
  public static final int MAINTAIN_CAR_SHOW_DAY = 14;

  // 未使用
  public static final int UN_USED = 0;

  // 已使用,已完成
  public static final int USED = 1;

  public static Map<String, List<Shops>> shopMap;

  public static String WECHAT_CONFIG_SESSION = "wechatConfigSession";
  public static String WECHAT_CUSTOMER_SESSION = "wechatCustomerSession";
  public static String CUSTOMER_SESSION = "customerSession";
  public static String MASTERWORKER_SESSION = "masterWorkerSession"; // 社区用户登录信息

  public static final String WXMPUSER_SESSION = "wxmpuser_session"; // 微信用户信息
  public static final String WXMP_APPID_SESSION = "wxmpuser_appid_session"; // 社区登录关联公总号appid

  public static final String WXMP_OPENID_SESSION = "openId"; // 社区登录用户授权的openId

  public static final String WXMP_PAYTYPE_JSAPI = "JSAPI"; // JSAPI

  public static final String WXMP_NOTIFY_URL = "http://lay.easy.echosite.cn/liantu/wechat/community/pay/notify.do"; // 回调地址

  public static final String WXMP_UNIFIEDORDER_URL ="https://api.mch.weixin.qq.com/pay/unifiedorder";

  public static final Integer ORDER_STATUS_ONE = 1;//订单取消

  public static final Integer ORDER_STATUS_TWO = 2;//订单未完成

  public static final Integer ORDER_STATUS_THREE = 3;//订单已完成

  public static String RESSUCCESS_XML = "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";

  public static String RESFAIL_XML = "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[报文为空]]></return_msg></xml>";
  /**
   * 角色表role_key字段: 微信社区注册用户角色记录
   */
  public static final String ROLE_KEY_WX_USER = "ROLE_WX_USER";

  public static String APPID = "appid";
  public static String OPENID = "openid";

  /**
   * 门店微信公众号信息
   */
  public static String SHOP_WECHAT_CONFIG_SESSION = "shopWechatConfigSession";
  /**
   * 平台数据库名称
   */
  public static final String PLAT_DBNAME = "ltplat";// ltplat

  // 管理员标示
  public static final String IS_ADMIN_FLAG = "admin";

  // 车辆邀约短信推送标题
  public static final String CAR_INVITATION_TITLE = "尊敬的客户您好，您的爱车";


  /**
   * 社区视频收费类型
   */
  public enum CarVideoMember {
    NORMAL(0), // 免费
    VIP   (1), // VIP
    FREE  (2); // 收费

    private Integer code;
    CarVideoMember(Integer code){
      this.code = code;
    }
    public Integer getCode() {
      return code;
    }
  }
  /**
   * 社区视频收费类型
   */
  public enum CommunityOrderType {
    VIP   (1), // VIP充值
    RENEW (2), // 续费
    VIDEO (3); // 付费视频充值

    private Integer code;
    CommunityOrderType(Integer code){
      this.code = code;
    }
    public Integer getCode() {
      return code;
    }
  }

  public interface shopsContant {
    String JDBC_DRIVER = "com.mysql.jdbc.Driver";

    String JDBC_URL_PREFIX = "jdbc:mysql://";

    String JDBC_URL_SUFFIX = "?useUnicode=true&characterEncoding=utf8";

    Integer WECHAT_ID = 1;

    Integer USER_COUNT = 0;

    String JDBC_USERNAME = "root";

    String JDBC_PASSWORD = "Kyx123!@#qwe+";
    // String JDBC_PASSWORD = "123456";

    // 门店状态运行中
    String RUNNING = "0";

    // 默认数据库开头
    String DB_NAME_PREFIX = "ltshop";
  }

  public interface userContant {

    String USING_STATUS = "1";

    Integer BOSS_ROLE_ID = 2;

    String ENABLE = "1";

    String POST = "老板";

    Integer BOSS_ACCOUNT = 1;
  }

  public interface payTypeContant {

    Integer WECHAT = 1; // 微信支付

    Integer ALIPAY = 2;// 支付宝

    Integer CASH = 3; // 现金

    Integer BALANCE = 4;// 余额

    Integer COUPON = 5; // 代金券

    Integer CARDSET = 6;// 优惠券

  }

  public interface WorkFlowContant {

    /**
     * 入库模版ID
     */
    public static final Integer RK_TEMPLATEID = 1;

    /**
     * 退货模版ID
     */
    public static final Integer TH_TEMPLATEID = 3;

  }

  public interface BoxContant {

    /**
     * 使用
     */
    public static final Integer USING = 0;

    /**
     * 禁用
     */
    public static final Integer DISABLED = -1;

    /**
     * 闲置
     */
    public static final Integer IDLE = 0;

    /**
     * 指令类型
     */
    public static final Integer OPEN_LOCK = 0;

  }

  public static final String CURRENT_DB_NAME = "currentDBName";

  public static final String WECHAT_APPID = "wx3c957523d27e8cb0";

  public static final String WECHAT_APPSECRET = "2c64390939e4575693f0b6c80a301bdb";

  // 夜间洗车服务默认class_id
  public static final Integer NIGHT_CLASS_ID = -1;

  // 接单数最大闲置
  public static final Integer IS_MAX_ORDER = 2;
}
