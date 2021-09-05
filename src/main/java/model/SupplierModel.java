package model;

import client.MysqlClient;
import common.ErrorCode;
import entity.category_product.CategoryProduct;
import entity.supplier.Supplier;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SupplierModel {

    private static final MysqlClient dbClient = MysqlClient.getMysqlCli();
    private final String NAMETABLE = "supplier";
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    public static SupplierModel INSTANCE = new SupplierModel();

    public List<Supplier> getSliceSupplier(int offset, int limit) {
        List<Supplier> resultListSupplier = new ArrayList<>();
        Connection conn = null;
        try {
            conn = dbClient.getDbConnection();
            if (null == conn) {
                return resultListSupplier;

            }
            String sql = "SELECT * FROM `" + NAMETABLE + "` LIMIT " + limit + " OFFSET " + offset;

            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                Supplier supplier = new Supplier();
                supplier.setId(rs.getInt("id"));
                supplier.setName(rs.getString("name"));
                supplier.setAddress(rs.getString("address"));
                supplier.setPhone(rs.getString("phone"));
                supplier.setEmail(rs.getString("email"));
                supplier.setFax(rs.getString("fax"));

                long currentTimeMillis = rs.getLong("created_date");
                Date date = new Date(currentTimeMillis);
                String dateString = sdf.format(date);
                supplier.setCreated_date(dateString);

                long currentTimeUpdated = rs.getLong("updated_date");
                Date dateUpdated = new Date(currentTimeUpdated);
                String dateStringUpdated = sdf.format(dateUpdated);
                supplier.setUpdated_date(dateStringUpdated);

                resultListSupplier.add(supplier);
            }

            return resultListSupplier;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            dbClient.releaseDbConnection(conn);
        }

        return resultListSupplier;
    }

    public int getTotalSupplier() {
        int total = 0;
        Connection conn = null;
        try {
            conn = dbClient.getDbConnection();
            if (null == conn) {
                return total;
            }
            String sql = "SELECT COUNT(id) AS total FROM `" + NAMETABLE;

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

    public Supplier getSupplierByID(int id) {
        Supplier result = new Supplier();
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
                result.setAddress(rs.getString("address"));
                result.setPhone(rs.getString("phone"));
                result.setEmail(rs.getString("email"));
                result.setFax(rs.getString("fax"));

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

    public int addSupplier(String name, String address, String phone, String email, String fax) {
        Connection conn = null;
        try {
            conn = dbClient.getDbConnection();
            if (null == conn) {
                return ErrorCode.CONNECTION_FAIL.getValue();
            }
            String sql = "INSERT INTO `" + NAMETABLE + "`"
                    + "(`name`, `address`, `phone`, `email`, `created_date`, `updated_date`) "
                    + "VALUES "
                    + "('" + name + "', '" + address + "','" + phone + "', '" + email + "','" + System.currentTimeMillis() + "', '" + System.currentTimeMillis() + "')";

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

//    public int editCategory(int id, String name, int id_parent, int orders, int status) {
//        Connection conn = null;
//        try {
//            conn = dbClient.getDbConnection();
//            if (null == conn) {
//                return ErrorCode.CONNECTION_FAIL.getValue();
//            }
//
//            String sql = "UPDATE `" + NAMETABLE + "` "
//                    + "SET `name`='" + name + "' , "
//                    + "`id_parent`='" + id_parent + "', "
//                    + "`orders`='" + orders + "', "
//                    + "`status`='" + status + "', "
//                    + "`updated_date`='" + System.currentTimeMillis() + "'"
//                    + "WHERE `id`='" + id + "'";
//
//            PreparedStatement preparedStatement = conn.prepareStatement(sql);
//            int rs = preparedStatement.executeUpdate();
//
//            return rs;
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        } finally {
//            dbClient.releaseDbConnection(conn);
//        }
//        return ErrorCode.FAIL.getValue();
//    }
//
//    public int deleteCategory(int id) {
//        Connection conn = null;
//        try {
//            conn = dbClient.getDbConnection();
//            if (null == conn) {
//                return ErrorCode.CONNECTION_FAIL.getValue();
//            }
//
//            CategoryProduct categoryByID = getCategoryByID(id);
//            if (categoryByID.getId() == 0) {
//                return ErrorCode.NOT_EXIST.getValue();
//            }
//            String sql = "DELETE FROM `" + NAMETABLE + "` WHERE `id`='" + id + "'";
//
//            PreparedStatement preparedStatement = conn.prepareStatement(sql);
//            int rs = preparedStatement.executeUpdate();
//
//            return rs;
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        } finally {
//            dbClient.releaseDbConnection(conn);
//        }
//        return ErrorCode.FAIL.getValue();
//    }
}
