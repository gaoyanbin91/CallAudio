package com.ybin.callaudio.utils.okhttp;


import com.ybin.callaudio.utils.LogUtils;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by siberiawolf on 17/5/15.
 */

public class TokenInterceptor implements Interceptor {

    String TAG = TokenInterceptor.class.getName();
    String reAuthorizeCode;

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);


        LogUtils.info("response.code=" + response.code());

        if (isTokenExpired(response)) {//根据和服务端的约定判断token过期

            //同步请求方式，获取最新的Token
            //   String newSession = getNewToken();
            //使用新的Token，创建新的请求
            Request newRequest = chain.request()
                    .newBuilder()
                    .header("Cookie", "JSESSIONID=" + "")
                    .build();
            //重新请求
            return chain.proceed(newRequest);
        }

        return response;
    }

    /**
     * 根据Response，判断Token是否失效
     * @param response
     * @return
     */
    private boolean isTokenExpired(Response response) {
        if (response.code() == 401) {
            return true;
        }
        return false;
    }


    private synchronized void refreshNewToken() {
        try {

            LogUtils.info("====>refreshNewToken<=====");
//            long currentTime = System.currentTimeMillis();
//
//            if (currentTime - Global.getTokenExpire < Comm.expire)
//                return;
//            LogUtils.e("====>synchronized<=====", "synchronized->before");
//            synchronized (this) {
//                LogUtils.e("====>synchronized<=====", "synchronized->isNeedReauthorize");
            if (!isNeedReauthorize(reAuthorizeCode)) {
                return;
            }

            if ("401".equals(reAuthorizeCode))
                return;




            LogUtils.info("静默自动刷新Token,然后重新请求数据");
            HashMap<String, String> publickeyparams = new HashMap<>();

            HashMap<String, String> publickeyheaders = new HashMap<>();

            //  ParamsUtils.parseParams(MyApp.getContext(), Comm.getEncodeKeys, publickeyparams, publickeyheaders);





            HashMap<String, String> tokenparams = new HashMap<>();
            HashMap<String, String> tokenheaders = new HashMap<>();



//            LogUtils.e(" EncodeUtil.decrypt", "TokenId====EncodeUtil.decrypt(Global.aesKey, accessTokenIdModel.accessTokenId);");
//            LogUtils.e(" EncodeUtil.decrypt", "AesKey==="+Global.aesKey);
//            LogUtils.e(" EncodeUtil.decrypt", "Model.TokenId==="+accessTokenIdModel.accessTokenId);

//            Global.getTokenExpire = currentTime;
            reAuthorizeCode = "200";

//            }
//            LogUtils.e("====>synchronized<=====", "synchronized->after");

        } catch (Throwable e) {
            LogUtils.e(TAG, e);
        }
    }

    /**
     * 根据服务器返回的code，判断是否需要再次获取授权
     *
     * @param codeType
     * @return boolean
     */
    private boolean isNeedReauthorize(String codeType) {
        return ("4010".equals(codeType) || "4050".equals(codeType)) || "4060".equals(codeType) || "4020".equals(codeType);
    }

}
