package ru.practicum.ewm.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ViewStats {

    private final String app;
    private final String uri;
    private final Long hits;
}
