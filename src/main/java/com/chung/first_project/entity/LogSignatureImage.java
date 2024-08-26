package com.chung.first_project.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Table(name="LOG_SIG_SIGNATURE_IMAGE")
public class LogSignatureImage extends LogParent{

}
