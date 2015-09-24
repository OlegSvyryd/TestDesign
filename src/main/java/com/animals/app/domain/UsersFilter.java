package com.animals.app.domain;

import javax.validation.constraints.DecimalMin;

public class UsersFilter {
	@DecimalMin(value = "1")
    private int page;
	 
	@DecimalMin(value = "1")
    private int limit;
	
	private User user;

    public UsersFilter() {
    }

    public UsersFilter(int page, int limit) {
        this.page = page;
        this.limit = limit;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public long getOffset() {
        return (page-1) * limit;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "UserssFilter{" +
                "page=" + page +
                ", limit=" + limit +
                ", user=" + user +
                '}';
    }
}
