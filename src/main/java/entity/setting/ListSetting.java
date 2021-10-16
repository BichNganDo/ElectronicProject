package entity.setting;

import java.util.List;

public class ListSetting {

    private int total;
    private List<Setting> listSetting;
    private int itemPerPage;

    public ListSetting() {
    }

    public ListSetting(int total, List<Setting> listSetting, int itemPerPage) {
        this.total = total;
        this.listSetting = listSetting;
        this.itemPerPage = itemPerPage;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<Setting> getListSetting() {
        return listSetting;
    }

    public void setListSetting(List<Setting> listSetting) {
        this.listSetting = listSetting;
    }

    public int getItemPerPage() {
        return itemPerPage;
    }

    public void setItemPerPage(int itemPerPage) {
        this.itemPerPage = itemPerPage;
    }

}
