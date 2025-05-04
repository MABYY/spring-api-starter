package com.codewithmosh.store.dtos;

import lombok.Data;

@Data
public class ChangePasswordDTO {

   private String old_password;
   private String new_password;

}
