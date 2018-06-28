package org.opensrp.stock.management;

import java.util.List;
import java.util.UUID;

public class TradeItem extends BaseEntity {

    private Gtin gtin;
    private String manufacturerOfTradeItem;
    List<TradeItemClassification> classifications;

    public TradeItem(String manufacturerOfTradeItem, List<TradeItemClassification> classifications) {
        this.manufacturerOfTradeItem = manufacturerOfTradeItem;
        this.classifications = classifications;
    }

    /**
     * A TradeItem can fulfill for the given product if the product is this trade item or if this
     * product's CommodityType is the given product.
     * @param product the product we'd like to fulfill for.
     * @return true if we can fulfill for the given product, false otherwise.
     */
    public boolean canFulfill(CommodityType product) {
        for (TradeItemClassification classification : classifications) {
            if (product.getClassificationSystem().equals(classification.getClassificationSystem())
                    && product.getClassificationId().equals(classification.getClassificationId())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Assigns a commodity type to this trade item - will associate this trade item
     * with the classification system of the provided commodity type.
     * @param commodityType the commodity type to associate with
     */
    public void assignCommodityType(CommodityType commodityType) {
        assignCommodityType(commodityType.getClassificationSystem(),
                commodityType.getClassificationId());
    }

    /**
     * Assigns to the classification system and classification id.
     * @param classificationSystem the classification system
     * @param classificationId the id of the classification system.
     */
    public void assignCommodityType(String classificationSystem, String classificationId) {
        TradeItemClassification existingClassification = findClassificationById(classificationId);

        if (existingClassification == null) {
            classifications.add(new TradeItemClassification(this, classificationSystem,
                    classificationId));
        } else {
            existingClassification.setClassificationSystem(classificationSystem);
        }
    }

    TradeItemClassification findClassificationById(String classificationId) {
        for (TradeItemClassification classification : classifications) {
            if (classificationId.equals(classification.getClassificationId())) {
                return classification;
            }
        }
        return null;
    }
}

