package com.avit.common.json.bean;
// default package

import java.util.Date;
public class UILinkInfo  implements java.io.Serializable {

	private static final long serialVersionUID = 4032990978091997438L;
    // Fields    


	 private Long id;
     private String categoryCode;
     private String linkName;
     private String linkUrl;
     private String linkUrlSmart;
     private Date createTime;
     private Date updateTime;
     private String templateType;


    // Constructors

    /** default constructor */
    public UILinkInfo() {
    }

    public UILinkInfo(String linkName,String templateType)
    {
        this.linkName=linkName;
        this.templateType=templateType;
    }
	/** minimal constructor */
    public UILinkInfo(Long id, String categoryCode, String linkName, String linkUrl, Date createTime) {
        this.id = id;
        this.categoryCode = categoryCode;
        this.linkName = linkName;
        this.linkUrl = linkUrl;
        this.createTime = createTime;
    }
    
    /** full constructor */
    public UILinkInfo(Long id, String categoryCode, String linkName, String linkUrl, String linkUrlSmart, Date createTime, Date updateTime) {
        this.id = id;
        this.categoryCode = categoryCode;
        this.linkName = linkName;
        this.linkUrl = linkUrl;
        this.linkUrlSmart = linkUrlSmart;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

   
    // Property accessors


    public Long getId() {
        return this.id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }


    public String getCategoryCode() {
        return this.categoryCode;
    }
    
    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }


    public String getLinkName() {
        return this.linkName;
    }
    
    public void setLinkName(String linkName) {
        this.linkName = linkName;
    }


    public String getLinkUrl() {
        return this.linkUrl;
    }
    
    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public String getLinkUrlSmart() {
        return this.linkUrlSmart;
    }
    
    public void setLinkUrlSmart(String linkUrlSmart) {
        this.linkUrlSmart = linkUrlSmart;
    }

    public Date getCreateTime() {
        return this.createTime;
    }
    
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return this.updateTime;
    }
    
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
   public String getTemplateType()
    {
        return templateType;
    }

    public void setTemplateType(String templateType)
    {
        this.templateType = templateType;
    }

public boolean equals(Object other) {
         if ( (this == other ) ) return true;
		 if ( (other == null ) ) return false;
		 if ( !(other instanceof UILinkInfo) ) return false;
		 UILinkInfo castOther = ( UILinkInfo ) other; 
         
		 return ( (this.getId()==castOther.getId()) || ( this.getId()!=null && castOther.getId()!=null && this.getId().equals(castOther.getId()) ) )
 && ( (this.getCategoryCode()==castOther.getCategoryCode()) || ( this.getCategoryCode()!=null && castOther.getCategoryCode()!=null && this.getCategoryCode().equals(castOther.getCategoryCode()) ) )
 && ( (this.getLinkName()==castOther.getLinkName()) || ( this.getLinkName()!=null && castOther.getLinkName()!=null && this.getLinkName().equals(castOther.getLinkName()) ) )
 && ( (this.getLinkUrl()==castOther.getLinkUrl()) || ( this.getLinkUrl()!=null && castOther.getLinkUrl()!=null && this.getLinkUrl().equals(castOther.getLinkUrl()) ) )
 && ( (this.getLinkUrlSmart()==castOther.getLinkUrlSmart()) || ( this.getLinkUrlSmart()!=null && castOther.getLinkUrlSmart()!=null && this.getLinkUrlSmart().equals(castOther.getLinkUrlSmart()) ) )
 && ( (this.getCreateTime()==castOther.getCreateTime()) || ( this.getCreateTime()!=null && castOther.getCreateTime()!=null && this.getCreateTime().equals(castOther.getCreateTime()) ) )
 && ( (this.getUpdateTime()==castOther.getUpdateTime()) || ( this.getUpdateTime()!=null && castOther.getUpdateTime()!=null && this.getUpdateTime().equals(castOther.getUpdateTime()) ) );
   }
   
   public int hashCode() {
         int result = 17;
         
         result = 37 * result + ( getId() == null ? 0 : this.getId().hashCode() );
         result = 37 * result + ( getCategoryCode() == null ? 0 : this.getCategoryCode().hashCode() );
         result = 37 * result + ( getLinkName() == null ? 0 : this.getLinkName().hashCode() );
         result = 37 * result + ( getLinkUrl() == null ? 0 : this.getLinkUrl().hashCode() );
         result = 37 * result + ( getLinkUrlSmart() == null ? 0 : this.getLinkUrlSmart().hashCode() );
         result = 37 * result + ( getCreateTime() == null ? 0 : this.getCreateTime().hashCode() );
         result = 37 * result + ( getUpdateTime() == null ? 0 : this.getUpdateTime().hashCode() );
         return result;
   }   





}