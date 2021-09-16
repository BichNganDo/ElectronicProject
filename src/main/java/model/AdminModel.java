package model;

import client.MysqlClient;
import common.ErrorCode;
import entity.admin.Admin;
import helper.SecurityHelper;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

public class AdminModel {

    private static final MysqlClient dbClient = MysqlClient.getMysqlCli();
    private final String NAMETABLE = "admin";
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    public static AdminModel INSTANCE = new AdminModel();

    public List<Admin> getSliceAdmin(int offset, int limit, String searchQuery, int searchStatus) {
        List<Admin> resultListAdmin = new ArrayList<>();
        Connection conn = null;
        try {
            conn = dbClient.getDbConnection();
            if (null == conn) {
                return resultListAdmin;

            }
            String sql = "SELECT * FROM `" + NAMETABLE
                    + "` WHERE 1 = 1";

            if (StringUtils.isNotEmpty(searchQuery)) {
                sql = sql + " AND (name LIKE ? OR phone LIKE ?) ";
            }

            if (searchStatus > 0) {
                sql = sql + " AND status = ? ";
            }

            sql = sql + " LIMIT ? OFFSET ? ";

            PreparedStatement ps = conn.prepareStatement(sql);
            int param = 1;

            if (StringUtils.isNotEmpty(searchQuery)) {
                ps.setString(param++, "%" + searchQuery + "%");
                ps.setString(param++, "%" + searchQuery + "%");
            }

            if (searchStatus > 0) {
                ps.setInt(param++, searchStatus);
            }

            ps.setInt(param++, limit);
            ps.setInt(param++, offset);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Admin admin = new Admin();
                admin.setId(rs.getInt("id"));
                admin.setName(rs.getString("name"));
                admin.setRole(rs.getInt("role"));
                admin.setUsername(rs.getString("username"));
                admin.setPhone(rs.getString("phone"));
                admin.setPassword(rs.getString("password"));
                admin.setStatus(rs.getInt("status"));

                long currentTimeMillis = rs.getLong("created_date");
                Date date = new Date(currentTimeMillis);
                String dateString = sdf.format(date);
                admin.setCreated_date(dateString);

                long currentTimeUpdated = rs.getLong("updated_date");
                Date dateUpdated = new Date(currentTimeUpdated);
                String dateStringUpdated = sdf.format(dateUpdated);
                admin.setUpdated_date(dateStringUpdated);

                resultListAdmin.add(admin);
            }

            return resultListAdmin;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            dbClient.releaseDbConnection(conn);
        }

