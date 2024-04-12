package org.apache.ibatis.entity;

import java.util.List;

/**
 * @author furious 2024/4/13
 */
public class UserDTO {

    private String name;
    private Integer age;

    private List<CarDO> carList;

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

    public List<CarDO> getCarList() {
        return carList;
    }

    public void setCarList(List<CarDO> carList) {
        this.carList = carList;
    }
}
