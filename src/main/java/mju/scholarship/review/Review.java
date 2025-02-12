package mju.scholarship.review;

import jakarta.persistence.*;
import mju.scholarship.scholoarship.Scholarship;
import mju.scholarship.util.BaseTimeEntity;

@Entity
public class Review extends BaseTimeEntity {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Scholarship scholarship;

    private String content;
}
