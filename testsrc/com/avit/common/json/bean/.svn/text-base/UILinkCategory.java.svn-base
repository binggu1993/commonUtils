package com.avit.common.json.bean;
// default package

import java.util.Date;



public class UILinkCategory  implements java.io.Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -6583793746312789440L;
	
	// Fields    

     private Long id;
     private String code;
     private String name;
     private String description;
     private Date createTime;
     private Date updateTime;


    // Constructors

    /** default constructor */
    public UILinkCategory() {
    }

	/** minimal constructor */
    public UILinkCategory(Long id, String code, String name, Date createTime) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.createTime = createTime;
    }
    
    /** full constructor */
    public UILinkCategory(Long id, String code, String name, String description, Date createTime, Date updateTime) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.description = description;
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


    public String getCode() {
        return this.code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }


    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }


    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(String description) {
        this.description = description;
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
   



   public boolean equals(Object other) {
         if ( (this == other ) ) return true;
		 if ( (other == null ) ) return false;
		 if ( !(other instanceof UILinkCategory) ) return false;
		 UILinkCategory castOther = ( UILinkCategory ) other; 
         
		 return ( (this.getId()==castOther.getId()) || ( this.getId()!=null && castOther.getId()!=null && this.getId().equals(castOther.getId()) ) )
 && ( (this.getCode()==castOther.getCode()) || ( this.getCode()!=null && castOther.getCode()!=null && this.getCode().equals(castOther.getCode()) ) )
 && ( (this.getName()==castOther.getName()) || ( this.getName()!=null && castOther.getName()!=null && this.getName().equals(castOther.getName()) ) )
 && ( (this.getDescription()==castOther.getDescription()) || ( this.getDescription()!=null && castOther.getDescription()!=null && this.getDescription().equals(castOther.getDescription()) ) )
 && ( (this.getCreateTime()==castOther.getCreateTime()) || ( this.getCreateTime()!=null && castOther.getCreateTime()!=null && this.getCreateTime().equals(castOther.getCreateTime()) ) )
 && ( (this.getUpdateTime()==castOther.getUpdateTime()) || ( this.getUpdateTime()!=null && castOther.getUpdateTime()!=null && this.getUpdateTime().equals(castOther.getUpdateTime()) ) );
   }
   
   public int hashCode() {
         int result = 17;
         
         result = 37 * result + ( getId() == null ? 0 : this.getId().hashCode() );
         result = 37 * result + ( getCode() == null ? 0 : this.getCode().hashCode() );
         result = 37 * result + ( getName() == null ? 0 : this.getName().hashCode() );
         result = 37 * result + ( getDescription() == null ? 0 : this.getDescription().hashCode() );
         result = 37 * result + ( getCreateTime() == null ? 0 : this.getCreateTime().hashCode() );
         result = 37 * result + ( getUpdateTime() == null ? 0 : this.getUpdateTime().hashCode() );
         return result;
   }   





}