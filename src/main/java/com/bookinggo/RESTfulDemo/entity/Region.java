package com.bookinggo.RESTfulDemo.entity;


public enum Region {
    South("South"), West("West"), North("North"), East("East");

    private String label;

    private Region(String label) {

        this.label = label;
    }

    public static Region findByLabel(String byLabel) {
        for(Region r:Region.values()) {
            if (r.label.equalsIgnoreCase(byLabel)) {
                return r;
            }
        }

        return null;
    }

    public String getLabel(){
        return label;
    }
}
