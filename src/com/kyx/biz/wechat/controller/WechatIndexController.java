package com.kyx.biz.wechat.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.kyx.basic.db.Dbs;
import com.kyx.basic.shops.model.Shops;
import com.kyx.basic.shops.service.ShopsService;
import com.kyx.basic.user.model.User;
import com.kyx.basic.user.service.UserService;
import com.kyx.basic.util.AppUtils;
import com.kyx.basic.util.BasicContant;
import com.kyx.basic.util.RetInfo;
import com.kyx.basic.util.WechatConfigUtils;
import com.kyx.biz.base.controller.BaseController;
import com.kyx.biz.custom.model.Custom;
import com.kyx.biz.custom.service.CustomService;
import com.kyx.biz.customType.model.CustomType;
import com.kyx.biz.customType.service.CustomTypeService;
import com.kyx.biz.pay.model.CommunityMeal;
import com.kyx.biz.pay.service.CommunityMealService;
import com.kyx.biz.wechat.config.MainConfiguration;
import com.kyx.biz.wechat.model.WechatCommunity;
import com.kyx.biz.wechat.model.WechatConfig;
import com.kyx.biz.wechat.model.WechatCustomer;
import com.kyx.biz.wechat.service.WechatCommunityService;
import com.kyx.biz.wechat.service.WechatConfigService;
import com.kyx.biz.wechat.service.WechatCustomerService;
import com.kyx.biz.wechat.vo.LableValue;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/wechat")
public class WechatIndexController extends BaseController {

  @Resource
  private WechatConfigService wechatConfigService;
  @Resource
  private WechatCustomerService wechatCustomerService;
  @Resource
  private WechatCommunityService wechatCommunityService;
  @Resource
  private ShopsService shopsService;
  @Resource
  private CustomService customService;
  @Resource
  private CustomTypeService customTypeService;

  // TODO(daibin): 以下注入要删除
  @Resource
  private UserService userService;

  @Resource
  private CommunityMealService communityMealService;

  @RequestMapping("/index")
  public String index(Model model, HttpServletRequest request, HttpSession session) {
    Custom custom = (Custom) session.getAttribute(BasicContant.CUSTOMER_SESSION);
    custom = customService.selectByPrimaryKey(custom.getId());
    session.setAttribute(BasicContant.CUSTOMER_SESSION, custom);
    model.addAttribute("custom", custom);
    CustomType customType = customTypeService.selectByPrimaryKey(custom.getCustType());
    model.addAttribute("customType", customType);

    com.kyx.basic.util.WechatConfig wechatConfig = WechatConfigUtils.getWechatChonfig();
    model.addAttribute("wechatConfig", JSONObject.toJSON(wechatConfig));
    Dbs.setDbName(Dbs.getMainDbName());
    Shops shops = shopsService.getById(custom.getShopId());
    request.getSession().setAttribute(Shops.SHOPS_SESSION, shops);

    return "wechatindex";
  }

  /**
   * 验证是否存在
   * 
   * @param custom
   * @return
   */
  @RequestMapping("/isHasCustom")
  @ResponseBody
  public String isHasCustom(Custom custom, HttpServletRequest request) {
    System.out.println("======开始绑定======");
    custom.setAppid(getAppId(request));
    custom.setOpenId(getOpenId(request));
    Dbs.setDbName(Dbs.getMainDbName());
    Shops shops = shopsService.getById(custom.getShopId());
    Dbs.setDbName(shops.getDbName());
    RetInfo ret = new RetInfo(customService.isHasCustom(custom, request), "查询会员");
    System.out.println(AppUtils.getReturnInfo(ret));
    return AppUtils.getReturnInfo(ret);
  }

