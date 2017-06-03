package ru.meteo.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.val;
import lombok.extern.slf4j.Slf4j;
import ru.meteo.config.ApplicationProperties;
import ru.meteo.dto.IndexModel;

@Slf4j
@Controller
public class IndexController {
	
	@Autowired
	private ApplicationProperties properties;

	
	@RequestMapping(value="/")
    public String index() {
		log.info("index: ");
        return "index";
    }
	
	@ModelAttribute("IndexModel")
	public IndexModel indexModel() {
		val result = new IndexModel();
		result.setJeoqueryUsername(properties.getJeoqueryUsername());
		return result;
	}
	
}
