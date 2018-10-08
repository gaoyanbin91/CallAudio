package com.ybin.callaudio.model;

import java.io.Serializable;

/**
 * Created by Yanbin on 2018/9/26.
 * 描述:
 */
public class CompanyBean implements Serializable {

    /**
     * status : 0
     * msg :
     * code :
     * data : {"id":1,"createTime":1537945885000,"logo":"http://115.60.255.11/static/zram_logo.jpg","watermark":"中融国信","title":"中融国信","note":null}
     */

    private int status;
    private String msg;
    private String code;
    private DataBean data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 1
         * createTime : 1537945885000
         * logo : http://115.60.255.11/static/zram_logo.jpg
         * watermark : 中融国信
         * title : 中融国信
         * note : null
         */

        private int id;
        private long createTime;
        private String logo;
        private String watermark;
        private String title;
        private Object note;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public String getWatermark() {
            return watermark;
        }

        public void setWatermark(String watermark) {
            this.watermark = watermark;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Object getNote() {
            return note;
        }

        public void setNote(Object note) {
            this.note = note;
        }
    }
}
