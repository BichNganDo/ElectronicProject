package model;

import client.MysqlClient;
import common.ErrorCode;
import entity.news.News;
import entity.setting.Setting;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

public class SettingModel {

    private static final MysqlClient dbClient = MysqlClient.getMysqlCli();
    private final String NAMETABLE = "setting";
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    public static SettingModel INSTANCE = new SettingModel();

    public List<Setting> getSliceSetting(int offset, int limit, String searchQuery) {
        List<Setting> resultListSetting = new ArrayList<>();
        Connection conn = null;
        try {
            conn = dbClient.getDbConnection();
            if (null == conn) {
                return resultListSetting;

            }
            String sql = "SELECT * FROM `" + NAMETABLE
                    + "` WHERE 1 = 1";

            if (StringUtils.isNotEmpty(searchQuery)) {
                sql = sql + " AND key LIKE ? ";
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
                Setting setting = new Setting();
                setting.setId(rs.getInt("id"));
                setting.setKey(rs.getString("key"));
                setting.setValue(rs.getString("value"));

                resultListSetting.add(setting);
            }

            return resultListSetting;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            dbClient.releaseDbConnection(conn);
        }

        return resultListSetting;
    }

    public int getTotalSetting(String searchQuery) {
        int total = 0;
        Connection conn = null;
        try {
            conn = dbClient.getDbConnection();
            if (null == conn) {
                return total;
            }
            String sql = "SELECT COUNT(id) AS total FROM `" + NAMETABLE + "` WHERE 1 = 1";
            if (StringUtils.isNotEmpty(searchQuery)) {
                sql = sql + " AND key LIKE ? ";
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

    public Setting getSettingById(int id) {
        Setting result = new Setting();
        Connection conn = null;
        try {
            conn = dbClient.getDbConnection();
            if (null == conn) {
                return result;
            }
            PreparedStatement getNewsByTypeStmt = conn.prepareStatement("SELECT * FROM `" + NAMETABLE + "` WHERE id = ? ");
            getNewsByTypeStmt.setInt(1, id);

            ResultSet rs = getNewsByTypeStmt.executeQuery();

            if (rs.next()) {
                result.setId(rs.getInt("id"));
                result.setKey(rs.getString("key"));
                result.setValue(rs.getString("value"));
            }

            return result;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            dbClient.releaseDbConnection(conn);
        }
        return result;
    }

    public Setting getSettingByKey(String key) {
        Setting result = new Setting();
        Connection conn = null;
        try {
            conn = dbClient.getDbConnection();
            if (null == conn) {
                return result;
            }
            PreparedStatement getNewsByTypeStmt = conn.prepareStatement("SELECT * FROM `" + NAMETABLE + "` WHERE key = ? ");
            getNewsByTypeStmt.setString(1, key);

            ResultSet rs = getNewsByTypeStmt.executeQuery();

            if (rs.next()) {
                result.setId(rs.getInt("id"));
                result.setKey(rs.getString("key"));
                result.setValue(rs.getString("value"));
            }

            return result;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            dbClient.releaseDbConnection(conn);
        }
        return result;
    }

    public int addSetting(String key, String value) {
        Setting SettingByKey = INSTANCE.getSettingByKey(key);
        if (SettingByKey.getId() > 0) {
            return ErrorCode.EXIST_ACCOUNT.getValue();
        }
        Connection conn = null;
        try {
            conn = dbClient.getDbConnection();
            if (null == conn) {
                return ErrorCode.CONNECTION_FAIL.getValue();
            }

            PreparedStatement addStmt = conn.prepareStatement("INSERT INTO `" + NAMETABLE + "` (`key`, `value`)"
                    + " VALUES (?, ?)");
            addStmt.setString(1, key);
            addStmt.setString(2, value);

            int rs = addStmt.executeUpdate();

            return rs;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            dbClient.releaseDbConnection(conn);
        }

        return ErrorCode.FAIL.getValue();
    }

    public int editSetting(int id, String key, String value) {
        Connection conn = null;
        try {
            conn = dbClient.getDbConnection();
            if (null == conn) {
                return ErrorCode.CONNECTION_FAIL.getValue();
            }

            PreparedStatement editStmt = conn.prepareStatement("UPDATE `" + NAMETABLE + "` SET `key` = ?, `value` = ? "
                    + " WHERE id = ? ");
            editStmt.setString(1, key);
            editStmt.setString(2, value);
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

    public int deleteSetting(int id) {
        Connection conn = null;
        try {
            conn = dbClient.getDbConnection();
            if (null == conn) {
                return ErrorCode.CONNECTION_FAIL.getValue();
            }
            Setting settingById = getSettingById(id);
            if (settingById.getId() == 0) {
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
