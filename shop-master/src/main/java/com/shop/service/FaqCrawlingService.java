package com.shop.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import com.shop.entity.FaqCrawling;

@Service
public class FaqCrawlingService {

	private static String FAQ_URL = "https://www.tlj.co.kr:7008/shop/cs/faq.asp";

	@PostConstruct
	public List<FaqCrawling> getFaqs() throws IOException {
		
		List<FaqCrawling> faqCrawlingList = new ArrayList<>();
        Document doc = Jsoup.connect(FAQ_URL).get();
    	Elements contents = doc.select("div[class='q']");
    	
    	for(Element content : contents){
            Elements spanContents = content.select("span");

            FaqCrawling faqCrawling = FaqCrawling.builder()
                    .type(spanContents.get(1).text())
                    .title(spanContents.get(2).text())
                    .build();

            faqCrawlingList.add(faqCrawling);

        }
    	
    	return faqCrawlingList;
	}

}
