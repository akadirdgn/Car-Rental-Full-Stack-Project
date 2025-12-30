package com.carrentalsystem.resource;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import com.carrentalsystem.dto.AddDrivingLicenseRequest;
import com.carrentalsystem.dto.CommonApiResponse;
import com.carrentalsystem.dto.RegisterUserRequestDto;
import com.carrentalsystem.dto.UserDto;
import com.carrentalsystem.dto.UserLoginRequest;
import com.carrentalsystem.dto.UserLoginResponse;
import com.carrentalsystem.dto.UserResponseDto;
import com.carrentalsystem.dto.UserStatusUpdateRequestDto;
import com.carrentalsystem.entity.Address;
import com.carrentalsystem.entity.DrivingLicense;
import com.carrentalsystem.entity.User;
import com.carrentalsystem.exception.UserSaveFailedException;
import com.carrentalsystem.service.AddressService;
import com.carrentalsystem.service.DrivingLicenseService;
import com.carrentalsystem.service.StorageService;
import com.carrentalsystem.service.UserService;
import com.carrentalsystem.utility.Constants.ActiveStatus;
import com.carrentalsystem.utility.Constants.UserRole;
import com.carrentalsystem.utility.JwtUtils;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;

@Component
@Transactional
public class UserResource {

	private final Logger LOG = LoggerFactory.getLogger(UserResource.class);

	@Autowired
	private UserService userService;

	@Autowired
	private AddressService addressService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtUtils jwtUtils;

	@Autowired
	private StorageService storageService;

	@Autowired
	private DrivingLicenseService drivingLicenseService;

