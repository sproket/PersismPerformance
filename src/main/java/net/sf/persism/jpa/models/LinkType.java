package net.sf.persism.jpa.models;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "LinkTypes", schema = "dbo", catalog = "StackOverflow2010")
public class LinkType {

    @Id
    @Column(name = "Id")
    private Integer id;

    @Basic
    @Column(name = "Type")
    private String type;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LinkType linkType = (LinkType) o;
        return Objects.equals(id, linkType.id) && Objects.equals(type, linkType.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type);
    }
}
