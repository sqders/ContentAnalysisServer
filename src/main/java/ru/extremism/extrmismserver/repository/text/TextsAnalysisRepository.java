package ru.extremism.extrmismserver.repository.text;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.extremism.extrmismserver.model.text.ReferenceSamplesEntity;

public interface TextsAnalysisRepository extends JpaRepository<ReferenceSamplesEntity, Long> {
}
