package com.ybin.callaudio.model;

import java.io.Serializable;

/**
 * Created by Yanbin on 2018/9/27.
 * 描述:
 */
public class ReaultBean implements Serializable {

    /**
     * status : 0
     * msg :
     * code :
     * data : null
     */

    private int status;
    private String msg;
    private String code;
    private Object data;

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

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
