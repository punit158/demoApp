package org.demo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import io.swagger.annotations.Api;
import org.demo.repository.UserDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.demo.exception.ResourceNotFoundException;
import org.demo.model.UserDetails;


@Api(value = "UserDetailsController", description = "REST APIs related to UserDetails Entity!!!!")
@RestController
@RequestMapping("/api")
public class UserDetailsController {
	@Autowired
	private UserDetailsRepository userdetailsRepository;

	@GetMapping("/userDetails")
	public List<UserDetails> getAllUserDetails() {
		return userdetailsRepository.findAll();
	}

	@GetMapping("/userDetails/{id}")
	public ResponseEntity<UserDetails> getUserDetailsById(@PathVariable(value = "id") Long UserDetailsId)
			throws ResourceNotFoundException {
		UserDetails userDetails = userdetailsRepository.findById(UserDetailsId)
				.orElseThrow(() -> new ResourceNotFoundException("UserDetails not found for this id :: " + UserDetailsId));
		return ResponseEntity.ok().body(userDetails);
	}

	@PostMapping("/userDetails")
	public UserDetails createUserDetails(@Valid @RequestBody UserDetails userDetails) {
		return userdetailsRepository.save(userDetails);
	}

	@PutMapping("/userDetails/{id}")
	public ResponseEntity<UserDetails> updateUserDetails(@PathVariable(value = "id") Long UserDetailsId,
														 @Valid @RequestBody UserDetails userDetailsDetails) throws ResourceNotFoundException {
		UserDetails userDetails = userdetailsRepository.findById(UserDetailsId)
				.orElseThrow(() -> new ResourceNotFoundException("UserDetails not found for this id :: " + UserDetailsId));

		userDetails.setEmailId(userDetailsDetails.getEmailId());
		userDetails.setLastName(userDetailsDetails.getLastName());
		userDetails.setFirstName(userDetailsDetails.getFirstName());
		final UserDetails updatedUserDetails = userdetailsRepository.save(userDetails);
		return ResponseEntity.ok(updatedUserDetails);
	}

	@DeleteMapping("/userDetails/{id}")
	public Map<String, Boolean> deleteUserDetails(@PathVariable(value = "id") Long UserDetailsId)
			throws ResourceNotFoundException {
		UserDetails userDetails = userdetailsRepository.findById(UserDetailsId)
				.orElseThrow(() -> new ResourceNotFoundException("UserDetails not found for this id :: " + UserDetailsId));

		userdetailsRepository.delete(userDetails);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;
	}
}
