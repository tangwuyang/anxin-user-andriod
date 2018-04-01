package com.anxin.kitchen.bean;

import java.util.List;

/**
 * Created by 唐午阳 on 2018/3/27.
 *康复食疗数据实体类
 */

public class RecoverBean {
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


    public class FoodList {

        private String dishName;
        private int foodId;
        private int cuisineId;
        private int tasteId;
        private int makeType;
        private int status;
        private int type;
        private String img;
        public void setDishName(String dishName) {
            this.dishName = dishName;
        }
        public String getDishName() {
            return dishName;
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

    }


    public static class Data {

        private String menuDay;
        private int kitchenId;
        private int packageId;
        private int dietId;
        private int cuisineId;
        private int price;
        private String dashPrice;
        private String packageName;
        private String img;
        private String eatType;
        private List<FoodList> foodList;
        private int nums;

        public int getNums() {
            return nums;
        }

        public void setNums(int nums) {
            this.nums = nums;
        }

        public void setMenuDay(String menuDay) {
            this.menuDay = menuDay;
        }
        public String getMenuDay() {
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

        public void setCuisineId(int cuisineId) {
            this.cuisineId = cuisineId;
        }
        public int getCuisineId() {
            return cuisineId;
        }

        public void setPrice(int price) {
            this.price = price;
        }
        public int getPrice() {
            return price;
        }

        public void setDashPrice(String dashPrice) {
            this.dashPrice = dashPrice;
        }
        public String getDashPrice() {
            return dashPrice;
        }

        public void setPackageName(String packageName) {
            this.packageName = packageName;
        }
        public String getPackageName() {
            return packageName;
        }

        public void setImg(String img) {
            this.img = img;
        }
        public String getImg() {
            return img;
        }

        public void setEatType(String eatType) {
            this.eatType = eatType;
        }
        public String getEatType() {
            return eatType;
        }

        public void setFoodList(List<FoodList> foodList) {
            this.foodList = foodList;
        }
        public List<FoodList> getFoodList() {
            return foodList;
        }

    }

}
