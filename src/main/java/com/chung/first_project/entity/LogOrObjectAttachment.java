package com.chung.first_project.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name="LOG_OR_OBJECT_ATTACHMENT")
public class LogOrObjectAttachment extends LogParent{

}
