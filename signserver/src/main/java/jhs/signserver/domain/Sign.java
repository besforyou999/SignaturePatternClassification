package jhs.signserver.domain;

import java.sql.Date;

public class Sign {
    private Long id;
    // private Json data?
    private String imgLink; // img URL
    private int type;       // 0 == number, 1 == Number, 2 == Korean, 3 == English
    private Date created;   // create date

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImgLink() {
        return imgLink;
    }

    public void setImgLink(String imgLink) {
        this.imgLink = imgLink;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
