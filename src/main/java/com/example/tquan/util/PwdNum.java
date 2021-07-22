package com.example.tquan.util;

import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Random;

public class PwdNum {

    public String   Pwd(int accountRank,int accountLength,String pwd){
        String num="0123456789";
        String smallLetter ="abcdefghijklmnopqrstuvwxyz";
        String majuscule="ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String specialCharacter="!@#$%^&*.=+";
        String all="abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ!@#$%^&*.=+";
        String nss="abcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*.=+";
        String nsm="abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String ns="abcdefghijklmnopqrstuvwxyz0123456789";
        int pross=0;
        StringBuffer sb=new StringBuffer();
        if(accountRank==1){
            Random rm = new Random();
            // 获得随机数
            pross = (int) ((1 + rm.nextDouble()) * Math.pow(10, accountLength-1));
            pwd=String.valueOf(pross);
        }else if (accountRank==2){
            Random random=new Random();
            for(int i=0;i<accountLength;i++){
                if(i==2){
                    int number=random.nextInt(10);
                    sb.append(num.charAt(number));
                }else if (i==4){
                    int number=random.nextInt(26);
                    sb.append(smallLetter.charAt(number));
                }else {
                    int number=random.nextInt(36);
                    sb.append(ns.charAt(number));
                }
            }
            pwd=sb.toString();
        }else if (accountRank==3){
            Random random=new Random();
            for(int i=0;i<accountLength;i++){
                if(i==1){
                    int number=random.nextInt(10);
                    sb.append(num.charAt(number));
                }else if (i==2){
                    int number=random.nextInt(26);
                    sb.append(majuscule.charAt(number));
                }else if (i==4){
                    int number=random.nextInt(26);
                    sb.append(smallLetter.charAt(number));
                }else {
                    int number=random.nextInt(62);
                    sb.append(nsm.charAt(number));
                }
            }
            pwd=sb.toString();
        }else if (accountRank==4){
            Random random=new Random();
            for(int i=0;i<accountLength;i++){
                if(i==3){
                    int number=random.nextInt(10);
                    sb.append(num.charAt(number));
                }else if (i==4){
                    int number=random.nextInt(26);
                    sb.append(smallLetter.charAt(number));
                }else if (i==1){
                    int number=random.nextInt(11);
                    sb.append(specialCharacter.charAt(number));
                }else {
                    int number=random.nextInt(47);
                    sb.append(nss.charAt(number));
                }
            }
            pwd=sb.toString();
        }else if (accountRank==5){
            Random random=new Random();
            for(int i=0;i<accountLength;i++){
                if(i==3){
                    int number=random.nextInt(10);
                    sb.append(num.charAt(number));
                }else if (i==4){
                    int number=random.nextInt(26);
                    sb.append(smallLetter.charAt(number));
                }else if (i==2){
                    int number=random.nextInt(26);
                    sb.append(majuscule.charAt(number));
                }else if (i==1){
                    int number=random.nextInt(11);
                    sb.append(specialCharacter.charAt(number));
                }else {
                    int number=random.nextInt(73);
                    sb.append(all.charAt(number));
                }
            }
            pwd=sb.toString();
            System.out.println("密码："+pwd);
        }
        System.out.println("密码："+pwd);
        return pwd;
    }
}
