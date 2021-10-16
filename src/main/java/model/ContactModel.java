package model;

import client.MysqlClient;
import common.ErrorCode;
import entity.contact.ContactClient;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

public class ContactModel {

    private static final MysqlClient dbClient = MysqlClient.getMysqlCli();
    private final String NAMETABLE = "contact";
    public static ContactModel INSTANCE = new ContactModel();

    public List<ContactClient> getSliceContact(int offset, int limit, String searchQuery, int searchStatus) {
        List<ContactClient> resultListContact = new ArrayList<>();
        Connection conn = null;
        try {
            conn = dbClient.getDbConnection();
            if (null == conn) {
                return resultListContact;

            }
            String sql = "SELECT * FROM `" + NAMETABLE + "` "
                    + " WHERE 1=1";

            if (StringUtils.isNotEmpty(searchQuery)) {
                sql = sql + " AND (name LIKE ? OR email LIKE ? OR phone LIKE ?) ";
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
                ps.setString(param++, "%" + searchQuery + "%");
            }

            if (searchStatus > 0) {
                ps.setInt(param++, searchStatus);
            }

            ps.setInt(param++, limit);
            ps.setInt(param++, offset);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ContactClient contactClient = new ContactClient();
                contactClient.setId(rs.getInt("id"));
                contactClient.setName(rs.getString("name"));
                contactClient.setEmail(rs.getString("email"));
                contactClient.setPhone(rs.getString("phone"));
                contactClient.setMessage(rs.getString("message"));
                contactClient.setStatus(rs.getInt("status"));

                resultListContact.add(contactClient);
            }

            return resultListContact;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            dbClient.releaseDbConnection(conn);
        }

        return resultListContact;
    }

    public int getTotalContact(String searchQuery, int searchStatus) {
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

            if (StringUtils.isNotEmpty(searchQuery)) {
                sql = sql + " AND email LIKE ? ";
            }

            if (StringUtils.isNotEmpty(searchQuery)) {
                sql = sql + " AND phone LIKE ? ";
            }
            if (searchStatus > 0) {
                sql = sql + " AND status = ? ";
            }

            PreparedStatement ps = conn.prepareStatement(sql);
            int param = 1;

            if (StringUtils.isNotEmpty(searchQuery)) {
                ps.setString(param++, "%" + searchQuery + "%");
            }

            if (StringUtils.isNotEmpty(searchQuery)) {
                ps.setString(param++, "%" + searchQuery + "%");
            }

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

    public ContactClient getContactByID(int id) {
        ContactClient result = new ContactClient();
        Connection conn = null;
        try {
            conn = dbClient.getDbConnection();
            if (null == conn) {
                return result;
            }
            PreparedStatement getContactByIdStmt = conn.prepareStatement("SELECT * FROM `" + NAMETABLE + "` WHERE id = ? ");
            getContactByIdStmt.setInt(1, id);

            ResultSet rs = getContactByIdStmt.executeQuery();

            if (rs.next()) {
                result.setId(rs.getInt("id"));
                result.setName(rs.getString("name"));
                result.setEmail(rs.getString("email"));
                result.setPhone(rs.getString("phone"));
                result.setStatus(rs.getInt("status"));
                result.setMessage(rs.getString("message"));
            }

            return result;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            dbClient.releaseDbConnection(conn);
        }
        return result;
    }

    public int addContact(String name, String email, String phone, String message, int status) {
        Connection conn = null;
        try {
            conn = dbClient.getDbConnection();
            if (null == conn) {
                return ErrorCode.CONNECTION_FAIL.getValue();
            }

            PreparedStatement addStmt = conn.prepareStatement("INSERT INTO `" + NAMETABLE + "` (name, email, phone, message, status)"
                    + " VALUES (?, ?, ?, ?, ?)");
            addStmt.setString(1, name);
            addStmt.setString(2, email);
            addStmt.setString(3, phone);
            addStmt.setString(4, message);
            addStmt.setInt(5, status);

            int rs = addStmt.executeUpdate();

            return rs;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            dbClient.releaseDbConnection(conn);
        }

        return ErrorCode.FAIL.getValue();
    }

    public int editContact(int id, String name, String email, String phone, String message, int status) {
        Connection conn = null;
        try {
            conn = dbClient.getDbConnection();
            if (null == conn) {
                return ErrorCode.CONNECTION_FAIL.getValue();
            }

            PreparedStatement editStmt = conn.prepareStatement("UPDATE `" + NAMETABLE + "` SET name = ?, email = ?, phone = ?, "
                    + "message = ?, status = ? WHERE id = ? ");
            editStmt.setString(1, name);
            editStmt.setString(2, email);
            editStmt.setString(3, phone);
            editStmt.setString(4, message);
            editStmt.setInt(5, status);
            editStmt.setInt(6, id);

            int rs = editStmt.executeUpdate();

            return rs;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            dbClient.releaseDbConnection(conn);
        }
        return ErrorCode.FAIL.getValue();
    }

    public int deleteContact(int id) {
        Connection conn = null;
        try {
            conn = dbClient.getDbConnection();
            if (null == conn) {
                return ErrorCode.CONNECTION_FAIL.getValue();
            }

            ContactClient contactByID = getContactByID(id);
            if (contactByID.getId() == 0) {
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
