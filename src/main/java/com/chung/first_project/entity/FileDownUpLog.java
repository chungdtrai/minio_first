package com.chung.first_project.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "file_up_down_log")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileDownUpLog extends LogParent{

}
