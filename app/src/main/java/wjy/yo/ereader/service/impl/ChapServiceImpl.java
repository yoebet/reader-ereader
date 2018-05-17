package wjy.yo.ereader.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import wjy.yo.ereader.model.Chap;
import wjy.yo.ereader.model.Para;
import wjy.yo.ereader.service.ChapService;

/**
 * Created by wsx on 2018/4/19.
 */

@Singleton
public class ChapServiceImpl implements ChapService {

    @Inject
    public ChapServiceImpl(){
        System.out.println("new ChapServiceImpl");
    }

}
