package com.avit.common.json.bean;
// default package

import java.util.Date;


public class UIPoster  implements java.io.Serializable {



	private static final long serialVersionUID = 5411292281353324279L;
	// Fields    

     private Long id;
     private Long infoId;
     private String posterPath;
     private String posterResolution;
     private Boolean orderFlag;
     private Long groupNo;
     private Date createTime;


    // Constructors

    /** default constructor */
    public UIPoster() {
    }

	/** minimal constructor */
    public UIPoster(Long id, Long infoId, String posterPath, String posterResolution, Date createTime) {
        this.id = id;
        this.infoId = infoId;
        this.posterPath = posterPath;
        this.posterResolution = posterResolution;
        this.createTime = createTime;
    }
    
    /** full constructor */
    public UIPoster(Long id, Long infoId, String posterPath, String posterResolution, Boolean orderFlag, Long groupNo, Date createTime) {
        this.id = id;
        this.infoId = infoId;
        this.posterPath = posterPath;
        this.posterResolution = posterResolution;
        this.orderFlag = orderFlag;
        this.groupNo = groupNo;
        this.createTime = createTime;
    }

   
    // Property accessors

    public Long getId() {
        return this.id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }


    public Long getInfoId() {
        return this.infoId;
    }
    
    public void setInfoId(Long infoId) {
        this.infoId = infoId;
    }


    public String getPosterPath() {
        return this.posterPath;
    }
    
    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }


    public String getPosterResolution() {
        return this.posterResolution;
    }
    
    public void setPosterResolution(String posterResolution) {
        this.posterResolution = posterResolution;
    }


    public Boolean getOrderFlag() {
        return this.orderFlag;
    }
    
    public void setOrderFlag(Boolean orderFlag) {
        this.orderFlag = orderFlag;
    }


    public Long getGroupNo() {
        return this.groupNo;
    }
    
    public void setGroupNo(Long groupNo) {
        this.groupNo = groupNo;
    }

    public Date getCreateTime() {
        return this.createTime;
    }
    
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
   



   public boolean equals(Object other) {
         if ( (this == other ) ) return true;
		 if ( (other == null ) ) return false;
		 if ( !(other instanceof UIPoster) ) return false;
		 UIPoster castOther = ( UIPoster ) other; 
         
		 return ( (this.getId()==castOther.getId()) || ( this.getId()!=null && castOther.getId()!=null && this.getId().equals(castOther.getId()) ) )
 && ( (this.getInfoId()==castOther.getInfoId()) || ( this.getInfoId()!=null && castOther.getInfoId()!=null && this.getInfoId().equals(castOther.getInfoId()) ) )
 && ( (this.getPosterPath()==castOther.getPosterPath()) || ( this.getPosterPath()!=null && castOther.getPosterPath()!=null && this.getPosterPath().equals(castOther.getPosterPath()) ) )
 && ( (this.getPosterResolution()==castOther.getPosterResolution()) || ( this.getPosterResolution()!=null && castOther.getPosterResolution()!=null && this.getPosterResolution().equals(castOther.getPosterResolution()) ) )
 && ( (this.getOrderFlag()==castOther.getOrderFlag()) || ( this.getOrderFlag()!=null && castOther.getOrderFlag()!=null && this.getOrderFlag().equals(castOther.getOrderFlag()) ) )
 && ( (this.getGroupNo()==castOther.getGroupNo()) || ( this.getGroupNo()!=null && castOther.getGroupNo()!=null && this.getGroupNo().equals(castOther.getGroupNo()) ) )
 && ( (this.getCreateTime()==castOther.getCreateTime()) || ( this.getCreateTime()!=null && castOther.getCreateTime()!=null && this.getCreateTime().equals(castOther.getCreateTime()) ) );
   }
   
   public int hashCode() {
         int result = 17;
         
         result = 37 * result + ( getId() == null ? 0 : this.getId().hashCode() );
         result = 37 * result + ( getInfoId() == null ? 0 : this.getInfoId().hashCode() );
         result = 37 * result + ( getPosterPath() == null ? 0 : this.getPosterPath().hashCode() );
         result = 37 * result + ( getPosterResolution() == null ? 0 : this.getPosterResolution().hashCode() );
         result = 37 * result + ( getOrderFlag() == null ? 0 : this.getOrderFlag().hashCode() );
         result = 37 * result + ( getGroupNo() == null ? 0 : this.getGroupNo().hashCode() );
         result = 37 * result + ( getCreateTime() == null ? 0 : this.getCreateTime().hashCode() );
         return result;
   }   





}