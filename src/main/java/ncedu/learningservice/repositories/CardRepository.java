package ncedu.learningservice.repositories;

import ncedu.learningservice.entities.CardEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CardRepository extends JpaRepository<CardEntity, Long> {
    Optional<CardEntity> findByWordAndTranslation(String word, String translation);
}
