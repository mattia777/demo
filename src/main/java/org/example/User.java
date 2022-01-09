package org.example;

import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;
import org.json.JSONObject;

@DataType()
public class User {

    @Property()
    private String name;
    @Property()
    private String surname;
    @Property()
    private String gender;
    @Property()
    private int age;
    
    public User(){
    }

    // get methods

    public String getName() { // The get method returns the value of the variable.
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getGender() {
        return gender;
    }

    public int getAge() {
        return age;
    }

    // set methods

    public void setName(String value) { // The set method takes a parameter and assigns it to the variable.
        this.name = value;              // The 'this' keyword is used to refer to the current object.
    }

    public void setSurname(String value) {
        this.surname = value;
    }

    public void setGender(String value) {
        this.gender = value;
    }

    public void setAge(Integer value) {
        this.age = value;
    }

    public String toJSONString() {
        return new JSONObject(this).toString();
    }

    public static User fromJSONString(String json) {
        String name = new JSONObject(json).getString("name");
        String surname = new JSONObject(json).getString("surname");
        String gender = new JSONObject(json).getString("gender");
        Integer age = new JSONObject(json).getInt("age");

        User asset = new User();
        asset.setName(name);
        asset.setSurname(surname);
        asset.setGender(gender);
        asset.setAge(age);
        return asset;
    }
}
