package model;

import client.MysqlClient;
import common.ErrorCode;
import entity.email_register.Email;
import entity.info_buyer.InfoBuyer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

public class InfoBuyerModel {

    private static final MysqlClient dbClient = MysqlClient.getMysqlCli();
    private final String NAMETABLE = "info_buyer";
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    public static InfoBuyerModel INSTANCE = new InfoBuyerModel();

    public List<InfoBuyer> getSliceInfoBuyer(int offset, int limit, String searchQuery) {
        List<InfoBuyer> resultListInfoBuyer = new ArrayList<>();
        Connection conn = null;
        try {
            conn = dbClient.getDbConnection();
            if (null == conn) {
                return resultListInfoBuyer;

            }
            String sql = "SELECT * FROM `" + NAMETABLE
                    + "` WHERE 1 = 1";

            if (StringUtils.isNotEmpty(searchQuery)) {
                sql = sql + " AND (name LIKE ? OR email LIKE ? OR phone LIKE ?) ";
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
                InfoBuyer infoBuyer = new InfoBuyer();
                infoBuyer.setId(rs.getInt("id"));
                infoBuyer.setName(rs.getString("name"));
                infoBuyer.setEmail(rs.getString("email"));
                infoBuyer.setPhone(rs.getString("phone"));
                infoBuyer.setAddress(rs.getString("address"));
                infoBuyer.setNote(rs.getString("note"));

                long currentTimeMillis = rs.getLong("created_date");
                Date date = new Date(currentTimeMillis);
                String dateString = sdf.format(date);
                infoBuyer.setCreated_date(dateString);

                resultListInfoBuyer.add(infoBuyer);
            }

            return resultListInfoBuyer;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            dbClient.releaseDbConnection(conn);
        }

        return resultListInfoBuyer;
    }

    public int getTotalInfoBuyer(String searchQuery) {
        int total = 0;
        Connection conn = null;
        try {
            conn = dbClient.getDbConnection();
            if (null == conn) {
                return total;
            }
            String sql = "SELECT COUNT(id) AS total FROM `" + NAMETABLE + "` WHERE 1=1";
            if (StringUtils.isNotEmpty(searchQuery)) {
                sql = sql + " AND (name LIKE ? OR email LIKE ? OR phone LIKE ?) ";
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

    public InfoBuyer getInfoBuyerByID(int id) {
        InfoBuyer result = new InfoBuyer();
        Connection conn = null;
        try {
            conn = dbClient.getDbConnection();
            if (null == conn) {
                return result;
            }
            PreparedStatement getInfoBuyerByIdStmt = conn.prepareStatement("SELECT * FROM `" + NAMETABLE + "` WHERE id = ? ");
            getInfoBuyerByIdStmt.setInt(1, id);

            ResultSet rs = getInfoBuyerByIdStmt.executeQuery();

            if (rs.next()) {
                result.setId(rs.getInt("id"));
                result.setName(rs.getString("name"));
                result.setEmail(rs.getString("email"));
                result.setPhone(rs.getString("phone"));
                result.setAddress(rs.getString("address"));
                result.setNote(rs.getString("note"));

                long currentTimeMillis = rs.getLong("created_date");
                Date date = new Date(currentTimeMillis);
                String dateString = sdf.format(date);
                result.setCreated_date(dateString);
            }

            return result;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            dbClient.releaseDbConnection(conn);
        }
        return result;
    }

    public int addInfoBuyer(String name, String email, String phone, String address, String note) {
        Connection conn = null;
        try {
            conn = dbClient.getDbConnection();
            if (null == conn) {
                return ErrorCode.CONNECTION_FAIL.getValue();
            }

            PreparedStatement addStmt = conn.prepareStatement("INSERT INTO `" + NAMETABLE + "` (name, email, phone, address, note, created_date) "
                    + "VALUES (?, ?, ?, ?, ?, ?)");
            addStmt.setString(1, name);
            addStmt.setString(2, email);
            addStmt.setString(3, phone);
            addStmt.setString(4, address);
            addStmt.setString(5, note);
            addStmt.setString(6, System.currentTimeMillis() + "");
            int rs = addStmt.executeUpdate();
            return rs;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            dbClient.releaseDbConnection(conn);
        }

        return ErrorCode.FAIL.getValue();
    }

    public int editInfoBuyer(int id, String name, String email, String phone, String address, String note) {
        Connection conn = null;
        try {
            conn = dbClient.getDbConnection();
            if (null == conn) {
                return ErrorCode.CONNECTION_FAIL.getValue();
            }

            PreparedStatement editStmt = conn.prepareStatement("UPDATE `" + NAMETABLE + "` SET name = ?, email = ?, phone = ?, address = ?, note = ? "
                    + "WHERE id = ? ");
            editStmt.setString(1, name);
            editStmt.setString(2, email);
            editStmt.setString(3, phone);
            editStmt.setString(4, address);
            editStmt.setString(5, note);
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

    public int deleteInfoBuyer(int id) {
        Connection conn = null;
        try {
            conn = dbClient.getDbConnection();
            if (null == conn) {
                return ErrorCode.CONNECTION_FAIL.getValue();
            }
            InfoBuyer infoBuyerById = getInfoBuyerByID(id);
            if (infoBuyerById.getId() == 0) {
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
