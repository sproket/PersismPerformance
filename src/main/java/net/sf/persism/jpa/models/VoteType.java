package net.sf.persism.jpa.models;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "VoteTypes", schema = "dbo", catalog = "StackOverflow2010")
public class VoteType {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        VoteType voteType = (VoteType) o;
        return Objects.equals(id, voteType.id) && Objects.equals(name, voteType.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
