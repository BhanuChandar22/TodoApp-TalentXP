package app.todo.serviceImpl;

import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.todo.entity.UserEntity;
import app.todo.pojo.UserDto;
import app.todo.repository.IUserRepo;
import app.todo.service.IUserService;
import app.todo.singleton.ModelMapperSingleton;

@Service
public class UserServiceImpl implements IUserService {

	@Autowired
	private IUserRepo repo;
	
	
	@Override
	public UserDto createUser(UserDto userDto) {
		
		userDto.setUserId(UUID.randomUUID().toString());
		userDto.setEncryptedPassword("no pass");
		
//		ModelMapper mapper = new ModelMapper();
//		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		ModelMapper mapper = ModelMapperSingleton.mapper();
		
		UserEntity convertedToEntity = mapper.map(userDto, UserEntity.class);
		
		UserEntity userEntity = repo.save(convertedToEntity);
		UserDto dto = mapper.map(userEntity, UserDto.class);
		
		return dto;
	}

}
