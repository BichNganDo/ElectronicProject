package entity.product;

public class Product {

    private int id;
    private int id_cate;
    private int id_supplier;
    private String category;
    private String supplier;
    private String name;
    private int price;
    private int price_sale;
    private int quantity;
    private String image_url;
    private String content;
    private String warranty;
    private Property property;
    private String created_date;

    public Product() {
    }

    public Product(int id, int id_cate, int id_supplier, String category, String supplier,
            String name, int price, int price_sale, int quantity, String image_url, String content,
            String warranty, Property property, String created_date) {
        this.id = id;
        this.id_cate = id_cate;
        this.id_supplier = id_supplier;
        this.category = category;
        this.supplier = supplier;
        this.name = name;
        this.price = price;
        this.price_sale = price_sale;
        this.quantity = quantity;
        this.image_url = image_url;
        this.content = content;
        this.warranty = warranty;
        this.property = property;
        this.created_date = created_date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_cate() {
        return id_cate;
    }

    public void setId_cate(int id_cate) {
        this.id_cate = id_cate;
    }

    public int getId_supplier() {
        return id_supplier;
    }

    public void setId_supplier(int id_supplier) {
        this.id_supplier = id_supplier;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getPrice_sale() {
        return price_sale;
    }

    public void setPrice_sale(int price_sale) {
        this.price_sale = price_sale;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getWarranty() {
        return warranty;
    }

    public void setWarranty(String warranty) {
        this.warranty = warranty;
    }

    public Property getProperty() {
        return property;
    }

    public void setProperty(Property property) {
        this.property = property;
    }

    public void setProperty(int numberProperty) {
        this.property = new Property(numberProperty);
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    private static class Property {

        private boolean hot;
        private boolean productNew;
        private boolean promo;

        public Property(int numberProperty) {
            if ((numberProperty & 1) > 0) {
                hot = true;
            }
            if ((numberProperty & 2) > 0) {
                productNew = true;
            }
            if ((numberProperty & 4) > 0) {
                promo = true;
            }

        }

        public Property(boolean hot, boolean productNew, boolean promo) {
            this.hot = hot;
            this.productNew = productNew;
            this.promo = promo;
        }

        public boolean isHot() {
            return hot;
        }

        public void setHot(boolean hot) {
            this.hot = hot;
        }

        public boolean isProductNew() {
            return productNew;
        }

        public void setProductNew(boolean productNew) {
            this.productNew = productNew;
        }

        public boolean isPromo() {
            return promo;
        }

        public void setPromo(boolean promo) {
            this.promo = promo;
        }

        public static void main(String[] args) {
            System.out.println(2 & 1);
        }

    }

}
