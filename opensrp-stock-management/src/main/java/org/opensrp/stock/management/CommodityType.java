package org.opensrp.stock.management;

import java.util.UUID;

public class CommodityType extends BaseEntity {

    private String name;
    private CommodityType parent;
    private String classificationSystem;
    private String classificationId;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CommodityType getParent() {
        return parent;
    }

    public void setParent(CommodityType parent) {
        this.parent = parent;
    }

    public String getClassificationSystem() {
        return classificationSystem;
    }

    public void setClassificationSystem(String classificationSystem) {
        this.classificationSystem = classificationSystem;
    }

    public String getClassificationId() {
        return classificationId;
    }

    public void setClassificationId(String classificationId) {
        this.classificationId = classificationId;
    }
}
