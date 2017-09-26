package com.avit.common.json.bean;

import com.avit.common.json.Person;



public class ChannelEpg {

	private Long id;
	private String channelCode;
	private String programGuideId;
	private String eventName;
	private String eventDesc;
	private String beginTime;
	private String endTime;
	private String operationCode;
	private long begin;
	private long end;
	private Person person;

/*	public ChannelEpg(Long id,String channelCode,String programGuideId,String eventName,String eventDesc,String beginTime,String endTime,String operationCode){
		this.id=id;
		this.channelCode=channelCode;
		this.programGuideId=programGuideId;
		this.eventName=eventName;
		this.eventDesc=eventDesc;
		this.beginTime=beginTime;
		this.endTime=endTime;
		this.operationCode=operationCode;
	}*/
	
	
	
	public long getBegin() {
		return begin;
	}
	public Person getPerson()
    {
        return person;
    }
    public void setPerson(Person person)
    {
        this.person = person;
    }
    public void setBegin(long begin) {
		this.begin = begin;
	}
	public long getEnd() {
		return end;
	}
	public void setEnd(long end) {
		this.end = end;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getChannelCode() {
		return channelCode;
	}
	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}
	public String getProgramGuideId() {
		return programGuideId;
	}
	public void setProgramGuideId(String programGuideId) {
		this.programGuideId = programGuideId;
	}
	public String getEventName() {
		return eventName;
	}
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
	public String getEventDesc() {
		return eventDesc;
	}
	public void setEventDesc(String eventDesc) {
		this.eventDesc = eventDesc;
	}
	public String getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getOperationCode() {
		return operationCode;
	}
	public void setOperationCode(String operationCode) {
		this.operationCode = operationCode;
	}
	public int compareTo(ChannelEpg o) {
		return this.begin<o.getBegin() ? 1 : (this.begin==o.getBegin() ? 0 : -1);
	}	
	
}
