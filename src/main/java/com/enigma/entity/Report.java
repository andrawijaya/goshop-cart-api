package com.enigma.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Report {
    private String productCode;
    private Date date;
    private String vendorName;
    private String productName;
    private String categoryName;
    private Integer productPrice;
    private Integer qty;
    private Integer total;

}
