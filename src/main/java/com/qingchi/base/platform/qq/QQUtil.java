package com.qingchi.base.platform.qq;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qingchi.base.config.ResultException;
import com.qingchi.base.constant.PushMsgErrCode;
import com.qingchi.base.constant.WxErrCode;
import com.qingchi.base.model.notify.NotifyDO;
import com.qingchi.base.platform.PushMessageUtils;
import com.qingchi.base.platform.PushMsgDTO;
import com.qingchi.base.platform.TokenDTO;
import com.qingchi.base.platform.weixin.HttpResult;
import com.qingchi.base.utils.QingLogger;
import com.qingchi.base.utils.TokenUtils;
import com.thoughtworks.xstream.XStream;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;

/**
 * @author qinkaiyuan
 * @date 2019-10-24 13:30
 */
@Component
public class QQUtil {
    private static RestTemplate restTemplate;
    private static ObjectMapper objectMapper;
    private static String tokenUrl;

    @Value("${config.tokenUrl}")
    public void setTokenUrl(String tokenUrl) {
        QQUtil.tokenUrl = tokenUrl;
    }

    @Resource
    public void setRestTemplate(RestTemplate restTemplate) {
        QQUtil.restTemplate = restTemplate;
    }


    @Resource
    public void setObjectMapper(ObjectMapper objectMapper) {
        QQUtil.objectMapper = objectMapper;
    }

    /**
     * 获取微信token
     *
     * @return
     */
    public static String getAccessToken() {
        ResponseEntity<TokenDTO> responseEntity = restTemplate.getForEntity(tokenUrl + "getQQSession", TokenDTO.class);
        return Objects.requireNonNull(responseEntity.getBody()).getAccessToken();
    }

    public static String refreshAccessToken() {
        ResponseEntity<TokenDTO> responseEntity = restTemplate.getForEntity(tokenUrl + "refreshQQSession", TokenDTO.class);
        return Objects.requireNonNull(responseEntity.getBody()).getAccessToken();
    }

    private static HttpResult checkContentQQSecPost(String content) {
        HashMap<String, Object> postData = new HashMap<>();
        postData.put("content", content);
        String url = QQConst.qq_msg_sec_check_url + QQUtil.getAccessToken();
        return restTemplate.postForEntity(url, postData, HttpResult.class).getBody();
    }

    /**
     * 校验内容是否违规
     *
     * @param content
     */
    public static HttpResult checkContentQQSec(String content) {
        HttpResult qqResult = checkContentQQSecPost(content);
        assert qqResult != null;
        if (qqResult.hasError()) {
            if (WxErrCode.token_invalid.equals(qqResult.getErrcode())) {
                QQUtil.refreshAccessToken();
                qqResult = checkContentQQSecPost(content);
            }
        }
        return qqResult;
    }


    public static void qqPushMsgCommon(String openId, String platform, PushMsgDTO pushMsgDTO, NotifyDO notify) {
        //如果评论
        //相同
        String accessToken = QQUtil.getAccessToken();

        pushMsgDTO.setAccess_token(accessToken);
        pushMsgDTO.setTouser(openId);

//         = new PushMsgDTO(accessToken, openId, template_id, page, data, emphasis_keyword)
        String url = QQConst.push_msg_url + accessToken;
        HttpResult result = restTemplate.postForEntity(url, pushMsgDTO, HttpResult.class).getBody();
        PushMessageUtils.savePushMsg(notify, pushMsgDTO, result, platform);
        if (result != null && result.hasError()) {
            Integer errCode = result.getErrcode();
            if (PushMsgErrCode.token_expired.equals(errCode)) {
                //刷新token后重试
                accessToken = QQUtil.refreshAccessToken();
                pushMsgDTO.setAccess_token(accessToken);
                url = QQConst.push_msg_url + accessToken;
                result = restTemplate.postForEntity(url, pushMsgDTO, HttpResult.class).getBody();
                PushMessageUtils.savePushMsg(notify, pushMsgDTO, result, platform);
            }
        }
    }

    private static String qq_mp_id;
    private static String qq_merchant_id;
    private static String qq_merchant_key;

    @Value("${config.qq.mp.qq_mp_id}")
    public void setQq_mp_id(String qq_mp_id) {
        QQUtil.qq_mp_id = qq_mp_id;
    }

    @Value("${config.qq.merchant.qq_merchant_id}")
    public void setQq_merchant_id(String qq_merchant_id) {
        QQUtil.qq_merchant_id = qq_merchant_id;
    }

