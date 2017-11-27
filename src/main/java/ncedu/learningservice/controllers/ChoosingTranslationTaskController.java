package ncedu.learningservice.controllers;


import ncedu.learningservice.entities.TaskEntity;
import ncedu.learningservice.services.ChoosingTranslationTaskService;
import ncedu.learningservice.settings.GeneralSettings;
import ncedu.learningservice.wrappers.cardtask.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = GeneralSettings.CHOOSING_TRANSLATION_TASK)
public class ChoosingTranslationTaskController {

    @Autowired
    private ChoosingTranslationTaskService service;

//    @RequestMapping(method = RequestMethod.GET)
//    public ResponseEntity<ChoosingTranslationWrapper> testing(){
//        ChoosingTranslationCardWrapper c1 = new ChoosingTranslationCardWrapper("нож", new String[]{"knife, smth, smth, smth"});
//        ChoosingTranslationCardWrapper c2 = new ChoosingTranslationCardWrapper("нож", new String[]{"knife, smth, smth, smth"});
//        ChoosingTranslationCardWrapper c3 = new ChoosingTranslationCardWrapper("нож", new String[]{"knife, smth, smth, smth"});
//        ChoosingTranslationCardWrapper c4 = new ChoosingTranslationCardWrapper("нож", new String[]{"knife, smth, smth, smth"});
//        ChoosingTranslationCardWrapper c5 = new ChoosingTranslationCardWrapper("нож", new String[]{"knife, smth, smth, smth"});
//        ChoosingTranslationCardWrapper c6 = new ChoosingTranslationCardWrapper("нож", new String[]{"knife, smth, smth, smth"});
//
//        ChoosingTranslationWrapper res = new ChoosingTranslationWrapper(new ChoosingTranslationCardWrapper[]{c1, c2, c3, c4, c5, c6});
//        return new ResponseEntity<ChoosingTranslationWrapper>(res, HttpStatus.OK);
//    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<TaskEntity> addTask(@RequestBody ChoosingTranslationTaskWrapper wrapper){
        return service.addTask(wrapper);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}/task")
    public ResponseEntity<TaskEntity> getTaskById(@PathVariable Long id){
        return service.getTaskById(id);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public ResponseEntity<ChoosingTranslationTaskWrapper> get(@PathVariable Long id){
        return service.getById(id);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/task/{id}")
    public ResponseEntity<ChoosingTranslationWrapper> getTask(@PathVariable Long id){
        return service.getTask(id);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/task/result")
    public ResponseEntity<CardResponseWrapper> checkTranslation(@RequestBody CardWrapperIdAndTranslation wrapper){
        return service.checkTranslation(wrapper);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/create/basic")
    public ResponseEntity createBasicTask(@RequestBody BasicTaskWrapper wrapper){
        return service.createBasicTask(wrapper);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/create/advanced")
    public ResponseEntity createAdvancedTask(@RequestBody AdvancedTaskWrapper wrapper){
        return service.createAdvancedTask(wrapper);
    }

}
