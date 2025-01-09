package mju.scholarship.scholoarship;

import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "scholarships")
@NoArgsConstructor
@Getter
public class ScholarshipDocument {

    @Id
    @Field(type = FieldType.Keyword)
    private String id;

    @Field(type = FieldType.Text)
    private String name;

    @Field(type = FieldType.Text)
    private String description;

    @Field(type = FieldType.Integer)
    private Integer price;

    @Field(type = FieldType.Text)
    private String category;

    @Field(type = FieldType.Text)
    private String university;

    @Field(type = FieldType.Integer)
    private Integer minAge;

    @Field(type = FieldType.Integer)
    private Integer maxAge;

    @Field(type = FieldType.Text)
    private String gender;

    @Field(type = FieldType.Text)
    private String province;

    @Field(type = FieldType.Text)
    private String city;

    @Field(type = FieldType.Text)
    private String department;

    @Field(type = FieldType.Double)
    private Double grade;

    @Field(type = FieldType.Integer)
    private Integer incomeQuantile;

    @Builder
    public ScholarshipDocument(String name, Integer price, String category, String description, String university, Integer minAge, Integer maxAge, String gender, String province, String city, String department, Double grade, Integer incomeQuantile) {
        this.name = name;
        this.price = price;
        this.category = category;
        this.description = description;
        this.university = university;
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.gender = gender;
        this.province = province;
        this.city = city;
        this.department = department;
        this.grade = grade;
        this.incomeQuantile = incomeQuantile;
    }
}