    @Value("${config.qq.merchant.qq_merchant_key}")
    public void setQq_merchant_key(String qq_merchant_key) {
        QQUtil.qq_merchant_key = qq_merchant_key;
    }

    //发起支付
    public static String postPayUrl(String deviceIp, String orderNo, String total_feeStr) throws IOException {
        Map<String, String> map = new HashMap<>();
        map.put("appid", qq_mp_id);
        String attachStr = "qingchiapp";
        map.put("attach", attachStr);
        String bodystr = "qingchiapp";
        map.put("body", bodystr);

        map.put("fee_type", "CNY");
        map.put("mch_id", qq_merchant_id);
        String nonce_strstr = TokenUtils.getUUID();
        map.put("nonce_str", nonce_strstr);
        map.put("notify_url", QQConst.qq_pay_result_notify_url);
        map.put("out_trade_no", orderNo);
        map.put("spbill_create_ip", deviceIp);
        map.put("total_fee", total_feeStr);
        map.put("trade_type", "MINIAPP");
        HttpHeaders requestHeader = new HttpHeaders();
        requestHeader.setContentType(MediaType.APPLICATION_XML);
        StringBuilder xmlString = new StringBuilder();
        String appid = "<appid>" + qq_mp_id + "</appid>";
        String attach = "<attach>" + attachStr + "</attach>";
        String body = "<body>" + bodystr + "</body>";
        String mch_id = "<mch_id>" + qq_merchant_id + "</mch_id>";
        String nonce_str = "<nonce_str>" + nonce_strstr + "</nonce_str>";

        String sign = getSignToken(map);
        String signXmlStr = "<sign>" + sign + "</sign>";

        String notify = "<notify_url>" + QQConst.qq_pay_result_notify_url + "</notify_url>";
        String out_trade_no = "<out_trade_no>" + orderNo + "</out_trade_no>";
        String spbill_create_ip = "<spbill_create_ip>" + deviceIp + "</spbill_create_ip>";
        String total_fee = "<total_fee>" + total_feeStr + "</total_fee>";
        xmlString.append("<xml>")
                .append(appid)
                .append(attach)
                .append(body)
                .append("<fee_type>CNY</fee_type>")
                .append(mch_id)
                .append(nonce_str)
                .append(notify)
                .append(out_trade_no)
                .append(spbill_create_ip)
                .append(total_fee)
                .append("<trade_type>MINIAPP</trade_type>")
                .append(signXmlStr)
                .append("</xml>");
        // 创建 HttpEntity
        HttpEntity<String> requestEntity = new HttpEntity<>(xmlString.toString(), requestHeader);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(QQConst.qq_pay_url, requestEntity, String.class);
        XStream xstream = new XStream();
        xstream.alias("xml", QQPayResult.class);
        Object qqPayResult = xstream.fromXML(responseEntity.getBody());
        String result = objectMapper.writeValueAsString(qqPayResult);
        QingLogger.logger.info(result);
        QQPayResult result1 = objectMapper.readValue(result, QQPayResult.class);
        if (result1.hasError()) {
            throw new ResultException("支付失败");
        }
        return result1.getPrepay_id();
    }


    /**
     * Description:MD5工具生成token
     *
     * @param value
     * @return
     */
    public String getMD5Value(String value) {
        try {
            /*MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] md5ValueByteArray = messageDigest.digest(value.getBytes());
            BigInteger bigInteger = new BigInteger(1 , md5ValueByteArray);
            return bigInteger.toString(16).toUpperCase();*/
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 生成签名
     *
     * @param map
     * @return
     */
    public static String getSignToken(Map<String, String> map) {
        String result;
        List<Map.Entry<String, String>> infoIds = new ArrayList<>(map.entrySet());
        // 对所有传入参数按照字段名的 ASCII 码从小到大排序（字典序）
        infoIds.sort(Map.Entry.comparingByKey());
        // 构造签名键值对的格式
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> item : infoIds) {
            String itemKey = item.getKey();
            String itemVal = item.getValue();
            if (StringUtils.isNotEmpty(itemKey) && StringUtils.isNotEmpty(itemVal)) {
                sb.append(itemKey).append("=").append(itemVal).append("&");
            }
        }
        //key为密钥
        result = sb.toString() + "key=" + qq_merchant_key;
        //进行MD5加密
        result = DigestUtils.md5Hex(result).toUpperCase();
        return result;
    }
}
