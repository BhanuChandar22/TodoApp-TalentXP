package app.todo.singleton;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

public class ModelMapperSingleton {
	private static ModelMapper mapper = null;

	private ModelMapperSingleton() {
	}

	static {
		mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
	}

	public static ModelMapper mapper() {	
		return mapper;
	}
}
