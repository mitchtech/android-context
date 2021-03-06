package edu.fsu.cs.contextprovider.data;

import net.smart_entity.AbstractEntity;
import net.smart_entity.AbstractField;
import net.smart_entity.BelongsTo;
import net.smart_entity.DateField;
import net.smart_entity.DoubleField;
import net.smart_entity.HasMany;
import net.smart_entity.IntegerField;
import net.smart_entity.StringField;
import net.smart_entity.TextField;

public class WeatherEntity extends AbstractEntity {
	
    public final StringField Condition = new StringField(ContextConstants.WEATHER_CONDITION);
    public final IntegerField Temperature = new IntegerField(ContextConstants.WEATHER_TEMPERATURE);
    public final StringField Humidity = new StringField(ContextConstants.WEATHER_HUMIDITY);
    public final StringField Wind = new StringField(ContextConstants.WEATHER_WIND);
    public final StringField HazardLevel = new StringField(ContextConstants.WEATHER_HAZARD);

	@Override
	public AbstractEntity createNewInstance() {
		// TODO Auto-generated method stub
		return new WeatherEntity();
	}

	@Override
	public BelongsTo[] getBelongsToRelations() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbstractField<?>[] getFields() {
		// TODO Auto-generated method stub
		return new AbstractField<?>[]{ Condition, Temperature, Humidity, Wind, HazardLevel };
	}

	@Override
	public HasMany[] getHasManyRelations() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSchemaName() {
		// TODO Auto-generated method stub
		return "WeatherEntity";
	}

}
