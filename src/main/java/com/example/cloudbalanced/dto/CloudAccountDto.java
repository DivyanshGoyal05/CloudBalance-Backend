
package com.example.cloudbalanced.dto;





import com.example.cloudbalanced.model.AccountStatus;
import com.example.cloudbalanced.model.CloudProvider;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;



import java.util.Set;





@Data
@AllArgsConstructor
@NoArgsConstructor
public class CloudAccountDto {
    private Long id;
    private String name;
    private String accountId;
    private CloudProvider provider;
    private String region;
    private String arnRole;
    private Set<String> assignedTo;
    private AccountStatus status;
    private Date createdAt;
}
