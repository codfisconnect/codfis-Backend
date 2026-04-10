package com.example.codeFiz.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class TrainerDTO {

    private String name;
    private String gender;
    private String email;
    @Id
    private String mobile;
    private String description;
    private String status;

    @Lob
    @Column(name = "resume", columnDefinition = "LONGBLOB")
    private byte[] resume;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_type")
    private String fileType;

}
