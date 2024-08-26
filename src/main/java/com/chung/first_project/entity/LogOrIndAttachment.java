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
@Table(name="LOG_OR_IND_ATTACHMENT")
public class LogOrIndAttachment extends LogParent{

}
