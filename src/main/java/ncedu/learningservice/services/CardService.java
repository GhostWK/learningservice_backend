package ncedu.learningservice.services;


import ncedu.learningservice.entities.CardEntity;
import ncedu.learningservice.repositories.CardRepository;
import ncedu.learningservice.wrappers.cardtask.CardWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class CardService {

    @Autowired
    private CardRepository cardRepository;

    public ResponseEntity<Iterable<CardEntity>> getAll(){
        return new ResponseEntity<Iterable<CardEntity>>(cardRepository.findAll(), HttpStatus.OK);
    }

    public ResponseEntity<CardEntity> addCard(CardWrapper wrapper){
        CardEntity entity = new CardEntity(wrapper);
        entity = cardRepository.save(entity);
        return new ResponseEntity<CardEntity>(entity, HttpStatus.OK);
    }
}
