package com.anxin.kitchen.bean.Order;

/**
 * Created by xujianjun on 2018/4/10.
 */

public class User {
    private long id;//用户ID
    private int size;//份量,1:成人餐,2:儿童餐
    private int status;//用户状态,1:正常，0：禁用
    private int sex;//性别,0:未知，1:男性,2:女
    private String phone;//手机号
    private String password;//密码
    private String initials;//首字母
    private String nickName;//昵称
    private String trueName;//真实姓名
    private String weixin;//微信号
    private int num;//份数
    private double money;//用户余额
    private double deposit;//押金
    private String alipay;//支付宝帐号
    private long birthdayTime;//生日
    private String userLogo;//用户头像
    private int addressNum;//地址个数
    private int groupUserNum;//团友数量
    private long updateTime;//更新时间
    private long lastLoginTime;//最后登录时间
    private long createTime;//创建时间

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getInitials() {
        return initials;
    }

    public void setInitials(String initials) {
        this.initials = initials;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getTrueName() {
        return trueName;
    }

    public void setTrueName(String trueName) {
        this.trueName = trueName;
    }

    public String getWeixin() {
        return weixin;
    }

    public void setWeixin(String weixin) {
        this.weixin = weixin;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public double getDeposit() {
        return deposit;
    }

    public void setDeposit(double deposit) {
        this.deposit = deposit;
    }

    public String getAlipay() {
        return alipay;
    }

    public void setAlipay(String alipay) {
        this.alipay = alipay;
    }

    public long getBirthdayTime() {
        return birthdayTime;
    }

    public void setBirthdayTime(long birthdayTime) {
        this.birthdayTime = birthdayTime;
    }

    public String getUserLogo() {
        return userLogo;
    }

    public void setUserLogo(String userLogo) {
        this.userLogo = userLogo;
    }

    public int getAddressNum() {
        return addressNum;
    }

    public void setAddressNum(int addressNum) {
        this.addressNum = addressNum;
    }

    public int getGroupUserNum() {
        return groupUserNum;
    }

    public void setGroupUserNum(int groupUserNum) {
        this.groupUserNum = groupUserNum;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public long getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(long lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

}
