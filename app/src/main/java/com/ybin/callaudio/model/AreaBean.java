package com.ybin.callaudio.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Yanbin on 2018/9/27.
 * 描述:
 */
public class AreaBean implements Serializable {

    /**
     * status : 0
     * msg :
     * code :
     * data : {"list":[{"region":"郑州","id":1,"createTime":1537948282000,"note":null},{"region":"洛阳","id":2,"createTime":1537948283000,"note":null},{"region":"北京","id":3,"createTime":1537948283000,"note":null}]}
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
        private List<ListBean> list;

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            /**
             * region : 郑州
             * id : 1
             * createTime : 1537948282000
             * note : null
             */

            private String region;
            private int id;
            private long createTime;
            private Object note;

            public String getRegion() {
                return region;
            }

            public void setRegion(String region) {
                this.region = region;
            }

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

            public Object getNote() {
                return note;
            }

            public void setNote(Object note) {
                this.note = note;
            }
        }
    }
}
