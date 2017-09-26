package com.avit.common.json.bean;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ChannelEpgAfter implements java.io.Serializable{

	private Long id;
	private String channelCode;//频道编码
	private String eventName;//epg名称
	private String eventDesc;//epg描述
	private String day;//日期yyyy-MM-DD
	private Date beginTime;//开始日期
	private Date endTime;//结束日期
	private int flag;//是否事后epg，1为是，0为否，默认为0
	private Date lastUpdate;//最后更新时间
	private Integer record;//0：等待采集（默认），1：正在采集，2：采集成功，3：采集失败
	
	public ChannelEpgAfter(){
		this.record = 1;
		this.flag = 1;
	}
	
	public ChannelEpgAfter(Date beginTime, String eventName, Date endTime, String channelCode){
		this.beginTime = beginTime;
		this.eventName = eventName;
		this.endTime = endTime;
		this.channelCode = channelCode;
		this.record = 1;
		this.day = "1";
		this.flag = 1;
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
	
	public String getDay() {
		return day;
	}
	public void setDay(String day) {
		this.day = day;
	}
	
	public Date getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}
	
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	
	public Date getLastUpdate() {
		return lastUpdate;
	}
	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
	
	public Integer getRecord() {
		return record;
	}
	public void setRecord(Integer record) {
		this.record = record;
	}
	
	public String getLastUpdateFormat(){
		if(lastUpdate == null){
			return "";
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		return format.format(lastUpdate);
	}
	public String getBeginTimeFormat(){
		if(beginTime == null){
			return "";
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		return format.format(beginTime);
	}
	public String getEndTimeFormat(){
		if(endTime == null){
			return "";
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		return format.format(endTime);
	}
}
