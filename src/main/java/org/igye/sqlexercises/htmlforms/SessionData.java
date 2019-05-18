package org.igye.sqlexercises.htmlforms;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.igye.sqlexercises.data.NodeDao;
import org.igye.sqlexercises.model.User;
import org.igye.sqlexercises.selection.Selection;

@NoArgsConstructor
public class SessionData {
    @Getter @Setter
    private Selection selection;
    private User user;
    @Getter @Setter
    private LearnTextData learnTextData = new LearnTextData();
    @Getter @Setter
    private CyclicRandom cyclicRandom = new CyclicRandom();
    @Getter @Setter
    private LearnNodesData learnNodesData;

    public SessionData(NodeDao nodeDao) {
        learnNodesData = new LearnNodesData(nodeDao);
    }

    public synchronized User getCurrentUser() {
        return user;
    }
}
