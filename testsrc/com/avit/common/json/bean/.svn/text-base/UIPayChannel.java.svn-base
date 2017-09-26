package com.avit.common.json.bean;
// default package

import javax.xml.bind.annotation.XmlAttribute;

public class UIPayChannel implements java.io.Serializable
{
    
    private static final long serialVersionUID = 9181279863899967411L;
    
    @XmlAttribute
    private Long id;
    
    @XmlAttribute
    private String channelCode;
    
    @XmlAttribute
    private String channelName;
    
    @XmlAttribute
    private String serviceId;
    
    @XmlAttribute
    private String posterUrl;
    
    @XmlAttribute
    private String posterSpec;
    
    // Constructors
    
    /** default constructor */
    public UIPayChannel()
    {
    }
    
    public Long getId()
    {
        return this.id;
    }
    
    public void setId(Long id)
    {
        this.id = id;
    }
    
    public String getChannelCode()
    {
        return channelCode;
    }
    
    public void setChannelCode(String channelCode)
    {
        this.channelCode = channelCode;
    }
    
    public String getChannelName()
    {
        return channelName;
    }
    
    public void setChannelName(String channelName)
    {
        this.channelName = channelName;
    }
    
    public String getServiceId()
    {
        return serviceId;
    }
    
    public void setServiceId(String serviceId)
    {
        this.serviceId = serviceId;
    }
    
    private Long order;
    
    public Long getOrder()
    {
        return order;
    }
    
    public void setOrder(Long order)
    {
        this.order = order;
    }
    
    public String getPosterUrl()
    {
        return posterUrl;
    }
    
    public void setPosterUrl(String posterUrl)
    {
        this.posterUrl = posterUrl;
    }
    
    public String getPosterSpec()
    {
        return posterSpec;
    }
    
    public void setPosterSpec(String posterSpec)
    {
        this.posterSpec = posterSpec;
    }
    
    public boolean equals(Object other)
    {
        if ((this == other))
            return true;
        if ((other == null))
            return false;
        if (!(other instanceof UIPayChannel))
            return false;
        UIPayChannel castOther = (UIPayChannel)other;
        
        return ((this.getId() == castOther.getId()) || (this.getId() != null && castOther.getId() != null && this.getId().equals(castOther.getId())))
                && ((this.getChannelCode() == castOther.getChannelCode()) || (this.getChannelCode() != null && castOther.getChannelCode() != null && this.getChannelCode()
                        .equals(castOther.getChannelCode())))
                && ((this.getChannelName() == castOther.getChannelName()) || (this.getChannelName() != null && castOther.getChannelName() != null && this.getChannelName()
                        .equals(castOther.getChannelName())))
                && ((this.getServiceId() == castOther.getServiceId()) || (this.getServiceId() != null && castOther.getServiceId() != null && this.getServiceId().equals(castOther.getServiceId())));
    }
    
    public int hashCode()
    {
        int result = 17;
        
        result = 37 * result + (getId() == null ? 0 : this.getId().hashCode());
        result = 37 * result + (getChannelCode() == null ? 0 : this.getChannelCode().hashCode());
        result = 37 * result + (getChannelName() == null ? 0 : this.getChannelName().hashCode());
        result = 37 * result + (getServiceId() == null ? 0 : this.getServiceId().hashCode());
        return result;
    }
    
}