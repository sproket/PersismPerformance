package net.sf.persism.jpa.models;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "PostTypes", schema = "dbo", catalog = "StackOverflow2010")
public class PostType {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

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
        PostType postType = (PostType) o;
        return Objects.equals(id, postType.id) && Objects.equals(type, postType.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type);
    }
}
