package entity.admin;

import java.util.List;

public class ListAdmin {

    private int total;
    private List<Admin> listAdmin;
    private int itemPerPage;

    public ListAdmin() {
    }

    public ListAdmin(int total, List<Admin> listAdmin, int itemPerPage) {
        this.total = total;
        this.listAdmin = listAdmin;
        this.itemPerPage = itemPerPage;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<Admin> getListAdmin() {
        return listAdmin;
    }

    public void setListAdmin(List<Admin> listAdmin) {
        this.listAdmin = listAdmin;
    }

    public int getItemPerPage() {
        return itemPerPage;
    }

    public void setItemPerPage(int itemPerPage) {
        this.itemPerPage = itemPerPage;
    }

}
