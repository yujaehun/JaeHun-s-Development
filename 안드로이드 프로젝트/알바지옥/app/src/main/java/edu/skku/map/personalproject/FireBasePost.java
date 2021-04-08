package edu.skku.map.personalproject;

import java.util.HashMap;
import java.util.Map;

public class FireBasePost {
    public String username;
    public String password;
    public String fullname;
    public String birthday;
    public String phone_number;

    public FireBasePost(){

    }
    public FireBasePost(String username, String password, String fullname, String birthday, String phone_number){
        this.username = username;
        this.password = password;
        this.fullname = fullname;
        this.birthday = birthday;
        this.phone_number = phone_number;
    }

    public Map<String, Object> tomap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("username", username);
        result.put("password", password);
        result.put("fullname", fullname);
        result.put("birthday", birthday);
        result.put("phone_number", phone_number);
        return result;
    }
}
