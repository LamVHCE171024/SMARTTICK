/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAOs;

import DB.DBContext;
import Models.Attribute;
import Models.AttributeDetail;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author 
 */
public class AttributeDAO {

    DBContext db = new DBContext();
    Connection connector = db.getConnection();

    public AttributeDetail getAttributeInforByID(int attributeId) {
        AttributeDetail attribute = null;
        String query = "SELECT AttributeID, ProductID, AttributeInfor FROM AttributeDetails WHERE AttributeID = ?";

        try {
            PreparedStatement ps = connector.prepareStatement(query);
            ps.setInt(1, attributeId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                attribute = new AttributeDetail(
                        rs.getInt("AttributeID"),
                        rs.getInt("ProductID"),
                        rs.getString("AttributeInfor"),
                        rs.getString("AttributeName")
                );
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return attribute;
    }

    public List<Attribute> getAttributesByCategoryID(int categoryId) {
        List<Attribute> attributes = new ArrayList<>();
        String query = "SELECT AttributeID, CategoryID, Name FROM Attributes WHERE CategoryID = ?";

        try {
            PreparedStatement ps = connector.prepareStatement(query);
            ps.setInt(1, categoryId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Attribute attribute = new Attribute(
                        rs.getInt("AttributeID"),
                        rs.getInt("CategoryID"),
                        rs.getString("Name")
                );
                attributes.add(attribute);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return attributes;
    }

    public List<AttributeDetail> getAttributesByProductID(int productId) {
        List<AttributeDetail> attributes = new ArrayList<>();
        String query = "SELECT ad.AttributeID, ad.ProductID, ad.AttributeInfor, a.Name AS AttributeName "
                + "FROM AttributeDetails ad "
                + "JOIN Attributes a ON ad.AttributeID = a.AttributeID "
                + "WHERE ad.ProductID = ?";

        try {
            PreparedStatement ps = connector.prepareStatement(query);
            ps.setInt(1, productId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                AttributeDetail attribute = new AttributeDetail(
                        rs.getInt("AttributeID"),
                        rs.getInt("ProductID"),
                        rs.getString("AttributeInfor"),
                        rs.getString("AttributeName") // Ensure this is captured
                );
                attributes.add(attribute);
            }
            while (rs.next()) {
                System.out.println("Attributes size: " + attributes.size());
                for (AttributeDetail attr : attributes) {
                    System.out.println("Attribute Name: " + attr.getAttributeName() + " - Info: " + attr.getAttributeInfor());
                }

            }

        } catch (SQLException ex) {
            Logger.getLogger(ProductDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return attributes;
    }
    public static void main(String[] args) {
        AttributeDAO a = new AttributeDAO();
        List<Attribute> l = a.getAttributesByCategoryID(1);
        for (Attribute attribute : l) {
            System.out.println(attribute.getAttributeName());
        }
    }

}
