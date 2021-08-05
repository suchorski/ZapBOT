package com.suchorski.zapbot.resources.social;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.suchorski.zapbot.exceptions.NothingFoundException;
import com.suchorski.zapbot.services.commands.social.ProfileBackgroundService;

@RestController
@RequestMapping("/background")
public class ProfileBackgroundsResource {

	@Autowired private ProfileBackgroundService profileBackgroundService;

	@GetMapping(value = "/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
	public byte[] profileBackground(@PathVariable Integer id) throws NothingFoundException {
		return profileBackgroundService.findById(id).getImage();
	}

}
