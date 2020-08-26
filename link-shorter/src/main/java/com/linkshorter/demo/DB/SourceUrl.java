package com.linkshorter.demo.DB;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "source_url")
public class SourceUrl {

    private String rootUrl;
    private String suffixUrl;
    private String resourceId;
    private String totalClickCount;
    private String expirationDate;

    public SourceUrl() {

    }

    public SourceUrl(String rootUrl, String suffixUrl, String resourceId, String totalClickCount, String expirationDate) {
        super();
        this.rootUrl = rootUrl;
        this.suffixUrl = suffixUrl;
        this.resourceId = resourceId;
        this.totalClickCount = totalClickCount;
        this.expirationDate = expirationDate;
    }

    @DynamoDBHashKey(attributeName = "root_url")
    public String getRootUrl() {
        return rootUrl;
    }

    public void setRootUrl(String rootUrl) {
        this.rootUrl = rootUrl;
    }

    @DynamoDBRangeKey(attributeName = "suffix_url")
    public String getSuffixUrl() {
        return suffixUrl;
    }

    public void setSuffixUrl(String suffixUrl) {
        this.suffixUrl = suffixUrl;
    }

    @DynamoDBAttribute(attributeName = "resource_id")
    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    @DynamoDBAttribute(attributeName = "total_click_count")
    public String getTotalClickCount() {
        return totalClickCount;
    }

    public void setTotalClickCount(String totalClickCount) {
        this.totalClickCount = totalClickCount;
    }

    @DynamoDBAttribute(attributeName = "expiration_date")
    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }
}
