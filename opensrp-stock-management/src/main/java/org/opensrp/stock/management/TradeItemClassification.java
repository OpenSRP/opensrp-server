package org.opensrp.stock.management;

public class TradeItemClassification extends BaseEntity {

    private TradeItem tradeItem;
    private String classificationSystem;
    private String classificationId;

    TradeItemClassification(TradeItem tradeItem, String classificationSystem, String classificationId) {
        this.tradeItem = tradeItem;
        this.classificationSystem = classificationSystem;
        this.classificationId = classificationId;
    }

    public TradeItem getTradeItem() {
        return tradeItem;
    }

    public void setTradeItem(TradeItem tradeItem) {
        this.tradeItem = tradeItem;
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
