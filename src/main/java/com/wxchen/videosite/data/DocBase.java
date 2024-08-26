package com.wxchen.videosite.data;

import java.util.List;

public class DocBase {
    protected String name;
    protected Boolean group;
    protected List<DocBase> docs;


    /**
     * 找子一级
     * @param name
     * @return
     */
    public DocBase sub(String name) {
        for (DocBase doc : this.docs) {
            if(doc.name.equals(name)) {
                return doc;
            }
        }
        return null;
    }

    public void addSub(DocBase base) {
        this.docs.add(base);
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getGroup() {
        return group;
    }

    public void setGroup(Boolean group) {
        this.group = group;
    }

    public List<DocBase> getDocs() {
        return docs;
    }

    public void setDocs(List<DocBase> docs) {
        this.docs = docs;
    }
}
