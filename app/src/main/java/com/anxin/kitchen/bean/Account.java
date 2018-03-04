package com.anxin.kitchen.bean;


/**
 * 用户信息
 */

public class Account {
    private String UserID;//用户ID
    private String UserSize;//份量,1:成人餐,2:儿童餐
    private String UserStatus;//用户状态,1:正常，0：禁用
    private String UserSex;//性别,0:未知，1:男性,2:女
    private String UserPhone;//手机号
    private String UserPassword;//密码
    private String UserInitials;//首字母
    private String UserNickName;//昵称
    private String UserTrueName;//真实姓名
    private String UserWeiXin;//微信号
    private String UserNum;//份数
    private String UserMoney;//用户余额
    private String UserDeposit;//押金
    private String UserAlipay;//支付宝帐号
    private String UserBirthdayTime;//用户生日
    private String UserLogoPathURL;//用户头像
    private int UserAddressNum;//地址个数
    private int UserGroupUserNum;//团友数量
    private Long UserUpdateTime;//更新时间
    private Long UserLastLoginTime;//最后登录时间
    private Long UserCreateTime;//创建时间
    private String UserAMToKen;

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getUserSize() {
        return UserSize;
    }

    public void setUserSize(String userSize) {
        UserSize = userSize;
    }

    public String getUserStatus() {
        return UserStatus;
    }

    public void setUserStatus(String userStatus) {
        UserStatus = userStatus;
    }

    public String getUserSex() {
        return UserSex;
    }

    public void setUserSex(String userSex) {
        UserSex = userSex;
    }

    public String getUserPhone() {
        return UserPhone;
    }

    public void setUserPhone(String userPhone) {
        UserPhone = userPhone;
    }

    public String getUserPassword() {
        return UserPassword;
    }

    public void setUserPassword(String userPassword) {
        UserPassword = userPassword;
    }

    public String getUserInitials() {
        return UserInitials;
    }

    public void setUserInitials(String userInitials) {
        UserInitials = userInitials;
    }

    public String getUserNickName() {
        return UserNickName;
    }

    public void setUserNickName(String userNickName) {
        UserNickName = userNickName;
    }

    public String getUserTrueName() {
        return UserTrueName;
    }

    public void setUserTrueName(String userTrueName) {
        UserTrueName = userTrueName;
    }

    public String getUserWeiXin() {
        return UserWeiXin;
    }

    public void setUserWeiXin(String userWeiXin) {
        UserWeiXin = userWeiXin;
    }

    public String getUserNum() {
        return UserNum;
    }

    public void setUserNum(String userNum) {
        UserNum = userNum;
    }

    public String getUserMoney() {
        return UserMoney;
    }

    public void setUserMoney(String userMoney) {
        UserMoney = userMoney;
    }

    public String getUserDeposit() {
        return UserDeposit;
    }

    public void setUserDeposit(String userDeposit) {
        UserDeposit = userDeposit;
    }

    public String getUserAlipay() {
        return UserAlipay;
    }

    public void setUserAlipay(String userAlipay) {
        UserAlipay = userAlipay;
    }

    public String getUserBirthdayTime() {
        return UserBirthdayTime;
    }

    public void setUserBirthdayTime(String userBirthdayTime) {
        UserBirthdayTime = userBirthdayTime;
    }

    public String getUserLogoPathURL() {
        return UserLogoPathURL;
    }

    public void setUserLogoPathURL(String userLogoPathURL) {
        UserLogoPathURL = userLogoPathURL;
    }

    public int getUserAddressNum() {
        return UserAddressNum;
    }

    public void setUserAddressNum(int userAddressNum) {
        UserAddressNum = userAddressNum;
    }

    public int getUserGroupUserNum() {
        return UserGroupUserNum;
    }

    public void setUserGroupUserNum(int userGroupUserNum) {
        UserGroupUserNum = userGroupUserNum;
    }

    public Long getUserUpdateTime() {
        return UserUpdateTime;
    }

    public void setUserUpdateTime(Long userUpdateTime) {
        UserUpdateTime = userUpdateTime;
    }

    public Long getUserLastLoginTime() {
        return UserLastLoginTime;
    }

    public void setUserLastLoginTime(Long userLastLoginTime) {
        UserLastLoginTime = userLastLoginTime;
    }

    public Long getUserCreateTime() {
        return UserCreateTime;
    }

    public void setUserCreateTime(Long userCreateTime) {
        UserCreateTime = userCreateTime;
    }

    public String getUserAMToKen() {
        return UserAMToKen;
    }

    public void setUserAMToKen(String userAMToKen) {
        UserAMToKen = userAMToKen;
    }

    @Override
    public String toString() {
        return "Account{" +
                "UserID='" + UserID + '\'' +
                ", UserSize='" + UserSize + '\'' +
                ", UserStatus='" + UserStatus + '\'' +
                ", UserSex='" + UserSex + '\'' +
                ", UserPhone='" + UserPhone + '\'' +
                ", UserPassword='" + UserPassword + '\'' +
                ", UserInitials='" + UserInitials + '\'' +
                ", UserNickName='" + UserNickName + '\'' +
                ", UserTrueName='" + UserTrueName + '\'' +
                ", UserWeiXin='" + UserWeiXin + '\'' +
                ", UserNum='" + UserNum + '\'' +
                ", UserMoney='" + UserMoney + '\'' +
                ", UserDeposit='" + UserDeposit + '\'' +
                ", UserAlipay='" + UserAlipay + '\'' +
                ", UserBirthdayTime='" + UserBirthdayTime + '\'' +
                ", UserLogoPathURL='" + UserLogoPathURL + '\'' +
                ", UserAddressNum=" + UserAddressNum +
                ", UserGroupUserNum=" + UserGroupUserNum +
                ", UserUpdateTime=" + UserUpdateTime +
                ", UserLastLoginTime=" + UserLastLoginTime +
                ", UserCreateTime=" + UserCreateTime +
                ", UserAMToKen='" + UserAMToKen + '\'' +
                '}';
    }
}
