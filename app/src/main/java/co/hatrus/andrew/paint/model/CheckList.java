package co.hatrus.andrew.paint.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by user on 19.02.15.
 */
public class CheckList extends RealmObject {
    @PrimaryKey
    private int id;
    private String item;
    private boolean isChecked=false;

    public CheckList() {
    }

    public CheckList(String item) {
        super();
        this.item = item;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }
}
