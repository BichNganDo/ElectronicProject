package model;

import client.MysqlClient;
import common.ErrorCode;
import entity.admin.Admin;
import entity.email_register.Email;
import entity.user_register.UserRegister;
import helper.SecurityHelper;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

public class RegisterModel {

    private static final MysqlClient dbClient = MysqlClient.getMysqlCli();
    private final String NAMETABLE = "register";
    public static RegisterModel INSTANCE = new RegisterModel();

    public List<UserRegister> getSliceRegister(int offset, int limit, String searchQuery) {
        List<UserRegister> resultListUserRegister = new ArrayList<>();
        Connection conn = null;
        try {
            conn = dbClient.getDbConnection();
            if (null == conn) {
                return resultListUserRegister;

            }
            String sql = "SELECT * FROM `" + NAMETABLE
                    + "` WHERE 1 = 1";

            if (StringUtils.isNotEmpty(searchQuery)) {
                sql = sql + " AND (name LIKE ? OR email LIKE ?) ";
            }

            sql = sql + " LIMIT ? OFFSET ? ";

            PreparedStatement ps = conn.prepareStatement(sql);
            int param = 1;

            if (StringUtils.isNotEmpty(searchQuery)) {
                ps.setString(param++, "%" + searchQuery + "%");
                ps.setString(param++, "%" + searchQuery + "%");
            }
            ps.setInt(param++, limit);
            ps.setInt(param++, offset);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                UserRegister userRegister = new UserRegister();
                userRegister.setId(rs.getInt("id"));
                userRegister.setName(rs.getString("name"));
                userRegister.setEmail(rs.getString("email"));
                userRegister.setPassword(rs.getString("password"));
                userRegister.setRe_password(rs.getString("re_password"));

                resultListUserRegister.add(userRegister);
            }

            return resultListUserRegister;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            dbClient.releaseDbConnection(conn);
        }

        return resultListUserRegister;
    }

    public int getTotalUserRegister(String searchQuery) {
        int total = 0;
        Connection conn = null;
        try {
            conn = dbClient.getDbConnection();
            if (null == conn) {
                return total;
            }
            String sql = "SELECT COUNT(id) AS total FROM `" + NAMETABLE + "` WHERE 1=1";
            if (StringUtils.isNotEmpty(searchQuery)) {
                sql = sql + " AND name LIKE ? ";
            }

            if (StringUtils.isNotEmpty(searchQuery)) {
                sql = sql + " AND email LIKE ? ";
            }

            PreparedStatement ps = conn.prepareStatement(sql);
            int param = 1;

            if (StringUtils.isNotEmpty(searchQuery)) {
                ps.setString(param++, "%" + searchQuery + "%");
            }

            if (StringUtils.isNotEmpty(searchQuery)) {
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

    public UserRegister getUserRegisterByID(int id) {
        UserRegister result = new UserRegister();
        Connection conn = null;
        try {
            conn = dbClient.getDbConnection();
            if (null == conn) {
                return result;
            }
            PreparedStatement getUserRegisterByIdStmt = conn.prepareStatement("SELECT * FROM `" + NAMETABLE + "` WHERE id = ? ");
            getUserRegisterByIdStmt.setInt(1, id);

            ResultSet rs = getUserRegisterByIdStmt.executeQuery();

            if (rs.next()) {
                result.setId(rs.getInt("id"));
                result.setName(rs.getString("name"));
                result.setEmail(rs.getString("email"));
                result.setPassword(rs.getString("password"));
                result.setRe_password(rs.getString("re_password"));
            }

            return result;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            dbClient.releaseDbConnection(conn);
        }
        return result;
    }

    public boolean isExistEmail(String email) {
        Connection conn = null;
        try {
            conn = dbClient.getDbConnection();
            if (null == conn) {
                return false;
            }

            PreparedStatement isExistEmailStmt = conn.prepareStatement("SELECT * FROM `" + NAMETABLE + "` WHERE `email` = ?");
            isExistEmailStmt.setString(1, email);

            ResultSet rs = isExistEmailStmt.executeQuery();
            if (rs.next()) {
                return true;
            }

            return false;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            dbClient.releaseDbConnection(conn);
        }

        return false;
    }

    public int addUserRegister(String name, String email, String password, String rePassword) {
        Connection conn = null;
        Boolean isExistEmail = INSTANCE.isExistEmail(email);
        if (isExistEmail == true) {
            return ErrorCode.EXIST_ACCOUNT.getValue();
        }
        if (!password.equals(rePassword)) {
            return ErrorCode.INVALID_DATA.getValue();
        }
        password = SecurityHelper.getMD5Hash(password);
        rePassword = SecurityHelper.getMD5Hash(rePassword);
        try {
            conn = dbClient.getDbConnection();
            if (null == conn) {
                return ErrorCode.CONNECTION_FAIL.getValue();
            }

            PreparedStatement addStmt = conn.prepareStatement("INSERT INTO `" + NAMETABLE + "` (name, email, password, re_password) "
                    + "VALUES (?, ?, ?, ?)");
            addStmt.setString(1, name);
            addStmt.setString(2, email);
            addStmt.setString(3, password);
            addStmt.setString(4, rePassword);
            int rs = addStmt.executeUpdate();
            return rs;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            dbClient.releaseDbConnection(conn);
        }

        return ErrorCode.FAIL.getValue();
    }

    public int editUserRegister(int id, String name, String email) {
        Connection conn = null;
        try {
            conn = dbClient.getDbConnection();
            if (null == conn) {
                return ErrorCode.CONNECTION_FAIL.getValue();
            }

            PreparedStatement editStmt = conn.prepareStatement("UPDATE `" + NAMETABLE + "` SET name = ?, email = ? "
                    + "WHERE id = ? ");
            editStmt.setString(1, name);
            editStmt.setString(2, email);
            editStmt.setInt(3, id);

            int rs = editStmt.executeUpdate();

            return rs;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            dbClient.releaseDbConnection(conn);
        }
        return ErrorCode.FAIL.getValue();
    }

    public UserRegister checkLogin(String email, String password) {
        UserRegister userRegister = new UserRegister();
        Connection conn = null;
        try {
            conn = dbClient.getDbConnection();
            if (null == conn) {
                return userRegister;
            }
            PreparedStatement checkLoginStmt = conn.prepareStatement("SELECT * FROM `" + NAMETABLE + "` WHERE email = ? AND password = ?");
            checkLoginStmt.setString(1, email);
            checkLoginStmt.setString(2, password);
            ResultSet rs = checkLoginStmt.executeQuery();
            if (rs.next()) {
                userRegister.setId(rs.getInt("id"));
                userRegister.setName(rs.getString("name"));
                userRegister.setEmail(rs.getString("email"));
                userRegister.setPassword(rs.getString("password"));
                userRegister.setRe_password(rs.getString("re_password"));
            }

            return userRegister;

        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            dbClient.releaseDbConnection(conn);
        }

        return userRegister;
    }
}
