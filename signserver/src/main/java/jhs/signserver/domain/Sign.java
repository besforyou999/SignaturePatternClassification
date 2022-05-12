package jhs.signserver.domain;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name="sign")
public class Sign {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // private Json data?
    private String data; // img URL
    private byte[] image;
    private int label;       // 0 == number, 1 == Number, 2 == Korean, 3 == English
    private Date created;   // create date

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getLabel() {
        return label;
    }

    public void setLabel(int label) {
        this.label = label;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
