package wjy.yo.ereader.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import wjy.yo.ereader.model.DictEntry;
import wjy.yo.ereader.model.DictEntry.MeaningItem;
import wjy.yo.ereader.model.DictEntry.PosMeanings;
import wjy.yo.ereader.model.DictEntry.SimpleMeaning;
import wjy.yo.ereader.service.DictService;

@Singleton
public class DictServiceImpl implements DictService {

    static Map<String, DictEntry> dict = new HashMap<>();

    static {
        DictEntry company = new DictEntry("company");
        SimpleMeaning[] sms = new SimpleMeaning[2];
        sms[0] = new SimpleMeaning("n", "公司");
        sms[1] = new SimpleMeaning("v", "陪伴");
        company.setSimpleMeanings(sms);
        PosMeanings[] cms = new PosMeanings[2];
        MeaningItem[] mis = new MeaningItem[2];
        mis[0] = new MeaningItem(1, "陪伴，做伴");
        mis[1] = new MeaningItem(2, "公司");
        cms[0] = new PosMeanings("n", mis);
        MeaningItem[] mis2 = new MeaningItem[2];
        mis2[0] = new MeaningItem(3, "交往，结交");
        mis2[1] = new MeaningItem(4, "陪同，陪伴，伴随");
        cms[1] = new PosMeanings("v", mis2);
        company.setCompleteMeanings(cms);
        company.setPhonetic("kʌmpəni");
        Map<String, Object> categories = new HashMap<>();
        categories.put("cet", 4);
        categories.put("SD", 10000);
        company.setCategories(categories);
//        company.setBaseForms(new String[]{});
        company.setForms(new String[]{"company", "companied", "companies", "companying"});

        dict.put("company", company);
    }

    @Inject
    public DictServiceImpl(){
        System.out.println("new DictServiceImpl");
    }

    public DictEntry lookup(String word) {
        return dict.get(word);
    }

}
