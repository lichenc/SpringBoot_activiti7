package com.example.tquan.entity;

public class ActivitiEntity {

        //private String id;
    /*   map.put("appId", activiti.getAppId());
        map.put("role", activiti.getRole());
        map.put("account", activiti.getAccount());
        map.put("actPwd", activiti.getActPwd());
        map.put("status", activiti.getStatus());
        map.put("userId", activiti.getUserId());*/
        private String name;
        private String type;
        private String app;
        private String proposer;
        private String role;
        private String appId;
        private String account;
        private String actPwd;
        private String status;
        private String userId;
        private String description;
    public String getProposer() {
        return proposer;
    }

    public void setProposer(String proposer) {
        this.proposer = proposer;
    }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getApp() {
            return app;
        }

        public void setApp(String app) {
            this.app = app;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getActPwd() {
        return actPwd;
    }

    public void setActPwd(String actPwd) {
        this.actPwd = actPwd;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

    @Override
    public String toString() {
        return "ActivitiEntity{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", app='" + app + '\'' +
                ", proposer='" + proposer + '\'' +
                ", role='" + role + '\'' +
                ", appId='" + appId + '\'' +
                ", account='" + account + '\'' +
                ", actPwd='" + actPwd + '\'' +
                ", status='" + status + '\'' +
                ", userId='" + userId + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}



