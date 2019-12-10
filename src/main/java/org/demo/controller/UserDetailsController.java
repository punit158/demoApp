package org.demo.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import io.swagger.annotations.Api;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.demo.repository.UserDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.demo.exception.ResourceNotFoundException;
import org.demo.model.UserDetails;
import org.springframework.web.multipart.MultipartFile;


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

	@PostMapping("/bulkUpload")
	public ResponseEntity<String> mapReapExcelDatatoDB(@RequestParam("uploadFile") MultipartFile reapExcelDataFile) throws IOException {

		List<UserDetails> tempStudentList = new ArrayList<UserDetails>();
		XSSFWorkbook workbook = new XSSFWorkbook(reapExcelDataFile.getInputStream());
		XSSFSheet worksheet = workbook.getSheetAt(0);

		for(int i=1;i<worksheet.getPhysicalNumberOfRows() ;i++) {
			UserDetails userDetails = new UserDetails();

			XSSFRow row = worksheet.getRow(i);

			userDetails.setFirstName(row.getCell(0).getStringCellValue());
			userDetails.setLastName(row.getCell(1).getStringCellValue());
			userDetails.setEmailId(row.getCell(2).getStringCellValue());
			userDetails.setPassportNumber(row.getCell(3).getStringCellValue());
			userdetailsRepository.save(userDetails);

		}
		return ResponseEntity.ok().body("File Uploaded Successfully");
	}
}