  /**
   * 我的会员卡
   * 
   * @param model
   * @param session
   * @param state
   * @param code
   * @return
   * @throws Exception
   */
  @RequestMapping("/bindcustinfo")
  public String bindCustInfo(Model model, HttpSession session, String state, String code)
      throws Exception {
    System.out.println("initBindCustinfo start"+"---"+state+"+++"+code);
    Dbs.setDbName(Dbs.getMainDbName());
//    WechatConfig wechatConfig = null;
//    WechatConfig wechatCfig =
//        (WechatConfig) session.getAttribute(BasicContant.WECHAT_CONFIG_SESSION);
//      if(!(wechatCfig != null && wechatCfig.getAppid().equals(state))) {
 	   // 根据appid获取公众号信息
    WechatConfig  wechatConfig = wechatConfigService.getByAppId(state);
     session.setAttribute(BasicContant.WECHAT_CONFIG_SESSION, wechatConfig);
//    }
    session.setAttribute(BasicContant.APPID, state);
    System.out.println("oepnId start: " + (String) session.getAttribute(BasicContant.OPENID));
   // String openId = (String) session.getAttribute(BasicContant.OPENID);
    //System.out.println("openId is ok: " + openId);
//    if ( !(StringUtils.isNotEmpty(openId) && state.equals(wechatCfig.getAppid()))) {
//      System.out.println("openId is empty");
      MainConfiguration mainConfig =
          new MainConfiguration(wechatConfig.getAppid(), wechatConfig.getAppsecret(),
              wechatConfig.getToken(), wechatConfig.getEncodingaeskey());
      WxMpService wxMpService = mainConfig.wxMpService();
      WxMpOAuth2AccessToken wxMpOAuth2AccessToken = wxMpService.oauth2getAccessToken(code);
      String openId = wxMpOAuth2AccessToken.getOpenId();
      session.setAttribute(BasicContant.OPENID, openId);
      System.out.println("openId start"+"-------------------"+openId);
//    }
    // 根据appId和openId查询登录记录
//    WechatCustomer wechatCustomer =
//        (WechatCustomer) session.getAttribute(BasicContant.WECHAT_CUSTOMER_SESSION);
    /* System.out.println("wechatCustomer: " + wechatCustomer.toString()); */
//    if (!(wechatCustomer != null && state.equals(wechatCfig.getAppid()))) {
//      System.out.println("wechatCustomer IS empty");
      WechatCustomer  wechatCustomer = wechatCustomerService.getByAppidAndOpenId(state, openId);
//    }
//    wechatConfig = (WechatConfig) session.getAttribute(BasicContant.WECHAT_CONFIG_SESSION);
    Map<Integer, String> map = shopsService.getMapByWechatId(wechatConfig.getId());
    session.setAttribute(Shops.SHOP_LIST_SESSION, map);
    if (wechatCustomer == null) {// 之前没有绑定过会员或者登录过 跳转到绑定页面
      model.addAttribute("shops", JSON.toJSONString(shopsService.getShops(map)));
      return "wechatLogin";
    }
    String dbName = shopsService.getById(wechatCustomer.getShopId()).getDbName();
    Dbs.setDbName(dbName);
    session.setAttribute(BasicContant.CURRENT_DB_NAME, dbName);
    Custom custom = customService.getByCarNo(wechatCustomer.getCardNo());
    session.setAttribute(BasicContant.CUSTOMER_SESSION, custom);
    // 存在绑定信息的情况
    session.setAttribute(BasicContant.WECHAT_CUSTOMER_SESSION, wechatCustomer);

    return "redirect:/wechat/index.do";
  }

