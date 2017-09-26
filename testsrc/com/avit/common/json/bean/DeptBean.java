package com.avit.common.json.bean;

import java.util.Date;

import com.avit.common.json.jackson.JsonDateFormat2;
import com.avit.common.json.jackson.JsonDateFormatFull;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class DeptBean
{
    @JsonDeserialize(using = JsonDateFormat2.class)
    @JsonSerialize(using = JsonDateFormatFull.class)
    private Date createDate;
    @JsonSerialize(using = JsonDateFormatFull.class)
    private Date dateFormat;

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Override
    public String toString() {
        return "DeptBean [createDate=" + createDate + ", dateFormat=" + dateFormat
                + "]";
    }

    public DeptBean(Date createDate, Date dateFormat) {
        super();
        this.createDate = createDate;
        this.dateFormat = dateFormat;
    }

    public DeptBean() {
        super();
    }

    public Date getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(Date dateFormat) {
        this.dateFormat = dateFormat;
    }
 
}
