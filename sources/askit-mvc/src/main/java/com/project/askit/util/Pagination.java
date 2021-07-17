package com.project.askit.util;

import java.util.ArrayList;
import java.util.List;

public class Pagination {

    public static List<Integer> getPagination(Integer currentPage, Integer pagesToCreate, Integer totalPages) {

        // Current page is the current page the pages are being created on
        // Max page is the maximum number of pages that are going to be created
        // Total pages is the number of total pages from which the pages will be created
        Integer min = currentPage - pagesToCreate / 2;
        Integer max = currentPage + pagesToCreate / 2;

        if (pagesToCreate > totalPages) {
            min = 0;
            max = totalPages - 1;
        }

        if (min < 0) {
            min = 0;
            max = pagesToCreate - 1;
        }

        if (max >= totalPages) {
            max = totalPages - 1;
            min = max - pagesToCreate + 1;
        }

        List<Integer> pages = new ArrayList<>();
        for (int i = min; i <= max; i++) {
            pages.add(i);
        }

        return pages;
    }

}
