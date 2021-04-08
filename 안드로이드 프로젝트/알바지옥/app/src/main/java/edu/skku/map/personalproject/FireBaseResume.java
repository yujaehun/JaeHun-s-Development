package edu.skku.map.personalproject;

import java.util.HashMap;
import java.util.Map;

public class FireBaseResume {

    public String username;
    public String store_name;
    public String fullname;
    public String birthday;
    public String phone_number;
    public String resume_content;
    public String resume_img;

    public FireBaseResume(){

    }
    public FireBaseResume(String username, String store_name, String fullname, String birthday, String phone_number, String resume_content, String resume_img){
        this.username = username;
        this.store_name = store_name;
        this.fullname = fullname;
        this.birthday = birthday;
        this.phone_number = phone_number;
        this.resume_content = resume_content;
        this.resume_img = resume_img;
    }

    public Map<String, Object> tomap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("username", username);
        result.put("store_name", store_name);
        result.put("fullname", fullname);
        result.put("birthday", birthday);
        result.put("phone_number", phone_number);
        result.put("resume_content", resume_content);
        result.put("resume_img", resume_img);
        return result;
    }
}
