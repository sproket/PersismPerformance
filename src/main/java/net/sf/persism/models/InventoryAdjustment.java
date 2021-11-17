package net.sf.persism.models;

import java.sql.Date;
import java.util.Objects;

public final class InventoryAdjustment {
    private int identity;
    private String itemId;
    private String itemName;
    private short adjustmentType;
    private Date adjustmentDate;
    private String userName;
    private Double stockBefore;
    private Double stockAfter;
    private String stockUnits;
    private String comments;

    public int getIdentity() {
        return identity;
    }

    public void setIdentity(int identity) {
        this.identity = identity;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public short getAdjustmentType() {
        return adjustmentType;
    }

    public void setAdjustmentType(short adjustmentType) {
        this.adjustmentType = adjustmentType;
    }

    public Date getAdjustmentDate() {
        return adjustmentDate;
    }

    public void setAdjustmentDate(Date adjustmentDate) {
        this.adjustmentDate = adjustmentDate;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Double getStockBefore() {
        return stockBefore;
    }

    public void setStockBefore(Double stockBefore) {
        this.stockBefore = stockBefore;
    }

    public Double getStockAfter() {
        return stockAfter;
    }

    public void setStockAfter(Double stockAfter) {
        this.stockAfter = stockAfter;
    }

    public String getStockUnits() {
        return stockUnits;
    }

    public void setStockUnits(String stockUnits) {
        this.stockUnits = stockUnits;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InventoryAdjustment that = (InventoryAdjustment) o;
        return identity == that.identity && Objects.equals(itemId, that.itemId) && Objects.equals(itemName, that.itemName) && Objects.equals(adjustmentType, that.adjustmentType) && Objects.equals(adjustmentDate, that.adjustmentDate) && Objects.equals(userName, that.userName) && Objects.equals(stockBefore, that.stockBefore) && Objects.equals(stockAfter, that.stockAfter) && Objects.equals(stockUnits, that.stockUnits) && Objects.equals(comments, that.comments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identity, itemId, itemName, adjustmentType, adjustmentDate, userName, stockBefore, stockAfter, stockUnits, comments);
    }

    @Override
    public String toString() {
        return "InventoryAdjustment{" +
                "identity=" + identity +
                ", itemId='" + itemId + '\'' +
                ", itemName='" + itemName + '\'' +
                ", adjustmentType=" + adjustmentType +
                ", adjustmentDate=" + adjustmentDate +
                ", userName='" + userName + '\'' +
                ", stockBefore=" + stockBefore +
                ", stockAfter=" + stockAfter +
                ", stockUnits='" + stockUnits + '\'' +
                ", comments='" + comments + '\'' +
                "}\n";
    }
}
