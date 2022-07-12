package model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
public class student {
    private String studentId;
   private String studentName;
   private String email;
   private String contact;
   private String address;
   private String nic;
}
