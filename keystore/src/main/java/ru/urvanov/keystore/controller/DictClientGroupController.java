package ru.urvanov.keystore.controller;

import java.io.Serializable;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ru.urvanov.keystore.controller.domain.ObjectResponse;
import ru.urvanov.keystore.controller.domain.SimpleResponse;
import ru.urvanov.keystore.controller.domain.TreeItem;
import ru.urvanov.keystore.controller.domain.TreeResponse;
import ru.urvanov.keystore.domain.DictClientGroup;
import ru.urvanov.keystore.service.DictClientGroupService;

@Controller
@RequestMapping(value = "/dictClientGroup")
public class DictClientGroupController {

    private static final Logger logger = LoggerFactory
            .getLogger(DictClientGroupController.class);

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private DictClientGroupService dictClientGroupService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public @ResponseBody TreeResponse list() {
        logger.info("/dictClientGroup/list started.");
        TreeResponse result = new TreeResponse();
        result.getChildren().setText("text");
        result.getChildren().setExpanded(true);
        TreeItemTotalRecordsCounter totalRecordsCounter = new TreeItemTotalRecordsCounter();
        dictClientGroupService.findAllHierarchy().stream().filter(dcg -> {
            return dcg.getDictClientGroup() == null;
        }).forEach(dcg -> {
            TreeItem treeItem = createTreeItem(dcg, totalRecordsCounter);
            result.getChildren().getChildren().add(treeItem);
        });

        result.setTotalRecords(totalRecordsCounter.getTotalRecords());
        result.setSuccess(true);
        logger.info("/dictClientGroup/list finished.");
        return result;
    }

    private class TreeItemTotalRecordsCounter {
        private long totalRecords;

        public long getTotalRecords() {
            return totalRecords;
        }

        public void setTotalRecords(long totalRecords) {
            this.totalRecords = totalRecords;
        }
    }

    /**
     * Now performance is fine, but later... May be I must to expand recursive
     * call...
     */
    private TreeItem createTreeItem(DictClientGroup dcg,
            TreeItemTotalRecordsCounter totalRecordsCounter) {
        TreeItem treeItem = new TreeItem();
        totalRecordsCounter.setTotalRecords(totalRecordsCounter
                .getTotalRecords() + 1);
        treeItem.setId(String.valueOf(dcg.getId()));
        treeItem.setText(dcg.getName());
        treeItem.setLeaf(dcg.getChildDictClientGroups().size() == 0);
        treeItem.setExpanded(true);
        if (dcg.getChildDictClientGroups().size() > 0) {
            dcg.getChildDictClientGroups()
                    .stream()
                    .forEach(
                            childDictClientGroup -> {
                                TreeItem childTreeItem = createTreeItem(
                                        childDictClientGroup,
                                        totalRecordsCounter);
                                treeItem.getChildren().add(childTreeItem);
                            });
        }
        return treeItem;
    }

    @RequestMapping(value = "/edit", method = { RequestMethod.POST,
            RequestMethod.GET })
    public @ResponseBody ObjectResponse<DictClientGroupJson> edit(
            @RequestParam(value = "id") Long id) {
        logger.info("/dictClientGroup/edit started.");
        DictClientGroup dictClientGroup = dictClientGroupService.findById(id);
        if (dictClientGroup == null) {
            ObjectResponse<DictClientGroupJson> result = new ObjectResponse<>();
            result.setMessage("Группа клиентов с id=" + id + " не найдена.");
            return result;
        }
        ObjectResponse<DictClientGroupJson> result = new ObjectResponse<>();
        DictClientGroupJson info = new DictClientGroupJson();
        result.setInfo(info);
        info.setId(String.valueOf(dictClientGroup.getId()));
        info.setName(dictClientGroup.getName());
        DictClientGroup parentDictClientGroup = dictClientGroup
                .getDictClientGroup();
        if (parentDictClientGroup != null) {
            info.setDictClientGroupId(String.valueOf(parentDictClientGroup
                    .getId()));
            info.setDictClientGroupName(parentDictClientGroup.getName());
        }
        result.setSuccess(true);
        logger.info("/dictClientGroup/edit finished.");
        return result;
    }

    public static class DictClientGroupJson implements Serializable {
        /**
         * 
         */
        private static final long serialVersionUID = -5611452716318449875L;
        private String id;
        @NotNull
        @Size(min = 1, max = 100)
        private String name;
        private String dictClientGroupId;
        private String dictClientGroupName;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDictClientGroupId() {
            return dictClientGroupId;
        }

        public void setDictClientGroupId(String dictClientGroupId) {
            this.dictClientGroupId = dictClientGroupId;
        }

        public String getDictClientGroupName() {
            return dictClientGroupName;
        }

        public void setDictClientGroupName(String dictClientGroupName) {
            this.dictClientGroupName = dictClientGroupName;
        }

        @Override
        public String toString() {
            return "DictClientGroup id=" + id + ", name = " + name
                    + ", parent=" + dictClientGroupId;
        }
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public @ResponseBody SimpleResponse save(
            @RequestBody @Valid DictClientGroupJson dictClientGroupJson,
            BindingResult bindingResult) {
        logger.info("/dictClientGroup/save started.");
        if (bindingResult.hasErrors()) {
            SimpleResponse result = new SimpleResponse();
            result.setMessage(messageSource.getMessage(
                    "ru.urvanov.keystore.check_form", null, null));
            return result;
        }
        DictClientGroup dictClientGroup = null;

        if (dictClientGroupJson.getId() == null
                || "".equals(dictClientGroupJson.getId()))
            dictClientGroup = new DictClientGroup();
        else
            dictClientGroup = dictClientGroupService.findById(Long
                    .valueOf(dictClientGroupJson.getId()));
        dictClientGroup.setName(dictClientGroupJson.getName());
        if (dictClientGroupJson.getDictClientGroupId() != null
                && dictClientGroupJson.getDictClientGroupId().length() > 0)
            dictClientGroup.setDictClientGroup(dictClientGroupService
                    .findById(Long.valueOf(dictClientGroupJson
                            .getDictClientGroupId())));

        dictClientGroupService.save(dictClientGroup);

        SimpleResponse result = new SimpleResponse();
        result.setSuccess(true);
        logger.info("/dictClientgroup/save finished.");
        return result;
    }
}