        return resultListAdmin;
    }

    public int getTotalAdmin(String searchQuery, int searchStatus) {
        int total = 0;
        Connection conn = null;
        try {
            conn = dbClient.getDbConnection();
            if (null == conn) {
                return total;
            }
            String sql = "SELECT COUNT(id) AS total FROM `" + NAMETABLE + "` WHERE 1=1";
            if (StringUtils.isNotEmpty(searchQuery)) {
                sql = sql + " AND (name LIKE ? OR phone LIKE ?) ";
            }

            if (searchStatus > 0) {
                sql = sql + " AND status = ? ";
            }

            PreparedStatement ps = conn.prepareStatement(sql);
            int param = 1;

            if (StringUtils.isNotEmpty(searchQuery)) {
                ps.setString(param++, "%" + searchQuery + "%");
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

    public Admin getAdminByID(int id) {
        Admin result = new Admin();
        Connection conn = null;
        try {
            conn = dbClient.getDbConnection();
            if (null == conn) {
                return result;
            }
            PreparedStatement getNewsByIdStmt = conn.prepareStatement("SELECT * FROM `" + NAMETABLE + "` WHERE id = ? ");
            getNewsByIdStmt.setInt(1, id);

            ResultSet rs = getNewsByIdStmt.executeQuery();

            if (rs.next()) {
                result.setId(rs.getInt("id"));
                result.setName(rs.getString("name"));
                result.setRole(rs.getInt("role"));
                result.setUsername(rs.getString("username"));
                result.setPhone(rs.getString("phone"));
                result.setPassword(rs.getString("password"));
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

    public boolean checkExistAccount(String phone) {
        Connection conn = null;
        try {
            conn = dbClient.getDbConnection();
            if (null == conn) {
                return false;
            }
            PreparedStatement checkPhoneStmt = conn.prepareStatement("SELECT * FROM `" + NAMETABLE + "` WHERE  phone = ?");
            checkPhoneStmt.setString(1, phone);
            ResultSet rs = checkPhoneStmt.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            dbClient.releaseDbConnection(conn);
        }

        return false;
    }

    public int addAdmin(String name, int role, String username, String phone, String password, int status) {
        Connection conn = null;
        try {
            conn = dbClient.getDbConnection();
            if (null == conn) {
                return ErrorCode.CONNECTION_FAIL.getValue();
            }
            if (checkExistAccount(phone)) {
                return ErrorCode.EXIST_ACCOUNT.getValue();
            }
            password = SecurityHelper.getMD5Hash(password);
            PreparedStatement addStmt = conn.prepareStatement("INSERT INTO `" + NAMETABLE + "` (name, role, username, phone, password, status,"
                    + "created_date, updated_date) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
            addStmt.setString(1, name);
            addStmt.setInt(2, role);
            addStmt.setString(3, username);
            addStmt.setString(4, phone);
            addStmt.setString(5, password);
            addStmt.setInt(6, status);
            addStmt.setString(7, System.currentTimeMillis() + "");
            addStmt.setString(8, System.currentTimeMillis() + "");

            int rs = addStmt.executeUpdate();

            return rs;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            dbClient.releaseDbConnection(conn);
        }

        return ErrorCode.FAIL.getValue();
    }

    public int editAdmin(int id, String name, int role, String username, String phone, String password, int status) {
        Connection conn = null;
        try {
            conn = dbClient.getDbConnection();
            if (null == conn) {
                return ErrorCode.CONNECTION_FAIL.getValue();
            }
            password = SecurityHelper.getMD5Hash(password);
            PreparedStatement editStmt = conn.prepareStatement("UPDATE `" + NAMETABLE + "` SET name = ?, role = ?, username = ?, phone = ?, password = ?, "
                    + "status = ?, updated_date = ? WHERE id = ? ");
            editStmt.setString(1, name);
            editStmt.setInt(2, role);
            editStmt.setString(3, username);
            editStmt.setString(4, phone);
            editStmt.setString(5, password);
            editStmt.setInt(6, status);
            editStmt.setString(7, System.currentTimeMillis() + "");
            editStmt.setInt(8, id);

            int rs = editStmt.executeUpdate();

            return rs;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            dbClient.releaseDbConnection(conn);
        }
        return ErrorCode.FAIL.getValue();
    }

    public int deleteAdmin(int id) {
        Connection conn = null;
        try {
            conn = dbClient.getDbConnection();
            if (null == conn) {
                return ErrorCode.CONNECTION_FAIL.getValue();
            }

            Admin adminByID = getAdminByID(id);
            if (adminByID.getId() == 0) {
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

    public boolean checkLogin(String phone, String password) {
        Connection conn = null;
        try {
            conn = dbClient.getDbConnection();
            if (null == conn) {
                return false;
            }
            PreparedStatement checkLogin = conn.prepareStatement("SELECT * FROM `" + NAMETABLE + "` WHERE phone = ? AND password = ?");
            checkLogin.setString(1, phone);
            checkLogin.setString(2, password);
            ResultSet rs = checkLogin.executeQuery();
            if (rs.next()) {
                if (rs.getString("phone") != null && !rs.getString("phone").trim().isEmpty()) {
                    return true;
                }
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            dbClient.releaseDbConnection(conn);
        }

        return false;
    }

}
