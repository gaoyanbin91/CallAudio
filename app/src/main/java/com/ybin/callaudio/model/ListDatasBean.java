package com.ybin.callaudio.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Yanbin on 2018/9/27.
 * 描述:
 */
public class ListDatasBean implements Serializable {

    /**
     * status : 0
     * msg :
     * code :
     * data : {"list":[{"amount":"131","id":1,"caseId":"11","visitingAddress":"河南省新乡市","name":"张三","bank":"http://115.60.255.11/static/bank.png","cardNumber":"2121"},{"amount":"331","id":2,"caseId":"22","visitingAddress":"河南省新乡市","name":"李四","bank":"http://115.60.255.11/static/bank.png","cardNumber":"1121"},{"amount":"221","id":3,"caseId":"22","visitingAddress":"河南省新乡市","name":"王五","bank":"http://115.60.255.11/static/bank.png","cardNumber":"1212"}]}
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
             * amount : 131
             * id : 1
             * caseId : 11
             * visitingAddress : 河南省新乡市
             * name : 张三
             * bank : http://115.60.255.11/static/bank.png
             * cardNumber : 2121
             */

            private String amount;
            private int id;
            private String caseId;
            private String visitingAddress;
            private String name;
            private String bank;
            private String cardNumber;

            public String getAmount() {
                return amount;
            }

            public void setAmount(String amount) {
                this.amount = amount;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getCaseId() {
                return caseId;
            }

            public void setCaseId(String caseId) {
                this.caseId = caseId;
            }

            public String getVisitingAddress() {
                return visitingAddress;
            }

            public void setVisitingAddress(String visitingAddress) {
                this.visitingAddress = visitingAddress;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getBank() {
                return bank;
            }

            public void setBank(String bank) {
                this.bank = bank;
            }

            public String getCardNumber() {
                return cardNumber;
            }

            public void setCardNumber(String cardNumber) {
                this.cardNumber = cardNumber;
            }
        }
    }
}
