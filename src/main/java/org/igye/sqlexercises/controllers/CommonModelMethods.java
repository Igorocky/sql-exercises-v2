package org.igye.sqlexercises.controllers;

import org.igye.sqlexercises.data.DaoUtils;
import org.igye.sqlexercises.data.NodeDao;
import org.igye.sqlexercises.htmlforms.EditNodeForm;
import org.igye.sqlexercises.htmlforms.SessionData;
import org.igye.sqlexercises.model.Paragraph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.igye.sqlexercises.common.OutlineUtils.getCurrentUser;

@Service
public class CommonModelMethods {
    @Autowired
    private SessionData sessionData;
    @Autowired
    private DaoUtils daoUtils;
    @Autowired
    private NodeDao nodeDao;

    public void initModel(Model model) {
        model.addAttribute("sessionData", sessionData);
        model.addAttribute("currentUser", sessionData.getCurrentUser().getName());
        model.addAttribute("isAdmin", daoUtils.isAdmin(getCurrentUser()));
    }

    public void prepareModelForEditNode(Model model, EditNodeForm form) {
        initModel(model);
        if (form.getParentId() != null) {
            addPath(model, nodeDao.getParagraphById(form.getParentId()));
        } else {
            addPath(model, null);
        }
        model.addAttribute("form", form);
    }

    public void addPath(Model model, Paragraph paragraph) {
        List<Paragraph> path = buildPath(paragraph);
        Collections.reverse(path);
        model.addAttribute("path", path);
    }

    private List<Paragraph> buildPath(Paragraph paragraph) {
        if (paragraph == null) {
            return new ArrayList<>();
        } else {
            List<Paragraph> res = new ArrayList<>();
            res.add(paragraph);
            res.addAll(buildPath((Paragraph) paragraph.getParentNode()));
            return res;
        }
    }

}
