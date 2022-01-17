package com.elliot.demo.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

/**
 * @author shi-wei
 * @date 2022/1/15
 */
@Entity
@Data
public class ExchangeRate {

    @Id
    String id= UUID.randomUUID().toString();

    @Column
    String currencyId;

    @Column
    String rate;

    @Column
    Double reteFloat  ;

    @LastModifiedDate
    @Column(nullable = false)
    Date updated = new Date();
}
