package org.apache.ibatis.entity;

import java.util.ArrayList;

/**
 * @author furious 2024/4/13
 */
public class UserDTO {

    private String name;
    private Integer age;

    private ArrayList<CarDO> carList = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public ArrayList<CarDO> getCarList() {
        return carList;
    }

    public void setCarList(ArrayList<CarDO> carList) {
        this.carList = carList;
    }
}
