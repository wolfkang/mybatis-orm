package org.mybatisorm.util;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class Search {

	public static final String SEARCH_TYPE_SUBJECT = "SUBJECT";
	public static final String SEARCH_TYPE_CONTENT = "CONTENT";
	public static final String SEARCH_TYPE_NICKNAME = "NICKNAME";
	
	private Page page;
	private String orderQuery;
	private Object parameter;
	
	private int seqno;
	private int grade;
	private int reccnt;
	private String searchtype;
	private String keyword;
	private String id;
	private String type;
	private String subtype;
	private String userid;
	private String webzineid;
	private String catecode;
	private String month;
	private String nextMonth;
	private String list;
	private String status;
	private String display;
	private String starttype;
	private String startid;
	private String endtype;
	private String endid;
	private String drugcode;
	private String rxid;
	private String diseaseid;
	private String rxchartid;
	private String reviewid;
	private Object gradeavg;
	private Object reviewcnt;
	private Object imagecnt;
	private String sort;
	private String groupby;
	private String enname;
	private String idopen;
	private String jobcode;
	private String ingredient;
	private String tipid;
	private String centerid;
	private String guideid;
	private String code;
	private String coid;
	private String noticeid;
	private String index;
	private String etc;
	private String roottype;
	private String rootid;
	private String estimatecheck;
	private String date;
	private String hospitalid;
	private String emaildomain;
	private String orgid;
	private String confirmkey;
	private String rsvid;
	private String storyid;
	private String contentid;
	private String showchat;
	private String doctorid;
	private String maulid;
	private String noteid;
	private String service;
	private String locacode;
	private String paystatus;
	private String authorize;
	private String inspectid;
	private String checkup;
	private String maleage;
	private String nickname;
	private String password;
	private String email;
	private String name;
	private String historyid;
	private String target;
	private String marriage;
	private String searchfield;
	private String requestid;
	private String column;
	private String expid;
	private String displayorg;
	private Object value;
	
	public String getExpid() {
		return expid;
	}
	public void setExpid(String expid) {
		this.expid = expid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getMaleage() {
		return maleage;
	}
	public void setMaleage(String maleage) {
		this.maleage = maleage;
	}
	public String getFemaleage() {
		return femaleage;
	}
	public void setFemaleage(String femaleage) {
		this.femaleage = femaleage;
	}
	public String getFemalemarry() {
		return femalemarry;
	}
	public void setFemalemarry(String femalemarry) {
		this.femalemarry = femalemarry;
	}
	private String femaleage;
	private String femalemarry;
	
	public String getService() {
		return service;
	}
	public void setService(String service) {
		this.service = service;
	}
	public String getLocacode() {
		return locacode;
	}
	public void setLocacode(String locacode) {
		this.locacode = locacode;
	}
	public String getRoottype() {
		return roottype;
	}
	public void setRoottype(String roottype) {
		this.roottype = roottype;
	}
	public String getRootid() {
		return rootid;
	}
	public void setRootid(String rootid) {
		this.rootid = rootid;
	}
	public String getSearchtype() {
		return searchtype;
	}
	public void setSearchtype(String searchtype) {
		this.searchtype = searchtype;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String getOrderQuery() {
		return orderQuery;
	}
	public void setOrderQuery(String orderQuery) {
		this.orderQuery = orderQuery;
	}
	public Page getPage() {
		return this.page;
	}
	public void setPage(Page page) {
		this.page = page;
	}
	public void setPage(int articlePerPage) {
		this.page = new PageNavigation(articlePerPage);
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getWebzineid() {
		return webzineid;
	}
	public void setWebzineid(String webzineid) {
		this.webzineid = webzineid;
	}
	public String getCatecode() {
		return catecode;
	}
	public void setCatecode(String catecode) {
		this.catecode = catecode;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public void setNextMonth(String nextMonth) {
		this.nextMonth = nextMonth;
	}
	public String getNextMonth() {
		return nextMonth;
	}
	public void setList(String list) {
		this.list = list;
	}
	public String getList() {
		return list;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	public String getStatus() {
		return status;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ToStringStyle.MULTI_LINE_STYLE);
	}
	public void setDisplay(String display) {
		this.display = display;
	}
	public String getDisplay() {
		return display;
	}
	public String getStartid() {
		return startid;
	}
	public void setStartid(String startid) {
		this.startid = startid;
	}
	public String getEndid() {
		return endid;
	}
	public void setEndid(String endid) {
		this.endid = endid;
	}
	public String getRxid() {
		return rxid;
	}
	public void setRxid(String rxid) {
		this.rxid = rxid;
	}
	public String getDiseaseid() {
		return diseaseid;
	}
	public void setDiseaseid(String diseaseid) {
		this.diseaseid = diseaseid;
	}
	public Object isGradeavg() {
		return gradeavg;
	}
	public void setGradeavg(boolean gradeavg) {
		this.gradeavg = gradeavg;
	}
	public Object isReviewcnt() {
		return reviewcnt;
	}
	public void setReviewcnt(boolean reviewcnt) {
		this.reviewcnt = reviewcnt;
	}
	public Object isImagecnt() {
		return imagecnt;
	}
	public void setImagecnt(boolean imagecnt) {
		this.imagecnt = imagecnt;
	}
	public void setReviewid(String reviewid) {
		this.reviewid = reviewid;
	}
	public String getReviewid() {
		return reviewid;
	}
	public void setSeqno(int seqno) {
		this.seqno = seqno;
	}
	public int getSeqno() {
		return seqno;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	public String getSort() {
		return sort;
	}
	public void setGrade(int grade) {
		this.grade = grade;
	}
	public int getGrade() {
		return grade;
	}
	public void setReccnt(int reccnt) {
		this.reccnt = reccnt;
	}
	public int getReccnt() {
		return reccnt;
	}
	public void setGroupby(String groupby) {
		this.groupby = groupby;
	}
	public String getGroupby() {
		return groupby;
	}
	public void setEnname(String enname) {
		this.enname = enname;
	}
	public String getEnname() {
		return enname;
	}
	public void setIdopen(String idopen) {
		this.idopen = idopen;
	}
	public String getIdopen() {
		return idopen;
	}
	public void setJobcode(String jobcode) {
		this.jobcode = jobcode;
	}
	public String getJobcode() {
		return jobcode;
	}
	public String getIngredient() {
		return ingredient;
	}
	public void setIngredient(String ingredient) {
		this.ingredient = ingredient;
	}
	
	public String getStarttype() {
		return starttype;
	}
	public void setStarttype(String starttype) {
		this.starttype = starttype;
	}
	public String getEndtype() {
		return endtype;
	}
	public void setEndtype(String endtype) {
		this.endtype = endtype;
	}
	public void setRxchartid(String rxchartid) {
		this.rxchartid = rxchartid;
	}
	public String getRxchartid() {
		return rxchartid;
	}
	public void setDrugcode(String drugcode) {
		this.drugcode = drugcode;
	}
	public String getDrugcode() {
		return drugcode;
	}
	public void setCenterid(String centerid) {
		this.centerid = centerid;
	}
	public String getCenterid() {
		return centerid;
	}
	public void setGuideid(String guideid) {
		this.guideid = guideid;
	}
	public String getGuideid() {
		return guideid;
	}
	public void setTipid(String tipid) {
		this.tipid = tipid;
	}
	public String getTipid() {
		return tipid;
	}
	public void setSubtype(String subtype) {
		this.subtype = subtype;
	}
	public String getSubtype() {
		return subtype;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getCode() {
		return code;
	}
	public void setCoid(String coid) {
		this.coid = coid;
	}
	public String getCoid() {
		return coid;
	}
	public void setNoticeid(String noticeid) {
		this.noticeid = noticeid;
	}
	public String getNoticeid() {
		return noticeid;
	}
	public void setIndex(String index) {
		this.index = index;
	}
	public String getIndex() {
		return index;
	}
	public void setEtc(String etc) {
		this.etc = etc;
	}
	public String getEtc() {
		return etc;
	}
	public void setEstimatecheck(String estimatecheck) {
		this.estimatecheck = estimatecheck;
	}
	public String getEstimatecheck() {
		return estimatecheck;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getDate() {
		return date;
	}
	public void setHospitalid(String hospitalid) {
		this.hospitalid = hospitalid;
	}
	public String getHospitalid() {
		return hospitalid;
	}
	public void setEmaildomain(String emaildomain) {
		this.emaildomain = emaildomain;
	}
	public String getEmaildomain() {
		return emaildomain;
	}
	public void setOrgid(String orgid) {
		this.orgid = orgid;
	}
	public String getOrgid() {
		return orgid;
	}
	public void setConfirmkey(String confirmkey) {
		this.confirmkey = confirmkey;
	}
	public String getConfirmkey() {
		return confirmkey;
	}
	public void setRsvid(String rsvid) {
		this.rsvid = rsvid;
	}
	public String getRsvid() {
		return rsvid;
	}
	public void setStoryid(String storyid) {
		this.storyid = storyid;
	}
	public String getStoryid() {
		return storyid;
	}
	public void setContentid(String contentid) {
		this.contentid = contentid;
	}
	public String getContentid() {
		return contentid;
	}
	public void setShowchat(String showchat) {
		this.showchat = showchat;
	}
	public String getShowchat() {
		return showchat;
	}
	public void setDoctorid(String doctorid) {
		this.doctorid = doctorid;
	}
	public String getDoctorid() {
		return doctorid;
	}
	public void setMaulid(String maulid) {
		this.maulid = maulid;
	}
	public String getMaulid() {
		return maulid;
	}
	public void setNoteid(String noteid) {
		this.noteid = noteid;
	}
	public String getNoteid() {
		return noteid;
	}
	public void setPaystatus(String paystatus) {
		this.paystatus = paystatus;
	}
	public String getPaystatus() {
		return paystatus;
	}
	public void setAuthorize(String authorize) {
		this.authorize = authorize;
	}
	public String getAuthorize() {
		return authorize;
	}
	public void setInspectid(String inspectid) {
		this.inspectid = inspectid;
	}
	public String getInspectid() {
		return inspectid;
	}
	public void setCheckup(String checkup) {
		this.checkup = checkup;
	}
	public String getCheckup() {
		return checkup;
	}
	public void setHistoryid(String historyid) {
		this.historyid = historyid;
	}
	public String getHistoryid() {
		return historyid;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public String getTarget() {
		return target;
	}
	public void setMarriage(String marriage) {
		this.marriage = marriage;
	}
	public String getMarriage() {
		return marriage;
	}
	public void setParameter(Object parameter) {
		this.parameter = parameter;
	}
	public Object getParameter() {
		return parameter;
	}
	public void setSearchfield(String searchfield) {
		this.searchfield = searchfield;
	}
	public String getSearchfield() {
		return searchfield;
	}
	public void setRequestid(String requestid) {
		this.requestid = requestid;
	}
	public String getRequestid() {
		return requestid;
	}
	public void setColumn(String column) {
		this.column = column;
	}
	public String getColumn() {
		return column;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	public Object getValue() {
		return value;
	}
	public void setDisplayorg(String displayorg) {
		this.displayorg = displayorg;
	}
	public String getDisplayorg() {
		return displayorg;
	}
}