  /**
   * 预约服务
   * 
   * @param model
   * @param session
   * @param state
   * @param code
   * @return
   * @throws Exception
   */
  @RequestMapping("/serviceappointment")
  public String serviceAppointment(Model model, HttpServletRequest request, HttpSession session,
      String state, String code) throws Exception {
	  System.out.println("initserviceappointment start"+"---"+state+"+++++"+code);
       Dbs.setDbName(Dbs.getMainDbName());
 
    	// 根据appid获取公众号信息
    	WechatConfig   wechatConfig = wechatConfigService.getByAppId(state);
        session.setAttribute(BasicContant.WECHAT_CONFIG_SESSION, wechatConfig);

    
       session.setAttribute(BasicContant.APPID, state);
   // String openId = (String) session.getAttribute(BasicContant.OPENID);
      MainConfiguration mainConfig =
          new MainConfiguration(wechatConfig.getAppid(), wechatConfig.getAppsecret(),
              wechatConfig.getToken(), wechatConfig.getEncodingaeskey());
      WxMpService wxMpService = mainConfig.wxMpService();
      WxMpOAuth2AccessToken wxMpOAuth2AccessToken = wxMpService.oauth2getAccessToken(code);
      String   openId = wxMpOAuth2AccessToken.getOpenId();
      session.setAttribute(BasicContant.OPENID, openId);
      System.out.println("openId start"+"-------------------"+openId);
      
    // 根据appId和openId查询登录记录
//    WechatCustomer wechatCustomer =
//        (WechatCustomer) session.getAttribute(BasicContant.WECHAT_CUSTOMER_SESSION);
//    if (!(wechatCustomer != null && state.equals(wechatCfig.getAppid()))) {
      WechatCustomer  wechatCustomer = wechatCustomerService.getByAppidAndOpenId(state, openId);
//    }
   // wechatConfig = (WechatConfig) session.getAttribute(BasicContant.WECHAT_CONFIG_SESSION);
    Map<Integer, String> map = shopsService.getMapByWechatId(wechatConfig.getId());
    session.setAttribute(Shops.SHOP_LIST_SESSION, map);

    if (wechatCustomer == null ) {// 之前没有绑定过会员或者登录过 跳转到绑定页面
      model.addAttribute("shops", JSON.toJSONString(shopsService.getShops(map)));
      return "wechatLogin";
    }
    String dbName = shopsService.getById(wechatCustomer.getShopId()).getDbName();
    Dbs.setDbName(dbName);
    session.setAttribute("currentDBName", dbName);
    Custom custom = customService.getByCarNo(wechatCustomer.getCardNo());
    session.setAttribute(BasicContant.CUSTOMER_SESSION, custom);
    // 存在绑定信息的情况
    session.setAttribute(BasicContant.WECHAT_CUSTOMER_SESSION, wechatCustomer);

    return "redirect:/wechat/appoint/serviceappointment.do";
  }

  /**
   * 退出绑定
   * 
   * @param model
   * @param session
   * @param state
   * @param code
   * @return
   * @throws Exception
   */
  @RequestMapping("/exitBand")
  public String exitBand(Model model, HttpSession session, String state, String code)
      throws Exception {
    Dbs.setDbName(Dbs.getMainDbName());
    WechatConfig wechatConfig =
        (WechatConfig) session.getAttribute(BasicContant.WECHAT_CONFIG_SESSION);
    if (wechatConfig == null) {
      // 根据appid获取公众号信息
      wechatConfig = wechatConfigService.getByAppId(state);
      session.setAttribute(BasicContant.WECHAT_CONFIG_SESSION, wechatConfig);
    }
    session.setAttribute(BasicContant.APPID, state);
    String openId = (String) session.getAttribute(BasicContant.OPENID);
    if (StringUtils.isEmpty(openId)) {
      MainConfiguration mainConfig =
          new MainConfiguration(wechatConfig.getAppid(), wechatConfig.getAppsecret(),
              wechatConfig.getToken(), wechatConfig.getEncodingaeskey());
      WxMpService wxMpService = mainConfig.wxMpService();
      WxMpOAuth2AccessToken wxMpOAuth2AccessToken = wxMpService.oauth2getAccessToken(code);
      openId = wxMpOAuth2AccessToken.getOpenId();
      session.setAttribute(BasicContant.OPENID, openId);
    }
    // 根据appId和openId查询登录记录
    WechatCustomer wechatCustomer =
        (WechatCustomer) session.getAttribute(BasicContant.WECHAT_CUSTOMER_SESSION);
    if (wechatCustomer == null) {
      wechatCustomer = wechatCustomerService.getByAppidAndOpenId(state, openId);
    }
    Map<Integer, String> map = shopsService.getMapByWechatId(wechatConfig.getId());
    session.setAttribute(Shops.SHOP_LIST_SESSION, map);
    model.addAttribute("shops", JSON.toJSONString(shopsService.getShops(map)));
    return "wechatLogin";
  }

