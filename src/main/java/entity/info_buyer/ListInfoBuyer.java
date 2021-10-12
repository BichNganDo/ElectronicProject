package entity.info_buyer;

import java.util.List;

public class ListInfoBuyer {

    private int total;
    private List<InfoBuyer> listInfoBuyer;
    private int itemPerPage;

    public ListInfoBuyer() {
    }

    public ListInfoBuyer(int total, List<InfoBuyer> listInfoBuyer, int itemPerPage) {
        this.total = total;
        this.listInfoBuyer = listInfoBuyer;
        this.itemPerPage = itemPerPage;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<InfoBuyer> getListInfoBuyer() {
        return listInfoBuyer;
    }

    public void setListInfoBuyer(List<InfoBuyer> listInfoBuyer) {
        this.listInfoBuyer = listInfoBuyer;
    }

    public int getItemPerPage() {
        return itemPerPage;
    }

    public void setItemPerPage(int itemPerPage) {
        this.itemPerPage = itemPerPage;
    }

}
