package model;

import client.MysqlClient;
import common.ErrorCode;
import entity.product.Product;
import entity.supplier.Supplier;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

public class ProductModel {

    private static final MysqlClient dbClient = MysqlClient.getMysqlCli();
    private final String NAMETABLE = "product";
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    public static ProductModel INSTANCE = new ProductModel();

    public List<Product> getSliceProduct(int offset, int limit, String searchQuery, int searchCate, int searchSupplier) {
        List<Product> resultListProduct = new ArrayList<>();
        Connection conn = null;
        try {
            conn = dbClient.getDbConnection();
            if (null == conn) {
                return resultListProduct;

            }
            String sql = "SELECT product.*, category_product.name AS `category`, "
                    + "supplier.name AS `supplier` "
                    + "FROM product "
                    + "INNER JOIN category_product ON product.id_cate= category_product.id "
                    + "INNER JOIN supplier ON product.id_supplier = supplier.id WHERE 1=1";

            if (StringUtils.isNotEmpty(searchQuery)) {
                sql = sql + " AND product.name LIKE '%" + searchQuery + "%' ";
            }

            if (searchCate > 0) {
                sql = sql + " AND product.id_cate = " + searchCate;
            }

            if (searchSupplier > 0) {
                sql = sql + " AND product.id_supplier = " + searchSupplier;
            }
            sql = sql + " LIMIT " + limit + " OFFSET " + offset;

            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                Product product = new Product();
                product.setId(rs.getInt("id"));
                product.setId_cate(rs.getInt("id_cate"));
                product.setId_supplier(rs.getInt("id_supplier"));
                product.setCategory(rs.getString("category"));
                product.setSupplier(rs.getString("supplier"));
                product.setName(rs.getString("name"));
                product.setPrice(rs.getInt("price"));
                product.setPrice_sale(rs.getInt("price_sale"));
                product.setQuantity(rs.getInt("quantity"));
                product.setImage_url(rs.getString("image_url"));
                product.setContent(rs.getString("content"));
                product.setWarranty(rs.getString("warranty"));
                product.setHot(rs.getString("hot"));

                long currentTimeMillis = rs.getLong("created_date");
                Date date = new Date(currentTimeMillis);
                String dateString = sdf.format(date);
                product.setCreated_date(dateString);

                resultListProduct.add(product);
            }

            return resultListProduct;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            dbClient.releaseDbConnection(conn);
        }

        return resultListProduct;
    }

    public int getTotalProduct(String searchQuery, int searchCate, int searchSupplier) {
        int total = 0;
        Connection conn = null;
        try {
            conn = dbClient.getDbConnection();
            if (null == conn) {
                return total;
            }
            String sql = "SELECT COUNT(id) AS total FROM `" + NAMETABLE + "` WHERE 1 = 1";
            if (StringUtils.isNotEmpty(searchQuery)) {
                sql = sql + " AND name LIKE '%" + searchQuery + "%' ";
            }
            if (searchCate > 0) {
                sql = sql + " AND product.id_cate = " + searchCate;
            }

            if (searchSupplier > 0) {
                sql = sql + " AND product.id_supplier = " + searchSupplier;
            }
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

    public Product getProductByID(int id) {
        Product result = new Product();
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
<<<<<<< HEAD
                result.setId(rs.getInt("id"));
=======
>>>>>>> master
                result.setId_cate(rs.getInt("id_cate"));
                result.setId_supplier(rs.getInt("id_supplier"));
                result.setName(rs.getString("name"));
                result.setPrice(rs.getInt("price"));
                result.setPrice_sale(rs.getInt("price_sale"));
                result.setQuantity(rs.getInt("quantity"));
                result.setImage_url(rs.getString("image_url"));
                result.setContent(rs.getString("content"));
                result.setWarranty(rs.getString("warranty"));
                result.setHot(rs.getString("hot"));

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

    public int addProduct(int id_cate, int id_supplier, String name, int price, int price_sale, int quantity, String image_url, String content, String warranty, String hot) {
        Connection conn = null;
        try {
            conn = dbClient.getDbConnection();
            if (null == conn) {
                return ErrorCode.CONNECTION_FAIL.getValue();
            }
            String sql = "INSERT INTO `" + NAMETABLE + "`"
                    + "(`id_cate`, `id_supplier`, `name`, `price`, `price_sale`, "
                    + "`quantity`, `image_url`, `content`, `warranty`, `hot`, `created_date`) "
                    + "VALUES "
                    + "('" + id_cate + "', '" + id_supplier + "','" + name + "', '" + price + "', '" + price_sale + "', '" + quantity + "',"
                    + " '" + image_url + "', '" + content + "', '" + warranty + "', '" + hot + "', '" + System.currentTimeMillis() + "')";

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
<<<<<<< HEAD

    public int editProduct(int id, int id_cate, int id_supplier, String name, int price,
            int price_sale, int quantity, String image_url, String content, String warranty, String hot) {
        Connection conn = null;
        try {
            conn = dbClient.getDbConnection();
            if (null == conn) {
                return ErrorCode.CONNECTION_FAIL.getValue();
            }

            String sql = "UPDATE `" + NAMETABLE + "` "
                    + "SET `id_cate`='" + id_cate + "' , "
                    + "`id_supplier`='" + id_supplier + "', "
                    + "`name`='" + name + "', "
                    + "`price`='" + price + "', "
                    + "`price_sale`='" + price_sale + "', "
                    + "`quantity`='" + quantity + "', "
                    + "`image_url`='" + image_url + "', "
                    + "`warranty`='" + warranty + "', "
                    + "`hot`='" + hot + "', "
                    + "`content`='" + content + "'"
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

    public int deleteProduct(int id) {
        Connection conn = null;
        try {
            conn = dbClient.getDbConnection();
            if (null == conn) {
                return ErrorCode.CONNECTION_FAIL.getValue();
            }

            Product productByID = getProductByID(id);
            if (productByID.getId() == 0) {
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
=======
>>>>>>> master
}
