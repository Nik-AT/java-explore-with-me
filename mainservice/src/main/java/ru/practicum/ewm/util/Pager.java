package ru.practicum.ewm.util;

import org.springframework.data.domain.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Pager<T> {

    public Page<T> getPage(List<T> list, int from, int size) {
        Pageable pageable = getPageable(from, size);
        int startOfPage = (int) pageable.getOffset();
        int endOfPage = Math.min((startOfPage + pageable.getPageSize()), list.size());
        return new PageImpl<>(list.subList(startOfPage, endOfPage), pageable, list.size());
    }

    private Pageable getPageable(int from, int size) {
        return PageRequest.of(from, size, Sort.by("eventDate").descending());
    }
}
