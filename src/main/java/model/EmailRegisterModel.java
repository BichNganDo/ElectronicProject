package model;

import client.MysqlClient;
import common.ErrorCode;
import entity.category_news.CategoryNews;
import entity.email_register.Email;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import servlets.client.EmailRegister;

public class EmailRegisterModel {
    
    private static final MysqlClient dbClient = MysqlClient.getMysqlCli();
    private final String NAMETABLE = "email_register";
    public static EmailRegisterModel INSTANCE = new EmailRegisterModel();
    
    public List<Email> getSliceEmail(int offset, int limit, String searchQuery) {
        List<Email> resultListEmail = new ArrayList<>();
        Connection conn = null;
        try {
            conn = dbClient.getDbConnection();
            if (null == conn) {
                return resultListEmail;
                
            }
            String sql = "SELECT * FROM `" + NAMETABLE
                    + "` WHERE 1 = 1";
            
            if (StringUtils.isNotEmpty(searchQuery)) {
                sql = sql + " AND email LIKE ? ";
            }
            
            sql = sql + " LIMIT ? OFFSET ? ";
            
            PreparedStatement ps = conn.prepareStatement(sql);
            int param = 1;
            
            if (StringUtils.isNotEmpty(searchQuery)) {
                ps.setString(param++, "%" + searchQuery + "%");
            }
            ps.setInt(param++, limit);
            ps.setInt(param++, offset);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Email email = new Email();
                email.setId(rs.getInt("id"));
                email.setEmail(rs.getString("email"));
                
                resultListEmail.add(email);
            }
            
            return resultListEmail;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            dbClient.releaseDbConnection(conn);
        }
        
        return resultListEmail;
    }
    
    public int getTotalEmail(String searchQuery) {
        int total = 0;
        Connection conn = null;
        try {
            conn = dbClient.getDbConnection();
            if (null == conn) {
                return total;
            }
            String sql = "SELECT COUNT(id) AS total FROM `" + NAMETABLE + "` WHERE 1=1";
            if (StringUtils.isNotEmpty(searchQuery)) {
                sql = sql + " AND email LIKE ? ";
            }
            
            PreparedStatement ps = conn.prepareStatement(sql);
            int param = 1;
            
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
    
    public Email getEmailByID(int id) {
        Email result = new Email();
        Connection conn = null;
        try {
            conn = dbClient.getDbConnection();
            if (null == conn) {
                return result;
            }
            PreparedStatement getEmailByIdStmt = conn.prepareStatement("SELECT * FROM `" + NAMETABLE + "` WHERE id = ? ");
            getEmailByIdStmt.setInt(1, id);
            
            ResultSet rs = getEmailByIdStmt.executeQuery();
            
            if (rs.next()) {
                result.setId(rs.getInt("id"));
                result.setEmail(rs.getString("email"));
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
    
    public int addEmail(String email) {
        Connection conn = null;
        Boolean isExistEmail = INSTANCE.isExistEmail(email);
        if (isExistEmail == true) {
            return ErrorCode.EXIST_ACCOUNT.getValue();
        }
        try {
            conn = dbClient.getDbConnection();
            if (null == conn) {
                return ErrorCode.CONNECTION_FAIL.getValue();
            }
            
            PreparedStatement addStmt = conn.prepareStatement("INSERT INTO `" + NAMETABLE + "` (email) VALUES (?)");
            addStmt.setString(1, email);
            int rs = addStmt.executeUpdate();
            return rs;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            dbClient.releaseDbConnection(conn);
        }
        
        return ErrorCode.FAIL.getValue();
    }
    
    public int editEmail(int id, String email) {
        Connection conn = null;
        try {
            conn = dbClient.getDbConnection();
            if (null == conn) {
                return ErrorCode.CONNECTION_FAIL.getValue();
            }
            
            PreparedStatement editStmt = conn.prepareStatement("UPDATE `" + NAMETABLE + "` SET email = ? WHERE id = ? ");
            editStmt.setString(1, email);
            editStmt.setInt(2, id);
            
            int rs = editStmt.executeUpdate();
            
            return rs;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            dbClient.releaseDbConnection(conn);
        }
        return ErrorCode.FAIL.getValue();
    }
    
    public int deleteEmail(int id) {
        Connection conn = null;
        try {
            conn = dbClient.getDbConnection();
            if (null == conn) {
                return ErrorCode.CONNECTION_FAIL.getValue();
            }
            Email emailById = getEmailByID(id);
            if (emailById.getId() == 0) {
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
