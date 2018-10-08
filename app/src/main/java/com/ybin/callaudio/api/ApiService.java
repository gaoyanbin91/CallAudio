package com.ybin.callaudio.api;

/**
 * Created by gaoyanbin on 2018/3/26.
 * 描述:
 */

public class ApiService {


    public static final String base = "115.60.255.11/zram/";
    //        public static final String base = "61.163.81.139:8080/";
    public static final String BASE_URL = "http://" + base;//外网

    //   public static final String BASE_URL = "http://192.168.1.124:8080/";//黄妞妞

    public static final String QUERY_COMPANY_MES = BASE_URL + "zram/list";//查询公司信息版本
    public static final String QUERY_AREA_LISTS = BASE_URL + "wfjlDq/list";//查询 地区列表数据
    public static final String QUERY_LISTS_MESAGES = BASE_URL + "wfjl/list";//查询  数据列表
    public static final String SAVE_STATUS =  BASE_URL +"wfjl/updateWfjl";// 修改 状态
    public static final String QUERY_DETAIL =  BASE_URL +"wfjl/detail";// 查看详情
    public static final String UPLOAD_PIC = BASE_URL + "uploadPicture/addUploadPicture";//上传图片
    public static final String UPLOAD_MP3_SHISHI = BASE_URL + "uploadSound/addUploadRealSound";//实时上传
    public static final String UPLOAD_AUDIO = BASE_URL + "uploadSound/addUploadSound";//录音上传
    public static final String UPLOAD_DATA = BASE_URL + "uploadData/addUploadData";//上chua数据

}
