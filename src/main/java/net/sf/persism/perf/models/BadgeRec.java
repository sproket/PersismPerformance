package net.sf.persism.perf.models;

import net.sf.persism.annotations.Table;

import java.sql.Timestamp;
@Table("Badges")
public record BadgeRec(Integer id, String name, Integer userId, Timestamp date) {
}
