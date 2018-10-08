package com.ybin.callaudio.model;

import java.io.Serializable;

/**
 * Created by Yanbin on 2018/9/28.
 * 描述:
 */
public class FileReaultBean implements Serializable {

    /**
     * status : 0
     * msg :
     * code :
     * data : {"id":"5"}
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
         * id : 5
         */

        private String id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}
