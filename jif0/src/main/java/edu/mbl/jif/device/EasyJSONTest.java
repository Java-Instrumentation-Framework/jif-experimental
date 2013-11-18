/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.mbl.jif.device;

import java.util.Date;
import java.util.HashMap;
import javax.xml.crypto.Data;
//import net.sf.easyjson.JSON;

/**
 *
 * @author GBH
 */
public class EasyJSONTest {

    public static void main(String[] args) {
//        EasyJSONTest t = new EasyJSONTest();
//        String s = JSON.getJSONString(t);
//        System.out.println(JSON.getJSONString(t));
//        EasyJSONTest x=(EasyJSONTest)JSON.getObject(s);
    }

   	private HashMap map;
   	
   	private String param1;
   	
   	private Date param2;
   
    public HashMap getMap() {
        return map;
    }

    public void setMap(HashMap map) {
        this.map = map;
    }
    
    public void setString(String s){
    	this.param1=s;
   	}
    
    public void setDate(Date s){
    	this.param2=s;
   	}
   	
   	public Date getDate(){
   		return param2;
   	}
   	
   	public String getString(){
   		return param1;
   	}	
}

