package id.co.myrepublic.salessupport.model;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * Created by myrepublicid on 2/10/17.
 */

public class ResponseUserSelect implements Serializable {

    private static final long serialVersionUID = 1221356393249153455L;

    private Particulars particulars;

    public Particulars getParticulars() {
        return particulars;
    }

    public void setParticulars(Particulars particulars) {
        this.particulars = particulars;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
