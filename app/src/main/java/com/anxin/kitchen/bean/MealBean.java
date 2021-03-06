package com.anxin.kitchen.bean;

import java.util.List;

/**
 * Created by 唐午阳 on 2018/3/21.
 */

public class MealBean {
    private int code;
    private String message;
    private List<Data> data;
    public void setCode(int code) {
        this.code = code;
    }
    public int getCode() {
        return code;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public String getMessage() {
        return message;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }
    public List<Data> getData() {
        return data;
    }

    public static class FoodList {
        private String dishName;
        private int foodId;
        private int cuisineId;
        private int tasteId;
        private int makeType;
        private int status;
        private int type;
        private String img;

        public String getDishName() {
            return dishName;
        }

        public void setDishName(String dishName) {
            this.dishName = dishName;
        }

        public void setFoodId(int foodId) {
            this.foodId = foodId;
        }
        public int getFoodId() {
            return foodId;
        }

        public void setCuisineId(int cuisineId) {
            this.cuisineId = cuisineId;
        }
        public int getCuisineId() {
            return cuisineId;
        }

        public void setTasteId(int tasteId) {
            this.tasteId = tasteId;
        }
        public int getTasteId() {
            return tasteId;
        }

        public void setMakeType(int makeType) {
            this.makeType = makeType;
        }
        public int getMakeType() {
            return makeType;
        }

        public void setStatus(int status) {
            this.status = status;
        }
        public int getStatus() {
            return status;
        }

        public void setType(int type) {
            this.type = type;
        }
        public int getType() {
            return type;
        }

        public void setImg(String img) {
            this.img = img;
        }
        public String getImg() {
            return img;
        }

        @Override
        public String toString() {
            return "FoodList{" +
                    "foodId=" + foodId +
                    ", cuisineId=" + cuisineId +
                    ", tasteId=" + tasteId +
                    ", makeType=" + makeType +
                    ", status=" + status +
                    ", type=" + type +
                    ", img='" + img + '\'' +
                    '}';
        }
    }


    public static class Data {
        private boolean isSelectByMaster = false;
        private long menuDay;
        private int kitchenId;
        private int packageId;
        private int dietId;
        private int cuisineId;
        private double price;
        private String dashPrice;
        private String packageName;
        private String img;
        private int eatType;
        private int nums ; //设置的份数
        private List<FoodList> foodList;
        private boolean grouporderTag;  //选择团下的单标志位
        private boolean setNumsTag;   //设置数量的单标志位
        private int relativeGroupId;  //假如选择的团 则有广联的团
        private String relatedGroupName; //关联的团的名字

        public int getRelativeGroupId() {
            return relativeGroupId;
        }

        public String getRelatedGroupName() {
            return relatedGroupName;
        }

        public void setRelatedGroupName(String relatedGroupName) {
            this.relatedGroupName = relatedGroupName;
        }

        public void setRelativeGroupId(int relativeGroupId) {
            this.relativeGroupId = relativeGroupId;
        }

        public boolean isGrouporderTag() {
            return grouporderTag;
        }

        public void setGrouporderTag(boolean grouporderTag) {
            this.grouporderTag = grouporderTag;
        }

        public boolean isSetNumsTag() {
            return setNumsTag;
        }

        public void setSetNumsTag(boolean setNumsTag) {
            this.setNumsTag = setNumsTag;
        }

        public int getNums() {
            return nums;
        }

        public void setNums(int nums) {
            this.nums = nums;
        }

        public boolean isSelectByMaster() {
            return isSelectByMaster;
        }

        public void setSelectByMaster(boolean selectByMaster) {
            isSelectByMaster = selectByMaster;
        }

        public void setMenuDay(long menuDay) {
            this.menuDay = menuDay;
        }

        public long getMenuDay() {
            return menuDay;
        }

        public void setKitchenId(int kitchenId) {
            this.kitchenId = kitchenId;
        }

        public int getKitchenId() {
            return kitchenId;
        }

        public void setPackageId(int packageId) {
            this.packageId = packageId;
        }

        public int getPackageId() {
            return packageId;
        }

        public void setDietId(int dietId) {
            this.dietId = dietId;
        }

        public int getDietId() {
            return dietId;
        }


        public int getCuisineId() {
            return cuisineId;
        }

        public void setCuisineId(int cuisineId) {
            this.cuisineId = cuisineId;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public String getDashPrice() {
            return dashPrice;
        }

        public void setDashPrice(String dashPrice) {
            this.dashPrice = dashPrice;
        }

        public String getPackageName() {
            return packageName;
        }

        public void setPackageName(String packageName) {
            this.packageName = packageName;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public int getEatType() {
            return eatType;
        }

        public void setEatType(int eatType) {
            this.eatType = eatType;
        }

        public List<FoodList> getFoodList() {
            return foodList;
        }

        public void setFoodList(List<FoodList> foodList) {
            this.foodList = foodList;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "menuDay=" + menuDay +
                    ", kitchenId=" + kitchenId +
                    ", packageId=" + packageId +
                    ", dietId=" + dietId +
                    ", cuisineId=" + cuisineId +
                    ", price=" + price +
                    ", dashPrice='" + dashPrice + '\'' +
                    ", packageName='" + packageName + '\'' +
                    ", img='" + img + '\'' +
                    ", eatType=" + eatType +
                    ", foodList=" + foodList +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "MealBean{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
