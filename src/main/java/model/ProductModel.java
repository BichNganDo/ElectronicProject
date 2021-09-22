package model;

import client.MysqlClient;
import common.ErrorCode;
import entity.category_product.CategoryProduct;
import entity.product.FilterProduct;
import entity.product.Product;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

public class ProductModel {

    private static final MysqlClient dbClient = MysqlClient.getMysqlCli();
    private final String NAMETABLE = "product";
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    public static ProductModel INSTANCE = new ProductModel();

    public List<Product> getSliceProduct(FilterProduct filterProduct) {
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

            if (StringUtils.isNotEmpty(filterProduct.getSearchQuery())) {
                sql = sql + " AND product.name LIKE ? ";
            }

            if (filterProduct.getSearchCate() > 0) {
                sql = sql + " AND product.id_cate = ? ";
            }

            if (filterProduct.getSearchSupplier() > 0) {
                sql = sql + " AND product.id_supplier = ? ";
            }

            if (filterProduct.getSearchProperty() > 0) {
                sql = sql + " AND product.property = ? ";
            }

            if (filterProduct.getOrderView() > 0) {
                sql = sql + " ORDER BY product.view DESC ";
            }
            sql = sql + " LIMIT ? OFFSET ? ";
            PreparedStatement ps = conn.prepareStatement(sql);
            int param = 1;

            if (StringUtils.isNotEmpty(filterProduct.getSearchQuery())) {
                ps.setString(param++, "%" + filterProduct.getSearchQuery() + "%");
            }

            if (filterProduct.getSearchCate() > 0) {
                ps.setInt(param++, filterProduct.getSearchCate());
            }

            if (filterProduct.getSearchSupplier() > 0) {
                ps.setInt(param++, filterProduct.getSearchSupplier());
            }

            if (filterProduct.getSearchProperty() > 0) {
                ps.setInt(param++, filterProduct.getSearchProperty());
            }

            ps.setInt(param++, filterProduct.getLimit());
            ps.setInt(param++, filterProduct.getOffset());
            ResultSet rs = ps.executeQuery();

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
                product.setProperty(rs.getInt("property"));
                product.setView(rs.getInt("view"));

                int discount = ProductModel.INSTANCE.percentDiscount(product.getPrice(), product.getPrice_sale());
                product.setDiscount(discount);

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

    public int percentDiscount(int price, int priceSale) {
        int tu = (price - priceSale);
        int percentDiscount = 0;
        if (price > 0) {
            percentDiscount = tu * 100 / price;
        }
        return percentDiscount;
    }

    public Map<Integer, List<Product>> multiGetProductByCategory(List<CategoryProduct> listCategory) {
        Map<Integer, List<Product>> result = new HashMap<>();
        for (int i = 0; i < listCategory.size(); i++) {
            CategoryProduct category = listCategory.get(i);
            int id = category.getId();

            FilterProduct filterProduct = new FilterProduct();
            filterProduct.setOffset(0);
            filterProduct.setLimit(16);
            filterProduct.setSearchCate(id);
            filterProduct.setSearchProperty(0);
            filterProduct.setSearchQuery("");
            filterProduct.setSearchSupplier(0);
            filterProduct.setOrderView(0);

            List<Product> listProduct = ProductModel.INSTANCE.getSliceProduct(filterProduct);
            result.put(id, listProduct);
        }
        return result;

    }

    public int getTotalProduct(FilterProduct filterProduct) {
        int total = 0;
        Connection conn = null;
        try {
            conn = dbClient.getDbConnection();
            if (null == conn) {
                return total;
            }

            String sql = "SELECT COUNT(id) AS total FROM `" + NAMETABLE + "` WHERE 1 = 1";
            if (StringUtils.isNotEmpty(filterProduct.getSearchQuery())) {
                sql = sql + " AND product.name LIKE ? ";
            }

            if (filterProduct.getSearchCate() > 0) {
                sql = sql + " AND product.id_cate = ? ";
            }

            if (filterProduct.getSearchSupplier() > 0) {
                sql = sql + " AND product.id_supplier = ? ";
            }

            if (filterProduct.getSearchProperty() > 0) {
                sql = sql + " AND product.property = ? ";
            }

            if (filterProduct.getOrderView() > 0) {
                sql = sql + " ORDER BY product.view DESC ";
            }

            PreparedStatement ps = conn.prepareStatement(sql);
            int param = 1;

            if (StringUtils.isNotEmpty(filterProduct.getSearchQuery())) {
                ps.setString(param++, "%" + filterProduct.getSearchQuery() + "%");
            }

            if (filterProduct.getSearchCate() > 0) {
                ps.setInt(param++, filterProduct.getSearchCate());
            }

            if (filterProduct.getSearchSupplier() > 0) {
                ps.setInt(param++, filterProduct.getSearchSupplier());
            }

            if (filterProduct.getSearchProperty() > 0) {
                ps.setInt(param++, filterProduct.getSearchProperty());
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

    public Product getProductByID(int id) {
        Product result = new Product();
        Connection conn = null;
        try {
            conn = dbClient.getDbConnection();
            if (null == conn) {
                return result;
            }
            PreparedStatement getProductByIdStmt = conn.prepareStatement("SELECT * FROM `" + NAMETABLE + "` WHERE id = ? ");
            getProductByIdStmt.setInt(1, id);

            ResultSet rs = getProductByIdStmt.executeQuery();

            if (rs.next()) {
                result.setId(rs.getInt("id"));
                result.setId_cate(rs.getInt("id_cate"));
                result.setId_supplier(rs.getInt("id_supplier"));
                result.setName(rs.getString("name"));
                result.setPrice(rs.getInt("price"));
                result.setPrice_sale(rs.getInt("price_sale"));
                result.setQuantity(rs.getInt("quantity"));
                result.setImage_url(rs.getString("image_url"));
                result.setContent(rs.getString("content"));
                result.setWarranty(rs.getString("warranty"));
                result.setProperty(rs.getInt("property"));
                result.setView(rs.getInt("view"));
                int discount = ProductModel.INSTANCE.percentDiscount(result.getPrice(), result.getPrice_sale());
                result.setDiscount(discount);

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

    public int addProduct(int id_cate, int id_supplier, String name, int price, int price_sale, int quantity,
            String image_url, String content, String warranty, int property) {
        Connection conn = null;
        try {
            conn = dbClient.getDbConnection();
            if (null == conn) {
                return ErrorCode.CONNECTION_FAIL.getValue();
            }
            PreparedStatement addStmt = conn.prepareStatement("INSERT INTO `" + NAMETABLE + "` (id_cate, id_supplier, name, price, price_sale, "
                    + "quantity, image_url, content, warranty, property, created_date, view ) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 0)");
            addStmt.setInt(1, id_cate);
            addStmt.setInt(2, id_supplier);
            addStmt.setString(3, name);
            addStmt.setInt(4, price);
            addStmt.setInt(5, price_sale);
            addStmt.setInt(6, quantity);
            addStmt.setString(7, image_url);
            addStmt.setString(8, content);
            addStmt.setString(9, warranty);
            addStmt.setInt(10, property);
            addStmt.setString(11, System.currentTimeMillis() + "");

            int rs = addStmt.executeUpdate();

            return rs;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            dbClient.releaseDbConnection(conn);
        }

        return ErrorCode.FAIL.getValue();
    }

    public int editProduct(int id, int id_cate, int id_supplier, String name, int price,
            int price_sale, int quantity, String image_url, String content, String warranty, int property) {
        Connection conn = null;
        try {
            conn = dbClient.getDbConnection();
            if (null == conn) {
                return ErrorCode.CONNECTION_FAIL.getValue();
            }
            PreparedStatement editStmt = conn.prepareStatement("UPDATE `" + NAMETABLE + "` SET id_cate = ?, id_supplier = ?, name = ?, "
                    + "price = ?, price_sale = ?, quantity = ?, image_url = ?, warranty = ?, property = ?, content = ? WHERE id = ? ");
            editStmt.setInt(1, id_cate);
            editStmt.setInt(2, id_supplier);
            editStmt.setString(3, name);
            editStmt.setInt(4, price);
            editStmt.setInt(5, price_sale);
            editStmt.setInt(6, quantity);
            editStmt.setString(7, image_url);
            editStmt.setString(8, warranty);
            editStmt.setInt(9, property);
            editStmt.setString(10, content);
            editStmt.setInt(11, id);
            int rs = editStmt.executeUpdate();

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
