/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

/**
 *
 * @author 
 */
public class AttributeDetail {

    private int attributeId;
    private int productId;
    private String attributeInfor;
    private String attributeName;

    public AttributeDetail() {
    }

    public AttributeDetail(int attributeId, int productId, String attributeInfor, String attributeName) {
        this.attributeId = attributeId;
        this.productId = productId;
        this.attributeInfor = attributeInfor;
        this.attributeName = attributeName;
    }

    public AttributeDetail(int attributeId, int productId, String attributeInfor) {
        this.attributeId = attributeId;
        this.productId = productId;
        this.attributeInfor = attributeInfor;
    }
    
    

    public int getAttributeId() {
        return attributeId;
    }

    public void setAttributeId(int attributeId) {
        this.attributeId = attributeId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getAttributeInfor() {
        return attributeInfor;
    }

    public void setAttributeInfor(String attributeInfor) {
        this.attributeInfor = attributeInfor;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

}
