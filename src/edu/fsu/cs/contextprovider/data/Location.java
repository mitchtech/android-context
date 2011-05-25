package edu.fsu.cs.contextprovider.data;

import edu.fsu.cs.contextprovider.ContextConstants;
import net.smart_entity.AbstractEntity;
import net.smart_entity.AbstractField;
import net.smart_entity.BelongsTo;
import net.smart_entity.DateField;
import net.smart_entity.DoubleField;
import net.smart_entity.HasMany;
import net.smart_entity.IntegerField;
import net.smart_entity.StringField;
import net.smart_entity.TextField;

public class Location extends AbstractEntity {
	
    public final DateField Timestamp = new DateField(ContextConstants.CONTEXT_TIMESTAMP);
    public final StringField Address = new StringField(ContextConstants.LOCATION_ADDRESS);
    public final StringField Neighborhood = new StringField(ContextConstants.LOCATION_HOOD);
    public final IntegerField Zip = new IntegerField(ContextConstants.LOCATION_ZIP);
    public final DoubleField Latitude = new DoubleField(ContextConstants.LOCATION_LATITUDE);
    public final DoubleField Longitude = new DoubleField(ContextConstants.LOCATION_LONGITUDE);
    public final DoubleField Altitude = new DoubleField(ContextConstants.LOCATION_ALTITUDE);

	@Override
	public AbstractEntity createNewInstance() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BelongsTo[] getBelongsToRelations() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbstractField<?>[] getFields() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HasMany[] getHasManyRelations() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSchemaName() {
		// TODO Auto-generated method stub
		return null;
	}

}
