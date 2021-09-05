package model;

import client.MysqlClient;
import common.ErrorCode;
import entity.category_product.CategoryProduct;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

public class CategoryProductModel {
    
    private static final MysqlClient dbClient = MysqlClient.getMysqlCli();
    private final String NAMETABLE = "category_product";
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    public static CategoryProductModel INSTANCE = new CategoryProductModel();
    
    public List<CategoryProduct> getSliceCategory(int offset, int limit, String searchQuery, int searchStatus) {
        List<CategoryProduct> resultListCategory = new ArrayList<>();
        Connection conn = null;
        try {
            conn = dbClient.getDbConnection();
            if (null == conn) {
                return resultListCategory;
                
            }
            String sql = "SELECT c.*, p.name cate_parent FROM `" + NAMETABLE + "` "
                    + "c LEFT JOIN `" + NAMETABLE + "` p ON c.id_parent = p.id "
                    + " WHERE 1=1";
            
            if (StringUtils.isNotEmpty(searchQuery)) {
                sql = sql + " AND c.name LIKE '%" + searchQuery + "%' ";
            }
            
            if (searchStatus > 0) {
                sql = sql + " AND c.status = " + searchStatus;
            }
            
            sql = sql + " ORDER BY `orders` DESC LIMIT " + limit + " OFFSET " + offset;
            
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            
            while (rs.next()) {
                CategoryProduct categoryProduct = new CategoryProduct();
                categoryProduct.setId(rs.getInt("id"));
                categoryProduct.setName(rs.getString("name"));
                categoryProduct.setId_parent(rs.getInt("id_parent"));
                categoryProduct.setOrders(rs.getInt("orders"));
                categoryProduct.setStatus(rs.getInt("status"));
                categoryProduct.setCate_parent(rs.getString("cate_parent"));
                
                long currentTimeMillis = rs.getLong("created_date");
                Date date = new Date(currentTimeMillis);
                String dateString = sdf.format(date);
                categoryProduct.setCreated_date(dateString);
                
                long currentTimeUpdated = rs.getLong("updated_date");
                Date dateUpdated = new Date(currentTimeUpdated);
                String dateStringUpdated = sdf.format(dateUpdated);
                categoryProduct.setUpdated_date(dateStringUpdated);
                
                resultListCategory.add(categoryProduct);
            }
            
            return resultListCategory;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            dbClient.releaseDbConnection(conn);
        }
        
        return resultListCategory;
    }
    
    public int getTotalCategory() {
        int total = 0;
        Connection conn = null;
        try {
            conn = dbClient.getDbConnection();
            if (null == conn) {
                return total;
            }
            String sql = "SELECT COUNT(id) AS total FROM category_product";
            
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            
            if (rs.next()) {
                total = rs.getInt("total");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            dbClient.releaseDbConnection(conn);
        }
        
        return total;
    }
    
    public CategoryProduct getCategoryByID(int id) {
        CategoryProduct result = new CategoryProduct();
        Connection conn = null;
        try {
            conn = dbClient.getDbConnection();
            if (null == conn) {
                return result;
            }
            String sql = "SELECT * FROM `" + NAMETABLE + "` WHERE `id`='" + id + "'";
            
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            
            if (rs.next()) {
                result.setId(rs.getInt("id"));
                result.setName(rs.getString("name"));
                result.setId_parent(rs.getInt("id_parent"));
                result.setOrders(rs.getInt("orders"));
                result.setStatus(rs.getInt("status"));
                
                long currentTimeMillis = rs.getLong("created_date");
                Date date = new Date(currentTimeMillis);
                String dateString = sdf.format(date);
                result.setCreated_date(dateString);
                
                long currentTimeUpdated = rs.getLong("updated_date");
                Date dateUpdated = new Date(currentTimeUpdated);
                String dateStringUpdated = sdf.format(dateUpdated);
                result.setUpdated_date(dateStringUpdated);
            }
            
            return result;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            dbClient.releaseDbConnection(conn);
        }
        return result;
    }
    
    public int addCategory(String name, int id_parent, int orders, int status) {
        Connection conn = null;
        try {
            conn = dbClient.getDbConnection();
            if (null == conn) {
                return ErrorCode.CONNECTION_FAIL.getValue();
            }
            String sql = "INSERT INTO `" + NAMETABLE + "`"
                    + "(`name`, `id_parent`, `orders`, `status`, `created_date`, `updated_date`) "
                    + "VALUES "
                    + "('" + name + "', '" + id_parent + "','" + orders + "', '" + status + "','" + System.currentTimeMillis() + "', '" + System.currentTimeMillis() + "')";
            
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            int rs = preparedStatement.executeUpdate();
            
            return rs;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            dbClient.releaseDbConnection(conn);
        }
        
        return ErrorCode.FAIL.getValue();
    }
    
    public int editCategory(int id, String name, int id_parent, int orders, int status) {
        Connection conn = null;
        try {
            conn = dbClient.getDbConnection();
            if (null == conn) {
                return ErrorCode.CONNECTION_FAIL.getValue();
            }
            
            String sql = "UPDATE `" + NAMETABLE + "` "
                    + "SET `name`='" + name + "' , "
                    + "`id_parent`='" + id_parent + "', "
                    + "`orders`='" + orders + "', "
                    + "`status`='" + status + "', "
                    + "`updated_date`='" + System.currentTimeMillis() + "'"
                    + "WHERE `id`='" + id + "'";
            
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            int rs = preparedStatement.executeUpdate();
            
            return rs;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            dbClient.releaseDbConnection(conn);
        }
        return ErrorCode.FAIL.getValue();
    }
    
    public int deleteCategory(int id) {
        Connection conn = null;
        try {
            conn = dbClient.getDbConnection();
            if (null == conn) {
                return ErrorCode.CONNECTION_FAIL.getValue();
            }
            
            CategoryProduct categoryByID = getCategoryByID(id);
            if (categoryByID.getId() == 0) {
                return ErrorCode.NOT_EXIST.getValue();
            }
            String sql = "DELETE FROM `" + NAMETABLE + "` WHERE `id`='" + id + "'";
            
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            int rs = preparedStatement.executeUpdate();
            
            return rs;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            dbClient.releaseDbConnection(conn);
        }
        return ErrorCode.FAIL.getValue();
    }
}
