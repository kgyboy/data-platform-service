package com.baiwang.dataplatform.entity;

public class CountBean {
    private String taxno;
    private String diskno;
    private String spcount;
    private String midcount;
    private String dsjcount;
    private String kprq;

    public CountBean() {
    }

    public CountBean(String taxno, String diskno, String spcount, String midcount, String dsjcount, String kprq) {
        this.taxno = taxno;
        this.diskno = diskno;
        this.spcount = spcount;
        this.midcount = midcount;
        this.dsjcount = dsjcount;
        this.kprq = kprq;
    }

    public String getTaxno() {
        return taxno;
    }

    public void setTaxno(String taxno) {
        this.taxno = taxno;
    }

    public String getDiskno() {
        return diskno;
    }

    public void setDiskno(String diskno) {
        this.diskno = diskno;
    }

    public String getSpcount() {
        return spcount;
    }

    public void setSpcount(String spcount) {
        this.spcount = spcount;
    }

    public String getMidcount() {
        return midcount;
    }

    public void setMidcount(String midcount) {
        this.midcount = midcount;
    }

    public String getDsjcount() {
        return dsjcount;
    }

    public void setDsjcount(String dsjcount) {
        this.dsjcount = dsjcount;
    }

    public String getKprq() {
        return kprq;
    }

    public void setKprq(String kprq) {
        this.kprq = kprq;
    }
}
