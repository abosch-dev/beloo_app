package com.beloo.test.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.locationtech.jts.geom.Point;

import java.time.Instant;

@Table(name = "location")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    @Column(name = "user_id")
    private String ciclystId;

    private Instant timeStamp;

    @Column(columnDefinition = "geometry(Point, 4326)")
    private Point position;
}
