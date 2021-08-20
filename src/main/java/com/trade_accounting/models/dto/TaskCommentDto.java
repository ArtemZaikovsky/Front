package com.trade_accounting.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskCommentDto {

    private Long id;

    private String commentContent;

    private Long publisherId;

    private String publishedDateTime;

    private Long taskId;
}