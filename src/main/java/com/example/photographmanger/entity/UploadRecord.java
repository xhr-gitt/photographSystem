package com.example.photographmanger.entity;




import lombok.Data;

import javax.persistence.*;


@Entity
@Table(name = "upload_records")
@Data
public class UploadRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;
    private String fileUrl;
    private Long fileSize;
}
