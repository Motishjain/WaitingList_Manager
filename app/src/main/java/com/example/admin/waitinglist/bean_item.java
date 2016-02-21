package com.example.admin.waitinglist;

/**
 * Created by ali on 3/2/16.
 */
public class bean_item {
    String id,name,contact,people,est_wait,tot_wait;

    public bean_item(String id,String name, String contact, String people, String est_wait, String tot_wait) {
        this.id = id;
        this.name = name;
        this.contact = contact;
        this.people = people;
        this.est_wait = est_wait;
        this.tot_wait = tot_wait;
    }

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

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getPeople() {
        return people;
    }

    public void setPeople(String people) {
        this.people = people;
    }

    public String getEst_wait() {
        return est_wait;
    }

    public void setEst_wait(String est_wait) {
        this.est_wait = est_wait;
    }

    public String getTot_wait() {
        return tot_wait;
    }

    public void setTot_wait(String tot_wait) {
        this.tot_wait = tot_wait;
    }
}
