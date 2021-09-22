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

    public List<CategoryProduct> getSliceCategory(int offset, int limit, String searchQuery, int searchStatus, int hot) {
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
                sql = sql + " AND c.name LIKE ? ";
            }

            if (searchStatus > 0) {
                sql = sql + " AND c.status = ? ";
            }

            if (hot > 0) {
                sql = sql + " AND c.hot = 1 ";
            }

            sql = sql + " ORDER BY `orders` DESC LIMIT ? OFFSET ? ";

            PreparedStatement ps = conn.prepareStatement(sql);
            int param = 1;

            if (StringUtils.isNotEmpty(searchQuery)) {
                ps.setString(param++, "%" + searchQuery + "%");
            }

            if (searchStatus > 0) {
                ps.setInt(param++, searchStatus);
            }

            ps.setInt(param++, limit);
            ps.setInt(param++, offset);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                CategoryProduct categoryProduct = new CategoryProduct();
                categoryProduct.setId(rs.getInt("id"));
                categoryProduct.setName(rs.getString("name"));
                categoryProduct.setId_parent(rs.getInt("id_parent"));
                categoryProduct.setOrders(rs.getInt("orders"));
                categoryProduct.setStatus(rs.getInt("status"));
                categoryProduct.setCate_parent(rs.getString("cate_parent"));
                categoryProduct.setHot(rs.getInt("hot"));

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

    public int getTotalCategory(String searchQuery, int searchStatus) {
        int total = 0;
        Connection conn = null;
        try {
            conn = dbClient.getDbConnection();
            if (null == conn) {
                return total;
            }
            String sql = "SELECT COUNT(id) AS total FROM `" + NAMETABLE + "` WHERE 1 = 1";

            if (StringUtils.isNotEmpty(searchQuery)) {
                sql = sql + " AND name LIKE ? ";
            }

            if (searchStatus > 0) {
                sql = sql + " AND status = ? ";
            }

            PreparedStatement ps = conn.prepareStatement(sql);
            int param = 1;

            if (StringUtils.isNotEmpty(searchQuery)) {
                ps.setString(param++, "%" + searchQuery + "%");
            }

            if (searchStatus > 0) {
                ps.setInt(param++, searchStatus);
            }

            ResultSet rs = ps.executeQuery();

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
            PreparedStatement getCategoryByIdStmt = conn.prepareStatement("SELECT * FROM `" + NAMETABLE + "` WHERE id = ? ");
            getCategoryByIdStmt.setInt(1, id);

            ResultSet rs = getCategoryByIdStmt.executeQuery();

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

    public int addCategory(String name, int id_parent, int orders, int status, int hot) {
        Connection conn = null;
        try {
            conn = dbClient.getDbConnection();
            if (null == conn) {
                return ErrorCode.CONNECTION_FAIL.getValue();
            }

            PreparedStatement addStmt = conn.prepareStatement("INSERT INTO `" + NAMETABLE + "` (name, id_parent, orders, status, hot,"
                    + "created_date, updated_date) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?)");
            addStmt.setString(1, name);
            addStmt.setInt(2, id_parent);
            addStmt.setInt(3, orders);
            addStmt.setInt(4, status);
            addStmt.setInt(5, hot);
            addStmt.setString(6, System.currentTimeMillis() + "");
            addStmt.setString(7, System.currentTimeMillis() + "");

            int rs = addStmt.executeUpdate();

            return rs;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            dbClient.releaseDbConnection(conn);
        }

        return ErrorCode.FAIL.getValue();
    }

    public int editCategory(int id, String name, int id_parent, int orders, int status, int hot) {
        Connection conn = null;
        try {
            conn = dbClient.getDbConnection();
            if (null == conn) {
                return ErrorCode.CONNECTION_FAIL.getValue();
            }

            PreparedStatement editStmt = conn.prepareStatement("UPDATE `" + NAMETABLE + "` SET name = ?, id_parent = ?, orders = ?, "
                    + "status = ?, hot = ?, updated_date = ? WHERE id = ? ");
            editStmt.setString(1, name);
            editStmt.setInt(2, id_parent);
            editStmt.setInt(3, orders);
            editStmt.setInt(4, status);
            editStmt.setInt(5, hot);
            editStmt.setString(6, System.currentTimeMillis() + "");
            editStmt.setInt(7, id);

            int rs = editStmt.executeUpdate();

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
            PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM `" + NAMETABLE + "` WHERE id = ?");
            deleteStmt.setInt(1, id);
            int rs = deleteStmt.executeUpdate();

            return rs;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            dbClient.releaseDbConnection(conn);
        }
        return ErrorCode.FAIL.getValue();
    }
}
