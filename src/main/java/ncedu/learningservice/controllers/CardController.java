package ncedu.learningservice.controllers;


import ncedu.learningservice.entities.CardEntity;
import ncedu.learningservice.services.CardService;
import ncedu.learningservice.settings.GeneralSettings;
import ncedu.learningservice.wrappers.cardtask.CardWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping( value = GeneralSettings.CARD_CONTROLLER)
public class CardController {

    @Autowired
    private CardService service;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Iterable<CardEntity>> getAll(){
        return service.getAll();
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<CardEntity> addCard(@RequestBody CardWrapper wrapper){
        return service.addCard(wrapper);
    }





}
