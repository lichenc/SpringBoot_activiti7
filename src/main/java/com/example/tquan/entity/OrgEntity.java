package com.example.tquan.entity;

import java.util.List;

public class OrgEntity {
    /**
     * 唯一编号 uuid
     */
    private String id;

    /**
     * 名称
     */
    private String name;

    /**
     * 父节点id
     */
    private String parentId;
    //扩展字段
    private String value;


    /**
     * 子节点(数据库中不存在该字段，仅用于传输数据使用)
     */
    private List<?> children;

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

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public List<?> getChildren() {
        return children;
    }

    public void setChildren(List<?> children) {
        this.children = children;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "OrgEntity{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", parentId='" + parentId + '\'' +
                ", value='" + value + '\'' +
                ", children=" + children +
                '}';
    }
}
