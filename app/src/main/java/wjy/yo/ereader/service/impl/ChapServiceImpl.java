package wjy.yo.ereader.service.impl;

import javax.inject.Inject;
import javax.inject.Singleton;

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
