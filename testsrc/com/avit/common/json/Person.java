package com.avit.common.json;

import java.util.Date;

import com.google.gson.annotations.Expose;

public class Person
{
    @Expose
    private String name;
    @Expose
    private String address;
    
    private String job;
    @Expose
    private Boolean flag;
    @Expose
    private Date birthday;
    
    public String getJob()
    {
        return job;
    }
    public void setJob(String job)
    {
        this.job = job;
    }
    public Date getBirthday()
    {
        return birthday;
    }
    public void setBirthday(Date birthday)
    {
        this.birthday = birthday;
    }
    public Boolean getFlag()
    {
        return flag;
    }
    public void setFlag(Boolean flag)
    {
        this.flag = flag;
    }
    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name = name;
    }
    public String getAddress()
    {
        return address;
    }
    public void setAddress(String address)
    {
        this.address = address;
    }
    
}
