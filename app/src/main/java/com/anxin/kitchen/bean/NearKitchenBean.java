package com.anxin.kitchen.bean;

/**
 * Created by 唐午阳 on 2018/3/5.
 */

public class NearKitchenBean {

    private int code;
    private String message;
    private Data data;
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

    public void setData(Data data) {
        this.data = data;
    }
    public Data getData() {
        return data;
    }

    public class Data {
        private int kitchenId;
        private String kitchenName;
        private String longitude;
        private String latitude;
        private double distance;
        public void setKitchenid(int kitchenId) {
            this.kitchenId = kitchenId;
        }
        public int getKitchenid() {
            return kitchenId;
        }

        public void setKitchenname(String kitchenName) {
            this.kitchenName = kitchenName;
        }
        public String getKitchenname() {
            return kitchenName;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }
        public String getLongitude() {
            return longitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }
        public String getLatitude() {
            return latitude;
        }

        public void setDistance(double distance) {
            this.distance = distance;
        }
        public double getDistance() {
            return distance;
        }

    }
}
