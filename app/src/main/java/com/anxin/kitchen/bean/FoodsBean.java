package com.anxin.kitchen.bean;

import com.anxin.kitchen.MyApplication;

import java.util.List;

/**
 * Created by 唐午阳 on 2018/4/7.
 */

public class FoodsBean {
    private int code;

    private String message;

    private List<Data> data;

    public void setCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public List<Data> getData() {
        return this.data;
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
            return this.dishName;
        }

        public void setFoodId(int foodId) {
            this.foodId = foodId;
        }

        public int getFoodId() {
            return this.foodId;
        }

        public void setCuisineId(int cuisineId) {
            this.cuisineId = cuisineId;
        }

        public int getCuisineId() {
            return this.cuisineId;
        }

        public void setTasteId(int tasteId) {
            this.tasteId = tasteId;
        }

        public int getTasteId() {
            return this.tasteId;
        }

        public void setMakeType(int makeType) {
            this.makeType = makeType;
        }

        public int getMakeType() {
            return this.makeType;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getStatus() {
            return this.status;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getType() {
            return this.type;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getImg() {
            return this.img;
        }

    }


    public class Data {
        private String menuDay;

        private int kitchenId;

        private int packageId;

        private int dietId;

        private int cuisineId;

        private double price;
        private double priceSmall;

        private String dashPrice;

        private String packageName;

        private String img;

        private String eatType;

        private int lastStock;

        private List<FoodList> foodList;

        public void setMenuDay(String menuDay) {
            this.menuDay = menuDay;
        }

        public String getMenuDay() {
            return this.menuDay;
        }

        public void setKitchenId(int kitchenId) {
            this.kitchenId = kitchenId;
        }

        public int getKitchenId() {
            return this.kitchenId;
        }

        public void setPackageId(int packageId) {
            this.packageId = packageId;
        }

        public int getPackageId() {
            return this.packageId;
        }

        public void setDietId(int dietId) {
            this.dietId = dietId;
        }

        public int getDietId() {
            return this.dietId;
        }

        public double getPriceSmall() {
            return priceSmall;
        }

        public void setPriceSmall(double priceSmall) {
            this.priceSmall = priceSmall;
        }

        public void setCuisineId(int cuisineId) {
            this.cuisineId = cuisineId;
        }

        public int getCuisineId() {
            return this.cuisineId;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public double getPrice() {
            Account account = MyApplication.getInstance().getAccount();
            if (account != null && account.getUserSize().equals("2")) {
                return this.priceSmall;
            }
            return this.price;
        }

        public void setDashPrice(String dashPrice) {
            this.dashPrice = dashPrice;
        }

        public String getDashPrice() {
            return this.dashPrice;
        }

        public void setPackageName(String packageName) {
            this.packageName = packageName;
        }

        public String getPackageName() {
            return this.packageName;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getImg() {
            return this.img;
        }

        public void setEatType(String eatType) {
            this.eatType = eatType;
        }

        public String getEatType() {
            return this.eatType;
        }

        public void setLastStock(int lastStock) {
            this.lastStock = lastStock;
        }

        public int getLastStock() {
            return this.lastStock;
        }

        public void setFoodList(List<FoodList> foodList) {
            this.foodList = foodList;
        }

        public List<FoodList> getFoodList() {
            return this.foodList;
        }

    }
}
