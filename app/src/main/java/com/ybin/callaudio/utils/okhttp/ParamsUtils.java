package com.ybin.callaudio.utils.okhttp;

import android.content.Context;

import com.ybin.callaudio.utils.LogUtils;

import java.net.URLEncoder;
import java.util.HashMap;

/**
 * Created by siberiawolf on 17/5/15.
 */

public class ParamsUtils {

    private static final String TAG = ParamsUtils.class.getName();

    public static void parseParams(Context context, String url, HashMap<String, String> apiParams, HashMap<String, String> headerParams) {
        try {


            String aseKey = EncodeUtil.initKey();


//            apiParams.put("mainland", "1");
//            apiParams.put("regionCode", "156");
//            apiParams.put("deviceId", Global.IMIE);
//            apiParams.put("version", Global.VERSION);

            String timestamp = String.valueOf(System.currentTimeMillis());

//            LogUtils.info("cid=>cid:" + Global.openUDID);

        } catch (Throwable e) {

        }
    }

    private static String toURLEncoded(String paramString) {
        if (paramString == null)
            return "";

        if (paramString.equals("")) {
            return paramString;
        }
        try {
            return URLEncoder.encode(new String(paramString.getBytes(), "UTF-8"), "UTF-8");

        } catch (Exception localException) {
            LogUtils.e("toURLEncoded error:" + paramString, localException);
        }
        return null;
    }

    //无需加密操作的白名单
    private static final String FLGS[] = {"h5",//H5页面
            "/server/mobilemark/save.do",//保存idfa
            "/regionService/check.do",//获取用户的地域和mainland值
            "/server/domob/collectUserLoginInfo.do",//第一次启动app保存cid和其他相关的信息
            "/server/opinion/saveopinion.do",//用户反馈（用给黑名单上的用户申请恢复账号）
//            "/server/video/catalog/videoDetails.do",//视频播放
            "/server/recipe/detailVideo.do",//视频播放
            "/server/utils/upload.do",//oss上传图片
            "/server/screening/parentList.do",//搜索筛选
            "/server/utils/getAccess.do",//OSS验证接口
            "/server/video/catalog/videoShare.do",//视频分享
            "/server/activity/share.do",//分享
            "/server/recipeSeries/relH5ContentList.do",//原生主题
            "auth/authKey.do",
            "auth/key.do"
//            "recommend/recommendationSeries.do"
    };

    private static boolean isNeedEncode(String url) {
        boolean flag = true;
        for (String s : FLGS) {
            if (url.contains(s)) {
                flag = false;
                LogUtils.e("whiteList", "url->" + url + "\n" + "s->" + s + "\n" + "flag->" + flag);
            }
        }
        return flag;
    }


}
