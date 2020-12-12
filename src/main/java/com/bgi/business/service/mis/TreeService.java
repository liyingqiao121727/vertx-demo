package com.bgi.business.service.mis;

import com.bgi.business.model.mis.Dictionary;
import com.bgi.vtx.annotation.Component;
import com.bgi.business.model.mis.Dictionary;
import com.bgi.vtx.annotation.Component;
import io.vertx.core.json.JsonArray;

import javax.swing.tree.TreeModel;
import java.util.ArrayList;
import java.util.List;

@Component("treeService")
public class TreeService {

    public static <T extends Dictionary> List<T> bulidTreeModel(List<T> treeList, Object root) {
        String rootTemp = root.toString();
        List<T> trees = new ArrayList<T>();
        for (T tree : treeList) {
            if (rootTemp.equals(tree.getParentId())) {
                trees.add(tree);
            }

            for (T it : treeList) {
                if (it.getParentId().equals(tree.getId())) {
                    if (tree.getChildren() == null) {
                        tree.setChildren(new JsonArray());
                    }
                    tree.add(it);
                }
            }
        }
        return trees;
    }
}
