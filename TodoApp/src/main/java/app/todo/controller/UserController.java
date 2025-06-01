package app.todo.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.todo.annotation.ExecutionTime;
import app.todo.pojo.UserDto;
import app.todo.pojo.UserRequestModel;
import app.todo.pojo.UserResponseModel;
import app.todo.service.IUserService;
import app.todo.singleton.ModelMapperSingleton;

@RestController
@RequestMapping("/users")
public class UserController {
	
	@Autowired
	private IUserService service;
	
	@PostMapping("/createUser")
	@ExecutionTime
	public ResponseEntity<UserResponseModel> createUser(@Validated @RequestBody UserRequestModel userReq){
//		ModelMapper mapper = new ModelMapper();
//		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		
		ModelMapper mapper = ModelMapperSingleton.mapper();
		UserDto userDto = mapper.map(userReq, UserDto.class);
		
		UserDto createdUser = service.createUser(userDto);
		UserResponseModel userResp = mapper.map(createdUser, UserResponseModel.class);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(userResp);
	}
}
