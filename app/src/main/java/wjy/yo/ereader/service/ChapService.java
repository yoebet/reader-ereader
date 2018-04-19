package wjy.yo.ereader.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wjy.yo.ereader.model.Chap;
import wjy.yo.ereader.model.Para;

/**
 * Created by wsx on 2018/4/19.
 */

public class ChapService {


    public static final Map<String, Chap> CHAP_MAP = new HashMap<String, Chap>();

    static {

        Chap c11 = new Chap("11", "Chap 1 fs", "第一章 zz");
        Chap c12 = new Chap("12", "Chap 2 zdg", "第二章 ee");

        Chap c21 = new Chap("21", "Chap 1 TSDs", "第一章 方法");
        Chap c22 = new Chap("22", "Chap 2 GSWEW", "第二章 说说");

        CHAP_MAP.put("11", c11);
        CHAP_MAP.put("12", c12);
        CHAP_MAP.put("21", c21);
        CHAP_MAP.put("22", c22);

        c11.setBookId("123");
        c12.setBookId("123");
        c21.setBookId("234");
        c22.setBookId("234");

        String bookId = "123";
        String chapId = "11";
        int pid = 111;

        Para p1 = new Para("" + (pid++), bookId, chapId, "Technology trends may push Silicon Valley back to the future.");
        Para p2 = new Para("" + (pid++), bookId, "11", "Carver Mead, a pioneer in integrated circuits and a professor of" +
                " computer science at the California Institute of Technology,");
        List<Para> paras = new ArrayList<>();
        paras.add(p1);
        paras.add(p2);
        c11.setParas(paras);

        chapId = "12";

        p1 = new Para("" + (pid++), bookId, chapId, "spawning a new generation of garage start-ups and giving the U.S. a jump on" +
                " its foreign rivals in getting new products to market fast.");
        p2 = new Para("" + (pid++), bookId, chapId, "'We're got more garages with smart people,' Mead observes. 'We really thrive on anarchy.'");
        paras = new ArrayList<>();
        paras.add(p1);
        paras.add(p2);
        c12.setParas(paras);

        bookId = "234";
        chapId = "21";

        p1 = new Para("" + (pid++), bookId, chapId, "And Chinese, Korean, Filipino and Indian engineers are graduating in droves from California's colleges.");
        p2 = new Para("" + (pid++), bookId, chapId, "As the heads of next-generation start-ups, these Asian innovators can draw on customs and languages" +
                " to forge tighter links with crucial Pacific Rim markets.");
        paras = new ArrayList<>();
        paras.add(p1);
        paras.add(p2);
        c21.setParas(paras);

        chapId = "22";

        p1 = new Para("" + (pid++), bookId, chapId, "For instance, Alex Au, a Stanford Ph.D. from Hong Kong, has set up a Taiwan factory" +
                " to challenge Japan's near lock on the memory-chip market.");
        p2 = new Para("" + (pid++), bookId, chapId, "India-born N.Damodar Reddy's tiny California company reopened an AT & T chip plant" +
                " in Kansas City last spring with financing from the state of Missouri.");
        paras = new ArrayList<>();
        paras.add(p1);
        paras.add(p2);
        c22.setParas(paras);

    }

}
