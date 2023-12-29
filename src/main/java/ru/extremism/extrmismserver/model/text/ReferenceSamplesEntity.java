package ru.extremism.extrmismserver.model.text;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "reference_samples", schema = "public", catalog = "testDb")
@IdClass(ReferenceSamplesEntityPK.class)
@ToString
public class ReferenceSamplesEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private UUID id;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "part")
    private int part;

    @Basic
    @Column(name = "order1")
    private String order1;


    @Basic
    @Column(name = "order2")
    private String order2;


    @Basic
    @Column(name = "order3")
    private String order3;


    @Basic
    @Column(name = "weight")
    private Double weight;


    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReferenceSamplesEntity that = (ReferenceSamplesEntity) o;
        return part == that.part && Objects.equals(id, that.id) && Objects.equals(order1,
                that.order1) && Objects.equals(order2, that.order2) && Objects.equals(order3,
                that.order3) && Objects.equals(weight, that.weight);
    }

    @Override public int hashCode() {
        return Objects.hash(id, part, order1, order2, order3, weight);
    }
}
