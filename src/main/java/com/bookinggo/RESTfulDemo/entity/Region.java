package com.bookinggo.RESTfulDemo.entity;

public enum Region {
    North("North"), East("East"), South("South"), West("West");

    private String label;

    Region(String label) {
        this.label = label;
    }

    public static Region findByLabel(String byLabel){
        for(Region r : Region.values()) {
            if(r.label.equalsIgnoreCase(byLabel)) {
                return r;
            }
        }

        return null;
    }
}
