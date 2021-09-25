package entity.product;

public class FilterProduct {

    private int offset;
    private int limit;
    private String searchQuery;
    private int searchCate;
    private int searchSupplier;
    private int searchProperty;
    private int orderView;
    private int sortByPrice;

    public FilterProduct() {
    }

    public FilterProduct(int offset, int limit, String searchQuery, int searchCate, int searchSupplier, int searchProperty, int orderView) {
        this.offset = offset;
        this.limit = limit;
        this.searchQuery = searchQuery;
        this.searchCate = searchCate;
        this.searchSupplier = searchSupplier;
        this.searchProperty = searchProperty;
        this.orderView = orderView;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    public int getSearchCate() {
        return searchCate;
    }

    public void setSearchCate(int searchCate) {
        this.searchCate = searchCate;
    }

    public int getSearchSupplier() {
        return searchSupplier;
    }

    public void setSearchSupplier(int searchSupplier) {
        this.searchSupplier = searchSupplier;
    }

    public int getSearchProperty() {
        return searchProperty;
    }

    public void setSearchProperty(int searchProperty) {
        this.searchProperty = searchProperty;
    }

    public int getOrderView() {
        return orderView;
    }

    public void setOrderView(int orderView) {
        this.orderView = orderView;
    }

    public int getSortByPrice() {
        return sortByPrice;
    }

    public void setSortByPrice(int sortByPrice) {
        this.sortByPrice = sortByPrice;
    }

}
