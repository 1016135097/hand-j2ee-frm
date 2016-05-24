/********************************************************
  EO of CUX_TEST_TBL
********************************************************/
package cux.oracle.apps.test.entity;

import hand.framework.ebs.entity.StandardEntity;

import java.math.BigDecimal;

import java.sql.Date;

public class CuxTestTblEO extends StandardEntity { 

    public CuxTestTblEO(){
    }

    private BigDecimal myId;

    private String myName;

    private String myOtherAttribtues;

    private String attirbuteCategory;

    private String attribute1;

    private BigDecimal rowVersionNumber;


    public void setMyId(BigDecimal value){
      this.myId = value;
    }

    public BigDecimal getMyId() {

      return this.myId;
    }

    public void setMyName(String value){
      this.myName = value;
    }

    public String getMyName() {

      return this.myName;
    }

    public void setMyOtherAttribtues(String value){
      this.myOtherAttribtues = value;
    }

    public String getMyOtherAttribtues() {

      return this.myOtherAttribtues;
    }

    public void setAttirbuteCategory(String value){
      this.attirbuteCategory = value;
    }

    public String getAttirbuteCategory() {

      return this.attirbuteCategory;
    }

    public void setAttribute1(String value){
      this.attribute1 = value;
    }

    public String getAttribute1() {

      return this.attribute1;
    }

    public void setRowVersionNumber(BigDecimal value){
      this.rowVersionNumber = value;
    }

    public BigDecimal getRowVersionNumber() {

      return this.rowVersionNumber;
    }

}