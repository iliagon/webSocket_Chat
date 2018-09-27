package Models;

import java.util.ArrayList;

public class Members extends JsonModel {
private ArrayList<String> list;

    public Members() {
        super("members");
    }

    public ArrayList<String> getList() {
        return list;
    }

    public Members setList(ArrayList<String> list) {
        this.list = list;
        return this;
    }
}
