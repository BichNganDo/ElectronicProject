package model;

import client.MysqlClient;
import common.ErrorCode;
import entity.supplier.Supplier;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

public class SupplierModel {

    private static final MysqlClient dbClient = MysqlClient.getMysqlCli();
    private final String NAMETABLE = "supplier";
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    public static SupplierModel INSTANCE = new SupplierModel();

    public List<Supplier> getSliceSupplier(int offset, int limit, String searchQuery) {
        List<Supplier> resultListSupplier = new ArrayList<>();
        Connection conn = null;
        try {
            conn = dbClient.getDbConnection();
            if (null == conn) {
                return resultListSupplier;

            }

            String sql = "SELECT * FROM `" + NAMETABLE
                    + "` WHERE 1 = 1";

            if (StringUtils.isNotEmpty(searchQuery)) {
                sql = sql + " AND name LIKE ? ";
                sql = sql + " OR phone LIKE ? ";
                sql = sql + " OR email LIKE ? ";

            }
            sql = sql + " LIMIT ? OFFSET ? ";
            PreparedStatement ps = conn.prepareStatement(sql);
            int param = 1;

            if (StringUtils.isNotEmpty(searchQuery)) {
                ps.setString(param++, "%" + searchQuery + "%");
                ps.setString(param++, "%" + searchQuery + "%");
                ps.setString(param++, "%" + searchQuery + "%");
            }
            ps.setInt(param++, limit);
            ps.setInt(param++, offset);
            ResultSet rs = ps.executeQuery();

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

    public int getTotalSupplier(String searchQuery) {
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
                sql = sql + " OR phone LIKE ? ";
                sql = sql + " OR email LIKE ? ";
            }
            PreparedStatement ps = conn.prepareStatement(sql);
            int param = 1;

            if (StringUtils.isNotEmpty(searchQuery)) {
                ps.setString(param++, "%" + searchQuery + "%");
                ps.setString(param++, "%" + searchQuery + "%");
                ps.setString(param++, "%" + searchQuery + "%");
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

    public Supplier getSupplierByID(int id) {
        Supplier result = new Supplier();
        Connection conn = null;
        try {
            conn = dbClient.getDbConnection();
            if (null == conn) {
                return result;
            }
            PreparedStatement getSupplierByIdStmt = conn.prepareStatement("SELECT * FROM `" + NAMETABLE + "` WHERE id = ? ");
            getSupplierByIdStmt.setInt(1, id);

            ResultSet rs = getSupplierByIdStmt.executeQuery();

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

            PreparedStatement addStmt = conn.prepareStatement("INSERT INTO `" + NAMETABLE + "` (name, address, phone, email, fax, created_date, updated_date) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?)");
            addStmt.setString(1, name);
            addStmt.setString(2, address);
            addStmt.setString(3, phone);
            addStmt.setString(4, email);
            addStmt.setString(5, fax);
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

    public int editSupplier(int id, String name, String address, String phone, String email, String fax) {
        Connection conn = null;
        try {
            conn = dbClient.getDbConnection();
            if (null == conn) {
                return ErrorCode.CONNECTION_FAIL.getValue();
            }

            PreparedStatement editStmt = conn.prepareStatement("UPDATE `" + NAMETABLE + "` SET name = ?, address = ?, phone = ?, "
                    + "email = ?, fax = ?, updated_date = ? WHERE id = ? ");
            editStmt.setString(1, name);
            editStmt.setString(2, address);
            editStmt.setString(3, phone);
            editStmt.setString(4, email);
            editStmt.setString(5, fax);
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

    public int deleteSUpplier(int id) {
        Connection conn = null;
        try {
            conn = dbClient.getDbConnection();
            if (null == conn) {
                return ErrorCode.CONNECTION_FAIL.getValue();
            }

            Supplier supplierByID = getSupplierByID(id);
            if (supplierByID.getId() == 0) {
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
