package com.codewithmosh.store.dtos;

import lombok.Data;

@Data
public class ChangePasswordDTO {

   public String old_password;
   public String new_password;

}
