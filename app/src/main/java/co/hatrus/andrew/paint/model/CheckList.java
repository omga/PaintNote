package co.hatrus.andrew.paint.model;

import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

/**
 * Created by user on 19.02.15.
 */
@RealmClass
public class CheckList extends RealmObject {
    @PrimaryKey
    private String id;
    private String item;
    private boolean isChecked=false;

    public CheckList() {
        id = UUID.randomUUID().toString();
    }

    public CheckList(String item) {
        super();
        id = UUID.randomUUID().toString();
        this.item = item;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