	public ResponseEntity<CommonApiResponse> registerAdmin(RegisterUserRequestDto registerRequest) {

		LOG.info("Request received for Register Admin");

		CommonApiResponse response = new CommonApiResponse();

		if (registerRequest == null) {
			response.setResponseMessage("kullanıcı boş olamaz");
			response.setSuccess(false);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		if (registerRequest.getEmailId() == null || registerRequest.getPassword() == null) {
			response.setResponseMessage("eksik bilgi");
			response.setSuccess(false);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		User existingUser = this.userService.getUserByEmailAndStatus(registerRequest.getEmailId(),
				ActiveStatus.ACTIVE.value());

		if (existingUser != null) {
			response.setResponseMessage("Bu E-posta adresi ile kayıtlı kullanıcı zaten var");
			response.setSuccess(false);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		User user = RegisterUserRequestDto.toUserEntity(registerRequest);

		user.setRole(UserRole.ROLE_ADMIN.value());
		user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
		user.setStatus(ActiveStatus.ACTIVE.value());

		existingUser = this.userService.addUser(user);

		if (existingUser == null) {
			response.setResponseMessage("admin kaydı başarısız oldu");
			response.setSuccess(false);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		response.setResponseMessage("Admin Başarıyla Kaydedildi");
		response.setSuccess(true);

		LOG.info("Response Sent!!!");

		return new ResponseEntity<CommonApiResponse>(response, HttpStatus.OK);
	}

	public ResponseEntity<CommonApiResponse> registerUser(RegisterUserRequestDto request) {

		LOG.info("Received request for register user");

		CommonApiResponse response = new CommonApiResponse();

		if (request == null) {
			response.setResponseMessage("kullanıcı boş olamaz");
			response.setSuccess(false);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		User existingUser = this.userService.getUserByEmailAndStatus(request.getEmailId(), ActiveStatus.ACTIVE.value());

		if (existingUser != null) {
			response.setResponseMessage("Bu E-posta adresi ile kayıtlı kullanıcı zaten var!!!");
			response.setSuccess(false);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		if (request.getRole() == null) {
			response.setResponseMessage("hatalı istek, Rol eksik");
			response.setSuccess(false);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		User user = RegisterUserRequestDto.toUserEntity(request);

		String encodedPassword = passwordEncoder.encode(user.getPassword());

		user.setStatus(ActiveStatus.ACTIVE.value());
		user.setPassword(encodedPassword);

		Address address = new Address();
		address.setCity(request.getCity());
		address.setPincode(request.getPincode());
		address.setStreet(request.getStreet());

		Address savedAddress = this.addressService.addAddress(address);

		if (savedAddress == null) {
			throw new UserSaveFailedException("Kayıt Başarısız, teknik bir sorun oluştu:(");
		}

		user.setAddress(savedAddress);

		existingUser = this.userService.addUser(user);

		if (existingUser == null) {
			throw new UserSaveFailedException("Kayıt Başarısız, teknik bir sorun oluştu:(");
		}

		response.setResponseMessage("Kullanıcı Başarıyla Kaydedildi");
		response.setSuccess(true);

		return new ResponseEntity<CommonApiResponse>(response, HttpStatus.OK);
	}

	public ResponseEntity<UserLoginResponse> login(UserLoginRequest loginRequest) {

		LOG.info("Received request for User Login");

		UserLoginResponse response = new UserLoginResponse();

		if (loginRequest == null) {
			response.setResponseMessage("Eksik Bilgi");
			response.setSuccess(false);

			return new ResponseEntity<UserLoginResponse>(response, HttpStatus.BAD_REQUEST);
		}

		String jwtToken = null;
		User user = null;

		List<GrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority(loginRequest.getRole()));

		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmailId(),
					loginRequest.getPassword(), authorities));
		} catch (Exception ex) {
			response.setResponseMessage("Geçersiz e-posta veya şifre.");
			response.setSuccess(false);
			return new ResponseEntity<UserLoginResponse>(response, HttpStatus.BAD_REQUEST);
		}

		jwtToken = jwtUtils.generateToken(loginRequest.getEmailId());

		user = this.userService.getUserByEmailIdAndRoleAndStatus(loginRequest.getEmailId(), loginRequest.getRole(),
				ActiveStatus.ACTIVE.value());

		UserDto userDto = UserDto.toUserDtoEntity(user);

		// user is authenticated
		if (jwtToken != null) {
			response.setUser(userDto);
			response.setResponseMessage("Giriş Başarılı");
			response.setSuccess(true);
			response.setJwtToken(jwtToken);
			return new ResponseEntity<UserLoginResponse>(response, HttpStatus.OK);
		}

		else {
			response.setResponseMessage("Giriş Başarısız");
			response.setSuccess(false);
			return new ResponseEntity<UserLoginResponse>(response, HttpStatus.BAD_REQUEST);
		}

	}

	public ResponseEntity<UserResponseDto> getUsersByRole(String role) {

		UserResponseDto response = new UserResponseDto();

		if (role == null) {
			response.setResponseMessage("Kullanıcı Bulunamadı");
			response.setSuccess(false);
			return new ResponseEntity<UserResponseDto>(response, HttpStatus.BAD_REQUEST);
		}

		List<User> users = new ArrayList<>();

		users = this.userService.getUserByRoleAndStatus(role, ActiveStatus.ACTIVE.value());

		if (users.isEmpty()) {
			response.setResponseMessage("Kullanıcı Bulunamadı");
			response.setSuccess(false);
			return new ResponseEntity<UserResponseDto>(response, HttpStatus.OK);
		}

		List<UserDto> userDtos = new ArrayList<>();

		for (User user : users) {

			UserDto dto = UserDto.toUserDtoEntity(user);

			userDtos.add(dto);

		}

		response.setUsers(userDtos);
		response.setResponseMessage("User Fetched Successfully");
		response.setSuccess(true);

		return new ResponseEntity<UserResponseDto>(response, HttpStatus.OK);
	}

	public ResponseEntity<CommonApiResponse> updateUserStatus(UserStatusUpdateRequestDto request) {

		LOG.info("Received request for updating the user status");

		CommonApiResponse response = new CommonApiResponse();

		if (request == null) {
			response.setResponseMessage("hatalı istek, veri eksik");
			response.setSuccess(false);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		if (request.getUserId() == 0) {
			response.setResponseMessage("hatalı istek, kullanıcı id eksik");
			response.setSuccess(false);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		User user = null;
		user = this.userService.getUserById(request.getUserId());

		user.setStatus(request.getStatus());

		User updatedUser = this.userService.updateUser(user);

		if (updatedUser == null) {
			throw new UserSaveFailedException("Kullanıcı durumu güncellenemedi");
		}

		response.setResponseMessage("Kullanıcı " + request.getStatus() + " Başarıyla!!!");
		response.setSuccess(true);
		return new ResponseEntity<CommonApiResponse>(response, HttpStatus.OK);

	}

	public ResponseEntity<UserResponseDto> getUserById(int userId) {

		UserResponseDto response = new UserResponseDto();

		if (userId == 0) {
			response.setResponseMessage("Geçersiz Giriş");
			response.setSuccess(false);
			return new ResponseEntity<UserResponseDto>(response, HttpStatus.BAD_REQUEST);
		}

		List<User> users = new ArrayList<>();

		User user = this.userService.getUserById(userId);
		users.add(user);

		if (users.isEmpty()) {
			response.setResponseMessage("Kullanıcı Bulunamadı");
			response.setSuccess(false);
			return new ResponseEntity<UserResponseDto>(response, HttpStatus.OK);
		}

		List<UserDto> userDtos = new ArrayList<>();

		for (User u : users) {

			UserDto dto = UserDto.toUserDtoEntity(u);

			userDtos.add(dto);

		}

		response.setUsers(userDtos);
		response.setResponseMessage("Kullanıcı Başarıyla Getirildi");
		response.setSuccess(true);

		return new ResponseEntity<UserResponseDto>(response, HttpStatus.OK);
	}

	public ResponseEntity<CommonApiResponse> deleteUserById(int userId) {

		CommonApiResponse response = new CommonApiResponse();

		if (userId == 0) {
			response.setResponseMessage("kullanıcı id eksik");
			response.setSuccess(false);
			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		User user = this.userService.getUserById(userId);

		if (user == null) {
			response.setResponseMessage("Kullanıcı bulunamadı");
			response.setSuccess(false);
			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		user.setStatus(ActiveStatus.DEACTIVATED.value());

		User updatedUser = this.userService.updateUser(user);

		if (updatedUser == null) {
			response.setResponseMessage("Kullanıcı Silinemedi");
			response.setSuccess(false);
			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.setResponseMessage("Kullanıcı Başarıyla Silindi!!");
		response.setSuccess(true);

		return new ResponseEntity<CommonApiResponse>(response, HttpStatus.OK);
	}

	public ResponseEntity<CommonApiResponse> addCustomerDrivingLicense(AddDrivingLicenseRequest request) {

		LOG.info("Request received for Register Admin");

		CommonApiResponse response = new CommonApiResponse();

		if (request == null) {
			response.setResponseMessage("hatalı istek - veri eksik");
			response.setSuccess(false);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		if (request.getCustomerId() == null || request.getLicenseNumber() == null || request.getLicensePic() == null
				|| request.getExpirationDate() == null) {
			response.setResponseMessage("eksik bilgi");
			response.setSuccess(false);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		User customer = this.userService.getUserById(request.getCustomerId());

		if (customer == null) {
			response.setResponseMessage("Müşteri bulunamadı");
			response.setSuccess(false);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		String licensePicName = this.storageService.storeLicenseImage(request.getLicensePic());

		DrivingLicense license = new DrivingLicense();
		license.setExpirationDate(request.getExpirationDate());
		license.setLicenseNumber(request.getLicenseNumber());
		license.setLicensePic(licensePicName);

		DrivingLicense addedLicense = this.drivingLicenseService.addLicense(license);

		if (addedLicense == null) {
			throw new UserSaveFailedException("Sürücü belgesi kaydedilemedi!");
		}

		customer.setLicense(addedLicense);

		User updatedCustomer = this.userService.updateUser(customer);

		if (updatedCustomer == null) {
			throw new UserSaveFailedException("Sürücü belgesi kaydedilemedi!");
		}

		response.setResponseMessage("Müşteri Sürücü Belgesi Başarıyla Eklendi.");
		response.setSuccess(true);

		LOG.info("Response Sent!!!");

		return new ResponseEntity<CommonApiResponse>(response, HttpStatus.OK);
	}

	public void fetchDrivingLicenceImage(String drivingLicense, HttpServletResponse resp) {
		System.out.println("request came for fetching driving license pic");
		System.out.println("Loading file: " + drivingLicense);
		Resource resource = storageService.loadLicenseImage(drivingLicense);
		if (resource != null) {
			try (InputStream in = resource.getInputStream()) {
				ServletOutputStream out = resp.getOutputStream();
				FileCopyUtils.copy(in, out);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		System.out.println("response sent!");
	}

}
