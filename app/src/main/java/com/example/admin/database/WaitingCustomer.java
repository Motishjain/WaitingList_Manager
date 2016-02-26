package com.example.admin.database;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by mjai37 on 1/21/2016.
 */

@DatabaseTable(tableName = "WAITING_CUSTOMER")
public class WaitingCustomer implements Serializable {

    public WaitingCustomer(Integer id, String name, String contactNumber, String totalPeople, String estWaitingTime, String totalWaitingTime, String notes) {
        this.id = id;
        this.name = name;
        this.contactNumber = contactNumber;
        this.totalPeople = totalPeople;
        this.estWaitingTime = estWaitingTime;
        this.totalWaitingTime = totalWaitingTime;
        this.notes = notes;
    }

    public WaitingCustomer() {

    }

    @DatabaseField(generatedId = true)
    private Integer id;

    @DatabaseField
    private String name;

    @DatabaseField
    private String contactNumber;

    @DatabaseField
    private String totalPeople;

    private String totalWaitingTime;

    @DatabaseField
    private String estWaitingTime;

    @DatabaseField(dataType = DataType.DATE_LONG)
    private Date createdTs;

    @DatabaseField(dataType = DataType.BOOLEAN)
    private boolean deleted;

    @DatabaseField
    private String notes;

    @DatabaseField(dataType = DataType.BOOLEAN)
    private boolean notified;

    @DatabaseField(dataType = DataType.BOOLEAN)
    private boolean delayed;

    @DatabaseField(dataType = DataType.BOOLEAN)
    private boolean confirmed;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getTotalPeople() {
        return totalPeople;
    }

    public void setTotalPeople(String totalPeople) {
        this.totalPeople = totalPeople;
    }

    public String getEstWaitingTime() {
        return estWaitingTime;
    }

    public void setEstWaitingTime(String estWaitingTime) {
        this.estWaitingTime = estWaitingTime;
    }

    public Date getCreatedTs() {
        return createdTs;
    }

    public void setCreatedTs(Date createdTs) {
        this.createdTs = createdTs;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getTotalWaitingTime() {
        return totalWaitingTime;
    }

    public void setTotalWaitingTime(String totalWaitingTime) {
        this.totalWaitingTime = totalWaitingTime;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public boolean isNotified() {
        return notified;
    }

    public void setNotified(boolean notified) {
        this.notified = notified;
    }

    public boolean isDelayed() {
        return delayed;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setDelayed(boolean delayed) {
        this.delayed = delayed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }
}
