package ncedu.learningservice.services;


import ncedu.learningservice.entities.CardEntity;
import ncedu.learningservice.entities.TaskEntity;
import ncedu.learningservice.repositories.CardRepository;
import ncedu.learningservice.repositories.TaskRepository;
import ncedu.learningservice.serializerwrappers.ChoosingTranslationTaskSerializerWrapper;
import ncedu.learningservice.settings.GeneralSettings;
import ncedu.learningservice.wrappers.cardtask.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ChoosingTranslationTaskService {


    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private CardRepository cardRepository;




    public ResponseEntity<TaskEntity> addTask(ChoosingTranslationTaskWrapper wrapper){
        ChoosingTranslationTaskSerializerWrapper c = new ChoosingTranslationTaskSerializerWrapper(wrapper.getCardsIds());
        byte[] bytes = Serializer.getBytes(c);

        TaskEntity task = new TaskEntity(GeneralSettings.CHOOSING_TASK_BASIC_TYPE, bytes, null);
        taskRepository.save(task);
        return new ResponseEntity<TaskEntity>(task, HttpStatus.OK);
    }


    public ResponseEntity<TaskEntity> getTaskById(Long id){
        TaskEntity task = taskRepository.findOne(id);
        if(task == null){
            return new ResponseEntity<TaskEntity>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<TaskEntity>(task, HttpStatus.OK);
    }

    public ResponseEntity<ChoosingTranslationTaskWrapper> getById(Long id){
        TaskEntity task = taskRepository.findOne(id);
        if(task == null){
            return new ResponseEntity<ChoosingTranslationTaskWrapper>(HttpStatus.NOT_FOUND);
        }

        ChoosingTranslationTaskSerializerWrapper c = (ChoosingTranslationTaskSerializerWrapper) Serializer.toObject(task.getTask());
        ChoosingTranslationTaskWrapper wrapper = new ChoosingTranslationTaskWrapper(c.getCardsIds());
        return new ResponseEntity<ChoosingTranslationTaskWrapper>(wrapper, HttpStatus.OK);
    }


    public ResponseEntity<ChoosingTranslationWrapper> getTask(Long id){
        TaskEntity task = taskRepository.findOne(id);
        if(task == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (task.getType().equalsIgnoreCase(GeneralSettings.CHOOSING_TASK_BASIC_TYPE)){
            return getBasicTask(task);
        }
        if(task.getType().equalsIgnoreCase(GeneralSettings.CHOOSING_TASK_ADVANCED_TYPE)){
            return getAdvancedTask(task);
        }
        return null;
    }

    public ResponseEntity<CardResponseWrapper> checkTranslation(CardWrapperIdAndTranslation wrapper) {
        CardEntity entity = cardRepository.findOne(wrapper.getId());
        if(entity == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if(entity.getTranslation().equalsIgnoreCase(wrapper.getTranslation())){
            //TODO increment the user's score
        } else {
            //TODO decrement the user's score
        }
        return new ResponseEntity<CardResponseWrapper>(new CardResponseWrapper(entity.getTranslation()), HttpStatus.OK);
    }

    private ResponseEntity<ChoosingTranslationWrapper> getBasicTask(TaskEntity task){
        ChoosingTranslationTaskSerializerWrapper c1 = (ChoosingTranslationTaskSerializerWrapper) Serializer.toObject(task.getTask());

        //Getting cards which will be used in task
        List<CardEntity> cards = new ArrayList<>();
        for (int i = 0; i < c1.getCardsIds().length; i++) {
            CardEntity entity = cardRepository.findOne(c1.getCardsIds()[i]);
            if(entity == null) continue;
            cards.add(entity);
        }
        return new ResponseEntity<ChoosingTranslationWrapper>(createTask(cards), HttpStatus.OK);
    }

    private ResponseEntity<ChoosingTranslationWrapper> getAdvancedTask(TaskEntity task){
        return null;
    }

    private ChoosingTranslationWrapper createTask(List<CardEntity> entities){
        List<ChoosingTranslationCardWrapper> list = new ArrayList<>();

        for (int i = 0; i < entities.size(); i++) {
            CardEntity current = entities.get(i);
            ChoosingTranslationCardWrapper iteration = new ChoosingTranslationCardWrapper();
            iteration.setWord(current.getWord());
            iteration.setPossibleTranslations(getPossibleTranslations(entities, current));
            iteration.setId(current.getId());
            list.add(iteration);
        }
        Collections.shuffle(list);
        ChoosingTranslationCardWrapper[] array = new ChoosingTranslationCardWrapper[list.size()];
        list.toArray(array);
        return new ChoosingTranslationWrapper(array);
    }

    private String[] getPossibleTranslations(List<CardEntity> list, CardEntity current){
        Random r = new Random();
        int num = list.size() > 4 ? 4 : list.size();
        String[] possibleTranslations = new String[num];
        possibleTranslations[0] = current.getTranslation();
        for (int j = 1; j < num; j++) {
            String temp = null;
            Iteration:do{
                int rand = r.nextInt(list.size());
                String possible = list.get(rand).getTranslation();
                for (int i = 0; i < num; i++) {
                    if(possible.equalsIgnoreCase(possibleTranslations[i])) continue  Iteration;
                }
                temp = possible;
                break;
            }while (true);
            possibleTranslations[j] = temp;
        }

        List<String> stringList = Arrays.asList(possibleTranslations);
        Collections.shuffle(stringList);
        return (String[]) stringList.toArray();
    }

    public ResponseEntity createBasicTask(BasicTaskWrapper wrapper){
        CardWrapper[] array = wrapper.getArray();
        List<Long> list = new ArrayList<>();
        for (CardWrapper x: array) {
            Optional<CardEntity> entityOptional = cardRepository.findByWordAndTranslation(x.getWord(), x.getTranslation());
            if(entityOptional.isPresent()){
                //the DB contains this word
                list.add(entityOptional.get().getId());
            }else{
                //There is no such field in DB. Creating it
                CardEntity current = new CardEntity(x);
                current = cardRepository.save(current);
                list.add(current.getId());
            }
        }
        long[] ids = new long[list.size()];
        for (int i = 0; i < list.size(); i++) {
            ids[i] = list.get(i);
        }
        return addTask(new ChoosingTranslationTaskWrapper(ids));
    }

    public ResponseEntity createAdvancedTask(AdvancedTaskWrapper wrapper){
        return null;
    }
}
