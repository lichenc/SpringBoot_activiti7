package com.example.tquan.entity;

public class ActivitiEntity {

        //private String id;
        private String name;
        private String type;
        private String app;
        private String proposer;
        private String role;
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

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        @Override
        public String toString() {
            return "ActivitiEntity{" +
                    /* "id='" + id + '\'' +*/
                    " name='" + name + '\'' +
                    ", type='" + type + '\'' +
                    ", proposer='" + proposer + '\'' +
                    ", app='" + app + '\'' +
                    ", role='" + role + '\'' +
                    ", description='" + description + '\'' +
                    '}';
        }
    }



