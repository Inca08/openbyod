/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package view.controller;

import entity.Call;
import entity.EmailEntity;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import reportUtils.ReportVO;

/**
 *
 * @author Castro
 */
@ManagedBean(name="reportsBean")
@RequestScoped
public class ReportsBean {
    
        private String month;
        private List<ReportVO> callReportList;
        private List<ReportVO> emailReceivedReportList;
        private List<ReportVO> emailSentReportList;
        private List<EmailEntity> emailkeyWordsReportList;
        
        private String keyWords = "NAMORO RELACIONAMENTO BALADA";

    public String getKeyWords() {
        return keyWords;
    }

    public void setKeyWords(String keyWords) {
        this.keyWords = keyWords;
    }
         
         
        
        public List<EmailEntity> getemailkeyWordsReportList(){
            return emailkeyWordsReportList;
        }
        
        public void setemailkeyWordsReportList(List<EmailEntity> emailkeyWordsReportList){
            this.emailkeyWordsReportList = emailkeyWordsReportList;
        }
        
        public List<ReportVO> getemailSentReportList(){
            return emailSentReportList;
        }
        
        public void setemailSentReportList(List<ReportVO> emailSentReportList){
            this.emailSentReportList = emailSentReportList;
        }
        
        public List<ReportVO> getemailReceivedReportList(){
            return emailReceivedReportList;
        }
        
        public void setemailReceivedReportList(List<ReportVO> emailReceivedReportList){
            this.emailReceivedReportList = emailReceivedReportList;
        }
        

        public List<ReportVO> getcallReportList(){
            return callReportList;
        }
        
        public void setcallReportList(List<ReportVO> callReportList){
            this.callReportList = callReportList;
        }
        
    /**
     * Get the value of month
     *
     * @return the value of month
     */
    public String getMonth() {
        return month;
    }

    /**
     * Set the value of month
     *
     * @param month new value of month
     */
    public void setMonth(String month) {
        this.month = month;
    }


    /**
     * Creates a new instance of ReportsBean
     */
    public ReportsBean() {
    }
    
    public String callReport() {
        //gets the paramter from the screen
        int month = Integer.parseInt(this.month);
        
        //gets the list from the database
        this.callReportList = Call.getReportList(month);
        
        //redirects to the page that will show the table
        return "callReportPage";
    }
    
    public String emailReport() {
        //gets the paramter from the screen
        int month = Integer.parseInt(this.month);
        
        //gets the list from the database
        this.emailSentReportList = EmailEntity.getEmailSentReportList(month);
        this.emailReceivedReportList = EmailEntity.getEmailReceivedReportList(month);
        
        //redirects to the page that will show the table
        return "emailReportPage";
    }
    public String keyWordReport() {
        //gets the paramter from the screen
        int month = Integer.parseInt(this.month);
        
        //gets the list from the database
        this.emailkeyWordsReportList = EmailEntity.getEmailKeyWordsReportList(month, this.keyWords);
        
        //redirects to the page that will show the table
        return "keyWordsReportPage";
    }
    
}
