package jp.co.axa.apidemo.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Employee {

  
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    
    @Column(name="EMPLOYEE_NAME")
    private String name;

    
    @Column(name="EMPLOYEE_SALARY")
    private Integer salary;

    
    @Column(name="DEPARTMENT")
    private String department;

}
