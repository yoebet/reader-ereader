package wjy.yo.ereader.serviceimpl;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import wjy.yo.ereader.entityvo.dict.DictEntry;
import wjy.yo.ereader.service.DictService;

@Singleton
public class DictServiceImpl implements DictService {

    @Inject
    public DictServiceImpl(){
        System.out.println("new DictServiceImpl");
    }

    public DictEntry lookup(String word) {
        return null;
    }

}
