/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 *
 * @author 
 */
public class Product {

    private int productId;
    private int brandId;
    private String categoryName;
    private String brandName;
    private int categoryId;
    private String model;
    private String fullName;
    private String description;
    private int deleted;

    private long price;
    private String image;
    private String image1;
    private String image2;
    private String image3;
    private int quantity;
    private int stock;

    private HashMap<String, String> attributes;
    private List<AttributeDetail> attributeDetails;

    public Product() {
    }

    public Product(String model, int deleted, int stock) {
        this.model = model;
        this.deleted = deleted;
        this.stock = stock;
    }

    public Product(int productId, String model, String fullName, String description, int deleted, long price, String image, int stock) {
        this.productId = productId;
        this.model = model;
        this.fullName = fullName;
        this.description = description;
        this.deleted = deleted;
        this.price = price;
        this.image = image;
        this.stock = stock;
    }

    public Product(String categoryName, String brandName, String model, String fullName, String description, int deleted, long price, String image, String image1, String image2, String image3) {
        this.categoryName = categoryName;
        this.brandName = brandName;
        this.model = model;
        this.fullName = fullName;
        this.description = description;
        this.deleted = deleted;
        this.price = price;
        this.image = image;
        this.image1 = image1;
        this.image2 = image2;
        this.image3 = image3;
    }
    
    

    public Product(int productId, String categoryName, String brandName, String model, String fullName, long price, int stock) {
        this.productId = productId;
        this.categoryName = categoryName;
        this.brandName = brandName;
        this.model = model;
        this.fullName = fullName;
        this.price = price;
        this.stock = stock;
    }

    public Product(int productId, int brandId, int categoryId, String model, String fullName, String description, int deleted, long price, String image, int quantity, int stock) {
        this.productId = productId;
        this.brandId = brandId;
        this.categoryId = categoryId;
        this.model = model;
        this.fullName = fullName;
        this.description = description;
        this.deleted = deleted;
        this.price = price;
        this.image = image;
        this.quantity = quantity;
        this.stock = stock;
    }
    
    public Product(int productId, int brandId, int categoryId, String model, String fullName, String description, int deleted, long price, String image) {
        this.productId = productId;
        this.brandId = brandId;
        this.categoryId = categoryId;
        this.model = model;
        this.fullName = fullName;
        this.description = description;
        this.deleted = deleted;
        this.price = price;
        this.image = image;
    }

    public Product(int productId, String categoryName, String brandName, String fullName, long price, int quantity, int deleted) {
        this.productId = productId;
        this.categoryName = categoryName;
        this.brandName = brandName;
        this.fullName = fullName;
        this.deleted = deleted;
        this.price = price;
        this.quantity = quantity;
    }

    public Product(int productId, String categoryName, String brandName, String model, String fullName, String description, int deleted, long price, String image, int stock) {
        this.productId = productId;
        this.categoryName = categoryName;
        this.brandName = brandName;
        this.model = model;
        this.fullName = fullName;
        this.description = description;
        this.deleted = deleted;
        this.price = price;
        this.image = image;
        this.stock = stock;
    }

    public Product(String model, String fullName, String description, long price, String image, int stock) {
        this.model = model;
        this.fullName = fullName;
        this.description = description;
        this.price = price;
        this.image = image;
        this.stock = stock;
    }

    public Product(int productId, String categoryName, String brandName, String fullName, long price, String image, int quantity, int deleted) {
        this.productId = productId;
        this.categoryName = categoryName;
        this.brandName = brandName;
        this.fullName = fullName;
        this.deleted = deleted;
        this.price = price;
        this.image = image;
        this.quantity = quantity;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public Product(int productId, String fullName, String description, int deleted, long price, String image) {
        this.productId = productId;
        this.fullName = fullName;
        this.description = description;
        this.deleted = deleted;
        this.price = price;
        this.image = image;
    }

    public Product(int productId, String model, String fullName, int deleted, long price, int stock) {
        this.productId = productId;
        this.model = model;
        this.fullName = fullName;
        this.deleted = deleted;
        this.price = price;
        this.stock = stock;
    }

    //create product
    public Product(int productId, String categoryName, String brandName, String model, String fullName, int deleted, long price) {
        this.productId = productId;
        this.categoryName = categoryName;
        this.brandName = brandName;
        this.model = model;
        this.fullName = fullName;
        this.deleted = deleted;
        this.price = price;
    }

    //getproduct by id
    public Product(int productId, String categoryName, String brandName, String model, String fullName, String description, int deleted, long price, String image, String image1, String image2, String image3, int stock) {
        this.productId = productId;
        this.categoryName = categoryName;
        this.brandName = brandName;
        this.model = model;
        this.fullName = fullName;
        this.description = description;
        this.deleted = deleted;
        this.price = price;
        this.image = image;
        this.image1 = image1;
        this.image2 = image2;
        this.image3 = image3;
        this.stock = stock;
    }

    public Product(int productId, String categoryName, String brandName, String model, String fullName, String description, int deleted, long price, String image, String image1, String image2, String image3) {
        this.productId = productId;
        this.categoryName = categoryName;
        this.brandName = brandName;
        this.model = model;
        this.fullName = fullName;
        this.description = description;
        this.deleted = deleted;
        this.price = price;
        this.image = image;
        this.image1 = image1;
        this.image2 = image2;
        this.image3 = image3;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getBrandId() {
        return brandId;
    }

    public void setBrandId(int brandId) {
        this.brandId = brandId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDeleted() {
        return deleted;
    }

    public void setDeleted(int deleted) {
        this.deleted = deleted;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getImage3() {
        return image3;
    }

    public void setImage3(String image3) {
        this.image3 = image3;
    }

    public HashMap<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(HashMap<String, String> attributes) {
        this.attributes = attributes;
    }

    public String getStatus() {
        if (deleted == 1) {
            return "Deleted";
        }
        return "Activate";
    }

    public String getPriceFormatted() {
        Locale vietnam = new Locale("vi", "VN");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(vietnam);
        return currencyFormatter.format(price);
    }

    public List<AttributeDetail> getAttributeDetails() {
        return attributeDetails;
    }

    public void setAttributeDetails(List<AttributeDetail> attributeDetails) {
        this.attributeDetails = attributeDetails;
    }
    
}
