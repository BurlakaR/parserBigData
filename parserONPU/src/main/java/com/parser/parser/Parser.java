package com.parser.parser;



import com.parser.dto.Product;
import com.parser.repositories.DBController;
import com.parser.repositories.DBcontrollerProd;
import lombok.extern.java.Log;;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;


@Service
@Log
@Configurable
public class Parser implements ParserInterface{

    private static final String TEST_URL="https://www.wikipedia.org/";

    private static final String A_HREF = "a[href]";
    private static final String H1_DETAIL = "h1[itemprop].detail-title";
    private static final String MERA_PRICE = "meta[itemprop]";
    private static final String IMG_BASE = "img#base_image";
    private static final String DIV_DESCRIPT = "div.short-description";
    private static final String DIV_DESCRIPT2 = "div.short-text";


    private static final String SRC = "src";
    private static final String ITEMPROP = "itemprop";
    private static final String CONTENT = "content";
    private static final String PRICE = "price";
    private static final String ABS_HREF = "abs:href";

    private static int THREAD_NUMBER=20;

    Queue<String> linkQueue = new LinkedBlockingQueue<>();
    Queue<Product> productQueue = new LinkedBlockingQueue<>();
    Queue<String> urlQueue = new LinkedBlockingQueue<>();



    @Autowired
    private DBcontrollerProd dBcontroller;

    @Autowired
    private DBController dBcontrollerLink;


    @PostConstruct
    private void init(){
        saver();
        sender();
        urlQueue.add("https://rozetka.com.ua/dell_cel3060_4_500_dvd/p18581717/");
        for(int i=0;i<THREAD_NUMBER;i++){
            parse(i);
        }
    }

    public void sender(){
        new Thread(() -> {
            while (true) {
                try {
                    if(linkQueue.peek()!=null)
                        if(dBcontrollerLink.BDCheck(linkQueue.peek())) {
                            urlQueue.add(linkQueue.poll());
                        }
                        else linkQueue.poll();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
//Saving
    public void saver(){

        new Thread(() -> {
            int i=0;
            while (true) {
                if(productQueue.peek()!=null) {
                    System.out.println((i++) + "Saved");
                    dBcontroller.save(productQueue.poll());
                }
            }
        }).start();
    }

public void testFightOrDie(){
    try {
        Document doc= Jsoup.connect(TEST_URL).get();
    } catch (IOException e) {
        e.printStackTrace();
    }

}

    public void parse(int number) {

        new Thread(() -> {

                testFightOrDie();
                System.out.println("Parser #" + number + " is ready.");


                while (true)
                    if (urlQueue.peek() != null) {
                        try {
                        String url = urlQueue.poll();
                        Document doc = Jsoup.connect(url).get();
                        Elements links = doc.select(A_HREF);
                        Elements names = doc.select(H1_DETAIL);
                        Elements prices = doc.select(MERA_PRICE);
                        Elements images = doc.select(IMG_BASE);
                        Elements descriptions = doc.select(DIV_DESCRIPT);
                        if (descriptions.size() == 0) descriptions = doc.select(DIV_DESCRIPT2);


                        for (Element link : links) {
                            linkQueue.add(link.attr(ABS_HREF));
                        }


                        String name = "";
                        String price = "";
                        String img = "";
                        String description = "";
                        try {
                            name = names.get(0).text();
                            img = images.get(0).attr(SRC);
                            description = descriptions.get(0).text();
                            for (Element priceT : prices) {
                                if (priceT.attr(ITEMPROP).equals(PRICE)) {
                                    price = priceT.attr(CONTENT);
                                    break;
                                }
                            }

                            productQueue.add(new Product(url, name, price, img, description));

                        } catch (IndexOutOfBoundsException e) {
                        }


                    } catch(Exception e){
                }
            }

        }).start();
    }


}
