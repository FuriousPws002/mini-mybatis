package org.apache.ibatis.entity;

/**
 * @author furious 2024/4/10
 */
public class UserDO {

    private String name;
    private String namex;
    private Integer age;

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

    public String getNamex() {
        return namex;
    }

    public void setNamex(String namex) {
        this.namex = namex;
    }
}
