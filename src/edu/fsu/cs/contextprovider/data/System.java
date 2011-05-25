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

public class System extends AbstractEntity {
	
    public final DateField Timestamp = new DateField("Timestamp");
    public final StringField Address = new StringField("Address");
    public final StringField Neighborhood = new StringField("Neighborhood");
    public final IntegerField Zip = new IntegerField("Zip");
    public final DoubleField Latitude = new DoubleField("Latitude");
    public final DoubleField Longitude = new DoubleField("Longitude");
    public final DoubleField Altitude = new DoubleField("Altitude");

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
