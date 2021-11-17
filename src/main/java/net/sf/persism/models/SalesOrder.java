package net.sf.persism.models;

import java.sql.Date;
import java.util.Objects;

public final class SalesOrder {
    private Object identity;
    private Date date;
    private String comments;
    private String salespersonName;
    private Object salespersonId;
    private String clientName;
    private Object clientId;
    private Double adjustment;
    private Double commission;
    private String currencyName;
    private Double currencyRate;
    private String poNumber;
    private Double totalCharge;
    private String customerNumber;
    private String shippedVia;
    private String shipToAddress;
    private String clientAddress;
    private String controlNumber;
    private short status;
    private Date invoiceDate;
    private Double subTotal;
    private String terms;
    private String usid;
    private String userName;
    private Integer taxCode;
    private Date requestedShipDate;
    private Integer workOrderPrintCount;

    public Object getIdentity() {
        return identity;
    }

    public void setIdentity(Object identity) {
        this.identity = identity;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getSalespersonName() {
        return salespersonName;
    }

    public void setSalespersonName(String salespersonName) {
        this.salespersonName = salespersonName;
    }

    public Object getSalespersonId() {
        return salespersonId;
    }

    public void setSalespersonId(Object salespersonId) {
        this.salespersonId = salespersonId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public Object getClientId() {
        return clientId;
    }

    public void setClientId(Object clientId) {
        this.clientId = clientId;
    }

    public Double getAdjustment() {
        return adjustment;
    }

    public void setAdjustment(Double adjustment) {
        this.adjustment = adjustment;
    }

    public Double getCommission() {
        return commission;
    }

    public void setCommission(Double commission) {
        this.commission = commission;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public Double getCurrencyRate() {
        return currencyRate;
    }

    public void setCurrencyRate(Double currencyRate) {
        this.currencyRate = currencyRate;
    }

    public String getPoNumber() {
        return poNumber;
    }

    public void setPoNumber(String poNumber) {
        this.poNumber = poNumber;
    }

    public Double getTotalCharge() {
        return totalCharge;
    }

    public void setTotalCharge(Double totalCharge) {
        this.totalCharge = totalCharge;
    }

    public String getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(String customerNumber) {
        this.customerNumber = customerNumber;
    }

    public String getShippedVia() {
        return shippedVia;
    }

    public void setShippedVia(String shippedVia) {
        this.shippedVia = shippedVia;
    }

    public String getShipToAddress() {
        return shipToAddress;
    }

    public void setShipToAddress(String shipToAddress) {
        this.shipToAddress = shipToAddress;
    }

    public String getClientAddress() {
        return clientAddress;
    }

    public void setClientAddress(String clientAddress) {
        this.clientAddress = clientAddress;
    }

    public String getControlNumber() {
        return controlNumber;
    }

    public void setControlNumber(String controlNumber) {
        this.controlNumber = controlNumber;
    }

    public short getStatus() {
        return status;
    }

    public void setStatus(short status) {
        this.status = status;
    }

    public Date getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(Date invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public Double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(Double subTotal) {
        this.subTotal = subTotal;
    }

    public String getTerms() {
        return terms;
    }

    public void setTerms(String terms) {
        this.terms = terms;
    }

    public String getUsid() {
        return usid;
    }

    public void setUsid(String usid) {
        this.usid = usid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(Integer taxCode) {
        this.taxCode = taxCode;
    }

    public Date getRequestedShipDate() {
        return requestedShipDate;
    }

    public void setRequestedShipDate(Date requestedShipDate) {
        this.requestedShipDate = requestedShipDate;
    }

    public Integer getWorkOrderPrintCount() {
        return workOrderPrintCount;
    }

    public void setWorkOrderPrintCount(Integer workOrderPrintCount) {
        this.workOrderPrintCount = workOrderPrintCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SalesOrder that = (SalesOrder) o;
        return Objects.equals(identity, that.identity) && Objects.equals(date, that.date) && Objects.equals(comments, that.comments) && Objects.equals(salespersonName, that.salespersonName) && Objects.equals(salespersonId, that.salespersonId) && Objects.equals(clientName, that.clientName) && Objects.equals(clientId, that.clientId) && Objects.equals(adjustment, that.adjustment) && Objects.equals(commission, that.commission) && Objects.equals(currencyName, that.currencyName) && Objects.equals(currencyRate, that.currencyRate) && Objects.equals(poNumber, that.poNumber) && Objects.equals(totalCharge, that.totalCharge) && Objects.equals(customerNumber, that.customerNumber) && Objects.equals(shippedVia, that.shippedVia) && Objects.equals(shipToAddress, that.shipToAddress) && Objects.equals(clientAddress, that.clientAddress) && Objects.equals(controlNumber, that.controlNumber) && Objects.equals(status, that.status) && Objects.equals(invoiceDate, that.invoiceDate) && Objects.equals(subTotal, that.subTotal) && Objects.equals(terms, that.terms) && Objects.equals(usid, that.usid) && Objects.equals(userName, that.userName) && Objects.equals(taxCode, that.taxCode) && Objects.equals(requestedShipDate, that.requestedShipDate) && Objects.equals(workOrderPrintCount, that.workOrderPrintCount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identity, date, comments, salespersonName, salespersonId, clientName, clientId, adjustment, commission, currencyName, currencyRate, poNumber, totalCharge, customerNumber, shippedVia, shipToAddress, clientAddress, controlNumber, status, invoiceDate, subTotal, terms, usid, userName, taxCode, requestedShipDate, workOrderPrintCount);
    }

    @Override
    public String toString() {
        return "SalesOrder{" +
                "identity=" + identity +
                ", date=" + date +
                ", comments='" + comments + '\'' +
                ", salespersonName='" + salespersonName + '\'' +
                ", salespersonId=" + salespersonId +
                ", clientName='" + clientName + '\'' +
                ", clientId=" + clientId +
                ", adjustment=" + adjustment +
                ", commission=" + commission +
                ", currencyName='" + currencyName + '\'' +
                ", currencyRate=" + currencyRate +
                ", poNumber='" + poNumber + '\'' +
                ", totalCharge=" + totalCharge +
                ", customerNumber='" + customerNumber + '\'' +
                ", shippedVia='" + shippedVia + '\'' +
                ", shipToAddress='" + shipToAddress + '\'' +
                ", clientAddress='" + clientAddress + '\'' +
                ", controlNumber='" + controlNumber + '\'' +
                ", status=" + status +
                ", invoiceDate=" + invoiceDate +
                ", subTotal=" + subTotal +
                ", terms='" + terms + '\'' +
                ", usid='" + usid + '\'' +
                ", userName='" + userName + '\'' +
                ", taxCode=" + taxCode +
                ", requestedShipDate=" + requestedShipDate +
                ", workOrderPrintCount=" + workOrderPrintCount +
                "}\n";
    }
}
