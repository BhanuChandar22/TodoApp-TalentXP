package todo.app.singleton;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import todo.app.dto.UserResponse;
import todo.app.entity.UserEntity;

public class SingletonModelMapper {

	private static final ModelMapper mapper = new ModelMapper();

	private SingletonModelMapper() {
	}

	static {
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		mapper.typeMap(UserEntity.class, UserResponse.class).addMappings(m -> {
			m.skip(UserResponse::setRoles);
		});
	}

	public static <D> D mapData(Object source, Class<D> destinationType) {
		return mapper.map(source, destinationType);
	}

	public static <T> T mapData(Object source, T destination) {
		mapper.map(source, destination);
		return destination;
	}

	public static ModelMapper getMapper() {
		return mapper;
	}
}