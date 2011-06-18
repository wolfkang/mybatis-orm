package org.mybatisorm.util;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;


public class PageNavigation implements Page {

	int pageNumber;
	int articlePerPage;
	int pagePerGroup;

    int count;
    int numArticleInCurrentPage = 0;
    int startPageNum = 0;
    int prevPageNum = 0;
    int nextPageNum = 0;	
    
    int limitstart = 0;
    int limitend = 0;
	
    int rowstart = 0;
    int rowend = 0;
    
	public PageNavigation(){}
	
	public PageNavigation(int articlePerPage) {
		this(1,articlePerPage-1,1);
	}		

	public PageNavigation(int pageNumber, int articlePerPage, int pagePerGroup) {
		this.pageNumber = pageNumber;
		this.pagePerGroup = pagePerGroup;
		this.articlePerPage = articlePerPage;

        // limit 번호
		this.limitstart = ((this.pageNumber - 1) / this.pagePerGroup)* (this.articlePerPage * this.pagePerGroup);
		//this.limitstart+=(this.pageNumber>this.pagePerGroup)?1:0;
		this.limitend = 1 + (this.articlePerPage * this.pagePerGroup);
		//this.limitend+=(this.pageNumber>this.pagePerGroup)?0:1;
	}

	public void makelink(int count) {
		this.count = count;
  
		// 화면에 보여줄 페이지 총 index수 1,2,3...10
        int tempTotalCount = this.count;
        if (this.articlePerPage * this.pagePerGroup < this.count) {
            tempTotalCount = this.articlePerPage * this.pagePerGroup;
        }
        if (tempTotalCount % this.articlePerPage > 0) {
            this.numArticleInCurrentPage = tempTotalCount / this.articlePerPage + 1;
        } else {
            this.numArticleInCurrentPage = tempTotalCount / this.articlePerPage;
        }

        // 화면에 보여줄 시작 페이징 수
        this.startPageNum = ((this.pageNumber - 1) / this.pagePerGroup)
                * this.pagePerGroup + 1;

        // 이전페이지 번호(없으면 0)
        if (this.pageNumber > this.pagePerGroup) {
            this.prevPageNum = (((this.pageNumber - 1) / this.pagePerGroup) - 1)
                    * this.pagePerGroup + this.pagePerGroup;
        }

        // 다음페이지 번호 (없으면 0)
        if (this.articlePerPage * this.pagePerGroup < this.count) {
            this.nextPageNum = this.startPageNum + this.pagePerGroup;
        }
  
        // View 화면 뿌릴때 필요 
		this.rowstart = (this.pageNumber%this.pagePerGroup ==0)?this.pagePerGroup-1:this.pageNumber%this.pagePerGroup-1;
		this.rowstart = this.rowstart*this.articlePerPage;
		
		this.rowend = this.rowstart+this.articlePerPage;
		this.rowend = (this.rowend>this.count)?this.count:this.rowend;
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public int getArticlePerPage() {
		return articlePerPage;
	}

	public void setArticlePerPage(int articlePerPage) {
		this.articlePerPage = articlePerPage;
	}

	public int getPagePerGroup() {
		return pagePerGroup;
	}

	public void setPagePerGroup(int pagePerGroup) {
		this.pagePerGroup = pagePerGroup;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getNumArticleInCurrentPage() {
		return numArticleInCurrentPage;
	}

	public void setNumArticleInCurrentPage(int numArticleInCurrentPage) {
		this.numArticleInCurrentPage = numArticleInCurrentPage;
	}

	public int getStartPageNum() {
		return startPageNum;
	}

	public void setStartPageNum(int startPageNum) {
		this.startPageNum = startPageNum;
	}

	public int getPrevPageNum() {
		return prevPageNum;
	}

	public void setPrevPageNum(int prevPageNum) {
		this.prevPageNum = prevPageNum;
	}

	public int getNextPageNum() {
		return nextPageNum;
	}

	public void setNextPageNum(int nextPageNum) {
		this.nextPageNum = nextPageNum;
	}

	public int getLimitstart() {
		return limitstart;
	}

	public void setLimitstart(int limitstart) {
		this.limitstart = limitstart;
	}

	public int getLimitend() {
		return limitend;
	}

	public void setLimitend(int limitend) {
		this.limitend = limitend;
	}

	public int getRowstart() {
		return rowstart; 
	}

	public void setRowstart(int rowstart) {
		this.rowstart = rowstart;
	}

	public int getRowend() {
		return rowend;
	}

	public void setRowend(int rowend) {
		this.rowend = rowend;
	}

	public void totalmakelink(int total) {
		int intStep = (this.pageNumber-1)/this.pagePerGroup; 
		this.makelink(total-(intStep*this.articlePerPage*this.pagePerGroup));
	}

    @Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ToStringStyle.MULTI_LINE_STYLE);
	}

}
