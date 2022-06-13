package com.airtel.common.dto.request;

public class DbRequestDto {
	
	
	    private String ip;
	    private String username;
	    private String password;
	    private String port;
	    private String sid;

	    public DbRequestDto(String ip, String username, String password, String port, String sid)
	    {
	        this.ip=ip;
	        this.username=username;
	        this.password=password;
	        this.port=port;
	        this.sid=sid;
	    }

	    public String getIp() {
	        return ip;
	    }

	    public void setIp(String ip) {
	        this.ip = ip;
	    }

	    public String getUsername() {
	        return username;
	    }

	    public void setUsername(String username) {
	        this.username = username;
	    }

	    public String getPassword() {
	        return password;
	    }

	    public void setPassword(String password) {
	        this.password = password;
	    }

	    public String getPort() {
	        return port;
	    }

	    public void setPort(String port) {
	        this.port = port;
	    }

	    public String getSid() {
	        return sid;
	    }

	    public void setSid(String sid) {
	        this.sid = sid;
	    }

	    @Override
	    public String toString() {
	        return "DBRequestDTO{" +
	                "ip='" + ip + '\'' +
	                ", username='" + username + '\'' +
	                ", password='" + password + '\'' +
	                ", port='" + port + '\'' +
	                ", sid='" + sid + '\'' +
	                '}';
	    }
	}



