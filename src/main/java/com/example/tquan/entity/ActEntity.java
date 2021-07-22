package com.example.tquan.entity;

public class ActEntity {
    private String id;
    private String name;
    private String remark;
    private String defaultValue;
    private String isBasic;
    private String isRequried;
    private String isInsert;
    private String isEdit;
    private String inputType;
    private String compant;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getIsBasic() {
        return isBasic;
    }

    public void setIsBasic(String isBasic) {
        this.isBasic = isBasic;
    }

    public String getIsRequried() {
        return isRequried;
    }

    public void setIsRequried(String isRequried) {
        this.isRequried = isRequried;
    }

    public String getIsInsert() {
        return isInsert;
    }

    public void setIsInsert(String isInsert) {
        this.isInsert = isInsert;
    }

    public String getIsEdit() {
        return isEdit;
    }

    public void setIsEdit(String isEdit) {
        this.isEdit = isEdit;
    }

    public String getInputType() {
        return inputType;
    }

    public void setInputType(String inputType) {
        this.inputType = inputType;
    }

    public String getCompant() {
        return compant;
    }

    public void setCompant(String compant) {
        this.compant = compant;
    }

    @Override
    public String toString() {
        return "ActEntity{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", remark='" + remark + '\'' +
                ", defaultValue='" + defaultValue + '\'' +
                ", isBasic='" + isBasic + '\'' +
                ", isRequried='" + isRequried + '\'' +
                ", isInsert='" + isInsert + '\'' +
                ", isEdit='" + isEdit + '\'' +
                ", inputType='" + inputType + '\'' +
                ", compant='" + compant + '\'' +
                '}';
    }
}