  @RequestMapping("/error")
  public String error() {
    return "error";
  }

  @RequestMapping("/login")
  public String login(Model model, HttpServletRequest request, HttpSession session, String state,
      String code) throws WxErrorException {
    Dbs.setDbName(Dbs.getMainDbName());
    WechatConfig wechatConfig =
        (WechatConfig) session.getAttribute(BasicContant.WECHAT_CONFIG_SESSION);
    if (wechatConfig == null) {
      // 根据appid获取公众号信息
      wechatConfig = wechatConfigService.getByAppId(state);
      session.setAttribute(BasicContant.WECHAT_CONFIG_SESSION, wechatConfig);
    }
    session.setAttribute(BasicContant.APPID, state);
    String openId = (String) session.getAttribute(BasicContant.OPENID);
    if (StringUtils.isEmpty(openId)) {
      MainConfiguration mainConfig =
          new MainConfiguration(wechatConfig.getAppid(), wechatConfig.getAppsecret(),
              wechatConfig.getToken(), wechatConfig.getEncodingaeskey());
      WxMpService wxMpService = mainConfig.wxMpService();
      WxMpOAuth2AccessToken wxMpOAuth2AccessToken = wxMpService.oauth2getAccessToken(code);
      openId = wxMpOAuth2AccessToken.getOpenId();
      session.setAttribute(BasicContant.OPENID, openId);
    }
    // 根据appId和openId查询登录记录
    WechatCustomer wechatCustomer =
        (WechatCustomer) session.getAttribute(BasicContant.WECHAT_CUSTOMER_SESSION);
    if (wechatCustomer == null) {
      wechatCustomer = wechatCustomerService.getByAppidAndOpenId(state, openId);
    }
    Map<Integer, String> map = shopsService.getMapByWechatId(wechatConfig.getId());
    session.setAttribute(Shops.SHOP_LIST_SESSION, map);
    if (wechatCustomer == null) {// 之前没有绑定过会员或者登录过 跳转到绑定页面
      model.addAttribute("shops", JSON.toJSONString(shopsService.getShops(map)));
      return "wechatLogin";
    }
    String dbName = shopsService.getById(wechatCustomer.getShopId()).getDbName();
    Dbs.setDbName(dbName);
    session.setAttribute("currentDBName", dbName);
    Custom custom = customService.getByCarNo(wechatCustomer.getCardNo());
    session.setAttribute(BasicContant.CUSTOMER_SESSION, custom);
    // 存在绑定信息的情况
    session.setAttribute(BasicContant.WECHAT_CUSTOMER_SESSION, wechatCustomer);

    return "redirect:/wechat/index.do";
  }

  /**
   * 以下为pc调试链接
   * 
   * @param model
   * @return
   */
  @RequestMapping("/wechatLogin")
  public String wechatLogin(Model model) {
    List<LableValue> list = Lists.newArrayList();
    LableValue l1 = new LableValue(1, "上海");
    LableValue l2 = new LableValue(2, "南京");
    list.add(l1);
    list.add(l2);
    model.addAttribute("shops", JSON.toJSONString(list));
    return "wechatLogin";
  }

