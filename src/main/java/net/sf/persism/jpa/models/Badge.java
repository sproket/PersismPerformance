package net.sf.persism.jpa.models;

import javax.persistence.*;
import java.sql.Date;
import java.util.Objects;

@Entity
@Table(name = "Badges", schema = "dbo", catalog = "StackOverflow2010")
public class Badge {

    @Id
    @Column(name = "Id")
    private Integer id;

    @Basic
    @Column(name = "Name")
    private String name;

    @Basic
    @Column(name = "UserId")
    private Integer userId;

    @Basic
    @Column(name = "Date")
    private Date date;

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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Badge badge = (Badge) o;
        return Objects.equals(id, badge.id) && Objects.equals(name, badge.name) && Objects.equals(userId, badge.userId) && Objects.equals(date, badge.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, userId, date);
    }
}
