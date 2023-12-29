package ru.extremism.extrmismserver.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnalyzedText {
    private String id;
    private List<String> signatures;
    private double weight;
}
