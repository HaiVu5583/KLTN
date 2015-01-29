/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kltn;

/**
 *
 * @author Vu
 */
public class PostData {
    public class PostDataVietcomBank{
        private String __EVENTTARGET;
        private String __EVENTARGUMENT;
        private String __LASTFOCUS;
        private String __VIEWSTATE;
        private String __VIEWSTATEENCRYPTED;

        public PostDataVietcomBank() {
            this.__EVENTTARGET = "";
            this.__EVENTARGUMENT = "";
            this.__LASTFOCUS = "";
            this.__VIEWSTATE = "";
            this.__VIEWSTATEENCRYPTED = "";
            
        }
        
        
        public PostDataVietcomBank(String __EVENTTARGET, String __EVENTARGUMENT, String __LASTFOCUS, String __VIEWSTATE, String __VIEWSTATEENCRYPTED) {
            this.__EVENTTARGET = __EVENTTARGET;
            this.__EVENTARGUMENT = __EVENTARGUMENT;
            this.__LASTFOCUS = __LASTFOCUS;
            this.__VIEWSTATE = __VIEWSTATE;
            this.__VIEWSTATEENCRYPTED = __VIEWSTATEENCRYPTED;
        }

        public String getEVENTTARGET() {
            return __EVENTTARGET;
        }

        public void setEVENTTARGET(String __EVENTTARGET) {
            this.__EVENTTARGET = __EVENTTARGET;
        }

        public String getEVENTARGUMENT() {
            return __EVENTARGUMENT;
        }

        public void setEVENTARGUMENT(String __EVENTARGUMENT) {
            this.__EVENTARGUMENT = __EVENTARGUMENT;
        }

        public String getLASTFOCUS() {
            return __LASTFOCUS;
        }

        public void setLASTFOCUS(String __LASTFOCUS) {
            this.__LASTFOCUS = __LASTFOCUS;
        }

        public String getVIEWSTATE() {
            return __VIEWSTATE;
        }

        public void setVIEWSTATE(String __VIEWSTATE) {
            this.__VIEWSTATE = __VIEWSTATE;
        }

        public String getVIEWSTATEENCRYPTED() {
            return __VIEWSTATEENCRYPTED;
        }

        public void setVIEWSTATEENCRYPTED(String __VIEWSTATEENCRYPTED) {
            this.__VIEWSTATEENCRYPTED = __VIEWSTATEENCRYPTED;
        }

       
        
    }
}
