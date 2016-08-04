package fi.oulu.tol.esde23.ohapclient23;

import com.opimobi.ohap.Item;

/**
 * Created by backd00red on 24/03/16.
 */
public final class ItemUtility {

    public static String getPath(Item item) {
        String path = String.format("%s : %d", item.getName(), item.getId());
        Item parent = item.getParent();

        while (parent != null) {
            path += String.format("/%s : %d", parent.getName(), parent.getId());
            parent = parent.getParent();
        }
        return path;
    }
}
