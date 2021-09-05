package entity.supplier;

import java.util.List;

public class ListSupplier {

    private int total;
    private List<Supplier> listSupplier;
    private int itemPerPage;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<Supplier> getListSupplier() {
        return listSupplier;
    }

    public void setListSupplier(List<Supplier> listSupplier) {
        this.listSupplier = listSupplier;
    }

    public int getItemPerPage() {
        return itemPerPage;
    }

    public void setItemPerPage(int itemPerPage) {
        this.itemPerPage = itemPerPage;
    }

    public ListSupplier(int total, List<Supplier> listSupplier, int itemPerPage) {
        this.total = total;
        this.listSupplier = listSupplier;
        this.itemPerPage = itemPerPage;
    }

    public ListSupplier() {
    }

}
