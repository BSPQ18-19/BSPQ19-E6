package com.spq.group6.server.data;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import java.io.Serializable;

@PersistenceCapable(detachable = "true")
public class Product implements Serializable {
    private static final long serialVersionUID = -7363525693084022738L;
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.INCREMENT)
    private long productID;
    private String name;
    private String description;

    public Product(String name, String description) {
        super();
        this.name = name;
        this.description = description;
    }

    public Product(Product p) {
        super();
        this.name = p.getName();
        this.description = p.getDescription();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return name.equals(product.name) &&
                description.equals(product.description);
    }

    public long getProductID() {
        return productID;
    }

    public void setProductID(long productID) {
        this.productID = productID;
    }


    public String toString() {
        return name;
    }

}