package ru.extremism.extrmismserver.model.text;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
public class ReferenceSamplesEntityPK implements Serializable {
    private UUID id;
    private int part;
}
