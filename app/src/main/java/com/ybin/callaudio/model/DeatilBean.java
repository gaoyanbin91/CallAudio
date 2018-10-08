package com.ybin.callaudio.model;

import java.io.Serializable;

/**
 * Created by Yanbin on 2018/9/27.
 * 描述:
 */
public class DeatilBean implements Serializable {

    /**
     * code :
     * data : {"product":{"IDNumber":"12345678978","address":"河南省新乡市","amount":"131","bank":"http://115.60.255.11/static/bank.png","batch":"01AA","billingAddress":"河南省新乡市","cardNumber":"2121","caseId":"11","closeDate":"2018-08-07 14:36","company":"河南测试有限公司","contactName":"历史","contactPhone":"12345678912","createTime":1538030349000,"homePhone":"12345678912","household":"河南省新乡市","id":1,"kinName":"历史","kinshipPhone":"12334567891","name":"张三","region":"洛阳","state":"2","unitAddress":"河南省新乡市","visitingAddress":"河南省新乡市","workTelephone":"12345678911"}}
     * msg :
     * status : 0
     */

    private String code;
    private DataBean data;
    private String msg;
    private int status;

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

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public static class DataBean {
        /**
         * product : {"IDNumber":"12345678978","address":"河南省新乡市","amount":"131","bank":"http://115.60.255.11/static/bank.png","batch":"01AA","billingAddress":"河南省新乡市","cardNumber":"2121","caseId":"11","closeDate":"2018-08-07 14:36","company":"河南测试有限公司","contactName":"历史","contactPhone":"12345678912","createTime":1538030349000,"homePhone":"12345678912","household":"河南省新乡市","id":1,"kinName":"历史","kinshipPhone":"12334567891","name":"张三","region":"洛阳","state":"2","unitAddress":"河南省新乡市","visitingAddress":"河南省新乡市","workTelephone":"12345678911"}
         */

        private ProductBean product;

        public ProductBean getProduct() {
            return product;
        }

        public void setProduct(ProductBean product) {
            this.product = product;
        }

        public static class ProductBean {
            /**
             * IDNumber : 12345678978
             * address : 河南省新乡市
             * amount : 131
             * bank : http://115.60.255.11/static/bank.png
             * batch : 01AA
             * billingAddress : 河南省新乡市
             * cardNumber : 2121
             * caseId : 11
             * closeDate : 2018-08-07 14:36
             * company : 河南测试有限公司
             * contactName : 历史
             * contactPhone : 12345678912
             * createTime : 1538030349000
             * homePhone : 12345678912
             * household : 河南省新乡市
             * id : 1
             * kinName : 历史
             * kinshipPhone : 12334567891
             * name : 张三
             * region : 洛阳
             * state : 2
             * unitAddress : 河南省新乡市
             * visitingAddress : 河南省新乡市
             * workTelephone : 12345678911
             */

            private String IDNumber;
            private String address;
            private String amount;
            private String bank;
            private String batch;
            private String billingAddress;
            private String cardNumber;
            private String caseId;
            private String closeDate;
            private String company;
            private String contactName;
            private String contactPhone;
            private long createTime;
            private String homePhone;
            private String household;
            private int id;
            private String kinName;
            private String kinshipPhone;
            private String name;
            private String region;
            private String state;
            private String unitAddress;
            private String visitingAddress;
            private String workTelephone;

            public String getNote() {
                return note;
            }

            public void setNote(String note) {
                this.note = note;
            }

            private String note
                    ;

            public String getIDNumber() {
                return IDNumber;
            }

            public void setIDNumber(String IDNumber) {
                this.IDNumber = IDNumber;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public String getAmount() {
                return amount;
            }

            public void setAmount(String amount) {
                this.amount = amount;
            }

            public String getBank() {
                return bank;
            }

            public void setBank(String bank) {
                this.bank = bank;
            }

            public String getBatch() {
                return batch;
            }

            public void setBatch(String batch) {
                this.batch = batch;
            }

            public String getBillingAddress() {
                return billingAddress;
            }

            public void setBillingAddress(String billingAddress) {
                this.billingAddress = billingAddress;
            }

            public String getCardNumber() {
                return cardNumber;
            }

            public void setCardNumber(String cardNumber) {
                this.cardNumber = cardNumber;
            }

            public String getCaseId() {
                return caseId;
            }

            public void setCaseId(String caseId) {
                this.caseId = caseId;
            }

            public String getCloseDate() {
                return closeDate;
            }

            public void setCloseDate(String closeDate) {
                this.closeDate = closeDate;
            }

            public String getCompany() {
                return company;
            }

            public void setCompany(String company) {
                this.company = company;
            }

            public String getContactName() {
                return contactName;
            }

            public void setContactName(String contactName) {
                this.contactName = contactName;
            }

            public String getContactPhone() {
                return contactPhone;
            }

            public void setContactPhone(String contactPhone) {
                this.contactPhone = contactPhone;
            }

            public long getCreateTime() {
                return createTime;
            }

            public void setCreateTime(long createTime) {
                this.createTime = createTime;
            }

            public String getHomePhone() {
                return homePhone;
            }

            public void setHomePhone(String homePhone) {
                this.homePhone = homePhone;
            }

            public String getHousehold() {
                return household;
            }

            public void setHousehold(String household) {
                this.household = household;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getKinName() {
                return kinName;
            }

            public void setKinName(String kinName) {
                this.kinName = kinName;
            }

            public String getKinshipPhone() {
                return kinshipPhone;
            }

            public void setKinshipPhone(String kinshipPhone) {
                this.kinshipPhone = kinshipPhone;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getRegion() {
                return region;
            }

            public void setRegion(String region) {
                this.region = region;
            }

            public String getState() {
                return state;
            }

            public void setState(String state) {
                this.state = state;
            }

            public String getUnitAddress() {
                return unitAddress;
            }

            public void setUnitAddress(String unitAddress) {
                this.unitAddress = unitAddress;
            }

            public String getVisitingAddress() {
                return visitingAddress;
            }

            public void setVisitingAddress(String visitingAddress) {
                this.visitingAddress = visitingAddress;
            }

            public String getWorkTelephone() {
                return workTelephone;
            }

            public void setWorkTelephone(String workTelephone) {
                this.workTelephone = workTelephone;
            }
        }
    }
}
