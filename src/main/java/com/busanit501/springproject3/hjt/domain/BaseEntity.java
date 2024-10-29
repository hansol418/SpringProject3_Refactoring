package com.busanit501.springproject3.hjt.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Date;


@MappedSuperclass
@EntityListeners(value = {AuditingEntityListener.class})
@Getter
public class BaseEntity {
    @CreatedDate
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @Column(name = "regDate", updatable = false)
    private Date regDate;

    @LastModifiedDate
    @JsonFormat(pattern = "yyyy-MM-dd HH-mm")
    @Column(name = "modDate")
    private LocalDateTime modDate;

}