  @RequestMapping("/indexs")
  public String indexs(Model model, HttpServletRequest request, HttpSession session) {
    Dbs.setDbName(Dbs.getMainDbName());
    String userName = "18715171217";
    User user = userService.querySingleUser(userName);
    Shops shops = shopsService.getById(user.getShopId());
    request.getSession().setAttribute(Shops.SHOPS_SESSION, shops);

    Map<Integer, String> map = shopsService.getShopsByBdName(shops.getDbName());
    request.getSession().setAttribute(Shops.SHOP_LIST_SESSION, map);
    Dbs.setDbName(shops.getDbName());
    session.setAttribute(BasicContant.CURRENT_DB_NAME, shops.getDbName());
    session.setAttribute(Shops.SHOPS_SESSION, shops);
    Custom custom = customService.selectByPrimaryKey(1);
    session.setAttribute(BasicContant.CUSTOMER_SESSION, custom);
    model.addAttribute("custom", custom);
    CustomType customType = customTypeService.selectByPrimaryKey(custom.getCustType());
    model.addAttribute("customType", customType);
    return "wechatindex";
  }


  /**
   * 社区登录页面
   *
   * @param model
   * @param request
   * @param session
   * @param state   公总号id
   * @param code    实时校验码
   * @return
   * @throws Exception
   */
  @RequestMapping("/community")
  public String community(HttpSession session, String state, String code) {

    Dbs.setDbName(Dbs.getMainDbName());
    String openId = "";
    session.setAttribute(BasicContant.WXMP_APPID_SESSION, state);
    WxMpUser wxMpUser = wechatConfigService.getWxMpUser(state, code);
//    state = "wx88aa9e73692893d1";
//    WxMpUser wxMpUser = wechatConfigService.getWxMpUserTest(state, "o9jvtwOM4OdZ655ca3U-r7kPk5ZA");
    if (wxMpUser != null) {
      System.out.println("community start" + "---openid: " + wxMpUser.getOpenId());
      session.setAttribute(BasicContant.WXMPUSER_SESSION, wxMpUser);
      openId = wxMpUser.getOpenId();
    }
    session.setAttribute(BasicContant.WXMP_OPENID_SESSION, openId);

    // 获取用户信息, 查询是否已绑定, 绑定后自动登录
    Object attribute = session.getAttribute(BasicContant.MASTERWORKER_SESSION);
    if (attribute == null) {
      WechatCommunity community = wechatCommunityService.getByAppidAndOpenId(state, openId);
      if (community == null) {
        return "community/login";
      } else {
        User user = userService.queryExistUserName(community.getUserName());
        if (user != null) {
          session.setAttribute(BasicContant.MASTERWORKER_SESSION, user);
        } else {
          return "community/login";
        }
      }
    }
    return "redirect:/wechat/community/index.do";
  }

    @RequestMapping("/wechatPay")
    public String wechatPay(Model model, HttpSession session) {
        try {
            //从session中获取用户信息
            User user = (User)session.getAttribute(BasicContant.MASTERWORKER_SESSION);
            //防止session中的数据不是最新的
            user  = userService.selectByPrimaryKey(user.getId());
            //判断用户是否为VIP
            String flag = null;
            if(null != user ){
                //判断当前用户是否VIP会员（日期是否为空并且失效日期是否超过当前日期 2:VIP会员 1：会员过期或者不是会员）
                if(null != user.getEndTime()){
                    String nowtime = AppUtils.date2String(new Date(), "yyyy-MM-dd HH:mm:ss");
                    String endtime =  AppUtils.date2String(user.getEndTime(), "yyyy-MM-dd HH:mm:ss");
                    if(endtime.compareTo(nowtime) > 0){

                        flag = AppUtils.date2String1(user.getEndTime());

                    }
                }
            }
            //查询套餐表
            List<CommunityMeal> communityMeals = communityMealService.queryComunityMeal();
            model.addAttribute("flag",flag);
            model.addAttribute("communityMeals", communityMeals);
            model.addAttribute("user",user);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return "wechat/recharge";
    }
}
