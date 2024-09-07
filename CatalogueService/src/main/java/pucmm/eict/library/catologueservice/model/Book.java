package pucmm.eict.library.catologueservice.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import pucmm.eict.library.catologueservice.util.ObjectIdDeserializer;
import pucmm.eict.library.catologueservice.util.ObjectIdSerializer;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "books")
public class Book {

    @Id
    @JsonDeserialize(using = ObjectIdDeserializer.class)
    @JsonSerialize(using = ObjectIdSerializer.class)
    private ObjectId id;

    private String title;
    private String description;
    private String author;
    private List<String> genres;
    private double price;
    private String cover;
    private LocalDateTime createdAt;
}


