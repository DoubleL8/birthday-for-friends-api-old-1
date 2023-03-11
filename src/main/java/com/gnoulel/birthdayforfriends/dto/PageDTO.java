package com.gnoulel.birthdayforfriends.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageDTO<T> {
    private List<T> data;
    private int currentPage;
    private int totalPages;
    private long totalItems;
}
