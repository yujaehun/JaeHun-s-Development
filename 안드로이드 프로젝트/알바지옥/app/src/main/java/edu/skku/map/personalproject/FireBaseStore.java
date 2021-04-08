package edu.skku.map.personalproject;

import java.util.HashMap;
import java.util.Map;

public class FireBaseStore {
    public String username;
    public String store_name;
    public String pay;
    public String job_type;
    public String phone_number;
    public String location;
    public String content;
    public double latitude;
    public double longitude;


    public FireBaseStore(){

    }
    public FireBaseStore(String username, String store_name, String pay, String job_type, String phone_number, String location, String content, double latitude, double longitude){
        this.username = username;
        this.store_name = store_name;
        this.pay = pay;
        this.job_type = job_type;
        this.phone_number = phone_number;
        this.location = location;
        this.content = content;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Map<String, Object> tomap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("username", username);
        result.put("store_name", store_name);
        result.put("pay", pay);
        result.put("job_type", job_type);
        result.put("phone_number", phone_number);
        result.put("location", location);
        result.put("content", content);
        result.put("latitude", latitude);
        result.put("longitude", longitude);
        return result;
    }
}
