package io.macgyver.core.web.handlebars;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class DummyHandlebarsController {

	
	@RequestMapping("/handlebars")
	@ResponseBody
	public ModelAndView home() {

		return new ModelAndView("testhandlebars");
	
		
	}
}
