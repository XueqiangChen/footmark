package com.xueqiang.footmark.controller;

import com.xueqiang.footmark.model.PersonForm;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.validation.Valid;

@Controller
public class WebController implements WebMvcConfigurer {

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/results").setViewName("results");
	}

	/**
	 * returns the form template. It includes a PersonForm in its method
	 * signature so that the template can associate form attributes with a PersonForm
	 * @param personForm
	 * @return template
	 */
	@GetMapping("/personform")
	public String showForm(PersonForm personForm) {
		return "personForm";
	}

	/**
	 * If all of the person’s attribute are valid, it redirects the browser to the final results template.
	 * @param personForm marked with @Valid to gather the attributes filled out in the form.
	 * @param bindingResult so that you can test for and retrieve validation errors.
	 * @return
	 */
	@PostMapping("/personform")
	public String checkPersonInfo(@Valid PersonForm personForm, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return "personForm";
		}

		return "redirect:/results";
	}
}
