package uj.java.w7.insurance;

import java.math.BigDecimal;

public class InsuranceEntry {
    int policyId;
    String county;
    BigDecimal tiv_2011;
    BigDecimal tiv_2012;

    InsuranceEntry(int policyId, String county, BigDecimal tiv_2011, BigDecimal tiv_2012){
        this.policyId = policyId;
        this.county = county;
        this.tiv_2011 = tiv_2011;
        this.tiv_2012 = tiv_2012;
    }

    public String county() {
        return county;
    }

    public BigDecimal tiv_2012() {
        return tiv_2012;
    }

    public BigDecimal sub_tiv(){
        return tiv_2012.subtract(tiv_2011);
    }

}
